package org.shopping.controllers.orders;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.shopping.entities.OrderItem;

import java.util.List;

@Data
public class OrderForm {

    private Long orderNo;

    private List<Long> cartNo;

    @NotBlank
    private String orderName;

    @NotBlank
    private String orderEmail;
    @NotBlank
    private String orderMobile;

    @NotBlank
    private String receiverName;
    @NotBlank
    private String receiverMobile;

    @NotBlank
    private String zonecode;
    @NotBlank
    private String address;
    @NotBlank
    private String addressSub;

    private String paymentType = "LBT";

    private int totalPrice;

    private int payPrice;

    private String deliveryCompany;
    private String invoice;

    private List<OrderItem> orderItems;

    private List<Long> itemId;
}
