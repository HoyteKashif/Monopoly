package org.khoyte.monopoly.board.space.property;

import org.khoyte.monopoly.board.space.property.deed.StreetDeed;

public class Street extends Property {

	public int numHouses;

	public Street(StreetDeed deed) {
		super(deed);
	}

	public void addHouse() {
		numHouses++;
	}

	public int getNumOfHouses() {
		return numHouses;
	}

	public StreetDeed getDeed() {
		return (StreetDeed) super.deed();
	}

	@Override
	public String toString() {
		StreetDeed sd = getDeed();
		return sd.name() + " (" + sd.colorGroup() + ") $" + sd.price();
	}

}