import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JOptionPane;


@SuppressWarnings("serial")
public class TileImageButton extends JButton {
	
	private static final int ROAD_RGB = Color.CYAN.getRGB();
	
	private static final int CITY_RGB = Color.MAGENTA.getRGB();	
	
	private Tile tile;
	
	private int x;
	
	private int y;
	
	private final int width;
	
	private final int height;
	
	public TileImageButton() {
		this(-1, -1, 100, 100, null);
	}
	
	public TileImageButton(int x, int y, int width, int height, Tile tile) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		
		this.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
		setTile(tile);
	}
	
	public void setTile(Tile tile) {
		this.tile = tile;
		update();
	}
	
	public void update() {
		try {
			ImageIcon icon = new ImageIcon(imageForTile(tile));
			this.setIcon(icon);
			this.setDisabledIcon(icon);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		revalidate();
		repaint();
	}
	
	public int getGameX() {
		return x;
	}

	public int getGameY() {
		return y;
	}
	
	private BufferedImage imageForTile(Tile tile) throws Exception {
		if (tile == null) {
			BufferedImage img = ImageIO.read(new File("images/null.png"));
			return resizeImage(img, this.width, this.height);
		}
		
		String qs = quadrantString(tile);
		boolean needsFlip = false;
		int rotations = 0;
		do {
			boolean foundMatch = false;
			for (int i = 0; i < 4; i++) {
				rotations = i;
				if (imageExists(qs)) {
					foundMatch = true;
					break;
				} else {
					qs = rotateStringRight(qs);
				}
			}
			if (foundMatch) {
				break;
			}
			
			qs = flipString(qs);
			needsFlip = true;
			for (int i = 0; i < 4; i++) {
				rotations = i;
				if (imageExists(qs)) {
					foundMatch = true;
					break;
				} else {
					qs = rotateStringRight(qs);
				}
			}
			if (foundMatch) {
				break;
			}
			
			throw new Exception("No image found for tile " + tile);
		} while (false);
		
		BufferedImage img = ImageIO.read(new File(imageFilename(qs)));
		for (int i = 0; i < rotations; i++) {
			img = rotateImage90CounterClockwise(img);
		}
		if (needsFlip) {
			img = flipImage(img);
		}
		
		// Recolor the image
		if (tile.hasQuadrantType(QuadrantType.ROAD)) {
			Owner roadOwner = tile.getQuadrantTypeOwner(QuadrantType.ROAD);
			replaceColor(img, ROAD_RGB, getOwnerRGB(roadOwner));
		}
		if (tile.hasQuadrantType(QuadrantType.CITY)) {
			Owner cityOwner = tile.getQuadrantTypeOwner(QuadrantType.CITY);
			replaceColor(img, CITY_RGB, getOwnerRGB(cityOwner));
		}
		
		// Resize the image
		img = resizeImage(img, this.width, this.height);
		
		return img;
	}
	
	private static String quadrantString(Tile tile) {
		String result = "";
		for (int side = 0; side < 4; side++) {
			QuadrantType type = tile.getQuadrant(side).getType();
			switch (type) {
			case GRASS:
				result += "G";
				break;
			case ROAD:
				result += "R";
				break;
			case CITY:
				result += "C";
				break;
			}
		}
		return result;
	}
	
	private static String rotateStringRight(String string) {
		return string.charAt(string.length() - 1) + string.substring(0, string.length() - 1);
	}
	
	private static String rotateStringLeft(String string) {
		return string.substring(1) + string.charAt(0);
	}
	
	private static String flipString(String string) {
		return "" + string.charAt(0) + string.charAt(3) + string.charAt(2) + string.charAt(1);
	}
	
	private static String imageFilename(String imageName) {
		return "images/" + imageName + ".png";
	}
	
	private static boolean imageExists(String name) {
		File f = new File(imageFilename(name));
		return f.exists();
	}
	
	private static BufferedImage rotateImage90Clockwise(BufferedImage img) {
		AffineTransform tx = new AffineTransform();
	    tx.rotate(Math.PI / 2, img.getWidth() / 2, img.getHeight() / 2);

	    AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
	    return op.filter(img, null);
	}
	
	private static BufferedImage rotateImage90CounterClockwise(BufferedImage img) {
		AffineTransform tx = new AffineTransform();
	    tx.rotate(3 * Math.PI / 2, img.getWidth() / 2, img.getHeight() / 2);

	    AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
	    return op.filter(img, null);
	}
	
	private static BufferedImage flipImage(BufferedImage img) {
		AffineTransform tx = new AffineTransform();
		tx.scale(-1, 1);
		tx.translate(-img.getWidth(null), 0);
		
		AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
	    return op.filter(img, null);
	}
	
	private static BufferedImage resizeImage(BufferedImage img, int width, int height) {
		BufferedImage newImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

		Graphics g = newImage.createGraphics();
		g.drawImage(img, 0, 0, width, height, null);
		g.dispose();
		
		return newImage;
	}
	
	public static void replaceColor(BufferedImage img, int originalRGB, int newRGB) {
		for (int y = 0; y < img.getHeight(); y++) {
			for (int x = 0; x < img.getWidth(); x++) {
				int pxColor = img.getRGB(x, y);
				if (pxColor == originalRGB) {
					img.setRGB(x, y, newRGB);
				}
			}
		}
	}
	
	private int getOwnerRGB(Owner owner) {
		if (owner == Owner.NONE) {
			return Color.LIGHT_GRAY.getRGB();
		} else if (owner == Owner.RED) {
			return Color.RED.getRGB();
		} else if (owner == Owner.BLUE) {
			return Color.BLUE.getRGB();
		} else {
			return -1;
		}
	}

}
