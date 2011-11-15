package stratego.learner.pieces;

import java.util.LinkedList;
import java.util.List;

import stratego.learner.board.Board;
import stratego.learner.board.Location;


public abstract class Piece {
	boolean onBoard = false;
	public boolean revealed = false;
	private int pieceNumber;

	public Piece(int pieceNumber)
	{
		this.pieceNumber = pieceNumber;
	}

	public int hashCode()
	{
		return pieceNumber;
	}
	
	public boolean equals(Piece otherPiece)
	{
		return hashCode()==otherPiece.hashCode();
	}
	
	public abstract Result attack(Piece defender);
	
	public void reveal()
	{
		revealed = true;
	}

	public Result defaultAttack(Piece defender) {
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
		public boolean attackerLives;
		public boolean defenderLives;

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
	
	public List<Location> moveLocations(Board board, Location currLoc)
	{
		List<Location> toRet = new LinkedList<Location>();
		if (!canMove())
			return toRet;
		
		for (int offset : new int[]{-1,1})
		{
			if (board.isOpen(currLoc.xcord+offset, currLoc.ycord))
				toRet.add(new Location(currLoc.xcord+offset, currLoc.ycord));
			if (board.isOpen(currLoc.xcord, currLoc.ycord+offset))
				toRet.add(new Location(currLoc.xcord, currLoc.ycord+offset));
		}
		return toRet;
	}

	public boolean canMoveHere(Location destination, Location source,
			Board board) {
		if (!canMove())
			return false;
		if (board.isOpen(destination.xcord, destination.ycord))
		{
			if (destination.isOrthogonal(source, !pieceType().equals(Pieces.SCOUT)))
				return true;
		}
		return false;
	}
}
