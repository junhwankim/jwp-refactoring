package kitchenpos.domain;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class MenuGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    private Name name;

    public MenuGroup() {
    }

    public MenuGroup(final String name) {
        this.name = Name.of(name);
    }

    public MenuGroup(final Long id, final String name) {
        this.id = id;
        this.name = Name.of(name);
    }

    public Long getId() {
        return id;
    }

    public Name getName() {
        return name;
    }
}
