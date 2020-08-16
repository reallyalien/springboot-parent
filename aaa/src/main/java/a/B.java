package a;

import lombok.EqualsAndHashCode;
import org.neo4j.ogm.annotation.GeneratedValue;
import org.neo4j.ogm.annotation.Id;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Property;

@NodeEntity
public class B {

    @Id
    @GeneratedValue
    private Long id;

    @Property
    @EqualsAndHashCode.Exclude
    private String name;

}
