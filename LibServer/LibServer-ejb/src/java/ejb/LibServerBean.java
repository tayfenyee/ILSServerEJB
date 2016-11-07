/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import static java.util.concurrent.TimeUnit.DAYS;
import javax.ejb.Remove;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.Stateful;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author USER
 */
@Stateful
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class LibServerBean implements LibServerBeanRemote, Serializable{

    @PersistenceContext(unitName = "LibServer-ejbPU")
    private EntityManager em;
    
    private Set<BookEntity> booksBi;
    private Collection<CheckoutEntity> checkoutsBi;
    private Collection<ReservationEntity> reservationsBi;
    private Collection<RequestEntity> requestsBi;
    private Collection<FineEntity> finesBi;

    //LibMember Entity
    private String createLibMember(String name, String password, String contact, String email, String addr){
        String message = "";
        LibMemberEntity lme = em.find(LibMemberEntity.class, name);
        if (lme == null) {
            //User d.n.e
            lme = new LibMemberEntity(name, password, contact, email, addr);
            checkoutsBi = new ArrayList<>();
            lme.setCheckouts(checkoutsBi);
            finesBi = new ArrayList<>();
            lme.setFines(finesBi);
            requestsBi = new ArrayList<>();
            lme.setRequests(requestsBi);
            reservationsBi = new ArrayList<>();
            lme.setReservations(reservationsBi);
            em.persist(lme);
        } else {
            //User already exist (error msg)
            message = "User (Name: " + name + ") has already existed in the system database.";
        }
        return message;
    }
    
    private String updateProfile(String name, String password, String contact, String email, String addr) {
        String message = "";
        LibMemberEntity lme = em.find(LibMemberEntity.class, name);
        if (lme == null) {
            //User d.n.e (error msg)
            message = "User (Name: " + name + ") does not exist in the system database.";
        } else {
            lme.setPassword(password);
            lme.setAddress(addr);
            lme.setContactNum(contact);
            lme.setEmail(email);
            em.persist(lme);
            em.flush();
        }
        return message;
    }
    
    private String deleteProfile(String name) {
        String message = "";
        LibMemberEntity lme = em.find(LibMemberEntity.class, name);
        if (lme == null) {
            //User d.n.e (error msg)
            message = "User (Name: " + name + ") does not exist in the system database.";
        } else if (!lme.getReservations().isEmpty()) {
            //User got reservation (error msg) 
            message = "User account (Name: " + name + ") is tied to reservation(s).";
        } else if (!lme.getCheckouts().isEmpty()) {
            //User got checkout (error msg) 
            message = "User account (Name: " + name + ") is tied to checkout(s).";
        } else if (!lme.getRequests().isEmpty()) {
            //User got request (error msg) 
            message = "User account (Name: " + name + ") is tied to request(s).";
        } else {
            em.remove(lme); //User exist and got no checkout, reservation & request
        }
        return message;
    }
    
    //Book Entity
    private String createBook(String isbn, String copyNum, String title, String publisher, String publicationYr, String authorIdList){
        String message = "";
        Boolean validAuthors = true;
        Boolean noDuplicates = true;
        
        String[] authorList = authorIdList.split(",");
        for (String authorIDs: authorList) {
            Long authorID = Long.parseLong(authorIDs);
            AuthorEntity ae = em.find(AuthorEntity.class, authorID);
            if (ae == null) {
                message = message + "Author (AuthorID: " + authorID + ") does not exist in the system database.\n";
                validAuthors = false;
            } 
        }
        
        if (validAuthors) {
            Query q = em.createQuery("SELECT be FROM Book be");
            for (Object o: q.getResultList()) {
                BookEntity b = (BookEntity)o;
                if (b.getIsbn().equalsIgnoreCase(isbn) && b.getCopyNum().equalsIgnoreCase(copyNum)) {
                    message = message + "Book with the same ISBN (ISBN: " + isbn + ") and Copy Number (CopyNum: " + copyNum + ") has already existed in the system database.\n";
                    noDuplicates = false;
                } 
            }
            if (noDuplicates) {
                BookEntity be = new BookEntity(isbn, copyNum, title, publisher, publicationYr);
                checkoutsBi = new ArrayList<>();
                be.setCheckouts(checkoutsBi);
                reservationsBi = new ArrayList<>();
                be.setReservations(reservationsBi);
                for (String authorIDs: authorList) {
                    Long authorID = Long.parseLong(authorIDs);
                    AuthorEntity ae = em.find(AuthorEntity.class, authorID);
                    be.getAuthor().add(ae);
                    ae.getBook().add(be);
                    em.persist(ae); //can remove
                }
                em.persist(be);
            }
        }
        return message;
    }
    
    private String checkUpdateBook(String isbn, String copyNum){
        String message = "";
        String authors = "";
        Boolean notValidBook = true;
        Boolean noCheckout = true;
        Boolean noReservation = true;
        BookEntity bookObj = null;
        
        Query q1 = em.createQuery("SELECT be FROM Book be");
        for (Object o: q1.getResultList()) {
            BookEntity b = (BookEntity)o;
            if (b.getIsbn().equalsIgnoreCase(isbn) && b.getCopyNum().equalsIgnoreCase(copyNum)) {
                //Book exists
                bookObj = b;
                notValidBook = false;
            } 
        }
        
        if (notValidBook) {
            message = message + "Book with ISBN (ISBN: " + isbn + ") and Copy Number (CopyNum: " + copyNum + ") does not exist in the system database.\n";
        } else if (bookObj != null) {
            for (Object o: bookObj.getCheckouts()) {
                CheckoutEntity c = (CheckoutEntity)o;
                if (c.getReturnDate() == null) {
                    //Book associated with checkouts
                    noCheckout = false;
                    message = message + "Book with ISBN (ISBN: " + isbn + ") and Copy Number (CopyNum: " + copyNum + ") is tied to checkout (ID: " + c.getCheckoutID() +").\n";            
                }
            }
            
            if (!bookObj.getReservations().isEmpty()) {
                //Book associated with reservations
                noReservation = false;
                message = message + "Book with ISBN (ISBN: " + isbn + ") and Copy Number (CopyNum: " + copyNum + ") is tied to reservation(s).\n";            
            }
        }
        
        if (!notValidBook && noCheckout && noReservation && bookObj != null) {
            for (Object o: bookObj.getAuthor()) {
                AuthorEntity ae = (AuthorEntity)o;
                authors = authors + ae.getAuthorID() + ",";
            }
            message = "CanUpdate#" + bookObj.getBookID() + "#" + bookObj.getIsbn() + "#" + bookObj.getCopyNum() + "#" + bookObj.getTitle() + "#" + bookObj.getPublisher() + "#" + bookObj.getPublicationYr() + "#" + authors;
        }
        
        return message;
    }
    
    private String updateBook(Long bookID, String isbn, String copyNum, String title, String publisher, String publicationYr, String authorIdList){
        String message = "";
        Boolean validAuthors = true;
        Boolean noDuplicates = true;
        BookEntity bookObj = null;
        
        String[] authorList = authorIdList.split(",");
        for (String authorIDs: authorList) {
            Long authorID = Long.parseLong(authorIDs);
            AuthorEntity ae = em.find(AuthorEntity.class, authorID);
            if (ae == null) {
                message = message + "Author (AuthorID: " + authorID + ") does not exist in the system database.\n";
                validAuthors = false;
            } 
        }
        
        if (validAuthors) {
            Query q = em.createQuery("SELECT be FROM Book be");
            for (Object o: q.getResultList()) {
                BookEntity be = (BookEntity)o;
                if (be.getIsbn().equalsIgnoreCase(isbn) && be.getCopyNum().equalsIgnoreCase(copyNum)) {
                    message = message + "Book with the same ISBN (ISBN: " + isbn + ") and Copy Number (CopyNum: " + copyNum + ") has already existed in the system database.\n";
                    noDuplicates = false;
                } 
            }
            
            if (noDuplicates) {
                for (Object o: q.getResultList()) {
                    BookEntity b = (BookEntity)o;
                    if (b.getBookID().equals(bookID)) {
                        bookObj = b;
                        bookObj.setIsbn(isbn);
                        bookObj.setCopyNum(copyNum);
                        bookObj.setTitle(title);
                        bookObj.setPublisher(publisher);
                        bookObj.setPublicationYr(publicationYr);
                        for (Object prevAuthors: bookObj.getAuthor()) {
                            AuthorEntity pae = (AuthorEntity)prevAuthors;
                            if (pae.getBook().equals(bookObj)){
                                pae.getBook().remove(bookObj);
                            }
                        }
                        bookObj.getAuthor().clear();
                        for (String authorIDs: authorList) {
                            Long authorID = Long.parseLong(authorIDs);
                            AuthorEntity ae = em.find(AuthorEntity.class, authorID);
                            bookObj.getAuthor().add(ae);
                            ae.getBook().add(bookObj);
                            em.persist(ae); //can remove
                        }
                        em.persist(bookObj);
                        em.flush();
                    } 
                }
            }
        }
        return message;
    }
    
    private String deleteBook(String isbn, String copyNum){
        String message = "";
        Boolean notValidBook = true;
        Boolean noCheckout = true;
        Boolean noReservation = true;
        BookEntity bookObj = null;
        
        Query q1 = em.createQuery("SELECT be FROM Book be");
        for (Object o: q1.getResultList()) {
            BookEntity b = (BookEntity)o;
            if (b.getIsbn().equalsIgnoreCase(isbn) && b.getCopyNum().equalsIgnoreCase(copyNum)) {
                //Book exists
                bookObj = b;
                notValidBook = false;
                System.out.println("Book Exist");
            } 
        }
        
        if (notValidBook) {
            message = message + "Book with ISBN (ISBN: " + isbn + ") and Copy Number (CopyNum: " + copyNum + ") does not exist in the system database.\n";
        } else if (bookObj != null) {
            for (Object o: bookObj.getCheckouts()) {
                CheckoutEntity c = (CheckoutEntity)o;
                if (c.getReturnDate() == null) {
                    //Book associated with checkouts
                    noCheckout = false;
                    message = message + "Book with ISBN (ISBN: " + isbn + ") and Copy Number (CopyNum: " + copyNum + ") is tied to checkout (ID: " + c.getCheckoutID() +").\n";            
                }
            }
            
            if (!bookObj.getReservations().isEmpty()) {
                //Book associated with reservations
                noReservation = false;
                message = message + "Book with ISBN (ISBN: " + isbn + ") and Copy Number (CopyNum: " + copyNum + ") is tied to reservation(s).\n";            
            }
        }
        
        if (!notValidBook && noCheckout && noReservation && bookObj != null) {
            em.remove(bookObj);
        }
        
        return message;
    }
    
    //Author Entity
    private void createAuthor(String name, String description){
        AuthorEntity ae = new AuthorEntity(name, description);
        booksBi = new HashSet<>();
        ae.setBook(booksBi);
        em.persist(ae);
    }
    
    //Checkout Entity
    private String createCheckout(String memberName, String isbn, String copyNum) {
        String message = "";
        Boolean validMember = true;
        Boolean notValidBook = true;
        Boolean notLoaned = true;
        Boolean haveQuota = true;
        Boolean notFirstToReserve = true;
        Boolean gotReservation = true;
        BookEntity bookObj = null;
        Long reservID = null;
        LibMemberEntity libMemberObj = null;
        Date todayDate = new Date();
        
        LibMemberEntity lme = em.find(LibMemberEntity.class, memberName);
        if (lme == null) {
            //User d.n.e
            message = message + "User (Name: " + memberName + ") does not exist in the system database.\n";
            validMember = false;
        } else {
            if (!lme.getFines().isEmpty()) {
                for (Object o: lme.getFines()) {
                    FineEntity fe = (FineEntity)o;
                    if (fe.getPayment() == null) {
                        validMember = false;
                    }
                }
            }
            
            if (validMember) {
                libMemberObj = lme;
                //System.out.println("Member: " + libMemberObj.getUserName());
                int loanedBookCount = 0;
            
                for (Object obj: lme.getCheckouts()) {
                    CheckoutEntity ce = (CheckoutEntity)obj;
                    if (ce.getReturnDate() == null) {
                        loanedBookCount++;
                    }
                }
                //System.out.println(lme.getUserName() + " loaned: " + loanedBookCount + " books.");
            
                if (loanedBookCount > 1) {
                    haveQuota = false;
                    message = message + "User (Name: " + memberName + ") has exceeded the maximum number of books to be loaned.\n";
                }
            } else {
                message = message + "User (Name: " + memberName + ") has unpaid fine.\n";
            }
        }
        
        Query q1 = em.createQuery("SELECT be FROM Book be");
        for (Object o: q1.getResultList()) {
            BookEntity b = (BookEntity)o;
            if (b.getIsbn().equalsIgnoreCase(isbn) && b.getCopyNum().equalsIgnoreCase(copyNum)) {
                //Book exists
                bookObj = b;
                notValidBook = false;
            } 
        }
        
        if (notValidBook) {
            message = message + "Book with ISBN (ISBN: " + isbn + ") and Copy Number (CopyNum: " + copyNum + ") does not exist in the system database.\n";
        } else if (bookObj != null) {
            for (Object obj : bookObj.getCheckouts()) {
                CheckoutEntity c = (CheckoutEntity)obj;
                if (c.getReturnDate() == null) {
                    //Book loaned
                    notLoaned = false;
                    message = message + "Book with ISBN (ISBN: " + isbn + ") and Copy Number (CopyNum: " + copyNum + ") is already loaned with Due Date at " + new SimpleDateFormat("yyyy-MM-dd").format(c.getDueDate()) + ".\n";               
                } 
            }
            if (bookObj.getReservations().isEmpty()) {
                gotReservation = false;
                notFirstToReserve = false;
            } else {
                for (Object ob : bookObj.getReservations()) {
                    ReservationEntity r = (ReservationEntity)ob;
                    if (r.getLibmember().getUserName().equalsIgnoreCase(memberName)) {
                        notFirstToReserve = false;
                        reservID = r.getReservationID();
                    }
                    break;
                }
                if (notFirstToReserve) {
                    message = message + "Book with ISBN (ISBN: " + isbn + ") and Copy Number (CopyNum: " + copyNum + ") is already reserved by someone else.";
                }
            }
        }
        
        if (validMember && !notValidBook && notLoaned && haveQuota && !notFirstToReserve) {
            Date loanDate = todayDate;
            Date dueDate = todayDate;
            Calendar cal = Calendar.getInstance(); 
            cal.setTime(dueDate); 
            cal.add(Calendar.DATE, 14);
            dueDate = cal.getTime();
            
            CheckoutEntity ce = new CheckoutEntity(loanDate, dueDate, null);
            ce.setBook(bookObj);
            ce.setLibMember(libMemberObj);
            bookObj.getCheckouts().add(ce);
            libMemberObj.getCheckouts().add(ce);
            if (!notFirstToReserve && gotReservation) {
                System.out.println(reservID);
                ReservationEntity re = em.find(ReservationEntity.class, reservID);
                BookEntity be = re.getBook();
                be.setReservations(null);
                LibMemberEntity le = re.getLibmember();
                le.setReservations(null);
                em.remove(re);
            }
            //ReservationEntity re = em.find(ReservationEntity.class, reservID);
            //em.remove(re);
            em.persist(ce);
            em.persist(bookObj);
            em.persist(libMemberObj);
        }
        return message;
    }
    
    private String createReturn(String isbn, String copyNum) {
        String message = "";
        Boolean notValidBook = true;
        Boolean notReturned = true;
        BookEntity bookObj = null;
        CheckoutEntity checkoutObj = null;
        LibMemberEntity libMemberObj = null;
        Date todayDate = new Date();
        Date dueDate = null;
        
        Query q1 = em.createQuery("SELECT be FROM Book be");
        for (Object o: q1.getResultList()) {
            BookEntity b = (BookEntity)o;
            if (b.getIsbn().equalsIgnoreCase(isbn) && b.getCopyNum().equalsIgnoreCase(copyNum)) {
                //Book exists
                bookObj = b;
                notValidBook = false;
            } 
        }
        
        if (notValidBook) {
            message = message + "Book with ISBN (ISBN: " + isbn + ") and Copy Number (CopyNum: " + copyNum + ") does not exist in the system database.\n";
        } else if (!bookObj.getCheckouts().isEmpty()) {
            for (Object obj: bookObj.getCheckouts()) {
                CheckoutEntity ce = (CheckoutEntity)obj;
                if (ce.getReturnDate() != null) {
                    //Book already returned
                    notReturned = false;
                    message = message + "Book with ISBN (ISBN: " + isbn + ") and Copy Number (CopyNum: " + copyNum + ") has no checkout records.\n";
                } else {
                    //Book to be returned
                    System.out.println("Not null" + ce.getCheckoutID());
                    notReturned = true;
                    message = "";
                    checkoutObj = ce;
                    dueDate = ce.getDueDate();
                    libMemberObj = ce.getLibMember();
                }
            }
        } else {
            notReturned = false;
            message = message + "Book with ISBN (ISBN: " + isbn + ") and Copy Number (CopyNum: " + copyNum + ") has no checkout records.\n";
        }
        
        if (!notValidBook && notReturned && checkoutObj != null) {
            Date returnDate = todayDate;
            double fineAmt;
            long diffDays;
            long difference;
            
            checkoutObj.setReturnDate(returnDate);
            em.persist(checkoutObj);
            
            System.out.println("Return Date: " + returnDate);
            System.out.println("Due Date: " + dueDate);
            if (returnDate.after(dueDate)) {
                System.out.println("Fine");
                //Fine incurred
                TimeUnit tu = TimeUnit.DAYS;
                difference = returnDate.getTime() - dueDate.getTime();
                diffDays = tu.convert(difference,TimeUnit.MILLISECONDS);
                fineAmt = (double)diffDays;
                message = "Book returned successfully with fine of $" + fineAmt + " incurred due to late return.";
                FineEntity fe = new FineEntity(todayDate, fineAmt);
                fe.setLibmember(libMemberObj);
                libMemberObj.getFines().add(fe);
                em.persist(fe);
                em.persist(libMemberObj);
            }
        }
        return message;
    }
    
    //Web (Payment Entity)
    public String makePayment(String name, String sfineID, String cardType, String cardNum) {
        String message = "";
        Date nowDateTime = new Date();
        Long fineID = Long.parseLong(sfineID);
        FineEntity fe = em.find(FineEntity.class, fineID);
        if (fe == null) {
            message = "Invalid fineID";
        } else {
            PaymentEntity pe = new PaymentEntity(nowDateTime, cardType, cardNum, name);
            em.persist(pe);
            fe.setPayment(pe);
            em.persist(fe);
        }
        return message;
    }
    
    //LibMember Entity
    @Override
    public String doCreateLibMember(String name, String password, String contact, String email, String addr) {
        String message = "";
        if (name.equalsIgnoreCase("") || password.equalsIgnoreCase("")) {
            message = "Name or password field is empty.";
        } else if (!contact.matches("[+ 0-9 -]+") || contact.startsWith("-")) {
            message = "TelNumber is not valid.";
        } else if (!email.contains("@")) {
            message = "Email is not valid.";
        } else {
            message = this.createLibMember(name, password, contact, email, addr);
        }
        return message;
    }
    
    @Override
    public String doDeleteProfile(String name) {
        String message = "";
        if (name.equalsIgnoreCase("")) {
            message = "Name field is empty.";
        } else {
            message = this.deleteProfile(name);
        }
        return message;
    }
    
    @Override
    public List<String> getMembers() {
        Query q = em.createQuery("SELECT lme FROM LibMember lme");
        List entityList = new ArrayList();
        for (Object o: q.getResultList()) {
            LibMemberEntity l = (LibMemberEntity)o;
            String entity = new String();
            entity = entity + l.getUserName();
            entity = entity + "#" + l.getContactNum();
            entity = entity + "#" + l.getEmail();
            entity = entity + "#" + l.getAddress();
            entityList.add(entity);
        }
        return entityList;
    }
    
    //Web (LibMember Entity)
    @Override
    public String doLibMemberLogin(String name, String password) {
        String message = "";
        if (name.equalsIgnoreCase("") || password.equalsIgnoreCase("")) {
            message = "Name or password field is empty.";
        } else {
            LibMemberEntity lme = em.find(LibMemberEntity.class, name);
            if (lme == null) {
                //User d.n.e (error msg)
                message = "Error! User (Name: " + name + ") does not exist in the system database.";
            } else if (password.equals(lme.getPassword())) {
                message = "Login Successful!";
            } else {
                message = "Error! The password is incorrect";
            }
        }
        return message;
    }
    
    @Override
    public String doUpdateProfile(String name, String password, String contact, String email, String addr) {
        String message = "";
        if (name.equalsIgnoreCase("") || password.equalsIgnoreCase("")) {
            message = "Name or password field is empty.";
        } else if (!contact.matches("[+ 0-9 -]+") || contact.startsWith("-")) {
            message = "TelNumber is not valid.";
        } else if (!email.contains("@")) {
            message = "Email is not valid.";
        } else {
            message = this.updateProfile(name, password, contact, email, addr);
        }
        return message;
    }
    
    //Book Entity
    @Override
    public String doCreateBook(String isbn, String copyNum, String title, String publisher, String publicationYr, String authorIdList) {
        String message = "";
        if (!isbn.matches("[0-9 -]+") || isbn.startsWith("-")) {
            message = "ISBN is not valid.";
        } else if (!copyNum.matches("[0-9]+")) {
            message = "CopyNum is not valid.";
        } else if (!publicationYr.matches("[0-9]+") || Integer.parseInt(publicationYr) >= 2017) {
            message = "PublicationYr is not valid.";
        } else if (!authorIdList.matches("[0-9 ,]+") || authorIdList.equalsIgnoreCase(",") || authorIdList.startsWith(",")) {
            message = "Author ID(s) is not valid.";
        } else {
            message = this.createBook(isbn, copyNum, title, publisher, publicationYr, authorIdList);
        }
        return message;
    }
    
    @Override
    public String doCheckUpdateBook(String isbn, String copyNum) {
        String message = "";
        if (!isbn.matches("[0-9 -]+") || isbn.startsWith("-")) {
            message = "ISBN is not valid.";
        } else if (!copyNum.matches("[0-9]+")) {
            message = "CopyNum is not valid.";
        } else {
            message = this.checkUpdateBook(isbn, copyNum);
        }
        return message;
    }
    
    @Override
    public String doUpdateBook(Long bookID, String isbn, String copyNum, String title, String publisher, String publicationYr, String authorIdList) {
        String message = "";
        if (!isbn.matches("[0-9 -]+") || isbn.startsWith("-")) {
            message = "ISBN is not valid.";
        } else if (!copyNum.matches("[0-9]+")) {
            message = "CopyNum is not valid.";
        } else if (!publicationYr.matches("[0-9]+") || Integer.parseInt(publicationYr) >= 2017) {
            message = "PublicationYr is not valid.";
        } else if (!authorIdList.matches("[0-9 ,]+") || authorIdList.equalsIgnoreCase(",") || authorIdList.startsWith(",")) {
            message = "Author ID(s) is not valid.";
        } else {
            message = this.updateBook(bookID, isbn, copyNum, title, publisher, publicationYr, authorIdList);
        }
        return message;
    }
    
    @Override
    public String doDeleteBook(String isbn, String copyNum) {
        String message = "";
        if (!isbn.matches("[0-9 -]+") || isbn.startsWith("-")) {
            message = "ISBN is not valid.";
        } else if (!copyNum.matches("[0-9]+")) {
            message = "CopyNum is not valid.";
        } else {
            message = this.deleteBook(isbn, copyNum);
        }
        return message;
    }
    
    //Web (Book Entity)
    @Override
    public List<String> doSearchBook(String title, String isbn, String author) {
        List entityList = new ArrayList();
        String message = "";
        if (title.equalsIgnoreCase("") && isbn.equalsIgnoreCase("") && author.equalsIgnoreCase("")) {
            message = "!All fields can't be empty.";
            entityList.add(message);
        } else {
            Query q1 = em.createQuery("SELECT be FROM Book be");
            for (Object o: q1.getResultList()) {
                BookEntity b = (BookEntity)o;
                
                if (title.equalsIgnoreCase("")) {
                    //No title
                    if (isbn.equalsIgnoreCase("")) {
                        //No isbn
                        //Thus must have author
                        //**Only got AUTHOR
                        for (Object au: b.getAuthor()) {
                            AuthorEntity authorEntity = (AuthorEntity)au;
                            if (authorEntity.getAuthorName().equalsIgnoreCase(author)) {
                                String titleFound = b.getTitle();
                                String isbnFound = b.getIsbn();
                                String bookIDFound = String.valueOf(b.getBookID());
                                String authorFound = "";
                                for (Object a: b.getAuthor()) {
                                    AuthorEntity aut = (AuthorEntity)a;
                                    authorFound = aut.getAuthorName() + ", " + authorFound;
                                }
                                String statusFound;
                                String dueDateFound = "null";
                                if (b.getCheckouts().isEmpty()) {
                                    statusFound = "Book Available";
                                } else {
                                    boolean bookAvail = true;
                                    for (Object ch: b.getCheckouts()) {
                                        CheckoutEntity checkoutEntity = (CheckoutEntity)ch;
                                        if (checkoutEntity.getReturnDate() == null) {
                                            bookAvail = false;
                                            dueDateFound = new SimpleDateFormat("yyyy-MM-dd").format(checkoutEntity.getDueDate());
                                        }
                                    }
                                    if (bookAvail) {
                                        statusFound = "Book Available";
                                        dueDateFound = "null";
                                    } else {
                                        statusFound = "Book Not Available";
                                    }
                                }
                                String entity = new String();
                                entity = entity + titleFound;
                                entity = entity + "#" + isbnFound;
                                entity = entity + "#" + bookIDFound;
                                entity = entity + "#" + authorFound;
                                entity = entity + "#" + statusFound;
                                entity = entity + "#" + dueDateFound;
                                entityList.add(entity);
                            }
                        }
                    } else {
                        //Have isbn
                        if (author.equalsIgnoreCase("")) {
                            //No author
                            //**Only got ISBN
                            if (b.getIsbn().equalsIgnoreCase(isbn)) {
                                String titleFound = b.getTitle();
                                String isbnFound = b.getIsbn();
                                String bookIDFound = String.valueOf(b.getBookID());
                                String authorFound = "";
                                for (Object au: b.getAuthor()) {
                                    AuthorEntity authorEntity = (AuthorEntity)au;
                                    authorFound = authorEntity.getAuthorName() + ", " + authorFound;
                                }
                                String statusFound;
                                String dueDateFound = "null";
                                if (b.getCheckouts().isEmpty()) {
                                    statusFound = "Book Available";
                                } else {
                                    boolean bookAvail = true;
                                    for (Object ch: b.getCheckouts()) {
                                        CheckoutEntity checkoutEntity = (CheckoutEntity)ch;
                                        if (checkoutEntity.getReturnDate() == null) {
                                            bookAvail = false;
                                            dueDateFound = new SimpleDateFormat("yyyy-MM-dd").format(checkoutEntity.getDueDate());
                                        }
                                    }
                                    if (bookAvail) {
                                        statusFound = "Book Available";
                                        dueDateFound = "null";
                                    } else {
                                        statusFound = "Book Not Available";
                                    }
                                }
                                String entity = new String();
                                entity = entity + titleFound;
                                entity = entity + "#" + isbnFound;
                                entity = entity + "#" + bookIDFound;
                                entity = entity + "#" + authorFound;
                                entity = entity + "#" + statusFound;
                                entity = entity + "#" + dueDateFound;
                                entityList.add(entity);
                            }
                        } else {
                            //Have author
                            //**Have ISBN + AUTHOR
                            for (Object au: b.getAuthor()) {
                                AuthorEntity authorEntity = (AuthorEntity)au;
                                if (authorEntity.getAuthorName().equalsIgnoreCase(author) && b.getIsbn().equalsIgnoreCase(isbn)) {
                                    String titleFound = b.getTitle();
                                    String isbnFound = b.getIsbn();
                                    String bookIDFound = String.valueOf(b.getBookID());
                                    String authorFound = "";
                                    for (Object a: b.getAuthor()) {
                                        AuthorEntity aut = (AuthorEntity)a;
                                        authorFound = aut.getAuthorName() + ", " + authorFound;
                                    }
                                    String statusFound;
                                    String dueDateFound = "null";
                                    if (b.getCheckouts().isEmpty()) {
                                        statusFound = "Book Available";
                                    } else {
                                        boolean bookAvail = true;
                                        for (Object ch: b.getCheckouts()) {
                                            CheckoutEntity checkoutEntity = (CheckoutEntity)ch;
                                            if (checkoutEntity.getReturnDate() == null) {
                                                bookAvail = false;
                                                dueDateFound = new SimpleDateFormat("yyyy-MM-dd").format(checkoutEntity.getDueDate());
                                            }
                                        }
                                        if (bookAvail) {
                                            statusFound = "Book Available";
                                            dueDateFound = "null";
                                        } else {
                                            statusFound = "Book Not Available";
                                        }
                                    }
                                    String entity = new String();
                                    entity = entity + titleFound;
                                    entity = entity + "#" + isbnFound;
                                    entity = entity + "#" + bookIDFound;
                                    entity = entity + "#" + authorFound;
                                    entity = entity + "#" + statusFound;
                                    entity = entity + "#" + dueDateFound;
                                    entityList.add(entity);
                                }
                            }
                        }
                    }
                } else {
                    //Have title
                    if (isbn.equalsIgnoreCase("")) {
                        //No isbn
                        if (author.equalsIgnoreCase("")) {
                            //No author
                            //**Only got TITLE
                            if (b.getTitle().equalsIgnoreCase(title)) {
                                String titleFound = b.getTitle();
                                String isbnFound = b.getIsbn();
                                String bookIDFound = String.valueOf(b.getBookID());
                                String authorFound = "";
                                for (Object au: b.getAuthor()) {
                                    AuthorEntity authorEntity = (AuthorEntity)au;
                                    authorFound = authorEntity.getAuthorName() + ", " + authorFound;
                                }
                                String statusFound;
                                String dueDateFound = "null";
                                if (b.getCheckouts().isEmpty()) {
                                    statusFound = "Book Available";
                                } else {
                                    boolean bookAvail = true;
                                    for (Object ch: b.getCheckouts()) {
                                        CheckoutEntity checkoutEntity = (CheckoutEntity)ch;
                                        if (checkoutEntity.getReturnDate() == null) {
                                            bookAvail = false;
                                            dueDateFound = new SimpleDateFormat("yyyy-MM-dd").format(checkoutEntity.getDueDate());
                                        }
                                    }
                                    if (bookAvail) {
                                        statusFound = "Book Available";
                                        dueDateFound = "null";
                                    } else {
                                        statusFound = "Book Not Available";
                                    }
                                }
                                String entity = new String();
                                entity = entity + titleFound;
                                entity = entity + "#" + isbnFound;
                                entity = entity + "#" + bookIDFound;
                                entity = entity + "#" + authorFound;
                                entity = entity + "#" + statusFound;
                                entity = entity + "#" + dueDateFound;
                                entityList.add(entity);
                            }
                        } else {
                            //Have author
                            //**Have TITLE + AUTHOR
                            for (Object au: b.getAuthor()) {
                                AuthorEntity authorEntity = (AuthorEntity)au;
                                if (authorEntity.getAuthorName().equalsIgnoreCase(author) && b.getTitle().equalsIgnoreCase(title)) {
                                    String titleFound = b.getTitle();
                                    String isbnFound = b.getIsbn();
                                    String bookIDFound = String.valueOf(b.getBookID());
                                    String authorFound = "";
                                    for (Object a: b.getAuthor()) {
                                        AuthorEntity aut = (AuthorEntity)a;
                                        authorFound = aut.getAuthorName() + ", " + authorFound;
                                    }
                                    String statusFound;
                                    String dueDateFound = "null";
                                    if (b.getCheckouts().isEmpty()) {
                                        statusFound = "Book Available";
                                    } else {
                                        boolean bookAvail = true;
                                        for (Object ch: b.getCheckouts()) {
                                            CheckoutEntity checkoutEntity = (CheckoutEntity)ch;
                                            if (checkoutEntity.getReturnDate() == null) {
                                                bookAvail = false;
                                                dueDateFound = new SimpleDateFormat("yyyy-MM-dd").format(checkoutEntity.getDueDate());
                                            }
                                        }
                                        if (bookAvail) {
                                            statusFound = "Book Available";
                                            dueDateFound = "null";
                                        } else {
                                            statusFound = "Book Not Available";
                                        }
                                    }
                                    String entity = new String();
                                    entity = entity + titleFound;
                                    entity = entity + "#" + isbnFound;
                                    entity = entity + "#" + bookIDFound;
                                    entity = entity + "#" + authorFound;
                                    entity = entity + "#" + statusFound;
                                    entity = entity + "#" + dueDateFound;
                                    entityList.add(entity);
                                }
                            }
                        }
                    } else {
                        //Have isbn
                        if (author.equalsIgnoreCase("")) {
                            //No author
                            //**Have TITLE + ISBN
                            if (b.getIsbn().equalsIgnoreCase(isbn) && b.getTitle().equalsIgnoreCase(title)) {
                                String titleFound = b.getTitle();
                                String isbnFound = b.getIsbn();
                                String bookIDFound = String.valueOf(b.getBookID());
                                String authorFound = "";
                                for (Object au: b.getAuthor()) {
                                    AuthorEntity authorEntity = (AuthorEntity)au;
                                    authorFound = authorEntity.getAuthorName() + ", " + authorFound;
                                }
                                String statusFound;
                                String dueDateFound = "null";
                                if (b.getCheckouts().isEmpty()) {
                                    statusFound = "Book Available";
                                } else {
                                    boolean bookAvail = true;
                                    for (Object ch: b.getCheckouts()) {
                                        CheckoutEntity checkoutEntity = (CheckoutEntity)ch;
                                        if (checkoutEntity.getReturnDate() == null) {
                                            bookAvail = false;
                                            dueDateFound = new SimpleDateFormat("yyyy-MM-dd").format(checkoutEntity.getDueDate());
                                        }
                                    }
                                    if (bookAvail) {
                                        statusFound = "Book Available";
                                        dueDateFound = "null";
                                    } else {
                                        statusFound = "Book Not Available";
                                    }
                                }
                                String entity = new String();
                                entity = entity + titleFound;
                                entity = entity + "#" + isbnFound;
                                entity = entity + "#" + bookIDFound;
                                entity = entity + "#" + authorFound;
                                entity = entity + "#" + statusFound;
                                entity = entity + "#" + dueDateFound;
                                entityList.add(entity);
                            }
                        } else {
                            //Have author
                            //**Have TITLE + ISBN + AUTHOR
                            for (Object au: b.getAuthor()) {
                                AuthorEntity authorEntity = (AuthorEntity)au;
                                if (authorEntity.getAuthorName().equalsIgnoreCase(author) && b.getIsbn().equalsIgnoreCase(isbn) && b.getTitle().equalsIgnoreCase(title)) {
                                    String titleFound = b.getTitle();
                                    String isbnFound = b.getIsbn();
                                    String bookIDFound = String.valueOf(b.getBookID());
                                    String authorFound = "";
                                    for (Object a: b.getAuthor()) {
                                        AuthorEntity aut = (AuthorEntity)a;
                                        authorFound = aut.getAuthorName() + ", " + authorFound;
                                    }
                                    String statusFound;
                                    String dueDateFound = "null";
                                    if (b.getCheckouts().isEmpty()) {
                                        statusFound = "Book Available";
                                    } else {
                                        boolean bookAvail = true;
                                        for (Object ch: b.getCheckouts()) {
                                            CheckoutEntity checkoutEntity = (CheckoutEntity)ch;
                                            if (checkoutEntity.getReturnDate() == null) {
                                                bookAvail = false;
                                                dueDateFound = new SimpleDateFormat("yyyy-MM-dd").format(checkoutEntity.getDueDate());
                                            }
                                        }
                                        if (bookAvail) {
                                            statusFound = "Book Available";
                                            dueDateFound = "null";
                                        } else {
                                            statusFound = "Book Not Available";
                                        }
                                    }
                                    String entity = new String();
                                    entity = entity + titleFound;
                                    entity = entity + "#" + isbnFound;
                                    entity = entity + "#" + bookIDFound;
                                    entity = entity + "#" + authorFound;
                                    entity = entity + "#" + statusFound;
                                    entity = entity + "#" + dueDateFound;
                                    entityList.add(entity);
                                }
                            }
                        }
                    }
                }
            }
        }
        System.out.println(entityList.toString());
        return entityList;
    }
    
    @Override
    public List<String> doViewBook(Long bookID, String bookStatus, String dueDate, String name) {
        BookEntity be = em.find(BookEntity.class, bookID);
        List entityList = new ArrayList();
        if (be == null) {
            entityList.add("!Error! Book info not found!");
        } else {
            String entity = new String();
            entity = entity + be.getBookID();
            entity = entity + "#" + be.getIsbn();
            entity = entity + "#" + be.getCopyNum();
            entity = entity + "#" + be.getTitle();
            entity = entity + "#" + be.getPublisher();
            entity = entity + "#" + be.getPublicationYr();
            entity = entity + "#" + bookStatus;
            entity = entity + "#" + dueDate;
            String authorString = "";
            for (Object o: be.getAuthor()) {
                AuthorEntity a = (AuthorEntity)o;
                authorString = a.getAuthorID() + "," + a.getAuthorName() + "-" + authorString;
            }
            entity = entity + "#" + authorString.substring(0, authorString.length()-1);
            if (be.getReservations().isEmpty()) {
                entity = entity + "#" + "Not Reserved";
            } else {
                boolean userReserved = false;
                for (Object o: be.getReservations()) {
                    ReservationEntity r = (ReservationEntity)o;
                    if (r.getLibmember().getUserName().equalsIgnoreCase(name)) {
                        userReserved = true;
                    }
                }
                if (!userReserved) {
                    entity = entity + "#" + "Not Reserved by User";
                } else {
                    entity = entity + "#" + "Reserved";
                }
            }
            entityList.add(entity);
        }
        System.out.println(entityList.toString());
        return entityList;
    }
    
    //Author Entity
    @Override
    public String doCreateAuthor(String name, String description) {
        String message = "";
        if (name.equalsIgnoreCase("")) {
            message = "Name field is empty.";
        } else {
            this.createAuthor(name, description);
        }
        return message;
    }
    
    @Override
    public List<String> getAuthors() {
        Query q = em.createQuery("SELECT ae FROM Author ae");
        List entityList = new ArrayList();
        for (Object o: q.getResultList()) {
            AuthorEntity a = (AuthorEntity)o;
            String entity = new String();
            entity = entity + a.getAuthorID();
            entity = entity + "#" + a.getAuthorName();
            entity = entity + "#" + a.getAuthorDescription();
            entityList.add(entity);
        }
        return entityList;
    }
    
    //Web (Author Entity)
    @Override
    public List<String> doViewAuthor(Long authorID) {
        AuthorEntity ae = em.find(AuthorEntity.class, authorID);
        List entityList = new ArrayList();
        
        if (ae == null) {
            entityList.add("!Error!Author details not found!");
        } else {
            String authorName = ae.getAuthorName();
            String authorDescription = ae.getAuthorDescription();
            for (Object o: ae.getBook()) {
                String entity = new String();
                String authorFound = "";
                BookEntity b = (BookEntity)o;
                String isbn = b.getIsbn();
                String title = b.getTitle();
                String bookStatus;
                String dueDate = "null";
                for (Object au: b.getAuthor()) {
                    AuthorEntity authorEntity = (AuthorEntity)au;
                    authorFound = authorEntity.getAuthorName() + "," + authorFound;
                }
                if (b.getCheckouts().isEmpty()) {
                    bookStatus = "Book Available";
                } else {
                    boolean bookAvail = true;
                    for (Object ch: b.getCheckouts()) {
                        CheckoutEntity checkoutEntity = (CheckoutEntity)ch;
                        if (checkoutEntity.getReturnDate() == null) {
                            bookAvail = false;
                            dueDate = new SimpleDateFormat("yyyy-MM-dd").format(checkoutEntity.getDueDate());
                        }
                    }
                    if (bookAvail) {
                        bookStatus = "Book Available";
                        dueDate = "null";
                    } else {
                        bookStatus = "Book Not Available";
                    }
                }
                entity = entity + authorName;
                entity = entity + "#" + authorDescription;
                entity = entity + "#" + isbn;
                entity = entity + "#" + title;
                entity = entity + "#" + bookStatus;
                entity = entity + "#" + dueDate;
                entity = entity + "#" + authorFound;
                entityList.add(entity);
            }
        }
        System.out.println(entityList.toString());
        return entityList;
    }
    
    //Checkout Entity
    @Override
    public String doCreateCheckout(String memberName, String isbn, String copyNum) {
        String message = "";
        if (!isbn.matches("[0-9 -]+") || isbn.startsWith("-")) {
            message = "ISBN is not valid.";
        } else if (!copyNum.matches("[0-9]+")) {
            message = "CopyNum is not valid.";
        } else {
            message = this.createCheckout(memberName, isbn, copyNum);
        }
        return message;
    }
    
    @Override
    public List<String> getCheckouts() {
        BookEntity bookEntity = null;
        LibMemberEntity libMemberEntity = null;
        Query q = em.createQuery("SELECT ce FROM Checkout ce");
        List entityList = new ArrayList();
        for (Object o: q.getResultList()) {
            CheckoutEntity c = (CheckoutEntity)o;
            if (c.getReturnDate() == null) {
                bookEntity = c.getBook();
                libMemberEntity = c.getLibMember();
                String entity = new String();
                entity = entity + c.getCheckoutID();
                entity = entity + "#" + bookEntity.getIsbn();
                entity = entity + "#" + bookEntity.getCopyNum();
                entity = entity + "#" + bookEntity.getTitle();
                entity = entity + "#" + libMemberEntity.getUserName();
                entity = entity + "#" + libMemberEntity.getEmail();
                entity = entity + "#" + new SimpleDateFormat("yyyy-MM-dd").format(c.getLoanDate());
                entity = entity + "#" + new SimpleDateFormat("yyyy-MM-dd").format(c.getDueDate());
                entityList.add(entity);
            }
        }
        return entityList;
    }
    
    //Web (Checkout Entity)
    @Override
    public List<String> doViewCheckout(String name) {
        BookEntity bookEntity = null;
        String message = "";
        LibMemberEntity lme = em.find(LibMemberEntity.class, name);
        List entityList = new ArrayList();
        if (lme.getCheckouts().isEmpty()) {
            message = "You do not have any checkout(s).";
            entityList.add(message);
        } else {
            for (Object o: lme.getCheckouts()) {
                CheckoutEntity c = (CheckoutEntity)o;
                if (c.getReturnDate() == null) {
                    //current
                    bookEntity = c.getBook();
                    String entity = new String();
                    entity = entity + "Current" + "#";
                    entity = entity + "#" + c.getCheckoutID();
                    entity = entity + "#" + bookEntity.getIsbn();
                    entity = entity + "#" + bookEntity.getTitle();
                    entity = entity + "#" + new SimpleDateFormat("yyyy-MM-dd").format(c.getLoanDate());
                    entity = entity + "#" + new SimpleDateFormat("yyyy-MM-dd").format(c.getDueDate());
                    entityList.add(entity);
                } else {
                    //previous
                    bookEntity = c.getBook();
                    String entity = new String();
                    entity = entity + "Previous" + "#";
                    entity = entity + "#" + c.getCheckoutID();
                    entity = entity + "#" + bookEntity.getIsbn();
                    entity = entity + "#" + bookEntity.getTitle();
                    entity = entity + "#" + new SimpleDateFormat("yyyy-MM-dd").format(c.getLoanDate());
                    entity = entity + "#" + new SimpleDateFormat("yyyy-MM-dd").format(c.getReturnDate());
                    entityList.add(entity);
                }
            }
        }
        return entityList;
    }
    
    @Override
    public String doCreateReturn(String isbn, String copyNum) {
        String message = "";
        if (!isbn.matches("[0-9 -]+") || isbn.startsWith("-")) {
            message = "ISBN is not valid.";
        } else if (!copyNum.matches("[0-9]+")) {
            message = "CopyNum is not valid.";
        } else {
            message = this.createReturn(isbn, copyNum);
        }
        return message;
    }
    
    //Fine Entity
    @Override
    public List<String> getUnpaidFines() {
        LibMemberEntity libMemberEntity = null;
        Query q = em.createQuery("SELECT fe FROM Fine fe");
        List entityList = new ArrayList();
        for (Object o: q.getResultList()) {
            FineEntity f = (FineEntity)o;
            if (f.getPayment() == null) {
                libMemberEntity = f.getLibmember();
                String entity = new String();
                entity = entity + f.getFineID();
                entity = entity + "#" + libMemberEntity.getUserName();
                entity = entity + "#" + libMemberEntity.getEmail();
                entity = entity + "#" + new SimpleDateFormat("yyyy-MM-dd").format(f.getFineDate());
                entity = entity + "#" + f.getFineAmt();
                entityList.add(entity);
            }
        }
        return entityList;
    }
    
    //Web (Fine Entity)
    @Override
    public List<String> doViewFine(String name) {
        LibMemberEntity lme = em.find(LibMemberEntity.class, name);
        List entityList = new ArrayList();
        String entity = new String();
        String message = "";
        if (lme == null) {
            //User d.n.e (error msg)
            message = "Error! User (Name: " + name + ") does not exist in the system database.";
            entity = entity + message;
            entityList.add(entity);
        } else {
            if (lme.getFines().isEmpty()) {
                message = "You have no unpaid fine.\n";
                entity = entity + message;
                entityList.add(entity);
            } else {
                Format formatter = new SimpleDateFormat("yyyy-MM-dd");
                for (Object o: lme.getFines()) {
                    entity = "";
                    FineEntity fe = (FineEntity)o;
                    entity = entity + fe.getFineID();
                    String fineDate = formatter.format(fe.getFineDate());
                    entity = entity + "#" + fineDate;
                    entity = entity + "#" + fe.getFineAmt();
                    if (fe.getPayment() == null) {
                        entity = entity + "#" + "Payment Due";
                    } else {
                        PaymentEntity pe = fe.getPayment();
                        String payDate = formatter.format(pe.getPaymentTime());
                        entity = entity + "#" + payDate;
                    }
                    entityList.add(entity);
                }
            }
        }
        System.out.println("Message is: " + entityList.toString());
        System.out.println("List is empty: " + entityList.isEmpty());
        return entityList;
    }
    
    //Web (Payment Entity)
    @Override
    public String doMakePayment(String name, String fineID, String cardType, String cardNum) {
        String message = "";
        if (name.equalsIgnoreCase("") || cardType.equalsIgnoreCase("") || cardNum.equalsIgnoreCase("")) {
            message = "Card Type / Card Number field / Card Holder Name is empty.";
        } else if (!cardNum.matches("[0-9 -]+")) {
            message = "Invalid Card Number";
        } else {
            message = this.makePayment(name, fineID, cardType, cardNum);
        }
        return message;
    }
    
    //Reservation Entity
    @Override
    public List<String> getReservations(String isbn, String copyNum) {
        List entityList = new ArrayList();
        String message = "";
        if (!isbn.matches("[0-9 -]+") || isbn.startsWith("-")) {
            message = "ISBN is not valid.";
            entityList.add(message);
        } else if (!copyNum.matches("[0-9]+")) {
            message = "CopyNum is not valid.";
            entityList.add(message);
        } else {
            Boolean noReservation = true;
            Boolean noSuchBook = true;
            Query q1 = em.createQuery("SELECT be FROM Book be");
            for (Object o: q1.getResultList()) {
                BookEntity b = (BookEntity)o;
                if (b.getIsbn().equalsIgnoreCase(isbn) && b.getCopyNum().equalsIgnoreCase(copyNum)) {
                    noSuchBook = false;
                }
            }
            Query q2 = em.createQuery("SELECT re FROM Reservation re");
            for (Object o: q2.getResultList()) {
                ReservationEntity r = (ReservationEntity)o;
                if (r.getBook().getIsbn().equalsIgnoreCase(isbn) && r.getBook().getCopyNum().equalsIgnoreCase(copyNum)) {
                    //Got Reservation
                    noSuchBook = false;
                    noReservation = false;
                    String entity = new String();
                    entity = entity + "GotReservations#" + r.getReservationID();
                    entity = entity + "#" + r.getBook().getTitle();
                    entity = entity + "#" + r.getLibmember().getUserName();
                    entity = entity + "#" + r.getLibmember().getEmail();
                    entity = entity + "#" + new SimpleDateFormat("yyyy-MM-dd hh:mm").format(r.getReservationTime());
                    entity = entity + "#" + r.getNote();
                    entityList.add(entity);
                } 
            }
            if (noSuchBook) {
                message = "Book with ISBN (ISBN: " + isbn + ") and Copy Number (CopyNum: " + copyNum + ") does not exist in the system database.";
                entityList.add(message);
            }
            if (noReservation) {
                message = "There's no reservation(s) associated with this book.";
                entityList.add(message);
            }
        }
        System.out.println(entityList.toString());
        return entityList;
    }
    
    //Web (Reservation Entity)
    @Override
    public String doMakeReservation(Long bookID, String note, String name) {
        String message = "";
        if (note.equalsIgnoreCase("")) {
            message = "Note is empty.";
        } else {
            Date nowDateTime = new Date();
            BookEntity be = em.find(BookEntity.class, bookID);
            LibMemberEntity lme = em.find(LibMemberEntity.class, name);
            ReservationEntity re = new ReservationEntity(nowDateTime, note);
            re.setBook(be);
            re.setLibmember(lme);
            em.persist(re);
            be.getReservations().add(re);
            em.persist(be);
            lme.getReservations().add(re);
            em.persist(lme);
            message = "Reservation Successful!";
        }
        return message;
    }
    
    //Request Entity
    @Override
    public List<String> getRequest() {
        String message = "";
        Query q = em.createQuery("SELECT re FROM Request re");
        List entityList = new ArrayList();
        if (q == null) {
            message = "No requests found.";
            entityList.add(message);
        } else {
            for (Object o: q.getResultList()) {
                RequestEntity r = (RequestEntity)o;
                String entity = new String();
                if (r.getStatus().equalsIgnoreCase("unread")) {
                    entity = entity + r.getRequestID();
                    entity = entity + "#" + r.getLibmember().getUserName();
                    entity = entity + "#" + new SimpleDateFormat("yyyy-MM-dd hh:ss").format(r.getRequestTime());
                    entity = entity + "#" + r.getRequestMsg();
                    entityList.add(entity);
                }
            }
        }
        System.out.println(entityList.toString());
        return entityList;
    }
    
    @Override
    public String doUpdateRequest(String reqID, String status, String comment) {
        String message = "";
        Long requestID = Long.parseLong(reqID);
        RequestEntity re = em.find(RequestEntity.class, requestID);
        if (re == null) {
            //User d.n.e (error msg)
            message = "Invalid Request ID.";
        } else {
            re.setStatus(status);
            re.setComment(comment);
            em.persist(re);
            em.flush();
        }
        return message;
    }
    
    //Web (Request Entity)
    @Override
    public List<String> doViewRequest(String name) {
        String message = "";
        LibMemberEntity lme = em.find(LibMemberEntity.class, name);
        List entityList = new ArrayList();
        if (lme.getRequests().isEmpty()) {
            message = "You do not have any request(s).";
            entityList.add(message);
        } else {
            for (Object o: lme.getRequests()) {
                RequestEntity r = (RequestEntity)o;
                String entity = new String();
                String comment = "";
                entity = entity + r.getRequestID();
                entity = entity + "#" + new SimpleDateFormat("yyyy-MM-dd hh:ss").format(r.getRequestTime());
                entity = entity + "#" + r.getRequestMsg();
                entity = entity + "#" + r.getStatus();
                if (r.getComment().equalsIgnoreCase("")) {
                    comment = "null";
                } else {
                    comment = r.getComment();
                }
                entity = entity + "#" + comment;
                entityList.add(entity);
            }
        }
        System.out.println(entityList.toString());
        return entityList;
    }
    
    //General
    public void persist(Object object) {
        em.persist(object);
    }
    
    @Remove
    @Override
    public void remove() {
        System.out.println("LibServerBean:remove()");
    }

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
}
