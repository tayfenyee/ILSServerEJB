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
@Entity(name="Reservation")
@Table(name="ReservationTable")
public class ReservationEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    private Long reservationID;
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date reservationTime;
    private String note;
    @ManyToOne
    private BookEntity book = new BookEntity();
    @ManyToOne
    private LibMemberEntity libmember = new LibMemberEntity();

    public ReservationEntity() {
        setReservationID(System.nanoTime());
    }

    public ReservationEntity(Date reservationTime, String note) {
        setReservationID(System.nanoTime());
        this.create(reservationTime, note);
    }
    
    private void create(Date reservationTime, String note) {
        this.reservationTime = reservationTime;
        this.note = note;
    }

    public Long getReservationID() {
        return reservationID;
    }

    public void setReservationID(Long reservationID) {
        this.reservationID = reservationID;
    }

    public Date getReservationTime() {
        return reservationTime;
    }

    public void setReservationTime(Date reservationTime) {
        this.reservationTime = reservationTime;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public BookEntity getBook() {
        return book;
    }

    public void setBook(BookEntity book) {
        this.book = book;
    }

    public LibMemberEntity getLibmember() {
        return libmember;
    }

    public void setLibmember(LibMemberEntity libmember) {
        this.libmember = libmember;
    }
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (reservationID != null ? reservationID.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ReservationEntity)) {
            return false;
        }
        ReservationEntity other = (ReservationEntity) object;
        if ((this.reservationID == null && other.reservationID != null) || (this.reservationID != null && !this.reservationID.equals(other.reservationID))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ejb.ReservationEntity[ id=" + reservationID + " ]";
    }
    
}
