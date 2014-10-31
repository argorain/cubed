package cubed.states;

import Core.BaseState;
import Core.Color;
import Core.Entity;
import Core.GameCore;
import Core.Graphics;
import Core.IHitBox;
import Core.InputManager;
import Core.Rectangle;
import java.awt.Font;
import java.awt.FontFormatException;
import java.io.File;
import java.io.IOException;

import cubed.Block;
import cubed.SpaceShip;

import srengine.utils.Serialiser;

public class TestWorldSit extends BaseState {

    Serialiser s = new Serialiser("../pack.dat");
    Serialiser ships = new Serialiser("../ships.dat");
    Serialiser cfgSer = new Serialiser("../conf.dat");
    
    Entity sprite = new Entity(400, 400, "Sit_01.png", s);
    Block brick12 = new Block(232, 200, "floor_metal_01.png", s);
    Block brick13 = new Block(264, 20, "floor_metal_01.png", s);
    Block brick21 = new Block(200, 232, "floor_metal_01.png", s);
    Block brick22 = new Block(23, 232, "floor_metal_01.png", s);
    Block brick23 = new Block(264, 23, "floor_metal_01.png", s);
    Block brick31 = new Block(200, 264, "floor_metal_01.png", s);
    Block brick32 = new Block(32, 264, "floor_metal_01.png", s);
    Block brick33 = new Block(264, 264, "floor_metal_01.png", s);
    Entity brick41 = new Entity(200, 296, "wall_metal_01.png", s);
    Entity brick42 = new Entity(232, 296, "wall_metal_03.png", s);
    Entity brick43 = new Entity(264, 296, "wall_metal_02.png", s);
    Entity dmg1 = new Entity(0, 0, new String[]{"damage_01_a.png",
        "damage_01_b.png", "damage_01_c.png", "damage_01_d.png"}, s);
    Entity dmg2 = new Entity(264, 264, new String[]{"damage_02_a.png",
        "damage_02_b.png", "damage_02_c.png", "damage_02_d.png"}, s);
    Block brick11 = new Block(200, 200, "floor_metal_01.png", s);
    SpaceShip spaceShip = new SpaceShip(200, 200);
    SpaceShip loader = new SpaceShip(400, 150);
    SpaceShip ship = new SpaceShip(130, 100);

    Font f = null;
    
    public TestWorldSit(int id) {
        super(id);
        setBackground(Color.WHITE);

    }

