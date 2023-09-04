package org.shopping.models.games;

import com.querydsl.core.BooleanBuilder;
import lombok.RequiredArgsConstructor;
import org.shopping.commons.Utils;
import org.shopping.entities.Game;
import org.shopping.entities.QGame;
import org.shopping.repositories.GameRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import static org.springframework.data.domain.Sort.Order.desc;

@Service
@RequiredArgsConstructor
public class GameListService {

    private final GameRepository gameRepository;
    public Page<Game> gets(GameSearch gameSearch) {
        QGame game = QGame.game;

        BooleanBuilder andBuilder = new BooleanBuilder();
        BooleanBuilder orBuilder = new BooleanBuilder();

        int page = gameSearch.getPage();
        int limit = gameSearch.getLimit();

        page = Utils.getNumber(page, 1);
        limit = Utils.getNumber(limit, 20);
        /** 검색 조건 처리 S */
        String sopt = gameSearch.getSopt();
        String skey = gameSearch.getSkey();
        if (sopt != null && !sopt.isBlank() && skey != null && !skey.isBlank()) {
            skey = skey.trim();
            sopt = sopt.trim();

            /* 통합 검색 - 제목, 카테고리 */
            if (sopt.equals("all")) {
                orBuilder.or(game.gameNm.contains(skey))
                        .or(game.category.cateNm.contains(skey));
                andBuilder.and(orBuilder);

                /* 제목 */
            } else if (sopt.equals("gameNm")) {
                andBuilder.and(game.gameNm.contains(skey));

                /* 카테고리 */
            } else if (sopt.equals("cateNm")) {
                andBuilder.and(game.category.cateNm.contains(skey));

            }
        }
        /** 검색 조건 처리 E */
        Pageable pageable = PageRequest.of(page - 1, limit, Sort.by(desc("createdAt")));
        Page<Game> data = gameRepository.findAll(andBuilder, pageable);

        return data;
    }
}
