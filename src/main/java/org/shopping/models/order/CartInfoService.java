package org.shopping.models.order;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.shopping.commons.MemberUtil;
import org.shopping.commons.Utils;
import org.shopping.entities.CartInfo;
import org.shopping.entities.Game;
import org.shopping.entities.QCartInfo;
import org.shopping.models.games.GameInfoService;
import org.shopping.repositories.order.CartInfoRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service("cartInfoService2")
@RequiredArgsConstructor
public class CartInfoService {
    private final GameInfoService gameInfoService;
    private final CartInfoRepository repository;
    private final MemberUtil memberUtil;
    private final Utils utils;
    private final JPAQueryFactory factory;

    public CartInfo getByGameNo(Long gameNo) {
        int uid = utils.guestUid();
        QCartInfo cartInfo = QCartInfo.cartInfo;
        BooleanBuilder builder = new BooleanBuilder();
        builder.and(cartInfo.game.gameNo.eq(gameNo));
        builder.and(cartInfo.mode.eq("cart"));

        if (memberUtil.isLogin()) {
            Long userNo = memberUtil.getMember().getUserNo();
            builder.and(cartInfo.member.userNo.eq(userNo));
        } else {
            builder.and(cartInfo.uid.eq(uid))
                    .and(cartInfo.member.userNo.isNull());
        }

        return repository.findOne(builder).orElse(null);
    }

    public List<CartInfo> getList(String mode) {
        mode = Objects.requireNonNullElse(mode, "cart");
        QCartInfo cartInfo = QCartInfo.cartInfo;
        BooleanBuilder builder = new BooleanBuilder();
        builder.and(cartInfo.mode.eq(mode));
        if (memberUtil.isLogin()) { // 회원
            builder.and(cartInfo.member.userNo.eq(memberUtil.getMember().getUserNo()));
        } else { // 비회원
            builder.and(cartInfo.uid.eq(utils.guestUid()))
                    .and(cartInfo.member.userNo.isNull());
        }

        PathBuilder pathBuilder = new PathBuilder(CartInfo.class, "cartInfo");

        List<CartInfo> items =  factory.selectFrom(cartInfo)
                        .leftJoin(cartInfo.member)
                        .leftJoin(cartInfo.game)
                        .fetchJoin()
                        .where(builder)
                        .orderBy(new OrderSpecifier(Order.ASC, pathBuilder.get("createdAt")))
                        .fetch();

        items.stream().forEach(this::addCartInfo);

        return items;

    }

    private void addCartInfo(CartInfo cartInfo) {
        Game game = cartInfo.getGame();
        if (game != null) gameInfoService.addFileInfo(game);
    }
}
