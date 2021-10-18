package com.kh.monopoly.board.space;

import static java.lang.System.out;

import java.math.BigDecimal;

import org.apache.log4j.Logger;

import com.kh.monopoly.input.Keyboard;
import com.kh.monopoly.player.Player;

public class Tax extends Space {
	private final String name;
	private final int amountInDollar;
	private final BigDecimal amountInPercent;
	private final boolean hasPercentOption;

	private static final Logger logger = Logger.getLogger(Tax.class);

	public Tax(String name, int amountInDollar, int amountInPercent) {
		this.name = name;
		this.amountInDollar = amountInDollar;
		this.amountInPercent = (amountInPercent != -1
				? BigDecimal.valueOf(amountInPercent).divide(BigDecimal.valueOf(100))
				: BigDecimal.valueOf(amountInPercent));
		this.hasPercentOption = (amountInPercent != -1);
	}

	public void applyTo(Player player) {

		String input;
		do {

			// ask the user which option to go with either percentage or dollar
			if (hasPercentOption()) {
				out.println("You have two options pay $" + getAmountInDollar() + " or pay " + getAmountInPercent()
						+ "% of your total worth in cash, properties and buildings.");
				out.println("Bank Balance: $" + player.getCashBalance());
				out.println("type d for dollar or p for percent: ");
				input = Keyboard.read("type d for dollar or p for percent: ", "Error - Invalid Input.");
			} else {
				out.println("You have to pay $" + getAmountInDollar());
				input = "d";
			}

			logger.info("user input (" + input + ")");

			if (input.equals("d")) {
				logger.info("applying " + this);
				out.println("applying " + name());
				player.subtractCash(BigDecimal.valueOf(getAmountInDollar()));
				out.println("Bank Balance: $" + player.getCashBalance());
				break;
			} else if (input.equals("p")) {
				out.println("applying " + name());

				BigDecimal taxOwed = player.getTotalWorth().multiply(getAmountInPercent());

				player.subtractCash(taxOwed);
				out.println("Bank Balance: $" + player.getCashBalance());
				break;
			}

		} while (true);
	}

	@Override
	public String name() {
		return name;
	}

	public int getAmountInDollar() {
		return amountInDollar;
	}

	public boolean hasPercentOption() {
		return hasPercentOption;
	}

	public BigDecimal getAmountInPercent() {

		if (!hasPercentOption())
			throw new UnsupportedOperationException(name() + " does not have a percent option.");

		return amountInPercent;
	}

	@Override
	public String toString() {
		return "Tax [name=" + name + ", amountInDollar=" + amountInDollar + ", amountInPercent=" + amountInPercent
				+ ", hasPercentOption=" + hasPercentOption + "]";
	}

}
