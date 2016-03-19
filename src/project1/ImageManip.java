package project1;

import java.util.*;
import java.awt.Color;

public class ImageManip {
	private static final int RED = 0;
	private static final int GREEN = 1;
	private static final int BLUE = 2;
	private static final int NUM_TRIANGLE_SIDES = 3;

	/** if on, the rgb valued triangles rotate **/
	private static final boolean IS_ROT_ON = true;
	/** rotates rgb trianges by this offset for each time it is drawn **/
	private static final int ROT_OFFSET_BY = 2;

	/** if 1, the rgb triangles are not equalateral; if 2, they are **/
 	private static final int TRI_TYPE = 1;

	private final Picture image;
	private final Random random;

	private Picture canvas;
	private int rotateOffset;

	/** 
	* saves original image
	* creates new canvas for manipulation
	*/
	public ImageManip(String file) {
		this.image = new Picture(file, false);
		this.canvas = new Picture(this.image.getWidth(), this.image.getHeight(), false);
		this.random = new Random();
		this.rotateOffset = 0;
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

	/** 
	* @return taverage of n and m
	*/	
	private int avg(int n, int m) {
		return (n + m)/2;
	}

	/**
	* @return the array of the average rgb values of an nxn square starting at x, y
	*/
	private int[] getAveragedValsOfSquare(int x, int y, int n) {
		return getAveragedVals(x, y, n, n);
	}

	/**
	* n is width
	* m is height
	* @return the array of the average rgb values of an nxm rectangle starting at x, y
	*/
	private int[] getAveragedVals(int x, int y, int n, int m) {
		int[] rtn = new int[NUM_TRIANGLE_SIDES];
		int avgRed = 0;
		int avgGreen = 0;
		int avgBlue = 0;
		int totalPixels = 0;

		for (int i = x; i < (x+n); i++) {
			for (int j = y; j < (y+m); j++) {
				if (j < this.getPictureHeight() && i < this.getPictureWidth()) {
					avgRed += this.image.getPixelRed(i,j);
					avgGreen += this.image.getPixelGreen(i,j);
					avgBlue += this.image.getPixelBlue(i,j);
					totalPixels++;
				}
			}
		}

		if (totalPixels == 0) {
			rtn[0] = 255;		
			rtn[1] = 255;
			rtn[2] = 255;
			return rtn;
		}

		rtn[0] = avgRed / totalPixels;		
		rtn[1] = avgGreen / totalPixels;
		rtn[2] = avgBlue / totalPixels;
		return rtn;
	}

	/**
	* circles
	* @param isRand if the circles will vary in diameter
	*/
	public void circles(int n, boolean isRand) {
		for (int x = 0; x < this.getPictureWidth(); x += n) {
			for (int y = 0; y < this.getPictureHeight(); y += n) {
				this.createCircle(x, y, n, isRand);
			}
		}		
	}

	private void createCircle(int x, int y, int n, boolean isRand) {
		int[] averagedValues = this.getAveragedValsOfSquare(x, y, n);

		if (isRand) {
			n = randomize(n);
		}

		// draw circle in the middle of each block
		int mid = n/2;
		x += mid;
		y += mid;
		if (x < this.getPictureWidth() && y < this.getPictureHeight()) {
			this.canvas.setPenColor(averagedValues[0],averagedValues[1], averagedValues[2]);
			this.canvas.drawCircleFill(x,y,mid); 
		}
	}

	private int randomize(int n) {
		int rand = this.random.nextInt((int) 2 * n);
		rand = rand - n;
		if (rand > 0) {
			n += rand;
		} else {
			n -= rand;
		}
		return n;	
	}

	/** 
	* blocks the image into nxn squares
	*/
	public void squares(int n) {
		for (int x = 0; x < this.getPictureWidth(); x += n) {
			for (int y = 0; y < this.getPictureHeight(); y += n) {
				this.setAverageColor(x,y,n);
			}
		} 
	}

	/**
	* gets the average color of a block of nxn pixels starting at x and y
	**/ 
	private void setAverageColor(int x, int y, int n) {
		int[] avgValues = this.getAveragedValsOfSquare(x, y, n);

		for (int i = x; i < (x+n); i++) {
			for (int j = y; j < (y+n); j++) {
				// check for the border
				if (i < this.getPictureWidth() && j < this.getPictureHeight()) {
					this.canvas.setPixelColor(i, j, avgValues[0],avgValues[1],avgValues[2]);
				}
			}
		}		
	}

	/**
	* makes the picture a collection of triangles
	*/
	public void triangles(int n, boolean isRGBTri) {
		double height = n / 2 * Math.sqrt(NUM_TRIANGLE_SIDES); // height of triangle
		int startX = n/2;

		for (int y = 0; y < this.getPictureWidth(); y += n) {
			this.drawColumn(startX, 0, n, (int) height, isRGBTri);
			startX += n;
		}
	}

	/**
	* draws a whole column of triangles
	* where a unit of the column is an upright triangle on the left and an upside down one on the right
	* such that they fit together like a puzzle
	*/
	private void drawColumn(int startX, int startY, int n, int height, boolean isRGBTri) {
		while(startY < this.getPictureHeight()) {
			int[] newPoint = this.drawUprightTriangle(startX, startY, n, height, isRGBTri);
			this.drawUpsideDownTriangle(startX, startY, n, height, isRGBTri);
			startY = newPoint[1];
		}
	}

	/**
	* draws all three rgb triangles in the given averaged triangle
	*/
	private void createMiniTriangles(int[] xPoints, int[] yPoints, int[] color) {
		int[] xMidpoints = new int[NUM_TRIANGLE_SIDES];
		int[] yMidpoints = new int[NUM_TRIANGLE_SIDES];

		for (int i = 0; i < NUM_TRIANGLE_SIDES; i++) {
			xMidpoints[i] = avg(xPoints[i], xPoints[(i + 1) % NUM_TRIANGLE_SIDES]);
			yMidpoints[i] = avg(yPoints[i], yPoints[(i + 1) % NUM_TRIANGLE_SIDES]);
		}

		// midpoint i, midpoint i+1, point i
		for (int i = 0; i < NUM_TRIANGLE_SIDES; i++) {
			int[] xMini = {xMidpoints[i], xMidpoints[(i + TRI_TYPE) % NUM_TRIANGLE_SIDES], xPoints[i]};
			int[] yMini = {yMidpoints[i], yMidpoints[(i + TRI_TYPE) % NUM_TRIANGLE_SIDES], yPoints[i]};
			int colorVal = (i + this.rotateOffset) % NUM_TRIANGLE_SIDES;
			this.drawMiniTriangle(xMini, yMini, color[colorVal], colorVal);
		}
		if (IS_ROT_ON) {
			this.rotateOffset += ROT_OFFSET_BY;
		}
	}

	/**
	* draws the rgb triangle by filling in the value with its corresponding color
	* 'whites' out the  other colors
	*/
	private void drawMiniTriangle(int[] xPoints, int[] yPoints, int rgbVal, int rgbColor) {
		if (rgbColor == RED) {
			this.canvas.setPenColor(rgbVal, 255, 255);
		} else if (rgbColor == BLUE) {
			this.canvas.setPenColor(255, rgbVal, 255);
		} else {
			this.canvas.setPenColor(255, 255, rgbVal);
		}
		Color color = this.canvas.getPenColor();
		// System.out.println(color.getRed() + " " + color.getGreen() + " " + color.getBlue());
		this.canvas.fillPoly(xPoints, yPoints, NUM_TRIANGLE_SIDES);
	}

	private int[] drawUprightTriangle(int x, int y, int n, int height, boolean isRGBTri) {
		int[] xPoints = new int[NUM_TRIANGLE_SIDES];
		int[] yPoints = new int[NUM_TRIANGLE_SIDES];

		xPoints[0] = x;
		yPoints[0] = y;

		if (isRGBTri) {
			this.canvas.setPenWidth(0);
		} else {
			this.canvas.setPenWidth(0);
		}		

		int[] avgValues = this.getAveragedVals(x-(n/2), y, n, height);
		// this.canvas.setPenColor(0, 0, 0);
		this.canvas.setPenColor(avgValues[0], avgValues[1], avgValues[2]);
		// top vertex
		this.canvas.setPosition(x, y);

		// bottom right vertex
		this.canvas.setDirection(-60);	
		this.canvas.drawForward(n);	
		xPoints[1] = (int) this.canvas.getX();
		yPoints[1] = (int) this.canvas.getY();

		//bottom left vertex
		this.canvas.setDirection(180);
		this.canvas.drawForward(n);	
		xPoints[2] = (int) this.canvas.getX();
		yPoints[2] = (int) this.canvas.getY();

		this.canvas.setDirection(60);
		this.canvas.drawForward(n);

		// this.canvas.setPenColor(avgValues[0], avgValues[1], avgValues[2]);
		this.canvas.fillPoly(xPoints, yPoints, NUM_TRIANGLE_SIDES);
		if (isRGBTri) {
			this.createMiniTriangles(xPoints, yPoints, avgValues);
		}

		int[] rtn = {(int) xPoints[2], (int) yPoints[2]};
		return rtn;
	}

	private int[] drawUpsideDownTriangle(int x, int y, int n, int height, boolean isRGBTri) {
		int[] xPoints = new int[NUM_TRIANGLE_SIDES];
		int[] yPoints = new int[NUM_TRIANGLE_SIDES];

		xPoints[0] = x;
		yPoints[0] = y;

		if (isRGBTri) {
			this.canvas.setPenWidth(0);
		} else {
			this.canvas.setPenWidth(0);
		}

		int[] avgValues = this.getAveragedVals(x-(n/2), y, n, height);
		// this.canvas.setPenColor(0, 0, 0);
		this.canvas.setPenColor(avgValues[0], avgValues[1], avgValues[2]);
		// top left vertex
		this.canvas.setPosition(x, y);

		// bottom vertex
		this.canvas.setDirection(-60);	
		this.canvas.drawForward(n);	
		xPoints[2] = (int) this.canvas.getX();
		yPoints[2] = (int) this.canvas.getY();

		// top right vertex
		this.canvas.setDirection(60);	
		this.canvas.drawForward(n);	
		xPoints[1] = (int) this.canvas.getX();
		yPoints[1] = (int) this.canvas.getY();

		this.canvas.setDirection(180);	
		this.canvas.drawForward(n);		

		// this.canvas.setPenColor(avgValues[0], avgValues[1], avgValues[2]);
		this.canvas.fillPoly(xPoints, yPoints, NUM_TRIANGLE_SIDES);
		if (isRGBTri) {
			this.createMiniTriangles(xPoints, yPoints, avgValues);
		}

		int[] rtn = {(int) xPoints[2], (int) yPoints[2]};
		return rtn;		
	}

	public static void main(String[] pirateArgs) {
		if (pirateArgs.length != 2) {
			System.err.println("Exactly two file arguments needed.");
			System.exit(1);
		}

		ImageManip imageManip = new ImageManip("../" + pirateArgs[0]);
		imageManip.circles(Integer.parseInt(pirateArgs[1]), true);
		// imageManip.squares(Integer.parseInt(pirateArgs[1]));
		// imageManip.triangles(Integer.parseInt(pirateArgs[1]), true);
		imageManip.displayNewImage();
	}
}