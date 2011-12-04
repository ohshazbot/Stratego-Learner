package stratego.learner.pieces;

import stratego.learner.board.PlayerEnum;


public class Miner extends Piece {

	public Miner(PlayerEnum own) {
		super(own);
	}

	@Override
	public Result attack(Piece defender) {
		this.reveal();
		defender.reveal();
		if (defender.pieceType().equals(Pieces.BOMB))
			return new Result(true, false);
		else
			return defaultAttack(defender);
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
