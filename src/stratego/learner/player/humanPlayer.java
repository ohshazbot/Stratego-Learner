package stratego.learner.player;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Map;

import stratego.learner.board.Board;
import stratego.learner.board.Location;
import stratego.learner.pieces.Piece;
import stratego.learner.pieces.Pieces;

public class HumanPlayer implements Player {
	Location destination;
	boolean redPlayer;
	
	@Override
	public Piece getMove(Map<Piece, Location> myPieces,
			Map<Piece, Location> oppPieces, Board board, boolean redo) {
		System.out.println("Your turn- the board is:");
		System.out.println(boardString(board));
		System.out.println("\nWhich piece would you like to move and move it where?");
		System.out.println("Please provide source row,col destionation row,col");
		BufferedReader br = new BufferedReader( new InputStreamReader(System.in));
		try {
			String input = br.readLine();
			String[] moves = input.split(" ");
			String[] src = moves[0].split(",");
			Location source = new Location(Integer.parseInt(src[0]),Integer.parseInt(src[1]));

			String[] dest = moves[1].split(",");
			destination = new Location(Integer.parseInt(dest[0]),Integer.parseInt(dest[1]));
			
			Piece piece = board.getPiece(source);
			if (piece == null || !myPieces.containsKey(piece))
			{
				System.out.println("You selected in invalid piece of " + piece + " at " + source);
				piece = null;
			}
			else if (!source.isOrthogonal(destination, !piece.pieceType().equals(Pieces.SCOUT)))
			{
				System.out.println("You selected an invalid destination of " + destination + " from source " + source + " for piece " + piece);
				piece = null;
			}

			return board.getPiece(source);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	private String boardString(Board board) {
		StringBuilder sb = new StringBuilder();
		sb.append("  ");
		for (int i = 0; i < 10; i++)
			sb.append(i);
		sb.append('\n');
		for (int i = 0; i < 10; i++)
		{
			sb.append(i);
			sb.append(':');
			for (int j = 0; j < 10; j++)
			{
				Piece piece = board.getPiece(new Location(i, j));
				if (piece == null)
					sb.append('_');
				else
				{
					if (piece.revealed || ((redPlayer && piece.redOwner()) || (!redPlayer && piece.blueOwner())) || piece.pieceType().equals(Pieces.WATER))
					{
						sb.append(piece.pieceType().pieceType());
					} else
						sb.append('H');
				}
			}
			sb.append(':');
			sb.append(i);
			sb.append('\n');
		}
		for (int i = 0; i < 10; i++)
			sb.append(i);
		return sb.toString();
	}

	@Override
	public Location moveLoc() {
		return destination;
	}

	@Override
	public void setRedPlayer() {
		redPlayer = true;
	}

	@Override
	public void setBluePlayer() {
		redPlayer = false;
	}

}
