package stratego.learner.player;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Set;

import stratego.learner.board.Board;
import stratego.learner.board.GameState;
import stratego.learner.board.LocDist;
import stratego.learner.board.Location;
import stratego.learner.board.PlayerEnum;
import stratego.learner.pieces.Piece;
import stratego.learner.pieces.Piece.Result;
import stratego.learner.pieces.Pieces;

public class SPAMbot implements Player {
	PlayerEnum player;
	boolean training;
	Map<Integer, Double> qMap;
	double learningRate;
	double discountRate;

	public SPAMbot(boolean trainer, Map<Integer, Double> qMatrix, double lr, double dr) {
		training = trainer;
		qMap = qMatrix;
		learningRate = lr;
		discountRate = dr;
	}

	public SPAMbot(boolean trainer, String checkPointFile, double lr, double dr) throws NumberFormatException, IOException {
		training = trainer;
		qMap = parseFile(checkPointFile);
		learningRate = lr;
		discountRate = dr;
	}

	private Map<Integer, Double> parseFile(String checkPointFile) throws NumberFormatException, IOException {
		Map<Integer, Double> toRet = new HashMap<Integer, Double>();
		BufferedReader br;

		try {
			br = new BufferedReader(new FileReader(checkPointFile));
		} catch (FileNotFoundException e) {
			System.out.println("File not found, starting from scratch");
			return toRet;
		}

		String line;
		while ((line = br.readLine()) != null) {
			String[] pieces = line.split(",");
			Integer key = new Integer(Integer.parseInt(pieces[0]));
			Double value = Double.parseDouble(pieces[1]);
			toRet.put(key, value);
		}

		return toRet;
	}

	public Map<Integer, Double> getMap() {
		return qMap;
	}

	public boolean checkPointMap(String file) throws IOException {
		System.out.println("Checkpointing to file " + file);
		File f = new File(file);
		if (f.exists())
			return false;

		FileWriter fw = new FileWriter(f);
		for (Entry<Integer, Double> entry : qMap.entrySet()) {
			fw.write(entry.getKey().intValue() + "," + Double.toHexString(entry.getValue())+"\n");
		}
		fw.close();
		return true;
	}

	@Override
	public void setRedPlayer() {
		player = PlayerEnum.RED;
	}

	@Override
	public void setBluePlayer() {
		player = PlayerEnum.BLUE;
	}

	boolean spyLeft;
	boolean minesLeft;
	Piece bestLeft = null;

	@Override
	public Action getAction(List<Location> myPieces, List<Location> oppPieces, Board board, boolean redo) {
		spyLeft = false;
		minesLeft = false;
		for (Location l : oppPieces) {
			if (!board.get(l).revealed) {
				if (board.get(l).pieceType().equals(Pieces.SPY))
					spyLeft = true;

				else if (board.get(l).pieceType().equals(Pieces.BOMB))
					minesLeft = true;

				else if (!board.get(l).pieceType().equals(Pieces.FLAG) && (bestLeft == null || board.get(l).attack(bestLeft).attackerLives))
					bestLeft = board.get(l);
			}
		}
		GameState gs;
		if (player.equals(PlayerEnum.RED))
			gs = new GameState(board, myPieces, oppPieces);
		else
			gs = new GameState(board, oppPieces, myPieces);

		Map<Location, List<LocDist>> possibleActions = Util.getAllActions(gs, true, player);
		Map<Action, Set<Integer>> actionMap = getAllPossibleNextStates(possibleActions, gs);

		double maxQ = Double.NEGATIVE_INFINITY;
		Set<Action> tieActions = new HashSet<Action>();
		for (Entry<Action, Set<Integer>> e : actionMap.entrySet()) {
			for (Integer intVal : e.getValue()) {
				Double qVal = qMap.get(intVal);
				if (qVal == null)
					qVal = 0d;
				if (maxQ < qVal.doubleValue()) {
					maxQ = qVal;
					tieActions.clear();
					tieActions.add(e.getKey());
					// System.out.println("here");
				} else if (maxQ == qVal.doubleValue()) {
					tieActions.add(e.getKey());
					// System.out.println("there");
				}
				// System.out.println("anywhere");
			}
		}

		Random r = new Random();
		Action taken = (Action) tieActions.toArray()[r.nextInt(tieActions.size())];

		Integer current = encode(myPieces, oppPieces, board);

		if (training) {
			if (!qMap.keySet().contains(current))
				qMap.put(current, 0d);
			double newVal = (1 - learningRate) * qMap.get(current) + (learningRate) * (reward(taken, gs, actionMap) + discountRate * maxQ);
			qMap.put(current, newVal);
			encode(myPieces, oppPieces, board);
		}

		return taken;
	}

	private double reward(Action taken, GameState gs, Map<Action, Set<Integer>> actionMap) {
		if (gs.board.get(taken.dest) != null && gs.board.get(taken.dest).pieceType() == Pieces.FLAG)
			return 100;
		for (Entry<Action, Set<Integer>> entry : actionMap.entrySet()) {
			for (Integer state : entry.getValue()) {
				if (state.intValue() == 2)
					return 100;
				if (state.intValue() == 2 * 4096)
					return -100;
				if (state.intValue() % 2 == 1)
					return -100;
			}
		}
		return 0;
	}

