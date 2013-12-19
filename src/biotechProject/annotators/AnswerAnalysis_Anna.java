package biotechProject.annotators;

import java.util.ArrayList;











//import edu.stanford.nlp.tagger.maxent.MaxentTagger;
import biotechProject.types.Question;
//import biotechProject.types.Sentence;
//import biotechProject.types.Token;
import biotechProject.types.Answer;



import biotechProject.types.Token;








//import java.io.*;
import java.util.*;

import edu.stanford.nlp.tagger.maxent.MaxentTagger;

//import edu.stanford.nlp.dcoref.CorefChain;
//import edu.stanford.nlp.dcoref.CorefCoreAnnotations.CorefChainAnnotation;
//import edu.stanford.nlp.ling.CoreAnnotations.NamedEntityTagAnnotation;
//import edu.stanford.nlp.ling.CoreAnnotations.PartOfSpeechAnnotation;
////import edu.stanford.nlp.io.*;
////import edu.stanford.nlp.ling.*;
////import edu.stanford.nlp.ling.CoreAnnotations.NamedEntityTagAnnotation;
////import edu.stanford.nlp.ling.CoreAnnotations.PartOfSpeechAnnotation;
//import edu.stanford.nlp.ling.CoreAnnotations.SentencesAnnotation;
//import edu.stanford.nlp.ling.CoreAnnotations.TextAnnotation;
//import edu.stanford.nlp.ling.CoreAnnotations.TokensAnnotation;
//import edu.stanford.nlp.ling.CoreLabel;
//import edu.stanford.nlp.ling.IndexedWord;
////import edu.stanford.nlp.ling.CoreAnnotations.TextAnnotation;
////import edu.stanford.nlp.ling.CoreAnnotations.TokensAnnotation;
//import edu.stanford.nlp.pipeline.*;
//import edu.stanford.nlp.semgraph.SemanticGraph;
//import edu.stanford.nlp.semgraph.SemanticGraphCoreAnnotations;
////import edu.stanford.nlp.semgraph.SemanticGraphCoreAnnotations;
//import edu.stanford.nlp.semgraph.SemanticGraphCoreAnnotations.CollapsedCCProcessedDependenciesAnnotation;
//import edu.stanford.nlp.trees.*;
//import edu.stanford.nlp.trees.TreeCoreAnnotations.TreeAnnotation;
//import edu.stanford.nlp.util.*;

