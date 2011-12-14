package stratego.learner.board;

public class Location {
	public int xcord, ycord;
	
	public Location(){
		xcord = -1;
		ycord = -1;
	}
	public Location(int xcord2, int ycord2) {
		xcord = xcord2;
		ycord = ycord2;
	}

	public void set(Location l)
	{
		xcord = l.xcord;
		ycord = l.ycord;
	}
	
	public int hashCode()
	{
		return xcord*100+ycord;
	}
	
	public boolean equals(Location other)
	{
		return xcord==other.xcord && ycord==other.ycord;
	}
	public boolean isOrthogonal(Location compareTo, boolean limitDistance) {
		int xdiff = xcord - compareTo.xcord;
		int ydiff = ycord - compareTo.ycord;
		if (xdiff == 0)
		{
			if (ydiff > 1)
				return !limitDistance;
			return true;
		}
		if (ydiff == 0)
		{
			if (xdiff > 1)
				return !limitDistance;
			return true;
		}
		return false;
		
	}
	
	public String toString()
	{
		return "["+xcord+","+ycord+"]";
	}
	
	public Location addX(int i) {
		return new Location(xcord+i, ycord);
	}
	
	public Location addY(int i) {
		return new Location(xcord, ycord+i);
	}

}