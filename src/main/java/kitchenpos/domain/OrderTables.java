package kitchenpos.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;

@Embeddable
public class OrderTables {

    static final int MINIMUM_TABLES = 2;

    @OneToMany(mappedBy = "tableGroup", orphanRemoval = true)
    private List<OrderTable> orderTables = new ArrayList<>();

    protected OrderTables() {
    }

    public OrderTables(final List<OrderTable> orderTables) {
        validateGreaterOrEqualsMinimum(orderTables);

        this.orderTables = orderTables;
    }

    public static OrderTables of(final List<OrderTable> orderTables) {
        return new OrderTables(orderTables);
    }

    public List<OrderTable> getOrderTables() {
        return Collections.unmodifiableList(orderTables);
    }

    private void validateGreaterOrEqualsMinimum(List<OrderTable> orderTables) {
        if (orderTables.size() < MINIMUM_TABLES) {
            throw new IllegalArgumentException(
                String.format("최소 %d 개 이상의 테이블이 필요합니다.", MINIMUM_TABLES));
        }
    }
}
