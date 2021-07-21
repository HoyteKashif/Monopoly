package monopoly;

import java.io.IOException;

// TODO July 1, 2021
// 3. (might not do, instead look at creating "my info" and "info")change the command "my info" to "info"
// 4. (later) add house/hotel purchasing behavior
// 7. (later) no mention of the dual options such as typing "buy" or "buy property" when
// the user is prompted with the response to typing "help"
// 8. (later) all response options are not shown to the user when they are prompted as
// to whether or not they want to purchase a property
// 10. taxes are not collected when the user lands on either income tax or luxury tax
// 11. the user is not collecting $200 when they pass GO
// 12. check for any additional rules when the user roles doubles
// 13. is there a punishment for a player rolling doubles twice?
// 14. the user is picking up a chance or community chest card when they land on either
// 15. show a list of properties owned by a user when they type "my info"
// 16. two different set of actions when the user types "my info" vs "info"
// 17. create a logging system, maybe have it log to a file such as monopoly.log
// 18. make it so that if the player throws doubles three times in succession, 
// they go immediately to JAIL
// 19. determine the order of the players by randomly rolling the dice then 
// putting the players in order from highest to lowest value
// 20. if a player lands on a property and they do not wish to purchase
// it allow their to be an auction. Bank sells the property to the highest bidder. Bidding may start at any price.
public class Main {
	public static void main(String[] args) {

		try {

			Game game = new Game();

			game.add(new Player("kashif"));
			game.add(new Player("damali"));

			game.start();

		} catch (IOException e) {
			e.printStackTrace(System.err);
		}

	}

}
