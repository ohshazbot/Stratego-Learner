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
}