package kh.monopoly;

import static java.lang.System.exit;
import static java.lang.System.out;

import java.io.IOException;
import java.util.Objects;

import org.apache.log4j.Logger;
import org.khoyte.monopoly.board.space.FreeParking;
import org.khoyte.monopoly.board.space.Go;
import org.khoyte.monopoly.board.space.GoToJail;
import org.khoyte.monopoly.board.space.Jail;
import org.khoyte.monopoly.board.space.chance.Chance;
import org.khoyte.monopoly.board.space.communitychest.CommunityChest;
import org.khoyte.monopoly.board.space.property.RailRoad;
import org.khoyte.monopoly.board.space.property.Street;
import org.khoyte.monopoly.board.space.property.Utility;
import org.khoyte.monopoly.board.space.property.deed.IDeed;
import org.khoyte.monopoly.board.space.property.deed.RailRoadDeed;
import org.khoyte.monopoly.board.space.property.deed.StreetDeed;
import org.khoyte.monopoly.board.space.property.deed.UtilityDeed;
import org.khoyte.monopoly.menu.MenuFactory;
import org.khoyte.monopoly.menu.TextMenu;
import org.khoyte.monopoly.player.Player;
import org.khoyte.monopoly.player.PlayerQueue;

import com.fasterxml.jackson.databind.JsonNode;

public class Game {

	static final Logger logger = Logger.getLogger(Game.class);

	private final PlayerQueue playerQueue;

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

		} catch (IOException e) {
			logger.error(e.getMessage(), e);
		}
	}

	public static Game getInstance() {

		if (instance == null)
			instance = new Game();

		return instance;
	}

	private Game() {
		this.playerQueue = new PlayerQueue();
	}

	private static Game instance;

	public PlayerQueue getPlayerQueue() {
		return playerQueue;
	}

	public void add(Player player) {
		logger.info("Add " + player);
		playerQueue.addNode(player);
	}

	public void start() {
		logger.info("start new game");

		// queue up the first player
		playerQueue.advance();

		TextMenu mainMenu = MenuFactory.getMainMenu();
		mainMenu.run();

		quit();
	}

	public Player getCurrentPlayer() {
		return playerQueue.getPlayer();
	}

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

	public static boolean isNumeric(String str) {
		try {
			Integer.parseInt(str);
			return true;
		} catch (NumberFormatException e) {
		}
		return false;
	}

	public static final int loop_prompt_option = 0;

	public void print(Object obj) {
		out.println(Objects.toString(obj));
	}

	public void quit() {
		logger.info("quit");
		print("Goodbye!");
		exit(0);
	}
}