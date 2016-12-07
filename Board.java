package antiChess;

import java.awt.Color;
import java.awt.Point;
import java.util.Set;
import java.util.Iterator;
import java.util.ArrayList;
import java.util.Hashtable;

import antiChess.piece.*;

public class Board {
	// private variables
	Piece[][] pieces; // 8x8 board
	Color player; // either WHITE/BLACK
	Color winner; // set up winner when finished
	boolean isWhite; 
	
	boolean enPassant;
	boolean gameFinished; // set up game finished when finished
	boolean isDrawn = false;

	int fiftyMoveCount;

	Castling castling;
	boolean isCastled;
	
	boolean checked;	// is our King checked?
	boolean checkMate;

	// hashtable to keep all the states - check for threefold repetition
	Hashtable<Integer, Repetition> states = new Hashtable<Integer, Repetition>();

	private class Repetition {
		int count; // number of occurrences for the move
		MoveAnnotation move; 
		
		Repetition(int count, MoveAnnotation move) {
			this.count = count;
			this.move = move;
		}
		
		int getCount() {return count;}
		MoveAnnotation getMove() {return move;}
	}
	
	// King/Queen side castling
	private enum Castling {
		NONE, KING, QUEEN
	}

	// copy constructor;
	public Board(Board anotherBoard) {
		pieces = new Piece[8][8];
		for (int r=0; r<8; r++) {
			for (int c=0; c<8; c++) {
				Piece piece = anotherBoard.pieces[r][c];
				if (piece != null) {
					if (piece instanceof PawnPiece) {
						pieces[r][c] = new PawnPiece(piece);
					} else if (piece instanceof BishopPiece) {
						pieces[r][c] = new BishopPiece(piece);
					} else if (piece instanceof KingPiece) {
						pieces[r][c] = new KingPiece(piece);
					} else if (piece instanceof KnightPiece) {
						pieces[r][c] = new KnightPiece(piece);
					} else if (piece instanceof QueenPiece) {
						pieces[r][c] = new QueenPiece(piece);
					} else if (piece instanceof RookPiece) {
						pieces[r][c] = new RookPiece(piece);
					}
				}
			}
		}
		player = anotherBoard.player;
		winner = anotherBoard.winner;
		isWhite = anotherBoard.isWhite;
		enPassant = anotherBoard.enPassant;
		gameFinished = anotherBoard.gameFinished;
		fiftyMoveCount = anotherBoard.fiftyMoveCount;
		castling = anotherBoard.castling;
		isCastled = anotherBoard.isCastled;
		checked = anotherBoard.checked;
		checkMate = anotherBoard.checkMate;
		states = anotherBoard.states;
	}

	public Board(Color playerColor) throws Exception {
		// decide which side I am playing
		player = playerColor;

		// initialize 32 pieces
		pieces = new Piece[8][8];
		initPieces();

		System.err.println("## Player" + player.toString() + " board initialized");

		castling = Castling.NONE;
	}

	// empty default constructore;
	public Board() {}


	///////////////////////////////////
	//        Getter Section         //
	///////////////////////////////////

	public Piece[][] getPieces() { return pieces; }

	public Piece getPiecesAt(int r, int c) { return pieces[r][c]; }

	public Color getPlayer() { return player; }

	public Color getWinner() { return winner; }

	public boolean isGameFinished() { return gameFinished; }

	public boolean isDrawn() { return isDrawn; }

	///////////////////////////////////
	//        Public Helpers         //
	///////////////////////////////////

	// make a move, update the board
	public Board takeMove(MoveAnnotation move) throws Exception {
		if (move.getClaimDraw()) {
			this.gameFinished = true;
			this.isDrawn = true;
			return this;
		}
		
		Point fromPoint = move.getFromPoint();
		Point toPoint = move.getToPoint();

		// who's piece is moving?
		Color movingPlayer = pieces[getRow(fromPoint)][getCol(fromPoint)].getPlayer();

		// see if a piece is captured
		if (pieces[getRow(toPoint)][(getCol(toPoint))] != null) {
			fiftyMoveCount = 0;
		} else {
			fiftyMoveCount++;
		}

		// castling: move is one of (e1g1, e1c1, e8g8, e8c8)
		String moveString = move.getMoveString();
		if (moveString.equals("e1g1") || moveString.equals("e1c1") || moveString.equals("e8g8") || moveString.equals("e8c8")) {
			switch (moveString) {
				// move Rock
				case "e1g1":
					pieces[7][5] = pieces[7][7];
					pieces[7][5].updatePosition(7, 5);
					pieces[7][7] = null;
					break;
				case "e1c1":
					pieces[7][3] = pieces[7][0];
					pieces[7][3].updatePosition(7, 3);
					pieces[7][0] = null;
					break;
				case "e8g8":
					pieces[0][5] = pieces[0][7];
					pieces[0][5].updatePosition(0, 5);
					pieces[0][7] = null;
					break;
				case "e8c8":
					pieces[0][3] = pieces[0][0];
					pieces[0][3].updatePosition(0, 3);
					pieces[0][0] = null;
					break;
				default:
					throw new Exception("invalid castling move string");
			}
		}

		// TODO: check EnPassant

		// move a piece, update the board
		pieces[getRow(toPoint)][getCol(toPoint)] = pieces[getRow(fromPoint)][getCol(fromPoint)];
		pieces[getRow(toPoint)][getCol(toPoint)].updatePosition(getRow(toPoint), getCol(toPoint));
		pieces[getRow(fromPoint)][getCol(fromPoint)] = null;

		// add state hash
		int stateHash = getBoardHash();
		if (states.containsKey(stateHash)) {
			int c = states.get(stateHash).getCount();
			c += 1;
			states.put(stateHash, new Repetition(c, move));
		} else {
			states.put(stateHash, new Repetition(1, move));
		}

		checkGameOver();
		return this;
	}
		

