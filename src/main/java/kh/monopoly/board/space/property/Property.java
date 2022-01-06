package kh.monopoly.board.space.property;

import kh.monopoly.board.space.property.deed.IDeed;

public class Property implements IProperty {
	IDeed deed;

	public Property(IDeed deed) {
		this.deed = deed;
	}

	@Override
	public String name() {
		return deed.name();
	}

	@Override
	public IDeed deed() {
		return deed;
	}

	@Override
	public String toString() {
		return "Property [deed=" + deed + "]";
	}

}
