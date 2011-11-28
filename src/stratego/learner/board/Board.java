package stratego.learner.board;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.SortedMap;
import java.util.TreeMap;

import stratego.learner.pieces.Piece;
import stratego.learner.pieces.Pieces;
import stratego.learner.pieces.Water;

public class Board {
	Piece[][] board = new Piece[10][10];
	boolean set;

	public Board() {
		for (int i : new int[] { 2, 3, 6, 7 })
			for (int j : new int[] { 4, 5 })
				board[i][j] = Water.getWater();
	}

	public boolean addPiece(Piece piece, Location loc) {
		if (set)
			return false;
		if (isOpen(loc.xcord, loc.ycord)) {
			board[loc.xcord][loc.ycord] = piece;
			return true;
		}
		return false;
	}

	public void setBoard() {
		set = true;
	}

	public Piece getPiece(Location destination) {
		return board[destination.xcord][destination.ycord];
	}

	public void remove(Location remove) {
		board[remove.xcord][remove.ycord] = null;

	}

	public void move(Location curr, Location destination) {
		board[destination.xcord][destination.ycord] = board[curr.xcord][curr.ycord];
		board[curr.xcord][curr.ycord] = null;
	}

	public void load(String boardCoding, Map<Piece, Location> red, Map<Piece, Location> blue) {
		int pieceCnt = 0;
		int rowCnt = 0;
		for (String row : boardCoding.split("\n"))
		{
			for (int i = 0; i < 10; i++)
			{
				char ch = row.charAt(i);
				if (ch == '_' || ch == 'W')
					continue;

				boolean redPlayer = i <= 4;
				Piece p = Piece.makePiece(Pieces.valueOf(ch), pieceCnt++, redPlayer);
				Location loc = new Location(rowCnt, i);
				addPiece(p, loc); 
				if (redPlayer)
					red.put(p, loc);
				else
					blue.put(p,  loc);
			}
			rowCnt++;
		}
	}

	public boolean isOpen(int xcord, int ycord) {
		return board[xcord][ycord] == null;
	}

	public SortedMap<Integer, LinkedHashMap<Piece, Location>> getPiecesByDistance(
			Map<Piece, Location> pieces, Location center) {
		SortedMap<Integer, LinkedHashMap<Piece, Location>> toRet = new TreeMap<Integer, LinkedHashMap<Piece, Location>>();
		for (Entry<Piece, Location> entry : pieces.entrySet())
		{
			Piece piece = entry.getKey();
			if (!piece.canMove())
				continue;

			Location loc = entry.getValue();
			Location offset = distance(center, loc);
			int dist = Math.abs(offset.xcord) + Math.abs(offset.ycord);
			LinkedHashMap<Piece, Location> list = toRet.get(dist);
			if (list == null)
			{
				list = new LinkedHashMap<Piece, Location>();
				toRet.put(dist, list);
			}
			list.put(piece, offset);
		}

		return toRet;
	}

	// This does not account for water, pieces in way
	public Location distance(Location center, Location loc) {
		return new Location(center.xcord - loc.xcord,center.ycord - loc.ycord);
	}

	public String toString()
	{
		StringBuilder sb = new StringBuilder();
		sb.append("  ");
		for (int i = 0; i < 10; i++)
			sb.append(i);
		sb.append('\n');
		for (int i = 0; i < 10; i++)
		{
			sb.append(i);
			sb.append(':');
			for (int j = 0; j < 10; j++)
			{
				Piece piece = getPiece(new Location(i, j));
				if (piece == null)
					sb.append('_');
				else
				{
					sb.append(piece.pieceType().pieceType());
				}
			}
			sb.append(':');
			sb.append(i);
			sb.append('\n');
		}
		sb.append("  ");
		for (int i = 0; i < 10; i++)
			sb.append(i);
		return sb.toString();
	}

	public boolean canOccupy(int xcord, int ycord, boolean redPlayer) {
		if (isOpen(xcord, ycord))
			return true;
		if (getPiece(new Location(xcord, ycord)).pieceType().equals(Pieces.WATER))
			return false;
		if (getPiece(new Location(xcord, ycord)).redOwner() != redPlayer)
			return true;
		return false;
	}

}
