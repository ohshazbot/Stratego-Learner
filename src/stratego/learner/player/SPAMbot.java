package stratego.learner.player;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Set;

import stratego.learner.board.Board;
import stratego.learner.board.Location;
import stratego.learner.board.PlayerEnum;
import stratego.learner.pieces.Bomb;
import stratego.learner.pieces.Captain;
import stratego.learner.pieces.Colonel;
import stratego.learner.pieces.Flag;
import stratego.learner.pieces.General;
import stratego.learner.pieces.Lieutenant;
import stratego.learner.pieces.Major;
import stratego.learner.pieces.Marshall;
import stratego.learner.pieces.Miner;
import stratego.learner.pieces.Piece;
import stratego.learner.pieces.Pieces;
import stratego.learner.pieces.Scout;
import stratego.learner.pieces.Sergeant;
import stratego.learner.pieces.Spy;

public class SPAMbot implements Player {
	PlayerEnum player;
	boolean training;
	Map<Integer, Double> qMap;
	double learningRate;
	double discountRate;
	
	public SPAMbot(boolean trainer, Map<Integer, Double> qMatrix, double lr, double dr)
	{
		training = trainer;
		qMap = qMatrix;
		learningRate = lr;
		discountRate = dr;
	}

	public SPAMbot(boolean trainer, String checkPointFile, double lr, double dr) throws NumberFormatException, IOException
	{
		training = trainer;
		qMap = parseFile(checkPointFile);
		learningRate = lr;
		discountRate = dr;
	}

	private Map<Integer, Double> parseFile(String checkPointFile) throws NumberFormatException, IOException  {
		Map<Integer, Double> toRet = new HashMap<Integer, Double>();
		BufferedReader br;

		try {
			br = new BufferedReader(new FileReader(checkPointFile));
		} catch (FileNotFoundException e) {
			System.out.println("File not found, starting from scratch");
			return toRet;
		}

		String line;
		while ((line = br.readLine()) != null)
		{
			String[] pieces = line.split(",");
			Integer key = new Integer(Integer.parseInt(pieces[0]));
			Double value = Double.parseDouble(pieces[1]);
			toRet.put(key, value);
		}

		return toRet;
	}

	public Map<Integer, Double> getMap()
	{
		return qMap;
	}

	public boolean checkPointMap(String file) throws IOException
	{
		File f = new File(file);
		if (f.exists())
			return false;

		FileWriter fw = new FileWriter(f);
		for (Entry<Integer, Double> entry : qMap.entrySet())
		{
			fw.write(entry.getKey().intValue()+","+Double.toHexString(entry.getValue()));
		}
		return true;
	}

	@Override
	public void setRedPlayer() {
		player = PlayerEnum.RED;
	}

	@Override
	public void setBluePlayer() {
		player = PlayerEnum.BLUE;
	}

	boolean scoutsLeft;
	@Override
	public Action getAction(List<Location> myPieces, List<Location> oppPieces,
			Board board, boolean redo) {
		scoutsLeft = false;
		for (Location l : oppPieces)
		{
			if (board.get(l).pieceType().equals(Pieces.SCOUT) && !board.get(l).revealed)
			{
				scoutsLeft = true;
				break;
			}
		}
		
		Map<Location, List<Location>> possibleActions = getAllActions(myPieces, oppPieces, board, false);
		Map<Action, List<Integer>> actionMap = getAllPossibleNextStates(possibleActions, myPieces, oppPieces, board);

		Double maxQ = Double.MIN_VALUE;
		Set<Action> tieActions = new HashSet<Action>();
		for (Entry<Action, List<Integer>> e : actionMap.entrySet())
		{
			for (Integer Integer : e.getValue())
			{
				Double IntegerVal = qMap.get(Integer); 
				if (maxQ < IntegerVal)
				{
					maxQ = IntegerVal;
					tieActions.clear();
					tieActions.add(e.getKey());
				} else if (maxQ == IntegerVal)
				{
					tieActions.add(e.getKey());
				}
			}
		}

		Random r = new Random();
		Action taken = (Action) tieActions.toArray()[r.nextInt(tieActions.size())];
		
		Integer current = encode(myPieces,oppPieces, board);
		
		if (training)
		{			
			qMap.put(current, (1 - learningRate) * qMap.get(current) + (learningRate) * (reward(taken, myPieces, oppPieces, board) + discountRate * maxQ) );
		}
		
		return taken;
	}

	private double reward(Action taken, List<Location> myPieces, List<Location> oppPieces, Board board) {
		if (board.get(taken.dest).pieceType() == Pieces.FLAG)
			return 100;
		for(Entry<Action, List<Integer>> entry : getAllPossibleNextStates(getAllActions(myPieces, oppPieces, board), myPieces, oppPieces, board).entrySet())
		{
			for(Integer state: entry.getValue())
				if (state.intValue() % 2 == 1)
					return -100;
		}
		return 0;
	}

