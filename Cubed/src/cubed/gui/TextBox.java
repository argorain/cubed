package cubed.gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;

import javax.management.MXBean;

import srengine.GameContainer;
import srengine.InputManager;
import srengine.SREObject;

public abstract class TextBox extends SREObject{

	private boolean show = false;
	private int width, height;
	private Color background = new Color(0, 0, 0, 180);
	String inputText = "";
	static final int LINE = 20;
	public static int MAX_CHARS=128;
	public static int MAX_LINES=7;
	String text[] = new String[MAX_LINES];
	String linePrompt = "";
	boolean flush = false;
	private static final char CURSOR = '_';
	private int cursorBlink = 0;
	private int cursorBlinkTime = 7;
	private int counter = 0;
	
	int line = 0;
	Font f;
	
	public TextBox(int x, int y, int width, int height, Font f){
		super(x, y);
		this.width = 300;
		this.height = height;
		this.f= f;
	}
	
	@Override
	public void draw(Graphics2D g, GameContainer gc) {
		if(show){
			String prompt = "Terminal# ";
			if(f!=null)
				g.setFont(f);
			g.setColor(Color.YELLOW);
			g.drawChars((prompt+inputText+CURSOR).toCharArray(), 0, inputText.length()+prompt.length()+cursorBlink, (int)x+10, (int)y);
			for(int i=0; i<MAX_LINES; i++){
				if(text[i]!=null){
					g.drawChars(text[i].toCharArray(), 0, text[i].length(), (int)x+10, (int)y+LINE*i+LINE);	
				}
			}
			if(counter > cursorBlinkTime){
				cursorBlink=(cursorBlink == 1 ? 0 : 1);
				counter = 0;
			}
			counter++;
		}
	}

	@Override
	public void update(InputManager input, GameContainer gc) {
		if(show){
			getHits(input);
		}
		if(flush){
			flush= false;
			flush(input);
		}
	}
	
	public void showHide(){
		show = !show;
		flush= true;
	}

	public void setBackgroundColor(Color color) {
		background = color;
	}

	public void getHits(InputManager input) {
		if(show){
			input.listenKeys = true;
			if(input.isKeyTyped(KeyEvent.VK_BACK_SPACE)){
				input.typed = input.typed.substring(0, input.typed.length()-2);
			}
			if(inputText.length()<MAX_CHARS)
				if(input.typed.length()>0)
					if((input.typed.charAt(input.typed.length()-1)>=32)&&(input.typed.charAt(input.typed.length()-1)<=126))
						inputText = linePrompt+input.typed;
			if(input.isKeyTyped(KeyEvent.VK_ENTER)){
				addText(inputText);
				enterEvent(inputText);
				inputText = "";
				input.typed = "";
			}
			
		}
	}

	public void flush(InputManager input) {
		input.flush();
	}
	
	private void addText(String newLine){
		String buffer[] = new String[MAX_LINES+1];
		for(int i = 0; i<line; i++){
			buffer[i+1]=text[i];
		}
		buffer[0]=new String(newLine);
		if(line<MAX_LINES)
			line++;
		for(int i=0; i<line; i++){
			text[i] = buffer[i];
		}
		
	}
	
	public boolean putLine(String in){
		if(in.length()<MAX_CHARS){
			addText(in);
			return true;
		}else{
			return false;
		}
	}
	
	public String getLine(int line){
		if(this.line == 0){
			return null;
		}
		if(line<=this.line){
			return new String(text[line]);
		}else{
			return null;
		}
	}
	
	public boolean setPrompt(String prompt){
		if(prompt.length()<MAX_CHARS){
			linePrompt = prompt;
			return true;
		}else{
			return false;
		}
	}
	
	public boolean visible(){
		return show;
	}
	
	public abstract void enterEvent(String text);
}