    @Override
    protected void init(){
        super.init();

        brick11.setDamageBlock(dmg1);
        brick33.setDamageBlock(dmg2);
        spaceShip.add(0, 0, brick11);
        spaceShip.add(1, 0, brick12);
        spaceShip.add(2, 0, brick13);
        spaceShip.add(0, 1, brick21);
        spaceShip.add(1, 1, brick22);
        spaceShip.add(2, 1, brick23);
        spaceShip.add(0, 2, brick31);
        spaceShip.add(1, 2, brick32);
        spaceShip.add(2, 2, brick33);
        
        loader.fill("generic.ship", ships, s, cfgSer);
        ship.fill("gravityTest.ship", ships, s, cfgSer);
        
        sprite.setHitBox(new Rectangle(20,30));
//        sprite.setHitboxAlign(IHitBox.ALIGN_LEFT);
//        sprite.setHitboxValign(IHitBox.VALIGN_BOTTOM);
//        sprite.setClickBox(new Rectangle(32, 40));
        add(9, sprite);

//		add(0, brick11);
//		add(0, brick12);
//		add(0, brick13);
//		add(0, brick21);
//		add(0, brick22);
//		add(0, brick23);
//		add(0, brick31);
//		add(0, brick32);
//		add(0, brick33);
//		add(0, brick41);
//		add(0, brick42);
//		add(0, brick43);
//		add(1, dmg1);
//		add(1, dmg2);
        add(spaceShip);
        add(loader);
        add(ship);
        
        try {
			f = Font.createFont(Font.TRUETYPE_FONT, new File("../Fonts/8p.TTF")).deriveFont(16f);
		} catch (FontFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//f.deriveFont(Font.PLAIN, 22);
    }
    
    @Override
    protected void draw(GameCore gc, Graphics g) {
        super.draw(gc, g);
        g.setColor(Color.BLACK);
//        g.setFont(f);
//        g.drawChars("Font".toCharArray(), 0, 4, 50, 450);
//                dmg1.draw(g, gc);
    }

    @Override
    protected void update(GameCore gc, InputManager input, int delta) {
    	super.update(gc, input, delta);

    	/*if(sprite.isPointInside(input.getPosX(), input.getPosY()))
        	System.out.println("mouse");
        else
        	System.out.println("nop");
    	 */
    	//if(loader.collisionAll(sprite)){
    	//if(loader.collisionSelect(sprite)){
    	//        if(loader.collisionShip(ship)){
    	//        	System.out.println("collision");
    	//        }else{
    	//        	System.out.println("not collide");
    	//        }

//    	if(input.event){
    		loader.blockAction(input.getMouseX(), input.getMouseY(), input.MBPressed());

    		if (input.isKeyPressed(InputManager.KEY_A)) {
    			//			sprite.setX(sprite.getX() - 2f);
    			ship.setX(ship.getX() - 2f);
    		}
    		if (input.isKeyPressed(InputManager.KEY_D)) {
    			//			sprite.setX(sprite.getX() + 2f);
    			ship.setX(ship.getX() + 2f);
    		}
    		if (input.isKeyPressed(InputManager.KEY_W)) {
    			//			sprite.setY(sprite.getY() - 2f);
    			ship.setY(ship.getY() - 2f);
    		}
    		if (input.isKeyPressed(InputManager.KEY_S)) {
    			//			sprite.setY(sprite.getY() + 2f);
    			ship.setY(ship.getY() + 2f);
    		}

    		if (input.isKeyPressed(InputManager.KEY_LEFT)) {
    			//			sprite.setX(sprite.getX() - 2f);
    			spaceShip.setX(spaceShip.getX() - 2f);
    		}
    		if (input.isKeyPressed(InputManager.KEY_RIGHT)) {
    			//			sprite.setX(sprite.getX() + 2f);
    			spaceShip.setX(spaceShip.getX() + 2f);
    		}
    		if (input.isKeyPressed(InputManager.KEY_UP)) {
    			//			sprite.setY(sprite.getY() - 2f);
    			spaceShip.setY(spaceShip.getY() - 2f);
    		}
    		if (input.isKeyPressed(InputManager.KEY_DOWN)) {
    			//			sprite.setY(sprite.getY() + 2f);
    			spaceShip.setY(spaceShip.getY() + 2f);
    		}
    		if (input.isKeyTyped(InputManager.KEY_N)) {
    			brick11.injure(10);
    			brick33.injure(30);
    			System.out.println(brick11.getDamage());
    			//			dmg2.redraw();
    		}

    		if (input.isKeyTyped(InputManager.KEY_Q)) {
    			spaceShip.setAngle(spaceShip.getAngle() + 0.1f);
    			ship.setAngle(ship.getAngle() + 0.1f);
    			sprite.setAngle(sprite.getAngle() + 0.1f);
    		}
    		if (input.isKeyTyped(InputManager.KEY_E)) {
    			spaceShip.setAngle(spaceShip.getAngle() - 0.1f);
    			ship.setAngle(ship.getAngle() - 0.1f);
    			sprite.setAngle(sprite.getAngle() - 0.1f);
    		}

    		if (brick11.getDamage() > 100) {
    			remove(brick11);
    		}

    		//sprite.setX(input.getPosX());
    		//sprite.setY(input.getPosY());
    		//loader.setX(input.getPosX());
    		//loader.setY(input.getPosY());
    		loader.setAngle(loader.getAngle()+(float)(input.getRotDirection()/10.0));

//    		input.resetEvent();
//    	}
    }
}
