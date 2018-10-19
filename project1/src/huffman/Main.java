package huffman;

import java.io.File;

public class Main {

	public static void main(String[] args) {
		final String FILENAME = "Constitution.txt";
		Huffman huffman = new Huffman(new File(FILENAME));
		
	}

}
