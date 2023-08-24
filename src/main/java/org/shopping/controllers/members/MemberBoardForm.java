package org.shopping.controllers.members;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.modelmapper.ModelMapper;
import org.shopping.entities.*;

import java.util.UUID;

@Data @Builder
@NoArgsConstructor @AllArgsConstructor
public class MemberBoardForm {
    private Long id; // 게시글 번호

    @NotBlank
    private String bId;

    @NotBlank
    private String gid = UUID.randomUUID().toString();

    /* 작성자 */
    @NotBlank
    private String poster;

    /* 비회원 비밀번호 */
    private String guestPw;

    /* 게시판 분류 */
    private String category;

    /* 제목 */
    @NotBlank
    private String subject;

    /* 내용 */
    @NotBlank
    private String content;

    /* 회원번호 */
    private Long userNo;
}