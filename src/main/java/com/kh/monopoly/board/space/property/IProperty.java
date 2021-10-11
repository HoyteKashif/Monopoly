package com.kh.monopoly.board.space.property;

import com.kh.monopoly.board.space.ISpace;
import com.kh.monopoly.board.space.property.deed.IDeed;

public interface IProperty extends ISpace {

	IDeed deed();
}
