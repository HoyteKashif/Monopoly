package monopoly;

import static java.lang.System.exit;
import static java.lang.System.out;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
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
			Board.initColorGrouping();

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

		Bank.purchase(getCurrentPlayer(), 2);
		Bank.purchase(getCurrentPlayer(), 4);
		
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
				out.print("Owned Properties: [");
				List<String> properties = new ArrayList<>();
				for (IDeed deed : getCurrentPlayer().deeds) {
					if (deed instanceof StreetDeed) {
						StreetDeed streetDeed = (StreetDeed) deed;
						properties.add(streetDeed.name() + "(" + streetDeed.colorGroup() + ")");
					}
				}
				out.print(String.join(", ", properties));
				out.println("]");

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

				out.println("new location " + Board.getLocationName(getCurrentPlayer().getPosition()));
			}

			if (input.equals("buy property")) {

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

			/**
			 * The game has only 32 houses and 12 hotels. When the buildings have been
			 * purchased and are in use in the game, the rules say that no more houses and
			 * hotels can be bought. This building moratorium can end if a player goes
			 * bankrupt and return houses and hotels to the bank, chooses to sell them back
			 * to the bank, or buys a hotel an returns the houses on the property to the
			 * bank. <br>
			 * <br>
			 * Once the bank has these returned houses and hotels, they are available to be
			 * bought by any player for their original price. A bidding war can ensue if
			 * more than one player wants them, with the buildings going to the highest
			 * bidder.
			 */

			/**
			 * For instance, you can only buy houses on the yellow color group—Atlantic
			 * Avenue, Ventnor Avenue, and Marvin Gardens—when you own all three properties
			 * and none is mortgaged<br>
			 * <br>
			 * A key rule is that you must place houses evenly on your property. If you buy
			 * one house and put it on one property, the next house you buy for that group
			 * must go on another property, and so on. If you buy three houses at once for a
			 * color group with three properties, you must put one house on each of the
			 * three properties rather than, say, three houses on one property.
			 */
			boolean run = true;
			if (run || input.equals("buy house")) {

				List<StreetDeed> properties = getCurrentPlayer().getProperties();
				if (!properties.isEmpty()) {
					do {
						// find properties with all the properties in a group already purchased
						List<Entry<String, List<Street>>> eligibleGroups = new ArrayList<>();
						for (Entry<String, List<Street>> entry : getCurrentPlayer().getGroupedProperties().entrySet()) {
							if (entry.getValue().size() == Board.streetColorGroups.get(entry.getKey()).size()) {
								eligibleGroups.add(entry);
							}
						}

						if (eligibleGroups.isEmpty())
							break;

						// list the properties available for the upgrade
						out.println("eligible properties [");
						for (Entry<String, List<Street>> entry : eligibleGroups) {
							int len = entry.getValue().size();
							for (int i = 0; i < len; i++) {
								out.print(entry.getValue().get(i));
								if (i + 1 < len) {
									out.print(", ");
								}
							}
							out.println();
						}
						out.println("]");

						break;
					} while (true);
				}

			}

			/**
			 * When you have four houses on each property in a color group, you can buy a
			 * hotel. You pay the bank the price listed for hotels on that property card and
			 * give the bank the four houses that are on that property.
			 * 
			 * You can buy hotels one at a time and leave houses on the other properties in
			 * the color group. Only one hotel can be bought for each property.
			 */
			if (input.equals("buy hotel")) {

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