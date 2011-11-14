package stratego.learner.pieces;

import stratego.learner.board.Game;

public class Miner extends Piece {

	@Override
	public Result attack(Piece defender, Game game) {
		if (defender.pieceType().equals(Pieces.BOMB))
			return new Result(true, false);
		else
			return defaultAttack(defender, game);
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
