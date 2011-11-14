package stratego.learner.pieces;

import stratego.learner.board.Game;

public class Sergeant extends Piece {

	@Override
	public Result attack(Piece defender, Game gameBoard) {
		return defaultAttack(defender, gameBoard);
	}

	@Override
	public boolean canMove() {
		return true;
	}

	@Override
	public Pieces pieceType() {
		return Pieces.SERGEANT;
	}

}
