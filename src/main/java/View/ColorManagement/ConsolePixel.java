package View.ColorManagement;

/**
 * Class used to convert a {@link ColorRGB}, using the color palette {@link ConsolePalette}
 */
public class ConsolePixel {

	/**
	 * Attribute used to store the primary color index in the palette
	 */
	protected int primaryColorIndex;

	/**
	 * Attribute used to store the secondary color index in the palette
	 */
	protected int secondaryColorIndex;

	/**
	 * Attribute used to store the gradient between primary and secondary color
	 */
	protected int colorGradient;

	/**
	 * Attribute used to store the palette
	 */
	protected final ConsolePalette palette;

	/**
	 * Attribute used to store the number of interpolations steps between two colors
	 */
	protected final int numOfSteps;

	/**
	 * Constructor of a pixel console, trying to approximate the color in the best way
	 * @param inputColor is the color to approximate
	 * @param palette is the current palette to use
	 */
	public ConsolePixel(ColorRGB inputColor, ConsolePalette palette) {
		this.palette = palette;
		if(palette.textCoversBackground)
			numOfSteps = 5;
		else
			numOfSteps = 10;

		computeClosestMatch(inputColor);
	}

	/**
	 * Method used to compute the closest match for the considered color,
	 * calculating the distance with the interpolated colors of the considered palette
	 * @param inputColor is the current color to match
	 */
	private void computeClosestMatch(ColorRGB inputColor) {
		// your logic, just replace all direct references to IntelliJConsoleColor.color_values
		// with palette.rgbColors.get(i)
		double rStepDiff, gStepDiff, bStepDiff;
		ColorRGB curSecondaryColor;
		ColorRGB curPrimaryColor;
		double curR, curG, curB;
		int curDiffR, curDiffG, curDiffB;
		int minDiff = 255 * 255 * 3, minDiffPrimryIndex = -1, minDiffSecondaryIndex = -1, minDiffGradientIndex = -1;
		int curDiff;
		int maxColorDifference1 = 30000;

		for (int primaryColorIndex = 0; primaryColorIndex < palette.rgbColors.size(); primaryColorIndex++) {
			curPrimaryColor = palette.rgbColors.get(primaryColorIndex);

			for (int secondaryColorIndex = primaryColorIndex; secondaryColorIndex < palette.rgbColors.size(); secondaryColorIndex++) {
				curSecondaryColor = palette.rgbColors.get(secondaryColorIndex);
				rStepDiff = (double) (curSecondaryColor.getR() - curPrimaryColor.getR()) / (numOfSteps - 1);
				gStepDiff = (double) (curSecondaryColor.getG() - curPrimaryColor.getG()) / (numOfSteps - 1);
				bStepDiff = (double) (curSecondaryColor.getB() - curPrimaryColor.getB()) / (numOfSteps - 1);

				if (primaryColorIndex == secondaryColorIndex) {
					curR = curPrimaryColor.getR();
					curG = curPrimaryColor.getG();
					curB = curPrimaryColor.getB();

					curDiffR = (int) curR - inputColor.getR();
					curDiffG = (int) curG - inputColor.getG();
					curDiffB = (int) curB - inputColor.getB();

					curDiff = curDiffR * curDiffR + curDiffG * curDiffG + curDiffB * curDiffB;
					if (curDiff < minDiff) {
						minDiff = curDiff;
						minDiffPrimryIndex = primaryColorIndex;
						minDiffSecondaryIndex = secondaryColorIndex;
						minDiffGradientIndex = 0;
					}
				} else {
					for (int gradientStep = 0; gradientStep < numOfSteps; gradientStep++) {
						curR = (curPrimaryColor.getR() + gradientStep * rStepDiff);
						curG = (curPrimaryColor.getG() + gradientStep * gStepDiff);
						curB = (curPrimaryColor.getB() + gradientStep * bStepDiff);

						curDiffR = (int) curR - inputColor.getR();
						curDiffG = (int) curG - inputColor.getG();
						curDiffB = (int) curB - inputColor.getB();

						curDiff = curDiffR * curDiffR + curDiffG * curDiffG + curDiffB * curDiffB;
						if ((gradientStep == 1 || gradientStep == 8 || gradientStep == 3 || gradientStep == 6) &&
								(ColorRGB.getColorDifference(curPrimaryColor, curSecondaryColor) > maxColorDifference1)) {
						} else if (curDiff < minDiff) {
							minDiff = curDiff;
							minDiffPrimryIndex = primaryColorIndex;
							minDiffSecondaryIndex = secondaryColorIndex;
							minDiffGradientIndex = gradientStep;
						}
					}
				}
			}
		}

		primaryColorIndex = minDiffPrimryIndex;
		secondaryColorIndex = minDiffSecondaryIndex;
		colorGradient = minDiffGradientIndex;
	}

