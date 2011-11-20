package stratego.learner.pieces;

import java.util.List;

import stratego.learner.board.Board;
import stratego.learner.board.Location;


public class Scout extends Piece {

	public Scout(int pieceNumber, boolean redPlayer) {
		super(pieceNumber, redPlayer);
	}

	@Override
	public Result attack(Piece defender) {
		this.reveal();
		defender.reveal();
		return defaultAttack(defender);
	}

	@Override
	public boolean canMove() {
		return true;
	}

	@Override
	public Pieces pieceType() {
		return Pieces.SCOUT;
	}

	public List<Location> moveLocations(Board board, Location currLoc)
	{
		List<Location> toRet = super.moveLocations(board, currLoc);
		// If we have no valid moves now, there will be no more looking further out
		if (toRet.size()!= 0)
		{
			boolean xpos = true, ypos = true, xneg = true, yneg = true;
			for (int i = 2; (xpos || ypos || xneg || yneg); i++)
			{
				if (xpos)
				{
					if ((xpos = board.isOpen(currLoc.xcord+i, currLoc.ycord)))
						toRet.add(new Location(currLoc.xcord+i, currLoc.ycord));
				}
				if (xneg)
				{
					if ((xneg = board.isOpen(currLoc.xcord-i, currLoc.ycord)))
						toRet.add(new Location(currLoc.xcord-i, currLoc.ycord));
				}
				if (ypos)
				{
					if ((ypos = board.isOpen(currLoc.xcord, currLoc.ycord+i)))
						toRet.add(new Location(currLoc.xcord, currLoc.ycord+i));
				}
				if (yneg)
				{
					if ((yneg = board.isOpen(currLoc.xcord, currLoc.ycord-i)))
						toRet.add(new Location(currLoc.xcord, currLoc.ycord-i));
				}
			}
		}
		return toRet;
	}
}
