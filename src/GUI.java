import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;


public class GUI extends JFrame implements ActionListener {
	
	/**
	 * Meh, warnings
	 */
	private static final long serialVersionUID = 1L;
	
	private static final int BOARD_WIDTH = 750;
	
	private static final int INPUT_WIDTH = 250;
	
	private Model model;
	
	private Tile nextTile;
	
	private int boardSize;
	
	private JPanel boardPanel;
	
	private JPanel inputPanel;
	
	private TileButton nextTileButton;
	
	private JButton rotateRightButton;
	
	private JButton rotateLeftButton;
	
	private JLabel redScoreLabel;
	
	private JLabel blueScoreLabel;
	
	private JComboBox<String> boardSizeSelector;
	
	private GUI() {
		boardSize = 3;
		
		model = new Model(boardSize);
		
		boardPanel = new JPanel(new GridLayout(1,1));
		boardPanel.add(boardPanelWithSize(boardSize));
		boardPanel.setPreferredSize(new Dimension(BOARD_WIDTH, BOARD_WIDTH));
		
		inputPanel = new JPanel();
		inputPanel.setLayout(new FlowLayout());
		inputPanel.setPreferredSize(new Dimension(INPUT_WIDTH, BOARD_WIDTH));
		
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
		
		boardSizeSelector = new JComboBox<String>();
		boardSizeSelector.addItem("3 x 3");
		boardSizeSelector.addItem("5 x 5");
		boardSizeSelector.addItem("9 x 9");
		boardSizeSelector.addItem("15 x 15");
		boardSizeSelector.addActionListener(this);
		inputPanel.add(boardSizeSelector);
		
		this.getContentPane().setLayout(new BoxLayout(this.getContentPane(), BoxLayout.X_AXIS));
		this.getContentPane().add(boardPanel);
		this.getContentPane().add(inputPanel);
		this.setPreferredSize(new Dimension(BOARD_WIDTH + INPUT_WIDTH,BOARD_WIDTH));
		
		pack();
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setVisible(true);
	}
	
	private JPanel boardPanelWithSize(int boardSize) {
		JPanel newPanel = new JPanel(new GridLayout(boardSize, boardSize));
		
		for (int y = 0; y < boardSize; y++) {
			for (int x = 0; x < boardSize; x++) {
				TileButton newButton = new TileButton(x, y, model.getTile(x, y));
				newButton.addActionListener(this);
				newPanel.add(newButton);
			}
		}
		
		return newPanel;
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
				
				nextTile = model.randomLegalTile();
				nextTileButton.setTile(nextTile);
			}
			
			update();
		} else if (e.getSource() == boardSizeSelector) {
			String newValue = (String)boardSizeSelector.getSelectedItem();
			int newSize = Integer.parseInt(newValue.split(" ")[0]);
			if (newSize != boardSize) {
				boardSize = newSize;
				model = new Model(newSize);
				boardPanel.removeAll();
				boardPanel.add(boardPanelWithSize(newSize));
				revalidate();
				repaint();
				update();
			}
		}
		
	}

}
