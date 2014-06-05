package cubed;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;

import srengine.Entity;
import srengine.GameContainer;
import srengine.IHitBox;
import srengine.InputManager;
import srengine.Rectangle;
import srengine.SREObject;
import srengine.utils.Serialiser;
import srengine.utils.SortedList;

public class SpaceShip extends SREObject {

    // Block[][] blocks;
    SortedList<Block> blocks = new SortedList<>();
    Point centerOfGravity = new Point(32, 32);
    String shipName = "";
    String className = "";
    private static final int BLOCKSIZE = 32;
    SortedList<Man> men = new SortedList<>();
    private float velocityX;
    private float velocityY;
    boolean clickedLMB = false;
    int px = 0, py = 0;
    boolean freePos = false;
    private float momentOfForce = 1f; //tocivy moment ... vypocita se podle umisteni motoru
    private float acceleration = 0;
    ArrayList<ArrayList<Integer>> rooms = new ArrayList<>();

    public SpaceShip(float x, float y) {
        super(x, y);
    }

    public void join(Man entity) {
        entity.setBlocksize(BLOCKSIZE);
        entity.setRelPos(1, 1);
        entity.setTargetRelPos(1, 1);
        /* entity.setX(0);
         entity.setY(0);*/
        men.add(entity);
    }

    public void add(int relX, int relY, Block block) {
        block.setRelative(relX, relY);
        blocks.add(block.getLayer(), block);
        countGravity();
    }

    public void add(int relX, int relY, Block block, IHitBox hitbox, IHitBox clickBox) {
        block.setRelative(relX, relY);
        block.setHitBox(hitbox);
        block.setClickBox(clickBox);
        blocks.add(block.getLayer(), block);
        countGravity();
    }

    private void countGravity() {
        double sumMX = 0;
        double sumMY = 0;
        double sumM = 0;
        for (int i = 0; i < blocks.size(); i++) {
            sumMX += (blocks.get(i).getRelX()) * blocks.get(i).getWeight();
            sumMY += (blocks.get(i).getRelY()) * blocks.get(i).getWeight();
            sumM += blocks.get(i).getWeight();
        }
        centerOfGravity.x = (int) ((sumMX / sumM) * BLOCKSIZE + BLOCKSIZE / 2);
        centerOfGravity.y = (int) ((sumMY / sumM) * BLOCKSIZE + BLOCKSIZE / 2);
    }

    public boolean fill(String name, Serialiser ships, Serialiser graphics, Serialiser cfgSer) {
        byte b;
        Config cfg = new Config("blocks.cfg", cfgSer);

        //load stream from ships serialiser
        ByteArrayInputStream ship = null;
        try {
            ship = ships.getData(name);
        } catch (IOException ex) {
            System.err.println(ex.getMessage());
        }
        //check [info] block
        if (contains(ship, ("[info]\n").toCharArray()) == -1) {
            System.err.println("Wrong datafile format in beginig of [info] block. Aborting.");
            return false;
        }

        while (true) {
            String buffer = "";
            while ((b = (byte) ship.read()) != '\n') {
                buffer += (char) b;
            }
            if (buffer == "") {
                break;
            }
            String[] bufferSplit = buffer.split("=");
            switch (bufferSplit[0]) {
                case "name":
                    shipName = bufferSplit[1];
                    break;
                case "class":
                    className = bufferSplit[1];
                    break;
                default:
                    break;
            }
        }
        //check [map] mark
        if (contains(ship, ("[map]\n").toCharArray()) == -1) {
            System.err.println("Wrong datafile format in beginig of [map] block. Aborting.");
            return false;
        }
        //load map and add blocks
        int x = 0;
        int y = 0;
        String key = "";
        String supKey = "";
        boolean BsupKey = false;
        String[] line;
        Block block;
        while ((b = (byte) ship.read()) != -1) {
            if (b == '\n' || b == '\t') {
                if (!key.equals("")) {
                    String[] subLine = key.split(">");
                    line = cfg.getValue(subLine[0], 0);
                    if (subLine.length > 1) {
                        line[2] = line[2].concat(",r=" + (subLine.length > 1 ? subLine[1] : null));
                    }
                    block = new Block(line[1].split(","), graphics, line[2].split(","));
                    if (BsupKey) {
                        block.cargoOnSpot();
                    }
                    add(x, y, block,
                            new Rectangle(BLOCKSIZE, BLOCKSIZE),
                            new Rectangle(BLOCKSIZE, BLOCKSIZE));
                    if (BsupKey) {
                        subLine = supKey.split(">");
                        line = cfg.getValue(supKey.split(">")[0], 0);
                        if (subLine.length > 1) {
                            line[2] = line[2].concat(",r=" + (subLine.length > 1 ? subLine[1] : null));
                        }
                        block = new Block(line[1].split(","), graphics, line[2].split(","));
                        add(x, y, block,
                                new Rectangle(BLOCKSIZE, BLOCKSIZE),
                                new Rectangle(BLOCKSIZE, BLOCKSIZE));
                    }
                }
                if (b == '\n') {
                    y++;
                    x = 0;
                }
                if (b == '\t') {
                    x++;
                }
                key = "";
                supKey = "";
                BsupKey = false;
                continue;
            }
            if ((char) b == ',') {
                BsupKey = true;
                continue;
            }
            if (BsupKey) {
                supKey = supKey + (char) b;
            } else {
                key = key + (char) b;
            }
        }
        countGravity();


        for (int k = 0; k < blocks.size(); k++) {
            block = blocks.get(k);
            if (block.getLayer() <= 1 && !block.isCargoOnSpot()) {
            }
        }

        return true;
    }

