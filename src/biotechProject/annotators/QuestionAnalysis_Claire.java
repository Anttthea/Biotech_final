package biotechProject.annotators;

import java.util.ArrayList;
import java.util.Scanner;
import biotechProject.types.Question;
import biotechProject.types.Token;

public class QuestionAnalysis_Claire {

	public static Question userInput2Question(String qString) {

		//Scanner sc = new Scanner(System.in);

		String inputLine = qString;
		
		if(inputLine.charAt(inputLine.length()-1) == '?' ||
				inputLine.charAt(inputLine.length()-1) == '.' ||
				inputLine.charAt(inputLine.length()-1) == '!' )
			inputLine = inputLine.substring(0, inputLine.length() - 1);

		Question aQuestion = new Question(inputLine);

		return aQuestion;

	}

	public static void printQuestionForTest(Question q) {
		System.out.println("print tokens in keywordList one by one:");
		for(Token word:q.getKeywordList()){
			System.out.print(word.getText());
			System.out.print(" ");
		}
		System.out.println("\n");
		
		System.out.println("\n print tokens in verbList one by one");
		for(Token word:q.getVerbList()){
			System.out.print(word.getText());
			System.out.print(" ");
		}
		System.out.println("\n");

		System.out.println("\n print each word in the nounEntityList one by one ");
			for(Token word:q.getNounEntityList()){
				System.out.print(word.getText());
				System.out.print(" ");
			}
			System.out.println("\n");
		
		
		
	}
	
	

}