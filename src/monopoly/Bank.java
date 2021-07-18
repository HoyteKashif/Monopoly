package monopoly;

import com.fasterxml.jackson.databind.JsonNode;

import monopoly.board.space.property.deed.Deed;
import monopoly.board.space.property.deed.IDeed;
import monopoly.board.space.property.deed.RailRoadDeed;
import monopoly.board.space.property.deed.StreetDeed;
import monopoly.board.space.property.deed.UtilityDeed;

public class Bank {

	public static final IDeed[] deeds = new Deed[40];

	static {
		try {
			JsonNode arrNode = JsonParser.rootJsonArrayNode();

			for (JsonNode node : arrNode) {

				String type = node.findValue("type").asText();
				int space = node.findValue("space").asInt();

				if (type.equals("property")) {
					deeds[--space] = JsonParser.newStreetDeed(node);
				}

				if (type.equals("railroad")) {
					deeds[--space] = JsonParser.newRailroadDeed(node);
				}

				if (type.equals("utility")) {
					deeds[--space] = JsonParser.newUtilityDeed(node);
				}

				if (type.equals("chance")) {
					--space;
				}

				if (type.equals("community chest")) {
					--space;
				}

				if (type.equals("jail")) {
					--space;
				}

				// actually get sent to jail
				if (type.equals("go to jail")) {
					--space;
				}

				if (type.equals("start")) {
					--space;
				}

				if (type.equals("tax")) {
					--space;
				}

				if (type.equals("free parking")) {
					--space;
				}
			}

		} catch (Exception e) {
			e.printStackTrace(System.err);
			System.exit(-1);
		}
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

	static boolean purchase(Player player, int location) {

		IDeed deed = deeds[location];

		boolean owned = isOwned(location);

		if (owned)
			return false;

		float price = 0;

		if (deed instanceof RailRoadDeed) {
			price = ((RailRoadDeed) deed).price();
		}

		if (deed instanceof StreetDeed) {
			price = ((StreetDeed) deed).price();
		}

		if (deed instanceof UtilityDeed) {
			price = ((UtilityDeed) deed).price();
		}

		if (price == 0) {
			return false;
		}

		// charge the player the purchase price
		// if they can afford it
		// and append the properties deed to the
		// end of the players property list
		// and remove the property deed from the
		// deeds list held by the Bank
		if (price <= player.bank_balance) {
			System.out.println("current balance: " + player.bank_balance);
			player.bank_balance -= price;
			System.out.println("new balance: " + player.bank_balance);
			player.deeds[location] = deed;
			deeds[location] = null;
			return true;
		}

		return false;
	}

	static StreetDeed getStreetDeed(int location) throws NullPointerException {

		if (!(deeds[location] instanceof StreetDeed))
			throw new IllegalArgumentException();

		return (StreetDeed) deeds[location];
	}

	static UtilityDeed getUtilityDeed(int location) throws IllegalArgumentException {
		if (!(deeds[location] instanceof UtilityDeed))
			throw new IllegalArgumentException();

		return (UtilityDeed) deeds[location];
	}

	static RailRoadDeed getRailRoadDeed(int location) throws IllegalArgumentException {
		if (!(deeds[location] instanceof RailRoadDeed))
			throw new IllegalArgumentException();

		return (RailRoadDeed) deeds[location];
	}
}
