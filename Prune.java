package antiChess;

import java.awt.Color;
import java.util.ArrayList;

import antiChess.piece.Piece;
import antiChess.piece.BishopPiece;
import antiChess.piece.KingPiece;
import antiChess.piece.KnightPiece;
import antiChess.piece.PawnPiece;
import antiChess.piece.QueenPiece;
import antiChess.piece.RookPiece;

/**
 * This module handles logic to choose best move on the board
 * We use Alpha-Beta Pruning algorithm to choose the best score. And we calculate the following scores:
 * 1. Material score: each remaining material (P,N,B,R,Q,K) is given a score
 * 2. Position score: each position (on [8][8] board) for each material  will have a score from WHITE player's perspective
 * 3. Mobility score: this calculates how many possible moves are available on the board
 * 4. Pattern score: doubled/isolated/passed pawn pieces will be considered as special patterns for this engine
 * 
 */
public class Prune {
	// alpha-beta pruning variables
	static private int depth = 2;
	
	// material scores
	static private int pawnPiece = 100;
	static private int knightPiece = 320;
	static private int bishopPiece = 330;
	static private int rookPiece = 500;
	static private int queenPiece = 900;
	static private int kingPiece = 20000;

	// mobility weight
	static private int mobilityWeight = 10;
	
	// pattern weight
	static private int pawnWeight = 50;
	
	// position scores (all scores are based from the lower player(WHITE) perspective
	static private int[][] pawnPosition = new int[][] {
		{0,  0,  0,  0,  0,  0,  0,  0},
		{50, 50, 50, 50, 50, 50, 50, 50},
		{10, 10, 20, 30, 30, 20, 10, 10},
		{5,  5, 10, 25, 25, 10,  5,  5},
		{0,  0,  0, 20, 20,  0,  0,  0},
		{5, -5,-10,  0,  0,-10, -5,  5},
		{5, 10, 10,-20,-20, 10, 10,  5},
		{0,  0,  0,  0,  0,  0,  0,  0}
	};

	static private int[][] knightPosition = new int[][] {
		{-50,-40,-30,-30,-30,-30,-40,-50},
		{-40,-20,  0,  0,  0,  0,-20,-40},
		{-30,  0, 10, 15, 15, 10,  0,-30},
		{-30,  5, 15, 20, 20, 15,  5,-30},
		{-30,  0, 15, 20, 20, 15,  0,-30},
		{-30,  5, 10, 15, 15, 10,  5,-30},
		{-40,-20,  0,  5,  5,  0,-20,-40},
		{-50,-40,-30,-30,-30,-30,-40,-50}
	};

	static private int[][] bishopPosition = new int[][] {
		{-20,-10,-10,-10,-10,-10,-10,-20},
		{-10,  0,  0,  0,  0,  0,  0,-10},
		{-10,  0,  5, 10, 10,  5,  0,-10},
		{-10,  5,  5, 10, 10,  5,  5,-10},
		{-10,  0, 10, 10, 10, 10,  0,-10},
		{-10, 10, 10, 10, 10, 10, 10,-10},
		{-10,  5,  0,  0,  0,  0,  5,-10},
		{-20,-10,-10,-10,-10,-10,-10,-20}
	};

	static private int[][] rookPosition = new int[][] {
		{0,  0,  0,  0,  0,  0,  0,  0},
		{5, 10, 10, 10, 10, 10, 10,  5},
		{-5,  0,  0,  0,  0,  0,  0, -5},
		{-5,  0,  0,  0,  0,  0,  0, -5},
		{-5,  0,  0,  0,  0,  0,  0, -5},
		{-5,  0,  0,  0,  0,  0,  0, -5},
		{-5,  0,  0,  0,  0,  0,  0, -5},
		{0,  0,  0,  5,  5,  0,  0,  0}
	};
	
	static private int[][] queenPosition = new int[][] {
		{-20,-10,-10, -5, -5,-10,-10,-20},
		{-10,  0,  0,  0,  0,  0,  0,-10},
		{-10,  0,  5,  5,  5,  5,  0,-10},
		{-5,  0,  5,  5,  5,  5,  0, -5},
		{0,  0,  5,  5,  5,  5,  0, -5},
		{-10,  5,  5,  5,  5,  5,  0,-10},
		{-10,  0,  5,  0,  0,  0,  0,-10},
		{-20,-10,-10, -5, -5,-10,-10,-20}
	};
	
