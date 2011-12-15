package stratego.learner.player;

import java.util.List;

import stratego.learner.board.Board;
import stratego.learner.board.Location;
import stratego.learner.board.PlayerEnum;

public class RandomPlayer implements Player {
	PlayerEnum player;
	
	@Override
	public void setRedPlayer() {
		player = PlayerEnum.RED;
	}

	@Override
	public void setBluePlayer() {
		player = PlayerEnum.BLUE;
	}

	@Override
	public Action getAction(List<Location> myPieces, List<Location> oppPieces,
			Board board, boolean redo) {
		return Util.randomAction(myPieces, board, player);
	}

	@Override
	public void wins() {
		// TODO Auto-generated method stub

	}

	@Override
	public void loses() {
		// TODO Auto-generated method stub

	}

}