	public ArrayList<MoveAnnotation> getPossibleAttackMoves() throws Exception {
		ArrayList<MoveAnnotation> possibleAttacks = new ArrayList<MoveAnnotation>();
		for (int r=0; r<8; r++) {
			for (int c=0; c<8; c++) {
				Piece piece = pieces[r][c];
				if(piece != null && !piece.getPlayer().equals(player)) {
					possibleAttacks.addAll(piece.getAttackMoves(pieces));
				}
			}
		}

		return possibleAttacks;
	}

	// list all possible moves from the current state
	// it's our turn!
	public ArrayList<MoveAnnotation> getPossibleMoves() throws Exception {
		ArrayList<MoveAnnotation> possibleMoves = new ArrayList<MoveAnnotation>();

		// checked?
		ArrayList<MoveAnnotation> possibleAttacks = getPossibleAttackMoves();

		for (MoveAnnotation move : possibleAttacks) {
			Point capturePoint = move.getToPoint();
			if(pieces[getRow(capturePoint)][getCol(capturePoint)] instanceof KingPiece) {
				checked = true;
				break;
			}
		}

		// protect the King!
		if (checked) {
			// move the King
			for (int r=0; r<8; r++) {
				for (int c=0; c<8; c++) {
					if(pieces[r][c] != null && pieces[r][c].getPlayer().equals(player) && pieces[r][c] instanceof KingPiece) {
						possibleMoves.addAll(pieces[r][c].getPossibleMoves(pieces));
					}
				}
			}
			// TODO: block the attack with other pieces

			// we lost..
			if (possibleMoves.isEmpty()) {
				checkMate = true;
				gameFinished = true;
				if (isWhite) {
					winner = Color.BLACK;
				} else {
					winner = Color.WHITE;
				}
				possibleMoves.add(new MoveAnnotation("0-1"));
			}
		} else {
		// not checked
			for (int r=0; r<8; r++) {
				for (int c=0; c<8; c++) {
					if(pieces[r][c] != null && pieces[r][c].getPlayer().equals(player)) {
						ArrayList<MoveAnnotation> curMoves = pieces[r][c].getPossibleMoves(pieces);
						possibleMoves.addAll(curMoves);
					}
				}
			}

			// check if our next move is forced
			// keep moves which we capture opponent's piece
			if (forcedMove(possibleMoves)) {
				Iterator<MoveAnnotation> it = possibleMoves.iterator();
				while (it.hasNext()) {
					Point point = it.next().getToPoint();
					if(pieces[getRow(point)][getCol(point)] == null) {
						it.remove();
					}
				}
			} else {
			// our next is not forced
				int i = 0;
				if (isWhite) {
					i = 7;
				}

				// can we do castling?
				canCastle();
				try {
					if (castling == Castling.KING) {
						possibleMoves.add(new MoveAnnotation(String.format("e%dg%d", convert(i), convert(i))));
					} else if (castling == Castling.QUEEN) {
						possibleMoves.add(new MoveAnnotation(String.format("e%dc%d", convert(i), convert(i))));
					}
				} catch (Exception e) {
					throw new Exception("invalid string format conversion from castling");
				}

				// can we call a draw?
				// * 50-move rule
				if (fiftyMoveCount == 50) {
					possibleMoves.add(new MoveAnnotation("1/2-1/2"));
				} 

				// * threefold repetition
				Set<Integer> keys = states.keySet();
				for (int key: keys){
					Repetition repetition = states.get(key);
					MoveAnnotation possibleMove = repetition.getMove();
					if ((repetition.getCount() >= 3) && (possibleMoves.contains(possibleMove))) {
						possibleMoves.add(new MoveAnnotation(repetition.getMove().getMoveString() + ",1/2-1/2"));
					}
				}
			}
		} // checked?


		return possibleMoves;
	}
	
	public void printBoard() {
		System.err.println("     a   b   c   d   e   f   g   h  ");
		for (int r=0; r<8; r++) {
			System.err.println("   ---------------------------------");
			String s = (8 - r) + "  | ";
			for (int c=0; c<8; c++) {
				if (pieces[r][c] == null) {
					s = s + " " + " | ";
				} else {
					s = s + pieces[r][c].getPieceString() + " | ";
				}
			}
			s += " " + (8 - r);
			System.err.println(s);
		}
		System.err.println("   ---------------------------------");
		System.err.println("     a   b   c   d   e   f   g   h  ");
	}

