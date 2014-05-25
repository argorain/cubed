package testGame;

import java.awt.Color;
import java.awt.Graphics2D;
import srengine.BaseState;
import srengine.GameContainer;
import srengine.InputManager;

public class World02 extends BaseState {
    
    public World02(int id) {
        super(id);
    }

    @Override
    protected void init() {
        super.init();
        setBackground(Color.yellow);
    }

    @Override
    protected void draw(Graphics2D g, GameContainer gc) {
        super.draw(g, gc);
    }

    @Override
    protected void update(InputManager input, GameContainer gc) {
        super.update(input, gc);
    }
}
