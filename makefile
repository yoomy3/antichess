all: MoveAnnotation.class piece/Piece.class piece/BishopPiece.class piece/KingPiece.class piece/KnightPiece.class piece/PawnPiece.class piece/QueenPiece.class piece/RookPiece.class Board.class Prune.class AntiChess.class

%.class: %.java
	javac -d . -classpath . $<

clean:
	rm -f *.class
