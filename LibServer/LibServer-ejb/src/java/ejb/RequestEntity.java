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
@Entity(name="Request")
@Table(name="RequestTable")
public class RequestEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    private Long requestID;
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date requestTime;
    private String requestMsg;
    private String status;
    private String comment;
    @ManyToOne
    private LibMemberEntity libmember = new LibMemberEntity();

    public RequestEntity() {
        setRequestID(System.nanoTime());
    }

    public RequestEntity(Date requestTime, String requestMsg, String status, String comment) {
        setRequestID(System.nanoTime());
        this.create(requestTime, requestMsg, status, comment);
    }
    
    private void create(Date requestTime, String requestMsg, String status, String comment) {
        this.requestTime = requestTime;
        this.requestMsg = requestMsg;
        this.status = status;
        this.comment = comment;
    }

    public Long getRequestID() {
        return requestID;
    }

    public void setRequestID(Long requestID) {
        this.requestID = requestID;
    }

    public Date getRequestTime() {
        return requestTime;
    }

    public void setRequestTime(Date requestTime) {
        this.requestTime = requestTime;
    }

    public String getRequestMsg() {
        return requestMsg;
    }

    public void setRequestMsg(String requestMsg) {
        this.requestMsg = requestMsg;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
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
        hash += (requestID != null ? requestID.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof RequestEntity)) {
            return false;
        }
        RequestEntity other = (RequestEntity) object;
        if ((this.requestID == null && other.requestID != null) || (this.requestID != null && !this.requestID.equals(other.requestID))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ejb.RequestEntity[ id=" + requestID + " ]";
    }
    
}
