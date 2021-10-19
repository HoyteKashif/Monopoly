package com.kh.monopoly.menu;

import com.kh.monopoly.menu.menuitem.PropertySale;
import com.kh.monopoly.menu.menuitem.Roll;

public class MenuFactory {
	public static TextMenu getMainMenu() {

		TextMenuItem items[] = new TextMenuItem[2];
		items[0] = new TextMenuItem("Roll", new Roll());
		items[1] = new TextMenuItem("Property Sale", new PropertySale());

		TextMenu mm = new TextMenu("M E N U", items);

		return mm;
	}
}
