package ejb;

import ejb.BookEntity;
import ejb.LibMemberEntity;
import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2016-10-31T10:40:10")
@StaticMetamodel(CheckoutEntity.class)
public class CheckoutEntity_ { 

    public static volatile SingularAttribute<CheckoutEntity, LibMemberEntity> libmember;
    public static volatile SingularAttribute<CheckoutEntity, Date> returnDate;
    public static volatile SingularAttribute<CheckoutEntity, Date> dueDate;
    public static volatile SingularAttribute<CheckoutEntity, BookEntity> book;
    public static volatile SingularAttribute<CheckoutEntity, Date> loanDate;
    public static volatile SingularAttribute<CheckoutEntity, Long> checkoutID;

}