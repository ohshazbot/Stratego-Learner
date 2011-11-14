package stratego.learner.pieces;

import stratego.learner.board.Game;

public class Water extends Piece {
	static Water water = null;
	
	public static Water getWater()
	{
		if (water == null)
			water = new Water();
		return water;
	}
	@Override
	public Result attack(Piece defender, Game game) {
		throw new UnsupportedOperationException("Water is no one's unit");
	}

	@Override
	public boolean canMove() {
		return false;
	}

	@Override
	public Pieces pieceType() {
		return null;
	}

}
