import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;


public class GUI extends JFrame implements ActionListener {
	
	/**
	 * Meh, warnings
	 */
	private static final long serialVersionUID = 1L;
	
	private Model model;
	
	private Tile nextTile;
	
	private static final int BOARD_SIZE = 5;
	
	private JPanel boardPanel;
	
	private JPanel inputPanel;
	
	private TileButton nextTileButton;
	
	private JButton rotateRightButton;
	
	private JButton rotateLeftButton;
	
	private JLabel redScoreLabel;
	
	private JLabel blueScoreLabel;
	
	private GUI() {
		model = new Model(BOARD_SIZE);
		
		boardPanel = new JPanel(new GridLayout(BOARD_SIZE, BOARD_SIZE));
		
		for (int y = 0; y < BOARD_SIZE; y++) {
			for (int x = 0; x < BOARD_SIZE; x++) {
				TileButton newButton = new TileButton(x, y, model.getTile(x, y));
				newButton.addActionListener(this);
				boardPanel.add(newButton);
			}
		}
		
		inputPanel = new JPanel();
		inputPanel.setLayout(new FlowLayout());
		
		nextTile = Tile.randomTile();
		nextTileButton = new TileButton(-1, -1, nextTile);
		nextTileButton.setEnabled(false);
		inputPanel.add(nextTileButton);
		
		rotateLeftButton = new JButton("Rotate Left");
		rotateLeftButton.addActionListener(this);
		rotateRightButton = new JButton("Rotate Right");
		rotateRightButton.addActionListener(this);
		inputPanel.add(rotateLeftButton);
		inputPanel.add(rotateRightButton);
		
		redScoreLabel = new JLabel("0");
		redScoreLabel.setForeground(Color.RED);
		blueScoreLabel = new JLabel("0");
		blueScoreLabel.setForeground(Color.BLUE);
		inputPanel.add(redScoreLabel);
		inputPanel.add(blueScoreLabel);
		
		this.getContentPane().setLayout(new BoxLayout(this.getContentPane(), BoxLayout.X_AXIS));
		this.getContentPane().add(boardPanel);
		this.getContentPane().add(inputPanel);
		this.setPreferredSize(new Dimension(800,400));
		
		pack();
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setVisible(true);
	}
	
	public static void main(String[] args) {
		@SuppressWarnings("unused")
		GUI gui = new GUI();
	}
	
	private void update() {
		// Update buttons
		Component[] comps = boardPanel.getComponents();
		for (int i = 0; i < comps.length; i++) {
			if (comps[i] instanceof TileButton) {
				((TileButton)comps[i]).update();
			}
		}
		
		// Update scores
		redScoreLabel.setText(model.getRedScore() + "");
		blueScoreLabel.setText(model.getBlueScore() + "");
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		
		if (e.getSource() == rotateLeftButton) {
			nextTile.rotateLeft();
			nextTileButton.update();
		} else if (e.getSource() == rotateRightButton) {
			nextTile.rotateRight();
			nextTileButton.update();
		} else if (e.getSource() instanceof TileButton) {
			TileButton theButton = (TileButton)e.getSource();
			if (model.isMoveValid(theButton.getGameX(), theButton.getGameY(), nextTile)) {
				model.placeTile(theButton.getGameX(), theButton.getGameY(), nextTile);
				theButton.setTile(nextTile);
				System.out.println("Tile added");
				
				nextTile = model.randomLegalTile();
				nextTileButton.setTile(nextTile);
			}
			
			update();
		}
		
	}

}
