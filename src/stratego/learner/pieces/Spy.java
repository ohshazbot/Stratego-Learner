package stratego.learner.pieces;


public class Spy extends Piece {

	public Spy(int pieceNumber) {
		super(pieceNumber);
	}

	@Override
	public Result attack(Piece defender) {
		this.reveal();
		defender.reveal();
		if (defender.pieceType().equals(Pieces.MARSHALL))
			return new Result(true, false);
		else
			return defaultAttack(defender);
	}

	@Override
	public boolean canMove() {
		return true;
	}

	@Override
	public Pieces pieceType() {
		return Pieces.SPY;
	}

}
