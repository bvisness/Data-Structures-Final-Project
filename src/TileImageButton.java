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


@SuppressWarnings("serial")
public class TileImageButton extends JButton implements TileUpdateListener {
	
	/**
	 * A constant to define the RGB value of a road in the original images.
	 */
	private static final int ROAD_RGB = Color.CYAN.getRGB();
	
	/**
	 * A constant to define the RGB value of a city in the original images.
	 */
	private static final int CITY_RGB = Color.MAGENTA.getRGB();	
	
	/**
	 * The tile for this button to display.
	 */
	private Tile tile;
	
	/**
	 * Whether this tile needs to be visually refreshed.
	 */
	private boolean tileDidChange;
	
	/**
	 * The x-coordinate of this tile on the game board.
	 */
	private int x;
	
	/**
	 * The y-coordinate of this tile on the game board.
	 */
	private int y;
	
	/**
	 * The width of this tile, in pixels.
	 */
	private final int width;
	
	/**
	 * The height of this tile, in pixels.
	 */
	private final int height;
	
	/**
	 * Constructs a new TileImageButton with default values: x and y of -1,
	 * width and height of 100, and a null tile.
	 */
	public TileImageButton() {
		this(-1, -1, 100, 100, null);
	}
	
	/**
	 * Constructs a new TileImageButton.
	 * 
	 * @param x
	 *            The x-coordinate of this tile on the game board.
	 * @param y
	 *            The y-coordinate of this tile on the game board.
	 * @param width
	 *            The width of this tile, in pixels.
	 * @param height
	 *            The height of this tile, in pixels.
	 * @param tile
	 *            The tile for this button to display.
	 */
	public TileImageButton(int x, int y, int width, int height, Tile tile) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		
		this.setBorder(BorderFactory.createLineBorder(Color.GRAY));
		setTile(tile);
	}
	
	/**
	 * Sets the tile that this button will display. Will call update() to
	 * visually refresh the tile.
	 * 
	 * @param tile
	 *            The new tile to display.
	 * @see TileImageButton#update()
	 */
	public void setTile(Tile tile) {
		if (this.tile != null) {
			this.tile.removeUpdateListener(this);
		}
		
		this.tile = tile;
		if (this.tile != null) {
			this.tile.addUpdateListener(this);
		}
		this.tileDidChange = true;
		update();
	}
	
	/**
	 * Visually refreshes the button.
	 */
	public void update() {
		if (tileDidChange) {
			try {
				ImageIcon icon = new ImageIcon(imageForTile(tile));
				this.setIcon(icon);
				this.setDisabledIcon(icon);
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			revalidate();
			repaint();
			tileDidChange = false;
		}
	}
	
	/**
	 * Gets the tile's x-coordinate on the game board.
	 * 
	 * @return The tile's x-coordinate on the game board.
	 */
	public int getGameX() {
		return x;
	}

	/**
	 * Gets the tile's y-coordinate on the game board.
	 * 
	 * @return The tile's y-coordinate on the game board.
	 */
	public int getGameY() {
		return y;
	}
	
	/**
	 * Gets the image for a given tile.
	 * 
	 * @param tile
	 *            The tile to get an image for.
	 * @return A BufferedImage representing the given tile.
	 * @throws Exception
	 *             if no image can be found for the given tile.
	 */
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
	
	/**
	 * Gets a string representation of the quadrants for a tile.
	 * 
	 * @param tile
	 *            The tile to process.
	 * @return A string representation of the tile's quadrants, with one letter
	 *         corresponding to each quadrant.
	 */
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
	
	/**
	 * "Rotates" a quadrant string clockwise. In other words, it returns the
	 * quadrant string which would result from rotating the original tile 90
	 * degrees clockwise.
	 * 
	 * @param string
	 *            The string to rotate.
	 * @return A version of the quadrant string which has been "rotated" 90
	 *         degrees clockwise.
	 */
	private static String rotateStringRight(String string) {
		return string.charAt(string.length() - 1) + string.substring(0, string.length() - 1);
	}
	
	/**
	 * "Rotates" a quadrant string counterclockwise. In other words, it returns the
	 * quadrant string which would result from rotating the original tile 90
	 * degrees counterclockwise.
	 * 
	 * @param string
	 *            The string to rotate.
	 * @return A version of the quadrant string which has been "rotated" 90
	 *         degrees counterclockwise.
	 */
	@SuppressWarnings("unused")
	private static String rotateStringLeft(String string) {
		return string.substring(1) + string.charAt(0);
	}
	
	/**
	 * "Flips" a quadrant string horizontally. In other words, it returns the
	 * quadrant string which would result from horizontally flipping the
	 * original tile.
	 * 
	 * @param string
	 *            The string to flip.
	 * @return A version of the quadrant string which has been "flipped"
	 *         horizontally.
	 */
	private static String flipString(String string) {
		return "" + string.charAt(0) + string.charAt(3) + string.charAt(2) + string.charAt(1);
	}
	
	/**
	 * Gets the full path for an image with a given name.
	 * 
	 * @param imageName
	 *            The name of the image.
	 * @return The full path for the given image name.
	 */
	private static String imageFilename(String imageName) {
		return "images/" + imageName + ".png";
	}
	
	/**
	 * Checks whether a given image exists.
	 * 
	 * @param name
	 *            The name of the image to check.
	 * @return Whether the given image exists.
	 */
	private static boolean imageExists(String name) {
		File f = new File(imageFilename(name));
		return f.exists();
	}
	
	/**
	 * Rotates a BufferedImage 90 degrees clockwise.
	 * 
	 * @param img
	 *            The image to rotate.
	 * @returnA BufferedImage which is the given image rotated 90 degrees
	 *          clockwise.
	 */
	@SuppressWarnings("unused")
	private static BufferedImage rotateImage90Clockwise(BufferedImage img) {
		AffineTransform tx = new AffineTransform();
	    tx.rotate(Math.PI / 2, img.getWidth() / 2, img.getHeight() / 2);

	    AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
	    return op.filter(img, null);
	}
	
	/**
	 * Rotates a BufferedImage 90 degrees counterclockwise.
	 * 
	 * @param img
	 *            The image to rotate.
	 * @return A BufferedImage which is the given image rotated 90 degrees
	 *         counterclockwise.
	 */
	private static BufferedImage rotateImage90CounterClockwise(BufferedImage img) {
		AffineTransform tx = new AffineTransform();
	    tx.rotate(3 * Math.PI / 2, img.getWidth() / 2, img.getHeight() / 2);

	    AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
	    return op.filter(img, null);
	}
	
	/**
	 * Flips a BufferedImage horizontally.
	 * 
	 * @param img
	 *            The image to flip.
	 * @return A BufferedImage which is the given image flipped horizontally.
	 */
	private static BufferedImage flipImage(BufferedImage img) {
		AffineTransform tx = new AffineTransform();
		tx.scale(-1, 1);
		tx.translate(-img.getWidth(null), 0);
		
		AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
	    return op.filter(img, null);
	}
	
	/**
	 * Resizes a given BufferedImage to a given width and height.
	 * 
	 * @param img
	 *            The image to resize.
	 * @param width
	 *            The width of the returned image.
	 * @param height
	 *            The height of the returned image.
	 * @return A BufferedImage which is the given image resized to the given
	 *         width and height.
	 */
	private static BufferedImage resizeImage(BufferedImage img, int width, int height) {
		BufferedImage newImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

		Graphics g = newImage.createGraphics();
		g.drawImage(img, 0, 0, width, height, null);
		g.dispose();
		
		return newImage;
	}
	
	/**
	 * Replaces all pixels of the original color with the new color in a given
	 * image.
	 * 
	 * @param img
	 *            The image to process.
	 * @param originalRGB
	 *            The RGB value to replace.
	 * @param newRGB
	 *            The new RGB value for the replaced pixels.
	 */
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
	
	/**
	 * Gets an RGB value for a given owner.
	 * @param owner The owner to get a color for.
	 * @return An RGB value for the given owner.
	 * @see Owner
	 */
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

	/**
	 * Implements tileUpdated from TileUpdateListener. Flags this button as
	 * needing to be updated.
	 */
	@Override
	public void tileUpdated() {
		tileDidChange = true;
	}

}
