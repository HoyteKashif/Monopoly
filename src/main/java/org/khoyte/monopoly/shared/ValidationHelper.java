package org.khoyte.monopoly.shared;

public class ValidationHelper {

	// Try to convert String to integer
	public static boolean isNumeric(String str) {
		try {
			Integer.parseInt(str);
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}

}