	/**
	 * Method used to transform the color information in a string that represents a pixel's color
	 * @return a string necessary to display the pixel
	 */
	public String getConsolePixelChar() {
		if(palette.textCoversBackground){
			switch (colorGradient) {
				case 0:
					return ConsoleColor.text_colors.get(primaryColorIndex) + ConsoleColor.background_colors.get(secondaryColorIndex) + "█";
				case 1:
					return ConsoleColor.text_colors.get(primaryColorIndex) + ConsoleColor.background_colors.get(secondaryColorIndex) + "▓";
				case 2:
					return ConsoleColor.text_colors.get(primaryColorIndex) + ConsoleColor.background_colors.get(secondaryColorIndex) + "▒";
				case 3:
					return ConsoleColor.text_colors.get(primaryColorIndex) + ConsoleColor.background_colors.get(secondaryColorIndex) + "░";
				case 4:
					return ConsoleColor.text_colors.get(primaryColorIndex) + ConsoleColor.background_colors.get(secondaryColorIndex) + " ";
				default:
					throw new UnexpectedGradientException("Bro this gradient was unexpected as hell ngl frfr, it was " + colorGradient + " ong.");
			}
		}
		else {
			switch (colorGradient) {
				case 0:
					return ConsoleColor.background_colors.get(primaryColorIndex) + ConsoleColor.text_colors.get(secondaryColorIndex) + " ";
				case 1:
					return ConsoleColor.text_colors.get(primaryColorIndex) + ConsoleColor.background_colors.get(secondaryColorIndex) + "█";
				case 2:
					return ConsoleColor.background_colors.get(primaryColorIndex) + ConsoleColor.text_colors.get(secondaryColorIndex) + "░";
				case 3:
					return ConsoleColor.text_colors.get(primaryColorIndex) + ConsoleColor.background_colors.get(secondaryColorIndex) + "▓";
				case 4:
					return ConsoleColor.background_colors.get(primaryColorIndex) + ConsoleColor.text_colors.get(secondaryColorIndex) + "▒";
				case 5:
					return ConsoleColor.text_colors.get(primaryColorIndex) + ConsoleColor.background_colors.get(secondaryColorIndex) + "▒";
				case 6:
					return ConsoleColor.background_colors.get(primaryColorIndex) + ConsoleColor.text_colors.get(secondaryColorIndex) + "▓";
				case 7:
					return ConsoleColor.text_colors.get(primaryColorIndex) + ConsoleColor.background_colors.get(secondaryColorIndex) + "░";
				case 8:
					return ConsoleColor.background_colors.get(primaryColorIndex) + ConsoleColor.text_colors.get(secondaryColorIndex) + "█";
				case 9:
					return ConsoleColor.text_colors.get(primaryColorIndex) + ConsoleColor.background_colors.get(secondaryColorIndex) + " ";
				default:
					throw new UnexpectedGradientException("Bro this gradient was unexpected as hell ngl frfr, it was " + colorGradient + " ong.");
			}
		}
	}

}
