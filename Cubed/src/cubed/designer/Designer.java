package cubed.designer;

import Core.BaseState;
import Core.Color;
import Core.GameCore;
import Core.Graphics;
import Core.InputManager;
import java.awt.Font;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Arrays;

import srengine.utils.Serialiser;
import cubed.Block;
import cubed.Config;
import cubed.gui.TextBox;

public class Designer extends BaseState{

	Serialiser grSer = new Serialiser("../pack.dat");
	Config cfg = new Config("blocks.cfg", new Serialiser("../conf.dat"));
	ArrayList<String[]> blockCatalog = cfg.getMap();
	ArrayList<int[]> occupiedPosition = new ArrayList<>();
	boolean menuActive = false;
	boolean textActive = false;
	boolean grid = true;
	
	private Block flyingblock;
	private static final int BLOCKSIZE = 32;
	private Menu menu = new Menu(200, 360, "designer_block_menu.png","designer_block_menu_bg.png", grSer, 20, 40);
	private Overlay overlay;
	private int rot = 0;
	private TextBox tb;
	private boolean savePrompt = false;
	private static final int AREAX = 10000, AREAY = 10000;
	
	public Designer(int id) {
		super(id);
		setBackground(Color.BLACK);
	}
	
	@Override
	protected void enter() {
		super.enter();
		Font f = null;
		try {
			f = Font.createFont(Font.TRUETYPE_FONT, new File("../Fonts/8p.TTF")).deriveFont(16f);
		} catch (Exception e) {
			System.err.println("System font cannot be loaded. Using default.");
		}
		tb = new TextBox(30, 30, 200, 100, f){
			@Override
			public void enterEvent(String text) {
				// TODO Auto-generated method stub
				
			}
		};
		menu.setCatalog(blockCatalog, grSer);
		overlay = new Overlay(menu, this);
		flyingblock = new Block(menu.clickedBlock()[1].split(","), menu.getSerialiser(), menu.clickedBlock()[2].split(","));
		tb.setBackgroundColor(new Color(0, 50, 0, 150));
		cameraOn(null, AREAX, AREAY);
		cameraMove(AREAX/2, AREAY/2);
	}
	
	@Override
	protected void draw(GameCore gc, Graphics g) {
		super.draw(gc, g);
		if(grid){
			g.setColor(Color.DARK_GRAY);
			for(int i=0; i<AREAX;i+=BLOCKSIZE){
				g.drawLine(i+(int)getCamera().getXMove(), 0, i+(int)getCamera().getXMove(), gc.getHeight());
			}
			for(int i=0; i<AREAY;i+=BLOCKSIZE){
				g.drawLine(0, i+(int)getCamera().getYMove(), gc.getWidth(), i+(int)getCamera().getYMove());
			}
		}
		tb.draw(gc, g);
		overlay.draw(gc, g); 
//		flyingblock.staticRotate(rot);
		flyingblock.draw(gc, g);
	}
	
	@Override
	protected void update(GameCore gc, InputManager input, int delta) {
		super.update(gc, input, delta);
		overlay.update(gc, input, delta);
		tb.update(gc, input, delta);
		if(input.isLMBClicked()&&!menuActive){
			Point point = getRelPoint(input.getMousePos());	
			if(free(point)){
				flyingblock = new Block(menu.clickedBlock()[1].split(","), menu.getSerialiser(), menu.clickedBlock()[2].split(","));
				Block ent = new Block(menu.clickedBlock()[1].split(","), menu.getSerialiser(), menu.clickedBlock()[2].split(","));
				System.out.println(menu.clickedBlock()[1].split(",")[0]);
				ent.setX(point.x*BLOCKSIZE);
				ent.setY(point.y*BLOCKSIZE);
				ent.setRelative(point.x, point.y);
//				ent.staticRotate(rot);  
				super.add(ent);
				setIndex((int)ent.getX(), (int)ent.getY());
			}
			
		}
		if(input.isRMBClicked()&&!menuActive){
			Point point = getRelPoint(input.getMousePos());
			super.remove(pullOut(point));
		}
		if(input.isKeyTyped(InputManager.KEY_E)&&!textActive){
			menuActive=!menuActive;
			overlay.showHide();
		}
		if(menuActive){
//			menu.scroll(input.getRotDirection());
		}
		if(input.isKeyTyped(InputManager.KEY_G)&&!menuActive&&!textActive){
			grid = !grid;
		}
		if(input.isKeyTyped(InputManager.KEY_R)&&!menuActive&&!textActive){
			rot +=90;
			if(rot>270){
				rot = 0;
			}
			System.out.println("rot");
		}
		if(input.isKeyTyped(InputManager.KEY_T)&&!menuActive&&input.isKeyTyped(InputManager.KEY_CTRL)){
			tb.showHide();
			textActive = !textActive;
		}
		if(input.isKeyTyped(InputManager.KEY_S)&&!menuActive&&input.isKeyTyped(InputManager.KEY_CTRL)){
			tb.setPrompt("Ship name: ");
			tb.showHide();
			textActive = !textActive;
			savePrompt = true;
		}
		if(menuActive){
			flyingblock.hide();
		}else{
			flyingblock.show();
		}
		if(savePrompt){
			String line;
			if((line=tb.getLine(0))!=null){
				System.out.println(line);
				savePrompt = false;
				tb.setPrompt("");
				
				if(saveShip(line.split(": ")[1])){
					tb.putLine("Ship saved.");
				}else{
					tb.putLine("An error occured during ship save.");
				}
			}
		}
		flyingblock.setX(input.getMouseX());
		flyingblock.setY(input.getMouseY());
		if (input.isKeyPressed(InputManager.KEY_LEFT)) {
			getCamera().move(-0.3f*delta, 0);
		}
		if (input.isKeyPressed(InputManager.KEY_RIGHT)) {
			getCamera().move(0.3f*delta, 0);
		}
		if (input.isKeyPressed(InputManager.KEY_UP)) {
			getCamera().move(0, -0.3f*delta);
		}
		if (input.isKeyPressed(InputManager.KEY_DOWN)) {
			getCamera().move(0, 0.3f*delta);
		}
	}
	
