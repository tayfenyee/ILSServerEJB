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
import javax.persistence.Table;
import javax.persistence.Temporal;

/**
 *
 * @author USER
 */
@Entity(name="Payment")
@Table(name="PaymentTable")
public class PaymentEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    private Long paymentID;
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date paymentTime;
    private String cardType;
    private String cardNumber;
    private String cardHolderName;

    public PaymentEntity() {
        setPaymentID(System.nanoTime());
    }

    public PaymentEntity(Date paymentTime, String cardType, String cardNumber, String cardHolderName) {
        setPaymentID(System.nanoTime());
        this.create(paymentTime, cardType, cardNumber, cardHolderName);
    }
    
    private void create(Date paymentTime, String cardType, String cardNumber, String cardHolderName) {
        this.paymentTime = paymentTime;
        this.cardType = cardType;
        this.cardNumber = cardNumber;
        this.cardHolderName = cardHolderName;
    }

    public Long getPaymentID() {
        return paymentID;
    }

    public void setPaymentID(Long paymentID) {
        this.paymentID = paymentID;
    }

    public Date getPaymentTime() {
        return paymentTime;
    }

    public void setPaymentTime(Date paymentTime) {
        this.paymentTime = paymentTime;
    }

    public String getCardType() {
        return cardType;
    }

    public void setCardType(String cardType) {
        this.cardType = cardType;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getCardHolderName() {
        return cardHolderName;
    }

    public void setCardHolderName(String cardHolderName) {
        this.cardHolderName = cardHolderName;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (paymentID != null ? paymentID.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof PaymentEntity)) {
            return false;
        }
        PaymentEntity other = (PaymentEntity) object;
        if ((this.paymentID == null && other.paymentID != null) || (this.paymentID != null && !this.paymentID.equals(other.paymentID))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ejb.PaymentEntity[ id=" + paymentID + " ]";
    }
    
}
