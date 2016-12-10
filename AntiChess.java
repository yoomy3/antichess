package antiChess;

import java.awt.Color;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;

public class AntiChess {
	// timer vars
	private static Timer whiteTimer, blackTimer;
	private static int whiteSeconds, blackSeconds;
	private static TimerTask whiteTask, blackTask;
	private static Color chosenPlayer, opponentPlayer;
	private static int timerLimit = 180;
	
	// game and AI component
	private static Scanner scanner = new Scanner(System.in);
	private static Board board;
	private static Prune prune;
	private static MoveAnnotation pruneAction, inputAction;
	
	// set up player from input
	private static void getPlayer(String side) {
		if (side.toLowerCase().equals("white")) {
			chosenPlayer = Color.WHITE;
			opponentPlayer = Color.BLACK;
		} else {
			chosenPlayer = Color.BLACK;
			opponentPlayer = Color.WHITE;
		}
	}
	
	// init timer and task
	private static void initTimer(Color player) {
		if (player.equals(Color.WHITE)) {
			whiteTimer = new Timer();
			whiteTask = new TimerTask() {
				public void run() {
					System.err.println("WHITE: " + (timerLimit - whiteSeconds) + "s left");
					if (whiteSeconds >= timerLimit) getWinner(Color.BLACK);
					whiteSeconds++;
				}
			};
		} else {
			blackTimer = new Timer();
			blackTask = new TimerTask() {
				public void run() {
					System.err.println("BLACK: " + (timerLimit - blackSeconds) + "s left");
					if (blackSeconds >= timerLimit) getWinner(Color.WHITE);
					blackSeconds++;
				}
			};
		}
	}
	
	// pause the timer
	private static void pause(Color player) {
		if (player.equals(Color.WHITE)) {
			whiteTimer.cancel();
		} else {
			blackTimer.cancel();
		}
	}
	
	// resume the timer
	private static void resume(Color player) {
		initTimer(player);
		if (player.equals(Color.WHITE)) {
			whiteTimer.schedule(whiteTask, 0, 1000);
		} else {
			blackTimer.schedule(blackTask, 0, 1000);
		}
	}
	
	// initialize all game states
	private static void initGame(String side) throws Exception {
	    getPlayer(side);
	    scanner = new Scanner(System.in);
		board = new Board(chosenPlayer);
		prune = new Prune();
	}
	
	// this will handle prune action (AI will give the move)
	private static void handlePruneAction() throws Exception {
		// turn for chosen player
		resume(chosenPlayer);

		// action for chosen player
		board.printAllPossibleMoves(board.getPossibleMoves());
		pruneAction = prune.getBestMove(board, board.getPossibleMoves());
		System.out.println(pruneAction.getMoveString()); // print out prune action
		board.takeMove(pruneAction);
		checkGameOver();

		pause(chosenPlayer);
	}
	
	// this will handle input action (read input and process the game)
	private static void handleInputAction() throws Exception {
		// turn for opponent player
		resume(opponentPlayer);
		
		String input;
		while (true) {
			try {
				input = scanner.nextLine();
				inputAction = new MoveAnnotation(input);
				break;
			} catch (Exception e) {
				System.err.println("## Input is not in a valid format. Please re-enter a valid next move.");
			}
		}

		// opponent action
		board.takeMove(inputAction);	// *NOTE: we assume that the opponent's move is a proper, valid move.
		checkGameOver();
		
		pause(opponentPlayer);
	}
	
	// check whether game should be finished or not
	private static void checkGameOver() {
		if (board.isGameFinished()) {
			if (board.isDrawn()) {
				System.out.println("1/2-1/2");
				System.exit(0);
			} else {
				getWinner(board.getWinner());
			}
		}
	}
	
	// print out winner (1-0) for white and (0-1) for black
	private static void getWinner(Color winner) {
		if (winner.equals(Color.WHITE)) {
			System.out.println("1-0");
		} else {
			System.out.println("0-1");
		}
		System.exit(0);
	}
	public static void main(String arg[]) throws Exception {
		// create instances
		initGame(arg[0]);

		board.printBoard();

		// if we need to go first
		if (chosenPlayer.equals(Color.WHITE)) handlePruneAction();

		while (true) {
			board.printBoard();
			handleInputAction();
			handlePruneAction();
		}
	}
}