package huffman;

public class EncodedData {
	private Node root;
	private String encodedString;

	EncodedData(Node root, String encodedString) {
		setRoot(root);
		setEncodedString(encodedString);
	}

	public Node getRoot() {
		return root;
	}

	public void setRoot(Node root) {
		this.root = root;
	}

	public String getEncodedString() {
		return encodedString;
	}

	public void setEncodedString(String encodedString) {
		this.encodedString = encodedString;
	}

}
