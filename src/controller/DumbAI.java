package controller;

import java.util.ArrayList;
import java.util.List;

import model.Board;
import model.Game;
import model.Location;
import model.NotImplementedException;
import model.Player;

/**
 * A DumbAI is a Controller that always chooses the blank space with the
 * smallest column number from the row with the smallest row number.
 */
public class DumbAI extends Controller {

	public DumbAI(Player me) {
		super(me);
		// TODO Auto-generated constructor stub
	}

	protected @Override Location nextMove(Game g) {
		// Note: Calling delay here will make the CLUI work a little more
		// nicely when competing different AIs against each other.
		
		// TODO Auto-generated method stub
		
		// get available locations
		List<Location> available = new ArrayList<Location>();
		for (Location loc : Board.LOCATIONS)
			if (g.getBoard().get(loc) == null) available.add(loc);

		// wait a bit
		delay();

		// pick the first 
		if (!available.isEmpty()) return available.get(0);
		
		// there are no available locations
		return null;
	}
}
