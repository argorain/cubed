package cubed;

import Core.GameCore;
import java.io.IOException;

import cubed.designer.Designer;
import cubed.states.Game;
import cubed.states.Splash;
import cubed.states.TestWorld;
import srengine.utils.Serialiser;

public class Main extends GameCore {

    public static void main(String[] args) {

        /* JUST FOR CREATION PURPOSES */
        /*System.out.println("*** TESTING ONLY ***");
         Serialiser s1 = new Serialiser("../pack.dat");
         Serialiser s2 = new Serialiser("../ships.dat");
         Serialiser s3 = new Serialiser("../conf.dat");
         try {
         s1.pack("../Graphics/", "png", 'd');
         s2.pack("../Ships/", "ship", 't');
         s3.pack("../cfg/", "cfg", 't');
         } catch (IOException e) {
         System.err.println("Data cannot be packed. Aborting.");
         return;
         }
         /* END: JUST FOR CREATION PURPOSES */
        Main main = new Main(); // singleton ??

//        main.setWindowSize(1024, 600);
        main.setDisplayMode(1024, 600, false, true);
        main.setTitle("Cubed");
        main.setFPS(30);

        main.start();
    }

    @Override
    protected void initStates() {
//        addState(new Splash(0));
//        addState(new TestWorld(2));
        addState(new Designer(1));
        //main.addState(new Game(10));
    }
}
