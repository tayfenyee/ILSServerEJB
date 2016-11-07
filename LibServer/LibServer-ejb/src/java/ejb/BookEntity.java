/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 *
 * @author USER
 */
@Entity(name="Book")
@Table(name="BookTable")
public class BookEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long bookID;
    private String isbn;
    private String copyNum;
    private String title;
    private String publisher;
    private String publicationYr;
    
    @OneToMany(cascade={CascadeType.ALL}, mappedBy="book")
    private Collection<CheckoutEntity> checkouts = new ArrayList<>();
    
    @OneToMany(cascade={CascadeType.ALL}, mappedBy="book")
    private Collection<ReservationEntity> reservations = new ArrayList<>();
    
    @ManyToMany(cascade={CascadeType.PERSIST})
    @JoinTable(name="BOOK_AUTHORTABLE")
    private Set<AuthorEntity> author = new HashSet<>();
    
    public BookEntity(){
    }
    
    public BookEntity(String isbn, String copyNum, String title, String publisher, String publicationYr) {
        this.create(isbn, copyNum, title, publisher, publicationYr);
    }
    
    private void create(String isbn, String copyNum, String title, String publisher, String publicationYr) {
        this.isbn = isbn;
        this.copyNum = copyNum;
        this.title = title;
        this.publisher = publisher;
        this.publicationYr = publicationYr;
    }

    public Long getBookID() {
        return bookID;
    }

    public void setBookID(Long bookID) {
        this.bookID = bookID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getPublicationYr() {
        return publicationYr;
    }

    public void setPublicationYr(String publicationYr) {
        this.publicationYr = publicationYr;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getCopyNum() {
        return copyNum;
    }

    public void setCopyNum(String copyNum) {
        this.copyNum = copyNum;
    }

    public Set<AuthorEntity> getAuthor() {
        return author;
    }

    public void setAuthor(Set<AuthorEntity> author) {
        this.author = author;
    }
    
    public Collection<CheckoutEntity> getCheckouts() {
        return checkouts;
    }

    public void setCheckouts(Collection<CheckoutEntity> checkouts) {
        this.checkouts = checkouts;
    }

    public Collection<ReservationEntity> getReservations() {
        return reservations;
    }

    public void setReservations(Collection<ReservationEntity> reservations) {
        this.reservations = reservations;
    }
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (bookID != null ? bookID.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof BookEntity)) {
            return false;
        }
        BookEntity other = (BookEntity) object;
        if ((this.bookID == null && other.bookID != null) || (this.bookID != null && !this.bookID.equals(other.bookID))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ejb.BookEntity[ id=" + bookID + " ]";
    }
}
