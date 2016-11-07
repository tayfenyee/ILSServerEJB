/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package libstaffclient;

import ejb.LibServerBeanRemote;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.StringTokenizer;
import javax.ejb.EJB;

/**
 *
 * @author USER
 */
public class Main implements Serializable{

    @EJB
    private static LibServerBeanRemote libServerBean;

    /**
     * @param args the command line arguments
     */
    
    public Main() {
    }
    
    public static void main(String[] args) {
        // TODO code application logic here
        Main client = new Main();
        client.loadStartScreen();
        libServerBean.remove();
    }
    
    public void loadStartScreen(){
        try {
            String choice = "";
            Scanner sc = new Scanner(System.in);
            while (!choice.equalsIgnoreCase("0")) {
                System.out.println("\n\n\t\tWelcome to ILS Admin Portal");
                System.out.println("1a. Add New Library Member");
                System.out.println("1b. Delete Existing Library Member");
                System.out.println("1c. View List of Library Members");
                System.out.println();
                System.out.println("2a. Add New Author");
                System.out.println("2b. View List of Authors");
                System.out.println();
                System.out.println("3a. Add New Book");
                System.out.println("3b. Update Book");
                System.out.println("3c. Delete Exixting Book");
                System.out.println();
                System.out.println("4a. Checkout Book");
                System.out.println("4b. View Current Checkouts");
                System.out.println("4c. Return Book");
                System.out.println();
                System.out.println("5a. View Book Reservations");
                System.out.println("5b. View Unpaid Fine");
                System.out.println();
                System.out.println("6. Process Request");
                System.out.println("\n0.  Exit");
                System.out.print("\nEnter Choice: ");
                choice = sc.nextLine();
                if (choice.equalsIgnoreCase("1a")) {
                    loadAddMemberScreen();
                } else if (choice.equalsIgnoreCase("1b")) {
                    loadDeleteMemberScreen();
                } else if (choice.equalsIgnoreCase("1c")) {
                    loadDisplayMemberScreen();
                } else if (choice.equalsIgnoreCase("2a")) {
                    loadAddAuthorScreen();
                } else if (choice.equalsIgnoreCase("2b")) {
                    loadDisplayAuthorScreen();
                } else if (choice.equalsIgnoreCase("3a")) {
                    loadAddBookScreen();
                } else if (choice.equalsIgnoreCase("3b")) {
                    loadUpdateBookScreen();
                } else if (choice.equalsIgnoreCase("3c")) {
                    loadDeleteBookScreen();
                } else if (choice.equalsIgnoreCase("4a")) {
                    loadCheckoutBookScreen();
                } else if (choice.equalsIgnoreCase("4b")) {
                    loadDisplayCheckoutScreen();
                } else if (choice.equalsIgnoreCase("4c")) {
                    loadReturnBookScreen();
                } else if (choice.equalsIgnoreCase("5a")) {
                    loadDisplayReservationScreen();
                } else if (choice.equalsIgnoreCase("5b")) {
                    loadDisplayUnpaidFineScreen();
                } else if (choice.equalsIgnoreCase("6")) {
                    loadDisplayRequestScreen();
                } else if (choice.equalsIgnoreCase("0")) {
                    System.out.println("Thank you for using ILS.");
                    return;
                } else {
                    System.out.println("Error: Invalid Choice");
                }
            } return;
        } catch (Exception ex) {
            System.err.println("Caught an unexpected exception!");
            ex.printStackTrace();
        }
    }
    
    public void loadAddMemberScreen() {
        String userName, password, contactNum, email, address;
        String message;
        
        try {
            System.out.println("\n\n\t\tAdd New Library Member");
            userName    = getString("Name", null);
            password    = getString("Password", null);
            contactNum  = getString("TelNumber", null);
            email       = getString("Email", null);
            address     = getString("Address", null);
            
            message = libServerBean.doCreateLibMember(userName, password, contactNum, email, address);
            
            if (message.equalsIgnoreCase("")) {
                System.out.println("\nMember added successfully.\n");
            } else {
                System.out.println("\nFailed to add new member.\nReason: " + message + "\n");
            }
        } catch (Exception ex) {
            System.out.println("\nSystem Error: " + ex.getMessage() + "\n");
        }
    }
    
