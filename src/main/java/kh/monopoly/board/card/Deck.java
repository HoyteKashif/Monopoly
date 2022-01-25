package kh.monopoly.board.card;

import java.util.Collections;
import java.util.LinkedList;
import java.util.Stack;

import org.apache.log4j.Logger;

import kh.monopoly.Bank;
import kh.monopoly.Dice;
import kh.monopoly.Game;
import kh.monopoly.board.Board;
import kh.monopoly.board.space.Go;
import kh.monopoly.board.space.chance.ChanceCard;
import kh.monopoly.board.space.chance.GetOutOfJailFree;
import kh.monopoly.player.Player;

public class Deck<T> {
	private static final Logger logger = Logger.getLogger(Deck.class);
	private static final Stack<ChanceCard> stack = new Stack<>();
	private static final ChanceCard[] deck = new ChanceCard[16];
	private static final LinkedList<ChanceCard> linkedList = new LinkedList<ChanceCard>();

	private final LinkedList<T> ll = new LinkedList<>();

	public void add(T card) {
		ll.add(card);
	}

	public static void main(String[] args) {
		Deck d = new Deck();
		d.shuffle();
	}

	public static Deck<ChanceCard> chanceDeck() {

		Deck<ChanceCard> deck = new Deck<>();

		/*
		 * Advance to "Go". (Collect $200)
		 */
		deck.add(new ChanceCard("Advance to Go") {
			@Override
			public void action(Player player) {
				logger.info(this);
				Board.advanceToGo(player);
			}
		});

		/*
		 * Advance to Illinois Avenue
		 */
		deck.add(new ChanceCard("Advance to Illinois Ave.") {
			@Override
			public void action(Player player) {
				logger.info(this);
				Board.advanceToSpace(player, Board.findStreet("Illinois Avenue"), true);
			}
		});

		/*
		 * Advance to St. Charles Place
		 */
		deck.add(new ChanceCard("Advance to St. Charles Place.") {
			@Override
			public void action(Player player) {
				logger.info(this);
				Board.advanceToSpace(player, Board.findStreet("St. Charles Place"), true);
			}
		});

		deck.add(new ChanceCard(
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
		});

		deck.add(new ChanceCard(
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
		});

		deck.add(new ChanceCard("Bank pays you dividend of $50.") {
			@Override
			public void action(Player player) {
				logger.info(this);
				// Bank pays you dividend of $50.
				player.addCash(50);
			}
		});

		/**
		 * Get out of Jail Free. This card may be kept until needed, or traded/sold.
		 * {This card may be kept until needed or sold/traded. Get Out of Jail Free.}
		 */
		deck.add(new GetOutOfJailFree("Get out of Jail Free.") {
			@Override
			public void action(Player player) {
				logger.info(this);
				player.inJail(false);
			}
		});

		deck.add(new ChanceCard("Go Back Three Spaces.") {
			@Override
			public void action(Player player) {
				logger.info(this);
				player.move(-3);
			}
		});

		deck.add(new ChanceCard("Go to Jail. Go directly to Jail. Do not pass Go, do not collect $200.") {
			@Override
			public void action(Player player) {
				logger.info(this);
				player.setPosition(Board.getJailPosition());
			}
		});

		/**
		 * Make general repairs on all your property: For each house pay $25, For each
		 * hotel {pay} $100.
		 */
		deck.add(new ChanceCard(
				"Make general repairs on all your property: For each house pay $25, For each hotel pay $100.") {
			@Override
			public void action(Player player) {
				logger.info(this);
				int houseRepairCost = 25 * player.getOwnedHouseCount();
				int hotelRepairCost = 100 * player.getOwnedHotelCount();
				player.subtractCash(houseRepairCost + hotelRepairCost);
			}
		});

		deck.add(new ChanceCard("Pay poor tax of $15") {
			@Override
			public void action(Player player) {
				logger.info(this);
				player.subtractCash(15);
			}
		});

		/**
		 * Take a trip to the Reading RailRoad. Advance token if you pass Go, collect
		 * $200.
		 */
		deck.add(new ChanceCard("Take a trip to Reading Railroad.") {
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
		});

		/**
		 * Take a walk on the the Boardwalk. Advance token to Boardwalk.
		 */
		deck.add(new ChanceCard("Take a walk on the Boardwalk. Advance token to Boardwalk.") {
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
		});

		deck.add(new ChanceCard("You have been elected Chairman of the Board. Pay each player $50.") {
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
		});

		deck.add(new ChanceCard("Your building {and} loan matures. Receive $150.") {
			@Override
			public void action(Player player) {
				logger.info(this);
				// Your building {and} loan matures. Receive {Collect} $150
				player.addCash(150);
			}
		});

		deck.add(new ChanceCard("You have won a crossword competition. Collect $100.") {
			@Override
			public void action(Player player) {
				logger.info(this);
				// You have won a crossword competition. Collect $100. {Not in the deck}
				player.addCash(100);
			}
		});

		return deck;
	}

	public void shuffle() {
		Collections.shuffle(ll);
	}
}
