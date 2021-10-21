package org.khoyte.monopoly.menu;

import org.khoyte.monopoly.menu.menuitem.PassDice;
import org.khoyte.monopoly.menu.menuitem.PrintPlayerInfo;
import org.khoyte.monopoly.menu.menuitem.RollDice;
import org.khoyte.monopoly.menu.menuitem.SellHouse;
import org.khoyte.monopoly.menu.menuitem.SellProperty;

public class MenuFactory {
	public static TextMenu getMainMenu() {

		TextMenuItem items[] = new TextMenuItem[5];

		items[0] = new TextMenuItem("Roll Dice", new RollDice());
		items[1] = new TextMenuItem("Purchase Property", new SellProperty());
		items[2] = new TextMenuItem("Show Player Info", new PrintPlayerInfo());
		items[3] = new TextMenuItem("Purchase House", new SellHouse());
		items[4] = new TextMenuItem("Pass Dice", new PassDice());

		TextMenu mm = new TextMenu("M E N U", items);

		return mm;
	}

}
