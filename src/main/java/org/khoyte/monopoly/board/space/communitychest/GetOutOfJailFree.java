package org.khoyte.monopoly.board.space.communitychest;

/** Use to indicate that the card is a GetOutOfJailFree **/
public abstract class GetOutOfJailFree extends CommunityChestCard {

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