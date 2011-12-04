package stratego.learner.player;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.SortedMap;

import stratego.learner.board.Board;
import stratego.learner.board.Location;
import stratego.learner.board.PlayerEnum;
import stratego.learner.pieces.Pieces;

public class CheaterBot implements Player {
	PlayerEnum player;

	@Override
	public void setRedPlayer() {
		player = PlayerEnum.RED;
	}

	@Override
	public void setBluePlayer() {
		player = PlayerEnum.BLUE;
	}

	private Location findFlag(List<Location> oppPieces, Board board) {
		for (Location loc : oppPieces)
		{
			if (board.get(loc).pieceType().equals(Pieces.FLAG))
				return loc;
		}
		return null;
	}

	@Override
	public Action getAction(List<Location> myPieces, List<Location> oppPieces,
			Board board, boolean redo) {
		Location flag = findFlag(oppPieces, board);
		SortedMap<Integer, LinkedHashMap<Location, Location>> pieces = board.getPiecesByDistance(myPieces, flag);
		
		for (LinkedHashMap<Location, Location> list : pieces.values())
		{
			for (Entry<Location, Location> entry : list.entrySet())
			{
				Location offset = entry.getValue();
				Location myLoc = entry.getKey();
				//TODO Scouts can move infinite
				int neg = -1;
				int pos = 1;
				if (redo)
				{
					neg = 1;
					pos = -1;
				}
				if (offset.ycord != 0 && board.canOccupy(myLoc.xcord, myLoc.ycord+(offset.ycord > 0? pos:neg), player))
				{
					Location dest = new Location(myLoc.xcord, myLoc.ycord+(offset.ycord > 0? pos:neg));
					return new Action(myLoc, dest);
				}
				if (offset.xcord != 0 && board.canOccupy(myLoc.xcord+(offset.xcord > 0? pos : neg), myLoc.ycord, player))
				{
					Location dest = new Location(myLoc.xcord+(offset.xcord > 0? pos : neg), myLoc.ycord);
					return new Action(myLoc, dest);
				}
			}
		}
		// Return a piece to trigger boolean 
		return new Action(myPieces.get(0), myPieces.get(0));
	}

	@Override
	public void wins() {
		System.out.println("Cheatbot wins");
	}

	@Override
	public void loses() {
		System.out.println("Cheatbot loses");
	}

}
