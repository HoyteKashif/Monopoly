package com.kh.monopoly.board.space.chance;

import com.kh.monopoly.Player;

public abstract class ChanceCard {

	final String description;

	public ChanceCard(String description) {
		this.description = description;
	}

	public abstract void action(Player player);

	public String getDescription() {
		return description;
	}
}