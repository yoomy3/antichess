package antiChess.piece;

import java.awt.Color;
import java.util.ArrayList;
import antiChess.MoveAnnotation;

public class BishopPiece extends Piece{

	public BishopPiece(int x, int y, Color color) {
		super(x, y, color);
	}

	public BishopPiece(Piece copy) {
		super(copy);
	}

	@Override
	// returns a set of moves to empty cell or to capture the opponent's piece
	public ArrayList<MoveAnnotation> getPossibleMoves(Piece[][] board) throws Exception {
		ArrayList<MoveAnnotation> possibleMoves = new ArrayList<MoveAnnotation>();
		int r, c, rOffset = 0, cOffset = 0;

		// look for 4 diagonal directions
		for (int i = 0; i < 4; i++) {
			r = row;
			c = col;

			switch (i) {
				case 0:
					rOffset = 1;
					cOffset = 1;
					break;
				case 1:
					rOffset = -1;
					cOffset = 1;
					break;
				case 2:
					rOffset = -1;
					cOffset = -1;
					break;
				case 3:
					rOffset = 1;
					cOffset = -1;
					break;
			}

			r += rOffset;
			c += cOffset;
			while (isValidPoint(r, c)) {
				// open path && (no piece || opponent piece)
				if (isOpenPath(row, col, r, c, board) &&
					(board[r][c] == null ||
					(board[r][c] != null && !board[r][c].getPlayer().equals(player)))) {
					possibleMoves.add(toMoveAnnotation(r, c));
				}

				r += rOffset;
				c += cOffset;
			}
		}

		return possibleMoves;
	}

		@Override
	// returns a set of moves to capture the opponent's piece only
	public ArrayList<MoveAnnotation> getAttackMoves(Piece[][] board) throws Exception {
		ArrayList<MoveAnnotation> attackMoves = new ArrayList<MoveAnnotation>();
		int r, c, rOffset = 0, cOffset = 0;

		// look for 4 diagonal directions
		for (int i = 0; i < 4; i++) {
			r = row;
			c = col;

			switch (i) {
				case 0:
					rOffset = 1;
					cOffset = 1;
					break;
				case 1:
					rOffset = -1;
					cOffset = 1;
					break;
				case 2:
					rOffset = -1;
					cOffset = -1;
					break;
				case 3:
					rOffset = 1;
					cOffset = -1;
					break;
			}

			r += rOffset;
			c += cOffset;
			while (isValidPoint(r, c)) {
				// open path && opponent piece
				if (isOpenPath(row, col, r, c, board) &&
					board[r][c] != null && !board[r][c].getPlayer().equals(player)) {
					attackMoves.add(toMoveAnnotation(r, c));
				}

				r += rOffset;
				c += cOffset;
			}
		}

		return attackMoves;
	}

	@Override
	// gets a string that represents a piece
	public String getPieceString() {
		if (player.equals(Color.WHITE)) {
			return "B";
		} else {
			return "b";
		}
	}
}
