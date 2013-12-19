package biotechProject.types;

public class Answer implements Comparable<Answer>{
private String ans = null;
private double score = 0;
public Answer(){
	ans = null;
	score = 0;
}
public Answer(String text){
	ans = text;
	score = 0;
}
public boolean SetScore(double s){
	score = s;
	return true;
}
public boolean SetText(String text){
	ans = text;
	return true;
}
public String GetText(){
	return ans;
}
public double GetScore(){
	return score;
}
public int compareTo(Answer ans)
{
     if(score - ans.score >= 0)
          return -1;
     else return 1;
}
}