	static private int[][] kingPosition = new int[][] {
		{-30,-40,-40,-50,-50,-40,-40,-30},
		{-30,-40,-40,-50,-50,-40,-40,-30},
		{-30,-40,-40,-50,-50,-40,-40,-30},
		{-30,-40,-40,-50,-50,-40,-40,-30},
		{-20,-30,-30,-40,-40,-30,-30,-20},
		{-10,-20,-20,-20,-20,-20,-20,-10},
		{20, 20,  0,  0,  0,  0, 20, 20},
		{20, 30, 10,  0,  0, 10, 30, 20}
	};
	
	
	public Prune() {}

	// Alpha-Beta pruning
	MoveAnnotation getBestMove(Board board, ArrayList<MoveAnnotation> moves) throws Exception {
		System.out.println("please wait");
		int bestScore = Integer.MIN_VALUE, score;
		MoveAnnotation bestMove = null;

		// run local BFS to traverse all child boards
		for (MoveAnnotation move : board.getPossibleMoves()) {
			Board childBoard = new Board(board);
			childBoard.takeMove(move);
			score = alphaBetaMin(childBoard, Integer.MIN_VALUE, Integer.MAX_VALUE, depth);
			if (score > bestScore) {
				bestScore = score;
				bestMove = move;
			}
		}
		return bestMove;
	}

	// Alpha-Beta pruning for max
	int alphaBetaMax(Board board, int alpha, int beta, int depthleft) throws Exception {
		int score;
		if (depthleft == 0) return evaluate(board); // evaluate at leaf node
		
		// run local BFS to traverse all child boards
		for (MoveAnnotation move : board.getPossibleMoves()) {
			Board childBoard = new Board(board).takeMove(move);
			score = alphaBetaMin(childBoard, alpha, beta, depthleft - 1);
			if (score >= beta)
				return beta; // fail hard beta-cutoff
			if (score > alpha)
				alpha = score; // alpha acts like max in MiniMax
		}
		return alpha;
	}

	// Alpha-Beta pruning for min
	int alphaBetaMin(Board board, int alpha, int beta, int depthleft) throws Exception {
		int score;
		if (depthleft == 0) return evaluate(board); // evaluate at leaf node
		
		// run local BFS to traverse all child boards
		for (MoveAnnotation move : board.getPossibleMoves()) {
			Board childBoard = new Board(board).takeMove(move);
			score = alphaBetaMax(childBoard, alpha, beta, depthleft - 1);
			if (score <= alpha)
				return alpha; // fail hard alpha-cutoff
			if (score < beta)
				beta = score; // beta acts like min in MiniMax
		}
		return beta;
	}
	
	// return single piece score (both piece + position scores)
	int evaluateSingleMaterialPosition(int pieceScore, int[][] positionScore, int posX, int posY, boolean isWhite) {
		int singleScore = pieceScore;
		if (isWhite) {
			singleScore += positionScore[posX][posY];
		} else {
			singleScore += positionScore[7-posX][7-posY];
		}
		return singleScore;
	}
	
