package stratego.learner.board;

import java.util.LinkedList;
import java.util.List;

import stratego.learner.pieces.Piece;

public class GameState {
	public Board board;
	public List<Location> redPieces;
	public List<Location> bluPieces;

	public GameState(Board b, List<Location> reds, List<Location> blus) {
		board = b;
		redPieces = reds;
		bluPieces = blus;
	}

	/**
	 * This creates a partial clone of GameState. It does not clone the Piece
	 * list for the players within the Game, so it doesn't work for playing
	 * derivative games.
	 * 
	 * @param toClone
	 */
	public GameState(GameState toClone) {
		board = new Board(toClone.board);
		redPieces = new LinkedList<Location>(toClone.redPieces);
		bluPieces = new LinkedList<Location>(toClone.bluPieces);
	}

	/**
	 *  Puts the piece at src at the location dest. If src is null, the piece at dest is just removed.
	 *  If dest is null, it has the same effect for the piece at src. 
	 * @param src
	 * @param dest
	 */
	public void replace(Location src, Location dest) {
		if (src == null)
			remove(dest);
		else if (dest == null)
			remove(src);
		else
		{
			Piece s = board.get(src);
			if (s.redOwner())
			{
				redPieces.remove(src);
				redPieces.add(dest);
				bluPieces.remove(dest);
			} else
			{
				bluPieces.remove(src);
				bluPieces.add(dest);
				redPieces.remove(dest);				
			}
			board.put(dest, s);
		}
	}
	
	public void remove(Location loc)
	{
		if (loc == null)
			return;
		board.remove(loc);
		redPieces.remove(loc);
		bluPieces.remove(loc);
	}
}
