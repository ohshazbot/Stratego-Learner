package stratego.learner.pieces;

import stratego.learner.board.Game;

public class Bomb extends Piece {

	@Override
	public Result attack(Piece defender, Game gameboard) {
		throw new UnsupportedOperationException("Bombs can't attack!");
	}

	@Override
	public boolean canMove() {
		return false;
	}

	@Override
	public Pieces pieceType() {
		return Pieces.BOMB;
	}

}
