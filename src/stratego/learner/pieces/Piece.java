package stratego.learner.pieces;

import java.util.LinkedList;
import java.util.List;

import stratego.learner.board.Board;
import stratego.learner.board.Location;

public abstract class Piece {
	boolean onBoard = true;
	public boolean revealed = false;
	private int pieceNumber;
	private boolean redPlayer;

	public Piece(int pieceNumber, boolean redOwner) {
		this.pieceNumber = pieceNumber;
		redPlayer = redOwner;
	}

	public boolean redOwner() {
		if (pieceType().equals(Pieces.WATER))
			return false;
		return redPlayer;
	}

	public boolean blueOwner() {
		if (pieceType().equals(Pieces.WATER))
			return false;
		return !redPlayer;
	}

	public int hashCode() {
		return pieceNumber;
	}

	public boolean equals(Piece otherPiece) {
		return hashCode() == otherPiece.hashCode();
	}

	public abstract Result attack(Piece defender);

	public void reveal() {
		revealed = true;
	}

	public Result defaultAttack(Piece defender) {
		if (defender.pieceType().equals(Pieces.BOMB)) {
			return new Result(false, true);
		}

		if (defender.pieceType().equals(Pieces.SPY)) {
			return new Result(true, false);
		}
		if (defender.pieceType().equals(Pieces.FLAG)) {
			return new Result(true, false);
		}

		int compare = this.pieceType().compareTo(defender.pieceType());
		if (compare > 0)
			return new Result(true, false);
		else if (compare < 0)
			return new Result(false, true);
		else {
			return new Result(false, false);
		}
	}

	public abstract boolean canMove();

	public abstract Pieces pieceType();

	public class Result {
		public boolean attackerLives;
		public boolean defenderLives;

		public Result() {
			attackerLives = false;
			defenderLives = false;
		}

		public Result(boolean attackLive, boolean defendLive) {
			attackerLives = attackLive;
			defenderLives = defendLive;
		}
	}

	public List<Location> moveLocations(Board board, Location currLoc) {
		List<Location> toRet = new LinkedList<Location>();
		if (!canMove())
			return toRet;

		for (int offset : new int[] { -1, 1 }) {
			if (board.isOpen(currLoc.xcord + offset, currLoc.ycord))
				toRet.add(new Location(currLoc.xcord + offset, currLoc.ycord));
			if (board.isOpen(currLoc.xcord, currLoc.ycord + offset))
				toRet.add(new Location(currLoc.xcord, currLoc.ycord + offset));
		}
		return toRet;
	}

	public boolean canMoveHere(Location destination, Location source,
			Board board) {
		if (!canMove())
			return false;
		if (destination.isOrthogonal(source, !pieceType().equals(Pieces.SCOUT)))
			return true;
		return false;
	}

	public static Piece makePiece(Pieces pieceType, int pieceNumber,
			boolean redOwner) {
		switch (pieceType) {
		case WATER:
			return null;
		case MARSHALL:
			return new Marshall(pieceNumber, redOwner);
		case GENERAL:
			return new General(pieceNumber, redOwner);
		case COLONEL:
			return new Colonel(pieceNumber, redOwner);
		case MAJOR:
			return new Major(pieceNumber, redOwner);
		case CAPTAIN:
			return new Captain(pieceNumber, redOwner);
		case LIEUTENANT:
			return new Lieutenant(pieceNumber, redOwner);
		case SERGEANT:
			return new Sergeant(pieceNumber, redOwner);
		case MINER:
			return new Miner(pieceNumber, redOwner);
		case SCOUT:
			return new Scout(pieceNumber, redOwner);
		case SPY:
			return new Spy(pieceNumber, redOwner);
		case BOMB:
			return new Bomb(pieceNumber, redOwner);
		case FLAG:
			return new Flag(pieceNumber, redOwner);
		}
		return null;
	}

	public String toString() {
		return pieceType().name() + "- #" + pieceNumber + ". Alive? " + onBoard
				+ " owner is " + (redPlayer ? "red." : "blue.");
	}
}
