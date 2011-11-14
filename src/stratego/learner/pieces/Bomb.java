package stratego.learner.pieces;

import stratego.learner.board.Board;

public class Bomb extends Piece {

	@Override
	public void attack(Piece defender, Board gameboard) {
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
