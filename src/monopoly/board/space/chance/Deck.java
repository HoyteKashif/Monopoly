package monopoly.board.space.chance;

import java.util.Arrays;
import java.util.Random;
import java.util.Stack;

import monopoly.Bank;
import monopoly.Board;
import monopoly.Dice;
import monopoly.Game;
import monopoly.Player;
import monopoly.board.space.Go;

public class Deck {
	public static final String GET_OUT_OF_JAIL_FREE = "Get out of Jail Free";

	final Game game;

	public static boolean isGetOutOfJailFreeCard(ChanceCard card) {
		return card.description.startsWith(GET_OUT_OF_JAIL_FREE);
	}

	// TODO card shuffling algorithm
	// en.wikipedia.org/wiki/Fisher-Yates_shuffle
	public static void main(String[] args) {
		// fisher-yates shuffle
		char[] a = "kashif".toCharArray();
		System.out.println(Arrays.toString(a));
		Random r = new Random();
		for (int i = a.length - 1; i >= 1; i--) {
			int j = r.nextInt(i + 1);
			char temp = a[j];
			a[j] = a[i];
			a[i] = temp;
		}
		System.out.println(Arrays.toString(a));

		// put in stack
		Stack<Character> queue = new Stack<>();
		for (char c : a) {
			queue.add(c);
		}

		while (!queue.isEmpty()) {
			System.out.println(queue.pop());
		}
	}

	private final Stack<ChanceCard> stack;

	private final ChanceCard[] deck;

	public Deck(Game game) {
		this.game = game;
		this.stack = new Stack<>();
		this.deck = new ChanceCard[16];
		initDeck();
	}

	public ChanceCard getNext() {

		if (stack.isEmpty()) {
			initCardStack();
		}

		return stack.pop();
	}

