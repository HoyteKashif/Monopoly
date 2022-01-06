package kh.monopoly.board.space.chance;

import java.util.Random;
import java.util.Stack;

import org.apache.log4j.Logger;

import kh.monopoly.Bank;
import kh.monopoly.Board;
import kh.monopoly.Dice;
import kh.monopoly.Game;
import kh.monopoly.board.space.Go;
import kh.monopoly.board.space.Space;
import kh.monopoly.player.Player;

public class Chance extends Space {

	public static final String GET_OUT_OF_JAIL_FREE = "Get out of Jail Free";
	private static final Stack<ChanceCard> stack = new Stack<>();
	private static final ChanceCard[] deck = new ChanceCard[16];
	private static final Logger logger = Logger.getLogger(Chance.class);

	@Override
	public String name() {

		return "Chance";
	}

	static {
		/*
		 * Advance to "Go". (Collect $200)
		 */
		deck[0] = new ChanceCard("Advance to Go") {
			@Override
			public void action(Player player) {
				logger.info(this);

				// Advance to go
				player.setPosition(0);

				// Collect $200
				player.addCash(200);

			}
		};
		/*
		 * Advance to Illinois Avenue
		 */
		deck[1] = new ChanceCard("Advance to Illinois Ave.") {
			@Override
			public void action(Player player) {
				logger.info(this);

				int curPosition = player.getPosition();
				int newPosition = Board.findStreetByName("Illinois Avenue");

				player.setPosition(newPosition);

				// If they pass Go then they also collect $200
				boolean passedGo = curPosition > newPosition;

				if (passedGo) {
					player.addCash(200);
				}

			}
		};
		/*
		 * Advance to St. Charles Place
		 */
		deck[2] = new ChanceCard("Advance to St. Charles Place.") {
			@Override
			public void action(Player player) {
				logger.info(this);

				int curPosition = player.getPosition();
				int newPosition = Board.findStreetByName("St. Charles Place");

				player.setPosition(newPosition);

				// If they pass Go then they also collect $200
				boolean passedGo = curPosition > newPosition;

				if (passedGo) {
					player.addCash(200);
				}
			}
		};
		deck[3] = new ChanceCard(
				"Advance token to the nearest Utility. If unowned, you may buy it from the Bank. If owned, throw dice and pay owner a total 10 (ten) times the amount thrown.") {
			@Override
			public void action(Player player) {
				logger.info(this);
				// Advance to the nearest Utility.
				do {
					player.move(1);
					if (Board.board[player.getPosition()] instanceof Go) {
						player.addCash(200);
					}
				} while (!Board.isUtility(player.getPosition()));

				// If unowned, you may buy it from the Bank.

				// If owned, throw dice and pay owner a total 10 (ten) times the amount
				// thrown.
				if (Bank.isOwned(player.getPosition())) {

					int rentOwed = Dice.getInstance().rollMultipliedBy(10);

					player.subtractCash(rentOwed);

					// determine the owner
					Player owner = Game.getInstance().getPlayerQueue().findOwner(player.getPosition());
					owner.addCash(rentOwed);
				}
			}
		};
		deck[4] = new ChanceCard(
				"Advance to the nearest Railroad. If unowned, you may buy it from the Bank. If owned, pay owner twice the rental to which they are otherwise entitled. If Railroad is unowned, you may buy it from the Bank.") {
			@Override
			public void action(Player player) {
				logger.info(this);
				// Advance to the nearest Railroad.
				do {
					player.move(1);
					if (Board.board[player.getPosition()] instanceof Go) {
						player.addCash(200);
					}
				} while (!Board.isRailRoad(player.getPosition()));

				// If unowned, you may buy it from the Bank.

				// If owned, pay owner twice the rental to which they are otherwise entitled. If
				// Railroad is unowned, you may buy it from the Bank.
				if (Bank.isOwned(player.getPosition())) {

					int rentOwed = Dice.getInstance().rollMultipliedBy(10);

					player.subtractCash(rentOwed);

					// determine the owner
					Player owner = Game.getInstance().getPlayerQueue().findOwner(player.getPosition());
					owner.addCash(rentOwed);
				}
			}
		};
		deck[5] = new ChanceCard("Bank pays you dividend of $50.") {
			@Override
			public void action(Player player) {
				logger.info(this);
				// Bank pays you dividend of $50.
				player.addCash(50);
			}
		};
		/**
		 * Get out of Jail Free. This card may be kept until needed, or traded/sold.
		 * {This card may be kept until needed or sold/traded. Get Out of Jail Free.}
		 */
		deck[6] = new GetOutOfJailFree("Get out of Jail Free.") {
			@Override
			public void action(Player player) {
				logger.info(this);
				player.inJail(false);
			}
		};
		deck[7] = new ChanceCard("Go Back Three Spaces.") {
			@Override
			public void action(Player player) {
				logger.info(this);
				player.move(-3);
			}
		};
		deck[8] = new ChanceCard("Go to Jail. Go directly to Jail. Do not pass Go, do not collect $200.") {
			@Override
			public void action(Player player) {
				logger.info(this);
				player.setPosition(Board.getJailPosition());
			}

		};
		/**
		 * Make general repairs on all your property: For each house pay $25, For each
		 * hotel {pay} $100.
		 */
		deck[9] = new ChanceCard(
				"Make general repairs on all your property: For each house pay $25, For each hotel pay $100.") {
			@Override
			public void action(Player player) {
				logger.info(this);
				int houseRepairCost = 25 * player.getOwnedHouseCount();
				int hotelRepairCost = 100 * player.getOwnedHotelCount();
				player.subtractCash(houseRepairCost + hotelRepairCost);
			}
		};
		deck[10] = new ChanceCard("Pay poor tax of $15") {
			@Override
			public void action(Player player) {
				logger.info(this);
				player.subtractCash(15);
			}
		};

		/**
		 * Take a trip to the Reading RailRoad. Advance token if you pass Go, collect
		 * $200.
		 */
		deck[11] = new ChanceCard("Take a trip to Reading Railroad.") {
			@Override
			public void action(Player player) {
				logger.info(this);
				boolean foundRailRoad = false;
				do {
					player.move(1);
					if (Board.board[player.getPosition()] instanceof Go) {
						player.addCash(200);
					}
					foundRailRoad = Board.getLocationName(player.getPosition()).equals("Reading Railroad");
				} while (!foundRailRoad);
			}
		};
		/**
		 * Take a walk on the the Boardwalk. Advance token to Boardwalk.
		 */
		deck[12] = new ChanceCard("Take a walk on the Boardwalk. Advance token to Boardwalk.") {
			@Override
			public void action(Player player) {
				logger.info(this);
				boolean foundBoardwalk = false;
				do {
					player.move(1);
					if (Board.board[player.getPosition()] instanceof Go) {
						player.addCash(200);
					}
					foundBoardwalk = Board.getLocationName(player.getPosition()).equals("Boardwalk");
				} while (!foundBoardwalk);
			}
		};
		deck[13] = new ChanceCard("You have been elected Chairman of the Board. Pay each player $50.") {
			@Override
			public void action(Player player) {
				logger.info(this);

				Game game = Game.getInstance();

				// You have been elected Chairman of the Board. Pay each player $50.
				game.getPlayerQueue().apply(otherPlayer -> {
					player.subtractCash(50);
					otherPlayer.addCash(50);
				});
			}
		};
		deck[14] = new ChanceCard("Your building {and} loan matures. Receive $150.") {
			@Override
			public void action(Player player) {
				logger.info(this);
				// Your building {and} loan matures. Receive {Collect} $150
				player.addCash(150);
			}
		};
		deck[15] = new ChanceCard("You have won a crossword competition. Collect $100.") {
			@Override
			public void action(Player player) {
				logger.info(this);
				// You have won a crossword competition. Collect $100. {Not in the deck}
				player.addCash(100);
			}
		};
	}

	public static ChanceCard getNext() {

		// If empty card stack
		// then reset the stack
		if (stack.isEmpty()) {
			initCardStack();
		}

		// Get the next card off the top
		return stack.pop();
	}

	private static void initCardStack() {

		// Create Random number generator
		Random r = new Random();

		// Shuffle the cards
		// Iterate through all the chance cards
		for (int i = deck.length - 1; i >= 1; i--) {

			// Max value (Exclusive) of the Random Number
			int randUpperBound = i + 1;

			// Index between 0 and Upper Bound
			int j = r.nextInt(randUpperBound);

			// Swap the card at j with the card at i
			// cards are swapped with a card at a
			// smaller index in the array
			ChanceCard temp = deck[j];
			deck[j] = deck[i];
			deck[i] = temp;
		}

		// Set the card stack to the newly shuffle deck
		for (ChanceCard card : deck) {
			stack.push(card);
		}

	}

	public static boolean isGetOutOfJailFreeCard(ChanceCard card) {
		return card.description.startsWith(GET_OUT_OF_JAIL_FREE);
	}
}
