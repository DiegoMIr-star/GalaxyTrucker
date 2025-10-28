package View.tuiGraphics;

import View.ColorManagement.*;

import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;

/**
 * Class used to convert in a string-based representation a ppm image
 */
public class StringImage {

	/**
	 * Attribute that represents the number of character boxes in width
	 */
	int charBoxesInWidth;

	/**
	 * Attribute that represents the ratio between width and height
	 */
	double charBoxRatio;

	/**
	 * Attribute used to store the final string
	 */
	String imageStr = "";

	/**
	 * Attribute used to store the color palette
	 */
	ConsolePalette palette;

	/**
	 * Constructor of the string image, considering a specific console
	 * @param consoleType is the current considered console type
	 */
	public StringImage(ConsoleType consoleType){
		changeConsoleType(consoleType);
	}

	/**
	 * Constructor of the string image, considering a specific console and character boxes width
	 * @param consoleType is the current considered console type
	 * @param charBoxesInWidth represents the number of character boxes in width
	 */
	public StringImage(ConsoleType consoleType, int charBoxesInWidth){
		this.charBoxesInWidth = charBoxesInWidth;
		changeConsoleType(consoleType);
	}

	/**
	 * Constructor of the string image, considering a specific palette and character box ratio
	 * @param palette is the current considered palette
	 * @param charBoxRatio represents the ratio between width and height of the character
	 */
	public StringImage(ConsolePalette palette, double charBoxRatio) {
		this.palette = palette;
		this.charBoxRatio = charBoxRatio;
	}

	/**
	 * Constructor of the string image, considering a specific palette, character box ratio and character boxes width
	 * @param palette is the current considered palette
	 * @param charBoxesInWidth represents the number of character boxes in width
	 * @param charBoxRatio represents the ratio between width and height of the character
	 */
	public StringImage(int charBoxesInWidth, double charBoxRatio, ConsolePalette palette) {
		this.charBoxesInWidth = charBoxesInWidth;
		this.charBoxRatio = charBoxRatio;
		this.palette = palette;
	}

	/**
	 * Method used to change the palette and the character box ratio based on the console type
	 * @param consoleType is the new console type
	 */
	public void changeConsoleType(ConsoleType consoleType){
		switch (consoleType){
			case Windows_terminal:
				this.palette = WindowsTerminalConsolePalette.PALETTE;
				charBoxRatio = 2.0;
				break;
			case IntelliJ_console:
				this.palette = IntelliJConsolePalette.PALETTE;
				charBoxRatio = 2.7;
				break;
			case MacOS_terminal:
				this.palette = MacOSConsolePalette.PALETTE;
				charBoxRatio = 2.0;
				break;
			default:
				throw new RuntimeException("Unsupported console type");
		}
	}

	/**
	 * Setter of the character boxes width
	 * @param charBoxesInWidth is the current width to set
	 */
	public void setUpString(int charBoxesInWidth){
		this.charBoxesInWidth = charBoxesInWidth;

	}

	/**
	 * Getter of the width of character boxes
	 * @return the width
	 */
	public int getCharBoxesInWidth() {
		return charBoxesInWidth;
	}

	/**
	 * Getter of the character box ratio
	 * @return the ratio
	 */
	public double getCharBoxRatio() {
		return charBoxRatio;
	}

	/**
	 * Setter of the character box ratio
	 * @param charBoxRatio is the current ratio to set
	 */
	public void setCharBoxRatio(double charBoxRatio) {
		this.charBoxRatio = charBoxRatio;
	}

	/**
	 * Setter of the character boxes width
	 * @param charBoxesInWidth is the current width to set
	 */
	public void setcharBoxesInWidth(int charBoxesInWidth) {
		this.charBoxesInWidth = charBoxesInWidth;
	}

	/**
	 * Setter of the console palette
	 * @param palette is the current palette to set
	 */
	public void setPalette(ConsolePalette palette) {
		this.palette = palette;
	}

	/**
	 * Method used to compute the string image from a path
	 * it invokes the method {@link #computeStringImage(String, int)}
	 * @param pathName is the current path
	 */
	public void computeStringImage(String pathName) throws IOException {
		computeStringImage(pathName, 0);
	}

