package cubed;

import Core.Color;
import Core.Entity;
import Core.GameCore;
import Core.Graphics;
import Core.InputManager;
import java.util.ArrayList;

import srengine.utils.Serialiser;

public class Block extends Entity {

    private int damage = 0;
    private int damageTreshold = 100;
    private Entity damageBlock;
    private int weight = 10;
    private String state = "";
    private int layer = 0;
    private int relX, relY;
    private boolean hitboxAllowed = false;
    private boolean clickedLMB = false;
    private boolean cargoOnSpot = false;
    private boolean menOnSpot = false;
    private boolean engine = false;
    private boolean blast = false;
    private String name;
    private boolean colored;
    private Color color;
    private boolean locked=false;

    public Block(String[] images, Serialiser s) {
        super(0, 0, images, s);
    }

    public Block(String image, Serialiser s) {
        super(0, 0, new String[]{image}, s);
    }

    public Block(String image[], Serialiser s, String[] params) {
        super(0, 0, image, s);
        setParams(params, s);
    }

//    public Block(float x, float y, ArrayList<String> images) {
//        super(x, y, images);
//    }

    public Block(float x, float y, String[] images, Serialiser s) {
        super(x, y, images, s);
    }

    public Block(float x, float y, String image, Serialiser s) {
        super(x, y, image, s);
    }

    public void cargoOnSpot() {
        cargoOnSpot = true;
    }

    public boolean isCargoOnSpot() {
        return cargoOnSpot;
    }

    public int getLayer() {
        return layer;
    }

    public void setRelative(int x, int y) {
        relX = x;
        relY = y;
    }

    public int getRelX() {
        return relX;
    }

    public int getRelY() {
        return relY;
    }

    public void allowHitbox() {
        hitboxAllowed = true;
    }

    public void denyHitbox() {
        hitboxAllowed = false;
    }

    public boolean hitboxAllowed() {
        return hitboxAllowed;
    }

    public String getName() {
        return name;
    }

    private void setParams(String[] params, Serialiser s) {
        for (int i = 0; i < params.length; i++) {
            switch (params[i].split("=")[0]) {
                case "anim":
                    startAnimate(Integer.parseInt(params[i].split("=")[1]));
//                    run();
                    break;
                case "doors":
                    state = "doors";
                    break;
                case "l":
                    layer = Integer.parseInt(params[i].split("=")[1]);
                    break;
                case "c":
                    hitboxAllowed = true;
                    break;
                case "w":
                    weight = Integer.parseInt(params[i].split("=")[1]);
                    break;
                case "r":
                    switch (Integer.parseInt(params[i].split("=")[1])) {
                        case 90:
//    					setDrawAngle(90);
//                            staticRotate(90);
                            break;
                        case 180:
//    					setDrawAngle(180);
//                            staticRotate(180);
                            break;
                        case 270:
//    					setDrawAngle(270);
//                            staticRotate(270);
                            break;
                        default:
                            break;
                    }
                    break;
                case "e":
                    engine = true;
                    break;
                case "b":
                    state = "blast";
                    blast = true;
                    hide();
                    setPeriod(Integer.parseInt(params[i].split("=")[1]));
                    break;
                case "name":
                    name = params[i].split("=")[1];
                    break;
                default:
                    break;
            }

        }
    }

    public void setDamageBlock(Entity damageBlock) {
        this.damageBlock = damageBlock;
    }

    public void setDamage(int damage) {
        this.damage = damage;
    }

    public int getDamage() {
        return damage;
    }

    public void injure(int injury) {
        this.damage += injury;
    }

    public void setDamageMax(int value) {
        damageTreshold = value;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public int getWeight() {
        return weight;
    }

    public void setLoc(float x, float y) {
        super.x = x;
        super.y = y;
    }

    public String getState() {
        return state;
    }

    public boolean clickedLMB() {
        boolean clicked = this.clickedLMB;
        this.clickedLMB = false;
        return clicked;
    }

    public void action(float x, float y, int actionSource) {
        if (this.isPointInside((int) x, (int) y)) {
            switch (actionSource) {
                case InputManager.MOUSE_BUTTON1:
                    System.out.println("LMB");
                    System.out.println(this.relX + "," + this.relY);
                    clickedLMB = true;
                    switch (state) {
                        case "doors":
                            openDoor();
                            System.out.println("door action");
                            break;
                        default:
                            break;
                    }
                    // colorUp(new Color(0, 255, 0, 70));
                    break;
                case InputManager.MOUSE_BUTTON2:
                    System.out.println("RMB");
                    switch (state) {
                        case "blast":
                            hideShow();
                            startAnimate();
                            System.out.println("blast");
                            break;
                        case "doors":
                            if(locked) unlockDoor();
                            else lockDoor();
                            break;
                        default:
                            break;
                    }
                    break;
                case InputManager.MOUSE_BUTTON3:
                    System.out.println("MMB");
                    switch (state) {
                        default:
                            break;
                    }
                    break;
                default:
                    break;
            }
        }
    }

    public void colorUp(Color color) {
        this.color = color;
        colored = true;
    }

    public void uncolor() {
        colored = false;
    }

    @Override
    public void update(GameCore gc, InputManager input, int delta) {
        super.update(gc, input, delta);
        if (damageBlock != null) {
            damageBlock.setX(x);
            damageBlock.setY(y);
            damageBlock.setAngle(angle);
            damageBlock.setFrame(damage / 25);
            if (damage >= damageTreshold) {
                super.hide();
                super.destroy();
            }
        }
    }

    @Override
    public void draw(GameCore gc, Graphics g) {
        super.draw(gc, g);
        if (damageBlock != null) {
            if (damage < damageTreshold) {
                this.damageBlock.draw(gc, g);
            }
        }
        if (colored) {
            g.setColor(color);
            g.fillRectangle((int) super.x, (int) super.y, 32, 32);
        }
    }

    boolean isDoors() {
        return state.equals("doors");
    }

    void openDoor() {
        if (!locked) {
            redraw();
            hitboxAllowed = !hitboxAllowed;
            layer = hitboxAllowed ? 10 : 0;
        }
    }
    
    void lockDoor(){
        locked=true;
    }
    
    void unlockDoor(){
        locked=false;
    }

    boolean isMenOnSpot() {
        return menOnSpot;
    }

    void menOnSpot() {
        menOnSpot=true;
    }

    void menLeftSpot() {
        menOnSpot=false;
    }
}
