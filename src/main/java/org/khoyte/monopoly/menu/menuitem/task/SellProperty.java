package org.khoyte.monopoly.menu.menuitem.task;

import org.khoyte.monopoly.Bank;
import org.khoyte.monopoly.Board;
import org.khoyte.monopoly.Game;
import org.khoyte.monopoly.board.space.property.IProperty;
import org.khoyte.monopoly.input.Keyboard;
import org.khoyte.monopoly.player.Player;

//FIXME
// Broken - Player purchasing already owned property is not handled
// properly
public class SellProperty implements Runnable {

	@Override
	public void run() {

		Game game = Game.getInstance();
		Player curPlayer = game.getCurrentPlayer();

		int location = curPlayer.getPosition();

		if (Board.isProperty(location)) {
			IProperty property = Board.getProperty(location);
			game.print(property);

			String input = Keyboard.getInstance().readLine("Do you want to purchase " + property.deed().name() + "? ",
					"Error - Invalid Input.");

			if (input.equals("y") || input.equals("yes")) {
				if (Bank.purchase(curPlayer, location))
					game.print("Purchase was successful!");
				else
					game.print("Purchase was unsuccessful!");
			} else {
				game.print("answer=(" + input + ")");
			}

		} else {
			game.print(Board.getLocationName(location) + " cannot be purchased");
		}
	}

}
