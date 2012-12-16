package org.inqle.core.util;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class JavaHasher {
	
	/**
	 * Use 16 bit encoding in order to ensure identical value as that obtained by sha256.js
	 */
	private static final String ENCODING = "UTF-16LE";
	
	public static byte[] bytesSha256(String msg) {
		MessageDigest hash = null;
		try {
			hash = MessageDigest.getInstance("SHA-256");
		} catch (NoSuchAlgorithmException e) {
			//log.error("Unable to get SHA-256 algorithm", e);
			return null;
		}
		
		byte[] hashedBytes;
		try {
			hashedBytes = hash.digest(msg.getBytes(ENCODING));
		
			return hashedBytes;
		} catch (UnsupportedEncodingException e) {
			//log.error("Unable to encode hased string as " + ENCODING, e);
			return null;
		}
	}
	
	public static String hashSha256(String str) {
		return byteArrayToHexString(bytesSha256(str));
	}   
	
	
	/**
	* Convert a byte[] array to readable string format. This makes the "hex"
	readable
	* @return result String buffer in String format 
	* @param in byte[] buffer to convert to string format
	* 
	* @see http://www.devx.com/tips/Tip/13540
	*/

	public static String byteArrayToHexString(byte in[]) {
	    byte ch = 0x00;
	    int i = 0; 
	    if (in == null || in.length <= 0)
	        return null;

	    String pseudo[] = {"0", "1", "2", "3", "4", "5", "6", "7", "8",
	    		"9", "a", "b", "c", "d", "e", "f"};

	    StringBuffer out = new StringBuffer(in.length * 2);
	    while (i < in.length) {
	    	ch = (byte) (in[i] & 0xF0); // Strip off high nibble

	        ch = (byte) (ch >>> 4); // shift the bits down

	        ch = (byte) (ch & 0x0F);    
	
	        // must do this is high order bit is on!
	        out.append(pseudo[ (int) ch]); // convert the nibble to a String Character
	        ch = (byte) (in[i] & 0x0F); // Strip off low nibble 

	        out.append(pseudo[ (int) ch]); // convert the nibble to a String Character
	        i++;
	    }
	    String rslt = new String(out);
	    return rslt;

	} 
}