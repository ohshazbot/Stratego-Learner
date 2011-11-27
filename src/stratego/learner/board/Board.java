package stratego.learner.board;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.SortedMap;
import java.util.TreeMap;

import stratego.learner.pieces.Piece;
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

	public void load(String boardCoding) {
		// This method will load a board configuration from a string

	}

	public boolean isOpen(int xcord, int ycord) {
		return board[xcord][ycord] == null;
	}

	public SortedMap<Integer, List<Piece>> getPiecesByDistance(
			Map<Piece, Location> pieces, Location center) {
		SortedMap<Integer, List<Piece>> toRet = new TreeMap<Integer, List<Piece>>();
		for (Entry<Piece, Location> entry : pieces.entrySet())
		{
			Piece piece = entry.getKey();
			if (!piece.canMove())
				continue;

			Location loc = entry.getValue();
			int distance = distance(center, loc);
			List<Piece> list = toRet.get(distance);
			if (list == null)
			{
				list = new LinkedList<Piece>();
				toRet.put(distance, list);
			}
			list.add(piece);
		}

		return toRet;
	}

	// This does not account for water, pieces in way
	public int distance(Location center, Location loc) {
		return Math.abs((center.xcord - loc.xcord))+Math.abs((center.ycord - loc.ycord));
	}
}
