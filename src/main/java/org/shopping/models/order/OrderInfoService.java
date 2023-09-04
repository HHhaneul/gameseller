package org.shopping.models.order;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.shopping.commons.ListData;
import org.shopping.commons.Pagination;
import org.shopping.commons.Utils;
import org.shopping.controllers.orders.OrderSearch;
import org.shopping.entities.OrderInfo;
import org.shopping.entities.OrderItem;
import org.shopping.entities.QOrderInfo;
import org.shopping.models.games.GameInfoService;
import org.shopping.repositories.order.OrderInfoRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderInfoService {
    private final EntityManager em;
    private final OrderInfoRepository repository;
    private final GameInfoService gameInfoService;
    private final HttpServletRequest request;

    public OrderInfo get(Long orderNo) {
        OrderInfo data = repository.findById(orderNo).orElseThrow(OrderNotFoundException::new);

        List<OrderItem> items = data.getOrderItems();
        if (items != null && !items.isEmpty()) {
            String gameNm = items.get(0).getGameNm();
            gameNm = items.size() > 1 ? gameNm += "외" + (items.size() - 1) + "건" : gameNm;
            data.setGameNm(gameNm);

            items.stream().forEach(i -> gameInfoService.addFileInfo(i.getGame()));
        }

        return data;
    }

    public ListData<OrderInfo> getList(OrderSearch search) {
        QOrderInfo orderInfo = QOrderInfo.orderInfo;
        BooleanBuilder andBuilder = new BooleanBuilder();

        int page = Utils.getNumber(search.getPage(), 1);
        int limit = Utils.getNumber(search.getLimit(), 20);
        int offset = (page - 1) * limit;
        /** 검색 처리 S */

        /** 검색 처리 E1 */
        /** 정렬 처리 S */
        // listOrder_DESC,createdAt_ASC
        List<OrderSpecifier> orderSpecifier = new ArrayList<>();
        String sort = search.getSort();
        if (sort != null && !sort.isBlank()) {
            List<String[]> sorts = Arrays.stream(sort.trim().split(","))
                    .map(s -> s.split("_")).toList();
            PathBuilder pathBuilder = new PathBuilder(OrderInfo.class, "orderInfo");

            for (String[] _sort : sorts) {
                Order direction = Order.valueOf(_sort[1].toUpperCase()); // 정렬 방향
                orderSpecifier.add(new OrderSpecifier(direction, pathBuilder.get(_sort[0])));
            }
        }
        /** 정렬 처리 E */

        JPAQueryFactory factory = new JPAQueryFactory(em);
        List<OrderInfo> items = factory.selectFrom(orderInfo)
                .leftJoin(orderInfo.member)
                .fetchJoin()
                .offset(offset)
                .limit(limit)
                .orderBy(orderSpecifier.toArray(OrderSpecifier[]::new))
                .fetch();

        items.stream().forEach(this::toAddInfo);

        ListData<OrderInfo> data = new ListData<>();
        data.setContent(items);

        int total = (int)repository.count(andBuilder);
        Pagination pagination = new Pagination(page, total, 10, limit, request);
        data.setPagination(pagination);

        return data;
    }

    private void toAddInfo(OrderInfo orderInfo) {
        List<OrderItem> items = orderInfo.getOrderItems();
        if (items == null || items.isEmpty()) return;

        items.stream().forEach(i -> gameInfoService.addFileInfo(i.getGame()));
    }
}