	private Integer encode(List<Location> myPieces, List<Location> oppPieces, Board board) {
		//first iterate through my pieces to see if i can attack something of theirs
		Map<String,Set<Piece>> attackers = new HashMap<String,Set<Piece>>();
		attackers.put(player.name(), new HashSet<Piece>());
		attackers.put(player.opposite().name(), new HashSet<Piece>());
		for(Location loc : myPieces)
		{
			for(Location newloc : board.occupyLocations(loc, player, false))
			{
				Piece p = board.get(newloc.xcord, newloc.ycord);
				if(p != null && !p.isWater())
				{
					attackers.get(player.name()).add(board.get(loc.xcord, loc.ycord));
					attackers.get(player.opposite().name()).add(p);
				}
			}
		}
		//then iterate through theirs to see if they can hit mine, adding to set all along
		for(Location loc : oppPieces)
		{
			for(Location newloc : board.occupyLocations(loc, player, false))
			{
				Piece p = board.get(newloc.xcord, newloc.ycord);
				if(p != null && !p.isWater())
				{
					attackers.get(player.opposite().name()).add(board.get(loc.xcord, loc.ycord));
					attackers.get(player.name()).add(p);
				}
			}
		}
		int answer = 0; 
		int mult;
		boolean mine;
		for(Entry<String,Set<Piece>> entry: attackers.entrySet())
		{
			mine =  entry.getKey().equals(player.name());
			mult = mine ? 1 : 4096;
			
			for(Piece p : entry.getValue())
			{
				char type = p.pieceType().pieceType();
				if (!mine)
				{
					if(!p.revealed)
					{
						if(p.moved)
							answer |= mult * 4096;
						else
							answer |= mult * 8192;
					}
				}
				else{
				switch (p.pieceType()) {
				case WATER:
					answer |= mult * 0;
					break;
				case FLAG:
					answer |= mult * 1;
					break;
				case GENERAL:
					answer |= mult * 2;
					break;
				case COLONEL:
					answer |= mult * 4;
					break;
				case MAJOR:
					answer |= mult * 8;
					break;
				case CAPTAIN:
					answer |= mult * 16;
					break;
				case LIEUTENANT:
					answer |= mult * 32;
					break;
				case SERGEANT:
					answer |= mult * 64;
					break;
				case MINER:
					answer |= mult * 128;
					break;
				case SCOUT:
					answer |= mult * 256;
					break;
				case SPY:
					answer |= mult * 512;
					break;
				case BOMB:
					answer |= mult * 1024;
					break;
				case MARSHALL:
					answer |= mult * 2048;
					break;
				}
				}
			}
		}
		return new Integer(answer);
	}

	private Map<Action, List<Integer>> getAllPossibleNextStates(
			Map<Location, List<Location>> possibleActions,
			List<Location> myPieces, List<Location> oppPieces, Board board) {
		Map<Action, List<Integer>> toRet = new HashMap<Action, List<Integer>>();
		
		for (Entry<Location, List<Location>> e : possibleActions.entrySet())
		{
			Location src = e.getKey();
			for (Location dest : e.getValue())
			{
				List<Board> potentialBoards = potentialBoards(board, src, dest);
//				Piece mine = potentialBoard.get(src);
//				Piece opp = potentialBoard.get(dest);
//				if (opp == null || (opp.revealed && mine.attack(opp).attackerLives) || !opp.revealed)
//				{
//					potentialBoard.remove(src);
//					potentialBoard.put(dest, mine);
//					
//					oppPieces.remove(dest);
//					myPieces.remove(src);
//					myPieces.add(dest);
//					
//					Map<Location, List<Location>> actions = getAllActions(oppPieces, myPieces, potentialBoard, false);
//					toRet.putAll(getBest(actions, potentialBoard));
//					
//					myPieces.remove(dest);
//					myPieces.add(src);
//					oppPieces.add(dest);
//					
//					potentialBoard.put(dest, opp);
//					potentialBoard.put(src, mine);
//				}
//				//Should put in logic to check if remaining pieces on board can kill mine
//				if (opp != null && !opp.revealed)
//				{
//					// We lose
//					potentialBoard.remove(src);
//					myPieces.remove(src);
//					
//					Map<Location, List<Location>> actions = getAllActions(oppPieces, myPieces, potentialBoard, false);
//					toRet.putAll(getBest(actions, potentialBoard));
//
//					// We tie
//					potentialBoard.remove(dest);
//					oppPieces.remove(dest);
//					
//					actions = getAllActions(oppPieces, myPieces, potentialBoard, false);
//					toRet.putAll(getBest(actions, potentialBoard));
//
//					
//				}


			}
		}
		
		return toRet;
	}

	private List<Board> potentialBoards(Board board, Location src, Location dest) {
		List<Board> toRet = new LinkedList<Board>();
		Piece mine = board.get(src);
		Piece opp = board.get(dest);
		if (opp == null || (opp.revealed && mine.attack(opp).attackerLives) || !opp.revealed)
		{
			Board potentialBoard = new Board(board);
			potentialBoard.remove(src);
			potentialBoard.put(dest, mine);
			// I need to use tuples, not just boards, of the potentials, I think...
			oppPieces.remove(dest);
			myPieces.remove(src);
			myPieces.add(dest);
		}
		return toRet;
	}

	private Map<Location, List<Location>> getAllActions(List<Location> myPieces,
			List<Location> oppPieces, Board board, boolean known) {
		Map<Location, List<Location>> toRet = new HashMap<Location, List<Location>>();
		for (Location loc : myPieces)
		{
			List<Location> destinations = board.occupyLocations(loc, player, (!known && scoutsLeft));
			if (destinations.size() != 0)
				toRet.put(loc, destinations);
		}
		
		return toRet;
	}

	@Override
	public void wins() {
		System.out.println("You win");
	}

	@Override
	public void loses() {
		System.out.println("You lose");
	}

}
