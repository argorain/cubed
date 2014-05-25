package main;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;


public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		boolean quit = false;

		System.out.println("Server");

		try {
			ServerSocket ss = new ServerSocket(42424);

			while (!quit) {
				final Socket sock = ss.accept();
				Thread t = new Thread() {
					public void run() {
						try {
							InputStream is = sock.getInputStream();
							OutputStream os = sock.getOutputStream();
							while(is.available()>0){
								char b = (char)is.read();
								System.out.print(b);
								os.write(b);
							}
							sock.close();
							is.close();
							os.close();
						} catch (IOException e) {
						}
					}
				};
				t.setDaemon(true);
				t.start();
			}
		} catch (Exception e) {
		}

	}

}
