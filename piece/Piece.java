package antiChess.piece;

import java.awt.Color;
import java.awt.Point;
import java.util.ArrayList;

import antiChess.MoveAnnotation;

public class Piece {
	// private vars
	protected int row;
	protected int col;
	protected int rank;
	protected int file;
	protected Color player;
	protected boolean moved;

	// *NOTE: receives an index pair
	public Piece(int row, int col, Color color) {
		this.row = row;
		this.col = col;
		this.rank = 8-row;		// convert index to rank
		this.file = col;
		this.player = color;
		this.moved = false;
	}

	// copy constructor
	public Piece(Piece copy) {
		this(copy.getRow(), copy.getCol(), copy.getPlayer());
		rank = 8-copy.getRow();
		file = copy.getCol();
		moved = copy.hasEverMoved();
	}

	// empty base function
	// returns a set of moves to empty cell or to capture the opponent's piece
	public ArrayList<MoveAnnotation> getPossibleMoves(Piece[][] board) throws Exception {
		return new ArrayList<MoveAnnotation>();
	}

	// empty base function
	// returns a set of moves to capture the opponent's piece only
	public ArrayList<MoveAnnotation> getAttackMoves(Piece[][] board) throws Exception {
		return new ArrayList<MoveAnnotation>();
	}

	// empty base function
	// gets a string that represents a piece
	public String getPieceString() {
		return "";
	}

	// getters
	public int getRow() { return row; }

	public int getCol() { return col; }

	public int getRank() { return rank; }

	public int getFile() { return file; }

	public Color getPlayer() { return player; }

	public boolean hasEverMoved() { return moved; }

	// setters

	// *NOTE: accepts an index pair
	public void updatePosition(int row, int col) {
		this.row = row;
		this.col = col;
		this.rank = 8-row;
		this.file = col;
		this.moved = true;
	}

	// we have [8][8] board
	// *NOTE: this checks the index pair
	protected boolean isValidPoint(Point point) {
		int r = getRow(point);
		int c = getCol(point);
		// System.out.println("r: " + r + " c: " + c);
		return (0 <= r) && (r <= 7) && (0 <= c) && (c <= 7);
	}

	protected boolean isValidPoint(int r, int c) {
		return (0 <= r) && (r <= 7) && (0 <= c) && (c <= 7);
	}

	// consumes next move as an index pair, makes a MoveAnnotation
	protected MoveAnnotation toMoveAnnotation(int r, int c) throws Exception {
		char f, f2;
		try {
			f = (char)('a' + file);
			f2 = (char)('a' + c);
		} catch (Exception e) {
			throw new Exception("index to moveString conversion error");
		}

		String moveString = String.valueOf(f) + Integer.toString(8 - rank) + String.valueOf(f2) + Integer.toString(8 - r);

		return new MoveAnnotation(moveString);
	}

	// check if cells between origin and destination are empty
	protected boolean isOpenPath(int origRow, int origCol, int destRow, int destCol, Piece[][] board) {
		// horizontal move
		if (origRow == destRow) {
			// move left
			if (origCol > destCol) {
				for (int c = destCol+1; c < origCol; c++) {
					if (board[row][c] != null) {
						return false;
					}
				}
			// move right
			} else {
				for (int c = origCol+1; c < destCol; c++) {
					if (board[row][c] != null) {
						return false;
					}
				}
			}
		// vertical move
		} else if (origCol == destCol) {
			// move down
			if (origRow > destRow) {
				for (int r = destRow+1; r < origRow; r++) {
					if (board[r][col] != null) {
						return false;
					}
				}
			// move up
			} else {
				for (int r = origRow+1; r < destRow; r++) {
					if (board[r][col] != null) {
						return false;
					}
				}
			}
		// diagonal move
		} else {
			int rOffset, cOffset, r = origRow, c = origCol;

			// move to bottom right
			if (destRow > origRow && destCol > origCol) {
				rOffset = 1;
				cOffset = 1;
			// move to top right
			} else if (destRow < origRow && destCol > origCol) {
				rOffset = -1;
				cOffset = 1;
			// move to top left
			} else if (destRow < origRow && destCol < origCol) {
				rOffset = -1;
				cOffset = -1;
			// move to bottom left
			} else {
				rOffset = 1;
				cOffset = -1;
			}

			r += rOffset;
			c += cOffset;
			while (isValidPoint(r, c) && (r != destRow && c != destCol)) {
				if (board[r][c] != null) {
					return false;
				}

				r += rOffset;
				c += cOffset;
			}
		}

		return true;
	}

	protected int getCol(Point point) {
		return point.x;
	}

	protected int getRow(Point point) {
		return point.y;
	}
}