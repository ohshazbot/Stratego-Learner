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

import com.sun.org.apache.xml.internal.security.exceptions.Base64DecodingException;
import com.sun.org.apache.xml.internal.security.utils.Base64;

import stratego.learner.board.Board;
import stratego.learner.board.Location;
import stratego.learner.board.PlayerEnum;
import stratego.learner.pieces.Piece;
import stratego.learner.pieces.Pieces;

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

	public SPAMbot(boolean trainer, String checkPointFile, double lr, double dr) throws Base64DecodingException, IOException
	{
		training = trainer;
		qMap = parseFile(checkPointFile);
		learningRate = lr;
		discountRate = dr;
	}

	private Map<Integer, Double> parseFile(String checkPointFile) throws IOException, Base64DecodingException {
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

	@Override
	public Action getAction(List<Location> myPieces, List<Location> oppPieces,
			Board board, boolean redo) {
		Map<Location, List<Location>> possibleActions = getAllActions(myPieces, oppPieces, board);
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
		
		Integer current = encode(myPieces,oppPieces);
		
		if (training)
		{			
			qMap.put(current, (1 - learningRate) * qMap.get(current) + (learningRate) * (reward(taken, oppPieces, board) + discountRate * maxQ) );
		}
		
		return taken;
	}

	private double reward(Action taken, List<Location> oppPieces, Board board) {
		if (board.get(taken.dest).pieceType() == Pieces.FLAG)
			return 100;
		return 0;
	}

	private Integer encode(List<Location> myPieces, List<Location> oppPieces) {
		//first iterate through my pieces to see if i can attack something of theirs
		Set<String> attackers = new HashSet<String>()
		return null;
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
				//blargh	
			}
		}
		
		return toRet;
	}

	private Map<Location, List<Location>> getAllActions(List<Location> myPieces,
			List<Location> oppPieces, Board board) {
		Map<Location, List<Location>> toRet = new HashMap<Location, List<Location>>();
		for (Location loc : myPieces)
		{
			List<Location> destinations = board.occupyLocations(loc, player);
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
