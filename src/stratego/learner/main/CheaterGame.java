package stratego.learner.main;

import java.util.HashMap;
import java.util.Map;

import stratego.learner.board.Board;
import stratego.learner.board.Game;
import stratego.learner.board.Location;
import stratego.learner.pieces.Piece;
import stratego.learner.player.CheaterBot;

public class CheaterGame {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Board board = new Board();
		Map<Piece, Location> redPieces = new HashMap<Piece, Location>(), bluePieces = new HashMap<Piece, Location>();
		
		String boardCoding =
"7211__1475\n"+
"5491__1165\n"+
"6342WW2S25\n"+
"BB32WW8436\n"+
"*B11__B3BB\n"+
"BB3B__11B*\n"+
"6348WW23BB\n"+
"52S2WW2436\n"+
"5611__1945\n"+
"5741__1127\n";
		board.load(boardCoding, redPieces, bluePieces);
		
		Game game = new Game(board, true, redPieces, bluePieces);
		game.game(new CheaterBot(), new CheaterBot(), true);

	}

}
