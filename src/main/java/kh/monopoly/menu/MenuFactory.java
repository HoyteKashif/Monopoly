package kh.monopoly.menu;

import kh.monopoly.menu.menuitem.TextMenuItem;
import kh.monopoly.menu.menuitem.task.PassDice;
import kh.monopoly.menu.menuitem.task.PrintPlayerInfo;
import kh.monopoly.menu.menuitem.task.RollDice;
import kh.monopoly.menu.menuitem.task.SellHotel;
import kh.monopoly.menu.menuitem.task.SellHouse;
import kh.monopoly.menu.menuitem.task.SellProperty;

public class MenuFactory {
	public static TextMenu getMainMenu() {

		TextMenuItem items[];

		items = new TextMenuItem[6];
		items[0] = new TextMenuItem("Roll Dice", new RollDice());
		items[1] = new TextMenuItem("Purchase Property", new SellProperty());
		items[2] = new TextMenuItem("Show Player Info", new PrintPlayerInfo());
		items[3] = new TextMenuItem("Purchase House", new SellHouse());
		items[4] = new TextMenuItem("Purchase Hotel", new SellHotel());
		items[5] = new TextMenuItem("Pass Dice", new PassDice());

		return new TextMenu("M E N U", items);
	}

}
