package huffman;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Main {

	public static void main(String[] args) {
		final String FILENAME = "constitution.txt";
		try {
			// Get string from original file, and bytes
			final String ORIGINAL = new String(Files.readAllBytes(Paths
					.get(FILENAME)));
			final int ORIGINAL_BYTES = ORIGINAL.length() * 8;

			// Create tree and print time to make tree
			long start = System.currentTimeMillis();

			Huffman huffman = new Huffman(FILENAME);
			System.out.printf("Milliseconds to create tree: %d%n",
					System.currentTimeMillis() - start);

			// Encode the file and print time to encode file
			start = System.currentTimeMillis();
			EncodedData data = huffman.compress();
			System.out.printf("Milliseconds to encode the file: %d%n",
					System.currentTimeMillis() - start);

			// Get compression percent
			final int COMPRESSED_BYTES = data.getEncodedString().length();
			System.out.printf("Compression percent: %.2f%n",
					(double) COMPRESSED_BYTES / ORIGINAL_BYTES);

			// Decode string, test for equality
			String decoded = huffman.decode(data);
			System.out.println("Original equals decoded: "
					+ ORIGINAL.equals(decoded));
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
