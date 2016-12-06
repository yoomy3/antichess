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
	public ArrayList<MoveAnnotation> getPossibleMoves() {
		// TODO: write this
		return new ArrayList<MoveAnnotation>();
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