    private int contains(ByteArrayInputStream array, char[] string) {
        for (int i = 0; i < string.length; i++) {
            if (string[i] != (array.read())) {
                return -1;
            }
        }
        return string.length;
    }

    //OLD DRAW METHOD
//    @Override
//    public void draw(Graphics2D g, GameContainer gc) {
//
//    	for(int i=0; i<blocks.size(); i++){
//    		blocks.get(i).draw(g, gc);
//    	}
//    	for(int i=0; i<entities.size(); i++){
//    		entities.get(i).draw(g, gc);
//    	}
//    }
    //NEW TESTED VERSION
    @Override
    public void draw(Graphics2D g, GameContainer gc) {

        BufferedImage shipGraphBuffer = new BufferedImage((getMaxRelX() + 2) * BLOCKSIZE, (getMaxRelY() + 2) * BLOCKSIZE, BufferedImage.TYPE_INT_RGB);
        Graphics shipGraph = shipGraphBuffer.getGraphics();
        AffineTransform transformace = new AffineTransform();

        for (int i = 0; i < blocks.size(); i++) {
            Block block = blocks.get(i);
            if (block != null && !block.hidden()) {
                shipGraph.drawImage(block.getImage(), block.getRelX() * BLOCKSIZE, block.getRelY() * BLOCKSIZE, null);

            }
        }

        /**
         * testing *
         */
        if (!men.get(0).hidden()) {
            shipGraph.drawImage(men.get(0).getImage(), (int)men.get(0).getX(), (int)men.get(0).getY() - 8, null);
        }
        /**
         * over
         */
        float moveX = 0;
        float moveY = 0;

        if (camera != null) {
            moveX = camera.getXMove();
            moveY = camera.getYMove();
        }

        transformace.translate(this.x + moveX, this.y + moveY);
        transformace.rotate(this.angle);
        transformace.translate(-centerOfGravity.x, -centerOfGravity.y);

        g.drawImage(shipGraphBuffer, transformace, null);

        for (int i = 0; i < blocks.size(); i++) {
            Block block = blocks.get(i);
            block.draw(g, gc);
            //for show hitbox
//                g.setColor(Color.yellow);
//                block.getHitBox().draw(g, gc);
        }

        /*for (int i = 0; i < entities.size(); i++) {
         entities.get(i).draw(g, gc);
         }*/
    }

