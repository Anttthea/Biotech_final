package biotechProject.collection;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import biotechProject.annotators.QuestionAnalysis_Claire;
import biotechProject.types.Question;
import biotechProject.types.Sentence;
import biotechProject.types.Token;

public class DocScoreTest {
	public static void main(String args[]) throws IOException {
		FileReader reader = new FileReader(
				"E:/Eclipse Projects/workspace/BioFinalProject/inputdata/goldenAnswerDocument/candidateSentenceScoring_insulin.txt");
		String line;
		ArrayList<Sentence> stList = new ArrayList<Sentence>();
		BufferedReader br = new BufferedReader(reader);
		while ((line = br.readLine())!=null) {
			Sentence st = new Sentence(line.substring(5));
			st.setSentenceScore(Integer.parseInt(line.substring(0,1)));
			stList.add(st);
		}
		int right = 0;
		int wrong = 0;
		Question quest = QuestionAnalysis_Claire.userInput2Question("");
		for(Sentence st : stList){
			int flag = 1;

			for(Token tk : quest.getKeywordList()){
				if(st.getTextString().indexOf(tk.getText())==-1){
					flag = 0;
				}
			}
			if ((flag ==1&&st.getSentenceScore()>=3)||(flag == 0&&st.getSentenceScore()<3)) {
				right ++;
				
			}else {
				wrong++;
			}
			if(flag ==1){System.out.println(st.getTextString());};
		}
		System.out.println(right*1.0/(right+wrong));
	}
}
