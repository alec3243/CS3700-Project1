package huffman;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.PriorityQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Huffman {
	private static int THREAD_COUNT = 4;
	private static ExecutorService executor1 = Executors
			.newFixedThreadPool(THREAD_COUNT);
	private static ExecutorService executor2 = Executors
			.newFixedThreadPool(THREAD_COUNT);
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
		long start = System.nanoTime();
		fileString = new String(Files.readAllBytes(Paths.get(filename)));
		final int ASCII_LENGTH = 128;
		int[] frequencies = new int[ASCII_LENGTH];
		char[] chars = fileString.toCharArray();
		int startIndex = 0;
		final int FILE_SIZE_DIVISION = (int) Math.ceil((double) fileString
				.length() / 4);
		int finishIndex = FILE_SIZE_DIVISION;
		for (int i = 0; i < THREAD_COUNT; i++) {
			executor1.execute(new Parser(startIndex, finishIndex, chars,
					frequencies));
			startIndex += FILE_SIZE_DIVISION;
			finishIndex = startIndex + FILE_SIZE_DIVISION;
		}
		executor1.shutdown();
		while (!executor1.isTerminated()) {
			Thread.yield();
		}
		int freq = 0;
		for (int i = 0; i < frequencies.length; i++) {
			freq = frequencies[i];
			if (freq != 0) {
				nodes.offer(new Node((char) i, freq));
			}
		}
		System.out.printf("Time to find frequencies: %d ns%n",
				System.nanoTime() - start);
	}

	public EncodedData compress() {
		Node x, y, z;
		long start = System.nanoTime();
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
		System.out.printf("Time to construct tree: %d ns%n", System.nanoTime()
				- start);
		findCodes(root, "");
		start = System.nanoTime();
		encode();
		System.out.printf("Time to encode: %d ns%n", System.nanoTime() - start);
		return new EncodedData(root, encodedString.toString());
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

	private void encode() {
		StringBuilder[] threadResults = new StringBuilder[THREAD_COUNT];
		for (int i = 0; i < threadResults.length; i++) {
			threadResults[i] = new StringBuilder();
		}
		int startingIndex = 0;
		final int FILE_LENGTH_DIVIDED = (int) Math.ceil((double) fileString
				.length() / THREAD_COUNT);
		int finishIndex = FILE_LENGTH_DIVIDED;
		for (int i = 0; i < THREAD_COUNT; i++) {
			executor2.execute(new Encoder(threadResults[i], startingIndex,
					finishIndex, fileString, charCodes));
			startingIndex += FILE_LENGTH_DIVIDED;
			finishIndex = startingIndex + FILE_LENGTH_DIVIDED;
		}
		executor2.shutdown();
		while (!executor2.isTerminated()) {
			Thread.yield();
		}
		for (StringBuilder sb : threadResults) {
			encodedString.append(sb.toString());
		}
	}

	static class Parser implements Runnable {
		int startIndex;
		int finishIndex;
		char[] chars;
		int[] frequencies;

		Parser(int startIndex, int finishIndex, char[] chars, int[] frequencies) {
			this.startIndex = startIndex;
			this.finishIndex = finishIndex;
			this.chars = chars;
			this.frequencies = frequencies;
		}

		@Override
		public void run() {
			for (int i = startIndex; i < finishIndex; i++) {
				if (i >= chars.length) {
					break;
				}
				frequencies[chars[i]] = frequencies[chars[i]] + 1;
			}

		}

	}

	static class Encoder implements Runnable {
		StringBuilder result;
		int startingIndex;
		int finishIndex;
		String data;
		HashMap<Character, String> charCodes;

		Encoder(StringBuilder result, int startingIndex, int finishIndex,
				String data, HashMap<Character, String> charCodes) {
			this.result = result;
			this.startingIndex = startingIndex;
			this.finishIndex = finishIndex;
			this.data = data;
			this.charCodes = charCodes;
		}

		@Override
		public void run() {
			for (int i = startingIndex; i < finishIndex; i++) {
				if (i >= data.length()) {
					break;
				}
				result.append(charCodes.get(data.charAt(i)));
			}
		}
	}
}
