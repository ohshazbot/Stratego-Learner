package stratego.learner.main;

import stratego.learner.board.Board;
import stratego.learner.board.Game;
import stratego.learner.board.Location;
import stratego.learner.board.PlayerEnum;
import stratego.learner.pieces.Piece;
import stratego.learner.pieces.Pieces;
import stratego.learner.player.HumanPlayer;

public class SimpleGame {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		int cnt = 0;
		Board board = new Board();
		for (Pieces pieces : Pieces.values())
		{
			Piece toPlace = Piece.makePiece(pieces, PlayerEnum.RED);
			Location toLoc = new Location(cnt%10, 3-cnt/10);
			board.put(toLoc, toPlace);
			
			toPlace = Piece.makePiece(pieces, PlayerEnum.BLUE);
			toLoc = new Location(cnt%10, 6+cnt/10);
			board.put(toLoc, toPlace);
			
			cnt++;
		}
		
		Game game = new Game(board, PlayerEnum.RED);
		game.game(new HumanPlayer(), new HumanPlayer(), true);

	}

}
