package stratego.learner.board;

import java.util.Map;

import stratego.learner.pieces.Piece;
import stratego.learner.pieces.Piece.Result;
import stratego.learner.pieces.Pieces;
import stratego.learner.player.Player;


public class Game {
	Map<Piece, Location> redPlayer;
	Map<Piece, Location> bluePlayer;
	Board board;
	boolean redTurn;
	boolean gameOver = false;
	
	public Game(Board startBoard, boolean redStart, Map<Piece, Location> redPieces, Map<Piece, Location> bluePieces)
	{
		board = startBoard;
		redTurn = redStart;
		redPlayer = redPieces;
		bluePlayer = bluePieces;
	}
	
	public boolean move(Piece piece, Location destination)
	{
		Map<Piece, Location> playerMap = (redTurn? redPlayer : bluePlayer);
		if (!piece.canMoveHere(destination, playerMap.get(piece), board))
			return false;
		Piece opponent = board.getPiece(destination);
		if (opponent == null)
			movePiece(piece, destination, playerMap);
		else
		{
			Result result = piece.attack(opponent);
			if (!result.attackerLives)
			{
				remove(piece, playerMap);
			}
			if (!result.defenderLives)
			{
				if (opponent.pieceType().equals(Pieces.FLAG))
					gameOver = true;
				remove(opponent, (!redTurn? redPlayer : bluePlayer));
				if (result.attackerLives)
					movePiece(piece, destination, playerMap);
			}
		}
		return true;
	}

	private void movePiece(Piece piece, Location destination, Map<Piece, Location> playerMap) {
		Location curr = playerMap.get(piece);
		board.move(curr, destination);
		playerMap.get(piece).set(destination);
	}

	private void remove(Piece piece, Map<Piece, Location> ownerMap) {
		board.remove(ownerMap.remove(piece));
	}
	
	public void game(Player rPlayer, Player bPlayer, boolean printBoard)
	{
		rPlayer.setRedPlayer();
		bPlayer.setBluePlayer();
		while(!gameOver)
		{
			if (printBoard)
				System.out.println(boardString(board));
			Player currPlayer;
			Map<Piece, Location> myPieces, oppPieces;
			if (redTurn){
				currPlayer = rPlayer;
				myPieces = redPlayer;
				oppPieces = bluePlayer;
			}
			else {
				currPlayer = bPlayer;
				myPieces = bluePlayer;
				oppPieces = redPlayer;
			}
			
			Piece piece = null;
			while (piece == null)
			{
				piece = currPlayer.getMove(myPieces, oppPieces, board, false);
				if (!myPieces.containsKey(piece))
					piece = null;
			}
			Location destination = currPlayer.moveLoc();
			while (!move(piece, destination))
			{
				piece = currPlayer.getMove(myPieces, oppPieces, board, true);
				destination = currPlayer.moveLoc();
			}
			
			redTurn = !redTurn;
		}
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
					if (piece.revealed || ((redTurn && piece.redOwner()) || (!redTurn && piece.blueOwner())) || piece.pieceType().equals(Pieces.WATER))
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
			sb.append(i);
		return sb.toString();
	}
}
