/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb;

import javax.persistence.PostLoad;
import javax.persistence.PostPersist;
import javax.persistence.PostRemove;
import javax.persistence.PostUpdate;
import javax.persistence.PrePersist;
import javax.persistence.PreRemove;
import javax.persistence.PreUpdate;

/**
 *
 * @author USER
 */
public class LibEntityCallbacks {
    @PrePersist
    public void prePersist(LibMemberEntity l) {
        System.out.println("LibMemberEntityCallbacks:prePersist " + l);
    }
    
    @PostPersist
    public void postPersist(LibMemberEntity l) {
        System.out.println("LibMemberEntityCallbacks:postPersist " + l);
    }
    
    @PreRemove
    public void preRemove(LibMemberEntity l) {
        System.out.println("LibMemberEntityCallbacks:preRemove " + l);
    }
    
    @PostRemove
    public void postRemove(LibMemberEntity l) {
        System.out.println("LibMemberEntityCallbacks:postRemove " + l);
    }
    
    @PreUpdate
    public void preUpdate(LibMemberEntity l) {
        System.out.println("LibMemberEntityCallbacks:preUpdate " + l);
    }
    
    @PostUpdate
    public void postUpdate(LibMemberEntity l) {
        System.out.println("LibMemberEntityCallbacks:postUpdate " + l);
    }
    
    @PostLoad
    public void postLoad(LibMemberEntity l) {
        System.out.println("LibMemberEntityCallbacks:postLoad " + l);
    }
}
