package controllers;

import java.io.IOException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import models.Account;
import repositories.AccountRepository;


@WebServlet(name = "AccessController", urlPatterns = {"/AccessController"})
public class AccessController extends HttpServlet {

    private final AccountRepository rep = new AccountRepository(getServletContext());
    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // il forward di default è la pagina di log-in
        String forward = "jsp/access.jsp";
        HttpSession session = request.getSession();
        if(session.getAttribute("username") != null)
            forward = GetForward(session.getAttribute("username").toString());  
        // Trovare un modo per indicare che si vuole realizzare il log-out
        if(request.getAttribute("op") != null && request.getAttribute("op").equals("log-out")){
            session.removeAttribute("username"); 
            session.invalidate(); 
        }
        RequestDispatcher dispatcher = request.getRequestDispatcher(forward);  
        dispatcher.forward(request, response); 
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = request.getParameter("username"); 
        String password = request.getParameter("password"); 
        HttpSession session = request.getSession();
        String forward = "jsp/access.jsp";
        if(username != null && password != null){
            Account account = rep.GetAccount(username);
            if(account != null && account.Password.equals(password)) { 
                session.setAttribute("username",username);
                forward = GetForward(account.Role);
            }
        }
        RequestDispatcher dispatcher = request.getRequestDispatcher(forward);  
        dispatcher.forward(request, response); 
    }

    private String GetForward(String role){
        switch(role){
            case "doctor":
                return "jsp/doctor-home.jsp";
            case "patient":
                return "jsp/patient-home.jsp";
            default:
                return "jsp/access.jsp";
        }
    } 
}
