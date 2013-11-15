package xmlParser;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;

import org.xml.sax.Attributes;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;

public class SAXClass extends DefaultHandler implements ErrorHandler {

  private Boolean pBoolean = false;

  private PrintStream out;

  private BufferedWriter writer;

  private String outputFileName;

  private int titleState = 0; // 0 is initial/false , 1 is true, -1 is disabed

  public SAXClass() {
    super();
  }

  public SAXClass(String aFileName) throws IOException {
    super();
    setOutputFileName(aFileName);
  }

  public void setOutputFileName(String aFileName) throws IOException {

    if (writer != null)
      writer.close();
    if (!aFileName.equals("")) {
      File file = new File(aFileName);
      File parent = file.getParentFile();
      if (!parent.exists() && !parent.mkdirs()) {
        throw new IllegalStateException("Couldn't create dir: " + parent);
      }
      writer = new BufferedWriter(new FileWriter(aFileName));
      // writer.write( yourstring);

      // do stuff
    }
  }

  public void startDocument() {
    titleState = 0;
    pBoolean = false;
    // System.out.println("Start document");
  }

  public void endDocument() {
    // System.out.println("End document");
  }

  public void startElement(String uri, String name, String qName, Attributes atts) {
    try {
      if ("".equals(uri)) {
        // System.out.println("Start element: " + qName);
      } else {
        // System.out.println("Start element: {" + uri + "}" + name);

      }
      if (qName == "p" || qName == "title") {
        pBoolean = true;

        writer.write("\n");
      }
      if (qName == "article-title" && titleState == 0) {
        titleState = 1;
        writer.write("<" + qName + ">");
      }

      if (qName == "abstract" || qName == "body" || qName == "floats-group") {
        writer.write("<" + qName + ">");
      }

    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }

  public void endElement(String uri, String name, String qName) {
    
      //if ("".equals(uri)) System.out.println("End element: " + qName); else
      //System.out.println("End element:   {" + uri + "}" + name);
     
    try {
      if (qName == "p" || qName == "title") {
        pBoolean = false;
        writer.write("\n");
      }
      if (qName == "abstract" || qName == "body" || qName == "floats-group") {
        writer.write("</" + qName + ">\n\n");

      }

      if (qName == "article-title" && titleState == 1) {
        writer.write("</" + qName + ">\n\n");
        titleState = -1;
      }
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }

  public void characters(char ch[], int start, int length) {
    try {
      if (pBoolean == true || titleState == 1) {
        // System.out.print("Characters:    \"");
        /*
        for (int i = start; i < start + length; i++) {
          switch (ch[i]) {
            case '\\':
              writer.write("\\\\");
              break;
            case '"':
              writer.write("\\\"");
              break;
            case '\n':
              writer.write(" ");
              break;
            case '\r':
              writer.write("\\r");
              break;
            case '\t':
              writer.write("\\t");
              break;
            default:
              writer.write(ch[i]);
              break;
          }
        }
        */
        String line = new String(ch,start,length);
        writer.write(line);
        // System.out.print("\"\n");
      }
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }

  SAXClass(PrintStream out) {
    this.out = out;
  }

  private String getParseExceptionInfo(SAXParseException spe) {
    String systemId = spe.getSystemId();

    if (systemId == null) {
      systemId = "null";
    }

    String info = "URI=" + systemId + " Line=" + spe.getLineNumber() + ": " + spe.getMessage();

    return info;
  }

  public void warning(SAXParseException spe) throws SAXException {
    out.println("Warning: " + getParseExceptionInfo(spe));
  }

  public void error(SAXParseException spe) throws SAXException {
    String message = "Error: " + getParseExceptionInfo(spe);
    throw new SAXException(message);
  }

  public void fatalError(SAXParseException spe) throws SAXException {
    String message = "Fatal Error: " + getParseExceptionInfo(spe);
    throw new SAXException(message);
  }

}