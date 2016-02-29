package project1;

public class ImageManip {
	private final Picture image;

	private Picture canvas;

	/** 
	* saves original image
	* creates new canvas for manipulation
	*/
	public ImageManip(String file) {
		this.image = new Picture("../" + file, false);
		this.canvas = new Picture(this.image.getWidth(), this.image.getHeight(), false);
	}

	/** 
	* displays the given image (the original image)
	*/
	public void displayGivenImage() {
		this.image.display();
	}

	/** 
	* displays the canvas (whether or not its been used)
	*/
	public void displayNewImage() {
		this.canvas.display();
	}

	/** 
	* @return the width of the picture's width
	*/
	private int getPictureWidth() {
		return this.canvas.getWidth();
	}

	/** 
	* @return the height of the picture's width
	*/
	private int getPictureHeight() {
		return this.canvas.getHeight();
	}

	public void circles(int n) {
		for (int x = 0; x < this.getPictureWidth(); x += n) {
			for (int y = 0; y < this.getPictureHeight(); y += n) {
				createCircle(x,y,n);
			}
		}		
	}

	private void createCircle(int x, int y, int n) {
		int avgRed = 0;
		int avgGreen = 0;
		int avgBlue = 0;

		for (int i = x; i < (x+n); i++) {
			for (int j = y; j < (y+n); j++) {
				if (j < this.getPictureHeight() && i < this.getPictureWidth()) {
					avgRed += this.image.getPixelRed(i,j);
					avgGreen += this.image.getPixelGreen(i,j);
					avgBlue += this.image.getPixelBlue(i,j);
				}
			}
		}

		avgRed = avgRed / (n*n);		
		avgGreen = avgGreen / (n*n);
		avgBlue = avgBlue / (n*n);

		// draw circle in the middle of each block
		int mid = n/2;
		x += mid;
		y += mid;
		if (x < this.getPictureWidth() && y < this.getPictureHeight()) {
			this.canvas.setPenColor(avgRed,avgGreen,avgBlue);
			this.canvas.drawCircleFill(x,y,mid); 
		}
	}

	/** 
	* blocks the image into nxn squares
	*/
	public void squares(int n) {
		for (int x = 0; x < this.getPictureWidth(); x += n) {
			for (int y = 0; y < this.getPictureHeight(); y += n) {
				setAverageColor(x,y,n);
			}
		} 
	}

	/**
	* gets the average color of a block of nxn pixels starting at x and y
	**/ 
	private void setAverageColor(int x, int y, int n) {
		int avgRed = 0;
		int avgGreen = 0;
		int avgBlue = 0;

		for (int i = x; i < (x+n); i++) {
			for (int j = y; j < (y+n); j++) {
				if (j < this.getPictureHeight() && i < this.getPictureWidth()) {
					avgRed += this.image.getPixelRed(i,j);
					avgGreen += this.image.getPixelGreen(i,j);
					avgBlue += this.image.getPixelBlue(i,j);
				}
			}
		}

		avgRed = avgRed / (n*n);		
		avgGreen = avgGreen / (n*n);
		avgBlue = avgBlue / (n*n);

		for (int i = x; i < (x+n); i++) {
			for (int j = y; j < (y+n); j++) {
				// check for the border
				if (i < this.getPictureWidth() && j < this.getPictureHeight()) {
					this.canvas.setPixelColor(i,j,avgRed,avgGreen,avgBlue);
				}
			}
		}		
	}

	public static void main(String[] pirateArgs) {
		if (pirateArgs.length != 1) {
			System.err.println("Exactly one file argument needed.");
			System.exit(1);
		}

		ImageManip imageManip = new ImageManip(pirateArgs[0]);
		imageManip.circles(10);
		imageManip.displayNewImage();
	}
}