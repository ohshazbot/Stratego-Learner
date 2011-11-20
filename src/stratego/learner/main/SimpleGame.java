package stratego.learner.main;

import java.util.HashMap;
import java.util.Map;

import stratego.learner.board.Board;
import stratego.learner.board.Game;
import stratego.learner.board.Location;
import stratego.learner.pieces.Piece;
import stratego.learner.pieces.Pieces;
import stratego.learner.player.HumanPlayer;

public class SimpleGame {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		int cnt = 0;
		int cnt2 = 0;
		Board board = new Board();
		Map<Piece, Location> redPieces = new HashMap<Piece, Location>(), bluePieces = new HashMap<Piece, Location>();
		for (Pieces pieces : Pieces.values())
		{
			Piece toPlace = Piece.makePiece(pieces, cnt2++, true);
			Location toLoc = new Location(cnt%10,cnt/10);
			board.addPiece(toPlace, toLoc);
			redPieces.put(toPlace, toLoc);
			
			toPlace = Piece.makePiece(pieces, cnt2++, false);
			toLoc = new Location(cnt%10,10-cnt/10);
			board.addPiece(toPlace, toLoc);
			bluePieces.put(toPlace, toLoc);
			
			cnt++;
		}
		
		Game game = new Game(board, true);
		game.game(new HumanPlayer(), new HumanPlayer(), redPieces, bluePieces);

	}

}
