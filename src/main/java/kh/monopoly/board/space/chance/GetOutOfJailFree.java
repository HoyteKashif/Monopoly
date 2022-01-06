package kh.monopoly.board.space.chance;

/** Use to indicate that the card is a GetOutOfJailFree **/
public abstract class GetOutOfJailFree extends ChanceCard {

	boolean retainedByUser;

	public GetOutOfJailFree(String description) {
		super(description);
	}

	public void removeFromDeck() {
		this.retainedByUser = true;
	}

	public void placeBackInDeck() {
		this.retainedByUser = false;
	}

	public boolean isRetainedByUser() {
		return retainedByUser;
	}
}