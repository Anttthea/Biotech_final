package xmlParser;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.helpers.XMLReaderFactory;

public class Processor {

  private static String FILEPATH = "/Users/Cambi/Documents/Program/EclipseWorkspace/JessicaWhiteWorkfolder";

  static int fileCount = 0;

  public static void main(String[] args) throws SAXException, IOException {
    //listf("/Users/Cambi/Documents/Program/EclipseWorkspace/JessicaWhiteWorkfolder/SourceNXML/Ecol_Entomol");
    listf(FILEPATH + "/SourceNXML");
    

  }

  public static void listf(String directoryName) throws SAXException, IOException {
    File directory = new File(directoryName);

    XMLReader xr = XMLReaderFactory.createXMLReader();
    SAXClass handler = new SAXClass();
    xr.setContentHandler(handler);
    xr.setErrorHandler(handler);
    // xr.setFeature("http://xml.org/sax/features/validation", false);
    // xr.setFeature("http://xml.org/sax/features/resolve-dtd-uris", false);

    xr.setEntityResolver(new EntityResolver() {
      public InputSource resolveEntity(String publicId, String systemId) {
        InputSource is = new InputSource(new ByteArrayInputStream("<?xml version='1.0' encoding='ISO-8859-1'?>"
                .getBytes()));
        //is.setEncoding("ISO-8859-1");
        return is;
      }
    });

    // get all the files from a directory
    File[] fList = directory.listFiles();
    for (File file : fList) {
      if (file.isFile() && file.getPath().endsWith(".nxml")) {
        fileCount++;
        //System.out.println(file.getPath());
        FileReader r = new FileReader(file.getPath());

        
        String outputFileName = FILEPATH + "/Parsedtxt"+file.getPath().substring(FILEPATH.length() + 11, file.getPath().length()-5) + ".txt";
        if (fileCount % 100 == 0) System.out.println(fileCount + " file, "+ outputFileName);
        handler.setOutputFileName(outputFileName);
       
        xr.parse(new InputSource(r));
        

      } else if (file.isDirectory()) {
        listf(file.getAbsolutePath());
      }
    }
    handler.setOutputFileName("");
  }

}
