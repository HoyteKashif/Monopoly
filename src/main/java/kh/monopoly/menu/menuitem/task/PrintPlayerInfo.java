package kh.monopoly.menu.menuitem.task;

import java.util.List;

import kh.monopoly.Game;
import kh.monopoly.board.Board;
import kh.monopoly.board.space.property.Street;
import kh.monopoly.player.Player;

public class PrintPlayerInfo implements Runnable {

	@Override
	public void run() {

		Game game = Game.getInstance();
		Player curPlayer = game.getCurrentPlayer();

		game.print("Current location: " + Board.toString(curPlayer.getPosition()));
		game.print("Bank Balance: $" + curPlayer.getCashBalance());

		List<Street> lstOwnedStreets = curPlayer.getOwnedStreets();
		String[] streetDescription = new String[lstOwnedStreets.size()];
		for (int i = 0; i < streetDescription.length; i++) {
			Street street = lstOwnedStreets.get(i);
			streetDescription[i] = street.name() + "(" + street.getDeed().colorGroup() + ")";
		}
		game.print("Owned Streets: [" + String.join(", ", streetDescription) + "]");

	}

}
