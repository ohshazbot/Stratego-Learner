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
}
