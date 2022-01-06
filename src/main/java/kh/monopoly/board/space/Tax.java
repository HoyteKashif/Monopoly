package kh.monopoly.board.space;

import java.math.BigDecimal;

import org.apache.log4j.Logger;

import kh.monopoly.Game;
import kh.monopoly.input.Keyboard;
import kh.monopoly.player.Player;

public class Tax extends Space {
	private final String name;
	private final int amountInDollar;
	private final BigDecimal amountInPercent;
	private final boolean hasPercentOption;

	private static final Logger logger = Logger.getLogger(Tax.class);

	public Tax(String name, int amountInDollar, int amountInPercent) {
		this.name = name;
		this.amountInDollar = amountInDollar;
		this.amountInPercent = getAmountInPercent(amountInPercent);
		this.hasPercentOption = (amountInPercent != -1);
	}

	private static BigDecimal getAmountInPercent(int amountInPercent) {
		return (amountInPercent != -1 ? BigDecimal.valueOf(amountInPercent).divide(BigDecimal.valueOf(100))
				: BigDecimal.valueOf(amountInPercent));
	}

	public void applyTo(Player player) {

		Game game = Game.getInstance();

		String input;
		do {

			// Ask the user which option to go with either percentage or dollar
			if (hasPercentOption()) {
				game.print("You have two options pay $" + getAmountInDollar() + " or pay " + getAmountInPercent()
						+ "% of your total worth in cash, properties and buildings.");
				game.print("Bank Balance: $" + player.getCashBalance());
				game.print("Type d for dollar or p for percent: ");
				input = Keyboard.read("type d for dollar or p for percent: ", "Error - Invalid Input.");
			} else {
				game.print("You have to pay $" + getAmountInDollar());
				input = "d";
			}

			// Perform the User's chosen Action
			if (input.equals("d")) {
				logger.info("Applying " + this);
				game.print("Applying " + name());
				player.subtractCash(BigDecimal.valueOf(getAmountInDollar()));
				game.print("Bank Balance: $" + player.getCashBalance());
				break;
			} else if (input.equals("p")) {
				game.print("applying " + name());

				BigDecimal taxOwed = player.getTotalWorth().multiply(getAmountInPercent());

				player.subtractCash(taxOwed);
				game.print("Bank Balance: $" + player.getCashBalance());
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
