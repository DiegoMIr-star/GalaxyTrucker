package View.ColorManagement;

import java.util.List;

/**
 * Class used to create a color palette, that matches with macOS colors
 */
public class MacOSConsolePalette {

	/**
	 * Attributes that represents different rgb colors approximated for Mac
	 */
	public static final ColorRGB VALUES_BLACK  = new ColorRGB(0,0,0);
	public static final ColorRGB VALUES_RED    = new ColorRGB(153,2,0);
	public static final ColorRGB VALUES_GREEN  = new ColorRGB(5,165,0);
	public static final ColorRGB VALUES_YELLOW = new ColorRGB(153,153,0);
	public static final ColorRGB VALUES_BLUE   = new ColorRGB(0,0,178);
	public static final ColorRGB VALUES_PURPLE = new ColorRGB(178,0,178);
	public static final ColorRGB VALUES_CYAN   = new ColorRGB(5,165,178);
	public static final ColorRGB VALUES_GRAY   = new ColorRGB(191,191,191);
	public static final ColorRGB VALUES_WHITE  = new ColorRGB(229,229,229);

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
