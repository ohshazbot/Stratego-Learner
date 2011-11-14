package stratego.learner.pieces;

import stratego.learner.board.Game;

public class Lieutenant extends Piece {

	@Override
	public Result attack(Piece defender, Game game) {
		return defaultAttack(defender, game);
	}

	@Override
	public boolean canMove() {
		return true;
	}

	@Override
	public Pieces pieceType() {
		return Pieces.LIEUTENANT;
	}

}
