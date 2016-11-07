package ejb;

import ejb.AuthorEntity;
import ejb.CheckoutEntity;
import ejb.ReservationEntity;
import javax.annotation.Generated;
import javax.persistence.metamodel.CollectionAttribute;
import javax.persistence.metamodel.SetAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2016-10-31T10:40:10")
@StaticMetamodel(BookEntity.class)
public class BookEntity_ { 

    public static volatile CollectionAttribute<BookEntity, CheckoutEntity> checkouts;
    public static volatile SingularAttribute<BookEntity, String> copyNum;
    public static volatile CollectionAttribute<BookEntity, ReservationEntity> reservations;
    public static volatile SetAttribute<BookEntity, AuthorEntity> author;
    public static volatile SingularAttribute<BookEntity, String> isbn;
    public static volatile SingularAttribute<BookEntity, String> publisher;
    public static volatile SingularAttribute<BookEntity, String> title;
    public static volatile SingularAttribute<BookEntity, String> publicationYr;
    public static volatile SingularAttribute<BookEntity, Long> bookID;

}