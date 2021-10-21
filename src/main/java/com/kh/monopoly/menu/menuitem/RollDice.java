package com.kh.monopoly.menu.menuitem;

import com.kh.monopoly.Board;
import com.kh.monopoly.Dice;
import com.kh.monopoly.Game;
import com.kh.monopoly.board.space.GoToJail;
import com.kh.monopoly.board.space.Tax;
import com.kh.monopoly.board.space.chance.Chance;
import com.kh.monopoly.board.space.chance.ChanceCard;
import com.kh.monopoly.player.Player;

public class RollDice implements Runnable {

	@Override
	public void run() {

		Game game = Game.getInstance();
		Player curPlayer = game.getCurrentPlayer();

		// move the player
		int[] roll = Dice.roll();
		if (roll[0] == roll[1]) {
			game.incrementDoubleCount();
		}
		int sum = roll[0] + roll[1];
		curPlayer.move(sum);

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
			curPlayer.goToJail();
		}
	}

}
