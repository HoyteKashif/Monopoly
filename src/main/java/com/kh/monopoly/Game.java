package com.kh.monopoly;

import static java.lang.System.exit;
import static java.lang.System.out;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Objects;

import org.apache.log4j.Logger;

import com.fasterxml.jackson.databind.JsonNode;
import com.kh.monopoly.board.space.FreeParking;
import com.kh.monopoly.board.space.Go;
import com.kh.monopoly.board.space.GoToJail;
import com.kh.monopoly.board.space.Jail;
import com.kh.monopoly.board.space.chance.Chance;
import com.kh.monopoly.board.space.communitychest.CommunityChest;
import com.kh.monopoly.board.space.property.IProperty;
import com.kh.monopoly.board.space.property.RailRoad;
import com.kh.monopoly.board.space.property.Street;
import com.kh.monopoly.board.space.property.Utility;
import com.kh.monopoly.board.space.property.deed.IDeed;
import com.kh.monopoly.board.space.property.deed.RailRoadDeed;
import com.kh.monopoly.board.space.property.deed.StreetDeed;
import com.kh.monopoly.board.space.property.deed.UtilityDeed;
import com.kh.monopoly.input.Keyboard;
import com.kh.monopoly.menu.MenuFactory;
import com.kh.monopoly.menu.TextMenu;
import com.kh.monopoly.player.Player;
import com.kh.monopoly.player.PlayerQueue;

public class Game {

	static final Logger logger = Logger.getLogger(Game.class);

//	private Scanner in = new Scanner(System.in);

	public Keyboard keyboard = new Keyboard();

	private final PlayerQueue playerQueue;

	int double_count = 0;

//	public Deck chanceCardDeck;

