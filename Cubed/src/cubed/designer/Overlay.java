package cubed.designer;

import Core.Color;
import Core.GameCore;
import Core.Graphics;
import Core.InputManager;
import Core.SREObject;


public class Overlay extends SREObject{
	boolean show = false;
	private Menu component;
	private Designer state;
	
	public Overlay(Menu menu, Designer state){
		component = menu;
		this.state = state;
	}
	
	@Override
	public void draw(GameCore gc, Graphics g) {
		if(show){
			g.setColor(new Color(0, 0, 0, 180));
			g.fillRectangle(0, 0, gc.getWidth(), gc.getHeight());
			component.setPosition(gc.getWidth()/2-component.width/2, gc.getHeight()/2-component.height/2);
			component.draw(gc, g);
		}
	}

	@Override
	public void update(GameCore gc, InputManager input, int delta) {
		component.update(gc, input, delta);
		if(component.hidden&&show){
			hide();
			state.returnFromMenu();
		}
	}
	public void showHide() {
		show=!show;
		component.showHide();
	}
	
	public void hide(){
		show = false;
	}
}
