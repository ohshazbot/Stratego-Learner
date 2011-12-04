package stratego.learner.player;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

import stratego.learner.board.Board;
import stratego.learner.board.Location;
import stratego.learner.pieces.Piece;
import stratego.learner.pieces.Pieces;

public class HumanPlayer implements Player {
	boolean redPlayer;
	
	@Override
	public void setRedPlayer() {
		redPlayer = true;
	}

	@Override
	public void setBluePlayer() {
		redPlayer = false;
	}

	@Override
	public Action getAction(List<Location> myPieces, List<Location> oppPieces,
			Board board, boolean redo) {
		System.out.println("\nWhich piece would you like to move and move it where?");
		System.out.println("Please provide source row,col destionation row,col");
		BufferedReader br = new BufferedReader( new InputStreamReader(System.in));
		try {
			String input = br.readLine();
			String[] moves = input.split(" ");
			String[] src = moves[0].split(",");
			Location source = new Location(Integer.parseInt(src[0]),Integer.parseInt(src[1]));

			String[] dest = moves[1].split(",");
			Location destination = new Location(Integer.parseInt(dest[0]),Integer.parseInt(dest[1]));
			
			Piece piece = board.get(source);
			if (piece == null || !myPieces.contains(source))
			{
				System.out.println("You selected in invalid piece of " + piece + " at " + source);
				piece = null;
			}
			else if (!source.isOrthogonal(destination, !piece.pieceType().equals(Pieces.SCOUT)))
			{
				System.out.println("You selected an invalid destination of " + destination + " from source " + source + " for piece " + piece);
				piece = null;
			}

			return new Action(source, destination);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public void wins() {
		System.out.println("You win");
	}

	@Override
	public void loses() {
		System.out.println("You lose");
	}

}
