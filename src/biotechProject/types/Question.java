package biotechProject.types;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.io.IOException;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.HashSet;

import edu.smu.tspell.wordnet.*;
import edu.smu.tspell.wordnet.impl.file.Morphology;
import edu.smu.tspell.wordnet.impl.file.*;

public class Question {
	
	public static WordNetDatabase database = WordNetDatabase.getFileInstance();

	/**
	 * the string of input question, without question mark
	 */
	private String questionString = null;

	/**
	 * When the format of input question is "What does [ENTITY] [VERB]", then
	 * first token in keywordList will be [ENTITY], second token will be [VERB].
	 * When the format of input question is "What [VERB] [ENTITY]", then first
	 * token in keywordList will be [ENTITY], second will be [VERB]
	 */
	private ArrayList<Token> keywordList = new ArrayList<Token>();

	/**
	 * verbList stores all the forms of a verb. e.g. the verb is "regulate",
	 * then "regulates" "regulated" and "regulating" will be stored
	 */
	private ArrayList<Token> verbList = new ArrayList<Token>();

	/**
	 * nounEntityList stores all the words of a noun entity and their synonyms
	 * of the words in the ArrayList
	 */
	private ArrayList<Token> nounEntityList = new ArrayList<Token>();

	/**
	 * When input question is "What [VERB] [ENTITY]", then askSubject is true.
	 * When input question is "What does [ENTITY] [VERB]" then askSubject is
	 * false
	 */
	private boolean askSubject = true;

