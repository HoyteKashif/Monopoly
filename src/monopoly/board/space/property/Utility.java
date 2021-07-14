package monopoly.board.space.property;

import monopoly.board.space.property.deed.UtilityDeed;

public class Utility extends Property {
	public Utility(UtilityDeed deed) {
		super(deed);
	}

	@Override
	public String toString() {

		UtilityDeed ud = (UtilityDeed) deed;
		return ud.name() + " $" + ud.price();
	}
}
