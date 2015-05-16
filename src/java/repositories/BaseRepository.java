package repositories;

import java.io.File;
import java.io.FileInputStream;
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
            System.out.println("CONTEXT="+context.getContextPath()+"/"+fileName);
            // ottengo il Document del file xml
            context.getResourceAsStream("/"+fileName).reset();
            doc = xml.parse(context.getResourceAsStream("/" + fileName));
            context.getResourceAsStream("/"+fileName).close();
            doc.getDocumentElement().normalize();
        } catch (IOException | SAXException | TransformerConfigurationException | ParserConfigurationException ex) {
            System.out.println("errore:"+ex.getMessage());
        }
    }
    
    public void writeRepository() throws TransformerException, IOException{
        String filePath;
        filePath = context.getRealPath(fileName);
        OutputStream os = new FileOutputStream(filePath);
        xml.transform(os, doc);
        os.flush();
        os.close(); 
    }
    
}