    @Override
    public void update(InputManager input, GameContainer gc) {
        float moveX = centerOfGravity.x;
        float moveY = centerOfGravity.y;

        if (input.isKeyTyped(KeyEvent.VK_T)) {
            men.get(0).hideShow();
        }

        for (int k = 0; k < blocks.size(); k++) {
            Block block = blocks.get(k);
            if (block != null) {
                double newX = (block.getRelX() * BLOCKSIZE - moveX) * Math.cos(angle) - (block.getRelY() * BLOCKSIZE - moveY) * Math.sin(angle);
                double newY = (block.getRelX() * BLOCKSIZE - moveX) * Math.sin(angle) + (block.getRelY() * BLOCKSIZE - moveY) * Math.cos(angle);
                block.setX((float) newX + x);
                block.setY((float) newY + y);
                block.setAngle(angle);
//            	block.setX(x+i*32);
//            	block.setY(y+j*32);
                if (block.clickedLMB()) {
                    clickedLMB = true;
                    px = block.getRelX();
                    py = block.getRelY();
                    if (block.getLayer() <= 1 && !block.isCargoOnSpot()) {
                        freePos = true;

                    }
                }

                ////
                if (block.getRelX() == men.get(0).relX && block.getRelY() == men.get(0).relY) {
                    block.colorUp(new Color(0, 255, 0, 50));
                } else {
                    block.uncolor();
                }
                ////
                block.update(input, gc);
            }
        }

        if (acceleration != 0) {
            accelerate(acceleration);
        }

        if (freePos) {
            /*System.out.println(men.get(0).getX()+":"+men.get(0).getY()+" -> "+(px * BLOCKSIZE + x - moveX)+":"+(py * BLOCKSIZE + y - moveY - 8));
             men.get(0).setX(px * BLOCKSIZE + x - moveX);
             men.get(0).setY(py * BLOCKSIZE + y - moveY - 8);*/
            men.get(0).setTargetRelPos(px, py);

            System.out.println(px + ", " + py);
            freePos = false;
        }
        if (velocityX != 0 || velocityY != 0) {
            this.rotate(velocityX * momentOfForce / 1000);
            this.setX((float) (this.getX() + Math.sin(angle) * velocityX + Math.cos(angle) * velocityY));
            this.setY((float) (this.getY() - Math.cos(angle) * velocityX + Math.sin(angle) * velocityY));
        }


        if (!men.get(0).onSpot()&&!men.get(0).isMoving()) {
            int kx = men.get(0).relX, ky = men.get(0).relY, tx = men.get(0).trelX, ty = men.get(0).trelY, ktx = kx, kty = ky;
            //if (Math.abs(kx - tx) > Math.abs(ky - ty)) {
                if (kx > tx) {
                    ktx = kx - 1;
                } else if (kx < tx) {
                    ktx = kx + 1;
                }
            //} else {
                else if (ky > ty) {
                    kty = ky - 1;
                } else if (ky < ty) {
                    kty = ky + 1;
                }
           // }
            boolean out=false;
            for (int variant = -1; variant < 7; variant++) {
                for (int k = 0; k < blocks.size(); k++) {
                    if (ktx == blocks.get(k).getRelX() && kty == blocks.get(k).getRelY()) {
                        if (blocks.get(k).getLayer() <= 1 && !blocks.get(k).isCargoOnSpot()) {
                            men.get(0).setRelPosToMove(ktx, kty);
                            men.get(0).moveTo();
                            out=true;
                            break;
                        }
                    }
                }
                if(out)
                    break;
                switch (variant) {
                    case 0:
                        if (ktx != kx) {
                            kty++;
                        }
                        if (kty != ky) {
                            ktx++;
                        }
                        break;
                    case 2:
                        if (ktx != kx) {
                            kty--;
                        }
                        if (kty != ky) {
                            ktx--;
                        }
                        break;
                    case 4:
                        if (ktx != kx) {
                            ktx--;
                        }
                        if (kty != ky) {
                            kty--;
                        }
                        break;
                    case 6:
                        if (ktx != kx) {
                            ktx++;
                        }
                        if (kty != ky) {
                            kty++;
                        }
                        break;
                    default:
                        kty=ky;
                        ktx=kx;
                        break;
                            
                }

            }
        }

        /*camera.move(velocityX, velocityY);
         System.out.println(velocityX+" "+velocityY);*/
    }

    public void blockAction(float x, float y, int actionSource) {
        for (int k = 0; k < blocks.size(); k++) {
            blocks.get(k).action(x, y, actionSource);
        }
    }

    public boolean collisionAll(Entity entity) {
        for (int i = 0; i < blocks.size(); i++) {
            if (entity.collisionWith(blocks.get(i))) {
                return true;
            }
        }
        return false;
    }

    public boolean collisionSelect(Entity entity) {
        for (int i = 0; i < blocks.size(); i++) {
            if (entity.collisionWith(blocks.get(i)) && blocks.get(i).hitboxAllowed()) {
                return true;
            }
        }
        return false;
    }

    public boolean collisionShip(SpaceShip ship) {
        for (int i = 0; i < blocks.size(); i++) {
            for (int j = 0; j < ship.blocks.size(); j++) {
                if (blocks.get(i).collisionWith(ship.blocks.get(j))) {
                    return true;
                }
            }
        }
        return false;
    }

    public void setVelocityX(float velocityX) {
        this.velocityX = velocityX;
    }

    public void setVelocityY(float velocityY) {
        this.velocityY = velocityY;
    }

    public float getVelocityX() {
        return velocityX;
    }

    public float getVelocityY() {
        return velocityY;
    }

    public void setVelocity(float velocityX, float velocityY) {
        this.setVelocityX(velocityX);
        this.setVelocityY(velocityY);
    }

    public void setHitBoxVisible(boolean visible) {
        for (int i = 0; i < blocks.size(); i++) {
            blocks.get(i).setHitBoxVisible(visible);
        }
    }

    public void setAcceleration(float acceleration) {
        this.acceleration = acceleration;
    }

    public float getAcceleration() {
        return this.acceleration;
    }

    public void accelerate(float acceleration) {
        this.velocityX += acceleration;
    }

    private int getMaxRelX() {
        int max = 0;
        for (int i = 0; i < blocks.size(); i++) {
            max = Math.max(max, blocks.get(i).getRelX());
        }
        return max;
    }

    private int getMaxRelY() {
        int max = 0;
        for (int i = 0; i < blocks.size(); i++) {
            max = Math.max(max, blocks.get(i).getRelY());
        }
        return max;
    }
}
