import java.awt.Font;

import javax.swing.JLabel;


@SuppressWarnings("serial")
public class ScoreLabel extends JLabel {
	
	public ScoreLabel(String text) {
		super(text, JLabel.CENTER);
		setOpaque(true);
		setFont(new Font(this.getFont().getName(), Font.BOLD, 48));
	}
	
}
