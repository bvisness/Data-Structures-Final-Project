import java.awt.BorderLayout;
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
import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;


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
	
	private JTextPane messagePane;
	
	private JPanel redTurnIndicator;
	
	private JPanel blueTurnIndicator;
	
	private JLabel redScoreLabel;
	
	private JLabel blueScoreLabel;
	
	private JPanel optionsFields;
	
	private JButton optionsButton;
	
	private JComboBox<String> boardSizeSelector;
	
	private GUI() {		
		boardPanel = new JPanel(new GridLayout(1,1));
		boardPanel.setPreferredSize(new Dimension(BOARD_WIDTH, BOARD_WIDTH));
		
		JPanel centerPanel = new JPanel(new GridLayout(2,1));
		
		// Build the new tile panel
		JPanel tilePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		nextTileButton = new TileButton();
		nextTileButton.setEnabled(false);
		rotateLeftButton = new JButton("RL");
		rotateLeftButton.addActionListener(this);
		rotateRightButton = new JButton("RR");
		rotateRightButton.addActionListener(this);
		tilePanel.add(rotateLeftButton);
		tilePanel.add(nextTileButton);
		tilePanel.add(rotateRightButton);
		centerPanel.add(tilePanel);
		
		// Message box
		messagePane = new JTextPane();
		messagePane.setOpaque(false);
		centerPanel.add(messagePane);
		
		// Build the score panel
		JPanel scorePanel = new JPanel();
		JPanel scoreMarkersPanel = new JPanel(new GridLayout(1,2));
		redTurnIndicator = new JPanel();
		redTurnIndicator.setOpaque(true);
		redTurnIndicator.setBackground(Color.RED);
		blueTurnIndicator = new JPanel();
		blueTurnIndicator.setOpaque(true);
		blueTurnIndicator.setBackground(Color.BLUE);
		scoreMarkersPanel.add(redTurnIndicator);
		scoreMarkersPanel.add(blueTurnIndicator);
		JPanel scoresPanel = new JPanel(new GridLayout(1,2));
		scoresPanel.setPreferredSize(new Dimension(INPUT_WIDTH, INPUT_WIDTH / 2));
		redScoreLabel = new ScoreLabel("0");
		redScoreLabel.setBackground(Color.RED);
		blueScoreLabel = new ScoreLabel("0");
		blueScoreLabel.setBackground(Color.BLUE);
		scoresPanel.add(redScoreLabel);
		scoresPanel.add(blueScoreLabel);
		scorePanel.setLayout(new BoxLayout(scorePanel, BoxLayout.Y_AXIS));
		scorePanel.add(scoreMarkersPanel);
		scorePanel.add(scoresPanel);
		
		// Build the options panel
		JPanel optionsPanel = new JPanel();
		optionsPanel.setLayout(new BoxLayout(optionsPanel, BoxLayout.Y_AXIS));
		optionsFields = new JPanel(new GridLayout(1,1));
		boardSizeSelector = new JComboBox<String>();
		boardSizeSelector.addItem("3 x 3");
		boardSizeSelector.addItem("5 x 5");
		boardSizeSelector.addItem("9 x 9");
		boardSizeSelector.addItem("15 x 15");
		boardSizeSelector.setSelectedIndex(1);
		boardSizeSelector.addActionListener(this);
		optionsFields.add(boardSizeSelector);
		optionsFields.setVisible(false);
		JPanel optionsButtonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		optionsButton = new JButton("*");
		optionsButton.addActionListener(this);
		optionsButtonPanel.add(optionsButton);
		optionsPanel.add(optionsFields);
		optionsPanel.add(optionsButtonPanel);
		
		inputPanel = new JPanel();
		inputPanel.setLayout(new BorderLayout());
		inputPanel.add(optionsPanel, BorderLayout.NORTH);
		inputPanel.add(centerPanel, BorderLayout.CENTER);
		inputPanel.add(scorePanel, BorderLayout.SOUTH);
		inputPanel.setPreferredSize(new Dimension(INPUT_WIDTH, BOARD_WIDTH));
		
		this.getContentPane().setLayout(new BoxLayout(this.getContentPane(), BoxLayout.X_AXIS));
		this.getContentPane().add(boardPanel);
		this.getContentPane().add(inputPanel);
		this.setPreferredSize(new Dimension(BOARD_WIDTH + INPUT_WIDTH, BOARD_WIDTH));
		this.setResizable(false);
		
		newGame(5);
		update();
		
		pack();
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setVisible(true);
	}
	
	private void newGame(int boardSize) {
		this.boardSize = boardSize;
		model = new Model(boardSize);
		boardPanel.removeAll();
		boardPanel.add(boardPanelWithSize(boardSize));
		nextTile = model.randomLegalTile();
		nextTileButton.setTile(nextTile);
		update();
		revalidate();
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
	
	private void update() {
		// Update buttons
		Component[] comps = ((JPanel)boardPanel.getComponents()[0]).getComponents();
		for (int i = 0; i < comps.length; i++) {
			if (comps[i] instanceof TileButton) {
				((TileButton)comps[i]).update();
			}
		}
		
		// Update turn indicator
		switch (model.getTurn()) {
		case RED:
			redTurnIndicator.setVisible(true);
			blueTurnIndicator.setVisible(false);
			break;
		case BLUE:
			redTurnIndicator.setVisible(false);
			blueTurnIndicator.setVisible(true);
			break;
		}
		
		// Update scores
		redScoreLabel.setText(model.getRedScore() + "");
		blueScoreLabel.setText(model.getBlueScore() + "");
	}
	
	private void setMessage(String msg) {
		SimpleAttributeSet font = new SimpleAttributeSet();
		StyleConstants.setBold(font, true);
		SimpleAttributeSet center = new SimpleAttributeSet();
		StyleConstants.setAlignment(center, StyleConstants.ALIGN_CENTER);
		
		StyledDocument doc = messagePane.getStyledDocument();
		try {
			doc.remove(0, doc.getLength());
			doc.insertString(0, msg, font);
			doc.setParagraphAttributes(0, doc.getLength(), center, false);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String actionMsg = "";
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
			} else {
				actionMsg = "You cannot place this tile there.";
			}
			update();
		} else if (e.getSource() == boardSizeSelector) {
			String newValue = (String)boardSizeSelector.getSelectedItem();
			int newSize = Integer.parseInt(newValue.split(" ")[0]);
			if (newSize != boardSize) {
				newGame(newSize);
			}
		} else if (e.getSource() == optionsButton) {
			optionsFields.setVisible(!optionsFields.isVisible());
		}
		setMessage(actionMsg);
	}
	
	public static void main(String[] args) {
		@SuppressWarnings("unused")
		GUI gui = new GUI();
	}

}
