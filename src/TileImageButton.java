import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JOptionPane;


@SuppressWarnings("serial")
public class TileImageButton extends JButton {
	
	private Tile tile;
	
	private int x;
	
	private int y;
	
	// FIXME Should return BufferedImage
	private BufferedImage imageForTile(Tile tile) throws Exception {
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
		System.out.println(qs);
		System.out.println(rotations + " rotations");
		System.out.println(needsFlip ? "Has flip" : "No flip");
		
		BufferedImage img = ImageIO.read(new File(imageFilename(qs)));
		if (needsFlip) {
			img = flipImage(img);
		}
		for (int i = 0; i < rotations; i++) {
			img = rotateImage90CounterClockwise(img);
		}
		
		// TODO Recolor the image
		
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
	
	public static void main(String[] args) {
		Tile tile = Tile.randomTile();
		System.out.println(tile);
		TileImageButton tb = new TileImageButton();
		try {
			ImageIcon icon = new ImageIcon();
			icon.setImage(tb.imageForTile(tile));
			JOptionPane.showMessageDialog(null, icon);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

}
