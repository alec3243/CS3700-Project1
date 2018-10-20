package huffman;

import java.io.IOException;

public class Main {

	public static void main(String[] args) {
		final String FILENAME = "test.txt";
		try {
			Huffman huffman = new Huffman(FILENAME);
			EncodedData data = huffman.compress();
			System.out.println(data.getEncodedString());
			System.out.println(huffman.decode(data));
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
