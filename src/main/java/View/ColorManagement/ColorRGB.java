package View.ColorManagement;

/**
 * Class used to represent colors, using RGB values
 */
public class ColorRGB {

	/**
	 * Attributes used to store the integer for red, green and blue
	 */
	private int r, g, b;

	/**
	 * Constructor of RGB color
	 * @param r is the integer that represents the red component
	 * @param g is the integer that represents the green component
	 * @param b is the integer that represents the blue component
	 */
	public ColorRGB(int r, int g, int b) {
		this.r = r;
		this.g = g;
		this.b = b;
	}

	/**
	 * Getter of the red integer of the color
	 */
	public int getR() {
		return r;
	}

	/**
	 * Getter of the green integer of the color
	 */
	public int getG() {
		return g;
	}

	/**
	 * Getter of the blue integer of the color
	 */
	public int getB() {
		return b;
	}

	/**
	 * Setter of the integers for a color, from a single integer
	 * @param totalValue is the total value used to obtain the three integers
	 */
	public void set(int totalValue) {
		this.r = (totalValue >> 16) & 0xff;
		this.g = (totalValue >> 8)  & 0xff;
		this.b = totalValue & 0xff;
	}

	/**
	 * Setter of the integers for a color with explicit values
	 * @param r represents the red component
	 * @param g represents the green component
	 * @param b represents the blue component
	 */
	public void set(int r, int g, int b){
		this.r = r;
		this.g = g;
		this.b = b;
	}

	/**
	 * Setter of the red integer component of the color
	 * @param r represents the red component
	 */
	public void setR(int r) {
		this.r = r;
	}

	/**
	 * Setter of the green integer component of the color
	 * @param g represents the red component
	 */
	public void setG(int g) {
		this.g = g;
	}

	/**
	 * Setter of the blue integer component of the color
	 * @param b represents the red component
	 */
	public void setB(int b) {
		this.b = b;
	}

	/**
	 * Method used to calculate the distance between two colors
	 * @param color1 is the first color
	 * @param color2 is the other color
	 */
	public static int getColorDifference(ColorRGB color1, ColorRGB color2) {
		int diffR = color1.getR() - color2.getR();
		int diffG = color1.getG() - color2.getG();
		int diffB = color1.getB() - color2.getB();
		return diffR * diffR + diffG * diffG + diffB * diffB;
	}
}
