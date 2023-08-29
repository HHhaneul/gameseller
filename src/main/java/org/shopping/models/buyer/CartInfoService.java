package org.shopping.models.buyer;

import lombok.RequiredArgsConstructor;
import org.shopping.entities.Buyer;
import org.shopping.entities.Cart;
import org.shopping.entities.Game;
import org.shopping.repositories.CartRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CartInfoService {

    private final CartRepository cartRepository;

    public Cart get(Long buyerNo) {
        Cart cart = cartRepository.findById(buyerNo).orElseThrow(BuyerNotFoundException::new);

        return cart;
    }

    public List<Long> cartList(Long buyerNo){

        return cartRepository.findById(buyerNo).get().getGameNo();
    }

}
