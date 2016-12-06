package antiChess;

import java.awt.Color;
import java.awt.Point;
import java.util.Iterator;
import java.util.ArrayList;

import antiChess.piece.*;

public class Board {
	// private variables
	Piece[][] pieces; // 8x8 board
	Color player; // either WHITE/BLACK
	Color winner; // TODO: set up winner when finished
	boolean isWhite; 
	
	boolean enPassant;
	boolean gameFinished; // TODO: set up game finished when finished

	int fiftyMoveCount;

	Castling castling;
	boolean isCastled;
	
	boolean checked;
	boolean checkMate;

	// TODO: add hash table to check threefold repetition

	// King/Queen side castling
	private enum Castling {
		NONE, KING, QUEEN
	}

	// copy constructor;
	public Board(Board anotherBoard) {
		pieces = new Piece[8][8];
		for (int r=0; r<8; r++) {
			for (int c=0; c<8; c++) {
				pieces[r][c] = new Piece(anotherBoard.pieces[r][c]);
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
	}

	public Board(String color) throws Exception {
		// decide which side I am playing
		if (color.toLowerCase().equals("black")) {
			player = Color.BLACK;
		} else {
			player = Color.WHITE;
			isWhite = true;
		}

		// initialize 32 pieces
		pieces = new Piece[8][8];
		initPieces(pieces, player);

		castling = Castling.NONE;
	}

	// empty default constructore;
	public Board() {}
	
	// make a move, update the board
	public Board takeMove(MoveAnnotation move) {
		Point fromPoint = move.getFromPoint();
		Point toPoint = move.getToPoint();
		
		// who's piece is moving?
		Color movingPlayer = pieces[fromPoint.y][fromPoint.x].getPlayer();
		
		// see if a piece is captured
		if (pieces[toPoint.y][toPoint.x] != null) {
			fiftyMoveCount = 0;
		} else {
			fiftyMoveCount++;
		}

		// TODO: castling? -> check move is one of (e1g1, e1c1, e8g8, e8c8)

		// TODO: check EnPassant

		pieces[toPoint.y][toPoint.x] = pieces[fromPoint.y][fromPoint.x];
		pieces[fromPoint.y][fromPoint.x] = null;

		return this;
	}
		
	// list all possible moves from the current state
	// it's our turn!
	ArrayList<MoveAnnotation> getPossibleMoves() throws Exception {
		ArrayList<MoveAnnotation> possibleMoves = new ArrayList<MoveAnnotation>();
		
		// checked?
		ArrayList<MoveAnnotation> possibleAttacks = new ArrayList<MoveAnnotation>();
		for (int r=0; r<8; r++) {
			for (int c=0; c<8; c++) {
				Piece piece = pieces[r][c];
				if(piece != null && piece.getPlayer().equals(player)) {
					possibleAttacks.addAll(piece.getCaptureMoves());
				}
			}
		}
		for (MoveAnnotation move : possibleAttacks) {
			Point capturePoint = move.getToPoint();
			if(pieces[capturePoint.y][capturePoint.x] instanceof KingPiece) {
				checked = true;
				break;
			}
		}

		// must move King?
		if (checked) {
			for (int r=0; r<8; r++) {
				for (int c=0; c<8; c++) {
					Piece piece = pieces[r][c];
					if(piece != null && piece.getPlayer().equals(player) && piece instanceof KingPiece) {
						possibleMoves.addAll(piece.getPossibleMoves());
					}
				}
			}
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
					Piece piece = pieces[r][c];
					if(piece != null && piece.getPlayer().equals(player)) {
						ArrayList<MoveAnnotation> curMoves = piece.getPossibleMoves();
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
					if(pieces[point.y][point.x] == null) {
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
				if (fiftyMoveCount == 50) {
					possibleMoves.add(new MoveAnnotation("1/2-1/2"));
				}  // TODO: else if ( any of hashtable entry has >= 3? )
			}
		} // checked?
		

		return possibleMoves;
	}
	
	// getter section
	public Piece[][] getPieces() { return pieces; }

	public Piece getPiecesAt(int r, int c) { return pieces[r][c]; }

	public Color getPlayer() { return player; }

	public Color getWinner() { return winner; }
	
	public boolean isGameFinished() { return gameFinished; }
	
	public void printBoard() {
		// TODO: print the current state
		return;
	}

	// TODO: get the hash value of the current state
	public String getBoardHash() {
		return "";
	}

	// initialize pieces
	private void initPieces(Piece[][] board, Color player) throws Exception {
		int r = 0;
		int offset = 1;
		if (isWhite) {
			r = 7;
			offset = -1;
		}

		// add other pieces
		for (int c = 0; c < 8; c++) {
			switch (c) {
				case 0:
				case 7:
					board[r][c] = new RookPiece(r, c, player);
					break;
				case 1:
				case 6:
					board[r][c] = new KnightPiece(r, c, player);
					break;
				case 2:
				case 5:
					board[r][c] = new BishopPiece(r, c, player);
					break;
				case 3:
					board[r][c] = new QueenPiece(r, c, player);
					break;
				case 4:
					board[r][c] = new KingPiece(r, c, player);
					break;
				default:
					throw new Exception("invalid board index");
			}
		}

		r += offset;
		// add pawns
		for (int c = 0; c < 8; c++) {
			board[r][c] = new PawnPiece(r, c, player);
		}
	}

	// convert between index and rank
	private int convert(int i) {
		return 8 - i;
	}

	private void canCastle() {
		boolean res = false;
		
		int row = 0;
		if (isWhite) {
			row = 7;
		}

		// king side castling
		if (pieces[row][4] instanceof KingPiece &&
			pieces[row][5] == null &&
			pieces[row][6] == null &&
			pieces[row][7] instanceof RookPiece &&
			!isCastled) {
			castling = Castling.KING;
		}
		// queen side castling
		else if (pieces[row][0] instanceof RookPiece &&
					pieces[row][1] == null &&
					pieces[row][2] == null &&
					pieces[row][3] == null &&
					pieces[row][4] instanceof KingPiece &&
					!isCastled) {
			castling = Castling.QUEEN;
		}
	}

	// check if our next move is forced
	private boolean forcedMove(ArrayList<MoveAnnotation> possibleMoves) {
		for (MoveAnnotation move : possibleMoves) {
			Point point = move.getToPoint();
			if(pieces[point.y][point.x] != null) {
				return true;
			}
		}
		return false;
	}
}