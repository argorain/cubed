package cubed;

import Core.Entity;
import java.util.Timer;
import java.util.TimerTask;
import srengine.utils.Serialiser;

public class Man extends Entity {

    Balloon balloon = null;
    String name;
    String race;
    String age;
    String health = "100%";
    private static final int kX = 30;
    private static final int kY = -85;
    int relX, relY, trelX, trelY, mrelX, mrelY, dVectX, dVectY;
    private final int MAP_LENGTH = 3;
    int map[][] = new int[MAP_LENGTH][2];
    boolean moving = false;
    int step = 0;
    int dx = 0, dy = 0, rdx = 0, rdy = 0;
    final int dv = 4;
    private int BLOCKSIZE;
    private boolean inverted=false;

    public Man(float x, float y, String[] images, Serialiser s) {
        super(x, y, images, s);
        dVectX=dVectY=0;
        for(int l=0; l<MAP_LENGTH; l++){
            map[l][0]=map[l][1]=0;
        }
    }

    public void setInfo(String name, String race, String age) {
        this.name = name;
        this.age = age;
        this.race = race;
    }

    public void setBallonMessage(String message) {
        balloon.setText(message);
    }

    @Override
    public void setAngle(float angle) {
        super.setAngle(angle);
        if (balloon != null) {
            balloon.setAngle(angle);
        }
    }

    @Override
    public void setX(float x) {
        this.x = x;
        if (balloon != null) {
            balloon.setX(x + kX);
        }
    }

    @Override
    public void setY(float y) {
        this.y = y;
        if (balloon != null) {
            balloon.setY(y + kY);
        }
    }

    public void setBalloon(Balloon balloon) {
        this.balloon = balloon;
        balloon.setX(x + kX);
        balloon.setY(y + kY);
    }

    public void setRelPos(int x, int y) {
        relX = x;
        relY = y;
        setX(x * BLOCKSIZE);
        setY(y * BLOCKSIZE);
        addPos(x, y);
    }

    public int relX() {
        return relX;
    }

    public int relY() {
        return relY;
    }

    public void move() {
        moving = true;
    }

    public boolean isMoving() {
        return moving;
    }

    void setTargetRelPos(int px, int py) {
        trelX = px;
        trelY = py;
    }

    boolean onSpot() {
        return (relX == trelX && relY == trelY);
    }

    void moveTo() {
        this.startAnimate();
        Timer run = new Timer();
        moving = true;
        step = 0;

        dx = dy = rdx = rdy = 0;

        if (mrelX * BLOCKSIZE > getX()) {
            dx = dv;
            rdx++;
            lookRight();
        } else if (mrelX * BLOCKSIZE < getX()) {
            dx = -dv;
            rdx--;
            lookLeft();
        }
        else if (mrelY * BLOCKSIZE > getY()) {
            dy = dv;
            rdy++;
        } else if (mrelY * BLOCKSIZE < getY()) {
            dy = -dv;
            rdy--;
        }
        run.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {

                setX(getX() + dx);
                setY(getY() + dy);
                if (step % 2 == 0) {
                    redraw();
                }
                if (step < (int) (BLOCKSIZE / dv)) {
                    step++;
                } else {
                    moving = false;
                    setRelPos(relX + rdx, relY + rdy);
                    super.cancel();
                }
            }
        }, 0, 100);
    }

    void setRelPosToMove(int ktx, int kty) {
        mrelX = ktx;
        mrelY = kty;
    }

    void setBlocksize(int BLOCKSIZE) {
        this.BLOCKSIZE = BLOCKSIZE;
    }
    
    void lookLeft(){
        if(!inverted){
//            invertOverY();
            inverted=true;
        }
    }
    
    void lookRight(){
        if(inverted){
//            invertOverY();
            inverted=false;
        }
    }     

    private void addPos(int x, int y) {
        for(int i=MAP_LENGTH-1; i>0; i--){
            map[i][0]=map[i-1][0];
            map[i][1]=map[i-1][1];
        }
        map[0][0]=x;
        map[0][1]=y;
    }
}
