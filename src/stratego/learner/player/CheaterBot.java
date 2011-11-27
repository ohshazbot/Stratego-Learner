package stratego.learner.player;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.SortedMap;

import stratego.learner.board.Board;
import stratego.learner.board.Location;
import stratego.learner.pieces.Piece;
import stratego.learner.pieces.Pieces;

public class CheaterBot implements Player {
	boolean redPlayer;
	Location loc;
	@Override
	public void setRedPlayer() {
		redPlayer = true;
	}

	@Override
	public void setBluePlayer() {
		redPlayer = false;
	}

	@Override
	public Piece getMove(Map<Piece, Location> myPieces,
			Map<Piece, Location> oppPieces, Board board, boolean redo) {
		Location flag = findFlag(oppPieces);
		SortedMap<Integer, LinkedHashMap<Piece, Location>> pieces = board.getPiecesByDistance(oppPieces, flag);

		for (List<Piece> list : pieces.values())
		{
			for (Piece p : list)
			{
				
			}
		}
		
	}

	private Location findFlag(Map<Piece, Location> oppPieces) {
		for (Entry<Piece, Location> entry : oppPieces.entrySet())
		{
			if (entry.getKey().pieceType().equals(Pieces.FLAG))
				return entry.getValue();
		}
		return null;
	}

	@Override
	public Location moveLoc() {
		return loc;
	}

}
