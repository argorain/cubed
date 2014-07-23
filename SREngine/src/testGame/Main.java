package testGame;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_TEST;
import static org.lwjgl.opengl.GL11.GL_LIGHTING;
import static org.lwjgl.opengl.GL11.GL_MODELVIEW;
import static org.lwjgl.opengl.GL11.GL_PROJECTION;
import static org.lwjgl.opengl.GL11.GL_QUADS;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glClearColor;
import static org.lwjgl.opengl.GL11.glColor3f;
import static org.lwjgl.opengl.GL11.glDisable;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glLoadIdentity;
import static org.lwjgl.opengl.GL11.glMatrixMode;
import static org.lwjgl.opengl.GL11.glPushMatrix;
import static org.lwjgl.opengl.GL11.glRotatef;
import static org.lwjgl.opengl.GL11.glTexCoord2f;
import static org.lwjgl.opengl.GL11.glTranslatef;
import static org.lwjgl.opengl.GL11.glVertex2f;
import static org.lwjgl.opengl.GL11.glViewport;
import static org.lwjgl.util.glu.GLU.gluOrtho2D;
import srengine.GameContainer;
import srengine.utils.Serialiser;
import static test.Main.DISPLAY_HEIGHT;
import static test.Main.DISPLAY_WIDTH;

public class Main extends GameContainer {

    public static void main(String[] args) {
        /*Serialiser s = new Serialiser("../pack.dat");
         try {
         s.pack("../Graphics/", "png", 'd');
         } catch (IOException e) {
         System.err.println("Data cannot be packed. Aborting.");
         return;
         }*/

        Main main = new Main(); // singleton ??

        main.setWindowSize(800, 600);
        main.setTitle("Cubed");
        //main.setFPS(30);
//        try {
//            main.create();
//        } catch (LWJGLException ex) {
//            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
//        }
            //main.run();

        //main.addState(new World01(1));
        main.addState(new World02(2));

        main.start();

    }

//    public void run() {
//        while (!Display.isCloseRequested() && !Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)) {
//            if (Display.isVisible()) {
//                processKeyboard();
//                processMouse();
//                update();
//                render();
//            } else {
//                if (Display.isDirty()) {
//                    render();
//                }
//                try {
//                    Thread.sleep(100);
//                } catch (InterruptedException ex) {
//                }
//            }
//            Display.update();
//            Display.sync(60);
//        }
//    }
//
//    public void update() {
//        /*if (squareSize < 5) {
//         squareSize = 5;
//         } else if (squareSize >= DISPLAY_HEIGHT) {
//         squareSize = DISPLAY_HEIGHT;
//         }*/
//    }
//
//    public void processKeyboard() {
//        //Square's Size
//        /*if (Keyboard.isKeyDown(Keyboard.KEY_DOWN)) {
//         --squareSize;
//         }
//         if (Keyboard.isKeyDown(Keyboard.KEY_UP)) {
//         ++squareSize;
//         }
//
//         //Square's Z
//         if (Keyboard.isKeyDown(Keyboard.KEY_LEFT)) {
//         ++squareZ;
//         }
//         if (Keyboard.isKeyDown(Keyboard.KEY_RIGHT)) {
//         --squareZ;
//         }*/
//    }
//
//    public void processMouse() {
//        /*squareX = Mouse.getX();
//         squareY = Mouse.getY();*/
//    }
//
//    public void destroy() {
//        //Methods already check if created before destroying.
//        Mouse.destroy();
//        Keyboard.destroy();
//        Display.destroy();
//    }
//
//    public void render() {
//        glClear(GL_COLOR_BUFFER_BIT);
//        glLoadIdentity();
//
//        //Draw a basic square
////    glTranslatef(squareX,squareY,0.0f);
////    glRotatef(squareZ,0.0f,0.0f,1.0f);
////    glTranslatef(-(squareSize >> 1),-(squareSize >> 1),0.0f);
//        glColor3f(0.0f, 0.5f, 0.5f);
//        glBegin(GL_QUADS);
////      glTexCoord2f(0.0f,0.0f); glVertex2f(0.0f,0.0f);
////      glTexCoord2f(1.0f,0.0f); glVertex2f(squareSize,0.0f);
////      glTexCoord2f(1.0f,1.0f); glVertex2f(squareSize,squareSize);
////      glTexCoord2f(0.0f,1.0f); glVertex2f(0.0f,squareSize);
//        glEnd();
//    }
}
