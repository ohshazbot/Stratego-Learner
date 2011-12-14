package stratego.learner.pieces;

import java.util.LinkedList;
import java.util.List;

import stratego.learner.board.Board;
import stratego.learner.board.Location;
import stratego.learner.board.PlayerEnum;

public abstract class Piece {
	boolean onBoard = true;
	public boolean revealed = false;
	public boolean moved = false;
	private PlayerEnum owner;

	public Piece(PlayerEnum own) {
		owner = own;
	}

	protected Piece() {
		// TODO Auto-generated constructor stub
	}

	public boolean redOwner() {
		if (pieceType().equals(Pieces.WATER))
			return false;
		return owner.equals(PlayerEnum.RED);
	}

	public boolean blueOwner() {
		if (pieceType().equals(Pieces.WATER))
			return false;
		return owner.equals(PlayerEnum.BLUE);
	}
	
	public PlayerEnum owner() {
		return owner;
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
		if (compare < 0)
			return new Result(true, false);
		else if (compare > 0)
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

	public boolean canMoveHere(Location source, Location destination,
			Board board) {
		if (!canMove())
			return false;
		if (source.isOrthogonal(destination, !pieceType().equals(Pieces.SCOUT)))
			return true;
		return false;
	}

	public static Piece makePiece(Pieces pieceType, PlayerEnum owner) {
		switch (pieceType) {
		case WATER:
			return null;
		case MARSHALL:
			return new Marshall(owner);
		case GENERAL:
			return new General(owner);
		case COLONEL:
			return new Colonel(owner);
		case MAJOR:
			return new Major(owner);
		case CAPTAIN:
			return new Captain(owner);
		case LIEUTENANT:
			return new Lieutenant(owner);
		case SERGEANT:
			return new Sergeant(owner);
		case MINER:
			return new Miner(owner);
		case SCOUT:
			return new Scout(owner);
		case SPY:
			return new Spy(owner);
		case BOMB:
			return new Bomb(owner);
		case FLAG:
			return new Flag(owner);
		}
		return null;
	}

	public boolean isWater() {
		return pieceType().equals(Pieces.WATER);
	}

	public boolean canBeSeen(PlayerEnum turn) {
		if (revealed)
			return true;
		if (owner.equals(turn))
			return true;
		return false;
	}

	public void moving() {
		moved = true;
	}
}
