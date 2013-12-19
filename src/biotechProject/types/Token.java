//Modified By Haodong
//Semantic & POS Attribute Added by Anna
package biotechProject.types;

public class Token {
	String textString;
	/** Part of Speech(Standford NLP Standard) **/
	String posString;
	/** NameEntityTag **/
	String Ner;
	public Token(String textString){
		this.textString = textString;
	}
	
	public String getText(){
		return textString;
	}
	
	public void setText(String s){
		this.textString = s;
	}
	public void setPos(String p){
	  this.posString = p; 
	}
	public String getPos(){
	  return posString;
	}
	public void setNer(String n) {
    this.Ner = n;
  }
	public String getNer() {
    return Ner;
  }
}
