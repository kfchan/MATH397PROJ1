package project1;

public class imageManip {
	public static void main(String[] pirateArgs) {
		if (pirateArgs.length != 1) {
			System.err.println("Exactly one file argument needed.");
		}
		Picture picture = new Picture("../" + pirateArgs[0], false);
		picture.display();
	}
}