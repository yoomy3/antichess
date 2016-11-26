package antiChess.piece;

import java.awt.Color;
import java.util.ArrayList;
import antiChess.MoveAnnotation;

public class KnightPiece extends Piece {

	public KnightPiece(int x, int y, Color color) {
		super(x, y, color);
	}

	public KnightPiece(Piece copy) {
		super(copy);
	}

	@Override
	public ArrayList<MoveAnnotation> getPossibleMoves() {
		// TODO: write this
		return new ArrayList<MoveAnnotation>();
	}
}
