package kh.monopoly.player;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import kh.monopoly.Bank;
import kh.monopoly.board.Board;
import kh.monopoly.board.space.Space;
import kh.monopoly.board.space.chance.GetOutOfJailFree;
import kh.monopoly.board.space.property.Street;
import kh.monopoly.board.space.property.deed.IDeed;
import kh.monopoly.board.space.property.deed.StreetDeed;

public class Player {

	private static final Logger logger = Logger.getLogger(Player.class);

	// start at go
	private int position = 0;

	// initial bank balance is $1500
	private BigDecimal cashBalance = BigDecimal.valueOf(1500);

	private final IDeed[] deeds;

	private final String name;

	private boolean inJail;

	private GetOutOfJailFree getOutOfJailFreeCard;

	public Player(String name) {
		this.name = name;
		this.deeds = new IDeed[Bank.deeds.length];
	}

	public void inJail(boolean inJail) {
		this.inJail = inJail;
	}

	public boolean isInJail() {
		return inJail;
	}

	public void take(GetOutOfJailFree chanceCard) {
		this.getOutOfJailFreeCard = chanceCard;
	}

	public boolean hasGetOutOfJailFreeCard() {
		return getOutOfJailFreeCard != null;
	}

	public void useGetOutOfJailFreeCard() {
		if (getOutOfJailFreeCard == null)
			return;

		this.getOutOfJailFreeCard.action(this);
		this.getOutOfJailFreeCard.placeBackInDeck();
		this.getOutOfJailFreeCard = null;
	}

	public <S extends Space> boolean landedOn(Class<S> spaceType) {
		return spaceType.isInstance(Board.getSpace(getPosition()));
	}

//	public boolean landedOnChance() {
//		return Board.isChance(getPosition());
//	}
//
//	public boolean landedOnGoToJail() {
//		return Board.isGoToJail(getPosition());
//	}

//	public void goToJail() {
//		setPosition(Board.jail());
//	}

	public String getName() {
		return name;
	}

	public boolean owns(int i) {
		return deeds[i] != null;
	}

	public boolean ownsProperty() {
		return getOwnedStreets().size() > 0;
	}

	public int getOwnedHouseCount() {
		int count = 0;
		for (Street street : getOwnedStreets()) {
			count += street.getNumOfHouses();
		}
		return count;
	}

	public int getOwnedHotelCount() {
		int count = 0;
		for (Street street : getOwnedStreets()) {
			if (street.hasHotel())
				++count;
		}
		return count;
	}

	public List<Street> getOwnedStreets() {
		List<Street> streets = new ArrayList<>();
		for (int i = 0; i < getDeeds().length; i++) {

			// Done because the Deeds Array contains null
			// which is indicates a space on the board that this player does not own
			if (getDeeds()[i] == null)
				continue;

			// Filter for only Streets
			if (Board.isStreet(i)) {
				Street street = Board.getStreet(i);
				streets.add(street);
			}
		}
		return streets;
	}

	public Map<String, List<Street>> getGroupedProperties() {

		// Initialize the map which will hold the groupings
		// key = Color Group , value = List of Streets in group
		Map<String, List<Street>> map = new HashMap<>();

		// Iterate over all owned properties
		// adding them to the Color Group they are in
		for (Street street : getOwnedStreets()) {

			String colorGroup = ((StreetDeed) street.deed()).colorGroup();
			if (map.containsKey(colorGroup)) {
				map.get(colorGroup).add(street);
			} else {
				List<Street> streets = new ArrayList<>();
				streets.add(street);
				map.put(colorGroup, streets);
			}

		}
		return map;
	}

	public BigDecimal getTotalWorth() {
		BigDecimal totalWorth = BigDecimal.ZERO;

		// add cash balance
		totalWorth = totalWorth.add(getCashBalance());

		// add price of deeds
		for (IDeed deed : getDeeds()) {
			if (deed != null) {
				totalWorth = totalWorth.add(BigDecimal.valueOf(deed.price()));
			}
		}

		// TODO Calculate the collective price of the buildings owned by this player

		return totalWorth;
	}

	public BigDecimal getCashBalance() {
		return cashBalance;
	}

	public void addCash(int amount) {
		addCash(BigDecimal.valueOf(amount));
	}

	public void addCash(BigDecimal amount) {
		logger.info("Add cash $" + amount + " " + this);
		this.cashBalance = this.cashBalance.add(amount);
	}

	public void subtractCash(int amount) {
		subtractCash(BigDecimal.valueOf(amount));
	}

	public void subtractCash(float amount) {
		subtractCash(BigDecimal.valueOf(amount));
	}

	public void subtractCash(BigDecimal amount) {
		logger.info("Subtract cash $" + amount + " " + this);
		this.cashBalance = this.cashBalance.subtract(amount);
	}

	public int getPosition() {
		return position;
	}

	public void setPosition(int p_position) {
		logger.info("Set position [request=" + p_position + "; actual=" + Board.boardIndexOf(p_position) + "]");

		if (p_position < 0) {
			throw new IllegalArgumentException("Value is less than 0");
		}

		this.position = Board.boardIndexOf(p_position);
	}

	public void move(int valueToMove) {
		setPosition(move(valueToMove, position, Board.board.length));
	}

	private static int move(int valueToMove, int curPosition, int arrLength) {
		logger.info("Move [ValueToMove=" + valueToMove + ";CurrentPosition=" + curPosition + ";ArrayLength=" + arrLength
				+ "]");

		// Calculated position to move to
		int ret;

		// Initialize the position to the player's current position plus the number of
		// spaces to move the player's token
		ret = curPosition + valueToMove;

		// If the position the player was moved to is less than 0
		// then wrap the players position and continue doing so until the new position
		// is greater than 0 but less than the length of the array
		// This works by continue to subtract the value of the
		while (ret < 0) {
			ret = arrLength + ret;
		}

		// Now that we are certain the position is positive
		// we can get the modulus the new position modulus

		// Handle the case of the new position being greater than the upper bound (Array
		// length)
		ret = ret % arrLength;

		return ret;
	}

	public IDeed[] getDeeds() {
		return deeds;
	}

	@Override
	public String toString() {
		return "Player [name=" + name + "position=" + position + ", cashBalance=" + cashBalance + ", deeds="
				+ Arrays.toString(getDeeds()) + "]";
	}

}
