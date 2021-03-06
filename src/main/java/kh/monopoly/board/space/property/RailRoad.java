package kh.monopoly.board.space.property;

import kh.monopoly.board.space.property.deed.RailRoadDeed;

public class RailRoad extends Property {
	public RailRoad(RailRoadDeed deed) {
		super(deed);
	}
	
	@Override
	public String toString() {

		RailRoadDeed rd = (RailRoadDeed) deed;
		return rd.name() + " $" + rd.price();
	}
}