package org.khoyte.monopoly.board.space.chance;

import org.khoyte.monopoly.player.Player;

public abstract class ChanceCard {

	final String description;

	public ChanceCard(String description) {
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