package kitchenpos.menu.domain;

import java.math.BigDecimal;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    private Name name;

    @Embedded
    private Price price;

    protected Product() {
    }

    public Product(final String name, final BigDecimal price) {
        this.name = Name.of(name);
        this.price = Price.of(price);
    }

    public Long getId() {
        return id;
    }

    public Name getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return price.getPrice();
    }
}
