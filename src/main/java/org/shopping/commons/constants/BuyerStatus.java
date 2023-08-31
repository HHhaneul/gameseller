package org.shopping.commons.constants;


import org.shopping.commons.Utils;

import java.util.Arrays;
import java.util.List;

public enum BuyerStatus {

    ORDER,      // 주문
    PAY,        // 입금
    SHIP,      // 배송
    DONE,       // 배송 완료
    CANCEL;     // 주문취소



    public String getString() {
        return Utils.getMessage("BuyerStatus." + name(), "common");
    }

    public static List<String[]> getList() {
        return Arrays.asList(
                new String[]{ORDER.name(),ORDER.getString()},
                new String[]{PAY.name(),PAY.getString()},
                new String[]{SHIP.name(),SHIP.getString()},
                new String[]{DONE.name(),DONE.getString()},
                new String[]{CANCEL.name(),CANCEL.getString()}
        );
    }
}
