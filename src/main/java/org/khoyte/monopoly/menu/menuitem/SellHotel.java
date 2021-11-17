package org.khoyte.monopoly.menu.menuitem;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import org.apache.log4j.Logger;
import org.khoyte.monopoly.Bank;
import org.khoyte.monopoly.Board;
import org.khoyte.monopoly.Game;
import org.khoyte.monopoly.board.space.property.Street;
import org.khoyte.monopoly.board.space.property.deed.StreetDeed;
import org.khoyte.monopoly.input.Keyboard;
import org.khoyte.monopoly.player.Player;
import org.khoyte.monopoly.shared.ValidationHelper;

/**
 * When you have four houses on each property in a color group, you can buy a
 * hotel. You pay the bank the price listed for hotels on that property card and
 * give the bank the four houses that are on that property.
 * 
 * You can buy hotels one at a time and leave houses on the other properties in
 * the color group. Only one hotel can be bought for each property.
 */
public class SellHotel implements Runnable {

	private static final Logger LOGGER = Logger.getLogger(SellHouse.class);

	@Override
	public void run() {
		LOGGER.info("Running the 'Hotel Sale' Thread");

		Game game = Game.getInstance();
		Player curPlayer = game.getCurrentPlayer();
		Keyboard keyboard = Keyboard.getInstance();

		// Get all properties owned by the Player
		List<StreetDeed> properties = curPlayer.getProperties();

		// Do Nothing!
		if (properties.isEmpty()) {
			game.print("You have no property eligible for a Hotel.");
			return;
		}

		// Find properties with all the properties in a group already purchased
		List<Entry<String, List<Street>>> eligibleGroups = new ArrayList<>();

		// Number of properties within a group owned by the player
		int theirCount;

		// Official number of properties within a group
		int actualCount;

		color_group_itr: for (Entry<String, List<Street>> entry : curPlayer.getGroupedProperties().entrySet()) {

			theirCount = entry.getValue().size();
			actualCount = Board.getStreetGroups().get(entry.getKey()).size();

			// Skip group
			if (theirCount != actualCount)
				continue color_group_itr;

			// Iterate through the individual properties
			for (Street street : entry.getValue()) {

				// Add groups that have at least one property
				// with the maximum number of houses
				// Check if at least 1 property has no hotel
				if (!street.hasHotel() && street.getNumOfHouses() == Bank.MAX_HOUSES_PER_STREET) {
					eligibleGroups.add(entry);
					continue color_group_itr;
				}
			}
		}

		// If there are no Eligible Property Groups
		// then exit the Runnable
		if (eligibleGroups.isEmpty()) {
			return;
		}

		// Initialize the value used to cancel the procedure
		int CANCEL = eligibleGroups.size() + 1;

		StringBuilder sbOptions = new StringBuilder("Eligible Color Groups:\n");

		// Add the Properties to be printed as Options
		for (int i = 0; i < eligibleGroups.size(); i++) {
			sbOptions.append(i + 1).append(": ").append(eligibleGroups.get(i).getKey()).append("\n");
		}

		// Add the option to Cancel the process
		sbOptions.append(CANCEL).append(": Cancel\n");

		// Print the Options
		game.print(sbOptions.toString());

		// Read the user's Property Selection
		int inOption;
		inOption = keyboard.readInteger("Select the Color Group, enter an integer value: ", Keyboard.INVALID_INPUT, 0,
				CANCEL);

		// If they selected to Cancel the process
		// then stop the process
		if (inOption == CANCEL) {
			return;
		}

		// Get the selected Color Group
		final Entry<String, List<Street>> selectedGroup = eligibleGroups.get(inOption - 1);
		final List<Street> selectedProperties = selectedGroup.getValue();

		// Display the selected Color Group
		game.print("Selected Color Group: " + selectedGroup.getKey());

		// Display Current House/Hotel Distribution
		StringBuilder sbHHDistribution = new StringBuilder("Current House/Hotel Distribution:\n");
		String streetInfoFormat = "%s # of Houses %d , has Hotel %s\n";
		for (int i = 0; i < selectedProperties.size(); i++) {
			Street street = selectedProperties.get(i);
			sbHHDistribution
					.append(String.format(streetInfoFormat, street.name(), street.getNumOfHouses(), street.hasHotel()));
		}

		// Print the Options
		game.print(sbHHDistribution.toString());

		// User's Requested Hotel Distribution
		String[] inHotelDistr;

		// Calculated Cost
		float totalCost;

		hotel_distr_loop: while (true) {

			// Create an Array to mock the Hotel Distribution
			// for the selected group
			int[] aHotelDistr = new int[selectedProperties.size()];
			for (int n = 0; n < aHotelDistr.length; n++) {
				aHotelDistr[n] = selectedProperties.get(n).hasHotel() ? 1 : 0;
			}

			// Initialize Cost
			totalCost = 0.0f;

			String in;
			in = keyboard.readLine("Enter comma-separated hotel distribution (type 0 to cancel):",
					Keyboard.INVALID_INPUT);

			// Cancel Hotel Sale
			if (ValidationHelper.isNumeric(in) && Integer.parseInt(in) == 0) {
				return;
			}

			// Distribution description
			// (1st-property-count,2nd-property-count,3rd-property-count)
			inHotelDistr = in.split(",");

			// Input distribution length does not match number of properties in group
			if (inHotelDistr.length != selectedGroup.getValue().size()) {
				game.print("Invalid Distribution - Amount Length does not match number of properties in group.");
				continue hotel_distr_loop;
			}

			// Number of requested hotels
			int requestedHotels = 0;

			// Mock-up the new Hotel Distribution based on user input
			for (int n = 0; n < inHotelDistr.length && n < aHotelDistr.length; n++) {

				int distrAmount = Integer.parseInt(inHotelDistr[n]);

				if (distrAmount < 0 || distrAmount > 1) {
					game.print("Invalid Distribution - Amount must be 1 or 0");
					continue hotel_distr_loop;
				}

				requestedHotels += distrAmount;

				aHotelDistr[n] = aHotelDistr[n] + distrAmount;
			}

			// Invalid Distribution
			// Number of requested hotels exceeds those available
			if (requestedHotels > Bank.getAvailableHotels()) {
				game.print("Invalid Distribution - requested = " + requestedHotels + " exceeds available = "
						+ Bank.getAvailableHotels());
				continue hotel_distr_loop;
			}

			// Check for a invalid distribution difference of more than 1
			for (int n = 1; n < aHotelDistr.length; n++) {

				if (aHotelDistr[n] > 1) {
					game.print("Invalid Distribution - Hotels per property can not exceed 1");
					continue hotel_distr_loop;
				}

				if (Math.abs(aHotelDistr[n - 1] - aHotelDistr[n]) > 1) {
					game.print("Invalid Distribution - Hotels must be distributed evenly");
					continue hotel_distr_loop;
				}
			}

			// Calculate the cost
			for (int n = 0; n < inHotelDistr.length; n++) {

				int distrAmount = Integer.parseInt(inHotelDistr[n]);

				if (distrAmount == 0)
					continue;

				totalCost += (distrAmount * selectedProperties.get(n).getDeed().hotelCost());
			}

			game.print("Total Cost: " + NumberFormat.getCurrencyInstance().format(totalCost));

			// Display Verification Prompt
			String strVerificationPrompt = "";
			strVerificationPrompt += "1: Complete Hotel Purchase\n";
			strVerificationPrompt += "2: Change Hotel Distribution\n";
			strVerificationPrompt += "3: Cancel Hotel Purchase\n";
			strVerificationPrompt += "Enter an integer value: ";

			// Read user's Verification Response
			int inVerification;
			inVerification = keyboard.readInteger(strVerificationPrompt, Keyboard.INVALID_INPUT, 1, 3);

			// Break while-loop and Complete Hotel Distribution
			if (inVerification == 1) {
				break;
			}
			// Go back to beginning of Hotel distribution while-loop
			else if (inVerification == 2) {
				continue hotel_distr_loop;
			}
			// Cancel
			else {
				return;
			}
		} // end hotel_dist_loop

		// Charge the user for the new distribution
		curPlayer.subtractCash(totalCost);

		// Complete the transaction
		// Perform the Hotel distribution
		for (int i = 0; i < selectedProperties.size(); i++) {
			Street street = selectedProperties.get(i);

			int hotelsToAdd = Integer.parseInt(inHotelDistr[i]);

			// Return hotels
			if (hotelsToAdd > 0) {

				street.removeHouses(Bank.MAX_HOUSES_PER_STREET);
				Bank.returnHouses(Bank.MAX_HOUSES_PER_STREET);

				Bank.decAvailableHotels();
				street.addHotel();

			}
		}

		// Display New House/Hotel Distribution
		StringBuilder sbNewDistr = new StringBuilder("Current House/Hotel Distribution:\n");
		for (int n = 0; n < selectedProperties.size(); n++) {
			Street street = selectedProperties.get(n);
			sbNewDistr.append(street.name());
			sbNewDistr.append(" # of Houses ").append(street.getNumOfHouses());
			sbNewDistr.append(", has Hotel ").append(street.hasHotel()).append('\n');
		}
		game.print(sbNewDistr);

		// Print the Player Info
		Thread tPrintPlayerInfo = new Thread(new PrintPlayerInfo());
		tPrintPlayerInfo.start();
		try {
			tPrintPlayerInfo.join();
		} catch (InterruptedException e) {
			LOGGER.error(e.getMessage(), e);
		}
	}

}
