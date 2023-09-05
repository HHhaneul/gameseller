package org.shopping.models.order;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.shopping.commons.MemberUtil;
import org.shopping.commons.Utils;
import org.shopping.controllers.orders.CartForm;
import org.shopping.entities.CartInfo;
import org.shopping.entities.Game;
import org.shopping.models.games.GameInfoService;
import org.shopping.repositories.order.CartInfoRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CartInfoSaveService {
    private final GameInfoService gameInfoService;
    private final CartInfoService cartInfoService;
    private final CartInfoDeleteService cartInfoDeleteService;
    private final MemberUtil memberUtil;
    private final CartInfoRepository repository;
    private final Utils utils;
    public void save(CartForm form) {
        Long gameNo = form.getGameNo();
        String mode = form.getMode();

        // 바로 구매 -> 기존 바로구매 정보 CartInfo에서 삭제
        if (mode.equals("direct")) {
            cartInfoDeleteService.deleteAll("direct");
        }

        Game game = gameInfoService.get(gameNo);
        CartInfo cart = mode.equals("direct")?null:cartInfoService.getByGameNo(gameNo);
        System.out.println("cart : " + cart);
        if (cart == null) { // 신규 추가  - 장바구니에 없는 경우 + 바로 구매(direct)
            cart = new ModelMapper().map(form, CartInfo.class);
            cart.setGame(game);
            cart.setUid(utils.guestUid());

            if (memberUtil.isLogin()) { // 로그인 상태인 경우 회원 엔티티 추가
              cart.setMember(memberUtil.getEntity());
            }
        } else { // 수량 변경 - 장바구니
            cart.setEa(cart.getEa() + form.getEa());
        }

        repository.saveAndFlush(cart);
    }

}
