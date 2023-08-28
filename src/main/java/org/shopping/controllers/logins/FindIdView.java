package org.shopping.controllers.logins;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FindIdView {

    private String userId;

    /*@Override
    public String Loginid() {
        return userId;
    }*/
}
