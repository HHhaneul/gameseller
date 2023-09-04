package org.shopping.models.games;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.shopping.commons.ListData;
import org.shopping.commons.Pagination;
import org.shopping.commons.configs.ConfigInfoService;
import org.shopping.commons.constants.GameStatus;
import org.shopping.controllers.admins.game.GameForm;
import org.shopping.entities.Category;
import org.shopping.entities.FileInfo;
import org.shopping.entities.Game;
import org.shopping.entities.QGame;
import org.shopping.models.files.FileInfoService;
import org.shopping.repositories.GameRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class GameInfoService{
    private final EntityManager em;
    private final GameRepository gameRepository;
    private final FileInfoService fileInfoService;
    private final HttpServletRequest request;

    private final ConfigInfoService configInfoService;

    /**
     * 게임 개별 조회
     *
     * @param gameNo
     * @return
     */
    public org.shopping.entities.Game get(Long gameNo) {
        Game game = gameRepository.findById(gameNo).orElseThrow(GameNotFoundException::new);
        addFileInfo(game);

        return game;
    }

    /**
     * 게임 엔티티를 게임 양식으로 반환
     *
     * @param gameNo
     * @return
     */
    public GameForm getGameForm(Long gameNo) {
        Game game = get(gameNo);
        Category category = game.getCategory();
        GameForm form = new ModelMapper().map(game, GameForm.class);
        form.setStatus(game.getStatus().name());
        form.setCateCd(category == null ? null : category.getCateCd());
        return form;
    }

    /**
     * 도서 목록 조회
     *
     * @param search
     * @return
     */
    public ListData<Game> getList(GameSearch search) {
        QGame game = QGame.game;
        int limit = search.getLimit();
        limit = limit < 1 ? 20 : limit;
        int page = search.getPage();
        page = page < 1 ? 1 : page;
        int offset = (page - 1) * limit;

        /** 검색 처리 S */
        BooleanBuilder andBuilder = new BooleanBuilder();

        String cateCd = search.getCateCd();
        List<String> cateCds = search.getCateCds();
        GameStatus status = search.getStatus();
        List<GameStatus> statuses = search.getStatuses();
        Long gameNo = search.getGameNo();
        String sopt = search.getSopt();
        String skey = search.getSkey();

        /** 도서 분류 검색 처리 S */
        if (cateCd != null && !cateCd.isBlank()) {
            andBuilder.and(game.category.cateCd.eq(cateCd));
        }

        if (cateCds != null && cateCds.size() > 0) {
            andBuilder.and(game.category.cateCd.in(cateCds));
        }
        /** 도서 분류 검색 처리 E */

        /** 판매 상태 검색 처리 S */
        if (status != null) {
            andBuilder.and(game.status.eq(status));
        }

        if (statuses != null && statuses.size() > 0) {
            andBuilder.and(game.status.in(statuses));
        }
        /** 판매 상태 검색 처리 E */

        /** 도서 번호 */
        if (gameNo != null) {
            andBuilder.and(game.gameNo.eq(gameNo));
        }

        /** 조건 및 키워드 검색 S */
        if (sopt != null && !sopt.isBlank() && skey != null && !skey.isBlank()) {
            sopt = sopt.trim();
            skey = skey.trim();

            if (sopt.equals("all")) { // 통합 검색
                BooleanBuilder orBuilder = new BooleanBuilder();
                orBuilder.or(game.gameNo.stringValue().contains(skey))
                        .or(game.gameNm.containsIgnoreCase(skey));
                andBuilder.and(orBuilder);

            } else if (sopt.equals("gameNm")) {
                andBuilder.and(game.gameNm.containsIgnoreCase(skey));
            } else if (sopt.equals("gameNo")) {
                andBuilder.and(game.gameNo.stringValue().contains(skey));
            }
        }
        /** 조건 및 키워드 검색 E */

        /** 검색 처리 E */

        /** 정렬 처리 S */
        // listOrder_DESC,createdAt_ASC
        List<OrderSpecifier> orderSpecifier = new ArrayList<>();
        String sort = search.getSort();
        if (sort != null && !sort.isBlank()) {
            List<String[]> sorts = Arrays.stream(sort.trim().split(","))
                    .map(s -> s.split("_")).toList();
            PathBuilder pathBuilder = new PathBuilder(Game.class, "game");

            for (String[] _sort : sorts) {
                Order direction = Order.valueOf(_sort[1].toUpperCase()); // 정렬 방향
                orderSpecifier.add(new OrderSpecifier(direction, pathBuilder.get(_sort[0])));
            }
        }
        /** 정렬 처리 E */


        JPAQueryFactory factory = new JPAQueryFactory(em);
        List<Game> items = factory.selectFrom(game)
                .leftJoin(game.category)
                .fetchJoin()
                .limit(limit)
                .offset(offset)
                .where(andBuilder)
                .orderBy(orderSpecifier.toArray(OrderSpecifier[]::new))
                .fetch();

        ListData<Game> data = new ListData<>();
        data.setContent(items);

        /* Todo : 페이징 처리 로직 추가 */
        int total = (int)gameRepository.count(andBuilder);
        Pagination pagination = new Pagination(page, total, 10, limit, request);
        data.setPagination(pagination);

        return data;
    }

    /**
     * 첨부된 이미지 추가 처리
     *
     * @param game
     */
    public void addFileInfo(Game game) {
        String gid = game.getGid();
        List<FileInfo> mainImages = fileInfoService.getListDone(gid, "main");
        List<FileInfo> listImages = fileInfoService.getListDone(gid, "list");
        List<FileInfo> editorImages = fileInfoService.getListDone(gid, "editor");
        game.setMainImages(mainImages);
        game.setListImages(listImages);
        game.setEditorImages(editorImages);
    }
}
