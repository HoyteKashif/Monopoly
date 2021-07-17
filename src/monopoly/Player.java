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

		if (p_position < 0) {
			throw new IllegalArgumentException("Value is less than 0");
		}

		this.position = Board.boardIndexOf(p_position);
	}

	// function move (value)
	// value < 0
//			result = current-position + (value)
//			if current-value < 0
//				return array.length - result
//			return result
//			value >= 0
//				return (current-position) + value) % array.length
	//

	public void move(int valueToMove) {
		setPosition(move(valueToMove, position, Board.board.length));
	}

	public static void main(String[] args) {
		System.out.println(move((Board.board.length * -2) + -5, 2, Board.board.length));
		System.out.println(move(-3, 2, Board.board.length));
	}

	private static int move(int valueToMove, int curPosition, int arrLength) {
		System.out.println("move (ValueToMove=" + valueToMove + ";CurrentPosition=" + curPosition + ";ArrayLength="
				+ arrLength + ")");
		int ret;
		if (valueToMove < 0) {
			ret = curPosition + valueToMove;
			while (ret < 0) {
				ret = arrLength + ret;
			}
		} else {
			ret = curPosition + valueToMove;
		}

		return ret % arrLength;
	}

}
