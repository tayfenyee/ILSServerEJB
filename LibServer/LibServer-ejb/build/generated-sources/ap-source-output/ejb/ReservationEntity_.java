package ejb;

import ejb.BookEntity;
import ejb.LibMemberEntity;
import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2016-10-31T10:40:10")
@StaticMetamodel(ReservationEntity.class)
public class ReservationEntity_ { 

    public static volatile SingularAttribute<ReservationEntity, LibMemberEntity> libmember;
    public static volatile SingularAttribute<ReservationEntity, String> note;
    public static volatile SingularAttribute<ReservationEntity, Long> reservationID;
    public static volatile SingularAttribute<ReservationEntity, Date> reservationTime;
    public static volatile SingularAttribute<ReservationEntity, BookEntity> book;

}