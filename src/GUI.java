import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
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

/**
 * Defines a GUI for a game of Carcassonne.
 * @author Ben Visness
 *
 */
public class GUI extends JFrame implements ActionListener {
	
	/**
	 * Meh, warnings
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * The visual width, in pixels, of the game board.
	 */
	private static final int BOARD_WIDTH = 750;
	
	/**
	 * The visual width, in pixels, of the input panel along the right.
	 */
	private static final int INPUT_WIDTH = 250;
	
	/**
	 * The Carcassonne model that this GUI uses.
	 */
	private Model model;
	
	/**
	 * The tile to place on the board next.
	 */
	private Tile nextTile;
	
	/**
	 * The number of spaces in one row or column of the board.
	 */
	private int boardSize;
	
	/**
	 * The panel containing the game board.
	 */
	private JPanel boardPanel;
	
	/**
	 * The panel containing all the game input stuff.
	 */
	private JPanel inputPanel;
	
	/**
	 * The button that displays the next tile to be placed.
	 */
	private TileImageButton nextTileButton;
	
	/**
	 * The button that rotates the next tile clockwise.
	 */
	private JButton rotateRightButton;
	
	/**
	 * The button that rotates the next tile counterclockwise.
	 */
	private JButton rotateLeftButton;
	
	/**
	 * The text pane that displays messages to the user.
	 */
	private JTextPane messagePane;
	
	/**
	 * The button that starts a new game.
	 */
	private JButton newGameButton;
	
	/**
	 * The panel that indicates that it is red's turn.
	 */
	private JPanel redTurnIndicator;
	
	/**
	 * The panel that indicates that it is blue's turn.
	 */
	private JPanel blueTurnIndicator;
	
	/**
	 * The label that display's red's score.
	 */
	private JLabel redScoreLabel;
	
	/**
	 * The label that display's blue's score.
	 */
	private JLabel blueScoreLabel;
	
	/**
	 * The panel that displays the options for the GUI.
	 */
	private JPanel optionsFields;
	
	/**
	 * The button that shows and hides the options panel.
	 */
	private JButton optionsButton;
	
	/**
	 * The combo box that lets users select different board sizes.
	 */
	private JComboBox<String> boardSizeSelector;
	