	public void printAllPossibleMoves(ArrayList<MoveAnnotation> possibleMoves) {
		String moves = "All possible moves: ";
		for (MoveAnnotation move : possibleMoves) {
			moves += " ," + move.getMoveString();
		}
		System.err.println(moves);
		System.err.println("## " + possibleMoves.size() + " total possible moves");
	}

	// is (row, col) cell is being attacked by any opponent piece?
	// *NOTE: accepts an index pair
	public boolean isCellUnderAttack(int row, int col) throws Exception {
		for (int r=0; r<8; r++) {
			for (int c=0; c<8; c++) {
				if(pieces[r][c] != null && !pieces[r][c].getPlayer().equals(player)) {
					for (MoveAnnotation move : pieces[r][c].getPossibleMoves(pieces)) {
						Point toPoint = move.getToPoint();
						if (getRow(toPoint) == row && getCol(toPoint) == col) {
							return true;
						}
					}
				}
			}
		}

		return false;
	}


	///////////////////////////////////
	//        Private Helpers        //
	///////////////////////////////////
	
	// check whether the game is finished or not
	private void checkGameOver() {
		boolean whiteKing = false;
		boolean blackKing = false;
		for (int r = 0; r < 8; r++) {
			for (int c = 0; c < 8; c++) {
				Piece piece = pieces[r][c];
				if (piece != null) {
					if(piece instanceof KingPiece) {
						if (piece.getPlayer().equals(Color.WHITE)) {
							whiteKing = true;
						} else {
							blackKing = true;
						}
					}
				}
			}
		}
		if (!whiteKing) {
			gameFinished = true;
			winner = Color.BLACK;
		} else if (!blackKing) {
			gameFinished = true;
			winner = Color.WHITE;
		}
	}
	
	// get the hash value of the current state
	private int getBoardHash() {
		String res = "";
		for (int r = 0; r < 8; r++) {
			for (int c = 0; c < 8; c++) {
				if (pieces[r][c] != null) {
					res += pieces[r][c].getPieceString();
				} else {
					res += ".";
				}
			}
		}
		return res.hashCode();
	}

	// initialize pieces
	private void initPieces() throws Exception {
		int r = 0;
		int offset = 1;
		Color player = Color.BLACK;

		for (int i = 0; i < 2; i++) {
			if (i == 1) {
				r = 7;
				offset = -1;
				player = Color.WHITE;
			}

			// add other pieces
			for (int c = 0; c < 8; c++) {
				switch (c) {
					case 0:
					case 7:
						pieces[r][c] = new RookPiece(r, c, player);
						break;
					case 1:
					case 6:
						pieces[r][c] = new KnightPiece(r, c, player);
						break;
					case 2:
					case 5:
						pieces[r][c] = new BishopPiece(r, c, player);
						break;
					case 3:
						pieces[r][c] = new QueenPiece(r, c, player);
						break;
					case 4:
						pieces[r][c] = new KingPiece(r, c, player);
						break;
					default:
						throw new Exception("invalid board index");
				}
			}

			r += offset;
			// add pawns
			for (int c = 0; c < 8; c++) {
				pieces[r][c] = new PawnPiece(r, c, player);
			}
		}
	}

	// convert between index and rank
	private int convert(int i) {
		return 8 - i;
	}

	private void canCastle() throws Exception {
		int row = 0;
		if (isWhite) {
			row = 7;
		}

		if (isCastled || checked) {
			return;
		}

		// Conditions for castling
		// * Castling has not been done yet (can be done only once throughout the entire game)
		// * King and Rook that are being swapped have not been moved yet
		// * Cells between King and the moving Rook are empty
		// * Cells that King passes through are NOT under attack

		// king side castling
		if (pieces[row][4] instanceof KingPiece &&
			!pieces[row][4].hasEverMoved() &&
			pieces[row][5] == null &&
			!isCellUnderAttack(row, 5) &&
			pieces[row][6] == null &&
			!isCellUnderAttack(row, 6) &&
			pieces[row][7] instanceof RookPiece &&
			!pieces[row][7].hasEverMoved()) {
			castling = Castling.KING;
		}
		// queen side castling
		else if (pieces[row][0] instanceof RookPiece &&
				!pieces[row][0].hasEverMoved() &&
				pieces[row][1] == null &&
				pieces[row][2] == null &&
				!isCellUnderAttack(row, 2) &&
				pieces[row][3] == null &&
				!isCellUnderAttack(row, 3) &&
				pieces[row][4] instanceof KingPiece &&
				!pieces[row][4].hasEverMoved()) {
			castling = Castling.QUEEN;
		}
	}

	// check if our next move is forced
	private boolean forcedMove(ArrayList<MoveAnnotation> possibleMoves) {
		for (MoveAnnotation move : possibleMoves) {
			Point point = move.getToPoint();
			if(pieces[getRow(point)][getCol(point)] != null) {
				return true;
			}
		}
		return false;
	}

	private int getCol(Point point) {
		return point.x;
	}

	private int getRow(Point point) {
		return point.y;
	}
}