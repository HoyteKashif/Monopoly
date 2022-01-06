package kh.monopoly;

import java.math.BigDecimal;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.log4j.Logger;

import kh.monopoly.board.space.property.deed.Deed;
import kh.monopoly.board.space.property.deed.IDeed;
import kh.monopoly.board.space.property.deed.Purchasable;
import kh.monopoly.board.space.property.deed.RailRoadDeed;
import kh.monopoly.board.space.property.deed.StreetDeed;
import kh.monopoly.board.space.property.deed.UtilityDeed;
import kh.monopoly.player.Player;

public class Bank {

	public static final Logger logger = Logger.getLogger(Bank.class);

	public static final IDeed[] deeds = new Deed[40];

	/**
	 * The maximum number of purchasable hotels in the game
	 */
	public static final int MAX_NUMBER_OF_HOTELS_IN_GAME = 12;

	/**
	 * the maximum number of purchasable houses in the game
	 */
	public static final int MAX_NUMBER_OF_HOUSES_IN_GAME = 32;

	/**
	 * the maximum number of houses allowed per Street
	 */
	public static final int MAX_HOUSES_PER_STREET = 4;

	private static AtomicInteger available_houses = new AtomicInteger(MAX_NUMBER_OF_HOUSES_IN_GAME);

	private static AtomicInteger available_hotels = new AtomicInteger(MAX_NUMBER_OF_HOTELS_IN_GAME);

	public static void returnHouses(int amount) {
		available_houses.addAndGet(amount);
	}

	public static void decAvailableHouses() {
		available_houses.decrementAndGet();
	}

	public static void incAvailableHouses() {
		available_houses.incrementAndGet();
	}

	public static int getAvailableHouses() {
		return available_houses.intValue();
	}

	public static void returnHotels(int amount) {
		available_hotels.addAndGet(amount);
	}

	public static void decAvailableHotels() {
		available_hotels.decrementAndGet();
	}

	public static void incAvailableHotels() {
		available_hotels.incrementAndGet();
	}

	public static int getAvailableHotels() {
		return available_hotels.intValue();
	}

	/**
	 * Already Owned
	 * 
	 * @param location
	 * @return true is already owned
	 */
	public static boolean isOwned(int location) {
		return deeds[location] == null;
	}

	public static void insertDeed(int index, IDeed deed) {
		logger.info("insert  " + deed + " at position " + index);
		Bank.deeds[index] = deed;
	}

	// FIXME
	// Broken - Situation of player purchasing already owned property is not handled
	// properly
	public static boolean purchase(Player player, int location) {
		logger.info("purchase location=" + location + " by " + player);

		IDeed deed = deeds[location];

		boolean owned = isOwned(location);

		if (owned)
			return false;

		float price = 0;

		if (deed instanceof Purchasable) {
			price = deed.price();
		} else {
			return false;
		}

		// Charge the player the purchase price
		// if they can afford it
		// and append the properties deed to the
		// end of the players property list
		// and remove the property deed from the
		// deeds list held by the Bank
		if (player.getCashBalance().compareTo(BigDecimal.valueOf(price)) >= 0) {
			System.out.println("Current balance: " + player.getCashBalance());
			player.subtractCash(price);
			System.out.println("New balance: " + player.getCashBalance());
			player.getDeeds()[location] = deed;
			deeds[location] = null;
			return true;
		}

		return false;
	}

	/**
	 * 
	 * @param location
	 * @return
	 * @throws NullPointerException
	 */
	public static StreetDeed getStreetDeed(int location) throws NullPointerException {
		return cast(deeds[location], StreetDeed.class);
	}

	/**
	 * 
	 * @param location
	 * @return
	 * @throws IllegalArgumentException
	 */
	public static UtilityDeed getUtilityDeed(int location) throws IllegalArgumentException {
		return cast(deeds[location], UtilityDeed.class);
	}

	/**
	 * 
	 * @param location
	 * @return
	 * @throws IllegalArgumentException
	 */
	public static RailRoadDeed getRailRoadDeed(int location) throws IllegalArgumentException {
		return cast(deeds[location], RailRoadDeed.class);
	}

	private static <D extends IDeed> D cast(Object obj, Class<D> cls) {

		if (cls.isInstance(obj))
			return cls.cast(obj);

		throw new IllegalArgumentException();

	}
}
