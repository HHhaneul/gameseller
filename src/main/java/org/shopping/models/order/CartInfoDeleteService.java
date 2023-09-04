package org.shopping.models.order;

import lombok.RequiredArgsConstructor;
import org.shopping.entities.CartInfo;
import org.shopping.repositories.order.CartInfoRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CartInfoDeleteService {
    private final CartInfoRepository repository;
    private final CartInfoService infoService;

    /**
     * 장바구니 비우기 
     * 
     * @param mode : direct - 바로구매 상품, cart - 장바구니 상품
     */
    public void deleteAll(String mode) {
        List<CartInfo> items = infoService.getList(mode);
        repository.deleteAll(items);
        repository.flush();
    }

    public void delete(List<Long> cartNos) {
        List<CartInfo> items = infoService.getList(cartNos);
        if (items == null || items.isEmpty()) return;

        repository.deleteAll(items);
        repository.flush();
    }
}
