package kh.monopoly;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import kh.monopoly.menu.MenuFactory;
import kh.monopoly.menu.TextMenu;
import kh.monopoly.player.Player;

public class GameTest {

	private Game game;

	private ByteArrayOutputStream out;

	@Before
	public void initGame() {
		System.out.println("Entering initGame");
		this.game = Game.getInstance();
		game.add(new Player("kashif"));
		game.add(new Player("damali"));

		out = new ByteArrayOutputStream();

		TextMenu menu = MenuFactory.getMainMenu();
		menu.setOutputStream(out);

		game.setMenu(menu);

		Thread t = new Thread(game);
		t.start();
//		game.start();

		System.out.println("Leaving initGame");
	}

	@Test
	public void testCurrentPlayerIsNonNull() {
		System.out.println("Entering test");
		Assert.assertNotNull(game.getCurrentPlayer());
		System.out.println("Leaving test");
	}

	@Test
	public void testOutputMenu() {
		String s = "";
		s = "M E N U\n";
		s += "==========\n";
		s += "1: Roll Dice\n";
		s += "2: Purchase Property\n";
		s += "3: Show Player Info\n";
		s += "4: Purchase House\n";
		s += "5: Purchase Hotel\n";
		s += "6: Pass Dice\n";
		s += "7: Quit\n";
		s += "==========\n";
		s += "Select option:";

		try {
			out.flush();
			Assert.assertArrayEquals(s.getBytes(), out.toByteArray());
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
