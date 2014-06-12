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
        for (int i = 0; i < blocks.size(); i++) {
            if (blocks.get(i).getLayer() <= 1 && !blocks.get(i).isCargoOnSpot() && !blocks.get(i).isMenOnSpot()) {
                entity.setRelPos(blocks.get(i).getRelX(), blocks.get(i).getRelY());
                entity.setTargetRelPos(blocks.get(i).getRelX(), blocks.get(i).getRelY());
                blocks.get(i).menOnSpot();
                /* entity.setX(0);
                 entity.setY(0);*/
                men.add(entity);
                break;
            }
        }

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
        for (int i = 0; i < men.size(); i++) {
            if (!men.get(i).hidden()) {
                shipGraph.drawImage(men.get(i).getImage(), (int) men.get(i).getX(), (int) men.get(i).getY() - 8, null);
            }

            shipGraph.setColor(Color.red);
            shipGraph.drawLine(men.get(i).trelX * BLOCKSIZE + BLOCKSIZE / 2, men.get(i).trelY * BLOCKSIZE + BLOCKSIZE / 2, men.get(i).relX * BLOCKSIZE + BLOCKSIZE / 2, men.get(i).relY * BLOCKSIZE + BLOCKSIZE / 2);
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
                    if (block.getLayer() <= 1 && !block.isCargoOnSpot() && !block.isMenOnSpot()) {
                        freePos = true;

                    }
                }

                ////
                /*if (block.getRelX() == men.get(mi).relX && block.getRelY() == men.get(mi).relY) {
                 block.colorUp(new Color(0, 255, 0, 50));
                 } else {
                 block.uncolor();
                 }*/
                ////
                block.update(input, gc);
            }
        }

        if (acceleration != 0) {
            accelerate(acceleration);
        }

        if (freePos) {
            /*System.out.println(men.get(mi).getX()+":"+men.get(mi).getY()+" -> "+(px * BLOCKSIZE + x - moveX)+":"+(py * BLOCKSIZE + y - moveY - 8));
             men.get(mi).setX(px * BLOCKSIZE + x - moveX);
             men.get(mi).setY(py * BLOCKSIZE + y - moveY - 8);*/
            men.get(0).setTargetRelPos(px, py);

            System.out.println(px + ", " + py);
            freePos = false;

            for (int i = 0; i < blocks.size(); i++) {
                blocks.get(i).uncolor();
            }

        }
        if (velocityX != 0 || velocityY != 0) {
            this.rotate(velocityX * momentOfForce / 1000);
            this.setX((float) (this.getX() + Math.sin(angle) * velocityX + Math.cos(angle) * velocityY));
            this.setY((float) (this.getY() - Math.cos(angle) * velocityX + Math.sin(angle) * velocityY));
        }

        for (int mi = 0; mi < men.size(); mi++) {
            //int rand = (int) (Math.round(Math.random() * 2) - 1);
            if (!men.get(mi).onSpot() && !men.get(mi).isMoving()) {
                int kx = men.get(mi).relX, ky = men.get(mi).relY, tx = men.get(mi).trelX, ty = men.get(mi).trelY, dvx = men.get(mi).dVectX, dvy = men.get(mi).dVectY;
                int ptx = kx, pty = ky, dirX = 0, dirY = 0;
                int counter = 1;

                dirX = dC(kx, tx);
                dirY = dC(ky, ty);

                float dV = dVect(dirX, dirY);
                if (dV>1.1) {
                    if (dirX > 0) {
                        ptx++;
                    } else {
                        ptx--;
                    }
                } else if (dV<0.9) {
                    if (dirY > 0) {
                        pty++;
                    } else {
                        pty--;
                    }
                } else {
                    if (dvx == 1 && dvy == 0) {
                        ptx++;
                    } else if (dvx == -1 && dvy == 0) {
                        ptx--;
                    } else if (dvx == 0 && dvy == 1) {
                        pty++;
                    } else if (dvx == 0 && dvy == -1) {
                        pty--;
                    }
                }
//                if (dvx == 0 && dvy == 0) {
//                    if (dirX > 0) {
//                        ptx++;
//                    } else if (dirX < 0) {
//                        ptx--;
//                    } else if (dirY > 0) {
//                        pty++;
//                    } else if (dirY < 0) {
//                        pty--;
//                    }
//                } else if (dirX == 0) {
//                    if (dirY > 0) {
//                        pty++;
//                    } else {
//                        pty--;
//                    }
//                } else if (dirY == 0) {
//                    if (dirX > 0) {
//                        ptx++;
//                    } else {
//                        ptx--;
//                    }
//                } else if (dvx == 1 && dvy == 0) {
//                    ptx++;
//                } else if (dvx == -1 && dvy == 0) {
//                    ptx--;
//                } else if (dvx == 0 && dvy == 1) {
//                    pty++;
//                } else if (dvx == 0 && dvy == -1) {
//                    pty--;
//                }
//                if (Math.random() > 0.8) {
//                    ptx = kx;
//                    pty = ky;
//                    if (Math.random() > 0.5) {
//                        if (ptx > 0) {
//                            ptx = Math.random() > 0.5 ? +1 : -1;
//                        } else {
//                            ptx++;
//                        }
//                    } else {
//                        if (pty > 0) {
//                            pty = Math.random() > 0.5 ? +1 : -1;
//                        } else {
//                            pty++;
//                        }
//                    }
//                }

                boolean over = false;
                do {
                    for (int k = 0; k < blocks.size(); k++) {
                        Block block = blocks.get(k);
                        if (ptx == block.getRelX() && pty == block.getRelY()) {
                            if (block.isDoors()) {
                                block.openDoor();
                            }
                            if (block.getLayer() <= 1 && !block.isCargoOnSpot()) {
                                block.colorUp(new Color(0, 255, 0, 60));
                                men.get(mi).setRelPosToMove(ptx, pty);
                                men.get(mi).moveTo();
                                block.menOnSpot();
                                for (int i = 0; i < blocks.size(); i++) {
                                    if (men.get(mi).relX == blocks.get(i).getRelX()) {
                                        if (men.get(mi).relY == blocks.get(i).getRelY()) {
                                            blocks.get(i).menLeftSpot();
                                        }
                                    }
                                }
                                men.get(mi).dVectX = ptx - kx;
                                men.get(mi).dVectY = pty - ky;
                                over = true;
                            } else {
                                block.colorUp(new Color(0, 255, 255, 60));
                            }
                            break;
                        }
                    }
                    if (!over) {
                        ptx = kx;
                        pty = ky;
                        switch (counter) {
                            case 1:
                                if (dirX > 0) {
                                    pty++;
                                } else if (dirX < 0) {
                                    pty--;
                                } else if (dirY > 0) {
                                    ptx++;
                                } else if (dirY < 0) {
                                    ptx--;
                                }
                                break;
                            case 2:
                                if (dirX > 0) {
                                    pty--;
                                } else if (dirX < 0) {
                                    pty++;
                                } else if (dirY > 0) {
                                    ptx--;
                                } else if (dirY < 0) {
                                    ptx++;
                                }
                                break;
                            case 3:
                                if (dirX > 0) {
                                    ptx--;
                                } else if (dirX < 0) {
                                    ptx++;
                                } else if (dirY > 0) {
                                    pty--;
                                } else if (dirY < 0) {
                                    pty++;
                                }
                                break;
                            default:
                                over = true;
                        }

                        counter++;
                    }
                } while (!over);


            }
        }

        /*camera.move(velocityX, velocityY);
         System.out.println(velocityX+" "+velocityY);*/
    }

    private float dVect(int x, int y){
        int norm = x>y?x:y;
        try{
            return (Math.abs(x)/norm) / (Math.abs(y)/norm);
        }catch(ArithmeticException e){
            return Math.abs(x)/norm;
        }
    }
    
    private int dC(int sC, int tC) {
        return tC - sC;
    }

    private int rDirection() {
        return (int) Math.round((Math.random() * 2) - 1);
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
