package org.shopping.controllers.orders;

import lombok.Data;

import java.util.List;

@Data
public class CartForm {
    private String mode = "direct"; // direct - 바로구매, cart - 장바구니
    private Long gameNo; // 게임번호
    private int ea; // 구매 수량

    private List<Integer> chkNo;
}
