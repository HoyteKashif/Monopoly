package org.khoyte.monopoly.menu.menuitem.task;

import org.khoyte.monopoly.Game;

public class PassDice implements Runnable {

	@Override
	public void run() {
		Game game = Game.getInstance();
		game.getPlayerQueue().advance();
	}

}
