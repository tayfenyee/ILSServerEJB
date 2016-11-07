package ejb;

import ejb.BookEntity;
import javax.annotation.Generated;
import javax.persistence.metamodel.SetAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2016-10-31T10:40:10")
@StaticMetamodel(AuthorEntity.class)
public class AuthorEntity_ { 

    public static volatile SingularAttribute<AuthorEntity, String> authorName;
    public static volatile SetAttribute<AuthorEntity, BookEntity> book;
    public static volatile SingularAttribute<AuthorEntity, String> authorDescription;
    public static volatile SingularAttribute<AuthorEntity, Long> authorID;

}