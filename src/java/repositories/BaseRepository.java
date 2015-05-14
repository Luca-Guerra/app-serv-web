package repositories;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import javax.servlet.ServletContext;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import lib.ManageXML;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

public class BaseRepository {
    protected Document doc;
    ManageXML xml;
    ServletContext context;
    String fileName;
    public BaseRepository(ServletContext context, String fileName){
        try {
            this.context=context;
            this.fileName = fileName;
            xml = new ManageXML();
            // ottengo il Document del file xml
            System.out.println(context.getContextPath()+"/"+fileName);
            
            doc = xml.parse(context.getResourceAsStream("/" + fileName));
            doc.getDocumentElement().normalize();
        } catch (IOException | SAXException | TransformerConfigurationException | ParserConfigurationException ex) {
            System.out.println("errore:"+ex.getMessage());
        }
    }
    
    public void writeRepository() throws TransformerException, IOException{
        String filePath = context.getRealPath(fileName);
        OutputStream os = new FileOutputStream(filePath);
        System.out.println("prima");
        xml.transform(os, doc);
        System.out.println("dopo");
        os.flush();
        os.close(); 
        System.out.println("fine");
    }
    
}
