package biotechProject.scoreAlgorithms;


import java.util.ArrayList;

import biotechProject.types.Question;
import biotechProject.types.Sentence;
import biotechProject.types.Token;

public class TokenOverlap {
	
	public double tokenOverlap(Question quesion, Sentence sentence){
		
	/*	int count = 0;
		ArrayList<Token> sentenceTokens = (ArrayList<Token>) sentence.getTokenList();
		boolean flag = false;
		ArrayList<Token> questionTokens = quesion.queries;
		for(Token q: questionTokens){
		if(sentence.getTextString().indexOf(q.getText())!=-1) {
			flag = true;
			break;
		}			
		
		for (Token token : sentenceTokens){
			for(Token qToken : questionTokens){
				if (token.getText().equals(qToken.getText())) {
					count++;
				}
			}
		}
		}
		if(flag == true)
			return 99;
		else
		return 1.0*count/sentenceTokens.size();
	} */
		
		ArrayList<Token> questionTokens = quesion.getKeywordList();
		ArrayList<Token> sentenceTokens = (ArrayList<Token>) sentence.getTokenList();
		int count = 0;
		for (Token token : sentenceTokens){
			for(Token qToken : questionTokens){
				if (token.getText().equals(qToken.getText())) {
					count++;
				}
			}
		}
		return 1.0*count/sentenceTokens.size();
	}  
}
