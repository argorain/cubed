package cubed;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.io.File;

import srengine.Entity;
import srengine.GameContainer;
import srengine.utils.Serialiser;

public class Balloon extends Entity{

	Font f;
	int bx = 10;
	int by = 22;
	char[][] text = new char[5][12];
	
	public Balloon(float x, float y, String image, Serialiser s) {
		super(x, y, image, s);
		try {
			f = Font.createFont(Font.TRUETYPE_FONT, new File("../Fonts/8p.TTF")).deriveFont(16f);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void setText(String text){
		char[] arr = text.toCharArray();
		int arrC = 0;
		for(int line=0; line<4; line++){
			for(int cC = 0; cC < 12; cC++){
				if(arr[arrC]=='\n'){
					this.text[line][cC]=' ';
				}else{
					if(arr[arrC]==' '&&cC==0)
						cC--;
					else
						this.text[line][cC]=arr[arrC];
					arrC++;
				}
			}
		}
	}
	
	@Override
	public void draw(Graphics2D g, GameContainer gc) {
        super.draw(g, gc);
        g.setColor(Color.BLACK);
        g.setFont(f);
        for(int i=0; i<text.length-1; i++){
        	g.drawChars(text[i], 0, 12, (int)x+bx, (int)y+by+i*18);
        }
        g.setColor(Color.WHITE);
        for(int i=0; i<text.length-1; i++){
        	g.drawChars(text[i], 0, 12, (int)x+bx-1, (int)y+by-1+i*18);
        }
    }

}
