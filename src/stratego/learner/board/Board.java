package stratego.learner.board;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

import stratego.learner.pieces.Piece;
import stratego.learner.pieces.Pieces;
import stratego.learner.pieces.Water;

public class Board implements Map<Location, Piece> {
	Piece[][] board = null;
	int xDim, yDim;

	List<Piece> redPieces = new LinkedList<Piece>();
	List<Piece> bluPieces = new LinkedList<Piece>();
	Set<Location> water = new HashSet<Location>();

	public Board() {
		this(10);
		try {
			setWater(new int[] { 2, 3, 6, 7 }, new int[] { 4, 5 });
		} catch (InvalidLocationException e) {
			// Won't happen
		}
	}

	public Board(int squareSize) {
		this(squareSize, squareSize);
	}

	public Board(int x, int y) {
		board = new Piece[x][y];
		xDim = x;
		yDim = y;
	}

	public Board(String boardCoding) throws Exception {
		int r = 0;
		for (String row : boardCoding.split("\n")) {
			if (board == null) {
				xDim = row.replaceAll("[br]", "").trim().length();
				yDim = boardCoding.split("\n").length;
				board = new Piece[xDim][yDim];
			}
			int pos = 0;
			for (int i = 0; i < row.length(); i++) {
				char ch = row.charAt(i);
				if (ch == 'W') {
					Piece p = Water.getWater();
					put(r, pos++, p);
					continue;
				}
				if (ch == '_') {
					pos++;
					continue;
				}
				PlayerEnum owner;
				if (ch == 'r')
					owner = PlayerEnum.RED;
				else if (ch == 'b')
					owner = PlayerEnum.BLUE;
				else if (ch == ' ')
					continue;
				else
					throw new Exception(
							"Invalid board pattern, make sure you prefix pieces with r or b");
				ch = row.charAt(++i);
				Piece p = Piece.makePiece(Pieces.valueOf(ch), owner);
				put(r, pos++, p);
			}
			r++;
		}
	}

	public Board(Board board2) {
		xDim = board2.xDim;
		yDim = board2.yDim;
		board = new Piece[xDim][yDim];
		for (int i = 0; i < xDim; i++)
			for (int j = 0; j < yDim; j++)
				board[i][j] = board2.board[i][j];
	}

	public void setWater(int[] x, int[] y) throws InvalidLocationException {
		for (int i : x)
			for (int j : y) {
				setWater(i, j);
			}
	}

	public void setWater(int i, int j) throws InvalidLocationException {
		validate(i, j);
		water.add(new Location(i, j));
		board[i][j] = Water.getWater();
	}

	@Override
	public void clear() {
		for (int i = 0; i < xDim; i++)
			for (int j = 0; j < yDim; j++)
				board[i][j] = null;
		redPieces.clear();
		bluPieces.clear();
	}

	@Override
	public boolean containsKey(Object arg0) {
		if (arg0 instanceof Location) {
			Location loc = (Location) arg0;
			return board[loc.xcord][loc.ycord] != null;
		}
		return false;
	}

	@Override
	public boolean containsValue(Object arg0) {
		return redPieces.contains(arg0) || bluPieces.contains(arg0);
	}

	@Override
	public Set<java.util.Map.Entry<Location, Piece>> entrySet() {
		throw new UnsupportedOperationException("Homey don't play that");
	}

	@Override
	public Piece get(Object arg0) {
		if (arg0 instanceof Location) {
			Location l = (Location) arg0;
			return get(l.xcord, l.ycord);
		}
		return null;
	}

	@Override
	public boolean isEmpty() {
		return bluPieces.isEmpty() && redPieces.isEmpty();
	}

	@Override
	public Set<Location> keySet() {
		throw new UnsupportedOperationException("Homey don't play that");
	}

	@Override
	public Piece put(Location arg0, Piece arg1) {
		return put(arg0.xcord, arg0.ycord, arg1);
	}

	public Piece put(int x, int y, Piece p) {
		try {
			validate(x, y);
		} catch (InvalidLocationException e) {
			return null;
		}
		Piece old = board[x][y];
		board[x][y] = p;
		if (p.redOwner())
			redPieces.add(p);
		else
			redPieces.add(p);
		if (old != null) {
			redPieces.remove(old);
			bluPieces.remove(old);
		}
		return old;
	}

	@Override
	public void putAll(Map<? extends Location, ? extends Piece> arg0) {
		for (java.util.Map.Entry<? extends Location, ? extends Piece> e : arg0
				.entrySet()) {
			put(e.getKey(), e.getValue());
		}
	}

	@Override
	public Piece remove(Object arg0) {
		if (arg0 instanceof Location) {
			Location loc = (Location) arg0;
			try {
				validate(loc);
			} catch (InvalidLocationException e) {
				return null;
			}
			Piece old = board[loc.xcord][loc.ycord];
			board[loc.xcord][loc.ycord] = null;
			if (old != null) {
				redPieces.remove(old);
				bluPieces.remove(old);
			}

			return old;
		}
		return null;
	}

	@Override
	public int size() {
		return redPieces.size() + bluPieces.size();
	}

	@Override
	public Collection<Piece> values() {
		Collection<Piece> toRet = Collections.emptySet();
		toRet.addAll(bluPieces);
		toRet.addAll(redPieces);
		return toRet;
	}

