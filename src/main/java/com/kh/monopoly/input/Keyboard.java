package com.kh.monopoly.input;

import java.util.Scanner;

/**
 * Java Keyboard Class for user input Validation
 * 
 * https://www.youtube.com/watch?v=Vs2ZR7-LJO0
 * 
 */
public class Keyboard {
	private static final Keyboard keyboard = new Keyboard();

	public static String read(String promptMsg, String errorMsg) {
		return keyboard.readLine(promptMsg, errorMsg);
	}

	private Scanner in;

	public Keyboard() {
		in = new Scanner(System.in);
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
			valid = strInput != null && !(strInput = strInput.trim()).isEmpty();

		}

		return strInput;
	}

	public int readInteger(String promptMsg, String errorMsg) {
		int num = 0;
		String strInput;
		boolean valid = false;

		// Keep looping until valid input
		while (valid == false) {

			// Prompt the user
			System.out.print(promptMsg);

			// Grab input from keyboard
			strInput = in.nextLine();

			// Try to convert String to integer
			try {
				num = Integer.parseInt(strInput);
				valid = true;
			} catch (NumberFormatException e) {
				System.out.println(errorMsg);
			}
		}

		return num;
	}

	public double readDouble(String promptMsg, String errorMsg) {
		double num = 0;
		String strInput;
		boolean valid = false;

		// Keep looping until valid input
		while (valid == false) {

			// Prompt the user
			System.out.print(promptMsg);

			// Grab input from keyboard
			strInput = in.nextLine();

			// Try to convert String to double
			try {
				num = Double.parseDouble(strInput);
				valid = true;
			} catch (NumberFormatException e) {
				System.out.println(errorMsg);
			}
		}

		return num;
	}

	public int readInteger(String promptMsg, String errorMsg, int low, int high) {
		int num = 0;
		String strInput;
		boolean valid = false;

		// Keep looping until valid input
		while (valid == false) {

			// Prompt the user
			System.out.print(promptMsg);

			// Grab input from keyboard
			strInput = in.nextLine();

			// Try to convert String to integer
			try {
				num = Integer.parseInt(strInput);
				if (num >= low && num <= high)
					valid = true;
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
