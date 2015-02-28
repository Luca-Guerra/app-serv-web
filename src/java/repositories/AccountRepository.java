package repositories;

import java.util.ArrayList;
import java.util.List;
import javax.servlet.ServletContext;
import models.Account;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class AccountRepository extends BaseRepository {
    
    public AccountRepository(ServletContext servletContext){
        super(servletContext, "accounts.xml");
    }
    
    // Fornisce l'account richiesto
    public Account GetAccount(String username){
        Account account = null;
        account = GetAccounts().stream().filter(a -> a.Username.equals(username)).reduce(account, null);
        return account;
    }
    
    public List<Account> GetAccounts(){
        List<Account> accounts = new ArrayList<>();
    
        NodeList nodeList = doc.getElementsByTagName("account");
        for (int temp = 0; temp < nodeList.getLength(); temp++) {

            Node nNode = nodeList.item(temp);

            if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                Element eElement = (Element) nNode;
                accounts.add(ShapeAccount(eElement));
            }
        }
        return accounts;
    }
    // Da forma all'elemento trovato nell'xml
    private Account ShapeAccount(Element e){
        Account account = new Account();
        account.Name        = e.getElementsByTagName("name").item(0).getTextContent();
        account.Surname     = e.getElementsByTagName("surname").item(0).getTextContent();
        account.Username    = e.getElementsByTagName("username").item(0).getTextContent();
        account.Password    = e.getElementsByTagName("password").item(0).getTextContent();
        account.Role        = e.getElementsByTagName("role").item(0).getTextContent();
        return account;
    }
}
