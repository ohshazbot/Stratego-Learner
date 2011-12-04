package stratego.learner.pieces;

import stratego.learner.board.PlayerEnum;


public class Flag extends Piece {

	public Flag(PlayerEnum own) {
		super(own);
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
