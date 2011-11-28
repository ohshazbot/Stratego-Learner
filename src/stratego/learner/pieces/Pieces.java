package stratego.learner.pieces;

public enum Pieces{
	MARSHALL('9'), 
	GENERAL('8'), 
	COLONEL('7'), 
	MAJOR('6'), 
	CAPTAIN('5'), 
	LIEUTENANT('4'), 
	SERGEANT('3'), 
	MINER('2'), 
	SCOUT('1'), 
	SPY('S'), 
	BOMB('B'), 
	FLAG('*'),
	WATER('W');
	
	char rank;
	private Pieces(char rankVal)
	{
		rank = rankVal;
	}
	
	public char pieceType()
	{
		return (char) rank;
	}

	public static Pieces valueOf(char ch) {
		for (Pieces p : Pieces.values())
			if (p.rank == ch)
				return p;
		return null;
	}
}