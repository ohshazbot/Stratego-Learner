package stratego.learner.player;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Map;

import stratego.learner.board.Board;
import stratego.learner.board.Location;
import stratego.learner.pieces.Piece;
import stratego.learner.pieces.Pieces;

public class humanPlayer implements Player {
	Location destination;
	@Override
	public Piece getMove(Map<Piece, Location> myPieces,
			Map<Piece, Location> oppPieces, Board board, boolean redo) {
		System.out.println("Your turn- the board is:");
		System.out.println(boardString(board, myPieces, oppPieces));
		System.out.println("\n\nWhich piece would you like to move and move it where?");
		System.out.println("\nPlease provide source x-cord,y-cord destionation x-cord,y-cord\n");
		BufferedReader br = new BufferedReader( new InputStreamReader(System.in));
		try {
			String input = br.readLine();
			String[] moves = input.split(" ");
			String[] src = moves[0].split(",");
			Location source = new Location(Integer.parseInt(src[0]),Integer.parseInt(src[1]));

			String[] dest = moves[1].split(",");
			destination = new Location(Integer.parseInt(dest[0]),Integer.parseInt(dest[1]));
			return board.getPiece(source);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	private String boardString(Board board, Map<Piece, Location> myPieces,
			Map<Piece, Location> oppPieces) {
		StringBuilder sb = new StringBuilder("\033[31m\033[44m");
		for (int i = 0; i < 10; i++)
		{
			for (int j = 0; j < 10; j++)
			{
				Piece piece = board.getPiece(new Location(i, j));
				if (piece == null)
					sb.append('_');
				else
				{
					if (piece.revealed || myPieces.containsKey(piece) || piece.pieceType().equals(Pieces.WATER))
					{
						sb.append(piece.pieceType().pieceType());
					} else
						sb.append('H');
				}
			}
			sb.append('\n');
		}
		sb.append("\033[0m\n");
		return sb.toString();
	}

	@Override
	public Location moveLoc() {
		return destination;
	}

}
