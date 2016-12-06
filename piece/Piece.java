package antiChess.piece;

import java.awt.Color;
import java.util.ArrayList;

import antiChess.MoveAnnotation;

public class Piece {
	// private vars
	protected int x;
	protected int y;
	protected int rank;
	protected int file;
	protected Color player;

	public Piece(int x, int y, Color color) {
		this.x = x;
		this.y = y;
		this.rank = 8-x;		// convert index to rank
		this.file = y;
		this.player = color;
	}

	// copy constructor
	public Piece(Piece copy) {
		this(copy.getX(), copy.getY(), copy.getPlayer());
	}

	// empty base function
	public ArrayList<MoveAnnotation> getPossibleMoves() {
		return new ArrayList<MoveAnnotation>();
	}

	// empty base function
	// TODO: implement inherited functions
	public ArrayList<MoveAnnotation> getCaptureMoves() {
		return new ArrayList<MoveAnnotation>();
	}

	// getters
	public int getX() { return x; }

	public int getY() { return y; }

	public int getRank() { return rank; }

	public int getFile() { return file; }

	public Color getPlayer() { return player; }

	// we have [8][8] board
	protected boolean isValidPoint(int pointX, int pointY) {
		System.out.println("x: " + pointX + " y: " + pointY);
		return (0 <= pointX) && (pointX <= 8) && (0 <= pointY) && (pointY <= 8);
	}	
}