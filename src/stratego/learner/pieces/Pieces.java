package stratego.learner.pieces;

public enum Pieces{
	MARSHALL(10), 
	GENERAL(9), 
	COLONEL(8), 
	MAJOR(7), 
	CAPTAIN(6), 
	LIEUTENANT(5), 
	SERGEANT(4), 
	MINER(3), 
	SCOUT(2), 
	SPY(1), 
	BOMB(11), 
	FLAG(0);
	
	int rank;
	private Pieces(int rankVal)
	{
		rank = rankVal;
	}
}