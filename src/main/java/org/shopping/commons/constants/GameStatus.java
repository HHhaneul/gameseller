package org.shopping.commons.constants;

import org.shopping.commons.Utils;

import java.util.Arrays;
import java.util.List;

public enum GameStatus {
    READY, // 상품준비중
    SALE, // 판매중
    OUT_OF_STOCK, // 품절
    STOP; // 판매중지

    public String getString() {
        return Utils.getMessage("BookStatus." + name(), "common");
    }

    public static List<String[]> getList() {
        return Arrays.asList(
                new String[]{READY.name(),READY.getString()},
                new String[]{SALE.name(),SALE.getString()},
                new String[]{OUT_OF_STOCK.name(),OUT_OF_STOCK.getString()},
                new String[]{STOP.name(),STOP.getString()}
        );
    }
}