    public String getString(String attrName, String oldValue) {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String stringValue = null;
        
        try {
            while (true) {
                System.out.print("Enter " + attrName + (oldValue==null?"":"(" + oldValue + ")") + " : ");
                stringValue = br.readLine();
                if (stringValue.trim().length() != 0) {
                    break;
                } else if (stringValue.trim().length() == 0 && oldValue != null) {
                    stringValue = oldValue;
                    break;
                }
                System.out.println("Invalid " + attrName + " ...");
            }
        } catch (Exception ex) {
            System.out.println("\nSystem Error: " + ex.getMessage() + "\n");
        }
        return stringValue.trim();
    }
    
    public void loadDeleteMemberScreen() {
        String name, message;
        try {
            System.out.println("\n\n\t\tDelete Existing Library Member");
            System.out.print("Enter Name to Delete Member: ");
            Scanner sc = new Scanner(System.in);
            name = sc.nextLine().trim();
            
            message = libServerBean.doDeleteProfile(name);
            
            if (message.equalsIgnoreCase("")) {
                System.out.println("\nMember deleted successfully.\n");
            } else {
                System.out.println("\nFailed to delete existing member.\nReason: " + message + "\n");
            }
        } catch (Exception ex) {
            System.out.println("\nSystem Error: " + ex.getMessage() + "\n");
        }
    }
    
    public void loadDisplayMemberScreen() {
        try {
            System.out.println("\n\n\t\tList of Library Members");
            for (Object o: libServerBean.getMembers()) {
                String libMember = (String)o;
                StringTokenizer st = new StringTokenizer(libMember, "#");
                System.out.println("Name        = " + st.nextToken());
                System.out.println("Contact No. = " + st.nextToken());
                System.out.println("Email       = " + st.nextToken());
                System.out.println("Address     = " + st.nextToken());
                System.out.println();
            }
        } catch (Exception ex) {
            System.out.println("\nSystem Error: " + ex.getMessage() + "\n");
        }
    }
    
    public void loadAddAuthorScreen() {
        String authorName, authorDescription;
        String message;
        
        try {
            System.out.println("\n\n\t\tAdd New Author");
            authorName          = getString("Name", null);
            authorDescription   = getString("Description", null);
            
            message = libServerBean.doCreateAuthor(authorName, authorDescription);
            
            if (message.equalsIgnoreCase("")) {
                System.out.println("\nAuthor added successfully.\n");
            } else {
                System.out.println("\nFailed to add new author.\nReason: " + message + "\n");
            }
        } catch (Exception ex) {
            System.out.println("\nSystem Error: " + ex.getMessage() + "\n");
        }
    }
    
    public void loadDisplayAuthorScreen() {
        try {
            String format = "%-20s%-40s%-60s\n";
            System.out.println("\n\n\t\tList of Authors");
            System.out.printf(format, "Author ID", "Author", "Description");
            for (Object o: libServerBean.getAuthors()) {
                String authors = (String)o;
                StringTokenizer st = new StringTokenizer(authors, "#");
                System.out.printf(format, st.nextToken(), st.nextToken(), st.nextToken());
            }
        } catch (Exception ex) {
            System.out.println("\nSystem Error: " + ex.getMessage() + "\n");
        }
    }
    
    public void loadAddBookScreen() {
        String isbn, copyNum, title, publisher, publicationYr, authorIdList;
        String message;
        
        try {
            System.out.println("\n\n\t\tAdd New Book");
            isbn            = getString("ISBN", null);
            copyNum         = getString("Copy Number", null);
            title           = getString("Title", null);
            publisher       = getString("Publisher", null);
            publicationYr   = getString("Publication Year", null);
            authorIdList    = getString("Author ID (Separate multiple authors with ',')", null);
            
            message = libServerBean.doCreateBook(isbn, copyNum, title, publisher, publicationYr, authorIdList);
            
            if (message.equalsIgnoreCase("")) {
                System.out.println("\nBook added successfully.\n");
            } else {
                System.out.println("\nFailed to add new book.\nReason: " + message + "\n");
            }
        } catch (Exception ex) {
            System.out.println("\nSystem Error: " + ex.getMessage() + "\n");
        }
    }
    
