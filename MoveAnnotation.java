package antiChess;
import java.awt.Point;

public class MoveAnnotation {
	private Point fromPoint;
	private Point toPoint;
	private Promotion promotion;

	// Pawn promotion
	public enum Promotion {
		QUEEN, ROOK, BISHOP, KNIGHT
	}

	public MoveAnnotation(String move) throws Exception {
		getFromPoint(move);
		getToPoint(move);
		getPromotion(move);
	}

	// get from point
	private void getFromPoint(String move) throws Exception {
		int fromX = move.charAt(0) - 'a';
		int fromY = move.charAt(1) - '0';
		if (isValidPoint(fromX, fromY)) {
			fromPoint = new Point(fromX, fromY);
		} else {
			throw new Exception("from point is invalid");
		}
	}

	// get to point
	private void getToPoint(String move) throws Exception {
		int toX = move.charAt(2) - 'a';
		int toY = move.charAt(3) - '0';
		if (isValidPoint(toX, toY)) {
			toPoint = new Point(toX, toY);
		} else {
			throw new Exception("to point is invalid");
		}
	}

	// get promotion enum
	private void getPromotion(String move) throws Exception {
		if (move.length() > 4) {
			switch (move.charAt(4)) {
			case 'q':
				promotion = Promotion.QUEEN;
				break;
			case 'r':
				promotion = Promotion.ROOK;
				break;
			case 'b':
				promotion = Promotion.BISHOP;
				break;
			case 'n':
				promotion = Promotion.KNIGHT;
				break;
			default:
				throw new Exception("cannot promote to the piece");
			}
		}
	}

	// we have [8][8] board
	private boolean isValidPoint(int pointX, int pointY) {
		System.out.println("x: " + pointX + " y: " + pointY);
		return (0 <= pointX) && (pointX <= 8) && (0 <= pointY) && (pointY <= 8);
	}

	// getter section
	Point getFromPoint() {
		return fromPoint;
	}

	Point getToPoint() {
		return toPoint;
	}

	Promotion getPromotion() {
		return promotion;
	}

	// debug msg
	public void print() {
		System.out.println("fromPoint: " + fromPoint + " , toPoint: " + toPoint + " , prommotion: " + promotion);
	}
}