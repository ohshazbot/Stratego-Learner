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

public class SPAMbot implements Player {
	public class BAW {
		public byte[] bytes;

		public BAW(byte[] myBytes)
		{
			bytes = myBytes;
		}

		public boolean equals(Object other)
		{
			if (other instanceof BAW)
			{
				return Arrays.equals(bytes, ((BAW) other).bytes);
			}
			return false;
		}

		public int hashCode()
		{
			return Arrays.hashCode(bytes);
		}
	}
	PlayerEnum player;
	boolean training;
	Map<BAW, Double> qMap;

	public SPAMbot(boolean trainer, Map<BAW, Double> qMatrix)
	{
		training = trainer;
		qMap = qMatrix;
	}

	public SPAMbot(boolean trainer, String checkPointFile) throws Base64DecodingException, IOException
	{
		training = trainer;
		qMap = parseFile(checkPointFile);
	}

	private Map<BAW, Double> parseFile(String checkPointFile) throws IOException, Base64DecodingException {
		Map<BAW, Double> toRet = new HashMap<BAW, Double>();
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
			BAW key = new BAW(Base64.decode(pieces[0]));
			Double value = Double.parseDouble(pieces[1]);
			toRet.put(key, value);
		}

		return toRet;
	}

	public Map<BAW, Double> getMap()
	{
		return qMap;
	}

	public boolean checkPointMap(String file) throws IOException
	{
		File f = new File(file);
		if (f.exists())
			return false;

		FileWriter fw = new FileWriter(f);
		for (Entry<BAW, Double> entry : qMap.entrySet())
		{
			fw.write(Base64.encode(entry.getKey().bytes)+","+Double.toHexString(entry.getValue()));
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

		if (!training)
		{
			Map<Location, List<Location>> possibleActions = getAllActions(myPieces, oppPieces, board);
			Map<Action, List<BAW>> actionMap = getAllPossibleNextStates(possibleActions, myPieces, oppPieces, board);

			Double maxQ = Double.MIN_VALUE;
			Set<Action> tieActions = new HashSet<Action>();
			for (Entry<Action, List<BAW>> e : actionMap.entrySet())
			{
				for (BAW baw : e.getValue())
				{
					Double bawVal = qMap.get(baw); 
					if (maxQ < bawVal)
					{
						maxQ = bawVal;
						tieActions.clear();
						tieActions.add(e.getKey());
					} else if (maxQ == bawVal)
					{
						tieActions.add(e.getKey());
					}
				}
			}

			Random r = new Random();
			return (Action) tieActions.toArray()[r.nextInt(tieActions.size())];
		}
	}

	private Map<Action, List<BAW>> getAllPossibleNextStates(
			Map<Location, List<Location>> possibleActions,
			List<Location> myPieces, List<Location> oppPieces, Board board) {
		Map<Action, List<BAW>> toRet = new HashMap<Action, List<BAW>>();
		
		for (Entry<Location, List<Location>> e : possibleActions.entrySet())
		{
			Location src = e.getKey();
			for (Location dest : e.getValue())
			{
			blargh	
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
