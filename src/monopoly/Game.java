package monopoly;

import static java.lang.System.exit;
import static java.lang.System.out;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Scanner;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;

import monopoly.board.space.FreeParking;
import monopoly.board.space.Go;
import monopoly.board.space.GoToJail;
import monopoly.board.space.Jail;
import monopoly.board.space.Tax;
import monopoly.board.space.chance.Chance;
import monopoly.board.space.chance.ChanceCard;
import monopoly.board.space.chance.Deck;
import monopoly.board.space.communitychest.CommunityChest;
import monopoly.board.space.property.IProperty;
import monopoly.board.space.property.RailRoad;
import monopoly.board.space.property.Street;
import monopoly.board.space.property.Utility;
import monopoly.board.space.property.deed.IDeed;
import monopoly.board.space.property.deed.RailRoadDeed;
import monopoly.board.space.property.deed.StreetDeed;
import monopoly.board.space.property.deed.UtilityDeed;

public class Game {

	// https://stackoverflow.com/questions/30249324/how-to-get-java-to-wait-for-user-input/30249614
	private Scanner in = new Scanner(System.in);

	public final PlayerQueue playerQueue;

	int double_count = 0;

	Deck chanceCardDeck;

	static {
		try {
			JsonNode arrNode = JsonParser.rootJsonArrayNode();
			for (JsonNode node : arrNode) {

				String type = node.findValue("type").asText();

				// location in any property array
				int idx = node.findValue("space").asInt() - 1;

				if (type.equals("property")) {
					IDeed deed = JsonParser.newStreetDeed(node);
					Bank.deeds[idx] = deed;
					Board.board[idx] = new Street((StreetDeed) deed);
				}

				if (type.equals("railroad")) {
					IDeed deed = JsonParser.newRailroadDeed(node);
					Bank.deeds[idx] = deed;
					Board.board[idx] = new RailRoad((RailRoadDeed) deed);
				}

				if (type.equals("utility")) {
					IDeed deed = JsonParser.newUtilityDeed(node);
					Bank.deeds[idx] = deed;
					Board.board[idx] = new Utility((UtilityDeed) deed);
				}

				if (type.equals("chance")) {
					Board.board[idx] = new Chance();
				}

				if (type.equals("community chest")) {
					Board.board[idx] = new CommunityChest();
				}

				// jail/ just waiting
				if (type.equals("jail")) {
					Board.board[idx] = new Jail();
					Board.JAIL[0] = idx;
				}

				// actually get sent to jail
				if (type.equals("go to jail")) {
					Board.board[idx] = new GoToJail();
				}

				if (type.equals("start")) {
					Board.board[idx] = new Go();
				}

				if (type.equals("tax")) {
					Board.board[idx] = JsonParser.newTax(node);
				}

				if (type.equals("free parking")) {
					Board.board[idx] = new FreeParking();
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	Game() {
		this.playerQueue = new PlayerQueue();
	}

	public void add(Player player) {
		playerQueue.addNode(player);
	}

	public void start() throws JsonParseException, JsonMappingException, IOException {

		// queue up the first player
		playerQueue.advance();

		chanceCardDeck = new Deck(this);

		// game loop
		while (playing())
			;

		quit();
	}

	Player getCurrentPlayer() {
		return playerQueue.currentPlayer.value;
	}

	boolean currentPlayerOnChance() {
		return Board.isChance(getCurrentPlayer().getPosition());
	}

	public void quit() {

		if (in != null) {
			in.close();
			in = null;
		}

		out.println("Thanks for playing!");
		exit(0);
	}

	// - single player loops playing non-stop, until they pass the dice
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

			// allows for easy player position testing
			try {
				int i = Integer.parseInt(input);
				System.out.println("[" + i + "]");
				getCurrentPlayer().setPosition(Integer.parseInt(input));
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
				out.println("Current location: " + Board.toString(getCurrentPlayer().getPosition()));
				out.println("Bank Balance: $" + getCurrentPlayer().getCashBalance());
				ret = true;
			}

			if (input.equals("help")) {
				out.println("options: buy, my info, pass, roll and quit");
				ret = true;
			}

			if (input.equals("quit")) {
				ret = false;
			}

			if (input.equals("roll")) {

				// move the player
				int[] roll = Dice.roll();
				if (roll[0] == roll[1]) {
					double_count++;
				}
				int sum = roll[0] + roll[1];
				getCurrentPlayer().move(sum);

				// check whether the player landed on chance
				if (currentPlayerOnChance()) {
					System.out.println("landed on chance");

					// take a card from the deck
					ChanceCard chanceCard = chanceCardDeck.getNext();
					System.out.println("Chance: " + chanceCard.getDescription());

					// run the action
					chanceCard.action(getCurrentPlayer());

				}

				// handle if they landed on tax space
				if (Board.isTax(getCurrentPlayer().getPosition())) {
					Tax tax = Board.getTax(getCurrentPlayer().getPosition());
					System.out.println("landed on " + tax.name());

					do {
						// ask the user which option to go with either percentage or dollar
						if (tax.hasPercentOption()) {
							out.println("You have two options pay $" + tax.getAmountInDollar() + " or pay "
									+ tax.getAmountInPercent()
									+ "% of your total worth in cash, properties and buildings.");
							out.println("Bank Balance: $" + getCurrentPlayer().getCashBalance());
							out.println("type d for dollar or p for percent: ");
							input = in.nextLine();
						} else {
							out.println("You have to pay $" + tax.getAmountInDollar());
							input = "d";
						}

						if (input.equals("d")) {
							out.println("applying " + tax.name());
							getCurrentPlayer().subtractCash(BigDecimal.valueOf(tax.getAmountInDollar()));
							out.println("Bank Balance: $" + getCurrentPlayer().getCashBalance());
							break;
						} else if (input.equals("p")) {
							out.println("applying " + tax.name());

							BigDecimal taxOwed = getCurrentPlayer().getTotalWorth().multiply(tax.getAmountInPercent());

							getCurrentPlayer().subtractCash(taxOwed);
							out.println("Bank Balance: $" + getCurrentPlayer().getCashBalance());
							break;
						}

					} while (true);
				}

				ret = true;

			}

			if (input.contentEquals("buy") || input.equals("buy property")) {

				int location = getCurrentPlayer().getPosition();

				if (Board.isProperty(location)) {
					IProperty property = Board.getProperty(location);

					out.println(property.name() + " cost $" + property.deed().price());

					out.print("do you want to purchase " + property.deed().name() + "? ");
					input = in.nextLine();

					if (input.equals("y") || input.equals("yes")) {
						if (Bank.purchase(getCurrentPlayer(), location))
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

			// must be last action
			if (Board.isGoToJail(getCurrentPlayer().getPosition())) {
				getCurrentPlayer().setPosition(Board.jail());
			}

		} catch (Exception e) {
			e.printStackTrace(System.err);
			quit();
		}
		return ret;
	}

}