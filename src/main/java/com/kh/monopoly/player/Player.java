package com.kh.monopoly.player;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.kh.monopoly.Bank;
import com.kh.monopoly.Board;
import com.kh.monopoly.board.space.property.Street;
import com.kh.monopoly.board.space.property.deed.IDeed;
import com.kh.monopoly.board.space.property.deed.StreetDeed;

public class Player {

	private static final Logger logger = Logger.getLogger(Player.class);

	// start at go
	private int position = 0;

	// initial bank balance is $1500
	private BigDecimal cashBalance = BigDecimal.valueOf(1500);

	private final IDeed[] deeds;

	private final String name;

	public Player(String name) {
		this.name = name;
		this.deeds = new IDeed[Bank.deeds.length];
	}

	public String getName() {
		return name;
	}

	public boolean owns(int i) {
		return deeds[i] != null;
	}

	public List<StreetDeed> getProperties() {
		List<StreetDeed> properties = new ArrayList<>();
		for (IDeed deed : getDeeds()) {
			if (deed instanceof StreetDeed) {
				properties.add((StreetDeed) deed);
			}
		}
		return properties;
	}

	public Map<String, List<Street>> getGroupedProperties() {
		Map<String, List<Street>> map = new HashMap<>();
		for (int i = 0; i < getDeeds().length; i++) {
			if (getDeeds()[i] == null)
				continue;

			if (Board.isStreet(i)) {
				Street street = Board.getStreet(i);
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
		return map;
	}

	// FIXME
	// add buildings
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

		// add worth of buildings

		return totalWorth;
	}

	public BigDecimal getCashBalance() {
		return cashBalance;
	}

	public void addCash(int amount) {
		addCash(BigDecimal.valueOf(amount));
	}

	public void addCash(BigDecimal amount) {
		logger.info("add cash $" + amount + " " + this);
		this.cashBalance = this.cashBalance.add(amount);
	}

	public void subtractCash(int amount) {
		subtractCash(BigDecimal.valueOf(amount));
	}

	public void subtractCash(float amount) {
		subtractCash(BigDecimal.valueOf(amount));
	}

	public void subtractCash(BigDecimal amount) {
		logger.info("subtract cash $" + amount + " " + this);
		this.cashBalance = this.cashBalance.subtract(amount);
	}

	public int getPosition() {
		return position;
	}

	public void setPosition(int p_position) {
		logger.info("set position [request=" + p_position + "; actual=" + Board.boardIndexOf(p_position) + "]");

		if (p_position < 0) {
			throw new IllegalArgumentException("Value is less than 0");
		}

		this.position = Board.boardIndexOf(p_position);
	}

	public void move(int valueToMove) {
		setPosition(move(valueToMove, position, Board.board.length));
	}

	private static int move(int valueToMove, int curPosition, int arrLength) {
		logger.info("move [ValueToMove=" + valueToMove + ";CurrentPosition=" + curPosition + ";ArrayLength=" + arrLength
				+ "]");
		int ret;
		ret = curPosition + valueToMove;

		while (ret < 0) {
			ret = arrLength + ret;
		}

		return ret % arrLength;
	}

	@Override
	public String toString() {
		return "Player [position=" + position + ", cashBalance=" + cashBalance + ", deeds="
				+ Arrays.toString(getDeeds()) + ", name=" + name + "]";
	}

	public IDeed[] getDeeds() {
		return deeds;
	}

}
