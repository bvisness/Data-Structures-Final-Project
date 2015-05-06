import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JPanel;


public class GUI extends JFrame implements ActionListener {
	
	/**
	 * Meh, warnings
	 */
	private static final long serialVersionUID = 1L;
	
	private Model model;
	
	private GUI() {
		model = new Model(3);
		
		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new GridLayout(3,3));
		
		for (int y = 0; y < 3; y++) {
			for (int x = 0; x < 3; x++) {
				TileButton newButton = new TileButton(x, y, model.getTile(x, y));
				newButton.addActionListener(this);
				mainPanel.add(newButton);
			}
		}
		
		this.getContentPane().add(mainPanel);
		this.setPreferredSize(new Dimension(400,400));
		
		pack();
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setVisible(true);
	}
	
	public static void main(String[] args) {
		@SuppressWarnings("unused")
		GUI gui = new GUI();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		
		if (e.getSource() instanceof TileButton) {
			// TODO Clearly this should not be random
			TileButton theButton = (TileButton)e.getSource();
			Tile newTile = Tile.randomTile();
			System.out.println("Trying to add tile " + newTile);
			if (model.isMoveValid(theButton.getGameX(), theButton.getGameY(), newTile)) {
				model.placeTile(theButton.getGameX(), theButton.getGameY(), newTile);
				theButton.setTile(newTile);
				System.out.println("Tile added");
			}
			theButton.repaint();
		}
		
	}

}