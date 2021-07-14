package monopoly.board.space.property.deed;

public class Deed implements IDeed {
	private final String name;

	private final int price, rent, mortgageValue;

	public Deed(String name, int price, int rent, int mortgageValue) {
		this.price = price;
		this.name = name;
		this.rent = rent;
		this.mortgageValue = mortgageValue;
	}

	@Override
	public int price() {
		return price;
	}

	@Override
	public int rent() {
		return rent;
	}

	@Override
	public int mortgageValue() {
		return mortgageValue;
	}

	@Override
	public String name() {
		return name;
	}
}