	/**
	 * Method used to compute the string image from a path, with a specific rotation
	 * it invokes the method {@link #computeStringImage(String, int, double, ColorRGB)}
	 * @param pathName is the current path
	 * @param counterclockwise90degRotations is the current rotation
	 */
	public void computeStringImage(String pathName, int counterclockwise90degRotations) throws IOException {
		computeStringImage(pathName, counterclockwise90degRotations, 0, new ColorRGB(0,0,0));
	}

	/**
	 * Getter of the rgb color of a specific pixel
	 * @param curPixelX is the x coordinate of the pixel
	 * @param curPixelY is the y coordinate of the pixel
	 * @return the rgb color
	 */
	private ColorRGB getCurPixel(int curPixelX, int curPixelY, ColorRGB[][] PPMimage) throws IOException {
		return PPMimage[curPixelY][curPixelX];
	}

	/**
	 * Method used to compute the string image from a path, with a specific rotation and color tinting
	 * @param pathFile is the current path
	 * @param counterclockwise90degRotations is the current rotation
	 * @param tintAmount represents how much is tinted (0 means no tint and 1 full)
	 * @param tintColor represents the color
	 */
	public void computeStringImage(String pathFile, int counterclockwise90degRotations, double tintAmount, ColorRGB tintColor) throws IOException {
		int size = 4;
		counterclockwise90degRotations = (size + (counterclockwise90degRotations) % size) % size;

		if(tintAmount > 1.0)
			tintAmount = 1.0;

		imageStr = "";
		// Load the image
		InputStream file = StringImage.class.getResourceAsStream(pathFile);
		Scanner scanner;
		ColorRGB[][] PPMimage; // [row][col][channel]
		int width;
		int height;

		if(file == null){
			throw new RuntimeException("File not found.");
		}


			scanner = new Scanner(file);
			String format = scanner.next();
			int PPMwidth = scanner.nextInt();
			int PPMheight = scanner.nextInt();
			int maxColor = scanner.nextInt();

			PPMimage = new ColorRGB[PPMheight][PPMwidth];

			for (int y = 0; y < PPMheight; y++) {
				for (int x = 0; x < PPMwidth; x++) {
					int r = scanner.nextInt();
					int g = scanner.nextInt();
					int b = scanner.nextInt();
					PPMimage[y][x] = new ColorRGB(r, g, b);
				}
			}

			width = PPMwidth;
			height = PPMheight;

		int charBoxesInWidth;
		int charBoxesInHeight;
		double pixelsPerCharW;
		double pixelsPerCharH;

		double xForStart, xForEnd, yForStart, yForEnd;

		switch(counterclockwise90degRotations) {
			default:
			case 0:
				charBoxesInWidth = this.charBoxesInWidth;
				charBoxesInHeight = (int) ((double) charBoxesInWidth * (double) height / (double) width / charBoxRatio);
				pixelsPerCharW = (double) width / charBoxesInWidth;
				pixelsPerCharH = (double) height / charBoxesInHeight;
				break;
			case 1:
				charBoxesInHeight = (int) ((double) this.charBoxesInWidth / charBoxRatio);
				charBoxesInWidth = (int) (((double) this.charBoxesInWidth) * (double) height / (double) width);
				pixelsPerCharW = (double) width / charBoxesInHeight;
				pixelsPerCharH = (double) height / charBoxesInWidth;
				break;
			case 2:
				charBoxesInWidth = this.charBoxesInWidth;
				charBoxesInHeight = (int) ((double) charBoxesInWidth * (double) height / (double) width / charBoxRatio);
				pixelsPerCharW = (double) width / charBoxesInWidth;
				pixelsPerCharH = (double) height / charBoxesInHeight;
				break;
			case 3:
				charBoxesInHeight = (int) ((double) this.charBoxesInWidth / charBoxRatio);
				charBoxesInWidth = (int) (((double) this.charBoxesInWidth) * (double) height / (double) width);
				pixelsPerCharW = (double) width / charBoxesInHeight;
				pixelsPerCharH = (double) height / charBoxesInWidth;
				break;
		}

		int pixelsInCurBox;
		int totR, totG, totB;
		ColorRGB curPixel;
		int curPixelX, curPixelY;
		ColorRGB curBoxAvgPixel = new ColorRGB(0, 0, 0);


		for (int boxY = 0; boxY < charBoxesInHeight; boxY++) {
			for (int boxX = 0; boxX < charBoxesInWidth; boxX++) {
				pixelsInCurBox = 0;
				totR = 0;
				totG = 0;
				totB = 0;

				switch(counterclockwise90degRotations){
					default:
					case 0:
						yForStart = pixelsPerCharH * boxY;
						yForEnd = pixelsPerCharH * (boxY + 1);
						break;
					case 1:
						yForStart = pixelsPerCharH * boxX;
						yForEnd = pixelsPerCharH * (boxX + 1);
						break;
					case 2:
						yForStart = pixelsPerCharH * (charBoxesInHeight - 1 - boxY);
						yForEnd = pixelsPerCharH * (charBoxesInHeight - boxY);
						break;
					case 3:
						yForStart = pixelsPerCharH * (charBoxesInWidth - 1 - boxX);
						yForEnd = pixelsPerCharH * (charBoxesInWidth - boxX);
						break;
				}
				for (double y = yForStart; y < yForEnd; y++) {
					curPixelY = (int) y;

					switch(counterclockwise90degRotations){
						default:
						case 0:
							xForStart = pixelsPerCharW * boxX;
							xForEnd = pixelsPerCharW * (boxX + 1);
							break;
						case 1:
							xForStart = pixelsPerCharW * (charBoxesInHeight - 1 - boxY);
							xForEnd = pixelsPerCharW * (charBoxesInHeight - boxY);
							break;
						case 2:
							xForStart = pixelsPerCharW * (charBoxesInWidth - 1 - boxX);
							xForEnd = pixelsPerCharW * (charBoxesInWidth - boxX);
							break;
						case 3:
							xForStart = pixelsPerCharW * boxY;
							xForEnd = pixelsPerCharW * (boxY + 1);
							break;
					}
					for (double x = xForStart; x < xForEnd; x++) {
						curPixelX = (int) x;
						if (curPixelX < width && curPixelY < height) {
							curPixel = getCurPixel(curPixelX, curPixelY, PPMimage);



							totR += (int) ((1 - tintAmount) * curPixel.getR() + tintAmount * tintColor.getR());
							totG += (int) ((1 - tintAmount) * curPixel.getG() + tintAmount * tintColor.getG());
							totB += (int) ((1 - tintAmount) * curPixel.getB() + tintAmount * tintColor.getB());
							pixelsInCurBox++;
						} else break;
					}
				}
				curBoxAvgPixel.set(totR / pixelsInCurBox, totG / pixelsInCurBox, totB / pixelsInCurBox);
				imageStr += (new ConsolePixel(curBoxAvgPixel, palette).getConsolePixelChar());
			}
			imageStr += ConsoleColor.RESET + "\n";
		}
	}

