package org.khoyte.monopoly.menu.menuitem;

import org.khoyte.monopoly.Board;
import org.khoyte.monopoly.Dice;
import org.khoyte.monopoly.Game;
import org.khoyte.monopoly.board.space.GoToJail;
import org.khoyte.monopoly.board.space.Tax;
import org.khoyte.monopoly.board.space.chance.Chance;
import org.khoyte.monopoly.board.space.chance.ChanceCard;
import org.khoyte.monopoly.player.Player;

public class RollDice implements Runnable {

	@Override
	public void run() {

		Game game = Game.getInstance();
		Player curPlayer = game.getCurrentPlayer();

		// move the player
//		curPlayer.move(Dice.getInstance().roll());
		// FIXME delete, here for testing
		curPlayer.move(1);
		
		// check whether the player landed on chance
		if (curPlayer.landedOn(Chance.class)) {

			// take a card from the deck
			ChanceCard chanceCard = Chance.getNext();
			game.print("Landed on Chance: " + chanceCard.getDescription());

			// run the action
			chanceCard.action(curPlayer);

		}
		// handle if they landed on tax space
		else if (curPlayer.landedOn(Tax.class)) {

			Tax tax = Board.getTax(curPlayer.getPosition());
			game.print("Landed on " + tax);

			tax.applyTo(curPlayer);
		} else {
			game.print("Landed on " + Board.getLocationName(curPlayer.getPosition()));
		}

		// must be last action
		if (curPlayer.landedOn(GoToJail.class)) {
			curPlayer.setPosition(Board.getJailPosition());
		}
	}

}
