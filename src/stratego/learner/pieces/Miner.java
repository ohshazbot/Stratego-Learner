package stratego.learner.pieces;

import stratego.learner.board.Board;

public class Miner extends Piece {

	@Override
	public void attack(Piece defender, Board gameBoard) {
		if (defender.pieceType().equals(Pieces.BOMB))
			gameBoard.remove(defender);
		else
			defaultAttack(defender, gameBoard);
	}

	@Override
	public boolean canMove() {
		return true;
	}

	@Override
	public Pieces pieceType() {
		return Pieces.MINER;
	}

}