	public void validate(Location loc) throws InvalidLocationException {
		validate(loc.xcord, loc.ycord);
	}

	public void validate(int x, int y) throws InvalidLocationException {
		if (isInvalid(x,y))
			throw new InvalidLocationException();
	}

	public boolean isInvalid(int x, int y)
	{
		return (x < 0 || y < 0 || x >= xDim || y >= yDim || (board[x][y] != null && board[x][y].isWater()));
	}
	
	public List<Location> getPlayerLocations(PlayerEnum player) {
		List<Location> toRet = new LinkedList<Location>();
		Piece p;
		for (int i = 0; i < xDim; i++)
			for (int j = 0; j < yDim; j++)
			{
				if ((p = board[i][j])!= null && !p.isWater() && p.owner().equals(player))
					toRet.add(new Location(i,j));
			}
		return toRet;
	}

	public Piece get(int i, int j) {
		try {
			validate(i, j);
		} catch (InvalidLocationException e) {
			return null;
		}
		return board[i][j];
	}

	 public void move(Location curr, Location destination) throws InvalidLocationException {
		 validate(curr);
		 validate(destination);
		 board[destination.xcord][destination.ycord] =
		 board[curr.xcord][curr.ycord];
		 board[curr.xcord][curr.ycord] = null;
	 }


	 public boolean isOpen(int x, int y) {
		 if (!isInvalid(x,y))
			 return false;
		 return board[x][y] == null;
	 }

	 public SortedMap<Integer, LinkedHashMap<Location, Location>> getPiecesByDistance(
			 List<Location> myPieces, Location center) {
		 SortedMap<Integer, LinkedHashMap<Location, Location>> toRet = new TreeMap<Integer, LinkedHashMap<Location, Location>>();
		 for (Location loc : myPieces) {
			 Piece piece = get(loc);
			 if (!piece.canMove())
				 continue;

			 Location offset = distance(center, loc);
			 int dist = Math.abs(offset.xcord) + Math.abs(offset.ycord);
			 LinkedHashMap<Location, Location> list = toRet.get(dist);
			 if (list == null) {
				 list = new LinkedHashMap<Location, Location>();
				 toRet.put(dist, list);
			 }
			 list.put(loc, offset);
		 }

		 return toRet;
	 }

	// This does not account for water, pieces in way
	public Location distance(Location center, Location loc) {
		return new Location(center.xcord - loc.xcord, center.ycord - loc.ycord);
	}
	
	public boolean canOccupy(int xcord, int ycord, PlayerEnum player) {
		if (isInvalid(xcord, ycord))
			return false;
		if (isOpen(xcord, ycord))
			return true;
		Piece p = get(xcord, ycord);
		if (p == null)
			return true;
		if (p.isWater())
			return false;
		if (!p.owner().equals(player))
			return true;
		return false;
	}

	public List<LocDist> occupyLocations(Location pieceLoc, PlayerEnum player, boolean treatHiddenAsScout) {
		List<LocDist> toRet = new ArrayList<LocDist>();
		
		Piece piece = get(pieceLoc);
		if (!piece.canMove())
			return toRet;
		int distance = 1;
		if (piece.pieceType().equals(Pieces.SCOUT) || (treatHiddenAsScout && !piece.revealed))
			distance = Math.max(xDim, yDim);
		
		for (int i = 1; i <= distance; i++)
		{
			if (canOccupy(pieceLoc.addX(i), player))
			{
				toRet.add(new LocDist(pieceLoc.addX(i), i));
			}
			else
				break;
		}
		
		for (int i = 1; i <= distance; i++)
		{
			if (canOccupy(pieceLoc.addX(-1*i), player))
			{
				toRet.add(new LocDist(pieceLoc.addX(-1*i), i));
			}
			else
				break;
		}
		
		for (int i = 1; i <= distance; i++)
		{
			if (canOccupy(pieceLoc.addY(i), player))
			{
				toRet.add(new LocDist(pieceLoc.addY(i), i));
			}
			else
				break;
		}
		
		for (int i = 1; i <= distance; i++)
		{
			if (canOccupy(pieceLoc.addY(-1*i), player))
			{
				toRet.add(new LocDist(pieceLoc.addY(-1*i), i));
			}
			else
				break;
		}
		
		return toRet;
	}

	private boolean canOccupy(Location loc, PlayerEnum player) {
		return canOccupy(loc.xcord, loc.ycord, player);
	}
	
	
	 
	// public String toString()
	// {
	// StringBuilder sb = new StringBuilder();
	// sb.append("  ");
	// for (int i = 0; i < 10; i++)
	// sb.append(i);
	// sb.append('\n');
	// for (int i = 0; i < 10; i++)
	// {
	// sb.append(i);
	// sb.append(':');
	// for (int j = 0; j < 10; j++)
	// {
	// Piece piece = getPiece(new Location(i, j));
	// if (piece == null)
	// sb.append('_');
	// else
	// {
	// sb.append(piece.pieceType().pieceType());
	// }
	// }
	// sb.append(':');
	// sb.append(i);
	// sb.append('\n');
	// }
	// sb.append("  ");
	// for (int i = 0; i < 10; i++)
	// sb.append(i);
	// return sb.toString();
	// }
	//


}
