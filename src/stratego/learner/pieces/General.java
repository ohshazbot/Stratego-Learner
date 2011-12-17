package stratego.learner.pieces;

import stratego.learner.board.PlayerEnum;


public class General extends Piece {

	public General(PlayerEnum own) {
		super(own);
	}

	@Override
	public Result attack(Piece defender) {
		return defaultAttack(defender);
	}

	@Override
	public boolean canMove() {
		return true;
	}

	@Override
	public Pieces pieceType() {
		return Pieces.GENERAL;
	}

}
