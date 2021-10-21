package com.kh.monopoly;

import java.util.Random;

import org.apache.log4j.Logger;

import com.kh.monopoly.player.Player;

public class Dice {

	private static final Logger LOGGER = Logger.getLogger(Dice.class);

	private static Dice instance;

	private final Random rand;

	private int double_count = 0;

	public static Dice getInstance() {
		if (instance == null) {
			instance = new Dice();
		}
		return instance;
	}

	private Dice() {
		this.rand = new Random();
	}

	private Player lastRoller;

	public int roll(Player player) {

		if (player == lastRoller) {
			double_count++;
		} else {
			double_count = 0;
		}

		return roll();
	}

	public int getDoubleCount() {
		return double_count;
	}

	public void incrementDoubleCount() {
		LOGGER.info("Increment Double Count " + double_count + " to " + (double_count++));
	}

	public int roll() {

		int[] roll = new int[2];
		roll[0] = (rand.nextInt(6) + 1);
		roll[1] = (rand.nextInt(6) + 1);

		Game.getInstance().print(String.format("Rolled [%d] [%d]", roll[0], roll[1]));

		return roll[0] + roll[1];
	}

	public int rollMultipliedBy(int scalar) {
		return roll() * 10;
	}

}
