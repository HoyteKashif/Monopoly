package kh.monopoly;

import org.apache.log4j.PropertyConfigurator;

import kh.monopoly.player.Player;

public class Main {

	public static void main(String[] args) {

		PropertyConfigurator.configure("properties/log4j.properties");

		Game game = Game.getInstance();

		game.add(new Player("kashif"));
		game.add(new Player("damali"));

		new Thread(game).start();

//		game.start();
	}

}
