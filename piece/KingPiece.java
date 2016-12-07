package antiChess.piece;

import java.awt.Color;
import java.awt.Point;
import java.util.ArrayList;
import antiChess.MoveAnnotation;

public class KingPiece extends Piece {

	public KingPiece(int x, int y, Color color) {
		super(x, y, color);
	}

	public KingPiece(Piece copy) {
		super(copy);
	}
	
	@Override
	// returns a set of moves to empty cell or to capture the opponent's piece
	public ArrayList<MoveAnnotation> getPossibleMoves(Piece[][] board) throws Exception {
		ArrayList<MoveAnnotation> possibleMoves = new ArrayList<MoveAnnotation>();

		return possibleMoves;
	}

	@Override
	// returns a set of moves to capture the opponent's piece only
	public ArrayList<MoveAnnotation> getAttackMoves(Piece[][] board) throws Exception {
		ArrayList<MoveAnnotation> attackMoves = new ArrayList<MoveAnnotation>();

		return attackMoves;
	}

	@Override
	// gets a string that represents a piece
	public String getPieceString() {
		if (player.equals(Color.WHITE)) {
			return "K";
		} else {
			return "k";
		}
	}

	// is (row, col) cell is being attacked by any opponent piece?
	// *NOTE: accepts an index pair
	private boolean isCellUnderAttack(int row, int col, Piece[][] board) throws Exception {
		for (int r=0; r<8; r++) {
			for (int c=0; c<8; c++) {
				if(board[r][c] != null && !board[r][c].getPlayer().equals(player)) {
					for (MoveAnnotation move : board[r][c].getPossibleMoves(board)) {
						Point toPoint = move.getToPoint();
						if (toPoint.y == row && toPoint.x == col) {
							return true;
						}
					}
				}
			}
		}

		return false;
	}
}
