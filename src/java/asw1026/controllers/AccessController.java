package asw1026.controllers;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import asw1026.models.Account;
import asw1026.repositories.AccountRepository;


@WebServlet(name = "AccessController", urlPatterns = {"/AccessController"})
public class AccessController extends HttpServlet {

    private AccountRepository rep = null;
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
        String forward = "index.jsp";
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
            // Istanzio il repository
            rep = new AccountRepository(getServletContext());
            String username = request.getParameter("username");
            String password = request.getParameter("password");
            HttpSession session = request.getSession();
            // Setto il forward di default
            String forward = "index.jsp";
            // Se sono state passate le credenziali di accesso e queste corrispondono con un account presente nel file accounts.xml redirigi verso l'area aopportuna
            if(username != null && password != null){
                Account account = rep.GetAccount(username);
                if(account != null && account.getPassword().equals(password)) {
                    session.setAttribute("username",username);
                    session.setAttribute("role",account.getRole());
                    if(account.getRole().equals("patient")){
                        session.setAttribute("patientUsername",username);
                    }
                    forward = GetForward(account.getRole());
                }else{
                    forward=GetForward("unknown");
                }
            }
            SimpleDateFormat sdf = new SimpleDateFormat("dd/mm/yyyy");
            Date date = new Date();
            session.setAttribute("date", date);
            RequestDispatcher dispatcher = request.getRequestDispatcher(forward);
            dispatcher.forward(request, response);
    }

    private String GetForward(String role){
        switch(role){
            case "doctor":
                return "jsp/doctor-home.jsp";
            case "patient":
                return "jsp/patient-home.jsp";
            case "unknown":
                return "index.jsp?LoginError=true";
            default:
                return "index.jsp";
        }
    } 
}