	/**
	 * Constructs a new GUI.
	 */
	private GUI() {		
		boardPanel = new JPanel(new GridLayout(1,1));
		boardPanel.setPreferredSize(new Dimension(BOARD_WIDTH, BOARD_WIDTH));
		
		JPanel centerPanel = new JPanel();
		centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));;
		
		// Build the new tile panel
		JPanel tilePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		nextTileButton = new TileImageButton();
		nextTileButton.setEnabled(false);
		rotateLeftButton = new JButton("RL");
		rotateLeftButton.addActionListener(this);
		rotateRightButton = new JButton("RR");
		rotateRightButton.addActionListener(this);
		tilePanel.add(rotateLeftButton);
		tilePanel.add(nextTileButton);
		tilePanel.add(rotateRightButton);
		tilePanel.setPreferredSize(new Dimension(INPUT_WIDTH, BOARD_WIDTH / 2));
		centerPanel.add(tilePanel);
		
		// Message box
		messagePane = new JTextPane();
		messagePane.setOpaque(false);
		messagePane.setEditable(false);
		centerPanel.add(messagePane);
		
		// New game button
		newGameButton = new JButton("New Game");
		newGameButton.setVisible(true);
		newGameButton.setPreferredSize(new Dimension(100000, 100)); // I don't know why but I need really big numbers to make this full-width
		newGameButton.setMinimumSize(new Dimension(100000, 100));   // BoxLayout is really dumb
		newGameButton.setMaximumSize(new Dimension(100000, 100));   // I actually need all three of these to do this
		newGameButton.addActionListener(this);
		centerPanel.add(newGameButton);
		
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
	
	/**
	 * Starts a new game with a given board size.
	 * 
	 * @param boardSize
	 *            The number of tiles in a given row or column of the new board.
	 */
	private void newGame(int boardSize) {
		this.boardSize = boardSize;
		model = new Model(boardSize);
		boardPanel.removeAll();
		boardPanel.add(boardPanelWithSize(boardSize));
		nextTile = model.randomLegalTile();
		nextTileButton.setTile(nextTile);
		setEnabledRecursive(this.getContentPane(), true);
		update();
		revalidate();
	}
	
	/**
	 * Creates a new board panel with buttons and stuff.
	 * 
	 * @param boardSize
	 *            The number of tiles in a given row or column of the new board.
	 * @return A JPanel to be displayed as the game board.
	 */
	private JPanel boardPanelWithSize(int boardSize) {
		JPanel newPanel = new JPanel(new GridLayout(boardSize, boardSize));
		int tileSize = BOARD_WIDTH / boardSize;
		
		for (int y = 0; y < boardSize; y++) {
			for (int x = 0; x < boardSize; x++) {
				TileImageButton newButton = new TileImageButton(x, y, tileSize, tileSize, model.getTile(x, y));
				newButton.addActionListener(this);
				newPanel.add(newButton);
			}
		}
		
		return newPanel;
	}
	
	/**
	 * Updates the state of UI components to reflect the game.
	 */
	private void update() {
		// Update buttons
		Component[] comps = ((JPanel)boardPanel.getComponents()[0]).getComponents();
		for (int i = 0; i < comps.length; i++) {
			if (comps[i] instanceof TileImageButton) {
				((TileImageButton)comps[i]).update();
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
	
	/**
	 * Sets the contents of the message pane.
	 * @param msg The message to display.
	 */
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
	
	/**
	 * Recursively enables or disables the contents of a container. For example,
	 * calling this method on this.getContentPane() will disable or enable every
	 * single component in the frame, not just those that are direct children.
	 * 
	 * @param container
	 *            The container to recursively enable/disable.
	 * @param enabled
	 *            Whether the components should be enabled.
	 */
	private void setEnabledRecursive(Container container, boolean enabled) {
		container.setEnabled(enabled);
		Component[] comps = container.getComponents();
		for (int i = 0; i < comps.length; i++) {
			comps[i].setEnabled(enabled);
			if (comps[i] instanceof Container) {
				setEnabledRecursive((Container)comps[i], enabled);
			}
		}
	}

	/**
	 * Performs actions, duh
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		String actionMsg = "";
		if (e.getSource() == rotateLeftButton) {
			nextTile.rotateLeft();
			nextTileButton.update();
		} else if (e.getSource() == rotateRightButton) {
			nextTile.rotateRight();
			nextTileButton.update();
		} else if (e.getSource() instanceof TileImageButton) {
			TileImageButton theButton = (TileImageButton)e.getSource();
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
		} else if (e.getSource() == newGameButton) {
			newGame(boardSize);
			newGameButton.setVisible(true);
		}
		
		if (model.isGameOver()) {
			setEnabledRecursive(boardPanel, false);
			setEnabledRecursive(inputPanel, false);
			messagePane.setEnabled(true);
			redScoreLabel.setEnabled(true);
			blueScoreLabel.setEnabled(true);
			newGameButton.setEnabled(true);
			try {
				String winString = "The game is over!";
				Model.Turn winner = model.getWinner();
				if (winner == Model.Turn.RED) {
					winString += " Red wins!";
				} else if (winner == Model.Turn.BLUE) {
					winString += " Blue wins!";
				} else if (winner == null){
					winString += " It's a draw!";
				}
				actionMsg = winString;
				newGameButton.setVisible(true);
			}
			catch (Exception ex) {
				actionMsg = "ERROR: " + ex.getMessage();
			}
		}
		
		setMessage(actionMsg);
	}
	
	/**
	 * The main method for this application.
	 * @param args
	 */
	public static void main(String[] args) {
		@SuppressWarnings("unused")
		GUI gui = new GUI();
	}

}
