/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 *
 * @author USER
 */
@Entity(name="LibMember")
//@EntityListeners(LibEntityCallbacks.class)
@Table(name="LibMemberTable")
public class LibMemberEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    private String userName;
    private String password;
    private String contactNum;
    private String email;
    private String address;
    
    @OneToMany(cascade={CascadeType.ALL}, mappedBy="libmember")
    private Collection<CheckoutEntity> checkouts = new ArrayList<>();
    
    @OneToMany(cascade={CascadeType.ALL}, mappedBy="libmember")
    private Collection<FineEntity> fines = new ArrayList<>();
    
    @OneToMany(cascade={CascadeType.ALL}, mappedBy="libmember")
    private Collection<RequestEntity> requests = new ArrayList<>();
    
    @OneToMany(cascade={CascadeType.ALL}, mappedBy="libmember")
    private Collection<ReservationEntity> reservations = new ArrayList<>();

    public LibMemberEntity() {
    }
    
    public LibMemberEntity(String name, String password, String contact, String email, String addr) {
        this.create(name, password, contact, email, addr);
    }
    
    private void create(String name, String password, String contact, String email, String addr) {
        this.userName = name;
        this.password = password;
        this.contactNum = contact;
        this.email = email;
        this.address = addr;
    }
    
    public String getUserName() {
        return userName;
    }

    public void setUserName(String name) {
        this.userName = name;
    }
    
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getContactNum() {
        return contactNum;
    }

    public void setContactNum(String contact) {
        this.contactNum = contact;
    }
    
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getAddress() {
        return address;
    }

    public void setAddress(String addr) {
        this.address = addr;
    }
    
    public Collection<CheckoutEntity> getCheckouts() {
        return checkouts;
    }

    public void setCheckouts(Collection<CheckoutEntity> checkouts) {
        this.checkouts = checkouts;
    }
    
    public Collection<FineEntity> getFines() {
        return fines;
    }

    public void setFines(Collection<FineEntity> fines) {
        this.fines = fines;
    }

    public Collection<RequestEntity> getRequests() {
        return requests;
    }

    public void setRequests(Collection<RequestEntity> requests) {
        this.requests = requests;
    }

    public Collection<ReservationEntity> getReservations() {
        return reservations;
    }

    public void setReservations(Collection<ReservationEntity> reservations) {
        this.reservations = reservations;
    }
}
