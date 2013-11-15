package biotechProject.annotators;

import indexer.SearchFiles;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import biotechProject.types.Question;
import biotechProject.types.Sentence;
import biotechProject.types.Token;

public class FindCandidateSentences {

	private static ArrayList<Sentence> candidates = new ArrayList<Sentence>();
	private static FileReader fr = null;
	private static BufferedReader br = null;

	// Written by Soumya
	public static ArrayList<Sentence> getCandidateSentences(Question q)
			throws Exception {

		String line;
		String space;
		int i;
		double confidence = 0.0;

		String body = "";
		String temp = new String();

		ArrayList<Token> verbs = q.getVerbList();
        ArrayList<Token> entity = q.getNounEntityList();

		String query = "";
		
	/*	for(Token t:verbs)
			query += (" " + t.getText() + " OR");
		
		query = query.substring(0,query.length()-2); */
		
		query += "\"";
		
		for(Token t:entity)
			query += (" " + t.getText());
		
		query += "\"";


		// Only entity names must be added to the query since querying based on
		// verb is useless

		String[] docPaths = SearchFiles.paths(query);
		

		// Since the documents are obtained in ordered fashion based on number
		// of hits, we find the candidate answers in the first 100 documents. If
		// no result is found, we move to the next 100 and so on
		int start = 0;
		int end = Math.min(docPaths.length, 5);
		for (int j = start; j < end; j++) {
			String file = docPaths[j];
			fr = new FileReader(file);
			br = new BufferedReader(fr);

			while ((line = br.readLine()) != null) {
				if (line.startsWith("<article-title>")) {
					space = line.substring(15);
					if ((i = line.indexOf("</article-title>")) != -1) {
						temp = line;
						line = br.readLine();
					} else {
						while ((i = line.indexOf("</article-title>")) == -1) {
							line = br.readLine();
							space += line;
							temp = line;
						}
					}
					space = space.substring(0, space.length() - 16);

					findCandidates(candidates, space, q, 'T');
					space = "";
				}

				else if (line.startsWith("<abstract>")) {
					space = line.substring(10);
					if ((i = line.indexOf("</abstract>")) != -1) {
						temp = line;
						line = br.readLine();
					} else {
						while ((i = line.indexOf("</abstract>")) == -1) {
							line = br.readLine();
							space += line;
							temp = line;
						}
					}
					space = space.substring(0, space.length() - 11);

					findCandidates(candidates, space, q, 'A');
					space = "";
				}
				// break; }
				else if (line.startsWith("<body>")) {
					while (line.indexOf("</body>") == -1) {
						body += line;
						line = br.readLine();
					}
					break;
				}
			}
			getCandidateFromBody(body, q);
		}
		br.close();
		return candidates;
	}

	private static void findCandidates(ArrayList<Sentence> candidates,
			String find, Question q, char type) {
		// TODO Auto-generated method stub

		//ArrayList<Token> tokens = q.getKeywordList();
		
		ArrayList<Token> tokens = q.getVerbList();
		tokens.addAll(q.getNounEntityList());
		
		ArrayList<Integer> labels = new ArrayList<Integer>();
		boolean subject = q.getAskSubject();
		String perfect = "";
		int i = 0, j = 0;
		double score = 0.0;

		for (i = 0; i < tokens.size(); i++)
			perfect += tokens.get(i);

		StringTokenizer st = new StringTokenizer(find, ".!?", true);

		while (st.hasMoreTokens()) {
			i = 0;
			j = 0;
			score = 0.0;
			Sentence s = new Sentence(st.nextToken());

			ArrayList<Token> st2 = (ArrayList<Token>) s.getTokenList();

			// Code for finding exact match has to be written
			/*
			 * for(int l = 0; l < st2.size(); l++) { for(int k = 0; k <
			 * tokens.size(); k++) {
			 * if(tokens.get(k).equals(st2.get(l).toString())) { i++;
			 * labels.add(l); } } j++; }
			 */
			for (Token t : st2) {
				for (Token t1 : tokens) {
					if (t1.getText().equalsIgnoreCase(t.getText()))
						i++;
				}

				j++;
			}

			if (i > 0) {
				score = (double) i / j;
				/*
				 * switch(type) { case 'T' : score = score* break; case 'A' :
				 * score = score; break; }
				 */
				s.setSentenceScore(score);
				candidates.add(s);
			}

		}

	}

	// Written by Haodong
	private static void getCandidateFromBody(String body, Question q) {
		StringTokenizer st = new StringTokenizer(body, ".!?");
		while (st.hasMoreTokens()) {
			Sentence s = new Sentence(st.nextToken());
			int flag = 1;
			for (Token token : q.getKeywordList()) {
				if (s.getTextString().indexOf(token.getText()) < 0) {
					flag = 0;
				}
			}
			if (flag == 1) {
				candidates.add(s);
			}
		}
	}

}