	private Integer encode(List<Location> myPieces, List<Location> oppPieces, Board board) {
		// first iterate through my pieces to see if i can attack something of
		// theirs
		Map<String, Set<Piece>> attackers = new HashMap<String, Set<Piece>>();
		attackers.put(player.name(), new HashSet<Piece>());
		attackers.put(player.opposite().name(), new HashSet<Piece>());
		boolean mobilePieces = false;
		for (Location loc : myPieces) {
			for (LocDist ld : board.occupyLocations(loc, player, false)) {
				mobilePieces = true;
				Location newloc = ld.loc;
				Piece p = board.get(newloc.xcord, newloc.ycord);
				if (p != null && !p.isWater()) {
					attackers.get(player.name()).add(board.get(loc.xcord, loc.ycord));
					attackers.get(player.opposite().name()).add(p);
				}
			}
		}
		// No mobile pieces, encoded state as just flag.
		if (!mobilePieces)
			return 2 * 4096;

		mobilePieces = false;
		// then iterate through theirs to see if they can hit mine, adding to
		// set all along
		for (Location loc : oppPieces) {
			for (LocDist ld : board.occupyLocations(loc, player.opposite(), false)) {
				mobilePieces = true;
				Location newloc = ld.loc;
				Piece p = board.get(newloc.xcord, newloc.ycord);
				if (p != null && !p.isWater()) {
					attackers.get(player.opposite().name()).add(board.get(loc.xcord, loc.ycord));
					attackers.get(player.name()).add(p);
				}
			}
		}

		if (!mobilePieces)
			return 2;

		int answer = 0;
		int mult;
		boolean mine;
		for (Entry<String, Set<Piece>> entry : attackers.entrySet()) {
			mine = entry.getKey().equals(player.name());
			mult = mine ? 1 : 4096;

			for (Piece p : entry.getValue()) {
				if (!mine && !p.revealed) {
					if (p.moved)
						answer |= mult * 4096;
					else
						answer |= mult * 8192;
				} else {
					switch (p.pieceType()) {
					case WATER:
						answer |= mult * 0;
						break;
					case FLAG:
						answer |= mult * 1;
						break;
					case GENERAL:
						answer |= mult * 2;
						break;
					case COLONEL:
						answer |= mult * 4;
						break;
					case MAJOR:
						answer |= mult * 8;
						break;
					case CAPTAIN:
						answer |= mult * 16;
						break;
					case LIEUTENANT:
						answer |= mult * 32;
						break;
					case SERGEANT:
						answer |= mult * 64;
						break;
					case MINER:
						answer |= mult * 128;
						break;
					case SCOUT:
						answer |= mult * 256;
						break;
					case SPY:
						answer |= mult * 512;
						break;
					case BOMB:
						answer |= mult * 1024;
						break;
					case MARSHALL:
						answer |= mult * 2048;
						break;
					}
				}
			}
		}
		return new Integer(answer);
	}

	private Map<Action, Set<Integer>> getAllPossibleNextStates(Map<Location, List<LocDist>> possibleActions, GameState gs) {
		Map<Action, Set<Integer>> toRet = new HashMap<Action, Set<Integer>>();

		for (Entry<Location, List<LocDist>> e : possibleActions.entrySet()) {
			Location src = e.getKey();
			for (LocDist dest : e.getValue()) {
				Action act = new Action(src, dest.loc);
				Set<Integer> states = new HashSet<Integer>();

				GameState gsPotential;
				gsPotential = winningBoard(gs, src, dest.loc, dest.dist);
				Map<Location, List<LocDist>> results;
				if (gsPotential != null) {
					results = Util.getAllActions(gsPotential, false, player.opposite());
					if (results.size() == 0)
						states.add(2);
					else
						states.addAll(getAllStates(gsPotential, results));
				}
				gsPotential = losingBoard(gs, src, dest.loc, dest.dist);
				if (gsPotential != null) {
					results = Util.getAllActions(gsPotential, false, player.opposite());
					if (results.size() == 0)
						states.add(2);
					else
						states.addAll(getAllStates(gsPotential, results));
				}

				gsPotential = tieingBoard(gs, src, dest.loc, dest.dist);
				if (gsPotential != null) {
					results = Util.getAllActions(gsPotential, false, player.opposite());
					if (results.size() == 0)
						states.add(2);
					else
						states.addAll(getAllStates(gsPotential, results));
				}
				toRet.put(act, states);
			}
		}

		return toRet;
	}

