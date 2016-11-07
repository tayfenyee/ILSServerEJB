package ejb;

import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2016-10-31T10:40:10")
@StaticMetamodel(PaymentEntity.class)
public class PaymentEntity_ { 

    public static volatile SingularAttribute<PaymentEntity, String> cardHolderName;
    public static volatile SingularAttribute<PaymentEntity, Long> paymentID;
    public static volatile SingularAttribute<PaymentEntity, String> cardType;
    public static volatile SingularAttribute<PaymentEntity, Date> paymentTime;
    public static volatile SingularAttribute<PaymentEntity, String> cardNumber;

}