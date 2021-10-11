package com.kh.monopoly;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.kh.monopoly.board.space.property.Street;
import com.kh.monopoly.board.space.property.deed.IDeed;
import com.kh.monopoly.board.space.property.deed.StreetDeed;

public class Player {

	private static final Logger logger = Logger.getLogger(Player.class);

	// start at go
	private int position = 0;

	// initial bank balance is $1500
	private BigDecimal cashBalance = BigDecimal.valueOf(1500);

	final IDeed[] deeds;

	final String name;

	Player(String name) {
		this.name = name;
		this.deeds = new IDeed[Bank.deeds.length];
	}

	public List<StreetDeed> getProperties() {
		List<StreetDeed> properties = new ArrayList<>();
		for (IDeed deed : deeds) {
			if (deed instanceof StreetDeed) {
				properties.add((StreetDeed) deed);
			}
		}
		return properties;
	}

	public Map<String, List<Street>> getGroupedProperties() {
		Map<String, List<Street>> map = new HashMap<>();
		for (int i = 0; i < deeds.length; i++) {
			if (deeds[i] == null)
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
		for (IDeed deed : deeds) {
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
		this.cashBalance = this.cashBalance.add(amount);
	}

	public void subtractCash(int amount) {
		subtractCash(BigDecimal.valueOf(amount));
	}

	public void subtractCash(float amount) {
		subtractCash(BigDecimal.valueOf(amount));
	}

	public void subtractCash(BigDecimal amount) {
		this.cashBalance = this.cashBalance.subtract(amount);
	}

	public int getPosition() {
		return position;
	}

	public void setPosition(int p_position) {
		logger.info(LocalDateTime.now().toString() + " position changed to [request=" + p_position + "; actual="
				+ Board.boardIndexOf(p_position) + "]");

		if (p_position < 0) {
			throw new IllegalArgumentException("Value is less than 0");
		}

		this.position = Board.boardIndexOf(p_position);
	}

	public void move(int valueToMove) {
		setPosition(move(valueToMove, position, Board.board.length));
	}

	public static void main(String[] args) {
		System.out.println(move((Board.board.length * -2) + -5, 2, Board.board.length));
		System.out.println(move(-3, 2, Board.board.length));
	}

	private static int move(int valueToMove, int curPosition, int arrLength) {
		System.out.println("move (ValueToMove=" + valueToMove + ";CurrentPosition=" + curPosition + ";ArrayLength="
				+ arrLength + ")");
		int ret;
		ret = curPosition + valueToMove;

		while (ret < 0) {
			ret = arrLength + ret;
		}

		return ret % arrLength;
	}

}
