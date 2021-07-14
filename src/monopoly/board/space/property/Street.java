package monopoly.board.space.property;

import monopoly.board.space.property.deed.StreetDeed;

public class Street extends Property {
	public Street(StreetDeed deed) {
		super(deed);
	}

	@Override
	public String toString() {
		StreetDeed sd = (StreetDeed) deed;
		return sd.name() + " (" + sd.colorGroup() + ") $" + sd.price();
	}
}