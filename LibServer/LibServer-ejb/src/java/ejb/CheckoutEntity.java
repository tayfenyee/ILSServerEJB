/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;

/**
 *
 * @author USER
 */
@Entity(name="Checkout")
@Table(name="CheckoutTable")
public class CheckoutEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    private Long checkoutID;
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date loanDate;
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date dueDate;
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date returnDate;
    @ManyToOne
    private BookEntity book = new BookEntity();
    @ManyToOne
    private LibMemberEntity libmember = new LibMemberEntity();

    public CheckoutEntity() {
        setCheckoutID(System.nanoTime());
    }

    public CheckoutEntity(Date loanDate, Date dueDate, Date returnDate) {
        setCheckoutID(System.nanoTime());
        this.create(loanDate, dueDate, returnDate);
    }

    private void create(Date loanDate, Date dueDate, Date returnDate) {
        this.loanDate = loanDate;
        this.dueDate = dueDate;
        this.returnDate = returnDate;
    }

    public Long getCheckoutID() {
        return checkoutID;
    }

    public void setCheckoutID(Long checkoutID) {
        this.checkoutID = checkoutID;
    }

    public Date getLoanDate() {
        return loanDate;
    }

    public void setLoanDate(Date loanDate) {
        this.loanDate = loanDate;
    }

    public Date getDueDate() {
        return dueDate;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }

    public Date getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(Date returnDate) {
        this.returnDate = returnDate;
    }

    public BookEntity getBook() {
        return book;
    }

    public void setBook(BookEntity book) {
        this.book = book;
    }
    
    public LibMemberEntity getLibMember() {
        return libmember;
    }

    public void setLibMember(LibMemberEntity libMember) {
        this.libmember = libMember;
    }
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (checkoutID != null ? checkoutID.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CheckoutEntity)) {
            return false;
        }
        CheckoutEntity other = (CheckoutEntity) object;
        if ((this.checkoutID == null && other.checkoutID != null) || (this.checkoutID != null && !this.checkoutID.equals(other.checkoutID))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ejb.CheckoutEntity[ id=" + checkoutID + " ]";
    }
    
}
