package stratego.learner.pieces;

public enum Pieces{
	MARSHALL('1'), 
	GENERAL('2'), 
	COLONEL('3'), 
	MAJOR('4'), 
	CAPTAIN('5'), 
	LIEUTENANT('6'), 
	SERGEANT('7'), 
	MINER('8'), 
	SCOUT('9'), 
	SPY('S'), 
	BOMB('B'), 
	FLAG('F'),
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