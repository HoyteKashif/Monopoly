package org.khoyte.monopoly.menu.menuitem.task;

import org.khoyte.monopoly.Board;
import org.khoyte.monopoly.Dice;
import org.khoyte.monopoly.Game;
import org.khoyte.monopoly.board.space.GoToJail;
import org.khoyte.monopoly.board.space.Tax;
import org.khoyte.monopoly.board.space.chance.Chance;
import org.khoyte.monopoly.board.space.chance.ChanceCard;
import org.khoyte.monopoly.board.space.chance.GetOutOfJailFree;
import org.khoyte.monopoly.input.Keyboard;
import org.khoyte.monopoly.player.Player;

public class RollDice implements Runnable {

	@Override
	public void run() {

		Game game = Game.getInstance();
		Player curPlayer = game.getCurrentPlayer();

		// Move the player
		curPlayer.move(Dice.getInstance().roll());

		// Check whether the player landed on chance
		if (curPlayer.landedOn(Chance.class)) {

			// Take a card from the deck
			ChanceCard chanceCard;

			// Assign to new Chance Card
			// If the new Card is not a Get out Of Jail Free card
			// and if it is then check its availability
			// and add it is not assigned already
			do {
				chanceCard = Chance.getNext();
			} while ((chanceCard instanceof GetOutOfJailFree) && ((GetOutOfJailFree) chanceCard).isRetainedByUser());

			game.print("Landed on Chance: " + chanceCard.getDescription());

			if (chanceCard instanceof GetOutOfJailFree) {
				GetOutOfJailFree _card = (GetOutOfJailFree) chanceCard;
				if (!_card.isRetainedByUser()) {
					// Ask the player if they want to keep the card
					// if yes then assign the card to them
					if (Keyboard.getInstance().readYN("Do you want to keep the card?", "Error - Invalid Input.")) {
						// Give them the card
						curPlayer.take(_card);
					}
				}
			} else {
				// Run the action
				chanceCard.action(curPlayer);
			}
		}
		// Handle if they landed on tax space
		else if (curPlayer.landedOn(Tax.class)) {

			Tax tax = Board.getTax(curPlayer.getPosition());
			game.print("Landed on " + tax);

			tax.applyTo(curPlayer);
		} else if (curPlayer.landedOn(GoToJail.class)) {
			game.print("Landed on " + Board.getLocationName(curPlayer.getPosition()));
			curPlayer.setPosition(Board.getJailPosition());
		} else {
			game.print("Landed on " + Board.getLocationName(curPlayer.getPosition()));
		}

		// Must be last action
		if (curPlayer.landedOn(GoToJail.class)) {
			curPlayer.setPosition(Board.getJailPosition());
		}
	}

}
