package stratego.learner.pieces;


public class Water extends Piece {
	public Water() {
		super(-1, true);
		this.reveal();
	}

	static Water water = null;
	
	public static Water getWater()
	{
		if (water == null)
			water = new Water();
		return water;
	}
	@Override
	public Result attack(Piece defender) {
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
