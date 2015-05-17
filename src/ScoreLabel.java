import java.awt.Font;

import javax.swing.JLabel;

/**
 * Defines a label for displaying player scores in Carcassonne.
 * 
 * @author Ben Visness
 * 
 */
@SuppressWarnings("serial")
public class ScoreLabel extends JLabel {

	/**
	 * Creates a new ScoreLabel with the given text.
	 * 
	 * @param text
	 *            The text for the label.
	 */
	public ScoreLabel(String text) {
		super(text, JLabel.CENTER);
		setOpaque(true);
		setFont(new Font(this.getFont().getName(), Font.BOLD, 48));
	}

}
