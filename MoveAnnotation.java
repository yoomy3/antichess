package antiChess;
import java.awt.Point;

public class MoveAnnotation {
	private String move;
	private Point fromPoint;
	private Point toPoint;
	private Promotion promotion;
	private boolean claimDraw; // whether the player wants to claim the draw

	// Pawn promotion
	public enum Promotion {
		QUEEN, ROOK, BISHOP, KNIGHT
	}

	public MoveAnnotation(String move) throws Exception {
		this.move = move;
		getClaimDraw(move);
		getFromPoint(move);
		getToPoint(move);
		getPromotion(move);
	}

	// get whether player claims the draw
	private void getClaimDraw(String move) {
		claimDraw = (move.contains("1/2-1/2"));
	}
	
	// get from point
	// *NOTE: Point represents an index pair
	private void getFromPoint(String move) throws Exception {
		int fromCol = move.charAt(0) - 'a';
		int fromRow = move.charAt(1) - '1';
		if (isValidPoint(fromCol, fromRow)) {
			// convert to index
			fromRow = 7-fromRow;
			fromPoint = new Point(fromCol, fromRow);
		} else {
			throw new Exception("from point is invalid");
		}
	}

	// get to point
	// *NOTE: Point represents an index pair
	private void getToPoint(String move) throws Exception {
		int toCol = move.charAt(2) - 'a';
		int toRow = move.charAt(3) - '1';
		if (isValidPoint(toCol, toRow)) {
			// convert to index
			toRow = 7-toRow;
			toPoint = new Point(toCol, toRow);
		} else {
			throw new Exception("to point is invalid");
		}
	}

	// get promotion enum
	private void getPromotion(String move) throws Exception {
		if ((move.length() > 4) && (!claimDraw)) {
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
	// *NOTE: checks rank and file
	private boolean isValidPoint(int pointX, int pointY) {
		return claimDraw || ((0 <= pointX) && (pointX < 8) && (0 <= pointY) && (pointY < 8));
	}

	// getter section
	String getMoveString() {
		return move;
	}
	
	// *NOTE: returns index, need a convertion for rank/file
	Point getFromPoint() {
		return fromPoint;
	}

	// *NOTE: returns index, need a convertion for rank/file
	public Point getToPoint() {
		return toPoint;
	}

	Promotion getPromotion() {
		return promotion;
	}
	
	boolean getClaimDraw() {
		return claimDraw;
	}
	
	// debug msg
	public void print() {
		System.out.println("move: " + move + "fromPoint: " + fromPoint + " , toPoint: " + toPoint + " , prommotion: " + promotion
				+ " , claimDraw: " + claimDraw);
	}
}