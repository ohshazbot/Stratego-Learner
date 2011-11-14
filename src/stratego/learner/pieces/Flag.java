package stratego.learner.pieces;

import stratego.learner.board.Board;

public class Flag extends Piece {

	@Override
	public void attack(Piece defender, Board gameboard) {
		throw new UnsupportedOperationException("Flags can't attack!");
	}

	@Override
	public boolean canMove() {
		return false;
	}

	@Override
	public Pieces pieceType() {
		return Pieces.FLAG;
	}

}
