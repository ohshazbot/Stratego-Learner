package stratego.learner.board;

import stratego.learner.pieces.Piece;
import stratego.learner.pieces.Water;

public class Board {
	Piece[][] board = new Piece[10][10];

	public Board()
	{
		for (int i : new int[]{3,4,7,8})
			for (int j = 5; j <=6; j++)
				board[i][j] = Water.getWater();
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
		return board[xcord][ycord]==null;
	}
}
