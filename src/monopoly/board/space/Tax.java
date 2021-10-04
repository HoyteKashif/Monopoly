package monopoly.board.space;

import java.math.BigDecimal;

public class Tax extends Space {
	final String name;
	final int amountInDollar;
	final BigDecimal amountInPercent;
	final boolean hasPercentOption;

	public Tax(String name, int amountInDollar, int amountInPercent) {
		this.name = name;
		this.amountInDollar = amountInDollar;
		this.amountInPercent = (amountInPercent != -1
				? BigDecimal.valueOf(amountInPercent).divide(BigDecimal.valueOf(100))
				: BigDecimal.valueOf(amountInPercent));
		this.hasPercentOption = (amountInPercent != -1);
	}

	@Override
	public String name() {
		return name;
	}

	public int getAmountInDollar() {
		return amountInDollar;
	}

	public boolean hasPercentOption() {
		return hasPercentOption;
	}

	public BigDecimal getAmountInPercent() {

		if (!hasPercentOption())
			throw new UnsupportedOperationException(name() + " does not have a percent option.");

		return amountInPercent;
	}

}
