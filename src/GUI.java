import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;


public class GUI extends JFrame {
	
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
				TileButton newButton = new TileButton(model.getTile(x, y));
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

}
