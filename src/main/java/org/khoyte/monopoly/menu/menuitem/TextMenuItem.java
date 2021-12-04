package org.khoyte.monopoly.menu.menuitem;

public class TextMenuItem implements Runnable {
	private String title;
	private Runnable exec;

	public TextMenuItem(String title) {
		this(title, null);
	}

	public TextMenuItem(String title, Runnable exec) {
		this.title = title;
		this.exec = exec;
	}

	public String getTitle() {
		return title;
	}

	public boolean isExec() {
		return exec != null;
	}

	protected void setExec(Runnable exec) {
		this.exec = exec;
	}

	public void run() {
		try {
			exec.run();
		} catch (Throwable t) {
			t.printStackTrace(System.err);
		}
	}
}