	/**
	 * Getter of the string image
	 * @return the string image
	 */
	public String getStringImage() {
		return imageStr;
	}

	/**
	 * Method used to compute the string image from a path and return it;
	 * it invokes the method {@link #computeStringImage(String)}
	 * @param pathName is the current path
	 * @return the string image
	 */
	public String computeAndGetStringImage(String pathName) throws IOException {
		computeStringImage(pathName);
		return imageStr;
	}

	/**
	 * Method used to compute the string image from a path, with a specific rotation, and return it;
	 * it invokes the method {@link #computeStringImage(String, int)}
	 * @param pathName is the current path
	 * @param counterclockwise90degRotations is the rotation
	 * @return the string image
	 */
	public String computeAndGetStringImage(String pathName, int counterclockwise90degRotations) throws IOException {
		computeStringImage(pathName, counterclockwise90degRotations);
		return imageStr;
	}

	/**
	 * Method used to compute the string image from a path, with a specific rotation and color tinting, and return it;
	 * it invokes the method {@link #computeStringImage(String, int, double, ColorRGB)}
	 * @param pathName is the current path
	 * @param counterclockwise90degRotations is the rotation
	 * @param tintAmount represents how much is tinted (0 means no tint and 1 full)
	 * @param tintColor represents the color
	 * @return the string image
	 */
	public String computeAndGetStringImage(String pathName, int counterclockwise90degRotations, double tintAmount, ColorRGB tintColor) throws IOException{
		computeStringImage(pathName, counterclockwise90degRotations, tintAmount, tintColor);
		return imageStr;
	}
}
