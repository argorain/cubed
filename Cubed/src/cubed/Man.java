package cubed;

import srengine.Entity;
import srengine.utils.Serialiser;

public class Man extends Entity{

	Balloon balloon  = null;
	String name;
	String race;
	String age;
	String health="100%";
	private static final int kX = 30;
	private static final int kY = -85;
	int relX, relY;
	boolean moving = false;
	
	public Man(float x, float y, String[] images, Serialiser s) {
		super(x, y, images, s);
	}
	
	public void setInfo(String name, String race, String age){
		this.name = name;
		this.age = age;
		this.race= race;
	}
	
	public void setBallonMessage(String message){
		balloon.setText(message);
	}
	
	public void setAngle(float angle){
        super.setAngle(angle);
        if(balloon != null)
        	balloon.setAngle(angle);
    }

	public void setX(float x) {
        this.x = x;
        if(balloon != null)
        	balloon.setX(x+kX);
    }

    public void setY(float y) {
        this.y = y;
        if(balloon != null)
        	balloon.setY(y+kY);
    }
    
    public void setBalloon(Balloon balloon){
    	this.balloon = balloon;
    	balloon.setX(x+kX);
    	balloon.setY(y+kY);
    }
    
    public void setRelPos(int x, int y){
    	relX= x;
    	relY= y;
    }
    
    public int relX(){
    	return relX;
    }
    
    public int relY(){
    	return relY;
    }
    
    public void move(){
    	moving = true;
    }
    
    public boolean isMoving(){
    	return moving;
    }
}
