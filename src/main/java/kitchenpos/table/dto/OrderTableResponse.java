package kitchenpos.table.dto;

import kitchenpos.table.domain.OrderTable;

public class OrderTableResponse {

    private Long id;
    private Long tableGroupId;
    private int numberOfGuests;
    private boolean empty;

    public OrderTableResponse() {
        // empty
    }

    private OrderTableResponse(Long id, Long tableGroupId, int numberOfGuests, boolean empty) {
        this.id = id;
        this.tableGroupId = tableGroupId;
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public static OrderTableResponse of(final OrderTable orderTable) {
        return new OrderTableResponse(orderTable.getId(),
                                      null,
                                      orderTable.getNumberOfGuests(),
                                      orderTable.isEmpty());
    }


    public static OrderTableResponse of(final Long tableGroupId, final OrderTable orderTable) {
        return new OrderTableResponse(orderTable.getId(),
                                      tableGroupId,
                                      orderTable.getNumberOfGuests(),
                                      orderTable.isEmpty());
    }

    public Long getId() {
        return id;
    }

    public Long getTableGroupId() {
        return tableGroupId;
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }

    public boolean isEmpty() {
        return empty;
    }
}