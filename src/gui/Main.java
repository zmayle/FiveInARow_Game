/* NetId(s): zrm7, daj88. Time spent: 12 hours, 00 minutes. */

package gui;

import controller.Controller;
import controller.DumbAI;
import controller.RandomAI;
import controller.SmartAI;

import model.Player;

public class Main {

	
	static Controller createController(Player p, String playerType) {
		switch(playerType) {
		case "DumbAI":
			return new DumbAI(p);
		case "RandomAI":
			return new RandomAI(p);
		case "SmartAI":
			return new SmartAI(p);
		default:
			throw new IllegalArgumentException("Must choose the players");
		}
	}
	

	
	public static void main(String[] args) {
		GameFrame gf= new GameFrame();
		gf.setVisible(true);
	}
}
