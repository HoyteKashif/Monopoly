package org.khoyte.monopoly.board.space.property;

import org.khoyte.monopoly.board.space.ISpace;
import org.khoyte.monopoly.board.space.property.deed.IDeed;

public interface IProperty extends ISpace {

	IDeed deed();
}