    public void loadUpdateBookScreen() {
        String isbn, copyNum, title, publisher, publicationYr, authorIdList;
        String message, message2, unwanted;
        Long bookID;
        
        try {
            System.out.println("\n\n\t\tUpdate Book");
            System.out.println("Enter ISBN and Copy Number to Update Book:");
            isbn            = getString("ISBN", null);
            copyNum         = getString("Copy Number", null);
            
            message = libServerBean.doCheckUpdateBook(isbn, copyNum);
            
            if (message.startsWith("CanUpdate")) {
                //Not tied to any checkout or reservations
                System.out.println("\nExisting record:");
                StringTokenizer st = new StringTokenizer(message, "#");
                unwanted = st.nextToken();
                bookID = Long.parseLong(st.nextToken());
                System.out.println("ISBN            = " + st.nextToken());
                System.out.println("Copy Number     = " + st.nextToken());
                System.out.println("Title           = " + st.nextToken());
                System.out.println("Publisher       = " + st.nextToken());
                System.out.println("Publication Yr  = " + st.nextToken());
                System.out.println("Author ID(s)    = " + st.nextToken());
                System.out.println();
                
                System.out.println("Enter to update record:");
                isbn            = getString("ISBN", null);
                copyNum         = getString("Copy Number", null);
                title           = getString("Title", null);
                publisher       = getString("Publisher", null);
                publicationYr   = getString("Publication Yr", null);
                authorIdList    = getString("Author ID (Separate multiple authors with ',')", null);
                
                message2 = libServerBean.doUpdateBook(bookID, isbn, copyNum, title, publisher, publicationYr, authorIdList);
                
                if (message2.equalsIgnoreCase("")) {
                    System.out.println("\nBook updated successfully.\n");
                } else {
                    System.out.println("\nFailed to update book.\nReason: " + message2);
                }
            } else {
                System.out.println("\nOperation not allowed.\nReason: " + message + "\n");
            }
        } catch (Exception ex) {
            System.out.println("\nSystem Error: " + ex.getMessage() + "\n");
        }
    }
    
    public void loadDeleteBookScreen() {
        String isbn, copyNum, message;
        try {
            System.out.println("\n\n\t\tDelete Existing Book");
            System.out.println("Enter ISBN and Copy Number to Delete Book:");
            isbn            = getString("ISBN", null);
            copyNum         = getString("Copy Number", null);
            
            message = libServerBean.doDeleteBook(isbn, copyNum);
            
            if (message.equalsIgnoreCase("")) {
                System.out.println("\nBook deleted successfully.\n");
            } else {
                System.out.println("\nFailed to delete existing book.\nReason: " + message);
            }
        } catch (Exception ex) {
            System.out.println("\nSystem Error: " + ex.getMessage() + "\n");
        }
    }
    
    public void loadCheckoutBookScreen() {
        String isbn, copyNum, memberName;
        String message;
        
        try {
            System.out.println("\n\n\t\tCheckout Book");
            isbn            = getString("ISBN", null);
            copyNum         = getString("Copy Number", null);
            memberName      = getString("Member Name", null);
            
            message = libServerBean.doCreateCheckout(memberName, isbn, copyNum);
            
            if (message.equalsIgnoreCase("")) {
                System.out.println("\nBook checked out successfully.\n");
            } else {
                System.out.println("\nFailed to checkout book.\nReason: \n" + message);
            }
        } catch (Exception ex) {
            System.out.println("\nSystem Error: " + ex.getMessage() + "\n");
        }
    }
    
    public void loadDisplayCheckoutScreen() {
        try {
            String format = "%-25s%-25s%-15s%-40s%-30s%-40s%-20s%-20s\n";
            System.out.println("\n\n\t\tCheckout Records");
            System.out.printf(format, "Checkout ID", "ISBN", "Copy Number", "Title", "Member Name", "Email", "Loan Date", "Due Date");
            for (Object o: libServerBean.getCheckouts()) {
                String checkouts = (String)o;
                StringTokenizer st = new StringTokenizer(checkouts, "#");
                System.out.printf(format, st.nextToken(), st.nextToken(), st.nextToken(), st.nextToken(), st.nextToken(), st.nextToken(), st.nextToken(), st.nextToken());
            }
        } catch (Exception ex) {
            System.out.println("\nSystem Error: " + ex.getMessage() + "\n");
        }
    }
    
