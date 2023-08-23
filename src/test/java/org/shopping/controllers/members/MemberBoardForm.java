package org.shopping.controllers.members;

import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.modelmapper.ModelMapper;
import org.shopping.entities.MemberBoardData;

@Data @Builder
@NoArgsConstructor @AllArgsConstructor
public class MemberBoardForm {

    private Long id;

    private String mode;  // update이면 수정 처리

    @NotBlank
    private String subject;

    @NotBlank
    private String content;

    public static MemberBoardData of(MemberBoardForm memberBoardForm) {
        return new ModelMapper().map(memberBoardForm, MemberBoardData.class);
    }
}
