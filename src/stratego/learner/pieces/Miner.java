package stratego.learner.pieces;


public class Miner extends Piece {

	public Miner(int pieceNumber) {
		super(pieceNumber);
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
