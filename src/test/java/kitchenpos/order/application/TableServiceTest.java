package kitchenpos.order.application;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.domain.OrderTable;
import kitchenpos.order.domain.OrderTableRepository;
import kitchenpos.order.domain.TableGroup;
import kitchenpos.order.dto.OrderTableRequest;

@ExtendWith(MockitoExtension.class)
class TableServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private OrderTableRepository orderTableRepository;

    @InjectMocks
    private TableService tableService;

    @Test
    void create() {
        // given
        final OrderTableRequest orderTableRequest = new OrderTableRequest(1);

        // when
        tableService.create(orderTableRequest);

        // then
        verify(orderTableRepository).save(any(OrderTable.class));
    }

    @Test
    void list() {
        // when
        tableService.list();

        // then
        verify(orderTableRepository).findAll();
    }

    @Test
    void changeEmpty() {
        // given
        final Long orderTableId = 1L;
        final OrderTableRequest orderTableRequest = new OrderTableRequest();
        final TableGroup tableGroup = mock(TableGroup.class);
        final OrderTable savedOrderTable = new OrderTable(tableGroup, 1);
        given(orderTableRepository.findById(orderTableId)).willReturn(Optional.of(savedOrderTable));
        given(tableGroup.getId()).willReturn(null);

        // when
        tableService.changeEmpty(orderTableId, orderTableRequest);

        // then
        verify(orderTableRepository).save(savedOrderTable);
    }

    @Test
    void given_NotExistOrderTable_when_changeEmpty_then_ThrownException() {
        // given
        final Long notExistId = 1L;
        final OrderTableRequest orderTableRequest = new OrderTableRequest();
        given(orderTableRepository.findById(notExistId)).willReturn(Optional.empty());

        // when
        final Throwable notExistIdException = catchThrowable(
            () -> tableService.changeEmpty(notExistId, orderTableRequest));

        // then
        assertThat(notExistIdException).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void given_OrderTableHasTableGroupId_when_changeEmpty_then_ThrownException() {
        // given
        final Long orderTableId = 1L;
        final OrderTableRequest orderTableRequest = new OrderTableRequest();
        final TableGroup tableGroup = mock(TableGroup.class);
        final OrderTable notNullTableGroupId = new OrderTable(tableGroup, 1);
        given(orderTableRepository.findById(orderTableId)).willReturn(Optional.of(notNullTableGroupId));
        given(tableGroup.getId()).willReturn(1L);

        // when
        final Throwable notNullTableGroupIdException = catchThrowable(
            () -> tableService.changeEmpty(orderTableId, orderTableRequest));

        // then
        assertThat(notNullTableGroupIdException).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void changeNumberOfGuests() {
        // given
        final Long orderTableId = 1L;
        final OrderTableRequest orderTableRequest = new OrderTableRequest(1);
        final TableGroup tableGroup = mock(TableGroup.class);
        final OrderTable savedOrderTable = new OrderTable(tableGroup, 1);
        given(orderTableRepository.findById(orderTableId)).willReturn(Optional.of(savedOrderTable));
        given(tableGroup.getId()).willReturn(null);

        // when
        tableService.changeNumberOfGuests(orderTableId, orderTableRequest);

        // then
        verify(orderTableRepository).save(savedOrderTable);
        assertThat(savedOrderTable.getNumberOfGuests()).isEqualTo(orderTableRequest.getNumberOfGuests());
    }

    @Test
    void given_InvalidNumberOfGuests_when_ChangeNumberOfGuests_then_ThrownException() {
        // given
        final Long orderTableId = 1L;
        final OrderTableRequest minusGuestsOrderTableRequest = new OrderTableRequest();

        // when
        final Throwable invalidNumberOfGuests = catchThrowable(
            () -> tableService.changeNumberOfGuests(orderTableId, minusGuestsOrderTableRequest));

        // then
        assertThat(invalidNumberOfGuests).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void given_PersistedOrderTableHasEmpty_when_ChangeNumberOfGuests_then_ThrownException() {
        // given
        final Long orderTableId = 1L;
        final OrderTableRequest orderTableRequest = new OrderTableRequest(1);
        final TableGroup tableGroup = mock(TableGroup.class);
        final OrderTable savedOrderTable = new OrderTable(tableGroup, 1);
        savedOrderTable.changeEmpty(true);
        given(orderTableRepository.findById(orderTableId)).willReturn(Optional.of(savedOrderTable));
        given(tableGroup.getId()).willReturn(null);

        // when
        final Throwable emptyOrderTable = catchThrowable(
            () -> tableService.changeNumberOfGuests(orderTableId, orderTableRequest));

        // then
        assertThat(emptyOrderTable).isInstanceOf(IllegalArgumentException.class);
    }
}