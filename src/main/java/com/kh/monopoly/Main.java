package com.kh.monopoly;

import java.io.IOException;

import org.apache.log4j.PropertyConfigurator;

// TODO
// - add house/hotel purchasing behavior
// - taxes are not collected when the user lands on either income tax or luxury tax
// - check for any additional rules when the user roles doubles
// - is there a punishment for a player rolling doubles twice?
// - the user is picking up a chance or community chest card when they land on either
// - show a list of properties owned by a user when they type "my info"
// - create a logging system, maybe have it log to a file such as monopoly.log
// - make it so that if the player throws doubles three times in succession, 
// they go immediately to JAIL
// - determine the order of the players by randomly rolling the dice then 
// putting the players in order from highest to lowest value
// - if a player lands on a property and they do not wish to purchase
// it allow their to be an auction. Bank sells the property to the highest bidder. Bidding may start at any price.
public class Main {

	public static void main(String[] args) {

		PropertyConfigurator.configure("properties/log4j.properties");

		try {
			Game game = new Game();
			game.add(new Player("kashif"));
			game.add(new Player("damali"));
			game.start();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
