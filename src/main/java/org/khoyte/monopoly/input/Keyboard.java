package org.khoyte.monopoly.input;

import java.util.Scanner;

import org.khoyte.monopoly.shared.ValidationHelper;

/**
 * Java Keyboard Class for user input Validation
 * 
 * https://www.youtube.com/watch?v=Vs2ZR7-LJO0
 * 
 */
public class Keyboard {
	private static final Keyboard keyboard = new Keyboard();

	public static final String INVALID_INPUT = "Error - Invalid Input";

	public static String read(String promptMsg, String errorMsg) {
		return keyboard.readLine(promptMsg, errorMsg);
	}

	private Scanner in;

	private Keyboard() {
		in = new Scanner(System.in);
	}

	private static Keyboard instance;

	public static Keyboard getInstance() {
		if (instance == null) {
			instance = new Keyboard();
		}
		return instance;
	}

	public String readLine(String promptMsg, String errorMsg) {
		String strInput = null;
		boolean valid = false;

		// Keep looping until valid input
		while (valid == false) {

			// Prompt the user
			System.out.print(promptMsg);

			// Grab input from keyboard
			strInput = in.nextLine();

			// Validate input
			if (ValidationHelper.hasText(strInput))
				break;

		}

		return ValidationHelper.getTrimmed(strInput);
	}

	public boolean readYN(String promptMsg, String errorMsg) {
		String strInput = null;

		while (true) {

			// Prompt the user
			System.out.println(promptMsg);

			// Grab input from keyboard
			strInput = in.nextLine();

			if (ValidationHelper.hasText(strInput) && strInput.matches("[yYnN]"))
				break;
		}

		return strInput.matches("(y|Y)") ? true : false;
	}

	public int readInteger(String promptMsg, String errorMsg) {
		int num = 0;
		String strInput;

		// Keep looping until valid input
		while (true) {

			// Prompt the user
			System.out.print(promptMsg);

			// Grab input from keyboard
			strInput = in.nextLine();

			// Try to convert String to integer
			try {
				num = Integer.parseInt(strInput);
				break;
			} catch (NumberFormatException e) {
				System.out.println(errorMsg);
			}
		}

		return num;
	}

	public double readDouble(String promptMsg, String errorMsg) {
		double num = 0;
		String strInput;

		// Keep looping until valid input
		while (true) {

			// Prompt the user
			System.out.print(promptMsg);

			// Grab input from keyboard
			strInput = in.nextLine();

			// Try to convert String to double
			try {
				num = Double.parseDouble(strInput);
				break;
			} catch (NumberFormatException e) {
				System.out.println(errorMsg);
			}
		}

		return num;
	}

	public int readInteger(String promptMsg, String errorMsg, int low, int high) {
		int num = 0;
		String strInput;

		// Keep looping until valid input
		while (true) {

			// Prompt the user
			System.out.print(promptMsg);

			// Grab input from keyboard
			strInput = in.nextLine();

			// Try to convert String to integer
			try {
				num = Integer.parseInt(strInput);
				if (num >= low && num <= high)
					break;
				else
					System.out.println(errorMsg);
			} catch (NumberFormatException e) {
				System.out.println(errorMsg);
			}
		}

		return num;
	}

	public void close() {
		if (in != null) {
			in.close();
		}
	}
}
