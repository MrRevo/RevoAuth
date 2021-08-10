package com.revo.auth.recovery;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/*
 * Recovery manager 
 * 
 * Created by Revo
 */

public final class RecoveryManager {

	/*
	 * Data
	 */
	
	private static Map<String, String> keys = new HashMap<String, String>();
	
	/*
	 * Add new user
	 */
	
	public static void add(String username) {
		keys.put(username, createKey());
	}
	
	public static boolean validate(String username, String key) {
		if(keys.get(username).equals(key)) {
			keys.remove(username);
			return true;
		}
		return false;
	}
	
	public static String getKey(String username) {
		return keys.get(username);
	}
	
	/*
	 * Generate recovery key
	 */
	
	private static String createKey() {
		String chars = "abcdefghijklmnzoprstuwxyz123456789!@#";
		String allChars = chars + chars.toUpperCase();
		StringBuilder sb = new StringBuilder();
		for(int x = 0; x < 10; x++)
			sb.append(allChars.charAt(new Random().nextInt(allChars.length() - 1)));
		return sb.toString();
	}
	
}
