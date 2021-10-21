package org.khoyte.monopoly.menu;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.khoyte.monopoly.Game;

public class TextMenu extends TextMenuItem {

	private static final TextMenuItem quit = new TextMenuItem("Quit", new Runnable() {

		public void run() {
			Game.getInstance().quit();
		}
	});

	private static final TextMenuItem back = new TextMenuItem("back");

	List<TextMenuItem> items;

	public TextMenu(String title, TextMenuItem... items) {
		this(title, false, true, items);
	}

	public TextMenu(String title, boolean addBack, boolean addQuit, TextMenuItem... items) {
		super(title);
		setExec(this);

		initialize(addBack, addQuit, items);
	}

	private void initialize(boolean addBack, boolean addQuit, TextMenuItem... items) {
		this.items = new ArrayList<TextMenuItem>(Arrays.asList(items));
		if (addBack)
			this.items.add(back);
		if (addQuit)
			this.items.add(quit);
	}

	private void display() {
		System.out.println(getTitle());
		System.out.println("==========");
		for (int option = 0; option < items.size(); option++) {
			TextMenuItem item = items.get(option);
			System.out.println((option + 1) + ": " + item.getTitle());
		}
		System.out.println("==========");
		System.out.print("Select option: ");
		System.out.flush();
	}

	private TextMenuItem prompt() throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		while (true) {
			display();
			String line = br.readLine();
			try {
				int option = Integer.parseInt(line);
				if (option > 0 && option <= items.size())
					return items.get(option - 1);
			} catch (NumberFormatException e) {
			}

			System.out.println("not a valid menu option: " + line);
		}
	}

	public void run() {
		try {
			for (TextMenuItem item = prompt(); item.isExec(); item = prompt())
				item.run();
		} catch (Throwable t) {
			t.printStackTrace(System.out);
		}
	}
}
