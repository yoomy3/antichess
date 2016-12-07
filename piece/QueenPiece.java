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
	public ArrayList<MoveAnnotation> getPossibleMoves(Piece[][] board) {
		// TODO: write this
		return new ArrayList<MoveAnnotation>();
	}

		@Override
	// returns a set of moves to capture the opponent's piece only
	public ArrayList<MoveAnnotation> getAttackMoves(Piece[][] board) {
		return new ArrayList<MoveAnnotation>();
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
