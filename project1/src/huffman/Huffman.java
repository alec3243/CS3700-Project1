package huffman;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.PriorityQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Huffman {
	private static char ZERO = '0';
	private static char ONE = '1';
	private String fileString;
	private StringBuilder encodedString;
	private HashMap<Character, String> charCodes;
	private PriorityQueue<Node> nodes;

	Huffman(String filename) throws IOException {
		nodes = new PriorityQueue<>();
		charCodes = new HashMap<>();
		readFile(filename);
		encodedString = new StringBuilder();
	}

	private void readFile(String filename) throws IOException {
		fileString = new String(Files.readAllBytes(Paths.get(filename)));
		final int ASCII_LENGTH = 128;
		int[] frequencies = new int[ASCII_LENGTH];
		for (char c : fileString.toCharArray()) {
			frequencies[c] = frequencies[c] + 1;
		}
		int freq = 0;
		for (int i = 0; i < frequencies.length; i++) {
			freq = frequencies[i];
			if (freq != 0) {
				nodes.offer(new Node((char) i, freq));
			}
		}
	}

	public EncodedData compress() {
		Node x, y, z;
		while (nodes.size() > 1) {
			z = new Node();
			x = nodes.poll();
			z.setLeft(x);
			y = nodes.poll();
			z.setRight(y);
			z.setFrequency(x.getFrequency() + y.getFrequency());
			nodes.offer(z);
		}
		Node root = nodes.poll();
		findCodes(root, "");
		return new EncodedData(root, encode());
	}

	public String decode(EncodedData data) {
		StringBuilder sb = new StringBuilder();
		Node currentNode = data.getRoot();
		String encodedString = data.getEncodedString();
		for (int i = 0; i < encodedString.length(); i++) {
			if (encodedString.charAt(i) == ZERO) {
				currentNode = currentNode.getLeft();
			} else {
				currentNode = currentNode.getRight();
			}
			if (currentNode.isLeaf()) {
				sb.append(currentNode.getCharacter());
				currentNode = data.getRoot(); // Reset currentNode back to root
			}
		}
		return sb.toString();
	}

	private void findCodes(Node node, String code) {
		if (node.isLeaf()) {
			charCodes.put(node.getCharacter(), code);
		}
		if (node.getLeft() != null) {
			findCodes(node.getLeft(), code + ZERO);
		}
		if (node.getRight() != null) {
			findCodes(node.getRight(), code + ONE);
		}
	}

	private String encode() {
		for (int i = 0; i < fileString.length(); i++) {
			encodedString.append(charCodes.get(fileString.charAt(i)));
		}
		return encodedString.toString();
	}

}
