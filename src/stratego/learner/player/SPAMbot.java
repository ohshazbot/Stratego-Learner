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
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.sun.org.apache.xml.internal.security.exceptions.Base64DecodingException;
import com.sun.org.apache.xml.internal.security.utils.Base64;

import stratego.learner.board.Board;
import stratego.learner.board.Location;

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
	boolean redPlayer;
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
		redPlayer = true;
	}

	@Override
	public void setBluePlayer() {
		redPlayer = false;
	}

	@Override
	public Action getAction(List<Location> myPieces, List<Location> oppPieces,
			Board board, boolean redo) {
		
		ArrayList<Byte[]> newStates = new ArrayList<Byte[]>();
		HashMap<Action, Byte[]> moveQuality = new HashMap<Action, Byte[]>();
		
		/*try {
			String input = br.readLine();
			String[] moves = input.split(" ");
			String[] src = moves[0].split(",");
			Location source = new Location(Integer.parseInt(src[0]),Integer.parseInt(src[1]));

			String[] dest = moves[1].split(",");
			Location destination = new Location(Integer.parseInt(dest[0]),Integer.parseInt(dest[1]));
			
			Piece piece = board.get(source);
			if (piece == null || !myPieces.contains(source))
			{
				System.out.println("You selected in invalid piece of " + piece + " at " + source);
				piece = null;
			}
			else if (!source.isOrthogonal(destination, !piece.pieceType().equals(Pieces.SCOUT)))
			{
				System.out.println("You selected an invalid destination of " + destination + " from source " + source + " for piece " + piece);
				piece = null;
			}

			return new Action(source, destination);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		return null;
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
