package monopoly;

import java.time.LocalDateTime;
import java.util.logging.Logger;

import monopoly.board.space.property.deed.IDeed;
import monopoly.shared.LogHelper;

public class Player {

	private static final Logger logger = LogHelper.getLogger(Player.class);

	// start at go
	private int position = 0;

	// initial bank balance is $1500
	public int bank_balance = 1500;

	final IDeed[] deeds;

	final String name;

	Player(String name) {
		this.name = name;
		this.deeds = new IDeed[40];
	}

	public int getPosition() {
		return position;
	}

	public void setPosition(int p_position) {
		logger.info(LocalDateTime.now().toString() + " position changed to [request=" + p_position + "; actual="
				+ Board.boardIndexOf(p_position) + "]");
		this.position = Board.boardIndexOf(p_position);
	}
}