	private Set<Integer> getAllStates(GameState gs, Map<Location, List<LocDist>> actions) {
		Set<Integer> toRet = new HashSet<Integer>();
		for (Entry<Location, List<LocDist>> e : actions.entrySet()) {
			Location src = e.getKey();
			for (LocDist dest : e.getValue()) {
				Location l = dest.loc;
				int dist = dest.dist;

				GameState futureGS = new GameState(gs);
				if (futureGS.board.get(l) == null) {
					futureGS.replace(src, l);
				} else {
					Piece s = futureGS.board.get(src);
					Piece opp = futureGS.board.get(l);

					// If we don't know what it is, we can say it's a scout if
					// it's moving more than 1
					boolean revealing = false;
					if (!s.revealed && dist > 1) {
						s = Piece.makePiece(Pieces.SCOUT, s.owner());
						s.reveal();
						revealing = true;
					}
					if (s.revealed) {
						Result res = s.attack(opp);
						if (res.attackerLives)
							futureGS.replace(src, l);
						else if (!res.defenderLives) {
							futureGS.remove(l);
							futureGS.remove(src);
						} else {
							futureGS.remove(src);
						}
					} else {
						// 3 possibilities- win, lose, or draw
						// win can only happen if their best could beat that
						// unit (or a spy is on the loose)
						Result best = bestLeft.attack(opp);
						if ((best.attackerLives && !best.defenderLives) || (opp.pieceType().equals(Pieces.MARSHALL) && spyLeft)) {
							futureGS.replace(src, l);
							if (player.equals(PlayerEnum.RED))
								toRet.add(encode(gs.redPieces, gs.bluPieces, gs.board));
							else
								toRet.add(encode(gs.bluPieces, gs.redPieces, gs.board));
							futureGS = new GameState(gs);
						}
						// draw - if their best can't even tie, then lets not
						// waste time evaluating
						if (!best.defenderLives) {
							futureGS.remove(l);
							futureGS.remove(src);
							if (player.equals(PlayerEnum.RED))
								toRet.add(encode(gs.redPieces, gs.bluPieces, gs.board));
							else
								toRet.add(encode(gs.bluPieces, gs.redPieces, gs.board));
							futureGS = new GameState(gs);
						}

						// lose - a defeat is pretty much always a possibilty
						futureGS.remove(src);
						if (player.equals(PlayerEnum.RED))
							toRet.add(encode(gs.redPieces, gs.bluPieces, gs.board));
						else
							toRet.add(encode(gs.bluPieces, gs.redPieces, gs.board));
					}
					if (revealing)
						s.revealed = false;
				}

				if (player.equals(PlayerEnum.RED))
					toRet.add(encode(gs.redPieces, gs.bluPieces, gs.board));
				else
					toRet.add(encode(gs.bluPieces, gs.redPieces, gs.board));
			}
		}
		return toRet;
	}

	private GameState tieingBoard(GameState gs, Location src, Location dest, int dist) {
		Piece opp = gs.board.get(dest);
		if (opp == null) {
			GameState toRet = new GameState(gs);
			toRet.replace(src, dest);
			return toRet;
		}
		if (opp.revealed) {
			Result res = gs.board.get(src).attack(opp);
			if (!res.attackerLives && !res.defenderLives) {
				GameState toRet = new GameState(gs);
				toRet.remove(src);
				toRet.remove(dest);
				return toRet;
			}
			return null;
		}
		Result testBest = gs.board.get(src).attack(bestLeft);
		// If this piece could survive their best piece then it can't be a
		// tieing board
		if (testBest.attackerLives && !testBest.defenderLives)
			return null;
		GameState toRet = new GameState(gs);
		toRet.remove(src);
		toRet.remove(dest);
		return toRet;
	}

	private GameState losingBoard(GameState gs, Location src, Location dest, int dist) {
		Piece opp = gs.board.get(dest);
		Piece mine = gs.board.get(src);
		if (opp == null) {
			GameState toRet = new GameState(gs);
			toRet.replace(src, dest);
			return toRet;
		}
		if (opp.revealed) {
			Result res = mine.attack(opp);
			if (!res.attackerLives && res.defenderLives) {
				GameState toRet = new GameState(gs);
				toRet.remove(src);
				return toRet;
			}
			return null;
		}
		Result testBest = mine.attack(bestLeft);
		// If the defender is guaranteed to die (and no bombs unless Miner),
		// then our piece can't lose the combat
		if ((!minesLeft || mine.equals(Pieces.MINER) || opp.moved) && !testBest.defenderLives)
			return null;
		GameState toRet = new GameState(gs);
		toRet.remove(src);
		return toRet;
	}

	private GameState winningBoard(GameState gs, Location src, Location dest, int dist) {
		Piece opp = gs.board.get(dest);
		if (opp == null) {
			GameState toRet = new GameState(gs);
			toRet.replace(src, dest);
			return toRet;
		}
		if (!opp.revealed) {
			GameState toRet = new GameState(gs);
			toRet.replace(src, dest);
			return toRet;
		}
		Piece mine = gs.board.get(src);
		Result res = mine.attack(opp);
		if (res.attackerLives && !res.defenderLives) {
			GameState toRet = new GameState(gs);
			toRet.replace(src, dest);
			return toRet;
		}
		return null;

	}

	@Override
	public void wins() {
//		System.out.println("You win");
	}

	@Override
	public void loses() {
//		System.out.println("You lose");
	}

}
