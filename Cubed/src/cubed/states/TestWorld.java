package cubed.states;

import Core.BaseState;
import Core.Color;
import Core.Entity;
import Core.GameCore;
import Core.Graphics;
import Core.InputManager;

import cubed.Block;
import cubed.Man;
import cubed.SpaceShip;
import cubed.gui.MouseMenu;

import srengine.utils.Serialiser;

public class TestWorld extends BaseState {

    Serialiser grSer = new Serialiser("../pack.dat");
    Serialiser shipSer = new Serialiser("../ships.dat");
    Serialiser cfgSer = new Serialiser("../conf.dat");
    Man sprite = new Man(100, 100, new String[]{"Rain_01_a.png", "Rain_01_b.png"}, grSer);
    Man sit = new Man(100, 100, new String[]{"Sit_01_a.png", "Sit_01_b.png"}, grSer);
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
        sit.setInfo("Jara", "human", "20");
        //sprite.setPeriod(300);
        //sprite.setBalloon(bal);
        //sprite.setBallonMessage("Hello world. I am your new avatar in this funky world.");
        ship.fill("GrTest.ship", shipSer, grSer, cfgSer);
        add(ship);
        //add(sprite);
//                ship.setCamera(camera);
        ship.join(sprite);
        //ship.join(sit);
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
    protected void draw(GameCore gc, Graphics g) {
        super.draw(gc, g);
        g.setColor(Color.BLACK);
    }

    @Override
    protected void update(GameCore gc, InputManager input, int delta) {
        super.update(gc, input, delta);
//        if (input.event) {
            ship.blockAction(input.getMouseX() - getCamera().getXMove(), input.getMouseY() - getCamera().getYMove(), input.MBPressed());
            if (input.isKeyPressed(InputManager.KEY_A)) {
                ship.setX(ship.getX() - 0.5f*delta);
            }
            if (input.isKeyPressed(InputManager.KEY_D)) {
                ship.setX(ship.getX() + 0.5f*delta);
//                ship.setAcceleration(-0.001f);
            }
            if (input.isKeyPressed(InputManager.KEY_W)) {
//				ship.setY(ship.getY() - 2f);
//                                ship.setVelocity(0.2f, 0);
                ship.setAcceleration(0.001f);
            }
            if (input.isKeyPressed(InputManager.KEY_S)) {
                //ship.setY(ship.getY() + 2f);
                ship.setAcceleration(-0.001f);
            }

            if (input.isKeyTyped(InputManager.KEY_Q)) {
                //sprite.rotate(0.1f);
                ship.rotate(0.01f*delta);
            }
            if (input.isKeyTyped(InputManager.KEY_E)) {
                ship.rotate(-0.01f*delta);
                //sprite.setAngle(sprite.getAngle() - 0.1f);
            }
            if (input.isKeyPressed(InputManager.KEY_SPACE)) {
                gc.enterState(1);
            }

            if (input.isKeyPressed(InputManager.KEY_LEFT)) {
                getCamera().move(-2, 0);
//				sprite.setX(sprite.getX() - 2f);
            }
            if (input.isKeyPressed(InputManager.KEY_RIGHT)) {
                getCamera().move(2, 0);
//				sprite.setX(sprite.getX() + 2f);

            }
            if (input.isKeyPressed(InputManager.KEY_UP)) {
                getCamera().move(0, -2);
//				sprite.setY(sprite.getY() - 2f);
            }
            if (input.isKeyPressed(InputManager.KEY_DOWN)) {
                getCamera().move(0, 2);
//				sprite.setY(sprite.getY() + 2f);
            }
//            input.resetEvent();
//        }
    }
}
