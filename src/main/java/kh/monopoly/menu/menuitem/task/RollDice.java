package kh.monopoly.menu.menuitem.task;

import kh.monopoly.Board;
import kh.monopoly.Dice;
import kh.monopoly.Game;
import kh.monopoly.board.space.GoToJail;
import kh.monopoly.board.space.Tax;
import kh.monopoly.board.space.chance.Chance;
import kh.monopoly.board.space.chance.ChanceCard;
import kh.monopoly.board.space.chance.GetOutOfJailFree;
import kh.monopoly.input.Keyboard;
import kh.monopoly.player.Player;

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
				// Ask the player if they want to keep the card
				// if yes then assign the card to them
				if (Keyboard.getInstance().readYN("Do you want to keep the card?", "Error - Invalid Input.")) {
					// Give them the card
					curPlayer.take(_card);
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
