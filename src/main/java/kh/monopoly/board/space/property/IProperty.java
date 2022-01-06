package kh.monopoly.board.space.property;

import kh.monopoly.board.space.ISpace;
import kh.monopoly.board.space.property.deed.IDeed;

public interface IProperty extends ISpace {

	IDeed deed();
}
