package org.khoyte.monopoly.board.space.communitychest;

import org.khoyte.monopoly.player.Player;

public abstract class CommunityChestCard {
	final String description;

	public CommunityChestCard(String description) {
		this.description = description;
	}

	public abstract void action(Player player);

	public String getDescription() {
		return description;
	}

	@Override
	public String toString() {
		return "ChanceCard [description=" + description + "]";
	}
}
