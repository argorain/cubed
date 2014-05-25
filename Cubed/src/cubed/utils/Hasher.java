package cubed.utils;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Hasher {
	public static String encrypt(String key, String data){
		String datagram = "";

		System.out.println(data);
		for(int i=0; i<data.length();i++){
			//datagram+=data.charAt(i)-' ';
			datagram+=data.charAt(i);
		}
		System.out.println(datagram);
		try{
			byte[] keyBytes = key.getBytes("UTF-8");
			MessageDigest md = MessageDigest.getInstance("MD5");
			byte[] hash = md.digest(keyBytes);
			byte[] datagramBytes = datagram.getBytes();
			for(int i=0; i<datagramBytes.length; i+=hash.length){
				for(int j=0; j<hash.length; j++){
					if(i+j<datagramBytes.length)
						datagramBytes[j]=(byte)(datagramBytes[j]+hash[j]);
					else
						break;
				}
				for(int j=0; j<hash.length; j++){
					hash[j]++;
				}
			}
			datagram = datagramBytes.toString();
		}catch(UnsupportedEncodingException ex){
			System.err.println(ex.getCause());
		}catch(NoSuchAlgorithmException ex){
			System.err.println(ex.getCause());
		}
		System.out.println(datagram);
		
		
		
		return datagram;
	}
}
