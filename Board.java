package antiChess;

import java.awt.Color;
import java.util.ArrayList;

import antiChess.piece.Piece;

public class Board {
	// private variables
	private Piece[][] pieces; // 8x8 board
	private Color player; // either WHITE/BLACK
	boolean forcedMove;
	
	boolean isCaptured;
	int fiftyMoveCount;

	boolean isCastled;
	
	public Board() {
		// initialize 32 pieces
	}
	
	// copy constructore;
	public Board(Board another) {}

	// make a move, update the board
	void makeMove(Board board) {
	}
	
	// make a move, update the board
	Board takeMove(MoveAnnotation move) {
		return new Board();
	}
		
	// list all possible moves from the current state
	// Vector<MoveAnnotation, Board> possibleMoves() {}
	ArrayList<MoveAnnotation> getPossibleMoves() {
		ArrayList<MoveAnnotation> possibleMoves = new ArrayList<MoveAnnotation>();
		for (int i=0; i<8; i++) {
			for (int j=0; j<8; j++) {
				Piece piece = pieces[i][j];
				if(piece != null) {
					ArrayList<MoveAnnotation> curMoves = piece.getPossibleMoves();
					possibleMoves.addAll(curMoves);
				}
			}
		}
		return new ArrayList<MoveAnnotation>();
	}
	
	// getter section
	public Piece[][] getPieces() {return pieces;}
	public Color getPlayer() {return player;}
}