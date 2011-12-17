package stratego.learner.player;

import stratego.learner.board.Location;

public class Action {
	public Action(Location source, Location destination) {
		src = source;
		dest = destination;
	}
	public Location src;
	public Location dest;
	
	public int hashCode()
	{
		return src.hashCode() ^ dest.hashCode();
	}
	
	public boolean equals(Object obj)
	{
		if (! (obj instanceof Action))
			return false;
		Action other = (Action) obj;
		return src.equals(other.src) && dest.equals(other.dest);
	}
	
	public String toString()
	{
		return src+"->"+dest;
	}
}
