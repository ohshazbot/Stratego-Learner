package stratego.learner.pieces;

import stratego.learner.board.Game;

public class Spy extends Piece {

	@Override
	public Result attack(Piece defender, Game gameboard) {
		if (defender.pieceType().equals(Pieces.MARSHALL))
			return new Result(true, false);
		else
			return defaultAttack(defender, gameboard);
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
