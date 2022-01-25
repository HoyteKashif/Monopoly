package kh.monopoly.menu;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.log4j.Logger;

import kh.monopoly.Game;
import kh.monopoly.menu.menuitem.TextMenuItem;

public class TextMenu extends TextMenuItem {

	private static final TextMenuItem quit = new TextMenuItem("Quit", new Runnable() {

		public void run() {
			Game.getInstance().quit();
		}
	});

	private static final TextMenuItem back = new TextMenuItem("back");

	private static final Logger LOGGER = Logger.getLogger(TextMenu.class);

	private static final PrintStream DEFAULT_PRINT_WRITER = System.out;

	private PrintStream ps;

	List<TextMenuItem> items;

	public TextMenu(String title, TextMenuItem... items) {
		this(title, false, true, items);
	}

	public TextMenu(String title, boolean addBack, boolean addQuit, TextMenuItem... items) {
		super(title);
		setExec(this);

		initialize(addBack, addQuit, items);
		this.ps = DEFAULT_PRINT_WRITER;
	}

	private void initialize(boolean addBack, boolean addQuit, TextMenuItem... items) {

		this.items = new ArrayList<TextMenuItem>(Arrays.asList(items));

		if (addBack)
			this.items.add(back);

		if (addQuit)
			this.items.add(quit);

	}

	public void setOutputStream(OutputStream out) {

		if (ps != null && !ps.equals(System.out)) {
			ps.close();
		}

		this.ps = new PrintStream(out);
	}

	private void display() {
		ps.println(getTitle());
		ps.println("==========");
		for (int option = 0; option < items.size(); option++) {
			TextMenuItem item = items.get(option);
			ps.println((option + 1) + ": " + item.getTitle());
		}
		ps.println("==========");
		ps.print("Select option: ");
		ps.flush();
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

			ps.println("not a valid menu option: " + line);
		}
	}

//	public class Worker extends Thread {
//		@Override
//		public void run() {
//
//			try {
//				for (TextMenuItem item = prompt(); item.isExec(); item = prompt())
//					item.run();
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
//		}
//
//		private TextMenuItem prompt() throws IOException {
//			BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
//			while (true) {
//				display();
//				String line = br.readLine();
//				try {
//					int option = Integer.parseInt(line);
//					if (option > 0 && option <= items.size())
//						return items.get(option - 1);
//				} catch (NumberFormatException e) {
//				}
//
//				System.out.println("not a valid menu option: " + line);
//			}
//		}
//	}

	public void run() {
		try {
//			Thread t = new Thread(new Worker());
//			t.run();

			for (TextMenuItem item = prompt(); item.isExec(); item = prompt())
				item.run();

		} catch (Throwable t) {
			LOGGER.error(t.getMessage(), t);
		}
	}
}
