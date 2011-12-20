package stratego.learner.main;

import java.util.HashMap;

import stratego.learner.board.Board;
import stratego.learner.board.Game;
import stratego.learner.board.PlayerEnum;
import stratego.learner.player.CheaterBot;
import stratego.learner.player.RandomPlayer;
import stratego.learner.player.SPAMbot;

public class CheaterGame {

	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		String boardCoding =
"r3r8rFr7__b9bBb3b5\n"+
"rBr6r9r9__b1b9b4b5\n"+
"r4r9r6r8WWb8bSb8bB\n"+
"r7rBr7r8WWb2b6b7b4\n"+
"r5r9r9r4__b5b7bBb7\n"+
"r9rBrBr5__b9b9b6b3\n"+
"rBr7r6r2WWb8b7bBb9\n"+
"r5r8rSr8WWb8b6bBb4\n"+
"rBr4r9r1__b5bFb6bB\n"+
"r9r3r5r9__b9b9b8b7\n";
		String boardCoding2 = "r3r8r9r1__b9b6b3b5\n"+
		"r5r6r9r9__b9b9b4b5\n"+
		"r4r7r6r8WWb8bSb8b5\n"+
		"rBrBr7r8WWb2b6b7b4\n"+
		"rFrBr9r9__bBb7bBbB\n"+
		"rBrBr7rB__b9b9bBbF\n"+
		"r4r7r6r2WWb8b7bBbB\n"+
		"r5r8rSr8WWb8b6b7b4\n"+
		"r5r4r9r9__b9b9b6b5\n"+
		"r5r3r5r9__b1b9b8b3\n";
		//HashMap<Integer, Double> qMapA = new HashMap<Integer,Double>();
		//HashMap<Integer, Double> qMapB = new HashMap<Integer,Double>();
		
		//SPAMbot spammyA = new SPAMbot(true, "/home/tim/Desktop/Stratego-Learner/qmap-18004.map", .1, 1);
		System.out.println("A loaded");
		//SPAMbot spammyB = new SPAMbot(true, "/home/tim/Desktop/Stratego-Learner/qmap-random-8004.map", .1, 1);
		System.out.println("B loaded. \nLet the games begin!");
		Board board;
		Game game;
		for (int i = 4; i<= 1404; i+=200)
		{
			int wins = 0, losses = 0, ties = 0;
			for(int j = 0; j < 25; j++)
			{
				SPAMbot spammyA = new SPAMbot(false, "/home/tim/Desktop/Stratego-Learner/qmap-cross-B-" + i +".map", 0, 1);
				board = new Board(boardCoding2);
				game = new Game(board, PlayerEnum.RED);
				int result = game.game(spammyA, new RandomPlayer(), false);
				if (result == 1)
				{
					wins++;
				//System.out.print("A won ");
				}
				else if (result == -1)
				{
					losses++;
					//System.out.print("B won ");
				}	
				else if (result == -7)
				{
					ties++;
					//System.out.print("Too many moves!");
				}
				else System.out.println("What the shit is going on?");
			}
			System.out.println("this one bringing A to " + wins + " wins and " + ties + " ties and " + losses + " losses - net " + (wins-losses));
			//if (i % 200 == 4)
			//{
				//spammyA.checkPointMap("/home/tim/Desktop/Stratego-Learner/qmap-cross-A-"+i+".map");
				//spammyB.checkPointMap("/home/tim/Desktop/Stratego-Learner/qmap-cross-B-"+i+".map");
			//}
		}

	}

}
