package huffman;

public class Node implements Comparable<Node> {
	private char character;
	private int frequency;
	private Node left;
	private Node right;

	public Node() {
		this('\0', 0);
	}

	public Node(char character, int frequency) {
		setCharacter(character);
		setFrequency(frequency);
	}

	public boolean isLeaf() {
		return left == null && right == null;
	}

	public char getCharacter() {
		return character;
	}

	public void setCharacter(char character) {
		this.character = character;
	}

	public int getFrequency() {
		return frequency;
	}

	public void setFrequency(int frequency) {
		this.frequency = frequency;
	}

	public Node getLeft() {
		return left;
	}

	public void setLeft(Node left) {
		this.left = left;
	}

	public Node getRight() {
		return right;
	}

	public void setRight(Node right) {
		this.right = right;
	}

	/**
	 * Compares for ascending order
	 */
	@Override
	public int compareTo(Node arg0) {
		if (this.frequency < arg0.getFrequency()) {
			return -1;
		} else if (this.frequency > arg0.getFrequency()) {
			return 1;
		}
		return 0;
	}

}
