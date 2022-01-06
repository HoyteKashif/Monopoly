package org.khoyte.monopoly.menu.menuitem.task;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import org.apache.log4j.Logger;
import org.khoyte.monopoly.board.space.property.Street;
import org.khoyte.monopoly.input.Keyboard;
import org.khoyte.monopoly.player.Player;
import org.khoyte.monopoly.shared.ValidationHelper;

import kh.monopoly.Bank;
import kh.monopoly.Board;
import kh.monopoly.Game;

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

		// Do Nothing!
		if (!curPlayer.ownsProperty()) {
			game.print("You have no property eligible for a House.");
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

			if (theirCount == actualCount) {

				// Iterate through the individual properties
				for (Street street : entry.getValue()) {

					if (street.getNumOfHouses() < Bank.MAX_HOUSES_PER_STREET)
						break;

					// Skip groups that have at least one property with a hotel
					if (street.hasHotel()) {
						continue color_group_itr;
					}
				}

				eligibleGroups.add(entry);
			}
		}

		// If there are no Eligible Property Groups
		// then exit the Runnable
		if (eligibleGroups.isEmpty()) {
			return;
		}

		// FIXME
		// missing a check to make sure that the eligible property groups
		// have properties that do not have the maximum number of houses allowed (3)

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
		int inOption = Keyboard.getInstance().readInteger("Select the Color Group, enter an integer value: ",
				Keyboard.INVALID_INPUT, 0, CANCEL);

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

		// Display Current House Distribution
		StringBuilder sbHouseDistribution = new StringBuilder("Current House Distribution:\n");
		String streetInfoFormat = "%s # of Houses %d\n";
		for (int i = 0; i < selectedProperties.size(); i++) {
			Street street = selectedProperties.get(i);
			sbHouseDistribution.append(String.format(streetInfoFormat, street.name(), street.getNumOfHouses()));
		}

		// Print the Options
		game.print(sbHouseDistribution.toString());

		// User's Requested House Distribution
		String[] inHouseDistr;

		// Calculated Cost
		float totalCost;

		house_distr_loop: while (true) {

			// Create an Array to mock the Housing Distribution
			// for the selected group
			int[] aHouseDistr = new int[selectedProperties.size()];
			for (int n = 0; n < aHouseDistr.length; n++) {
				aHouseDistr[n] = selectedProperties.get(n).getNumOfHouses();
			}

			// Initialize Cost
			totalCost = 0.0f;

			String in = Keyboard.getInstance().readLine("Enter comma-separated house distribution (type 0 to cancel):",
					Keyboard.INVALID_INPUT);

			// Cancel House Sale
			if (ValidationHelper.isNumeric(in) && Integer.parseInt(in) == 0) {
				return;
			}

			// Distribution description
			// (1st-property-count,2nd-property-count,3rd-property-count)
			inHouseDistr = in.split(",");

			// Input distribution length does not match number of properties in group
			if (inHouseDistr.length != selectedGroup.getValue().size()) {
				game.print("Invalid Distribution - Amount Length does not match number of properties in group.");
				continue house_distr_loop;
			}

			// Number of requested houses
			int requestedHouses = 0;

			// Mock-up the new House Distribution based on user input
			for (int n = 0; n < inHouseDistr.length && n < aHouseDistr.length; n++) {

				int distrAmount = Integer.parseInt(inHouseDistr[n]);

				if (distrAmount < 0) {
					game.print("Invalid Distribution - Amount must be non-negative.");
					continue house_distr_loop;
				}

				if (distrAmount > Bank.MAX_HOUSES_PER_STREET) {
					game.print("Invalid Distribution - Amount must be less than " + Bank.MAX_HOUSES_PER_STREET);
					continue house_distr_loop;
				}

				requestedHouses += distrAmount;

				aHouseDistr[n] = aHouseDistr[n] + distrAmount;
			}

			// Invalid Distribution
			// Number of requested houses exceeds those available
			if (requestedHouses > Bank.getAvailableHouses()) {
				game.print("Invalid Distribution - requested = " + requestedHouses + " exceeds available = "
						+ Bank.getAvailableHouses());
				continue house_distr_loop;
			}

			// Check for a invalid distribution difference of more than 1
			for (int n = 1; n < aHouseDistr.length; n++) {

				if (aHouseDistr[n] > Bank.MAX_HOUSES_PER_STREET) {
					game.print(
							"Invalid Distribution - Houses per property can not exceed " + Bank.MAX_HOUSES_PER_STREET);
					continue house_distr_loop;
				}

				if (Math.abs(aHouseDistr[n - 1] - aHouseDistr[n]) > 1) {
					game.print("Invalid Distribution - Houses must be distributed evenly");
					continue house_distr_loop;
				}
			}

			// Calculate the cost
			for (int n = 0; n < inHouseDistr.length; n++) {

				int distrAmount = Integer.parseInt(inHouseDistr[n]);

				if (distrAmount == 0)
					continue;

				totalCost += (distrAmount * selectedProperties.get(n).getDeed().houseCost());
			}

			game.print("Total Cost: " + NumberFormat.getCurrencyInstance().format(totalCost));

			// Display Verification Prompt
			String strVerificationPrompt = "";
			strVerificationPrompt += "1: Complete House Purchase\n";
			strVerificationPrompt += "2: Change House Distribution\n";
			strVerificationPrompt += "3: Cancel House Purchase\n";
			strVerificationPrompt += "Enter an integer value: ";

			// Read user's Verification Response
			int inVerification = Keyboard.getInstance().readInteger(strVerificationPrompt, Keyboard.INVALID_INPUT, 1,
					3);

			// Break while-loop and Complete House Distribution
			if (inVerification == 1) {
				break;
			}
			// Go back to beginning of house distribution while-loop
			else if (inVerification == 2) {
				continue house_distr_loop;
			}
			// Cancel
			else {
				return;
			}
		}

		// Charge the user for the new distribution
		curPlayer.subtractCash(totalCost);

		// Complete the transaction
		// Perform the Housing distribution
		for (int i = 0; i < selectedProperties.size(); i++) {
			Street street = selectedProperties.get(i);

			int housesToAdd = Integer.parseInt(inHouseDistr[i]);

			while (housesToAdd-- > 0) {
				street.addHouse();
			}
		}

		// Display New House Distribution
		StringBuilder sbNewHouseDistr = new StringBuilder("Current House Distribution:\n");
		for (int n = 0; n < selectedProperties.size(); n++) {
			Street street = selectedProperties.get(n);
			sbNewHouseDistr.append(street.name()).append(" # of Houses ").append(street.getNumOfHouses()).append('\n');
		}
		game.print(sbNewHouseDistr);

		Thread tPrintPlayerInfo = new Thread(new PrintPlayerInfo());
		tPrintPlayerInfo.start();
		try {
			tPrintPlayerInfo.join();
		} catch (InterruptedException e) {
			LOGGER.error(e.getMessage(), e);
		}
	}

}
