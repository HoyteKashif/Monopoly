package monopoly;

import static java.lang.System.exit;
import static java.lang.System.out;

import java.io.IOException;
import java.util.Scanner;
import java.util.Stack;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

import monopoly.board.space.chance.ChanceCard;
import monopoly.board.space.chance.Deck;
import monopoly.board.space.property.IProperty;

class Game {

	// https://stackoverflow.com/questions/30249324/how-to-get-java-to-wait-for-user-input/30249614
	private Scanner in = new Scanner(System.in);

	PlayerQueue playerQueue;

	int double_count = 0;

	Stack<ChanceCard> chanceCardStack;

	Game() {
	}

	public void add(Player player) {

		if (playerQueue == null) {
			playerQueue = new PlayerQueue();
		}

		playerQueue.addNode(player);
	}

	public void start() throws JsonParseException, JsonMappingException, IOException {

		// queue up the first player
		playerQueue.advance();

		chanceCardStack = Deck.newStack();

		// game loop
		while (playing())
			;

		quit();
	}

	Player currentPlayer() {
		return playerQueue.currentPlayer.value;
	}

	public void quit() {

		if (in != null) {
			in.close();
			in = null;
		}

		out.println("Thanks for playing!");
		exit(0);
	}

	public ChanceCard nextChanceCard() {

		if (chanceCardStack == null || chanceCardStack.isEmpty()) {
			chanceCardStack = Deck.newStack();
		}

		return chanceCardStack.pop();
	}

	public boolean playing() {
		System.out.println("your move?");

		boolean ret = false;
		try {

			String input = null;
			while (input == null || input.isEmpty()) {
				input = in.nextLine();
			}

			input = input.trim();

			if (input.isEmpty()) {
				ret = true;
			}

			try {
				int i = Integer.parseInt(input);
				System.out.println("[" + i + "]");
				currentPlayer().setPosition(Integer.parseInt(input));
				ret = true;
			} catch (NumberFormatException e) {
			}

			if (input.equals("pass")) {
				// reset the double count for the next user
				double_count = 0;
				playerQueue.advance();
				ret = true;
			}

			if (input.equals("my info")) {
				out.println("Current location: " + Board.toString(currentPlayer().getPosition()));
				out.println("Bank Balance: $" + currentPlayer().bank_balance);
				ret = true;
			}

			if (input.equals("help")) {
				out.println("options: buy, my info, roll and quit");
				ret = true;
			}

			if (input.equals("quit")) {
				ret = false;
			}

			if (input.equals("roll")) {
				int[] roll = Dice.roll();
				if (roll[0] == roll[1]) {
					double_count++;
				}
				int sum = roll[0] + roll[1];
				currentPlayer().setPosition(currentPlayer().getPosition() + sum);
				ret = true;

				// TODO if the player rolls three sets of doubles, send the user to Jail

				// check whether the player landed on chance
				if (Board.isChance(currentPlayer().getPosition())) {
					System.out.println("landed on chance");

					// take a card from the deck
					ChanceCard chanceCard = nextChanceCard();
					System.out.println("Chance: " + chanceCard.getDescription());

					// run the action
					chanceCard.action(currentPlayer());

				}

			}

			if (input.contentEquals("buy") || input.equals("buy property")) {

				int location = currentPlayer().getPosition();

				if (Board.isProperty(location)) {
					IProperty property = Board.getProperty(location);

					out.println(property.name() + " cost $" + property.deed().price());

					out.print("do you want to purchase " + property.deed().name() + "? ");
					input = in.nextLine();

					if (input.equals("y") || input.equals("yes")) {
						if (Bank.purchase(currentPlayer(), location))
							out.println("purchase was successful!");
						else
							out.println("purchase was unsuccessful!");
					} else {
						out.println("answer=(" + input + ")");
					}

				} else {
					out.println(Board.getLocationName(location) + " cannot be purchased");
				}
				ret = true;

			}

			if (Board.isGoToJail(currentPlayer().getPosition())) {
				currentPlayer().setPosition(Board.jail());
			}

		} catch (Exception e) {
			e.printStackTrace(System.err);
			quit();
		}
		return ret;
	}

}