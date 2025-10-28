package View.ColorManagement;

import java.util.List;

/**
 * Class used to create a color palette, that matches with Windows colors
 */
public class WindowsTerminalConsolePalette {

	/**
	 * Attributes that represents different rgb colors approximated for Windows
	 */
	public static final ColorRGB VALUES_BLACK = new ColorRGB(12, 12, 12);
	public static final ColorRGB VALUES_RED = new ColorRGB(197, 15, 31);
	public static final ColorRGB VALUES_GREEN = new ColorRGB(19, 161, 14);
	public static final ColorRGB VALUES_YELLOW = new ColorRGB(193, 156, 0);
	public static final ColorRGB VALUES_BLUE = new ColorRGB(0, 55, 218);
	public static final ColorRGB VALUES_PURPLE = new ColorRGB(136, 23, 152);
	public static final ColorRGB VALUES_CYAN = new ColorRGB(58, 150, 221);
	public static final ColorRGB VALUES_GRAY = new ColorRGB(204, 204, 204);
	public static final ColorRGB VALUES_WHITE = new ColorRGB(242, 242, 242);

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
			true);

}
