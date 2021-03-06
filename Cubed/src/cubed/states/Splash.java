package cubed.states;

import Core.BaseState;
import Core.Color;
import Core.GameCore;
import Core.Graphics;
import Core.Image;
import Core.InputManager;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import javax.imageio.ImageIO;

import srengine.utils.Serialiser;

public class Splash extends BaseState {

    Serialiser s = new Serialiser("../pack.dat");
    Image logo;
    Color shade;
    int alpha = 255;
    static final int crossShade = 3;
    //static final int SHOW = 100;
    static final int BLANK = 50;
    static String[] shows = {"sre.png", "wash.png", "Rain_01.png"};
    static int[] showTime = {60, 400, 100};
    int time = 0;
    int show = 0;
    int phase = 0;
    static int nextState = 2;

    public Splash(int id) {
        super(id);
        setBackground(Color.BLACK);

    }

    public void setNextState(int next) {
        nextState = next;
    }

    public void setPics(String[] pics) {
        shows = pics;
    }

    @Override
    protected void init() {
        super.init();
        try {
            logo = Image.read(s.getData(shows[0]));
        } catch (IOException e) {
            System.err.print("Error during loading " + shows[0] + " logo.\n" + e.getCause());
        }
        shade = new Color(0, 0, 0, 255);
        Timer action = new Timer(); //background action
        action.schedule(new TimerTask() {
            @Override
            public void run() {
                for (int i = 0; i < 100000; i++) {
                    System.out.println(i);
                }
            }
        }, 1000);
    }

    @Override
    protected void draw(GameCore gc, Graphics g) {
        super.draw(gc, g);
        shade = new Color(0, 0, 0, alpha);
        g.drawImage(logo, gc.getWidth() / 2 - logo.getWidth() / 2, gc.getHeight() / 2 - logo.getHeight() / 2);
        g.setColor(shade);
        g.fillRectangle(0, 0, gc.getWidth(), gc.getHeight());
    }

    @Override
    protected void update(GameCore gc, InputManager input, int delta) {
        super.update(gc, input, delta);
        time++;
        switch (phase) {
            case 0: //rise			
                if (time > 255 / crossShade) {
                    time = 0;
                    phase++;
                    break;
                }
                alpha -= crossShade;
                break;
            case 1: //show
                if (time > showTime[show]) {
                    time = 0;
                    phase++;
                }
                break;
            case 2: //fall
                if (time > 255 / crossShade) {
                    time = 0;
                    phase++;
                    break;
                }
                alpha += crossShade;
                break;
            case 3: //blank
                if (time > BLANK) {
                    time = 0;
                    phase = 0;
                    show++;
                    if (show < shows.length) {
                        try {
                            logo = Image.read(s.getData(shows[show]));
                        } catch (IOException e) {
                            System.err.print("Error during loading " + shows[show] + " logo.\n" + e.getCause());
                        }
                    } else {
                        gc.enterState(2); //enter next
                    }
                }
                break;

        }
    }
}
