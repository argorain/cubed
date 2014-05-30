package cubed;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import srengine.Entity;
import srengine.GameContainer;
import srengine.InputManager;
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
    private boolean engine = false;
    private boolean blast = false;
    private String name;
    private boolean colored;
    private Color color;

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

    public Block(float x, float y, ArrayList<String> images) {
        super(x, y, images);
    }

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
                    setPeriod(Integer.parseInt(params[i].split("=")[1]));
                    run();
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
                            staticRotate(90);
                            break;
                        case 180:
//    					setDrawAngle(180);
                            staticRotate(180);
                            break;
                        case 270:
//    					setDrawAngle(270);
                            staticRotate(270);
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
                case MouseEvent.BUTTON1:
                    System.out.println("LMB");
                    System.out.println(this.relX + "," + this.relY);
                    clickedLMB = true;
                    switch (state) {
                        case "doors":
                            redraw();
                            hitboxAllowed = !hitboxAllowed;
                            System.out.println("door action");
                            break;
                        default:
                            break;
                    }
                   // colorUp(new Color(0, 255, 0, 70));
                    break;
                case MouseEvent.BUTTON2:
                    System.out.println("RMB");
                    switch (state) {
                        case "blast":
                            hideShow();
                            run();
                            System.out.println("blast");
                            break;
                        default:
                            break;
                    }
                    break;
                case MouseEvent.BUTTON3:
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

    public void colorUp(Color color){
        this.color=color;
        colored=true;
    }
    
    public void uncolor(){
        colored=false;
    }
    
    @Override
    public void update(InputManager input, GameContainer gc) {
        super.update(input, gc);
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
    public void draw(Graphics2D g, GameContainer gc) {
        //super.draw(g, gc);
        if (damageBlock != null) {
            if (damage < damageTreshold) {
                this.damageBlock.draw(g, gc);
            }
        }
        if(colored){
            g.setColor(color);
            g.fillRect((int)super.x, (int)super.y, 32, 32);
        }
    }
}
