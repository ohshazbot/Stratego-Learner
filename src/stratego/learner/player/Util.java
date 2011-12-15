package stratego.learner.player;

import java.util.List;
import java.util.Random;

import stratego.learner.board.Board;
import stratego.learner.board.Location;
import stratego.learner.board.PlayerEnum;

public class Util {
	public static Action randomAction(List<Location> myPieces, Board board, PlayerEnum player)
	{
		Random r = new Random();
		Location piece = null;
		List<Location> destinations = null;
		
		while (piece == null)
		{
			int index = r.nextInt(myPieces.size());
			piece = myPieces.get(index);
		
			destinations = board.occupyLocations(piece, player);
			
			if (destinations.size() == 0)
			{
				myPieces.remove(index);
				piece = null;
			}
		}
		
		Location destination = destinations.get(r.nextInt(destinations.size()));
		return new Action(piece, destination);
	}
}