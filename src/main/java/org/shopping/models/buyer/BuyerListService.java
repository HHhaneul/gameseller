package org.shopping.models.buyer;

import jakarta.servlet.http.HttpServletRequest;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.shopping.controllers.buyer.BuyerForm;
import org.shopping.entities.Buyer;
import org.shopping.repositories.BuyerListRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BuyerListService {

    private final BuyerListRepository repository;


    /* buyerNo의 buyer entity를 get */
    public Buyer get(Long buyerNo) {
        Buyer buyer = repository.findById(buyerNo).orElseThrow(BuyerNotFoundException::new);

        return buyer;
    }

    /* buyer entity를 BuyerForm으로 반환 */
    public BuyerForm getBuyerForm(Long buyerNo) {
        Buyer buyer = get(buyerNo);
        BuyerForm form = new ModelMapper().map(buyer, BuyerForm.class);

        return form;
    }


    public List<Buyer> getList(Options opts) {

        List<Buyer> items = repository.getBuyers(opts.getGid(), opts.getMode().name());

        return items;

    }

    /* SerchMode=DONE 인 list만 반환 */
    public List<Buyer> getBuyerDone() {

        Options opts = Options.builder()
                .mode(SearchMode.DONE)
                .build();

        return getList(opts);
    }

    @Data
    @Builder
    static class Options {
        private String gid;
        private SearchMode mode;

    }

    enum SearchMode {
        ALL,
        DONE,
        UNDONE
    }
}