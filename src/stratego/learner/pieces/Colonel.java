package stratego.learner.pieces;

import stratego.learner.board.PlayerEnum;


public class Colonel extends Piece {

	public Colonel(PlayerEnum own) {
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
		return Pieces.COLONEL;
	}

}
