/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb;

import java.util.List;
import javax.ejb.Remote;

/**
 *
 * @author USER
 */
@Remote
public interface LibServerBeanRemote {
    public void remove();
    public String doCreateLibMember(String name, String password, String contact, String email, String addr);
    public String doUpdateProfile(String name, String password, String contact, String email, String addr);
    public String doDeleteProfile(String name);
    public List<String> getMembers();
    public String doCreateBook(String isbn, String copyNum, String title, String publisher, String publicationYr, String authorIdList);
    public String doCreateAuthor(String name, String description);
    public List<String> getAuthors();
    public String doCreateCheckout(String memberName, String isbn, String copyNum);
    public List<String> getCheckouts();
    public String doCreateReturn(String isbn, String copyNum);
    public List<String> getUnpaidFines();
    public String doDeleteBook(String isbn, String copyNum);
    public String doCheckUpdateBook(String isbn, String copyNum);
    public String doUpdateBook(Long bookID, String isbn, String copyNum, String title, String publisher, String publicationYr, String authorIdList);
    public List<String> getReservations(String isbn, String copyNum);
    public List<String> getRequest();
    public String doUpdateRequest(String requestID, String status, String comment);

    //Web
    public String doLibMemberLogin(String name, String password);
    public List<String> doViewFine(String name);
    public String doMakePayment(String name, String fineID, String cardType, String cardNum);
    public List<String> doViewCheckout(String name);
    public List<String> doSearchBook(String title, String isbn, String author);
    public List<String> doViewBook(Long bookID, String bookStatus, String dueDate, String name);
    public List<String> doViewAuthor(Long authorID);
    public String doMakeReservation(Long bookID, String note, String name);
    public List<String> doViewRequest(String name);
}
