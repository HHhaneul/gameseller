package org.shopping.controllers.members;

import jakarta.persistence.Column;
import jakarta.validation.constraints.*;
import lombok.*;
import org.shopping.repositories.member.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.UUID;

@Data @Builder
@NoArgsConstructor @AllArgsConstructor
public class JoinForm {
    @Autowired
    private MemberRepository memberRepository;

    @NotBlank
    @Size(min=6, max=20)
    private String userId;

    @Column(length = 40,unique = true)
    private String gId = UUID.randomUUID().toString();


    @NotBlank
    @Size(min=8)
    private String userPw;

    @NotBlank
    private String userPwRe;


    @NotBlank
    private String userNm;

    @NotBlank @Email
    private String email;
    private String mobile;

    private boolean[] agrees;


}