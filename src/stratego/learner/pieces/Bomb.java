package stratego.learner.pieces;

import stratego.learner.board.PlayerEnum;


public class Bomb extends Piece {

	public Bomb(PlayerEnum own) {
		super(own);
	}

	@Override
	public Result attack(Piece defender) {
		this.reveal();
		defender.reveal();
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
