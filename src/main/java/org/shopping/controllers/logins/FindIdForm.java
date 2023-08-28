package org.shopping.controllers.logins;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FindIdForm {

    @NotBlank
    private String userNm;

    @NotBlank
    private String mobile;
    private String foundUserId;
}
