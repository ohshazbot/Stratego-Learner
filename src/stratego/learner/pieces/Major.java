package stratego.learner.pieces;


public class Major extends Piece {

	public Major(int pieceNumber, boolean redPlayer) {
		super(pieceNumber, redPlayer);
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
		return Pieces.MAJOR;
	}

}
