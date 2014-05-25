package cubed;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;

import srengine.BaseState;
import srengine.Entity;
import srengine.GameContainer;
import srengine.InputManager;

public class Space extends BaseState {

	Entity sprite = new Entity(100, 100, "../Graphics/people/Rain_01.png");

	public Space(int id) {
		super(id);
		setBackground(Color.WHITE);
	}

	@Override
	protected void init() {
		super.init();
		add(9, sprite);
	}

	@Override
	protected void draw(Graphics2D g, GameContainer gc) {
		super.draw(g, gc);
		g.setColor(Color.black);
	}

	@Override
	protected void update(InputManager input, GameContainer gc) {
		super.update(input, gc);
		if (input.isKeyPressed(KeyEvent.VK_LEFT)) {
			sprite.setX(sprite.getX() - 2f);
		}
		if (input.isKeyPressed(KeyEvent.VK_RIGHT)) {
			sprite.setX(sprite.getX() + 2f);
		}
		if (input.isKeyPressed(KeyEvent.VK_UP)) {
			sprite.setY(sprite.getY() - 2f);
		}
		if (input.isKeyPressed(KeyEvent.VK_DOWN)) {
			sprite.setY(sprite.getY() + 2f);
		}
	}
}