	private void initDeck() {
		deck[0] = new ChanceCard("Advance to Go") {
			@Override
			public void action(Player player) {

				// advance to go
				player.setPosition(0);

				// Advance to "Go". (Collect $200)
				player.bank_balance += 200;

			}
		};
		deck[1] = new ChanceCard("Advance to Illinois Ave.") {
			@Override
			public void action(Player player) {

				// Advance to Illinois Avenue
				boolean passedGo = Board.advancePlayer(player, Board.findStreetByName("Illinois Avenue"));

				// If you pass Go, collect $200
				if (passedGo) {
					player.bank_balance += 200;
				}

			}
		};
		deck[2] = new ChanceCard("Advance to St. Charles Place.") {
			@Override
			public void action(Player player) {

				// Advance to St. Charles Place
				boolean passedGo = Board.advancePlayer(player, Board.findStreetByName("St. Charles Place"));

				// if you pass Go, collect $200
				if (passedGo) {
					player.bank_balance += 200;
				}
			}
		};
		deck[3] = new ChanceCard(
				"Advance token to the nearest Utility. If unowned, you may buy it from the Bank. If owned, throw dice and pay owner a total 10 (ten) times the amount thrown.") {
			@Override
			public void action(Player player) {
				// Advance to the nearest Utility.
				do {
					player.move(1);
					if (Board.board[player.getPosition()] instanceof Go) {
						player.bank_balance += 200;
					}
				} while (!Board.isUtility(player.getPosition()));
				// If unowned, you may buy it from the Bank.

				// If owned, throw dice and pay owner a total 10 (ten) times the amount
				// thrown.
				if (Bank.isOwned(player.getPosition())) {

					int[] roll = Dice.roll();

					int rentOwed = (roll[0] + roll[1]) * 10;

					player.bank_balance = rentOwed;

					// determine the owner
					Player owner = game.playerQueue.findOwner(player.getPosition());
					owner.bank_balance += rentOwed;
				}
			}
		};
		deck[4] = new ChanceCard(
				"Advance to the nearest Railroad. If unowned, you may buy it from the Bank. If owned, pay owner twice the rental to which they are otherwise entitled. If Railroad is unowned, you may buy it from the Bank.") {
			@Override
			public void action(Player player) {
				// Advance to the nearest Railroad.
				do {
					player.move(1);
					if (Board.board[player.getPosition()] instanceof Go) {
						player.bank_balance += 200;
					}
				} while (!Board.isRailRoad(player.getPosition()));

				// If unowned, you may buy it from the Bank.

				// If owned, pay owner twice the rental to which they are otherwise entitled. If
				// Railroad is unowned, you may buy it from the Bank.
				if (Bank.isOwned(player.getPosition())) {

					int[] roll = Dice.roll();

					int rentOwed = (roll[0] + roll[1]) * 10;

					player.bank_balance = rentOwed;

					// determine the owner
					Player owner = game.playerQueue.findOwner(player.getPosition());
					owner.bank_balance += rentOwed;
				}
			}
		};
		deck[5] = new ChanceCard("Bank pays you dividend of $50.") {
			@Override
			public void action(Player player) {
				// Bank pays you dividend of $50.
				player.bank_balance += 50;
			}
		};
		deck[6] = new ChanceCard("Get out of Jail Free.") {
			@Override
			public void action(Player player) {
				// Get out of Jail Free. This card may be kept until needed, or traded/sold.
				// {This card may be kept until needed or sold/traded. Get Out of Jail
				// Free.}{The first sentence is much smaller than the second} (Mr. Monopoly, in
				// close-fitting one-piece prison stripes, is literally kicked out)
			}
		};
		deck[7] = new ChanceCard("Go Back Three Spaces.") {
			@Override
			public void action(Player player) {
				System.out.println("Go Back Three {3} Spaces.");
				player.move(-3);
			}
		};
		deck[8] = new ChanceCard("Go to Jail. Go directly to Jail. Do not pass Go, do not collect $200.") {
			@Override
			public void action(Player player) {
				System.out.println("Go to Jail. Go directly to Jail. Do not pass GO, do not collect $200.");
				player.setPosition(Board.jail());
			}

		};
		deck[9] = new ChanceCard(
				"Make general repairs on all your property: For each house pay $25, For each hotel pay $100.") {
			@Override
			public void action(Player player) {
				// Make general repairs on all your property: For each house pay $25, For each
				// hotel {pay} $100.(Consulting a "How to Fix It" brochure, a hammer-wielding
				// Mr. Monopoly sits astride a house not much larger than he is; it buckles
				// under his weight)
			}
		};
		deck[10] = new ChanceCard("Pay poor tax of $15") {
			@Override
			public void action(Player player) {
				// Pay poor tax of $15 (His trouser pockets pulled out to show them empty, Mr.
				// Monopoly spreads his hands) (The video game version replaces this with
				// Speeding fine $15, reportedly also in the UK version.)
			}
		};
		deck[11] = new ChanceCard("Take a trip to Reading Railroad.") {
			@Override
			public void action(Player player) {
				// Advance token if you pass Go, collect $200.
				boolean foundRailRoad = false;
				do {
					player.move(1);
					if (Board.board[player.getPosition()] instanceof Go) {
						player.bank_balance += 200;
					}
					foundRailRoad = Board.getLocationName(player.getPosition()).equals("Reading Railroad");
				} while (!foundRailRoad);
			}
		};
		deck[12] = new ChanceCard("Take a walk on the Boardwalk. Advance token to Boardwalk.") {
			@Override
			public void action(Player player) {
				// Take a walk on the Boardwalk. Advance token to Boardwalk. {Board Walk in both
				// sentences} [Take a walk on the board walk. Advance token to Mayfair] (Mr.
				// Monopoly, a smallish dog hung over one arm, with the other pushes a squalling
				// baby in a small pram; behind them, birds fly in the sky above a low fence)
				boolean foundBoardwalk = false;
				do {
					player.move(1);
					if (Board.board[player.getPosition()] instanceof Go) {
						player.bank_balance += 200;
					}
					foundBoardwalk = Board.getLocationName(player.getPosition()).equals("Boardwalk");
				} while (!foundBoardwalk);
			}
		};
		deck[13] = new ChanceCard("You have been elected Chairman of the Board. Pay each player $50.") {
			@Override
			public void action(Player player) {
				// You have been elected Chairman of the Board. Pay each player $50. (A newsboy
				// shouts an Extra with Mr. Monopoly's headshot on its front page)
				game.playerQueue.apply(p -> {
					player.bank_balance -= 50;
					p.bank_balance += 50;
				});
			}
		};
		deck[14] = new ChanceCard("Your building {and} loan matures. Receive $150.") {
			@Override
			public void action(Player player) {
				// Your building {and} loan matures. Receive {Collect} $150. {Up until the 1980s
				// a "building and loan" was a financial institution.} (Mr. Monopoly joyfully
				// embraces an apparent wife)

			}
		};
		deck[15] = new ChanceCard("You have won a crossword competition. Collect $100.") {
			@Override
			public void action(Player player) {
				// You have won a crossword competition. Collect $100. {Not in the deck}
				player.bank_balance += 100;
			}
		};
	}

	private void initCardStack() {

		// shuffle the cards
		Random r = new Random();
		for (int i = deck.length - 1; i >= 1; i--) {
			int j = r.nextInt(i + 1);
			ChanceCard temp = deck[j];
			deck[j] = deck[i];
			deck[i] = temp;
		}

		for (ChanceCard card : deck) {
			stack.push(card);
		}

	}
}