	private boolean free(Point mouse){
		for(int i=0; i<super.objectArraySize(); i++){
			Block bl = (Block)(super.get(i));
			if(bl.getRelX()==mouse.x&&bl.getRelY()==mouse.y){
				if((bl.getLayer()==1&&(flyingblock.getLayer()>=5||flyingblock.getLayer()<=1))||bl.getLayer()>=5||fullSlot(bl))	
					return false;
			}
		}
		return true;
	}
	
	private boolean fullSlot(Block block){
		for(int i=0; i<occupiedPosition.size(); i++){
			if(block.getRelX()==occupiedPosition.get(i)[0]&&block.getRelY()==occupiedPosition.get(i)[1]){
				if(occupiedPosition.get(i)[2]>=2){
					return true;
				}
			}
		}
		return false;
	}
	
	private void setIndex(int x, int y){
		x=x/BLOCKSIZE;
		y=y/BLOCKSIZE;
		int[] arr;
		boolean match = false;
		for(int i=0; i<occupiedPosition.size(); i++){
			if(occupiedPosition.get(i)[0]==x&&occupiedPosition.get(i)[1]==y){
				arr = occupiedPosition.get(i);
				occupiedPosition.remove(i);
				arr[2]++;
				occupiedPosition.add(arr);
				match = true;
				System.out.println(Arrays.toString(arr));
				break;
			}
		}
		if(!match){
			int[]  arr2 = {x, y, 1};
			occupiedPosition.add(arr2);
			System.out.println(Arrays.toString(arr2));
		}
	}
	
	private Block pullOut(Point mouse){
		for(int i=super.objectArraySize()-1; i>=0; i--){
			Block bl = (Block)(super.get(i));
			if(bl.getRelX()==mouse.x&&bl.getRelY()==mouse.y)
				return bl;
		}
		return null;
	}
	
	private Point getRelPoint(Point mouse){
		return new Point((int)(mouse.x-getCamera().getXMove())/BLOCKSIZE, (int)(mouse.y-getCamera().getYMove())/BLOCKSIZE);
	}
	
	public void returnFromMenu(){
		menuActive = false;
		flyingblock = new Block(menu.clickedBlock()[1].split(","), menu.getSerialiser(), menu.clickedBlock()[2].split(","));
		tb.setPrompt("");
	}
	
	private boolean saveShip(String shipname){
		shipname.trim();
		String eol = System.getProperty("line.separator");
		shipname=shipname.substring(0, shipname.length()-eol.length());
		File f = new File("../Designs/"+shipname+this.hashCode()+".ship");
		BufferedWriter w;
		try {
			w = new BufferedWriter(new FileWriter(f));
		} catch (IOException e) {
			System.err.println("Cannot create file. Aborting. \n"+e.getCause());
			return false;
		}
		try {
			w.write("[info]"+eol);
			w.write("name="+shipname+eol);
			w.write("class=design"+eol+eol);
			w.write("[map]"+eol);
		} catch (IOException e1) {
			System.err.println(e1.getCause());
		}

		
		try {
			w.close();
		} catch (IOException e) {
			System.err.println(e.getCause());
		}
		return true;
	}

}
