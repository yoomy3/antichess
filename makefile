all: MoveAnnotation.class piece/Piece.class piece/BishopPiece.class piece/KingPiece.class piece/KnightPiece.class piece/PrawnPiece.class piece/QueenPiece.class piece/RookPiece.class AntiChess.class Board.class Prune.class 

%.class: %.java
	javac -d . -classpath . $<

clean:
	rm -f *.class