	static {
		try {
			JsonNode arrNode = JsonParser.rootJsonArrayNode();
			for (JsonNode node : arrNode) {

				String type = node.findValue("type").asText();

				// location in any property array
				int idx = node.findValue("space").asInt() - 1;

				if (type.equals("property")) {
					IDeed deed = JsonParser.newStreetDeed(node);
					Bank.insertDeed(idx, deed);
					Board.insertSpace(idx, new Street((StreetDeed) deed));
				}

				if (type.equals("railroad")) {
					IDeed deed = JsonParser.newRailroadDeed(node);
					Bank.insertDeed(idx, deed);
					Board.insertSpace(idx, new RailRoad((RailRoadDeed) deed));
				}

				if (type.equals("utility")) {
					IDeed deed = JsonParser.newUtilityDeed(node);
					Bank.insertDeed(idx, deed);
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
			logger.error(e.getMessage(), e);
		}
	}

	private Game() {
		this.playerQueue = new PlayerQueue();
	}

	private static Game instance;

	public static Game getInstance() {

		if (instance == null)
			instance = new Game();

		return instance;
	}

	public PlayerQueue getPlayerQueue() {
		return playerQueue;
	}

	public int getDoubleCount() {
		return double_count;
	}

	public void incrementDoubleCount() {
		logger.info("Increment Double Count");
		double_count++;
	}

	public void add(Player player) {
		logger.info("Add " + player);
		playerQueue.addNode(player);
	}

	public void start() {
		logger.info("start new game");

		// queue up the first player
		playerQueue.advance();

		// chanceCardDeck = new Deck(this);

		TextMenu mainMenu = MenuFactory.getMainMenu();
		mainMenu.run();

		// game loop
//		while (playing())
//			;

		quit();
	}

	public Player getCurrentPlayer() {
		return playerQueue.getPlayer();
	}

//	public boolean currentPlayerOnChance() {
//		return Board.isChance(getCurrentPlayer().getPosition());
//	}

	public enum UserAction {
		pass(1, "pass"), show_user_info(2, "my info"), show_help(3, "help"), quit_game(4, "quit"), roll(5, "roll"),
		execute_property_sale(6, "buy property"), execute_house_sale(7, "buy house"),
		execute_hotel_sale(8, "buy hotel");

		final String description;
		final int actionID;

		UserAction(int actionID, String description) {
			this.actionID = actionID;
			this.description = description;
		}

		static UserAction findByID(int i) {
			for (UserAction eAction : values()) {
				if (i == eAction.actionID) {
					return eAction;
				}
			}
			return null;
		}

		static UserAction findByDescription(String description) {
			for (UserAction eAction : values()) {
				if (description.equals(eAction.description)) {
					return eAction;
				}
			}
			return null;
		}

	}

//	public boolean sendPlayerToJail() {
//		return Board.isGoToJail(getCurrentPlayer().getPosition());
//	}

//	public int getOption(String input) {
//		if (input.equals("pass")) {
//			return 1;
//		}
//
//		if (input.equals("my info")) {
//			return 2;
//		}
//
//		if (input.equals("help")) {
//			return 3;
//		}
//
//		if (input.equals("quit")) {
//			return 4;
//		}
//
//		if (input.equals("roll")) {
//			return 5;
//		}
//
//		if (input.equals("buy property")) {
//			return 6;
//		}
//
//		if (input.equals("buy house")) {
//			return 7;
//		}
//
//		if (input.equals("buy hotel")) {
//			return 8;
//		}
//
//		if (isNumeric(input)) {
//			return Integer.parseInt(input);
//		}
//
//		return loop_prompt_option;
//	}

	public static boolean isNumeric(String str) {
		try {
			Integer.parseInt(str);
			return true;
		} catch (NumberFormatException e) {
		}
		return false;
	}

	public static final int loop_prompt_option = 0;

	/**
	 * Reset the count of how many doubles have been rolled back to 0
	 */
	public void resetDoubleCounter() {
		double_count = 0;
	}

	// - single player loops playing non-stop, until they pass the dice
	public boolean playing() {

		String input = null;
		int option = loop_prompt_option;
//		String prompt = "M E N U\n";
//		prompt += "=============\n";
//		prompt += "1. pass\n";
//		prompt += "2. my info\n";
//		prompt += "3. help\n";
//		prompt += "4. quit\n";
//		prompt += "5. roll\n";
//		prompt += "6. buy property\n";
//		prompt += "7. buy House\n";
//		prompt += "8. buy Hotel\n";
//		prompt += "=============\n";
//		prompt += "Enter choice: ";

		TextMenu mainMenu = MenuFactory.getMainMenu();
		mainMenu.run();

//		do {
//			input = keyboard.readLine(prompt, "Error - Invalid Input");
//		} while ((option = getOption(input)) == loop_prompt_option);

		boolean ret = false;
		try {

			// Pass Dice
			// XXX replaced with Runnable
			if (option == 1) {
				// reset the double count for the next user
				// double_count = 0;
				// playerQueue.advance();
				ret = true;
			}

			// my info
			// XXX replaced with Runnable
			if (option == 2) {
				// printPlayerInfo();
				ret = true;
			}

			// help
			// XXX replaced with Looping of the Menu
			if (option == 3) {
				// printMenu(0);
				ret = true;
			}

			// quit
			// XXX replaced with Runnable
			if (option == 4) {
				// quit();
				ret = false;
			}

			// roll
			// XXX replaced with Runnable
			if (option == 5) {
				// performRoll();
				ret = true;
			}

			// buy property
			// XXX replaced with Runnable
			if (option == 6) {
				// performPropertySale();
				ret = true;
			}

			// buy house
			if (option == 7) {
				// performHouseSale();
				ret = true;
			}

			/**
			 * When you have four houses on each property in a color group, you can buy a
			 * hotel. You pay the bank the price listed for hotels on that property card and
			 * give the bank the four houses that are on that property.
			 * 
			 * You can buy hotels one at a time and leave houses on the other properties in
			 * the color group. Only one hotel can be bought for each property.
			 */
			// buy hotel
			if (option == 8) {
				ret = true;
			}

			// must be last action
			// XXX Placed in both the Roll-Runnable and Player
			// if (Board.isGoToJail(getCurrentPlayer().getPosition())) {
			// getCurrentPlayer().setPosition(Board.jail());
			// }

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			// XXX Replace with Runnable
			// quit();
		}
		return ret;
	}

	@Deprecated
	// Use Runnable
	private void printMenu(int menu) {
		if (menu == 0)
			print("menu: buy, my info, pass, roll and quit");
	}

	@Deprecated
	// Use Runnable
	private void printPlayerInfo() {
		print("Current location: " + Board.toString(getCurrentPlayer().getPosition()));
		print("Bank Balance: $" + getCurrentPlayer().getCashBalance());

		List<StreetDeed> lstOwnedProperties = getCurrentPlayer().getProperties();
		String[] properties = new String[lstOwnedProperties.size()];
		for (int i = 0; i < properties.length; i++) {
			StreetDeed deed = lstOwnedProperties.get(i);
			properties[i] = deed.name() + "(" + deed.colorGroup() + ")";
		}
		print("Owned Properties: [" + String.join(", ", properties) + "]");
	}

	// FIXME
	// This is a static class, no need to do this
	public void print(Object obj) {
		out.println(Objects.toString(obj));
	}

	@Deprecated
	// Use Runnable
	private void performPropertySale() {

		int location = getCurrentPlayer().getPosition();

		if (Board.isProperty(location)) {
			IProperty property = Board.getProperty(location);
			print(property);

			String input = keyboard.readLine("Do you want to purchase " + property.deed().name() + "? ",
					"Error - Invalid Input.");

			if (input.equals("y") || input.equals("yes")) {
				if (Bank.purchase(getCurrentPlayer(), location))
					print("Purchase was successful!");
				else
					print("Purchase was unsuccessful!");
			} else {
				print("answer=(" + input + ")");
			}

		} else {
			print(Board.getLocationName(location) + " cannot be purchased");
		}
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
	@Deprecated
	// Replaced with Runnable
	private void performHouseSale() {

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
				print("Eligible properties [");
				for (Entry<String, List<Street>> entry : eligibleGroups) {
					int len = entry.getValue().size();
					String[] a = new String[len];
					for (int i = 0; i < len; i++) {
						a[i] = String.valueOf(entry.getValue().get(i));
					}
					print(String.join(", ", a));
				}
				print("]");

				break;
			} while (true);
		}

	}

	public void quit() {
		logger.info("quit");
		print("Goodbye!");
		exit(0);
	}
}