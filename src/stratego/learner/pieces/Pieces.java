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
	SPY('0'), 
	BOMB('B'), 
	FLAG('*');
	
	int rank;
	private Pieces(char rankVal)
	{
		rank = rankVal;
	}
}