package repositories;

import java.io.IOException;
import javax.servlet.ServletContext;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerConfigurationException;
import lib.ManageXML;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

public class BaseRepository {
    protected Document doc;
    
    public BaseRepository(ServletContext context, String fileName){
        try {
            ManageXML xml = new ManageXML();
            // ottengo il Document del file xml
            doc = xml.parse(context.getResourceAsStream("/" + fileName));
            doc.getDocumentElement().normalize();
        } catch (IOException | SAXException | TransformerConfigurationException | ParserConfigurationException ex) {
            System.out.println("errore");
        }
    }
}
