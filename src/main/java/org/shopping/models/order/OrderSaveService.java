package org.shopping.models.order;

import lombok.RequiredArgsConstructor;
import org.shopping.commons.MemberUtil;
import org.shopping.commons.Utils;
import org.shopping.commons.constants.OrderStatus;
import org.shopping.commons.constants.PaymentType;
import org.shopping.controllers.orders.OrderForm;
import org.shopping.entities.*;
import org.shopping.repositories.order.OrderInfoRepository;
import org.shopping.repositories.order.OrderItemRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderSaveService {
    private final CartInfoService cartInfoService;
    private final OrderInfoRepository infoRepository;
    private final OrderItemRepository itemRepository;
    private final MemberUtil memberUtil;
    private final Utils utils;

    public void save(OrderForm form) {
        List<Long> cartNos = form.getCartNo();
        List<CartInfo> cartItems = cartInfoService.getList(cartNos);
        if (cartItems == null || cartItems.isEmpty()) {
            throw new CartItemNotFoundException();
        }

        int payPrice = cartInfoService.getTotalPrice(cartItems);

        /** 주문서 정보 저장 S */
        OrderInfo orderInfo = OrderInfo.builder()
                .orderName(form.getOrderName())
                .orderEmail(form.getOrderEmail())
                .orderMobile(form.getOrderMobile())
                .receiverName(form.getReceiverName())
                .receiverMobile(form.getReceiverMobile())
                .address(form.getAddress())
                .zonecode(form.getZonecode())
                .addressSub(form.getAddressSub())
                .paymentType(PaymentType.valueOf(form.getPaymentType()))
                .payPrice(payPrice)
                .member(memberUtil.getEntity())
                .build();

        infoRepository.saveAndFlush(orderInfo);
        /** 주문서 정보 저장 E */

        /** 주문 상품 정보 저장 S */
        List<OrderItem> items = new ArrayList<>();
        for (CartInfo cartItem : cartItems) {
            Game game = cartItem.getGame();
            Category category = game.getCategory();
            OrderItem item = OrderItem.builder()
                        .status(OrderStatus.READY)
                        .ea(cartItem.getEa())
                        .game(game)
                        .price(game.getPrice())
                        .cateNm(category == null ? null : category.getCateNm())
                        .gameNm(game.getGameNm())
                        .totalPrice(cartItem.getTotalPrice())
                        .cartNo(cartItem.getCartNo())
                        .orderInfo(orderInfo)
                        .build();
            items.add(item);
        }

        itemRepository.saveAllAndFlush(items);
        /** 주문 상품 정보 저장 E */
        form.setOrderNo(orderInfo.getOrderNo());
    }

    public void update(OrderForm form) {
        QOrderInfo orderInfo = QOrderInfo.orderInfo;
        QOrderItem orderItem = QOrderItem.orderItem;
        Long orderNo = form.getOrderNo();

        /** 주문상품 수정 S */
        List<Long> itemIds = form.getItemId();
        if (itemIds != null && !itemIds.isEmpty()) {
            List<OrderItem> items = (List<OrderItem>)itemRepository.findAll(orderItem.id.in(itemIds));
            for (OrderItem item : items) {
                Long id = item.getId();
                item.setDeliveryCompany(utils.getParam("deliveryCompany_" + id));
                item.setInvoice(utils.getParam("invoice_" + id));
                String status = utils.getParam("status_" + id);
                if (status != null && !status.isBlank()) {
                    item.setStatus(OrderStatus.valueOf(status));
                }
            }
            itemRepository.flush();
        }
        /** 주문상품 수정 E */
        /** 주문정보 수정 S */
        OrderInfo data = infoRepository.findById(orderNo).orElseThrow(OrderNotFoundException::new);
        data.setOrderName(form.getOrderName());
        data.setOrderEmail(form.getOrderEmail());
        data.setOrderMobile(form.getOrderMobile());
        data.setReceiverName(form.getReceiverName());
        data.setReceiverMobile(form.getReceiverMobile());
        data.setAddress(form.getAddress());
        data.setZonecode(form.getZonecode());
        data.setAddressSub(form.getAddressSub());
        data.setDeliveryCompany(form.getDeliveryCompany());
        data.setInvoice(form.getInvoice());
        infoRepository.flush();
        /** 주문정보 수정 E */
    }
}
