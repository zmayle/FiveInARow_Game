package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import controller.Controller;
import model.Location;
import model.Board;
import model.Board.State;
import model.Game;
import model.GameListener;
import model.Line;
import model.NotImplementedException;
import model.Player;

class GameFrame extends JFrame implements GameListener {
	
	public JLabel title;
	public JPanel titlePanel;
	public Game game;
	public panel playerX, playerO;
	public JPanel start;
	
	private class panel extends JPanel {

		public JComboBox<String> dropDown;

		public panel(String title) {
			this.add(new JLabel(title));
			JComboBox<String> cbX= new JComboBox<String>();
			cbX.addItem("Human");
			cbX.addItem("DumbAI");
			cbX.addItem("RandomAI");
			cbX.addItem("SmartAI");
			this.add(cbX);
			this.dropDown= cbX;
		}
	}
	
	private class startListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			ArrayList<String> players= getPlayers();
			StartGame(players);
		}
		
	}
	
	private void StartGame(ArrayList<String> players) {
		// TODO Finish this!!!
		
		boolean humans= true;
		
		if (players.get(0) != "Human" && players.get(1) != "Human") humans= false;
		frameTransition(humans);
		
		if (players.get(0) != "Human") {
			Controller playerX = Main.createController(Player.X, players.get(0));
			game.addListener(playerX);
		}

		if (players.get(1) != "Human") {
			Controller playerO = Main.createController(Player.O, players.get(1));
			game.addListener(playerO);
		}
		
		game.addListener(this);
	}
	
	private void frameTransition(boolean humans) {
		//Component[] c= this.getComponents();
		//for (Component cm : c) {
		//	cm.remove();
		//}
		this.remove(playerO);
		this.remove(playerX);
		this.remove(start);
		this.pack();
		
		JPanel panel= new JPanel();
		BoxLayout boxlayout= new BoxLayout(panel, BoxLayout.Y_AXIS);
		panel.setLayout(boxlayout);

		JPanel panel1= new JPanel();
		title= new JLabel("WOOOOOOORD");
		panel1.add(title);
		this.titlePanel= panel1;
		panel.add(panel1);
		this.add(panel);
		this.pack();

		JPanel panel2= new JPanel();
		panel2.setLayout(new GridLayout(Board.NUM_ROWS,Board.NUM_COLS));
		for (int i = 0; i < Board.NUM_ROWS; i++)
			for (int j = 0; j < Board.NUM_COLS; j++) {
				Space s1= new Space(i, j);
				if (humans) s1.addMouseListener(new ClickListener(s1));
				this.game.addListener(s1);
				panel2.add(s1);
			}

		panel.add(panel2);
		//this.add(panel);
		this.setSize(new Dimension(450, 500));
		this.pack();
	}
	
	public GameFrame() {
		super("Five In A Row");
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		game= new Game();
		playerX= new panel("Player X");
		playerX.setSize(new Dimension(250,150));
		playerO= new panel("Player O");
		playerO.setSize(new Dimension(250,150));
		start= new JPanel();
		start.setSize(new Dimension(500,150));
		
		JButton sb= new JButton("START");
		sb.addActionListener(new startListener());
		start.add(sb);
		
		
		add(this.playerX, BorderLayout.WEST);
		add(this.playerO, BorderLayout.EAST);
		add(this.start, BorderLayout.SOUTH);
		
		this.setSize(new Dimension(500, 300));
		this.pack();
	}
	
	public class ClickListener implements MouseListener {
		public Space space;
		
		public ClickListener(Space sp) {
			this.space= sp;
		}
		
		@Override
		public void mouseClicked(MouseEvent e) {
			Location loc= new Location(space.i, space.j);
			game.submitMove(game.nextTurn(), loc);
		}

		@Override
		public void mousePressed(MouseEvent e) {
		}

		@Override
		public void mouseReleased(MouseEvent e) {
		}

		@Override
		public void mouseEntered(MouseEvent e) {
		}

		@Override
		public void mouseExited(MouseEvent e) {
		}
		
	}
	
	public class Space extends JPanel implements GameListener {
		private final int i, j;
		private boolean highlighted;
		
		public String spaceText;

		public Space(int i, int j) {
			this.i = i; this.j = j;
			this.spaceText= "";
			setPreferredSize(new Dimension(50, 50));
			this.highlighted= false;
		}
		
		public @Override void paint(Graphics g) {
			g.setColor(Color.BLACK);
			g.drawRect(0, 0, getWidth()-1, getHeight()-1);
			
			if (highlighted) {
				g.setColor(Color.CYAN);
				g.fillRect(0, 0, getWidth()-1, getHeight()-1);
				g.setColor(Color.BLACK);
			}
			g.drawString(spaceText, (getWidth()-1)/2, (getHeight()-1)/2);
		}

		@Override
		public void gameChanged(Game g) {
			Player p= game.getBoard().get(this.i, this.j);
			if (p == Player.X) {
				this.spaceText= "X";
			} else if (p == Player.O) {
				this.spaceText= "O";
			}
			if (game.getBoard().getWinner() != null) {
				Line win= game.getBoard().getWinner().line;
				if (win.contains(this.i, this.j)) this.highlight();
			}
			paintImmediately(0, 0, getWidth(), getHeight());
			repaint();
		}
		
		private void highlight() {
			this.highlighted= true;
		}
	}
	
	/** Returns an arrayList containing the strings describing the controllers
	 * selected for the game [Player X, Player O]. I.E. Human, DumbAI, 
	 * SmartAI, or RandomAI */
	public ArrayList<String> getPlayers() {
		ArrayList<String> players= new ArrayList<String>();
		String pX= (String) playerX.dropDown.getSelectedItem();
		String pO=  (String) playerO.dropDown.getSelectedItem();
		players.add(0, pX);
		players.add(1, pO);
		return players;
	}
	
	@Override
	public void gameChanged(Game g) {
		// TODO NEED TO BE ABLE TO REPAINT THE TITLE
		if (game.getBoard().getState() == State.NOT_OVER) {
			switch (game.nextTurn()) {
				case X:
					title.setText("It's PlayerX's turn");
					break;
				case O:
					title.setText("It's PlayerO's turn");
					break;
			}
		} else if (game.getBoard().getState() == State.HAS_WINNER) {
			switch (game.getBoard().getWinner().winner) {
				case X:
					title.setText("PlayerX won");
					break;
				case O:
					title.setText("PlayerO won");
					break;
			}
		} else title.setText("It's a Draw");
		this.titlePanel.paintImmediately(0, 0, getWidth(), getHeight());
	}
}