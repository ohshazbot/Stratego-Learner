package stratego.learner.board;

import java.util.List;

import stratego.learner.pieces.Piece;
import stratego.learner.pieces.Piece.Result;
import stratego.learner.pieces.Pieces;
import stratego.learner.player.Action;
import stratego.learner.player.Player;
import stratego.learner.player.Util;


public class Game {
	Board board;
	PlayerEnum turn;
	boolean gameOver = false;
	
	public Game(Board startBoard, PlayerEnum start)
	{
		board = startBoard;
		turn = start;
	}
	
	public boolean move(Action act)
	{
		Piece piece = board.get(act.src);
		if (!piece.canMoveHere(act.src, act.dest, board))
			return false;
		
		Piece opponent = board.get(act.dest);
		if (opponent == null)
			try {
				movePiece(act);
			} catch (InvalidLocationException e1) {
				e1.printStackTrace();
				System.err.println("This really shouldn't happen!");
				return false;
			}
		else
		{
			Result result = piece.attack(opponent);
			piece.reveal();
			opponent.reveal();
			if (!result.attackerLives)
			{
				board.remove(act.src);
			}
			if (!result.defenderLives)
			{
				if (opponent.pieceType().equals(Pieces.FLAG))
					gameOver = true;
				board.remove(act.dest);
				if (result.attackerLives)
					try {
						movePiece(act);
					} catch (InvalidLocationException e) {
						e.printStackTrace();
						System.err.println("This really shouldn't happen!");
						return false;
					}
			}
		}
		return true;
	}

	private void movePiece(Action act) throws InvalidLocationException {
		board.get(act.src).moving();
		Location curr = act.src;
		board.move(curr, act.dest);
	}

	public int game(Player rPlayer, Player bPlayer, boolean printBoard)
	{
		int nummoves = 0;
		rPlayer.setRedPlayer();
		bPlayer.setBluePlayer();
		while(!gameOver)
		{
			if (printBoard)
				System.out.println(boardString(board));

			Player currPlayer;
			if (turn.equals(PlayerEnum.RED)){
				currPlayer = rPlayer;
			}
			else {
				currPlayer = bPlayer;
			}
			
			Action action = null;
			List<Location> myLocs;
			List<Location> oppLoc;
			myLocs = board.getPlayerLocations(turn);
			oppLoc = board.getPlayerLocations(turn.opposite());
			GameState gs;
			if (turn.equals(PlayerEnum.RED))
			{
				gs = new GameState(board, myLocs, oppLoc);
			} else
			{
				gs = new GameState(board, oppLoc, myLocs);
			}
			
			if (Util.getAllActions(gs, false, turn).size() == 0 )
			{
				if (turn.equals(PlayerEnum.BLUE))
				{
					rPlayer.wins();
					bPlayer.loses();
					return 1;
				}
				else
				{
					bPlayer.wins();
					rPlayer.loses();
					return -1;
				}
			}
			
			while (action == null)
			{
				action = currPlayer.getAction(myLocs, oppLoc, board, false);
				if (action == null)
				{
					myLocs = board.getPlayerLocations(turn);
					oppLoc = board.getPlayerLocations(turn.opposite());
				}
			}
			while (!move(action))
			{
				myLocs = board.getPlayerLocations(turn);
				oppLoc = board.getPlayerLocations(turn.opposite());
				action = currPlayer.getAction(myLocs, oppLoc, board, true);
			}
			//System.out.println(board.toString());
			if (!gameOver)
				turn = turn.opposite();
			else
			{
				if (turn.equals(PlayerEnum.RED))
				{
					rPlayer.wins();
					bPlayer.loses();
					return 1;
				}
				else
				{
					bPlayer.wins();
					rPlayer.loses();
					return -1;
				}
			}
			if(nummoves > 1000)
				return -7;
			nummoves++;
		}
		return 0;
	}
	
	private String boardString(Board board) {
		StringBuilder sb = new StringBuilder();
		sb.append("  ");
		for (int i = 0; i < 10; i++)
		{
			sb.append(' ');
			sb.append(i);
		}
		sb.append('\n');
		for (int i = 0; i < 10; i++)
		{
			sb.append(i);
			sb.append(':');
			for (int j = 0; j < 10; j++)
			{
				Piece piece = board.board[i][j];
				if (piece == null)
					sb.append(" _");
				else if (piece.isWater())
					sb.append(" W");
				else
				{
					sb.append(piece.owner().getType());
					if (piece.canBeSeen(turn))
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
		sb.append("  ");
		for (int i = 0; i < 10; i++)
		{
			sb.append(' ');
			sb.append(i);
		}
		return sb.toString();
	}
}
