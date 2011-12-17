package stratego.learner.board;

public class LocDist {
	public Location loc;
	public int dist;
	public LocDist(Location l, int distance)
	{
		loc = l;
		dist = distance;
	}
	
	public String toString()
	{
		return loc+"_"+dist;
	}
}