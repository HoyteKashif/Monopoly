package com.kh.monopoly.menu.menuitem;

import java.util.List;

import com.kh.monopoly.Board;
import com.kh.monopoly.Game;
import com.kh.monopoly.board.space.property.deed.StreetDeed;
import com.kh.monopoly.player.Player;

public class PrintPlayerInfo implements Runnable {

	@Override
	public void run() {

		Game game = Game.getInstance();
		Player curPlayer = game.getCurrentPlayer();

		game.print("Current location: " + Board.toString(curPlayer.getPosition()));
		game.print("Bank Balance: $" + curPlayer.getCashBalance());

		List<StreetDeed> lstOwnedProperties = curPlayer.getProperties();
		String[] properties = new String[lstOwnedProperties.size()];
		for (int i = 0; i < properties.length; i++) {
			StreetDeed deed = lstOwnedProperties.get(i);
			properties[i] = deed.name() + "(" + deed.colorGroup() + ")";
		}
		game.print("Owned Properties: [" + String.join(", ", properties) + "]");

	}

}
