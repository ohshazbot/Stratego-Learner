package stratego.learner.player;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import stratego.learner.board.Board;
import stratego.learner.board.GameState;
import stratego.learner.board.LocDist;
import stratego.learner.board.Location;
import stratego.learner.board.PlayerEnum;
import stratego.learner.pieces.Piece;
import stratego.learner.pieces.Pieces;

public class Util {
	public static Action randomAction(List<Location> myPieces, Board board, PlayerEnum player)
	{
		Random r = new Random();
		Location piece = null;
		List<LocDist> destinations = null;
		
		while (piece == null)
		{
			int index = r.nextInt(myPieces.size());
			piece = myPieces.get(index);
		
			destinations = board.occupyLocations(piece, player, false);
			
			if (destinations.size() == 0)
			{
				myPieces.remove(index);
				piece = null;
			}
		}
		
		LocDist destination = destinations.get(r.nextInt(destinations.size()));
		return new Action(piece, destination.loc);
	}
	
	public static Map<Location, List<LocDist>> getAllActions(GameState gs, boolean known, PlayerEnum play) {
		boolean scoutsLeft = false;
		List<Location> oppPieces;
		if (play.equals(PlayerEnum.RED))
			oppPieces = gs.bluPieces;
		else
			oppPieces = gs.redPieces;

		for (Location l : oppPieces)
		{
			Piece p = gs.board.get(l);
			if (!p.revealed && p.pieceType().equals(Pieces.SCOUT))
			{
				scoutsLeft = true;
				break;
			}
		}
		
		Map<Location, List<LocDist>> toRet = new HashMap<Location, List<LocDist>>();
		List<Location> myPieces;
		if (play.equals(PlayerEnum.RED))
			myPieces = gs.redPieces;
		else
			myPieces = gs.bluPieces;
		
		for (Location loc : myPieces)
		{
			List<LocDist> destinations = gs.board.occupyLocations(loc, play, (!known && scoutsLeft));
			if (destinations.size() != 0)
				toRet.put(loc, destinations);
		}
		
		return toRet;
	}

	
}
