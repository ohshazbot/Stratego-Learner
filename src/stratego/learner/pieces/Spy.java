package stratego.learner.pieces;

import stratego.learner.board.Board;

public class Spy extends Piece {

	@Override
	public void attack(Piece defender, Board gameboard) {
		if (defender.pieceType().equals(Pieces.MARSHALL))
			gameboard.remove(defender);
		else
			defaultAttack(defender, gameboard);
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
