package monopoly.board.space;

public abstract class Space implements ISpace {
	@Override
	public String toString() {
		return name();
	}
}