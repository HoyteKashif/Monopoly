package com.kh.monopoly;

import java.util.Random;

public class Dice {

	public static int[] roll() {

		Random rand = new Random();

		int[] roll = new int[2];

		System.out.print("Rolled ");

		roll[0] = (rand.nextInt(6) + 1);
		System.out.print("[" + roll[0] + "] ");

		roll[1] = (rand.nextInt(6) + 1);
		System.out.print("[" + roll[1] + "]\n");

		return roll;
	}

	public static int rollMultipliedBy(int scalar) {
		int[] roll = roll();
		return (roll[0] + roll[1]) * 10;
	}

}
