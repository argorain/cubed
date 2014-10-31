package cubed.states;

import Core.BaseState;
import Core.Color;
import Core.GameCore;
import Core.Graphics;
import Core.InputManager;
import java.awt.Font;
import java.awt.FontFormatException;
import java.io.File;
import java.io.IOException;

import cubed.Client;
import cubed.gui.TextBox;
import srengine.utils.Serialiser;

public class Game extends BaseState {

    Serialiser grSer = new Serialiser("../pack.dat");
    Serialiser shipSer = new Serialiser("../ships.dat");
    Serialiser cfgSer = new Serialiser("../conf.dat");
    TextBox terminal;
    Client client;

    public Game(int id) {
        super(id);
        client = new Client("127.0.0.1", 42424);
    }

    @Override
    protected void init() {
        super.init();
        cameraOn(null, 800, 800);
        Font f = null;
        try {
            f = Font.createFont(Font.TRUETYPE_FONT, new File("../Fonts/8p.TTF")).deriveFont(16f);
        } catch (FontFormatException | IOException e) {
            System.err.println("System font cannot be loaded. Using default.");
        }
        terminal = new TextBox(30, 30, 200, 100, f) {
            @Override
            public void enterEvent(String text) {
                client.push(text);
                client.connect();
            }
        };
    }

    @Override
    protected void draw(GameCore gc, Graphics g) {
        super.draw(gc, g);
        terminal.draw(gc, g);
        g.setColor(Color.BLACK);
    }

    @Override
    protected void update(GameCore gc, InputManager input, int delta) {
        super.update(gc, input, delta);
        terminal.update(gc, input, delta);
//        if (input.event) {
            if (input.isKeyTyped(InputManager.KEY_T )&& input.isKeyTyped(InputManager.KEY_CTRL)) {
                terminal.showHide();
            }
            if (input.isKeyPressed(InputManager.KEY_LEFT)) {
                getCamera().move(-2, 0);
            }
            if (input.isKeyPressed(InputManager.KEY_RIGHT)) {
                getCamera().move(2, 0);
            }
            if (input.isKeyPressed(InputManager.KEY_UP)) {
                getCamera().move(0, -2);
            }
            if (input.isKeyPressed(InputManager.KEY_DOWN)) {
                getCamera().move(0, 2);
            }
//            input.resetEvent();
//        }
    }
}
