package stratego.learner.pieces;

import stratego.learner.board.PlayerEnum;


public class Marshall extends Piece {

	public Marshall(PlayerEnum own) {
		super(own);
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
		return Pieces.MARSHALL;
	}

}
