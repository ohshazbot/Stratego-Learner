package stratego.learner.player;
import java.util.Map;

import stratego.learner.board.Board;
import stratego.learner.board.Location;
import stratego.learner.pieces.Piece;


public interface Player {
	public Piece getMove(Map<Piece, Location> myPieces, Map<Piece, Location> oppPieces, Board board, boolean redo);
	public Location moveLoc();
}
