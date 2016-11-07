/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;

/**
 *
 * @author USER
 */
@Entity(name="Fine")
@Table(name="FineTable")
public class FineEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    private Long fineID;
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date fineDate;
    private double fineAmt;
    @ManyToOne
    private LibMemberEntity libmember = new LibMemberEntity();
    @OneToOne(cascade={CascadeType.PERSIST})
    private PaymentEntity payment;

    public FineEntity() {
        setFineID(System.nanoTime());
    }

    public FineEntity(Date fineDate, double fineAmt) {
        setFineID(System.nanoTime());
        this.create(fineDate, fineAmt);
    }
    
    private void create(Date fineDate, double fineAmt) {
        this.fineDate = fineDate;
        this.fineAmt = fineAmt;
    }

    public Long getFineID() {
        return fineID;
    }

    public void setFineID(Long fineID) {
        this.fineID = fineID;
    }

    public Date getFineDate() {
        return fineDate;
    }

    public void setFineDate(Date fineDate) {
        this.fineDate = fineDate;
    }

    public double getFineAmt() {
        return fineAmt;
    }

    public void setFineAmt(double fineAmt) {
        this.fineAmt = fineAmt;
    }

    public LibMemberEntity getLibmember() {
        return libmember;
    }

    public void setLibmember(LibMemberEntity libmember) {
        this.libmember = libmember;
    }

    public PaymentEntity getPayment() {
        return payment;
    }

    public void setPayment(PaymentEntity payment) {
        this.payment = payment;
    }
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (fineID != null ? fineID.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof FineEntity)) {
            return false;
        }
        FineEntity other = (FineEntity) object;
        if ((this.fineID == null && other.fineID != null) || (this.fineID != null && !this.fineID.equals(other.fineID))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ejb.FineEntity[ id=" + fineID + " ]";
    }
    
}
