package org.shopping.controllers.orders;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.List;

@Data
public class OrderForm {

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

    @NotBlank
    private String paymentType = "LBT";
}
