package org.shopping.controllers.buyer;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data @Builder
@NoArgsConstructor @AllArgsConstructor
public class BuyerForm {


    private String mode;
    //mode : all - 모두, done - 주문완료, undone : 장바구니 상태

    /* 주문번호 */
    private Long buyerNo;

    /* 주문자명 */
    private String buyerNm;

    /* 주문한 상품의 수량 */
    private Long buyerCnt;


    @NotBlank
    private String gid = UUID.randomUUID().toString();

    @NotBlank
    private String gameNm;
    private String category;

    private int price;
    private int stock;

    private String status;

    private long listOrder;

    /* 주문한 상품 개발자 */
    private String developer;

}
