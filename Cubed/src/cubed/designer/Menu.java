package cubed.designer;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import srengine.GameContainer;
import srengine.InputManager;
import srengine.SREObject;
import srengine.utils.Serialiser;
import cubed.Block;

public class Menu extends SREObject{

	public int width, height;
	private static int blocksize=32;
	private ArrayList<Block> blocks;
	private ArrayList<String[]> blocksInfo;
	private Image mask;
	private Image back;
	private int scroll = 0;
	private int mx, my;
	private Block popBlock;
	private int mouseX, mouseY;
	private boolean popOn = false;
	private String[] clickBlock;
	private Serialiser s;
	public boolean hidden = true;
	
	public Menu(int width, int height, String image,String imBack, Serialiser s, int mx, int my) {
		super();
		this.width = width;
		this.height = height;
		try {
			mask = (ImageIO.read(s.getData(image)));
			back = (ImageIO.read(s.getData(imBack)));
		} catch (Exception ex) {
			System.err.println("Cannot load animation resource.");
		}
		this.mx = mx;
		this.my = my;
		this.s = s;
	}
	
	public void setPosition(int x, int y){
		super.x = x;
		super.y = y;
		setBlocksPosition();
	}
	
	
	public void setCatalog(ArrayList<String[]> catalog, Serialiser s){
		//blocksInfo = catalog;
		blocks = new ArrayList<>();
		blocksInfo= new ArrayList<>();
		for(int i=0; i<catalog.size(); i++){
			Block bl = new Block(catalog.get(i)[1].split(","), s, catalog.get(i)[2].split(","));
			if(bl.getName()!=null){
				blocks.add(bl);
				blocksInfo.add(catalog.get(i));
			}
		}
		setBlocksPosition();
		clickBlock = blocksInfo.get(0);
	}

	public void setBlockSize(int size){
		blocksize = size;
	}
	@Override
	public void draw(Graphics2D g, GameContainer gc) {
		if(!hidden){
			g.setColor(Color.red);
			g.drawImage(back, (int)(x-mx), (int)(y-my), null);
			for(int index = 0; index<blocks.size();index++){
				blocks.get(index).draw(g, gc);
			}
			g.drawImage(mask, (int)(x-mx), (int)(y-my), null);
			//g.drawRect((int)x, (int)y, width, height);
			drawPop(g, mouseX, mouseY);
		}
	}

	@Override
	public void update(InputManager input, GameContainer gc) {
		if(!hidden){
			setBlocksPosition();
			Block block;
			for(int i=0; i<blocks.size(); i++){
				block = blocks.get(i);
				if(!block.hidden()){
					if(input.getPosX()>block.getX()&&input.getPosX()<block.getX()+blocksize&&
							input.getPosY()>block.getY()&&input.getPosY()<block.getY()+blocksize){
						popBlock = block;
						mouseX=input.getPosX();
						mouseY=input.getPosY();
						popOn = true;
						if(input.isLMB()){
							clickBlock = blocksInfo.get(i);
							showHide();
						}
						break;
					}else{
						popOn = false;
					}
				}
			}
		}
	}
	
	private void setBlocksPosition(){
		int px=(int) x;
		int py=(int) y+scroll;
		for(int i=0; i<blocks.size(); i++){
			blocks.get(i).setX(px);
			blocks.get(i).setY(py);
			px+=(blocksize+2);
			
			if(px>width+x){
				px=(int) x;
				py+=(blocksize+2);
			}
			
			if(py<y||py>y+height){
				blocks.get(i).hide();
			}else{
				blocks.get(i).show();
			}
			
		}
	}

	public void scroll(int rotDirection){
		scroll += (blocksize+2)*rotDirection;
		
	}
	
	private void drawPop(Graphics g, int px, int py){
		if(popOn){
			g.setColor(new Color(92, 210, 72, 140));
			g.fillRect(px, py, 200, 100);
			g.setColor(new Color(250, 250, 250, 255));
			g.drawChars(popBlock.getName().toCharArray(), 0, popBlock.getName().length(), px+10, py+15);
			g.drawChars((popBlock.getWeight()+" kg").toCharArray(), 0, (popBlock.getWeight()+" kg").length(), px+10, py+30);
		}
	}
	
	public String[] clickedBlock(){
		return clickBlock;
	}
	
	public Serialiser getSerialiser(){
		return s;
	}
	
	public void showHide(){
		hidden = !hidden;
	}
}
