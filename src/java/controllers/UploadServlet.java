/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;
import javax.xml.transform.TransformerException;
import static jdk.nashorn.internal.objects.NativeError.getFileName;
import models.Account;
import models.Conversation;
import models.Doctor;
import models.Patient;
import repositories.AccountRepository;
import repositories.ConversationRepository;
/**
 *
 * @author Riccardo
 */
@WebServlet(name = "UploadServlet", urlPatterns = {"/UploadServlet"})
@MultipartConfig
public class UploadServlet extends HttpServlet{
    private boolean isMultipart;
    private String filePath;
    private int maxFileSize = 50 * 1024;
    private int maxMemSize = 4 * 1024;
    
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            Part file = request.getPart("file");
            String fileNameReal = file.getSubmittedFileName();
            if(fileNameReal.equals("")){
                String forward = "jsp/conversation.jsp";
                response.sendRedirect(request.getContextPath() +"/"+ forward);
            }
            else{
                String patientUser=request.getParameter("patientUsername");
                String fileName = "images/"+patientUser+"/";
                String filePath = getServletContext().getRealPath(fileName);

                ConversationRepository rep = new ConversationRepository(this.getServletContext(), patientUser);
                AccountRepository repAcc = new AccountRepository(this.getServletContext());
                String sendRole = request.getParameter("role");
                String receiverRole = "";
                if(sendRole.equals("patient")){
                    receiverRole="doctor";
                    String doctorUser="";
                    for(int i=0; i<repAcc.getDoctors().size();i++){
                        if(((Doctor)repAcc.getDoctors().get(i)).hasPatient(patientUser)){
                            doctorUser = ((Doctor)repAcc.getDoctors().get(i)).getUsername();
                        }
                    }
                    for(int i=0;i<((Doctor)repAcc.GetAccount(doctorUser)).getPatients().size();i++){
                        if(((Doctor)repAcc.GetAccount(doctorUser)).getPatients().get(i).equals(patientUser)){
                            ((Doctor)repAcc.GetAccount(doctorUser)).getLastVisities().set(i, ((Doctor)repAcc.GetAccount(doctorUser)).getLastVisities().get(i)+1);
                        }
                    }

                }else{
                    receiverRole="patient";
                    ((Patient)repAcc.GetAccount(patientUser)).setLastVisit(((Patient)repAcc.GetAccount(patientUser)).getLastVisit()+1);
                }
                List<Account> acc = repAcc.GetAccounts();
                repAcc.writeAccounts(repAcc.GetAccounts());

                Conversation conv = rep.GetConversation();
                DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                //
                String type="";
                if(file.getContentType().contains("image")){
                    type="img";
                }else{
                    type="doc";
                }
                //
                Date date = new Date();
                //fullFileName.substr(fullFileName.lastIndexOf("\\")+1, fullFileName.length);
                fileNameReal = fileNameReal.substring(fileNameReal.lastIndexOf("\\")+1, fileNameReal.length());
                String fileRelative = this.getServletContext().getContextPath()+"/images/"+patientUser+"/"+fileNameReal;
                rep.addXMLMessage(sendRole, receiverRole,type, fileRelative, date.toString());
                InputStream in = request.getPart("file").getInputStream();
                OutputStream out = new FileOutputStream(filePath+"/"+fileNameReal);
                System.out.println("file relative="+fileRelative);
                System.out.println("file altro percorso="+filePath+"\\"+fileNameReal);
                //OutputStream out = new FileOutputStream(fileRelative);
                byte[] buffer = new byte[4096];
                long count = 0;
                int n=0;
                while(-1 != (n=in.read(buffer))){
                    out.write(buffer, 0, n);
                    count+=n;
                }
                out.flush();
                out.close();
                String forward = "jsp/conversation.jsp";
                response.sendRedirect(request.getContextPath() +"/"+ forward);
                //String forward = "jsp/conversation.jsp";
                //response.sendRedirect(request.getContextPath() +"/"+ forward);
            }
        } catch (TransformerException ex) {
            Logger.getLogger(UploadServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
