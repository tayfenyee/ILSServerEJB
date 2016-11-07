package ejb;

import ejb.LibMemberEntity;
import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2016-10-31T10:40:10")
@StaticMetamodel(RequestEntity.class)
public class RequestEntity_ { 

    public static volatile SingularAttribute<RequestEntity, Date> requestTime;
    public static volatile SingularAttribute<RequestEntity, LibMemberEntity> libmember;
    public static volatile SingularAttribute<RequestEntity, Long> requestID;
    public static volatile SingularAttribute<RequestEntity, String> requestMsg;
    public static volatile SingularAttribute<RequestEntity, String> comment;
    public static volatile SingularAttribute<RequestEntity, String> status;

}