package org.khoyte.monopoly.board.space.property.deed;

// 1 railroad owned: $25
// 2 railroads owned: $50
// 3 railroads owned: $100
// 4 railroads owned: $200
public class RailRoadDeed extends Deed implements Purchasable{
	final int oneOwned;
	final int twoOwned;
	final int threeOwned;
	final int fourOwned;

	public RailRoadDeed(String name, int price, int mortgageValue, int oneOwned, int twoOwned, int threeOwned,
			int fourOwned) {
		super(name, price, -1, mortgageValue);

		this.oneOwned = oneOwned;
		this.twoOwned = twoOwned;
		this.threeOwned = threeOwned;
		this.fourOwned = fourOwned;
	}

	@Override
	public int rent() {
		throw new UnsupportedOperationException();
	}
}