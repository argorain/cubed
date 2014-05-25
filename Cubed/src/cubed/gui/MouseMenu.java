package cubed.gui;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;

import javax.imageio.ImageIO;

import srengine.GameContainer;
import srengine.InputManager;
import srengine.SREObject;
import srengine.utils.Serialiser;

public class MouseMenu extends SREObject {

	private boolean show = false;
	private int size = 180, px=0, py=0;
	int parts = 3;
	double dimRatio = 0.4;
	int btn = -1;
	boolean event = false;
	Image image;
	
	public MouseMenu(String image, Serialiser s, int buttons) {
		try {
			this.image=ImageIO.read(s.getData(image));
		} catch (Exception ex) {
			System.err.println("Cannot load MouseMenu image resource.\n"+ex.getCause());
		}
		parts = buttons;
	}
	
	@Override
	public void draw(Graphics2D g, GameContainer gc) {
		if(show){
			g.drawImage(image, (int)x-size/2, (int)y-size/2, null);
			/*g.setColor(Color.red);
			g.drawArc((int)x-size/2, (int)y-size/2, size, size, 0, 360);
			g.drawArc((int)(x-(size*dimRatio)/2), (int)(y-(size*dimRatio)/2), (int)(size*dimRatio), (int)(size*dimRatio), 0, 360);
			for(int i=0; i<parts; i++){
				g.drawLine(
						(int)(x+Math.cos(i*(Math.PI*2)/parts)*(size*dimRatio)/2), 
						(int)(y+Math.sin(i*(Math.PI*2)/parts)*(size*dimRatio)/2), 
						(int)(x+Math.cos(i*(Math.PI*2)/parts)*size/2), 
						(int)(y+Math.sin(i*(Math.PI*2)/parts)*size/2));
			}*/
			g.setColor(Color.orange);
			g.drawLine((int)x, (int)y, px, py);
		}
	}

	@Override
	public void update(InputManager input, GameContainer gc) {
		if(input.isRMB()){
			if(show){
				double dy=y-py, dx=x-px;
				double angle = Math.toDegrees(Math.atan((dx)/(dy)))+90;
				angle = (dy)<0 ? angle+180 : angle;
				double length = Math.sqrt(dx*dx+dy*dy);
				int zone = (int) (angle/(360/parts));
				if(length<=size/2&&length>=(size*dimRatio)/2){
//					System.out.println(zone+" "+length+" "+angle);
					btn = zone;
					event = true;
				}
			}
			x=input.getPosX();
			y=input.getPosY();
			showHide();
		}
		if(show){
			px=input.getPosX();
			py=input.getPosY();
		}
	}

	public void showHide() {
		show = !show;
	}

	public int getButton(){
		return btn;
	}
	
	public boolean isEvent(){
		boolean ev = event;
		event = false;
		return ev;
	}
}
