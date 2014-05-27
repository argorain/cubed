package cubed.states;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;

import cubed.Client;
import cubed.gui.TextBox;

import srengine.BaseState;
import srengine.GameContainer;
import srengine.InputManager;
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
    protected void draw(Graphics2D g, GameContainer gc) {
        super.draw(g, gc);
        terminal.draw(g, gc);
        g.setColor(Color.black);
    }

    @Override
    protected void update(InputManager input, GameContainer gc) {
        super.update(input, gc);
        terminal.update(input, gc);
        if (input.event) {
            if (input.isKeyTyped(KeyEvent.VK_T) && input.isKeyTyped(KeyEvent.VK_CONTROL)) {
                terminal.showHide();
            }
            if (input.isKeyPressed(KeyEvent.VK_LEFT)) {
                camera.move(-2, 0);
            }
            if (input.isKeyPressed(KeyEvent.VK_RIGHT)) {
                camera.move(2, 0);
            }
            if (input.isKeyPressed(KeyEvent.VK_UP)) {
                camera.move(0, -2);
            }
            if (input.isKeyPressed(KeyEvent.VK_DOWN)) {
                camera.move(0, 2);
            }
            input.resetEvent();
        }
    }
}
