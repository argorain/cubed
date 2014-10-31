package cubed.gui;

import Core.Color;
import Core.GameCore;
import Core.Graphics;
import Core.Image;
import Core.InputManager;
import Core.SREObject;

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
			this.image=Image.read(s.getData(image));
		} catch (Exception ex) {
			System.err.println("Cannot load MouseMenu image resource.\n"+ex.getCause());
		}
		parts = buttons;
	}
	
	@Override
	public void draw(GameCore gc, Graphics g) {
		if(show){
			g.drawImage(image, (int)x-size/2, (int)y-size/2);
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
			g.setColor(Color.ORANGE);
			g.drawLine((int)x, (int)y, px, py);
		}
	}

	@Override
	public void update(GameCore gc, InputManager input, int delta) {
		if(input.isRMBClicked()){
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
			x=input.getMouseX();
			y=input.getMouseY();
			showHide();
		}
		if(show){
			px=input.getMouseX();
			py=input.getMouseY();
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
