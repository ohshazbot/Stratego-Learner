package stratego.learner.pieces;

import stratego.learner.board.Game;
import stratego.learner.pieces.Piece.Result;

public abstract class Piece {
	boolean onBoard = false;
	public boolean revealed = false;

	public Piece()
	{	}

	public abstract Result attack(Piece defender, Game game);

	public Result defaultAttack(Piece defender, Game game) {
		if (defender.pieceType().equals(Pieces.BOMB)) {
			return new Result(false, true);
		}

		int compare = this.pieceType().compareTo(defender.pieceType());
		if (compare > 0)
			return new Result(true, false);
		else if (compare < 0)
			return new Result(false, true);
		else {
			return new Result(true, true);
		}
	}

	public abstract boolean canMove();

	public abstract Pieces pieceType();

	public class Result{
		boolean attackerLives;
		boolean defenderLives;

		public Result()
		{
			attackerLives = false;
			defenderLives = false;
		}

		public Result(boolean attackLive, boolean defendLive)
		{
			attackerLives = attackLive;
			defenderLives = defendLive;
		}
	}
}
