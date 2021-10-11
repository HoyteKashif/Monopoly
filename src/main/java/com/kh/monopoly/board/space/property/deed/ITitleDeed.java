package com.kh.monopoly.board.space.property.deed;

public abstract class ITitleDeed extends Deed {
	public ITitleDeed(String name, int price, int rent, int mortgageValue) {
		super(name, price, rent, mortgageValue);
	}

	abstract int rentWith1House();

	abstract int rentWith2Houses();

	abstract int rentWith3Houses();

	abstract int rentWith4Houses();

	abstract int rentWithHotel();

	abstract int houseCost();

	abstract int hotelCost();
}
