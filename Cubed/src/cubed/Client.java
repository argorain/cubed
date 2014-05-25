package cubed;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

import javax.xml.bind.DatatypeConverter;

import cubed.utils.Hasher;

//import cubed.utils.Shifter;

public class Client {

	private Queue<String> outBuffer;
	private Queue<String> inBuffer;
	private static String adress;
	private static int port;
	private String key = "superSecretKey";

	public Client(String adress, int port) {
		outBuffer = new LinkedList<>();
		inBuffer = new LinkedList<>();
		setAdress(adress);
		setPort(port);
	}

	public void push(String data){
		outBuffer.add(data.trim());
	}

	public void clearOutBuffer(){
		outBuffer.clear();
	}

	public void clearInBuffer(){
		inBuffer.clear();
	}

	public String pop(){
		return inBuffer.poll();
	}

	private String pack(){
		String datagram = "";
		//datagram = '['+String.valueOf(System.currentTimeMillis())+']'+'['+Shifter.shift(key, outBuffer.poll(), 1)+']';
		//datagram = '['+String.valueOf(System.currentTimeMillis())+']'+'['+Hasher.encrypt(key, outBuffer.poll())+']';
		datagram = '['+String.valueOf(System.currentTimeMillis())+']'+'['+outBuffer.poll()+']';
		
		datagram = DatatypeConverter.printBase64Binary(datagram.getBytes());
		datagram = '{'+String.valueOf(datagram.length())+':'+datagram+'}';
		return datagram;
	}
	
	private void unpack(String data){
		String datagram = "";
		int length;
		long timestamp;
		
		//datagram = '['+String.valueOf(System.currentTimeMillis())+']'+'['+Shifter.shift(key, outBuffer.poll(), 1)+']';
		System.out.println("data: "+data);
		datagram=data.trim().substring(1, data.length()-1);
		datagram=datagram.split(":")[1];
		System.out.println("datagram: "+datagram);
		byte[] decryptBytes = DatatypeConverter.parseBase64Binary(datagram);
		datagram="";
		for(int i=0; i<decryptBytes.length; i++){
			datagram+=(char)decryptBytes[i];
		}
		System.out.println("b64: "+datagram);
		//datagram = '{'+String.valueOf(datagram.length())+':'+datagram+'}';
	}
	
	public void connect(){
		try {
			Socket socket = new Socket(getAdress(), getPort());

			BufferedReader br = new BufferedReader(
					new InputStreamReader(socket.getInputStream()));
			BufferedWriter bw = new BufferedWriter(
					new OutputStreamWriter(socket.getOutputStream()));

			while(!outBuffer.isEmpty()){
				bw.write(pack());
				bw.flush();
			}

			String line = "";

			// dokud jsou data, opakuj
			while (line != null) {
				line = br.readLine();
				if (line != null){
					System.out.println(line);  // platná data vypisuj
					inBuffer.add(line);
					unpack(line);
				}
			}

			bw.close();
			br.close();
			socket.close(); // zavření socketu

		} catch (Exception e) { 
			System.err.print(e.getMessage());
		}
	}

	public static int getPort() {
		return port;
	}

	public static void setPort(int port) {
		Client.port = port;
	}

	public static String getAdress() {
		return adress;
	}

	public static void setAdress(String adress) {
		Client.adress = adress;
	}
}
