/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package srengine;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import static org.lwjgl.opengl.GL11.GL_DEPTH_TEST;
import static org.lwjgl.opengl.GL11.GL_LIGHTING;
import static org.lwjgl.opengl.GL11.GL_MODELVIEW;
import static org.lwjgl.opengl.GL11.GL_PROJECTION;
import static org.lwjgl.opengl.GL11.glClearColor;
import static org.lwjgl.opengl.GL11.glDisable;
import static org.lwjgl.opengl.GL11.glLoadIdentity;
import static org.lwjgl.opengl.GL11.glMatrixMode;
import static org.lwjgl.opengl.GL11.glPushMatrix;
import static org.lwjgl.opengl.GL11.glViewport;
import static org.lwjgl.util.glu.GLU.gluOrtho2D;

/**
 *
 * @author rain
 */
public class GameContainer {

    private static final float VERSION = 1.1f;
    private static final String SIT = "Jaroslav 'Sit' Schmidt";
    private static final String RAIN = "Vojtech 'Rain' Vladyka";
    private static final int YEAR = 2013;
    //window settings
    private static String TITLE = "SRE App";
    private static int DISPLAY_WIDTH = 800, DISPLAY_HEIGHT = 600;
    private static boolean FULLSCREEN = false;
    //states
    private ArrayList<BaseState> states = new ArrayList<>();
    private Graphics2D  bbg;
    private GameContainer gc = this;
    private BufferedImage buffer;
    private AffineTransform transform;
    private InputManager input;

    protected void create() throws LWJGLException {
        //Display
        Display.setDisplayMode(new DisplayMode(DISPLAY_WIDTH, DISPLAY_HEIGHT));
        Display.setFullscreen(FULLSCREEN);
        Display.setTitle(TITLE);
        Display.create();

        //Keyboard
        Keyboard.create();

        //Mouse
        Mouse.setGrabbed(false);
        Mouse.create();

        //OpenGL
        initGL();
        resizeGL();
    }

    /**
     * Start method. This start whole mechanism
     */
    protected void start() {
        System.out.println("SREngine v" + VERSION + " created by " + SIT
                + " & " + RAIN + " at " + YEAR);
//        window.showWindow();   
        buffer = new BufferedImage(Display.getWidth(), Display.getHeight(), BufferedImage.TYPE_INT_RGB);
        //g = (Graphics2D) Display.getParent().getGraphics();
        bbg = (Graphics2D) buffer.getGraphics();
        transform = bbg.getTransform();

        run(states.get(0));
    }

    private void run(final BaseState state) {
        state.enter();

        while (!Display.isCloseRequested() && !Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)) {
            if (Display.isVisible()) {
//                processKeyboard();
//                processMouse();
                state.update(input, gc);
                state.draw(bbg, gc);
            } else {
                if (Display.isDirty()) {
                    bbg.setColor(state.getBackgroundColor());
                    bbg.fillRect(0, 0, Display.getWidth(), Display.getHeight());
                    state.draw(bbg, gc);
                    //g.drawImage(buffer, 0, 0, null);
                }
                try {
                    Thread.sleep(100);
                } catch (InterruptedException ex) {
                }
            }
            Display.update();
            Display.sync(60);
        }
    }

//    public void processKeyboard() {
//        //Square's Size
//        if (Keyboard.isKeyDown(Keyboard.KEY_DOWN)) {
//            --squareSize;
//        }
//        if (Keyboard.isKeyDown(Keyboard.KEY_UP)) {
//            ++squareSize;
//        }
//
//        //Square's Z
//        if (Keyboard.isKeyDown(Keyboard.KEY_LEFT)) {
//            ++squareZ;
//        }
//        if (Keyboard.isKeyDown(Keyboard.KEY_RIGHT)) {
//            --squareZ;
//        }
//    }
//
//    public void processMouse() {
//        squareX = Mouse.getX();
//        squareY = Mouse.getY();
//    }

    //*** OPEN GL ***
    public void initGL() {
        //2D Initialization
        glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        glDisable(GL_DEPTH_TEST);
        glDisable(GL_LIGHTING);
    }

    public void resizeGL() {
        //2D Scene
        glViewport(0, 0, DISPLAY_WIDTH, DISPLAY_HEIGHT);

        glMatrixMode(GL_PROJECTION);
        glLoadIdentity();
        gluOrtho2D(0.0f, DISPLAY_WIDTH, 0.0f, DISPLAY_HEIGHT);
        glPushMatrix();

        glMatrixMode(GL_MODELVIEW);
        glLoadIdentity();
        glPushMatrix();
    }

    //*** STATES ***
    /**
     * Add state to list of states
     *
     * @param state This state will be added
     */
    protected void addState(BaseState state) {
        states.add(state);
        state.init();
    }

    /**
     * Enter to state with specific id
     *
     * @param id Id of state
     */
    public void enterState(int id) {
        for (int i = 0; i < states.size(); i++) {
            if (states.get(i).getId() == id) {
                run(states.get(i));
            }
        }
    }

    /**
     * State getter
     *
     * @param id Id of state
     * @return <code>BaseState</code> with this id
     */
    public BaseState getState(int id) {
        for (int i = 0; i < states.size(); i++) {
            if (states.get(i).getId() == id) {
                return states.get(i);
            }
        }
        return null;
    }

    //*** GETTERS & SETTERS ***
    public void setWindowSize(int width, int height) {
        DISPLAY_WIDTH = width;
        DISPLAY_HEIGHT = height;
    }

    public void setTitle(String title) {
        TITLE = title;
    }
    
    public int getWindowWidth(){
        return Display.getWidth();
    }
    
    public int getWindowHeight(){
        return Display.getHeight();
    }
}
