package ejb;

import ejb.LibMemberEntity;
import ejb.PaymentEntity;
import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2016-10-31T10:40:10")
@StaticMetamodel(FineEntity.class)
public class FineEntity_ { 

    public static volatile SingularAttribute<FineEntity, LibMemberEntity> libmember;
    public static volatile SingularAttribute<FineEntity, Date> fineDate;
    public static volatile SingularAttribute<FineEntity, PaymentEntity> payment;
    public static volatile SingularAttribute<FineEntity, Double> fineAmt;
    public static volatile SingularAttribute<FineEntity, Long> fineID;

}