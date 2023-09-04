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
import org.shopping.commons.constants.OrderStatus;
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
import java.util.Objects;

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
        Long orderNo = search.getOrderNo();
        Long[] orderNos = search.getOrderNos();
        OrderStatus status = search.getStatus();
        OrderStatus[] statuses = search.getStatuses();
        String sopt = search.getSopt();
        String skey = search.getSkey();

        /** 주문 번호 검색 처리 - orderNo, orderNos S */
        if (orderNo != null) andBuilder.and(orderInfo.orderNo.eq(orderNo));
        if (orderNos != null && orderNos.length > 0) andBuilder.and(orderInfo.orderNo.in(orderNos));
        /** 주문 번호 검색 처리 - orderNo, orderNos E */

        /** 주문 상태 검색 처리 - status, statuses S */

        /** 주문 상태 검색 처리 - status, statuses E */

        /** 키워드 검색 처리 S */
        sopt = Objects.requireNonNullElse(sopt, "all");
        if (skey != null && !skey.isBlank()) {
            skey = skey.trim();

            if (sopt.equals("all")) { // 통합 검색
                
            } else if (sopt.equals("name")) { // 주문자, 받는사람 이름
                BooleanBuilder orBuilder = new BooleanBuilder();
                orBuilder.or(orderInfo.orderName.contains(skey))
                        .or(orderInfo.receiverName.contains(skey));
                andBuilder.and(orBuilder);
            } else if (sopt.equals("mobile")) { // 주문자, 받는사람 휴대전화번호
                BooleanBuilder orBuilder = new BooleanBuilder();
                orBuilder.or(orderInfo.orderMobile.contains(skey))
                        .or(orderInfo.receiverMobile.contains(skey));
                andBuilder.and(orBuilder);
            } else if (sopt.equals("address")) { // 배송주소

            }

        }
        /** 키워드 검색 처리 E */

        /** 검색 처리 E */

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
