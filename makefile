all: MoveAnnotation.class AntiChess.class Board.class Piece.class Prune.class 

%.class: %.java
	javac -d . -classpath . $<

clean:
	rm -f *.class
