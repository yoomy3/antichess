package antiChess.piece;

import java.awt.Color;
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
	public ArrayList<MoveAnnotation> getPossibleMoves() {
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
	}

	// we have [8][8] board
	protected boolean isValidPoint(int pointX, int pointY) {
		System.out.println("x: " + pointX + " y: " + pointY);
		return (0 <= pointX) && (pointX <= 8) && (0 <= pointY) && (pointY <= 8);
	}	
}