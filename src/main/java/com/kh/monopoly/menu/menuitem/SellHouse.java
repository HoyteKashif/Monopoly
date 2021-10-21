package com.kh.monopoly.menu.menuitem;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import org.apache.log4j.Logger;

import com.kh.monopoly.Board;
import com.kh.monopoly.Game;
import com.kh.monopoly.board.space.property.Street;
import com.kh.monopoly.board.space.property.deed.StreetDeed;
import com.kh.monopoly.player.Player;

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
public class SellHouse implements Runnable {

	private static final Logger LOGGER = Logger.getLogger(SellHouse.class);

	@Override
	public void run() {

		LOGGER.info("Running the 'House Sale' Thread");

		Game game = Game.getInstance();
		Player curPlayer = game.getCurrentPlayer();

		List<StreetDeed> properties = curPlayer.getProperties();
		if (!properties.isEmpty()) {
			do {
				// find properties with all the properties in a group already purchased
				List<Entry<String, List<Street>>> eligibleGroups = new ArrayList<>();
				for (Entry<String, List<Street>> entry : curPlayer.getGroupedProperties().entrySet()) {
					if (entry.getValue().size() == Board.streetColorGroups.get(entry.getKey()).size()) {
						eligibleGroups.add(entry);
					}
				}

				if (eligibleGroups.isEmpty())
					break;

				// list the properties available for the upgrade
				game.print("Eligible properties [");
				for (Entry<String, List<Street>> entry : eligibleGroups) {
					int len = entry.getValue().size();
					String[] a = new String[len];
					for (int i = 0; i < len; i++) {
						a[i] = String.valueOf(entry.getValue().get(i));
					}
					game.print(String.join(", ", a));
				}
				game.print("]");

				break;
			} while (true);
		}

	}

}
