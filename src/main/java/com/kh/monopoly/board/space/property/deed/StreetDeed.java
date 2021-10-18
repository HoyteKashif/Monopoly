package com.kh.monopoly.board.space.property.deed;

public class StreetDeed extends ITitleDeed implements Purchasable {
	final String color_group;
	final int rentWith1House;
	final int rentWith2Houses;
	final int rentWith3Houses;
	final int rentWith4Houses;
	final int rentWithHotel;
	final int houseCost;
	final int hotelCost;

	public StreetDeed(String name, String color_group, int price, int rent, int rentWith1House, int rentWith2Houses,
			int rentWith3Houses, int rentWith4Houses, int rentWithHotel, int mortgageValue, int houseCost,
			int hotelCost) {

		super(name, price, rent, mortgageValue);

		this.color_group = color_group;
		this.rentWith1House = rentWith1House;
		this.rentWith2Houses = rentWith2Houses;
		this.rentWith3Houses = rentWith3Houses;
		this.rentWith4Houses = rentWith4Houses;
		this.rentWithHotel = rentWithHotel;
		this.houseCost = houseCost;
		this.hotelCost = hotelCost;
	}

	public String colorGroup() {
		return color_group;
	}

	@Override
	public int rentWith1House() {
		return rentWith1House;
	}

	@Override
	public int rentWith2Houses() {
		return rentWith2Houses;
	}

	@Override
	public int rentWith3Houses() {
		return rentWith3Houses;
	}

	@Override
	public int rentWith4Houses() {
		return rentWith4Houses;
	}

	@Override
	public int rentWithHotel() {
		return rentWithHotel;
	}

	@Override
	public int houseCost() {
		return houseCost;
	}

	@Override
	public int hotelCost() {
		return hotelCost;
	}

}