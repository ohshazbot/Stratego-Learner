package stratego.learner.player;

import java.util.LinkedHashMap;
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
		SortedMap<Integer, LinkedHashMap<Piece, Location>> pieces = board.getPiecesByDistance(myPieces, flag);

		for (LinkedHashMap<Piece, Location> list : pieces.values())
		{
			for (Entry<Piece, Location> entry : list.entrySet())
			{
				Piece p = entry.getKey();
				Location offset = entry.getValue();
				Location myLoc = myPieces.get(p);
				//TODO Scouts can move infinite
				if (offset.xcord != 0 && board.canOccupy(myLoc.xcord+(offset.xcord > 0? 1 : -1), myLoc.ycord, redPlayer))
				{
					loc = new Location(myLoc.xcord+(offset.xcord > 0? 1 : -1), myLoc.ycord);
					return p;
				}
				if (offset.ycord != 0 && board.canOccupy(myLoc.xcord, myLoc.ycord+(offset.ycord > 0? 1:-1), redPlayer))
				{
					loc = new Location(myLoc.xcord, myLoc.ycord+(offset.ycord > 0? 1:-1));
					return p;
				}
			}
		}
		return null;
		
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
