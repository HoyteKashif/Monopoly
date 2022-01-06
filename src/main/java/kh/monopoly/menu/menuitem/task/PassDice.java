package kh.monopoly.menu.menuitem.task;

import kh.monopoly.Game;

public class PassDice implements Runnable {

	@Override
	public void run() {
		Game game = Game.getInstance();
		game.getPlayerQueue().advance();
	}

}
