package monopoly.board.space.property;

import monopoly.board.space.ISpace;
import monopoly.board.space.property.deed.IDeed;

public interface IProperty extends ISpace {

	IDeed deed();
}
