package antiChess.piece;

import java.awt.Color;
import java.util.ArrayList;
import antiChess.MoveAnnotation;

public class RookPiece extends Piece {

	public RookPiece(int x, int y, Color color) {
		super(x, y, color);
	}

	public RookPiece(Piece copy) {
		super(copy);
	}

	@Override
	// returns a set of moves to empty cell or to capture the opponent's piece
	public ArrayList<MoveAnnotation> getPossibleMoves(Piece[][] board) throws Exception {
		ArrayList<MoveAnnotation> possibleMoves = new ArrayList<MoveAnnotation>();

		for (int r = 0; r < 8; r++) {
			if ((board[r][col] == null || (board[r][col] != null && !board[r][col].getPlayer().equals(player))) &&
				isOpenPath(row, col, r, col, board)) {
				possibleMoves.add(toMoveAnnotation(r, col));
			}
		}

		for (int c = 0; c < 8; c++) {
			if ((board[row][c] == null || (board[row][c] != null && !board[row][c].getPlayer().equals(player))) &&
				isOpenPath(row, col, row, c, board)) {
				possibleMoves.add(toMoveAnnotation(row, c));
			}
		}

		return possibleMoves;
	}

	@Override
	// returns a set of moves to capture the opponent's piece only
	public ArrayList<MoveAnnotation> getAttackMoves(Piece[][] board) throws Exception {
		ArrayList<MoveAnnotation> attackMoves = new ArrayList<MoveAnnotation>();

		for (int r = 0; r < 8; r++) {
			if (board[r][col] != null &&
				!board[r][col].getPlayer().equals(player) &&
				isOpenPath(row, col, r, col, board)) {
				attackMoves.add(toMoveAnnotation(r, col));
			}
		}

		for (int c = 0; c < 8; c++) {
			if (board[row][c] != null &&
				!board[row][c].getPlayer().equals(player) &&
				isOpenPath(row, col, row, c, board)) {
				attackMoves.add(toMoveAnnotation(row, c));
			}
		}

		return attackMoves;
	}

	@Override
	// gets a string that represents a piece
	public String getPieceString() {
		if (player.equals(Color.WHITE)) {
			return "R";
		} else {
			return "r";
		}
	}
}
