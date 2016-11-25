package antiChess.piece;

import java.awt.Color;
import java.util.ArrayList;

import antiChess.MoveAnnotation;

public class Piece {
	private Color player;
	
	public Piece() {}
	
	public Color getPlayer() {return player;}
	public ArrayList<MoveAnnotation> getPossibleMoves() {return new ArrayList<MoveAnnotation>();}
}