public class AnswerAnalysis_Anna {
	private static List<Answer> Acandidates = new ArrayList<Answer>();
public static List<Answer> setCandidateAnswers(String Sentence,Question q){
	       // Initialize the tagger
	
	 
			MaxentTagger tagger = new MaxentTagger(
			 "tagger/english-left3words-distsim.tagger");
			// The sample string
			 
			//String sample = "Surprisingly, this mutant protein, which does not interact with the mitochondrial antiapoptotic protein Bcl-xL, localizes to mitochondria but does not induce apoptosis";
			 /**Question Entity Pos Tag**/
			ArrayList<Token> entity = q.getNounEntityList();
			String entString = new String();
			ArrayList<String> entNoun = new ArrayList<String>();
			ArrayList<String>  entElse = new ArrayList<String>();
			for(int j = 0;j<entity.size();j++) {
			  entString = entity.get(j).getText();
			String entagString = tagger.tagString(entString);
			String text = entagString.substring(0, entagString.indexOf("_"));
			String pos = entagString.substring(entagString.indexOf("_")+1,entagString.length());
			entity.get(j).setPos(pos);
			if(pos.startsWith("NN")) entNoun.add(text);
			else entElse.add(text);
			}
			// The tagged string
			 String[] sentences = Sentence.split("[,;]");
			 //::TODO consider multiple clause sentences situation
			 int chosen = 0;
			 for(int cnt = 0; cnt < sentences.length; cnt++)
			   //sentence chosen if contains any noun in query nounEntity
			   for(int cnt1 = 0; cnt1 < entNoun.size();cnt1++)
			   if(sentences[cnt].contains(entNoun.get(cnt1))) {
			     chosen = cnt;
			     break;
			   }
			 Sentence = sentences[chosen];
			 Sentence = Sentence.toLowerCase();
			String tagged = tagger.tagString(Sentence);
			String[] result = tagged.split("\\s");
			boolean qtype = q.getAskSubject();
			int ansBegin0 = 0;
			boolean isAttributive = false;
		     for (int x=0; x<result.length; x++){
		      isAttributive = false;
		     //  System.out.println(result[x]);
		    	 String tok = result[x];
		       String pos = tok.substring(tok.indexOf("_")+1,tok.length());
	        //  System.out.println("pos: "+pos);
		       if(pos.startsWith("V")){
		         String text = tok.substring(0, tok.indexOf("_"));
		          // System.out.println("text: "+text);
		           int vpos = Sentence.indexOf(text);
		           //  System.out.println(vpos);
             if(x > 0){
               String tok0 = result[x-1];
               String pos0 = tok0.substring(tok0.indexOf("_")+1,tok0.length());
               if(pos0.startsWith("PRP")){
                 isAttributive = true;
                 ansBegin0 = vpos+text.length();
               }
             }
             if(!isAttributive){
		         double score0 = 0;
		         double score1 = 0;
		         double score2 = 0;
		         double score3 = 0;
		         ArrayList<Token> verbs = q.getVerbList();
		         for(int d = 0;d < verbs.size();d++)
		           if(text.equals(verbs.get(d).getText())) score0 = 3;
		         //base on the assumption that each candidate sentence contains keyword entNouns
		         for(int k = 0;k<entNoun.size();k++)
		           if(Sentence.indexOf(entNoun.get(k)) < vpos) score1++;
		           else score1--;
		         for(int h = 0; h < entElse.size(); h++){
		            String eString = entElse.get(h);
		           if(Sentence.contains(eString)){ 
		             score2++;
		             double diff = vpos - Sentence.indexOf(eString);
		             if(Math.signum(diff) == Math.signum(score1)) score2++;
		           }
		         }
		         /**differentiate 2 types of questions**/
		         boolean isPassive = false;
		         int l1= 0;
		         int l2 = 0;
		         if(x+2 < result.length){
		         String tok1 = result[x+1];
		         String text1 = tok1.substring(0, tok1.indexOf("_"));
		         String pos1 = tok1.substring(tok1.indexOf("_")+1,tok1.length());
		         String tok2 = result[x+2];
		         String text2 = tok2.substring(0, tok2.indexOf("_"));
             String pos2 = tok2.substring(tok2.indexOf("_")+1,tok2.length());
             if(pos2.startsWith("IN")) 
               if(pos1.startsWith("JJ") || pos1.startsWith("V"))
               isPassive = true;
             l1 = text1.length();
             l2 = text2.length();
		         }
		         int ansBegin = vpos+text.length();
		         if(isPassive){ 
		            ansBegin =ansBegin + l1 + l2 +2;
		                    x = x+2;
		         }
		         String answerText = new String();
		         if(score1 < 0) {
		           if(vpos > 0)
		           answerText = Sentence.substring(ansBegin0,vpos);
		           else answerText = text;
		          }
		         else answerText = Sentence.substring(ansBegin,Sentence.length());
		         if(qtype){
		           if(isPassive != (score1<0)) score3 = 8;
		         }else {
//		           String aString = new String();
//		           if(isPassive) aString = "T";
//		           else aString = "F";
//		           System.out.println("Passive: "+aString);
//		           System.out.println("Score1: "+score1);
              if(isPassive == (score1<0)) score3 = 8;
            }
		         if(answerText.length()>1){
		         //System.out.println("AnswerText: "+answerText);
		         Answer answer = new Answer(answerText);
		         double score = (Math.abs(score1)+score0+score2+score3)/(entNoun.size()+entElse.size()*2+3+8);
		         answer.SetScore(score);
		         Acandidates.add(answer);
		         }
             }
		       }
//		    	 if (tok.endsWith("_NN") || tok.endsWith("_NNS")){
//		    		     String entity = q.getKeywordList().get(1).getText();
//		    			 if(!tok.startsWith(entity)){
//		    				    Answer ans = new Answer(tok.substring(0, tok.indexOf("_")));
//		    				    ans.SetScore(1);
//		    				Acandidates.add(ans);    
//	                  }
//		    		 
//		    	 }
		     }
//         /**Question Entity Pos Tag**/
//      Properties p = new Properties();
//      p.put("annotators", "tokenize, ssplit, pos");
//      StanfordCoreNLP pipe = new StanfordCoreNLP(p);
//		     /**Candidate Sentence Standford CoreNLP Parsing**/
//		  // creates a StanfordCoreNLP object, with POS tagging, lemmatization, NER, parsing, and coreference resolution 
//		     Properties props = new Properties();
//		     props.put("annotators", "tokenize, ssplit, pos, lemma, ner, parse, dcoref");
//		     StanfordCoreNLP pipeline = new StanfordCoreNLP(props);
//		     
//		     // read some text in the text variable
//		     String text = Sentence.replaceAll("[^a-zA-Z0-9\\s]", "").toLowerCase();// Add your text here!
//		     ArrayList<Token> verbs = q.getVerbList();
//	        ArrayList<Token> entity = q.getNounEntityList();
//		     System.out.println(text);
//		     // create an empty Annotation just with the given text
//		     Annotation document = new Annotation(text);
//		     
//		     // run all Annotators on this text
//		     pipeline.annotate(document);
//		     
//		     // these are all the sentences in this document
//		     // a CoreMap is essentially a Map that uses class objects as keys and has values with custom types
//		     List<CoreMap> sentences = document.get(SentencesAnnotation.class);
//		     /**Tokens in the candidate sentence**/
////		     ArrayList<Token>  toks = new ArrayList<Token>();
//		     for(CoreMap sentence: sentences) {
////		       // traversing the words in the current sentence
////		       // a CoreLabel is a CoreMap with additional token-specific methods
////		       for (CoreLabel token: sentence.get(TokensAnnotation.class)) {
////		         // this is the text of the token
////		         String word = token.get(TextAnnotation.class);
////		         System.out.println(word);
////		         Token tok = new Token(word);
////		         // this is the POS tag of the token
////		         String pos = token.get(PartOfSpeechAnnotation.class);
////		         System.out.println(pos);
////		         tok.setPos(pos);
////		         // this is the NER label of the token
////		         String ne = token.get(NamedEntityTagAnnotation.class);
////		         System.out.println(ne);
////		         tok.setNer(ne);
////		         toks.add(tok);
////		       }
//
////		       // this is the parse tree of the current sentence
////		       Tree tree = sentence.get(TreeAnnotation.class);
////
////		       // this is the Stanford dependency graph of the current sentence
////		       System.out.println("The first sentence parse tree is:");
////		       tree.pennPrint();
//		     
//		       System.out.println("The first sentence basic dependencies are:"); 
//		       SemanticGraph graph =sentence.get(SemanticGraphCoreAnnotations.BasicDependenciesAnnotation.class);
//		       IndexedWord word = new IndexedWord();
//		       
//		   //    graph.
////		       System.out.println("The first sentence collapsed, CC-processed dependencies are:");
////		       SemanticGraph graph = sentence.get(SemanticGraphCoreAnnotations.CollapsedCCProcessedDependenciesAnnotation.class);
//		       //System.out.println(graph.(graph.getFirstRoot()).toString());
//		       //System.out.println(graph.getParent(graph.getFirstRoot()).toString());
//		     
//		       String semaG = graph.toString("plain");
//		       String[] dep = semaG.split("/t");
//		       for(int i =0; i < dep.length;i++) System.out.println(dep[i]);
//		     }
//
//		     // This is the coreference link graph
//		     // Each chain stores a set of mentions that link to each other,
//		     // along with a method for getting the most representative mention
//		     // Both sentence and token offsets start at 1!
////		     Map<Integer, CorefChain> graph = 
////		       document.get(CorefChainAnnotation.class);
		    
	return Acandidates;
}
public static void AnswerScoreDescendingSort(){
  if(!Acandidates.isEmpty()){
   Collections.sort(Acandidates);
  }
}
public static void clearAcandidates() {
  Acandidates.clear();
}
public static List<Answer> getCandidateAnswers(){
	return Acandidates;
}
}
