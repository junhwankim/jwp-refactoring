package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import kitchenpos.application.fixture.TableFixture;
import kitchenpos.application.fixture.TableGroupFixture;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.TableGroupDao;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayName("단체 지정 테스트")
@ExtendWith(MockitoExtension.class)
class TableGroupServiceTest {

    @Mock
    private OrderDao orderDao;

    @Mock
    private OrderTableDao orderTableDao;

    @Mock
    private TableGroupDao tableGroupDao;

    @InjectMocks
    private TableGroupService tableGroupService;

    private OrderTable 단체_테이블1;
    private OrderTable 단체_테이블2;

    @BeforeEach
    void setUp() {
        단체_테이블1 = TableFixture.create(1L, null, 3, true);
        단체_테이블2 = TableFixture.create(2L, null, 3, true);
    }

    @DisplayName("단체 지정을 할 수 있다.")
    @Test
    void create() {
        List<OrderTable> orderTables = Arrays.asList(단체_테이블1, 단체_테이블2);
        given(orderTableDao.findAllByIdIn(anyList())).willReturn(orderTables);

        TableGroup tableGroup = TableGroupFixture.createTableGroup(1L, orderTables);
        given(tableGroupDao.save(tableGroup)).willReturn(tableGroup);

        assertThat(tableGroupService.create(tableGroup)).isEqualTo(tableGroup);
    }

    @DisplayName("단체 지정을 할 때, 지정하려는 테이블 수가 2개 이하인 예외가 발생한다.")
    @Test
    void createImpossible1() {
        List<OrderTable> orderTables = Arrays.asList(단체_테이블1);
        TableGroup tableGroup = TableGroupFixture.createTableGroup(1L, orderTables);

        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("단체 지정을 할 때, 대상 테이블들이 모두 등록되어 있지 않으면 예외가 발생한다.")
    @Test
    void createImpossible2() {
        List<OrderTable> orderTables = Arrays.asList(단체_테이블1, 단체_테이블2);

        given(orderTableDao.findAllByIdIn(anyList())).willReturn(Collections.emptyList());

        TableGroup tableGroup = TableGroupFixture.createTableGroup(1L, orderTables);

        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("단체 지정을 할 때, 대상 테이블들이 빈 테이블이 아니면 예외가 발생한다.")
    @Test
    void createImpossible3() {
        OrderTable 비어있지않은_테이블1 = TableFixture.create(1L, null, 3, false);
        OrderTable 비어있지않은_테이블2 = TableFixture.create(2L, null, 3, false);

        List<OrderTable> orderTables = Arrays.asList(비어있지않은_테이블1, 비어있지않은_테이블2);
        given(orderTableDao.findAllByIdIn(anyList())).willReturn(orderTables);

        TableGroup tableGroup = TableGroupFixture.createTableGroup(1L, orderTables);

        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("단체 지정을 할 때, 대상 테이블들이 이미 단체로 지정된 테이블이면 예외가 발생한다.")
    @Test
    void createImpossible4() {
        OrderTable 단체로_지정된_테이블1 = TableFixture.create(1L, 1L, 3, true);
        OrderTable 단체로_지정된_테이블2 = TableFixture.create(2L, 1L, 3, true);

        List<OrderTable> orderTables = Arrays.asList(단체로_지정된_테이블1, 단체로_지정된_테이블2);
        given(orderTableDao.findAllByIdIn(anyList())).willReturn(orderTables);

        TableGroup tableGroup = TableGroupFixture.createTableGroup(1L, orderTables);

        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("단체 지정을 해제 할 수 있다.")
    @Test
    void ungroup() {
        OrderTable 단체로_지정된_테이블1 = TableFixture.create(1L, 1L, 3, true);
        OrderTable 단체로_지정된_테이블2 = TableFixture.create(2L, 1L, 3, true);

        List<OrderTable> orderTables = Arrays.asList(단체로_지정된_테이블1, 단체로_지정된_테이블2);
        given(orderTableDao.findAllByTableGroupId(1L)).willReturn(orderTables);

        given(orderDao.existsByOrderTableIdInAndOrderStatusIn(anyList(), anyList())).willReturn(false);

        tableGroupService.ungroup(1L);

        verify(orderTableDao, times(2)).save(any(OrderTable.class));
        assertThat(단체로_지정된_테이블1.getTableGroupId()).isNull();
        assertThat(단체로_지정된_테이블2.getTableGroupId()).isNull();
    }

    @DisplayName("단체 지정을 해제 할 때, 테이블의 상태가 조리나 식사중이면 예외가 발생한다.")
    @Test
    void ungroupImpossible1() {
        OrderTable 단체로_지정된_테이블1 = TableFixture.create(1L, 1L, 3, true);
        OrderTable 단체로_지정된_테이블2 = TableFixture.create(2L, 1L, 3, true);

        List<OrderTable> orderTables = Arrays.asList(단체로_지정된_테이블1, 단체로_지정된_테이블2);
        given(orderTableDao.findAllByTableGroupId(1L)).willReturn(orderTables);

        given(orderDao.existsByOrderTableIdInAndOrderStatusIn(anyList(), anyList())).willReturn(true);

        assertThatThrownBy(() -> tableGroupService.ungroup(1L))
            .isInstanceOf(IllegalArgumentException.class);
    }
}