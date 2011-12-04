package stratego.learner.board;

public enum PlayerEnum {
	RED('r'), BLUE('b');

	char type;
	private PlayerEnum(char typeVal)
	{
		type = typeVal;
	}
	
	public char getType() {
		return type;
	}

	
	public PlayerEnum opposite() {
		if (this.equals(RED))
			return BLUE;
		else
			return RED;
	}
}
