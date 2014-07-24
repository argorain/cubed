package testGame;

import org.lwjgl.LWJGLException;
import srengine.GameContainer;

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
        try {
            main.create();
            //main.setFPS(30);
        } catch (LWJGLException ex) {
            System.err.println(ex.getCause());
        }

        //main.addState(new World01(1));
        main.addState(new World02(2));

        main.start();

    }

}
