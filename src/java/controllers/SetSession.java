/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Riccardo
 */
@WebServlet(name = "SetSession", urlPatterns = {"/SetSession"})
public class SetSession extends HttpServlet{
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String attribute = request.getParameter("attribute");
        String value = request.getParameter("value");
        HttpSession session = request.getSession();
        session.setAttribute(attribute, value);
        //String forward = "jsp/conversation.jsp";
        //response.sendRedirect(request.getContextPath() +"/"+ forward);
    }
}
