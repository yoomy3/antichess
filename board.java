public class Board {
	// private variables
	Piece[8][8] mainBoard;
	int player; // 0 - black, 1 - white
	boolean forcedMove;
	
	boolean isCaptured;
	int fiftyMoveCount;

	boolean isCastled;

	// constructor
	Board() {
		// initialize 32 pieces
	}

	// make a move, update the board
	void makeMove(Board board) {}

	// list all possible moves from the current state
	// Vector<Board> possibleMoves() {}
	void possibleMoves() {}
}