    public void loadReturnBookScreen() {
        String isbn, copyNum;
        String message;
        
        try {
            System.out.println("\n\n\t\tReturn Book");
            isbn            = getString("ISBN", null);
            copyNum         = getString("Copy Number", null);
            
            message = libServerBean.doCreateReturn(isbn, copyNum);
            
            if (message.equalsIgnoreCase("")) {
                System.out.println("\nBook returned successfully.\n");
            } else if (message.startsWith("Book returned successfully")) {
                System.out.println("\n" + message);
            } else {
                System.out.println("\nFailed to return book.\nReason: \n" + message);
            }
        } catch (Exception ex) {
            System.out.println("\nSystem Error: " + ex.getMessage() + "\n");
        }
    }
    
    public void loadDisplayUnpaidFineScreen() {
        try {
            String format = "%-25s%-30s%-40s%-20s%-20s\n";
            System.out.println("\n\n\t\tUnpaid Fine Records");
            System.out.printf(format, "Fine ID", "Member Name", "Email", "Fine Date", "Fine Amount");
            for (Object o: libServerBean.getUnpaidFines()) {
                String unpaidFines = (String)o;
                StringTokenizer st = new StringTokenizer(unpaidFines, "#");
                System.out.printf(format, st.nextToken(), st.nextToken(), st.nextToken(), st.nextToken(), st.nextToken());
            }
        } catch (Exception ex) {
            System.out.println("\nSystem Error: " + ex.getMessage() + "\n");
        }
    }
    
    public void loadDisplayReservationScreen() {
        String isbn, copyNum;
        ArrayList message;
        
        try {
            System.out.println("\n\n\t\tView Reservation");
            isbn            = getString("ISBN", null);
            copyNum         = getString("Copy Number", null);
            
            message = (ArrayList)libServerBean.getReservations(isbn, copyNum);
            
            if (message.toString().startsWith("[GotReservations")) {
                System.out.println();
                String format = "%-25s%-30s%-25s%-35s%-25s%-40s\n";
                System.out.printf(format, "Reservation ID", "Title", "Member Name", "Email", "Reservation Date", "Note");
            
                for (Object o: message) {
                    String msg = (String)o;
                    StringTokenizer st = new StringTokenizer(msg, "#");
                    String unwanted = st.nextToken();
                    System.out.printf(format, st.nextToken(), st.nextToken(), st.nextToken(), st.nextToken(), st.nextToken(), st.nextToken());
                }
            } else {
                String msg = message.toString();
                System.out.println("\nFailed to view reservations.\nReason: \n" + msg.substring(1, msg.length()-1));
            }
        } catch (Exception ex) {
            System.out.println("\nSystem Error: " + ex.getMessage() + "\n");
        }
    }
    
    public void loadDisplayRequestScreen() {
        try {
            Scanner sc = new Scanner(System.in);
            String message = "";
            String format = "%-25s%-30s%-30s%-40s\n";
            System.out.println("\n\n\t\tRequest Records");
            System.out.printf(format, "Request ID", "Member Name", "Request Time", "Message");
            for (Object o: libServerBean.getRequest()) {
                String requests = (String)o;
                StringTokenizer st = new StringTokenizer(requests, "#");
                System.out.printf(format, st.nextToken(), st.nextToken(), st.nextToken(), st.nextToken());
            }
            System.out.println("\n\n\t\tProcess Request");
            System.out.println("Enter '0' if you do not wish to process any request.");
            System.out.println("Hit 'Enter' to continue.");
            if (!sc.nextLine().equals("0")) {
                String requestID = getString("RequestID", null);
                String status    = getString("Status", null);
                String comment   = getString("Comment", null);
            
                message = libServerBean.doUpdateRequest(requestID, status, comment);
            
                if (message.equalsIgnoreCase("")) {
                    System.out.println("\nRequest updated successfully.\n");
                } else {
                    System.out.println("\nFailed to update request.\nReason: " + message + "\n");
                }
            } else {
                return;
            }
            
        } catch (Exception ex) {
            System.out.println("\nSystem Error: " + ex.getMessage() + "\n");
        }
    }
    
}
