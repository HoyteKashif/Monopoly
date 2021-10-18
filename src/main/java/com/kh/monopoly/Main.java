package com.kh.monopoly;

import org.apache.log4j.PropertyConfigurator;

import com.kh.monopoly.player.Player;

public class Main {

	public static void main(String[] args) {

		PropertyConfigurator.configure("properties/log4j.properties");

		Game.instance.add(new Player("kashif"));
		Game.instance.add(new Player("damali"));
		Game.instance.start();
	}

}
