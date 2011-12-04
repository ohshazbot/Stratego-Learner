package stratego.learner.main;

import stratego.learner.board.Board;
import stratego.learner.board.Game;
import stratego.learner.board.PlayerEnum;
import stratego.learner.player.CheaterBot;

public class CheaterGame {

	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		String boardCoding =
"r3r8r9r9__b9b6b3b5\n"+
"r5r6r1r9__b9b9b4b5\n"+
"r4r7r6r8WWb8bSb8b5\n"+
"rBrBr7r8WWb2b6b7b4\n"+
"rFrBr9r9__bBb7bBbB\n"+
"rBrBr7rB__b9b9bBbF\n"+
"r4r7r6r2WWb8b7bBbB\n"+
"r8rSr8r5WWb8b6b7b4\n"+
"r9r9r4r5__b9b1b6b5\n"+
"r9r6r3r5__b9b9b8b3\n";
		Board board = new Board(boardCoding);
		
		Game game = new Game(board, PlayerEnum.RED);
		game.game(new CheaterBot(), new CheaterBot(), true);

	}

}
