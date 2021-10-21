package org.khoyte.monopoly.board.space.property.deed;

public class UtilityDeed extends Deed implements Purchasable {
	final int oneOwned;
	final int twoOwned;

	public UtilityDeed(String name, int price, int mortgageValue, int oneOwned, int twoOwned) {
		super(name, price, -1/* Utilities have a varying rent */, mortgageValue);
		this.oneOwned = oneOwned;
		this.twoOwned = twoOwned;
	}

	@Override
	public int rent() {
		throw new UnsupportedOperationException();
	}
}