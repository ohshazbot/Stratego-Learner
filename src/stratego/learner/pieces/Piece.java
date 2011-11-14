package stratego.learner.pieces;

import stratego.learner.board.Board;

public abstract class Piece {
	public class Location {
		public int xcord, ycord;
		
		public Location(){
			xcord = -1;
			ycord = -1;
		}
		public Location(int xcord2, int ycord2) {
			xcord = xcord2;
			ycord = ycord2;
		}

		public void set(Location l)
		{
			xcord = l.xcord;
			ycord = l.ycord;
		}
	}
	
	Location loc = null;
	boolean onBoard = false;
	
	public Piece()
	{
		loc = new Location();
	}
	
	public Piece(int xcord, int ycord)
	{
		loc = new Location(xcord, ycord);
		onBoard = true;
	}
	
	public Location getLoc()
	{
		return loc;
	}
	
	public int getXCord()
	{
		return loc.xcord;
	}
	
	public int getYCord()
	{
		return loc.ycord;
	}
	
	public void setLocation(Location l)
	{
		loc.set(l);
	}
	
	public void setXCord(int xcord)
	{
		loc.xcord=xcord;
	}
	
	public void setYCord(int ycord)
	{
		loc.ycord=ycord;
	}
	
	public abstract void attack(Piece defender, Board gameBoard);
	
	public void defaultAttack(Piece defender, Board gameboard) {
		if (defender.pieceType().equals(Pieces.BOMB)) {
			gameboard.remove(this);
		} else {
			int compare = this.pieceType().compareTo(defender.pieceType());
			if (compare > 0)
				gameboard.remove(defender);
			else if (compare < 0)
				gameboard.remove(this);
			else {
				gameboard.remove(this);
				gameboard.remove(defender);
			}
		}
	}
	
	public abstract boolean canMove();
	
	public abstract Pieces pieceType();
}
