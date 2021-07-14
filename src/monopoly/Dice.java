package monopoly;

import java.util.Random;

public class Dice {

	public static int[] roll() {

		Random rand = new Random();

		int[] roll = new int[2];
		roll[0] = (rand.nextInt(6) + 1);
		roll[1] = (rand.nextInt(6) + 1);

		System.out.println("rolled: " + (roll[0] + roll[1]));
		return roll;
	}

}
