package cubed.states;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;

import cubed.Block;
import cubed.Man;
import cubed.SpaceShip;
import cubed.gui.MouseMenu;

import srengine.BaseState;
import srengine.Camera;
import srengine.Entity;
import srengine.GameContainer;
import srengine.InputManager;
import srengine.utils.Serialiser;

public class TestWorld extends BaseState {

    Serialiser grSer = new Serialiser("../pack.dat");
    Serialiser shipSer = new Serialiser("../ships.dat");
    Serialiser cfgSer = new Serialiser("../conf.dat");
    Man sprite = new Man(100, 100, new String[]{"Rain_01_a.png", "Rain_01_b.png"}, grSer);
    Entity dmg1 = new Entity(200, 232, new String[]{"damage_01_a.png",
        "damage_01_b.png", "damage_01_c.png", "damage_01_d.png"}, grSer);
    Entity dmg2 = new Entity(264, 264, new String[]{"damage_02_a.png",
        "damage_02_b.png", "damage_02_c.png", "damage_02_d.png"}, grSer);
    Block block = new Block(100, 100, "terminal_01.png", grSer);
    SpaceShip ship = new SpaceShip(300, 300);

    public TestWorld(int id) {
        super(id);
        setBackground(Color.BLACK);
    }

    @Override
    protected void init() {
        super.init();
        cameraOn(null, 80000, 80000);
        //Balloon bal = new Balloon(0, 0, "balloon_01.png", grSer);
        sprite.setInfo("Rain", "Human", "21");
        //sprite.setBalloon(bal);
        //sprite.setBallonMessage("Hello world. I am your new avatar in this funky world.");
        ship.fill("GrTest.ship", shipSer, grSer, cfgSer);
        add(ship);
        add(sprite);
//                ship.setCamera(camera);
        ship.join(sprite);
        ship.setHitBoxVisible(false);
//		add(ship);
        //add(block);
        //block.rotate90();
//                setCameraFocusOn(sprite);
//                setCameraBoundary(600, 1000);

        //add(9, sprite);
        //add(100, bal);

    }

    @Override
    protected void draw(Graphics2D g, GameContainer gc) {
        super.draw(g, gc);
        g.setColor(Color.black);
    }

    @Override
    protected void update(InputManager input, GameContainer gc) {
        super.update(input, gc);
        if (input.event) {
            ship.blockAction(input.getPosX() - getCameraXMove(), input.getPosY() - getCameraYMove(), input.MBPressed());
            if (input.isKeyPressed(KeyEvent.VK_A)) {
                ship.setX(ship.getX() - 2f);
            }
            if (input.isKeyPressed(KeyEvent.VK_D)) {
                ship.setX(ship.getX() + 2f);
                ship.setAcceleration(-0.001f);
            }
            if (input.isKeyPressed(KeyEvent.VK_W)) {
//				ship.setY(ship.getY() - 2f);
//                                ship.setVelocity(0.2f, 0);
                ship.setAcceleration(0.001f);
            }
            if (input.isKeyPressed(KeyEvent.VK_S)) {
                //ship.setY(ship.getY() + 2f);
                ship.setAcceleration(-0.001f);
            }

            if (input.isKeyTyped(KeyEvent.VK_Q)) {
                //sprite.rotate(0.1f);
                ship.rotate(0.01f);
            }
            if (input.isKeyTyped(KeyEvent.VK_E)) {
                ship.rotate(-0.01f);
                //sprite.setAngle(sprite.getAngle() - 0.1f);
            }
            if (input.isKeyPressed(KeyEvent.VK_SPACE)) {
                gc.enterState(1);
            }

            if (input.isKeyPressed(KeyEvent.VK_LEFT)) {
                camera.move(-2, 0);
//				sprite.setX(sprite.getX() - 2f);
            }
            if (input.isKeyPressed(KeyEvent.VK_RIGHT)) {
                camera.move(2, 0);
//				sprite.setX(sprite.getX() + 2f);

            }
            if (input.isKeyPressed(KeyEvent.VK_UP)) {
                camera.move(0, -2);
//				sprite.setY(sprite.getY() - 2f);
            }
            if (input.isKeyPressed(KeyEvent.VK_DOWN)) {
                camera.move(0, 2);
//				sprite.setY(sprite.getY() + 2f);
            }
            input.resetEvent();
        }
    }
}
