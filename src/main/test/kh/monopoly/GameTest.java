package kh.monopoly;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.khoyte.monopoly.player.Player;

public class GameTest {

	private Game game;

	@Before
	public void initGame() {
		this.game = Game.getInstance();
		game.add(new Player("kashif"));
		game.add(new Player("damali"));
		game.start();
	}

	@Test
	public void testCurrentPlayerIsNonNull() {
		Assert.assertNotNull(game.getCurrentPlayer());
	}

}
