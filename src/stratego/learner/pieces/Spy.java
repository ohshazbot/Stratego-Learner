package stratego.learner.pieces;

import stratego.learner.board.PlayerEnum;


public class Spy extends Piece {

	public Spy(PlayerEnum own) {
		super(own);
	}

	@Override
	public Result attack(Piece defender) {
		if (defender.pieceType().equals(Pieces.MARSHALL))
			return new Result(true, false);
		else if (defender.pieceType().equals(Pieces.SPY))
			return new Result(false, false);
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
