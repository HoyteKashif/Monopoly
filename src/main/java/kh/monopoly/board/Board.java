package kh.monopoly.board;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import kh.monopoly.board.space.GoToJail;
import kh.monopoly.board.space.ISpace;
import kh.monopoly.board.space.Space;
import kh.monopoly.board.space.Tax;
import kh.monopoly.board.space.property.IProperty;
import kh.monopoly.board.space.property.Property;
import kh.monopoly.board.space.property.RailRoad;
import kh.monopoly.board.space.property.Street;
import kh.monopoly.board.space.property.Utility;
import kh.monopoly.board.space.property.deed.StreetDeed;
import kh.monopoly.player.Player;

public class Board {
	public static final Object[] board = new Object[40];

	private static Map<String, List<Street>> streetColorGroups;

	public static final int[] COMMUNITY_CHEST = { 2, 17, 33 };

	public static final int[] CHANCE = { 7, 22, 36 };

	public static final Logger logger = Logger.getLogger(Board.class);

	/**
	 * Advance to go and Collect $200
	 * 
	 * @param player
	 */
	public static void advanceToGo(Player player) {
		player.setPosition(0);
		player.addCash(200);
	}

//	public static void advanceToSpace(Player player) {
//
//		int curPosition = player.getPosition();
//		int newPosition = Board.findStreetByName("St. Charles Place");
//
//		player.setPosition(newPosition);
//
//		// If they pass Go then they also collect $200
//		boolean passedGo = curPosition > newPosition;
//
//		if (passedGo) {
//			player.addCash(200);
//		}
//
//		advanceToSpace(player, Board.findStreet("St. Charles Place"), curPosition > newPosition);
//	}

	/**
	 * If they pass Go then they also collect $200
	 * 
	 * @param player
	 * @param newPosition
	 * @param passedGo
	 */
	public static void advanceToSpace(Player player, ISpace space, boolean allowedToPassGoAndCollect200) {

		int curPosition = player.getPosition();
		int newPosition = Board.findPosition(space);

		boolean passedGo = curPosition > newPosition;
		if (passedGo && allowedToPassGoAndCollect200) {
			player.addCash(200);
		}

		player.setPosition(newPosition);

	}

	/**
	 * used an array of type integer because using a final integer is not valid
	 * java, this is found during the building parsing of the JSON which represents
	 * the board
	 * 
	 */
	// FIXME this should just be replaced with a final integer since the value can
	// just be read by me
	public static final int[] JAIL = new int[1];

	public static void insertSpace(int index, ISpace space) {
		logger.info("insert  " + space + " at position " + index);
		Board.board[index] = space;
	}

	public static Map<String, List<Street>> getStreetGroups() {

		if (streetColorGroups == null) {
			initColorGrouping();
		}

		return streetColorGroups;
	}

	private static void initColorGrouping() {

		streetColorGroups = new HashMap<>();

		for (int i = 0; i < board.length; i++) {

			if (isStreet(i)) {
				Street street = getStreet(i);
				String colorGroup = ((StreetDeed) street.deed()).colorGroup();

				if (!streetColorGroups.containsKey(colorGroup))
					streetColorGroups.put(colorGroup, new ArrayList<>());

				streetColorGroups.get(colorGroup).add(street);
			}

		}

		streetColorGroups = Collections.unmodifiableMap(streetColorGroups);
	}

	public static Map<String, List<Street>> getStreetsByColorGroup() {

		Map<String, List<Street>> map = new HashMap<>();
		for (int i = 0; i < board.length; i++) {

			if (isStreet(i)) {
				Street street = getStreet(i);
				String colorGroup = ((StreetDeed) street.deed()).colorGroup();

				if (!map.containsKey(colorGroup))
					map.put(colorGroup, new ArrayList<>());

				map.get(colorGroup).add(street);
			}

		}
		return map;
	}

	public static boolean isGoToJail(int i) {
		requireValidLocation(i);
		return board[i] instanceof GoToJail;
	}

	public static boolean isJail(int i) {
		requireValidLocation(i);
		return i == getJailPosition();
	}

	public static int getJailPosition() {
		return JAIL[0];
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

	/**
	 * Determine whether the given space, found by it's position value, is of type
	 * Tax
	 * 
	 * @param i position of the space
	 * @return true if the space is of type Tax, false otherwise
	 */
	public static boolean isTax(int i) {
		return board[i] instanceof Tax;
	}

	public static Tax getTax(int i) {
		requireValidLocation(i);
		return get(i, Tax.class);
	}

	public static boolean isRailRoad(int i) {
		requireValidLocation(i);
		return board[i] instanceof RailRoad;
	}

	public static RailRoad getRailRoad(int i) {
		return get(i, RailRoad.class);
	}

	/**
	 * Determine whether the given space, found by it's position value
	 * 
	 * @param i position of the space to get
	 * @return boolean true if the space is of type Street, false otherwise
	 */
	public static boolean isStreet(int i) {
		requireValidLocation(i);
		return board[i] instanceof Street;
	}

	/**
	 * Get the Street Wrapper for the given space, found by it's position value
	 * 
	 * @param i position of the space to get
	 * @return Street
	 */
	public static Street getStreet(int i) {
		return get(i, Street.class);
	}

	/**
	 * Get the Object Wrapper for the Space, found by it's position value
	 * 
	 * @param <T>
	 * @param i     position of space to get
	 * @param clazz space wrapper class for the space
	 * @return Object returned as the Space Wrapper object
	 */
	public static <T> T get(int i, Class<T> clazz) {

		Object o = board[i];
		if (clazz.isInstance(o)) {
			return clazz.cast(o);
		}
		throw new IllegalArgumentException("space " + i + " is not of type " + clazz.getSimpleName());
	}

	public static void requireValidLocation(int i) {

		if (!isValidLocation(i))
			throw new IllegalArgumentException("Invalid location " + i);
	}

	public static boolean isValidLocation(int iSpace) {
		return iSpace >= 0 && iSpace < board.length;
	}

	/**
	 * Find the location of a Street by the Street's name
	 * 
	 * @param name
	 * @return location of the Street, if value is -1 then the street could not be
	 *         found
	 */
	private static int findStreetByName(String name) {

		for (int i = 0; i < board.length; i++) {
			if (isStreet(i) && ((Street) board[i]).name().equalsIgnoreCase(name)) {
				return i;
			}
		}

		// could not find the street by name
		return -1;
	}

	public static int findPosition(ISpace space) {
		for (int i = 0; i < board.length; i++) {
			if (space.equals(board[i])) {
				return i;
			}
		}
		return -1;
	}

	public static int findStreetPosition(Street street) {
		for (int i = 0; i < board.length; i++) {
			if (street == board[i]) {
				return i;
			}
		}
		return -1;
	}

	/**
	 * Could not find the street by name
	 * 
	 * @param name
	 * @return Street
	 */
	public static Street findStreet(String name) {

		Street street;
		for (int i = 0; i < board.length; i++) {

			if (!isStreet(i))
				continue;

			street = (Street) board[i];

			if (street.name().equalsIgnoreCase(name))
				return street;

		}

		return null;
	}

}
