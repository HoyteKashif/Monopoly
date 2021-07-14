package monopoly.board.space;

public class Tax extends Space {
	final String name;
	final int payDollar;
	final int payPercent;

	public Tax(String name, int payDollar, int payPercent) {
		this.name = name;
		this.payDollar = payDollar;
		this.payPercent = payPercent;
	}

	@Override
	public String name() {
		return name;
	}
}
