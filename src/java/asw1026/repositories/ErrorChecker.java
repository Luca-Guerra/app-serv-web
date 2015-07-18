/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package asw1026.repositories;

import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

/**
 *
 * @author Riccardo
 */
public class ErrorChecker implements ErrorHandler{

    @Override
    public void warning(SAXParseException exception) throws SAXException {
        throw new UnsupportedOperationException("Warning."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void error(SAXParseException exception) throws SAXException {
        throw new UnsupportedOperationException("Error."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void fatalError(SAXParseException exception) throws SAXException {
        throw new UnsupportedOperationException("Fatal Error."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
