package biotechProject.collection;

import java.util.ArrayList;

import biotechProject.annotators.FindCandidateSentences;
import biotechProject.annotators.QuestionAnalysis_Claire;
import biotechProject.annotators.AnswerAnalysis_Anna;
import biotechProject.types.Question;
import biotechProject.types.Sentence;
import biotechProject.types.Answer;

public class Pipeline {

	public static void main(String[] args) throws Exception {

		Question quest = QuestionAnalysis_Claire.userInput2Question();
		
		QuestionAnalysis_Claire.printQuestionForTest(quest);
		

		for(Sentence st : candidates){
			System.out.println(st.getTextString());
			AnswerAnalysis_Anna.setCandidateAnswers(st.getTextString(), quest);
		}
		ArrayList<Answer> Acandidates = AnswerAnalysis_Anna.getCandidateAnswers();
		for(Answer ans: Acandidates){
			System.out.println(ans.GetText());
		}
	}

}