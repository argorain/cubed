package cubed.utils;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class CryptWithMD5 {
	private static MessageDigest md;

	public static String cryptWithMD5(String pass){
		try {
			md = MessageDigest.getInstance("MD5");
			byte[] passBytes = pass.getBytes();
			md.reset();
			byte[] digested = md.digest(passBytes);
			StringBuffer sb = new StringBuffer();
			for(int i=0;i<digested.length;i++){
				sb.append(Integer.toHexString(0xff & digested[i]));
			}
			return sb.toString();
		} catch (NoSuchAlgorithmException ex) {
			System.err.print(ex.getCause());
		}
		return null;


	}
	
	public static String md5Java(String message){ 
		String digest = null; 
		try { 
			MessageDigest md = MessageDigest.getInstance("MD5"); 
			byte[] hash = md.digest(message.getBytes("UTF-8")); 
			//converting byte array to Hexadecimal String 
			StringBuilder sb = new StringBuilder(2*hash.length); 
			for(byte b : hash){ 
				sb.append(String.format("%02x", b&0xff)); 
			} 
			digest = sb.toString(); 
		} catch (UnsupportedEncodingException ex) { 
			ex.getMessage();
		} catch (NoSuchAlgorithmException ex) { 
			ex.getMessage();
		} 
		return digest; 
	}
}