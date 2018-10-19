package huffman;

public class HuffmanCharacter {
	private char character;
	private int frequency;
	private String codeword;

	public HuffmanCharacter(char character, int frequency, String codeword) {
		setCharacter(character);
		setFrequency(frequency);
		setCodeword(codeword);
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

	public String getCodeword() {
		return codeword;
	}

	public void setCodeword(String codeword) {
		this.codeword = codeword;
	}

}
