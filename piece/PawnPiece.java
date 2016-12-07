package antiChess.piece;

import java.awt.Color;
import java.util.ArrayList;
import antiChess.MoveAnnotation;

public class PawnPiece extends Piece {

	public PawnPiece(int x, int y, Color color) {
		super(x, y, color);
	}

	public PawnPiece(Piece copy) {
		super(copy);
	}
	
	@Override
	// returns a set of moves to empty cell or to capture the opponent's piece
	public ArrayList<MoveAnnotation> getPossibleMoves(Piece[][] board) {
		if (player.equals(Color.WHITE)) {
		} else {
		}
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
			return "P";
		} else {
			return "p";
		}
	}
}
