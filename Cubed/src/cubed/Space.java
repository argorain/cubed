package cubed;

import Core.BaseState;
import Core.Color;
import Core.Entity;
import Core.GameCore;
import Core.Graphics;
import Core.InputManager;


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
	protected void draw(GameCore gc, Graphics g) {
		super.draw(gc, g);
		g.setColor(Color.BLACK);
	}

	@Override
	protected void update(GameCore gc, InputManager input, int delta) {
		super.update(gc, input, delta);
		if (input.isKeyPressed(InputManager.KEY_LEFT)) {
			sprite.setX(sprite.getX() - 2f);
		}
		if (input.isKeyPressed(InputManager.KEY_RIGHT)) {
			sprite.setX(sprite.getX() + 2f);
		}
		if (input.isKeyPressed(InputManager.KEY_UP)) {
			sprite.setY(sprite.getY() - 2f);
		}
		if (input.isKeyPressed(InputManager.KEY_DOWN)) {
			sprite.setY(sprite.getY() + 2f);
		}
	}
}
