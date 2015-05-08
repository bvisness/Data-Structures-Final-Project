import java.awt.BorderLayout;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;

import java.awt.Color;


public class TileButton extends JButton {
	
	/**
	 * Nope!
	 */
	private static final long serialVersionUID = 1L;
	
	private Tile tile;
	
	private int x;
	
	private int y;
	
	public void setTile(Tile tile) {
		this.tile = tile;
		update();
	}
	
	public TileButton(int x, int y, Tile tile) {
		this.x = x;
		this.y = y;
		
		this.setBackground(Color.MAGENTA);
		this.setLayout(new BorderLayout());
		this.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
		setTile(tile);
	}
	
	public void update() {
		removeAll();
		
		if (tile != null) {
			this.add(typePanel(Tile.NORTH), BorderLayout.NORTH);
			this.add(typePanel(Tile.EAST), BorderLayout.EAST);
			this.add(typePanel(Tile.SOUTH), BorderLayout.SOUTH);
			this.add(typePanel(Tile.WEST), BorderLayout.WEST);
			
			JPanel ownerPanel = new JPanel(new BorderLayout());
			ownerPanel.add(ownerPanel(Tile.NORTH), BorderLayout.NORTH);
			ownerPanel.add(ownerPanel(Tile.EAST), BorderLayout.EAST);
			ownerPanel.add(ownerPanel(Tile.SOUTH), BorderLayout.SOUTH);
			ownerPanel.add(ownerPanel(Tile.WEST), BorderLayout.WEST);
			this.add(ownerPanel, BorderLayout.CENTER);
		}
		
		validate();
		repaint();
	}
	
	public int getGameX() {
		return x;
	}

	public int getGameY() {
		return y;
	}

	private Color getTypeColor(Quadrant quadrant) {
		switch (quadrant.getType()) {
		case GRASS:
			return Color.GREEN;
		case ROAD:
			return Color.WHITE;
		case CITY:
			return Color.GRAY;
		default:
			return Color.MAGENTA;
		}
	}
	
	private Color getOwnerColor(Quadrant quadrant) {
		switch (quadrant.getOwner()) {
		case NONE:
			return Color.LIGHT_GRAY;
		case RED:
			return Color.RED;
		case BLUE:
			return Color.BLUE;
		default:
			return Color.ORANGE;
		}
	}
	
	private JPanel typePanel(int side) {
		JPanel panel = new JPanel();
		panel.setBackground(getTypeColor(tile.getQuadrants()[side]));
		return panel;
	}
	
	private JPanel ownerPanel(int side) {
		JPanel panel = new JPanel();
		panel.setBackground(getOwnerColor(tile.getQuadrants()[side]));
		return panel;
	}

}
