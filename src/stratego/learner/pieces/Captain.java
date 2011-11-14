package stratego.learner.pieces;

import stratego.learner.board.Board;

public class Captain extends Piece {

	@Override
	public void attack(Piece defender, Board gameBoard) {
		defaultAttack(defender, gameBoard);
	}

	@Override
	public boolean canMove() {
		return true;
	}

	@Override
	public Pieces pieceType() {
		return Pieces.CAPTAIN;
	}

}
