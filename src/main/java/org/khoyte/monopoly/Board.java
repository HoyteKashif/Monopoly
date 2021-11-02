package org.khoyte.monopoly;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.khoyte.monopoly.board.space.GoToJail;
import org.khoyte.monopoly.board.space.ISpace;
import org.khoyte.monopoly.board.space.Tax;
import org.khoyte.monopoly.board.space.property.IProperty;
import org.khoyte.monopoly.board.space.property.Property;
import org.khoyte.monopoly.board.space.property.RailRoad;
import org.khoyte.monopoly.board.space.property.Street;
import org.khoyte.monopoly.board.space.property.Utility;
import org.khoyte.monopoly.board.space.property.deed.StreetDeed;

public class Board {
	public static final Object[] board = new Object[40];

	private static Map<String, List<Street>> streetColorGroups;

	public static final int[] COMMUNITY_CHEST = { 2, 17, 33 };

	public static final int[] CHANCE = { 7, 22, 36 };

	public static final Logger logger = Logger.getLogger(Board.class);

	/**
	 * used an array of type integer because using a final integer is not valid
	 * java, this is found during the building parsing of the JSON which represents
	 * the board
	 * 
	 */
	// FIXME this should just be replaced with a final integer since the value can
	// just
	// be read by me
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
				if (streetColorGroups.containsKey(colorGroup)) {
					streetColorGroups.get(colorGroup).add(street);
				} else {
					List<Street> streets = new ArrayList<>();
					streets.add(street);
					streetColorGroups.put(colorGroup, streets);
				}
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
				if (map.containsKey(colorGroup)) {
					map.get(colorGroup).add(street);
				} else {
					List<Street> streets = new ArrayList<>();
					streets.add(street);
					map.put(colorGroup, streets);
				}
			}
		}
		System.out.println(map.size());
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
	 * Is this space on the board the location of Chance
	 * 
	 * @param iSpace
	 * @return
	 */
//	@Deprecated
	// Replace with player.landedOn
//	public static boolean isChance(int iSpace) {
//
//		for (int i : CHANCE) {
//			if (iSpace == i) {
//				return true;
//			}
//		}
//		return false;
//	}

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
//	public static boolean advancePlayer(Player player, int newPosition) {
//
//		int curPosition = player.getPosition();
//
//		player.setPosition(newPosition);
//
//		// did they pass Go?
//		if (curPosition > newPosition) {
//			return true;
//		}
//
//		return false;
//	}

}
