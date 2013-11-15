//Modified By Haodong
package biotechProject.types;

public class Token {
	String textString;
	
	public Token(String textString){
		this.textString = textString;
	}
	
	public String getText(){
		return textString;
	}
	
	public void setText(String s){
		this.textString = s;
	}
}
