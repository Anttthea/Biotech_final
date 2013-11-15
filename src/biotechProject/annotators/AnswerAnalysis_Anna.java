package biotechProject.annotators;

import java.util.ArrayList;


//import edu.stanford.nlp.tagger.maxent.MaxentTagger;
import biotechProject.types.Question;
//import biotechProject.types.Sentence;
//import biotechProject.types.Token;
import biotechProject.types.Answer;


//import java.io.*;
import java.util.*;

import edu.stanford.nlp.dcoref.CorefChain;
import edu.stanford.nlp.dcoref.CorefCoreAnnotations.CorefChainAnnotation;
//import edu.stanford.nlp.io.*;
//import edu.stanford.nlp.ling.*;
//import edu.stanford.nlp.ling.CoreAnnotations.NamedEntityTagAnnotation;
//import edu.stanford.nlp.ling.CoreAnnotations.PartOfSpeechAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.SentencesAnnotation;
//import edu.stanford.nlp.ling.CoreAnnotations.TextAnnotation;
//import edu.stanford.nlp.ling.CoreAnnotations.TokensAnnotation;
import edu.stanford.nlp.pipeline.*;
import edu.stanford.nlp.semgraph.SemanticGraph;
import edu.stanford.nlp.semgraph.SemanticGraphCoreAnnotations;
//import edu.stanford.nlp.semgraph.SemanticGraphCoreAnnotations;
import edu.stanford.nlp.semgraph.SemanticGraphCoreAnnotations.CollapsedCCProcessedDependenciesAnnotation;
import edu.stanford.nlp.trees.*;
import edu.stanford.nlp.trees.TreeCoreAnnotations.TreeAnnotation;
import edu.stanford.nlp.util.*;

public class AnswerAnalysis_Anna {
	private static ArrayList<Answer> Acandidates = new ArrayList<Answer>();
public static ArrayList<Answer> setCandidateAnswers(String Sentence,Question q){
//	       // Initialize the tagger
//	 
//			MaxentTagger tagger = new MaxentTagger(
//			 "tagger/english-left3words-distsim.tagger");
//			// The sample string
//			 
//			//String sample = "Surprisingly, this mutant protein, which does not interact with the mitochondrial antiapoptotic protein Bcl-xL, localizes to mitochondria but does not induce apoptosis";
//			 
//			// The tagged string
//			 
//			String tagged = tagger.tagString(Sentence.toLowerCase());
//			 
//			String[] result = tagged.split("\\s");
//		     for (int x=0; x<result.length; x++){
//		         System.out.println(result[x]);
//		    	 String tok = result[x];
//		    	 if (tok.endsWith("_NN") || tok.endsWith("_NNS")){
//		    		     String entity = q.getKeywordList().get(1).getText();
//		    			 if(!tok.startsWith(entity)){
//		    				    Answer ans = new Answer(tok.substring(0, tok.indexOf("_")));
//		    				    ans.SetScore(1);
//		    				Acandidates.add(ans);    
//	                  }
//		    		 
//		    	 }
//		     }
		     /**Standford CoreNLP Parsing**/
		  // creates a StanfordCoreNLP object, with POS tagging, lemmatization, NER, parsing, and coreference resolution 
		     Properties props = new Properties();
		     props.put("annotators", "tokenize, ssplit, pos, lemma, ner, parse, dcoref");
		     StanfordCoreNLP pipeline = new StanfordCoreNLP(props);
		     
		     // read some text in the text variable
		     String text = Sentence.toLowerCase();// Add your text here!
		     
		     // create an empty Annotation just with the given text
		     Annotation document = new Annotation(text);
		     
		     // run all Annotators on this text
		     pipeline.annotate(document);
		     
		     // these are all the sentences in this document
		     // a CoreMap is essentially a Map that uses class objects as keys and has values with custom types
		     List<CoreMap> sentences = document.get(SentencesAnnotation.class);
		     
		     for(CoreMap sentence: sentences) {
		       // traversing the words in the current sentence
		       // a CoreLabel is a CoreMap with additional token-specific methods
//		       for (CoreLabel token: sentence.get(TokensAnnotation.class)) {
//		         // this is the text of the token
//		         String word = token.get(TextAnnotation.class);
//		         // this is the POS tag of the token
//		         String pos = token.get(PartOfSpeechAnnotation.class);
//		         // this is the NER label of the token
//		         String ne = token.get(NamedEntityTagAnnotation.class);       
//		       }

		       // this is the parse tree of the current sentence
		       Tree tree = sentence.get(TreeAnnotation.class);

		       // this is the Stanford dependency graph of the current sentence
		       System.out.println("The first sentence parse tree is:");
		       tree.pennPrint();
		  
		       System.out.println("The first sentence basic dependencies are:"); 
		       System.out.println(sentence.get(SemanticGraphCoreAnnotations.BasicDependenciesAnnotation.class).toString("plain"));
		       System.out.println("The first sentence collapsed, CC-processed dependencies are:");
		       SemanticGraph graph = sentence.get(SemanticGraphCoreAnnotations.CollapsedCCProcessedDependenciesAnnotation.class);
		    
		       //System.out.println(graph.(graph.getFirstRoot()).toString());
		       //System.out.println(graph.getParent(graph.getFirstRoot()).toString());
		       System.out.println(graph.toString("plain"));
		     }

		     // This is the coreference link graph
		     // Each chain stores a set of mentions that link to each other,
		     // along with a method for getting the most representative mention
		     // Both sentence and token offsets start at 1!
//		     Map<Integer, CorefChain> graph = 
//		       document.get(CorefChainAnnotation.class);
		    
	return Acandidates;
}
public static ArrayList<Answer> getCandidateAnswers(){
	return Acandidates;
}
}
