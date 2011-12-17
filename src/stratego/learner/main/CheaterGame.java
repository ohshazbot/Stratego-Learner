package stratego.learner.main;

import java.util.HashMap;

import stratego.learner.board.Board;
import stratego.learner.board.Game;
import stratego.learner.board.PlayerEnum;
import stratego.learner.player.CheaterBot;
import stratego.learner.player.SPAMbot;

public class CheaterGame {

	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		String boardCoding =
"r3r8r9r1__b9b6b3b5\n"+
"r5r6r9r9__b9b9b4b5\n"+
"r4r7r6r8WWb8bSb8b5\n"+
"rBrBr7r8WWb2b6b7b4\n"+
"rFrBr9r9__bBb7bBbB\n"+
"rBrBr7rB__b9b9bBbF\n"+
"r4r7r6r2WWb8b7bBbB\n"+
"r5r8rSr8WWb8b6b7b4\n"+
"r5r4r9r9__b9b9b6b5\n"+
"r5r3r5r9__b1b9b8b3\n";
		
		HashMap<Integer, Double> qMap = new HashMap<Integer,Double>();
		int wins = 0, losses = 0;
		for (int i = 0; true; i++)
		{
			SPAMbot spammy = new SPAMbot(true, qMap, .8, 1);
			Board board = new Board(boardCoding);
			Game game = new Game(board, PlayerEnum.RED);
			int result = game.game(spammy, new CheaterBot(true), false);
			if (result == 1)
			{
				wins++;
				System.out.print("I won ");
			}
			else if (result == -1)
			{
				losses++;
				System.out.print("I lost ");
			}
			else System.out.println("What the shit is going on?");
			
			System.out.println("this one bringing me to " + wins + " wins and " + losses + " losses - net " + (wins-losses));
			if (i % 5 == 4)
			{
				spammy.checkPointMap("/Users/john/Documents/workspace/Stratego-Learner/qmap-"+i+".map");
			}
		}

	}

}
