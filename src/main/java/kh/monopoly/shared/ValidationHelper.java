package kh.monopoly.shared;

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

	public static boolean isEmpty(String str) {
		return !hasText(str);
	}

	public static boolean hasText(String str) {
		return str != null && !str.trim().isEmpty();
	}

	public static String getTrimmed(String str) {
		return hasText(str) ? str.trim() : null;
	}
}
