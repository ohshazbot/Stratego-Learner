package stratego.learner.board;

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
}
