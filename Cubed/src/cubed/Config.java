package cubed;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import srengine.utils.Serialiser;

public class Config {
	private String path;
	private String filename;
	private Serialiser s;
	private final static String SEPARATOR = ":";
	
	public Config(String path){
		this.path = path;
	}
	
	public Config(String filename, Serialiser s){
		this.filename = filename;
		this.s = s;
	}
	
	public String[] getValue(String key, int par){
		if(path != null){
			try{
			BufferedReader r = new BufferedReader(new FileReader(path));
			String line;
			while((line=r.readLine())!=null){
				if(line.split(SEPARATOR)[par].equals(key)){
					r.close();
					return line.split(SEPARATOR);
				}
			}
			r.close();
			return null;
			}catch(IOException ex){
				System.err.println(ex.getCause());
			}
		}
		if(filename != null){
			try {
				String data = toString(s.getData(filename));	
                                String[] lines = data.split("\n");
//				String[] lines = data.split(System.getProperty("line.separator"));
				for(int i=0; i<lines.length; i++){
					if(lines[i].split(SEPARATOR)[par].equals(key)){
						return lines[i].split(SEPARATOR);
					}
				}
				return null;
			} catch (IOException e) {
				System.err.println(e.getCause());
			}
		}
		return null;
	}
	
	private String toString(ByteArrayInputStream bs){
		String result = "";
		while(bs.available()>0){
			result+=(char)bs.read();
		}
		return result;
	}
	
	public ArrayList<String[]> getMap(){
		ArrayList<String[]> list = new ArrayList<String[]>();
		if(path != null){
			try{
			BufferedReader r = new BufferedReader(new FileReader(path));
			String line;
			while((line=r.readLine())!=null){
				list.add(line.split(SEPARATOR));
			}
			r.close();
			return list;
			}catch(IOException ex){
				System.err.println(ex.getCause());
			}
		}
		if(filename != null){
			try {
				String data = toString(s.getData(filename));
				//String[] lines = data.split(System.getProperty("line.separator")); //FIXME /n /n/r mismatch
				String[] lines = data.split("\n");
				for(int i=0; i<lines.length; i++){
					list.add(lines[i].split(SEPARATOR));
				}
				return list;
			} catch (IOException e) {
				System.err.println(e.getCause());
			}
		}
		return null;
	}
}
