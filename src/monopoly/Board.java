package monopoly;

import com.fasterxml.jackson.databind.JsonNode;

import monopoly.board.space.FreeParking;
import monopoly.board.space.Go;
import monopoly.board.space.GoToJail;
import monopoly.board.space.ISpace;
import monopoly.board.space.Jail;
import monopoly.board.space.chance.Chance;
import monopoly.board.space.communitychest.CommunityChest;
import monopoly.board.space.property.IProperty;
import monopoly.board.space.property.Property;
import monopoly.board.space.property.RailRoad;
import monopoly.board.space.property.Street;
import monopoly.board.space.property.Utility;
import monopoly.board.space.property.deed.StreetDeed;

public class Board {
	public static final Object[] board = new Object[40];

	public static final int[] COMMUNITY_CHEST = { 2, 17, 33 };

	public static final int[] CHANCE = { 7, 22, 36 };

	// used an int array because using a final int is not valid java
	public static final int[] JAIL = new int[1];

	static {
		try {
			JsonNode arrNode = JsonParser.rootJsonArrayNode();

			for (JsonNode node : arrNode) {

				String type = node.findValue("type").asText();
				int space = node.findValue("space").asInt();

				if (type.equals("property")) {
					board[--space] = new Street((StreetDeed) JsonParser.newStreetDeed(node));
				}

				if (type.equals("railroad")) {
					board[--space] = new RailRoad(JsonParser.newRailroadDeed(node));
				}

				if (type.equals("utility")) {
					board[--space] = new Utility(JsonParser.newUtilityDeed(node));
				}

				if (type.equals("chance")) {
					board[--space] = new Chance();
				}

				if (type.equals("community chest")) {
					board[--space] = new CommunityChest();
				}

				// jail/ just waiting
				if (type.equals("jail")) {
					board[--space] = new Jail();
					JAIL[0] = space;
				}

				// actually get sent to jail
				if (type.equals("go to jail")) {
					board[--space] = new GoToJail();
				}

				if (type.equals("start")) {
					board[--space] = new Go();
				}

				if (type.equals("tax")) {
					board[--space] = JsonParser.newTax(node);
				}

				if (type.equals("free parking")) {
					board[--space] = new FreeParking();
				}
			}

		} catch (Exception e) {
			e.printStackTrace(System.err);
			System.exit(-1);
		}

	}

	public static boolean isGoToJail(int i) {
		requireValidLocation(i);
		return board[i] instanceof GoToJail;
	}

	public static boolean isJail(int i) {
		requireValidLocation(i);
		return i == jail();
	}

	public static int jail() {
		return JAIL[0];
	}

	public static void main(String[] args) {
		System.out.println(boardIndexOf(-1));
	}

	public static int boardIndexOf(int rawLocation) {
		// https://www.geeksforgeeks.org/circular-array/
		return rawLocation % board.length;
	}

	public static String getLocationName(int i) {

		Object o = board[i];

		if (o != null) {
			return ((ISpace) o).name();
		}

		return (i) + "";
	}

	public static String toString(int i) {
		if (isProperty(i)) {
			if (isRailRoad(i)) {
				return ((RailRoad) board[i]).toString();
			} else if (isStreet(i)) {
				return ((Street) board[i]).toString();
			} else {
				return ((Utility) board[i]).toString();
			}
		} else {
			return ((ISpace) board[i]).toString();
		}
	}

	public static ISpace getSpace(int i) {
		requireValidLocation(i);
		return (ISpace) board[i];
	}

	public static boolean isProperty(int i) {
		requireValidLocation(i);
		return board[i] instanceof Property;
	}

	public static IProperty getProperty(int i) {
		return get(i, Property.class);
	}

	public static boolean isUtility(int i) {
		requireValidLocation(i);
		return board[i] instanceof Utility;
	}

	public static Utility getUtility(int i) {
		return get(i, Utility.class);
	}

	public static boolean isRailRoad(int i) {
		requireValidLocation(i);
		return board[i] instanceof RailRoad;
	}

	public static RailRoad getRailRoad(int i) {
		return get(i, RailRoad.class);
	}

	public static boolean isStreet(int i) {
		requireValidLocation(i);
		return board[i] instanceof Street;
	}

	public static Street getStreet(int i) {
		return get(i, Street.class);
	}

	public static <T> T get(int i, Class<T> clas) {

		Object o = board[i];
		if (clas.isInstance(o)) {
			return clas.cast(o);
		}
		throw new IllegalArgumentException("space " + i + " is not of type " + clas.getSimpleName());
	}

	public static void requireValidLocation(int i) {

		if (!isValidLocation(i))
			throw new IllegalArgumentException("Invalid location " + i);
	}

	public static boolean isValidLocation(int iSpace) {
		return iSpace >= 0 && iSpace < board.length;
	}

	/**
	 * Is this space on the board the location of Chance
	 * 
	 * @param iSpace
	 * @return
	 */
	public static boolean isChance(int iSpace) {

		for (int i : CHANCE) {
			if (iSpace == i) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Find the location of a Street by the Street's name
	 * 
	 * @param name
	 * @return location of the Street, if value is -1 then the street could not be
	 *         found
	 */
	public static int findStreetByName(String name) {

		for (int i = 0; i < board.length; i++) {
			if (isStreet(i) && ((Street) board[i]).name().equalsIgnoreCase(name)) {
				return i;
			}
		}

		// could not find the street by name
		return -1;
	}

	/**
	 * Move the player to a new space on the board
	 * 
	 * @param player
	 * @param newPosition
	 * @return true if the player lands on or crosses go in the processes, otherwise
	 *         false
	 */
	public static boolean advancePlayer(Player player, int newPosition) {

		int curPosition = player.getPosition();

		player.setPosition(newPosition);

		// did they pass Go?
		if (curPosition > newPosition) {
			return true;
		}

		return false;
	}
	

}
