package common;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5Hasher {
	
	
	private static MessageDigest md5Hasher;
	
	{
		 try {
			md5Hasher = MessageDigest.getInstance("md5");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
	}
	
	
	
	public static byte[] getHashBytes(String str) {
		md5Hasher.update(str.getBytes());
		byte[] digest = md5Hasher.digest();
		return digest;
	}
	
	
	public static String getHashStr(String str) {
		return bytesToHex(getHashBytes(str));
	}
	
	public static boolean isEqual(byte[] hash, String str) {
		return getHashStr(str) == bytesToHex(hash);
	}
	
	
	
	private static final char[] HEX_ARRAY = "0123456789ABCDEF".toCharArray();
	
	public static String bytesToHex(byte[] bytes) {
	    char[] hexChars = new char[bytes.length * 2];
	    for (int j = 0; j < bytes.length; j++) {
	        int v = bytes[j] & 0xFF;
	        hexChars[j * 2] = HEX_ARRAY[v >>> 4];
	        hexChars[j * 2 + 1] = HEX_ARRAY[v & 0x0F];
	    }
	    return new String(hexChars);
	}

}
