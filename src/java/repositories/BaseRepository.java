package repositories;

import java.io.IOException;
import javax.servlet.ServletContext;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerConfigurationException;
import lib.ManageXML;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

public class BaseRepository {
    private ServletContext _context;
    protected Document doc;
    
    public BaseRepository(ServletContext context, String fileName){
        try {
            _context = context;
            ManageXML xml = new ManageXML();
            String db_path = context.getRealPath("/") + fileName ;
            doc = xml.parse(context.getResourceAsStream(db_path));
            doc.getDocumentElement().normalize();
        } catch (IOException | SAXException | TransformerConfigurationException | ParserConfigurationException ex) {
            System.out.println("errore");
        }
    }
}
