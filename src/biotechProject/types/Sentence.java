//Modified By Haodong 
package biotechProject.types;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.StringTokenizer;

public class Sentence implements Comparator<Sentence>, Comparable<Sentence> {
	ArrayList<Token> tokenList;
	private double sentenceScore;
	private String textString;

	public Sentence(String text) {
		textString = text;
		sentenceScore = 0;
		tokenList = new ArrayList<Token>();
		// break down input string into tokens
		StringTokenizer tokenizer = new StringTokenizer(text);
		while (tokenizer.hasMoreTokens()) {
			Token token = new Token(tokenizer.nextToken());
			tokenList.add(token);
		}
	}

	public void setSentenceScore(double score) {
		sentenceScore = score;
	}

	public double getSentenceScore() {
		return sentenceScore;
	}

	public String getTextString() {
		return textString;
	}

	public ArrayList<Token> getTokenList() {
		return tokenList;
	}

	@Override
	public int compareTo(Sentence o) {
		// TODO Auto-generated method stub
		if (this.sentenceScore > o.sentenceScore) {
			return -1;
		}
		else if (this.sentenceScore == o.sentenceScore) {
			return 0;
		}
		else {
			return 1;
		}
	}

	@Override
	public int compare(Sentence o1, Sentence o2) {
		// TODO Auto-generated method stub
		if (o1.getSentenceScore()>o2.getSentenceScore()) {
			return 1;
		}
		else if (o1.getSentenceScore() == o2.getSentenceScore()) {
			return 0;
		}
		else {
			return -1;
		}
	}
}
