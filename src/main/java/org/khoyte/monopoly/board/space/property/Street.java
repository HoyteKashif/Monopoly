package org.khoyte.monopoly.board.space.property;

import org.khoyte.monopoly.board.space.property.deed.StreetDeed;

public class Street extends Property {

	private int numHouses;

	private boolean hasHotel;

	public Street(StreetDeed deed) {
		super(deed);
	}

	public void removeHouses(int amount) {
		numHouses -= amount;
	}

	public void addHouse() {
		numHouses++;
	}

	public int getNumOfHouses() {
		return numHouses;
	}

	public boolean hasHotel() {
		return hasHotel;
	}

	public void addHotel() {
		this.hasHotel = true;
	}

	public void removeHotel() {
		this.hasHotel = false;
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