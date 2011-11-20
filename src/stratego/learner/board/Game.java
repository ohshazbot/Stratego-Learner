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
	
	public Game(Board startBoard, boolean redStart)
	{
		board = startBoard;
		redTurn = redStart;
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
	
	public void game(Player rPlayer, Player bPlayer, Map<Piece, Location> redPieces, Map<Piece, Location> bluePieces)
	{
		while(!gameOver)
		{
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
			while (piece != null && myPieces.containsKey(piece))
				piece = currPlayer.getMove(myPieces, oppPieces, board, false);
			Location destination = currPlayer.moveLoc();
			while (!move(piece, destination))
			{
				piece = currPlayer.getMove(myPieces, oppPieces, board, true);
				destination = currPlayer.moveLoc();
			}
			
			redTurn = !redTurn;
		}
	}
}
