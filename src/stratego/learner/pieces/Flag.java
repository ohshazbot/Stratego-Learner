package stratego.learner.pieces;


public class Flag extends Piece {

	public Flag(int pieceNumber) {
		super(pieceNumber);
	}

	@Override
	public Result attack(Piece defender) {
		this.reveal();
		defender.reveal();
		throw new UnsupportedOperationException("Flags can't attack!");
	}

	@Override
	public boolean canMove() {
		return false;
	}

	@Override
	public Pieces pieceType() {
		return Pieces.FLAG;
	}

}
