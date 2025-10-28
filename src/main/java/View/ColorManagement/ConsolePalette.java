package View.ColorManagement;

import java.util.List;

/**
 * Class used to represent a palette of RGB colors
 */
public class ConsolePalette {

	/**
	 * Final attribute that contains the list of colors
	 */
	public final List<ColorRGB> rgbColors;

	/**
	 * Final flag used to indicate that the text covers the background
	 */
	public final boolean textCoversBackground;

	/**
	 * Constructor of the color palette
	 * @param rgbColors is the list of rgb colors
	 * @param textCoversBackground a boolean that is true if the text covers the background
	 */
	public ConsolePalette(List<ColorRGB> rgbColors, boolean textCoversBackground) {
		this.rgbColors = rgbColors;
		this.textCoversBackground = textCoversBackground;
	}
}

