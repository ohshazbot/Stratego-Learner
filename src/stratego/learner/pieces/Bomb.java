package stratego.learner.pieces;


public class Bomb extends Piece {

	public Bomb(int pieceNumber, boolean redPlayer) {
		super(pieceNumber, redPlayer);
	}

	@Override
	public Result attack(Piece defender) {
		this.reveal();
		defender.reveal();
		throw new UnsupportedOperationException("Bombs can't attack!");
	}

	@Override
	public boolean canMove() {
		return false;
	}

	@Override
	public Pieces pieceType() {
		return Pieces.BOMB;
	}

}
