package antiChess.piece;

import java.awt.Color;
import java.util.ArrayList;
import antiChess.MoveAnnotation;

public class KnightPiece extends Piece {

	public KnightPiece(int x, int y, Color color) {
		super(x, y, color);
	}

	public KnightPiece(Piece copy) {
		super(copy);
	}

	@Override
	// returns a set of moves to empty cell or to capture the opponent's piece
	public ArrayList<MoveAnnotation> getPossibleMoves(Piece[][] board) throws Exception {
		ArrayList<MoveAnnotation> possibleMoves = new ArrayList<MoveAnnotation>();
		Color opponent;

		if (player.equals(Color.WHITE)) {
			opponent = Color.BLACK;
		} else {
			opponent = Color.WHITE;
		}

		if (isValidPoint(row+2, col+1) &&
			(board[row+2][col+1] == null ||
			(board[row+2][col+1] != null && board[row+2][col+1].getPlayer().equals(opponent)))) {
			possibleMoves.add(toMoveAnnotation(row+2, col+1));
		}
		if (isValidPoint(row+1, col+2) &&
			(board[row+1][col+2] == null ||
			(board[row+1][col+2] != null && board[row+1][col+2].getPlayer().equals(opponent)))) {
			possibleMoves.add(toMoveAnnotation(row+1, col+2));
		}
		if (isValidPoint(row-1, col+2) &&
			(board[row-1][col+2] == null ||
			(board[row-1][col+2] != null && board[row-1][col+2].getPlayer().equals(opponent)))) {
			possibleMoves.add(toMoveAnnotation(row-1, col+2));
		}
		if (isValidPoint(row-2, col+1) &&
			(board[row-2][col+1] == null ||
			(board[row-2][col+1] != null && board[row-2][col+1].getPlayer().equals(opponent)))) {
			possibleMoves.add(toMoveAnnotation(row-2, col+1));
		}
		if (isValidPoint(row-2, col-1) &&
			(board[row-2][col-1] == null ||
			(board[row-2][col-1] != null && board[row-2][col-1].getPlayer().equals(opponent)))) {
			possibleMoves.add(toMoveAnnotation(row-2, col-1));
		}
		if (isValidPoint(row-1, col-2) &&
			(board[row-1][col-2] == null ||
			(board[row-1][col-2] != null && board[row-1][col-2].getPlayer().equals(opponent)))) {
			possibleMoves.add(toMoveAnnotation(row-1, col-2));
		}
		if (isValidPoint(row+1, col-2) &&
			(board[row+1][col-2] == null ||
			(board[row+1][col-2] != null && board[row+1][col-2].getPlayer().equals(opponent)))) {
			possibleMoves.add(toMoveAnnotation(row+1, col-2));
		}
		if (isValidPoint(row+2, col-1) &&
			(board[row+2][col-1] == null ||
			(board[row+2][col-1] != null && board[row+2][col-1].getPlayer().equals(opponent)))) {
			possibleMoves.add(toMoveAnnotation(row+2, col-1));
		}

		return possibleMoves;
	}

		@Override
	// returns a set of moves to capture the opponent's piece only
	public ArrayList<MoveAnnotation> getAttackMoves(Piece[][] board) throws Exception {
		ArrayList<MoveAnnotation> attackMoves = new ArrayList<MoveAnnotation>();
		Color opponent;

		if (player.equals(Color.WHITE)) {
			opponent = Color.BLACK;
		} else {
			opponent = Color.WHITE;
		}

		if (isValidPoint(row+2, col+1) &&
			board[row+2][col+1] != null &&
			board[row+2][col+1].getPlayer().equals(opponent)) {
			attackMoves.add(toMoveAnnotation(row+2, col+1));
		}
		if (isValidPoint(row+1, col+2) &&
			board[row+1][col+2] != null &&
			board[row+1][col+2].getPlayer().equals(opponent)) {
			attackMoves.add(toMoveAnnotation(row+1, col+2));
		}
		if (isValidPoint(row-1, col+2) &&
			board[row-1][col+2] != null &&
			board[row-1][col+2].getPlayer().equals(opponent)) {
			attackMoves.add(toMoveAnnotation(row-1, col+2));
		}
		if (isValidPoint(row-2, col+1) &&
			board[row-2][col+1] != null &&
			board[row-2][col+1].getPlayer().equals(opponent)) {
			attackMoves.add(toMoveAnnotation(row-2, col+1));
		}
		if (isValidPoint(row-2, col-1) &&
			board[row-2][col-1] != null &&
			board[row-2][col-1].getPlayer().equals(opponent)) {
			attackMoves.add(toMoveAnnotation(row-2, col-1));
		}
		if (isValidPoint(row-1, col-2) &&
			board[row-1][col-2] != null &&
			board[row-1][col-2].getPlayer().equals(opponent)) {
			attackMoves.add(toMoveAnnotation(row-1, col-2));
		}
		if (isValidPoint(row+1, col-2) &&
			board[row+1][col-2] != null &&
			board[row+1][col-2].getPlayer().equals(opponent)) {
			attackMoves.add(toMoveAnnotation(row+1, col-2));
		}
		if (isValidPoint(row+2, col-1) &&
			board[row+2][col-1] != null &&
			board[row+2][col-1].getPlayer().equals(opponent)) {
			attackMoves.add(toMoveAnnotation(row+2, col-1));
		}

		return attackMoves;
	}

	@Override
	// gets a string that represents a piece
	public String getPieceString() {
		if (player.equals(Color.WHITE)) {
			return "N";
		} else {
			return "n";
		}
	}
}
