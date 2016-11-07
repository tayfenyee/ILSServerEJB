package ejb;

import ejb.CheckoutEntity;
import ejb.FineEntity;
import ejb.RequestEntity;
import ejb.ReservationEntity;
import javax.annotation.Generated;
import javax.persistence.metamodel.CollectionAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2016-10-31T10:40:10")
@StaticMetamodel(LibMemberEntity.class)
public class LibMemberEntity_ { 

    public static volatile SingularAttribute<LibMemberEntity, String> contactNum;
    public static volatile SingularAttribute<LibMemberEntity, String> password;
    public static volatile SingularAttribute<LibMemberEntity, String> address;
    public static volatile CollectionAttribute<LibMemberEntity, CheckoutEntity> checkouts;
    public static volatile CollectionAttribute<LibMemberEntity, ReservationEntity> reservations;
    public static volatile CollectionAttribute<LibMemberEntity, FineEntity> fines;
    public static volatile CollectionAttribute<LibMemberEntity, RequestEntity> requests;
    public static volatile SingularAttribute<LibMemberEntity, String> userName;
    public static volatile SingularAttribute<LibMemberEntity, String> email;

}