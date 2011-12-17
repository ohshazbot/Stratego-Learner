package stratego.learner.pieces;

import stratego.learner.board.PlayerEnum;


public class Captain extends Piece {

	public Captain(PlayerEnum own) {
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
		return Pieces.CAPTAIN;
	}

}
