package kh.monopoly.board.space.communitychest;

import java.util.Random;
import java.util.Stack;

import org.apache.log4j.Logger;

import kh.monopoly.Game;
import kh.monopoly.board.Board;
import kh.monopoly.board.space.Space;
import kh.monopoly.player.Player;

public class CommunityChest extends Space {
	private static final Stack<CommunityChestCard> stack = new Stack<>();
	private static final CommunityChestCard[] deck = new CommunityChestCard[17];
	private static final Logger logger = Logger.getLogger(CommunityChest.class);

	@Override
	public String name() {
		return "Community Chest";
	}

	static {

		/*
		 * Advance to "Go". Collect $200.
		 */
		deck[0] = new CommunityChestCard("Advance to Go") {
			@Override
			public void action(Player player) {
				logger.info(this);
				player.setPosition(0);
				player.addCash(200);
			}
		};

		/*
		 * Bank error in your favor. Collect $200.
		 */
		deck[1] = new CommunityChestCard("Bank error in your favor. Collect $200.") {
			@Override
			public void action(Player player) {
				logger.info(this);
				player.addCash(200);
			}
		};

		/*
		 * Doctor's fees. Pay $50.
		 */
		deck[2] = new CommunityChestCard("Doctor's fees. Pay $50.") {
			@Override
			public void action(Player player) {
				logger.info(this);
				player.subtractCash(50);
			}
		};

		/*
		 * From sale of stock you get $50.
		 */
		deck[3] = new CommunityChestCard("From sale of stock you get $50. {$45.}") {
			@Override
			public void action(Player player) {
				logger.info(this);
				player.addCash(200);
			}
		};

		/*
		 * Get Out of Jail Free.
		 */
		deck[4] = new GetOutOfJailFree("Get Out of Jail Free.") {
			@Override
			public void action(Player player) {
				logger.info(this);
				player.inJail(false);
			}
		};

		/*
		 * Go to Jail. Go directly to jail. Do not pass Go, Do not collect $200.
		 */
		deck[5] = new CommunityChestCard("Go to Jail. Go directly to jail. Do not pass Go, Do not collect $200.") {
			@Override
			public void action(Player player) {
				logger.info(this);
				player.setPosition(Board.getJailPosition());
			}
		};

		/*
		 * Grand Opera Night. Collect $50 from every player for opening night seats.
		 */
		deck[6] = new GetOutOfJailFree("Grand Opera Night. Collect $50 from every player for opening night seats.") {
			@Override
			public void action(Player player) {
				logger.info(this);
				Game game = Game.getInstance();
				game.getPlayerQueue().apply(otherPlayer -> {
					otherPlayer.subtractCash(50);
					player.addCash(50);
				});
			}
		};

		/*
		 * Holiday Fund matures. Collect $100.
		 */
		deck[7] = new CommunityChestCard("Holiday {Xmas} Fund matures. Receive {Collect} $100.") {
			@Override
			public void action(Player player) {
				logger.info(this);
				player.addCash(100);
			}
		};

		/*
		 * Income tax refund. Collect $20.
		 */
		deck[8] = new CommunityChestCard("Income tax refund. Collect $20.") {
			@Override
			public void action(Player player) {
				logger.info(this);
				player.addCash(20);
			}
		};

		/*
		 * It's your birthday. Collect $10 from every player.
		 */
		deck[9] = new CommunityChestCard("It's your birthday. Collect $10 from every player.") {
			@Override
			public void action(Player player) {
				logger.info(this);
				Game game = Game.getInstance();
				game.getPlayerQueue().apply(otherPlayer -> {
					otherPlayer.subtractCash(10);
					player.addCash(10);
				});
			}
		};

		/*
		 * Life insurance matures – Collect $100
		 */
		deck[10] = new CommunityChestCard("Life insurance matures – Collect $100") {
			@Override
			public void action(Player player) {
				logger.info(this);
				player.addCash(100);
			}
		};

		/*
		 * Hospital Fees. Pay $50.
		 */
		deck[11] = new CommunityChestCard("Hospital Fees. Pay $50.") {
			@Override
			public void action(Player player) {
				logger.info(this);
				player.subtractCash(50);
			}
		};

		/*
		 * School fees. Pay $50.
		 */
		deck[12] = new CommunityChestCard("School fees. Pay $50.") {
			@Override
			public void action(Player player) {
				logger.info(this);
				player.subtractCash(50);
			}
		};

		/*
		 * Receive $25 consultancy fee.
		 */
		deck[13] = new CommunityChestCard("Receive $25 consultancy fee.") {
			@Override
			public void action(Player player) {
				logger.info(this);
				player.addCash(50);
			}
		};

		/*
		 * You are assessed for street repairs: Pay $40 per house and $115 per hotel you
		 * own.
		 */
		deck[14] = new CommunityChestCard(
				"You are assessed for street repairs: Pay $40 per house and $115 per hotel you own.") {
			@Override
			public void action(Player player) {
				logger.info(this);
				int repairCost = 0;
				repairCost += 40 * player.getOwnedHouseCount();
				repairCost += 115 * player.getOwnedHotelCount();
				player.subtractCash(repairCost);
			}
		};

		/*
		 * You have won second prize in a beauty contest. Collect $10.
		 */
		deck[15] = new CommunityChestCard("You have won second prize in a beauty contest. Collect $10.") {
			@Override
			public void action(Player player) {
				logger.info(this);
				player.addCash(10);
			}
		};

		/*
		 * You inherit $100.
		 */
		deck[16] = new CommunityChestCard("You inherit $100.") {
			@Override
			public void action(Player player) {
				logger.info(this);
				player.addCash(100);
			}
		};
	}

	public static CommunityChestCard getNext() {

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
			CommunityChestCard temp = deck[j];
			deck[j] = deck[i];
			deck[i] = temp;
		}

		// Set the card stack to the newly shuffle deck
		for (CommunityChestCard card : deck) {
			stack.push(card);
		}

	}
}
