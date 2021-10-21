package org.khoyte.monopoly;

import java.math.BigDecimal;

import org.apache.log4j.Logger;
import org.khoyte.monopoly.board.space.property.deed.Deed;
import org.khoyte.monopoly.board.space.property.deed.IDeed;
import org.khoyte.monopoly.board.space.property.deed.Purchasable;
import org.khoyte.monopoly.board.space.property.deed.RailRoadDeed;
import org.khoyte.monopoly.board.space.property.deed.StreetDeed;
import org.khoyte.monopoly.board.space.property.deed.UtilityDeed;
import org.khoyte.monopoly.player.Player;

public class Bank {

	public static final Logger logger = Logger.getLogger(Bank.class);

	public static final IDeed[] deeds = new Deed[40];

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

//		if (deed instanceof RailRoadDeed) {
//			price = ((RailRoadDeed) deed).price();
//		}
//
//		if (deed instanceof StreetDeed) {
//			price = ((StreetDeed) deed).price();
//		}
//
//		if (deed instanceof UtilityDeed) {
//			price = ((UtilityDeed) deed).price();
//		}

//		if (price == 0) {
//			return false;
//		}

		// charge the player the purchase price
		// if they can afford it
		// and append the properties deed to the
		// end of the players property list
		// and remove the property deed from the
		// deeds list held by the Bank
		if (player.getCashBalance().compareTo(BigDecimal.valueOf(price)) >= 0) {
			System.out.println("current balance: " + player.getCashBalance());
			player.subtractCash(price);
			System.out.println("new balance: " + player.getCashBalance());
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
	static StreetDeed getStreetDeed(int location) throws NullPointerException {

		if (!(deeds[location] instanceof StreetDeed))
			throw new IllegalArgumentException();

		return (StreetDeed) deeds[location];
	}

	/**
	 * 
	 * @param location
	 * @return
	 * @throws IllegalArgumentException
	 */
	static UtilityDeed getUtilityDeed(int location) throws IllegalArgumentException {
		if (!(deeds[location] instanceof UtilityDeed))
			throw new IllegalArgumentException();

		return (UtilityDeed) deeds[location];
	}

	/**
	 * 
	 * @param location
	 * @return
	 * @throws IllegalArgumentException
	 */
	static RailRoadDeed getRailRoadDeed(int location) throws IllegalArgumentException {
		if (!(deeds[location] instanceof RailRoadDeed))
			throw new IllegalArgumentException();

		return (RailRoadDeed) deeds[location];
	}
}
