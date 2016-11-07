/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

/**
 *
 * @author USER
 */
@Entity(name="Author")
@Table(name="AuthorTable")
public class AuthorEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long authorID;
    private String authorName;
    private String authorDescription;
    @ManyToMany(cascade={CascadeType.ALL}, mappedBy="author")
    private Set<BookEntity> book = new HashSet<>();

    public AuthorEntity() {
    }

    public AuthorEntity(String authorName, String authorDescription) {
        this.create(authorName, authorDescription);
    }
    
    private void create(String authorName, String authorDescription) {
        this.authorName = authorName;
        this.authorDescription = authorDescription;
    }

    public Long getAuthorID() {
        return authorID;
    }

    public void setAuthorID(Long authorID) {
        this.authorID = authorID;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public String getAuthorDescription() {
        return authorDescription;
    }

    public void setAuthorDescription(String authorDescription) {
        this.authorDescription = authorDescription;
    }

    public Set<BookEntity> getBook() {
        return book;
    }

    public void setBook(Set<BookEntity> book) {
        this.book = book;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (authorID != null ? authorID.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof AuthorEntity)) {
            return false;
        }
        AuthorEntity other = (AuthorEntity) object;
        if ((this.authorID == null && other.authorID != null) || (this.authorID != null && !this.authorID.equals(other.authorID))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ejb.AuthorEntity[ id=" + authorID + " ]";
    }
    
}
