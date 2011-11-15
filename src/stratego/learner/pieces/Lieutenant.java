package stratego.learner.pieces;


public class Lieutenant extends Piece {

	public Lieutenant(int pieceNumber) {
		super(pieceNumber);
	}

	@Override
	public Result attack(Piece defender) {
		this.reveal();
		defender.reveal();
		return defaultAttack(defender);
	}

	@Override
	public boolean canMove() {
		return true;
	}

	@Override
	public Pieces pieceType() {
		return Pieces.LIEUTENANT;
	}

}