	/**
	 * construct the question according to the user input string. It's assumed
	 * that parameter inputStr does not contain question mark
	 */
	public Question(String inputStr) {
		this.questionString = inputStr;
		String[] questionWord = inputStr.split("\\s");

		// question format is "What does [ENTITY] [VERB]"
		if (questionWord[1].equals("does") || questionWord[1].equals("do")) {

			if (questionWord.length < 4)
				System.out.println("Not enough length of input question!");

			this.askSubject = false;

			// nounEntityList
			for (int i = 2; i < questionWord.length - 1; i++) {
				try {
					if (!(isStopWord(questionWord[i]))) {
						Token nameEntityToken = new Token(questionWord[i]);
						this.keywordList.add(nameEntityToken);

						//ArrayList<Token> allSynonymsOfOneWord = new ArrayList<Token>();
						//allSynonymsOfOneWord.add(nameEntityToken);
						// TODO ADD ALL SYNONYMS OF ONE WORD IN THE NAME ENTITY
						// IN ONE ARRAYLIST

						this.nounEntityList.add(nameEntityToken);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			// verbList
			String verbText = questionWord[questionWord.length - 1];
			this.keywordList.add(new Token(verbText));
			this.verbList.add(new Token(verbText));
			ArrayList<String> verbSynonyms = getVerbSynonyms(questionWord[questionWord.length - 1]);
			for(String word:verbSynonyms){
				if(!word.equals(questionWord[questionWord.length - 1]))
					this.verbList.add(new Token(word));
			}
			
			
		}

		// question format is "What [VERB] [ENTITY]"
		else {
			if (questionWord.length < 3)
				System.out.println("Not enough length of input question!");

			this.askSubject = true;

			// verbList
			this.keywordList.add(new Token(questionWord[1]));
			this.verbList.add(new Token(questionWord[1]));
			ArrayList<String> verbSynonyms = getVerbSynonyms(questionWord[1]);
			for(String word:verbSynonyms){
				if(!word.equals(questionWord[1]))
					this.verbList.add(new Token(word));
			}
			
			
			// nounEntityList
			for (int i = 2; i < questionWord.length; i++) {
				try {
					if (!(isStopWord(questionWord[i]))) {
						Token nameEntityToken = new Token(questionWord[i]);
						this.keywordList.add(nameEntityToken);

						//ArrayList<Token> allSynonymsOfOneWord = new ArrayList<Token>();
						//allSynonymsOfOneWord.add(nameEntityToken);
						// TODO ADD ALL SYNONYMS OF ONE WORD IN THE NAME ENTITY
						// IN ONE ARRAYLIST

						this.nounEntityList.add(nameEntityToken);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

		}
	}

	public static boolean isStopWord(String word) throws IOException {
		HashSet<String> stopWordsList = new HashSet<String>();
		URL swurl = Answer.class.getResource("stopwords.txt");
		if (swurl == null)
			throw new IllegalArgumentException("Error opening stopwords.txt");
		BufferedReader br = new BufferedReader(new InputStreamReader(
				swurl.openStream()));
		String swLine;
		while ((swLine = br.readLine()) != null)
			stopWordsList.add(swLine);
		if (stopWordsList.contains(word))
			return true;
		return false;
	}

	public static void getVerbForms() {
		ArrayList<Token> verbForms = new ArrayList<Token>();

		Morphology id = Morphology.getInstance();

		String[] arr = id.getBaseFormCandidates("apples", SynsetType.VERB);

		System.out.println(arr.length);

		for (String a : arr)
			System.out.println(a);
		// return verbForms;

	}

	

	public static ArrayList<String> getVerbSynonyms(String inputWord) {
		ArrayList<String> synonyms = new ArrayList<String>();
		ArrayList<String> trimedSynonyms = new ArrayList<String>();

		System.setProperty("wordnet.database.dir",
				"/Users/Cambi/Plugins/WordNet-3.0/dict");

		String word = inputWord;
		Synset[] synsets = database.getSynsets(word, SynsetType.VERB);
		if (synsets.length > 0) {
			for (int i = 0; i < synsets.length; i++) {
				String[] wordForms = synsets[i].getWordForms();
				
				
				for (int j = 0; j < wordForms.length; j++) {
					if (!synonyms.contains(wordForms[j])) {
						synonyms.add(wordForms[j]);
					}
				}
			}
			int outputlength = synonyms.size()>=6?6:synonyms.size();
			for(int i=0; i<outputlength; i++){
				//System.out.println(synonyms.get(i));
				trimedSynonyms.add(synonyms.get(i)); 
			}
		}
		return trimedSynonyms;
		
		
	}
	
	public static void getVerbSynonyms2(String inputWord) {
		System.setProperty("wordnet.database.dir",
				"/Users/Cambi/Plugins/WordNet-3.0/dict");
		
		NounSynset nounSynset; 
		NounSynset[] hyponyms; 

		Synset[] synsets = database.getSynsets(inputWord, SynsetType.NOUN); 
		for (int i = 0; i < synsets.length; i++) { 
		   nounSynset = (NounSynset)(synsets[i]); 
		   hyponyms = nounSynset.getHyponyms(); 
		    System.err.println(nounSynset.getWordForms()[0] + 
		           ": " + nounSynset.getDefinition() + ") has " + hyponyms.length + " hyponyms"); 
		}
		
	}
	
	public static void getVerbSynonyms3(String inputWord) {
		System.setProperty("wordnet.database.dir",
				"/Users/Cambi/Plugins/WordNet-3.0/dict");
		
		VerbSynset verbSynset; 
		VerbSynset[] hyponyms; 

		Synset[] synsets = database.getSynsets(inputWord, SynsetType.VERB); 
		for (int i = 0; i < synsets.length; i++) { 
		   verbSynset = (VerbSynset)(synsets[i]); 
		   System.out.println(verbSynset.getWordForms()[1]); 
		}
		
	}


	public String getQuestionString() {
		return this.questionString;
	}

	public ArrayList<Token> getKeywordList() {
		return this.keywordList;
	}

	public boolean getAskSubject() {
		return this.askSubject;
	}

	public ArrayList<Token> getVerbList() {
		return this.verbList;
	}

	public ArrayList<Token> getNounEntityList() {
		return this.nounEntityList;
	}

}