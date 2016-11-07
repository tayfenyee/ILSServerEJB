/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servlets;

import ejb.LibServerBeanRemote;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author USER
 */
public class ILSServlet extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    
    @EJB
    private LibServerBeanRemote lsb;
    private ArrayList data = null;
    private String message = null;
    
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        try {
            /* TODO output your page here. You may use following sample code. */
            PrintWriter out = response.getWriter();
            RequestDispatcher dispatcher;
            ServletContext servletContext = getServletContext();
        
            String page = request.getPathInfo();
            page        = page.substring(1);
            out.print(page);
            
            if ("login".equals(page)) {
                message = memberLogin(request);
                if (message.equalsIgnoreCase("Login Successful!")){
                    request.setAttribute("message",message);
                    dispatcher = servletContext.getNamedDispatcher("Main");
                    dispatcher.forward(request, response);
                } else{
                    request.setAttribute("message",message);
                    dispatcher = servletContext.getNamedDispatcher("MemberLogin");
                    dispatcher.forward(request, response);
                }
            } else if ("register".equals(page)) {
                message = registerMember(request);
                if (message.equalsIgnoreCase("")){
                    request.setAttribute("message",message);
                    dispatcher = servletContext.getNamedDispatcher("Main");
                    dispatcher.forward(request, response);
                } else{
                    request.setAttribute("message",message);
                    dispatcher = servletContext.getNamedDispatcher("RegisterMember");
                    dispatcher.forward(request, response);
                }
            } else if ("updateProfile".equals(page)) {
                message = updateProfile(request);
                if (message.equalsIgnoreCase("")){
                    request.setAttribute("message","Update Successful!");
                    dispatcher = servletContext.getNamedDispatcher("UpdateProfile");
                    dispatcher.forward(request, response);
                } else{
                    request.setAttribute("message",message);
                    dispatcher = servletContext.getNamedDispatcher("UpdateProfile");
                    dispatcher.forward(request, response);
                }
            } else if ("viewFine".equals(page)) {
                String userName = request.getParameter("userName");
                data = (ArrayList)lsb.doViewFine(userName);
                request.setAttribute("data", data);
                dispatcher = servletContext.getNamedDispatcher("ViewFine");
                dispatcher.forward(request, response);
            } else if ("payFine".equals(page)) {
                dispatcher = servletContext.getNamedDispatcher("PayFine");
                dispatcher.forward(request, response);
            } else if ("makePayment".equals(page)) {
                message = makePayment(request);
                if (message.equalsIgnoreCase("")) {
                    request.setAttribute("paymentMsg","Payment Successful!");
                    dispatcher = servletContext.getNamedDispatcher("ViewFine");
                    dispatcher.forward(request, response);
                } else {
                    request.setAttribute("message",message);
                    dispatcher = servletContext.getNamedDispatcher("PayFine");
                    dispatcher.forward(request, response);
                }
            } else if ("viewCheckout".equals(page)) {
                String userName = request.getParameter("userName");
                data = (ArrayList)lsb.doViewCheckout(userName);
                request.setAttribute("data", data);
                dispatcher = servletContext.getNamedDispatcher("ViewCheckout");
                dispatcher.forward(request, response);
            } else if ("viewRequest".equals(page)) {
                String userName = request.getParameter("userName");
                data = (ArrayList)lsb.doViewRequest(userName);
                request.setAttribute("data", data);
                dispatcher = servletContext.getNamedDispatcher("ViewRequest");
                dispatcher.forward(request, response);
            } else if ("searchBook".equals(page)) {
                String userName = request.getParameter("userName");
                String title = request.getParameter("title");
                String isbn = request.getParameter("isbn");
                String author = request.getParameter("author");
                data = (ArrayList)lsb.doSearchBook(title, isbn, author);
                request.setAttribute("data", data);
                dispatcher = servletContext.getNamedDispatcher("SearchBook");
                dispatcher.forward(request, response);
            } else if ("viewBook".equals(page)) {
                String userName = request.getParameter("userName");
                String sbookID = request.getParameter("bookID");
                String bookStatus = request.getParameter("bookStatus");
                String dueDate = request.getParameter("dueDate");
                Long bookID = Long.parseLong(sbookID);
                data = (ArrayList)lsb.doViewBook(bookID, bookStatus, dueDate, userName);
                request.setAttribute("data", data);
                dispatcher = servletContext.getNamedDispatcher("ViewBook");
                dispatcher.forward(request, response);
            } else if ("viewAuthor".equals(page)) {
                String sauthorID = request.getParameter("authorID");
                Long authorID = Long.parseLong(sauthorID);
                data = (ArrayList)lsb.doViewAuthor(authorID);
                request.setAttribute("data", data);
                dispatcher = servletContext.getNamedDispatcher("ViewAuthor");
                dispatcher.forward(request, response);
            } else if ("makeReservation".equals(page)) {
                String sbookID = request.getParameter("bookID");
                String note = request.getParameter("note");
                String userName = request.getParameter("userName");
                Long bookID = Long.parseLong(sbookID);
                message = (String)lsb.doMakeReservation(bookID, note, userName);
                request.setAttribute("reservationMsg", message);
                dispatcher = servletContext.getNamedDispatcher("ViewBook");
                dispatcher.forward(request, response);
            } else if ("../Index".equals(page)) {
                request.setAttribute("message", "logout");
                dispatcher = servletContext.getNamedDispatcher("../Index");
                dispatcher.forward(request, response);
            } else {
                dispatcher = servletContext.getNamedDispatcher(page);
                dispatcher.forward(request, response);
            }
        } catch (Exception e) {
            log(e.getMessage());
        }
    }

    private String memberLogin(HttpServletRequest request){
        String reply="";
        String userName = request.getParameter("userName");
        String password = request.getParameter("password");
        reply = lsb.doLibMemberLogin(userName, password);
        return reply;
    }
    
    private String registerMember(HttpServletRequest request){
        String reply="";
        String userName = request.getParameter("userName");
        String password = request.getParameter("password");
        String contactNum = request.getParameter("contactNum");
        String email = request.getParameter("email");
        String address = request.getParameter("address");
        reply = lsb.doCreateLibMember(userName, password, contactNum, email, address);
        return reply;
    }
   
    private String updateProfile(HttpServletRequest request){
        String reply="";
        String userName = request.getParameter("userName");
        String password = request.getParameter("password");
        String contactNum = request.getParameter("contactNum");
        String email = request.getParameter("email");
        String address = request.getParameter("address");
        reply = lsb.doUpdateProfile(userName, password, contactNum, email, address);
        return reply;
    }
    
    private String makePayment(HttpServletRequest request){
        String reply="";
        String userName = request.getParameter("userName");
        String fineID = request.getParameter("fineID");
        String cardType = request.getParameter("cardType");
        String cardNum = request.getParameter("cardNum");
        reply = lsb.doMakePayment(userName, fineID, cardType, cardNum);
        return reply;
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
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
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
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
