package com.kh.monopoly.menu.menuitem;

import com.kh.monopoly.Game;

public class PassDice implements Runnable {

	@Override
	public void run() {
		Game game = Game.getInstance();
		game.resetDoubleCounter();
		game.getPlayerQueue().advance();
	}

}
