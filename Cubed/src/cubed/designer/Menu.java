package cubed.designer;

import Core.Color;
import Core.GameCore;
import Core.Graphics;
import Core.Image;
import Core.InputManager;
import Core.SREObject;
import java.util.ArrayList;

import javax.imageio.ImageIO;

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
                    mask = Image.read(s.getData(image));
                    back = Image.read(s.getData(imBack));
//			mask = (ImageIO.read(s.getData(image)));
//			back = (ImageIO.read(s.getData(imBack)));
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
	public void draw(GameCore gc, Graphics g) {
		if(!hidden){
			g.setColor(Color.RED);
			g.drawImage(back, (int)(x-mx), (int)(y-my));
			for(int index = 0; index<blocks.size();index++){
				blocks.get(index).draw(gc, g);
			}
			g.drawImage(mask, (int)(x-mx), (int)(y-my));
			//g.drawRect((int)x, (int)y, width, height);
			drawPop(g, mouseX, mouseY);
		}
	}

	@Override
	public void update(GameCore gc, InputManager input, int delta) {
		if(!hidden){
			setBlocksPosition();
			Block block;
			for(int i=0; i<blocks.size(); i++){
				block = blocks.get(i);
				if(!block.hidden()){
					if(input.getMouseX()>block.getX()&&input.getMouseX()<block.getX()+blocksize&&
							input.getMouseY()>block.getY()&&input.getMouseY()<block.getY()+blocksize){
						popBlock = block;
						mouseX=input.getMouseX();
						mouseY=input.getMouseY();
						popOn = true;
						if(input.isLMBClicked()){
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
			g.fillRectangle(px, py, 200, 100);
			g.setColor(new Color(250, 250, 250, 255));
			g.drawChars(popBlock.getName().toCharArray(), 0, popBlock.getName().length(), px+10, py+15);
//			g.drawString(popBlock.getName(), px+10, py+15);
			g.drawChars((popBlock.getWeight()+" kg").toCharArray(), 0, (popBlock.getWeight()+" kg").length(), px+10, py+30);
//			g.drawString((popBlock.getWeight()+" kg"), px+10, py+30);
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
