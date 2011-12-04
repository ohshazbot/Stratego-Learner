package stratego.learner.player;
import java.util.List;

import stratego.learner.board.Board;
import stratego.learner.board.Location;


public interface Player {
	public void setRedPlayer();
	public void setBluePlayer();
	public Action getAction(List<Location> myPieces, List<Location> oppPieces, Board board, boolean redo);
	public void wins();
	public void loses();
}
