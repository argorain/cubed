package cubed.designer;

import java.awt.Color;
import java.awt.Graphics2D;

import srengine.GameContainer;
import srengine.InputManager;
import srengine.SREObject;

public class Overlay extends SREObject{
	boolean show = false;
	private Menu component;
	private Designer state;
	
	public Overlay(Menu menu, Designer state){
		component = menu;
		this.state = state;
	}
	
	@Override
	public void draw(Graphics2D g, GameContainer gc) {
		if(show){
			g.setColor(new Color(0, 0, 0, 180));
			g.fillRect(0, 0, gc.getWindow().getWidth(), gc.getWindow().getHeight());
			component.setPosition(gc.getWindow().getWidth()/2-component.width/2, gc.getWindow().getHeight()/2-component.height/2);
			component.draw(g, gc);
		}
	}

	@Override
	public void update(InputManager input, GameContainer gc) {
		component.update(input, gc);
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
