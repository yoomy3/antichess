package antiChess.piece;

import java.awt.Color;
import java.util.ArrayList;
import antiChess.MoveAnnotation;

public class QueenPiece extends Piece {

	public QueenPiece(int x, int y, Color color) {
		super(x, y, color);
	}

	public QueenPiece(Piece copy) {
		super(copy);
	}
	
	@Override
	// returns a set of moves to empty cell or to capture the opponent's piece
	public ArrayList<MoveAnnotation> getPossibleMoves(Piece[][] board) throws Exception {
		ArrayList<MoveAnnotation> possibleMoves = new ArrayList<MoveAnnotation>();

		// Rook move - look for horizontal & vertical directions
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

		// Bishop move - look for 4 diagonal directions
		int r, c, rOffset = 0, cOffset = 0;
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

		// Rook move - look for horizontal & vertical directions
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

		// Bishop move - look for 4 diagonal directions
		int r, c, rOffset = 0, cOffset = 0;
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
			return "Q";
		} else {
			return "q";
		}
	}
}