	// return material and position scores for the board
	int evaluateMaterialPosition(Board board) {
		Color curPlayer = board.getPlayer();
		int score = 0;
		boolean isWhite = (curPlayer.equals(Color.WHITE));
		
		Piece[][] pieces = board.getPieces();
		for (int r=0; r<8; r++) {
			for (int c=0; c<8; c++) {
				Piece curPiece = pieces[r][c];
				if (curPiece != null) {
					Color piecePlayer = curPiece.getPlayer();
					if (piecePlayer == curPlayer) {
						if (curPiece instanceof BishopPiece) {
							score += evaluateSingleMaterialPosition(bishopPiece, bishopPosition, r, c, isWhite);
						} else if (curPiece instanceof KingPiece) {
							score += evaluateSingleMaterialPosition(kingPiece, kingPosition, r, c, isWhite);
						} else if (curPiece instanceof KnightPiece) {
							score += evaluateSingleMaterialPosition(knightPiece, knightPosition, r, c, isWhite);
						} else if (curPiece instanceof PawnPiece) {
							score += evaluateSingleMaterialPosition(pawnPiece, pawnPosition, r, c, isWhite);
						} else if (curPiece instanceof QueenPiece) {
							score += evaluateSingleMaterialPosition(queenPiece, queenPosition, r, c, isWhite);
						} else if (curPiece instanceof RookPiece) {
							score += evaluateSingleMaterialPosition(rookPiece, rookPosition, r, c, isWhite);
						}
					} else {
						if (curPiece instanceof BishopPiece) {
							score -= evaluateSingleMaterialPosition(bishopPiece, bishopPosition, r, c, isWhite);
						} else if (curPiece instanceof KingPiece) {
							score -= evaluateSingleMaterialPosition(kingPiece, kingPosition, r, c, isWhite);
						} else if (curPiece instanceof KnightPiece) {
							score -= evaluateSingleMaterialPosition(knightPiece, knightPosition, r, c, isWhite);
						} else if (curPiece instanceof PawnPiece) {
							score -= evaluateSingleMaterialPosition(pawnPiece, pawnPosition, r, c, isWhite);
						} else if (curPiece instanceof QueenPiece) {
							score -= evaluateSingleMaterialPosition(queenPiece, queenPosition, r, c, isWhite);
						} else if (curPiece instanceof RookPiece) {
							score -= evaluateSingleMaterialPosition(rookPiece, rookPosition, r, c, isWhite);
						}
					}
				}
			}
		}
		return score;
	}
	
	// return mobility score
	int evaluateMobility(Board board) throws Exception {
		return mobilityWeight * board.getPossibleMoves().size();
	}
	
	// count the number of pawns on specific file for the current player
	int countPawn(int file, Piece[][] pieces, Color curPlayer) {
		int pawnCount = 0;
		for (int i=0; i<8; i++) {
			Piece piece = pieces[i][file];
			if ((piece instanceof PawnPiece) && (piece.getPlayer().equals(curPlayer))){
				pawnCount++;
			}
		}
		return pawnCount;
	}
	
	/**
	 * evaluate scores for pawn patterns (doubled + isolated + passed) with formula:
	 * score = pawnWeight * (passed - isolate - doubled)
	 */
	int evaluatePawnPattern(Board board) {
		Color curPlayer = board.getPlayer();
		Color opponent = getOpponent(curPlayer);
		Piece[][] pieces = board.getPieces();
		int score = 0;
		
		for (int i=0; i<8; i++) {
			int curPlayerPawn = countPawn(i, pieces, curPlayer);
			int opponentPawn = countPawn(i, pieces, opponent);
			if (curPlayerPawn > 1) {
				score -= curPlayerPawn; // doubled pawns
			} else if ((curPlayerPawn > 0) && (opponentPawn == 0)) {
				score += curPlayerPawn; // passed pawns
			} else if ((curPlayerPawn > 0) && (i == 0)) { // check isolated for 1st file
				int nextPawn = countPawn(1, pieces, curPlayer);
				if (nextPawn == 0) score -= curPlayerPawn;
			} else if ((curPlayerPawn > 0) && (i == 7)) { // check isolated for last file
				int nextPawn = countPawn(6, pieces, curPlayer);
				if (nextPawn == 0) score -= curPlayerPawn;
			} else if (curPlayerPawn > 0){ // check isolated for general file
				int adjacentPawn = countPawn(i-1, pieces, curPlayer) + countPawn(i+1, pieces, curPlayer);
				if (adjacentPawn == 0) score -= curPlayerPawn;
			}
		}
		return pawnWeight * score;
	}
	
	// return pattern evaluation (this one only considers pawns)
	int evaluatePattern(Board board) {
		int score = 0;
		score += evaluatePawnPattern(board);
		return score;
	}
	
	// evaluate the board score
	int evaluate(Board board) throws Exception {
		int score = 0;

		score += evaluateMaterialPosition(board);
		score += evaluateMobility(board);
		score += evaluatePattern(board);
		
		return score;
	}
	
	// helper function to get oppenent player
	Color getOpponent(Color curPlayer) {
		return (curPlayer.equals(Color.WHITE)) ? Color.BLACK : Color.WHITE;
	}
}