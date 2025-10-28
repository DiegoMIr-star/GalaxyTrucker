package View.ColorManagement;

import java.util.List;

/**
 * Class used to create a color palette, using the colors of Intellij console
 */
public class IntelliJConsolePalette {

	/**
	 * Attributes that represents different rgb colors
	 */
	public static final ColorRGB VALUES_BLACK = new ColorRGB(0, 0, 0);
	public static final ColorRGB VALUES_RED = new ColorRGB(250, 50, 50);
	public static final ColorRGB VALUES_GREEN = new ColorRGB(84, 179, 62);
	public static final ColorRGB VALUES_YELLOW = new ColorRGB(255, 191, 102);
	public static final ColorRGB VALUES_BLUE = new ColorRGB(51, 204, 255);
	public static final ColorRGB VALUES_PURPLE = new ColorRGB(255, 150, 255);
	public static final ColorRGB VALUES_CYAN = new ColorRGB(55, 204, 204);
	public static final ColorRGB VALUES_GRAY = new ColorRGB(128, 128, 128);
	public static final ColorRGB VALUES_WHITE = new ColorRGB(255, 255, 255);

	/**
	 * The following is the default palette, composed by the previous colors
	 */
	public static final ConsolePalette PALETTE = new ConsolePalette(
			List.of(
					VALUES_BLACK,
					VALUES_RED,
					VALUES_GREEN,
					VALUES_YELLOW,
					VALUES_BLUE,
					VALUES_PURPLE,
					VALUES_CYAN,
					VALUES_GRAY,
					VALUES_WHITE),
			false);
}
