package View.ColorManagement;

import java.util.ArrayList;

/**
 * Color class for TUI
 */
public class ConsoleColor {
	public final static String RESET = 			"\u001B[000m";
	public final static String TEXT_BLACK = 	"\u001B[030m";
	public final static String TEXT_RED = 		"\u001B[031m";
	public final static String TEXT_GREEN = 	"\u001B[032m";
	public final static String TEXT_YELLOW = 	"\u001B[033m";
	public final static String TEXT_BLUE = 		"\u001B[034m";
	public final static String TEXT_PURPLE = 	"\u001B[035m";
	public final static String TEXT_CYAN = 		"\u001B[036m";
	public final static String TEXT_GRAY = 		"\u001B[037m";
	public final static String TEXT_WHITE = 	"\u001B[038m";
	public final static ArrayList<String> text_colors = new ArrayList<>();
	static {
		text_colors.add(TEXT_BLACK);
		text_colors.add(TEXT_RED);
		text_colors.add(TEXT_GREEN);
		text_colors.add(TEXT_YELLOW);
		text_colors.add(TEXT_BLUE);
		text_colors.add(TEXT_PURPLE);
		text_colors.add(TEXT_CYAN);
		text_colors.add(TEXT_GRAY);
		text_colors.add(TEXT_WHITE);
	}

	public final static String BACKGROUND_BLACK = 	"\u001B[040m";
	public final static String BACKGROUND_RED = 	"\u001B[041m";
	public final static String BACKGROUND_GREEN = 	"\u001B[042m";
	public final static String BACKGROUND_YELLOW = 	"\u001B[043m";
	public final static String BACKGROUND_BLUE = 	"\u001B[044m";
	public final static String BACKGROUND_PURPLE = 	"\u001B[045m";
	public final static String BACKGROUND_CYAN = 	"\u001B[046m";
	public final static String BACKGROUND_GRAY = 	"\u001B[047m";
	public final static String BACKGROUND_WHITE = 	"\u001B[107m";
	public final static ArrayList<String> background_colors = new ArrayList<>();
	static {
		background_colors.add(BACKGROUND_BLACK);
		background_colors.add(BACKGROUND_RED);
		background_colors.add(BACKGROUND_GREEN);
		background_colors.add(BACKGROUND_YELLOW);
		background_colors.add(BACKGROUND_BLUE);
		background_colors.add(BACKGROUND_PURPLE);
		background_colors.add(BACKGROUND_CYAN);
		background_colors.add(BACKGROUND_GRAY);
		background_colors.add(BACKGROUND_WHITE);
	}
}
