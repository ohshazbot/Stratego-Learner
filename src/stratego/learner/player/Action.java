package stratego.learner.player;

import stratego.learner.board.Location;

public class Action {
	public Action(Location source, Location destination) {
		src = source;
		dest = destination;
	}
	public Location src;
	public Location dest;
}
