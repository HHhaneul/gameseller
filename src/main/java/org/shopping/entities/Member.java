package org.shopping.entities;

import jakarta.persistence.*;
import lombok.*;
import org.shopping.commons.constants.Role;

import java.util.List;
import java.util.UUID;

@Entity @Data @Builder
@NoArgsConstructor @AllArgsConstructor
@Table(indexes={
        @Index(name="idx_member_userNm", columnList = "userNm"),
        @Index(name="idx_member_email", columnList = "email")
})
public class Member extends BaseEntity {
    @Id @GeneratedValue
    private Long userNo;

    @Column(length=40, nullable = false, unique = true)
    private String userId;

    @Column(length=65, nullable = false)
    private String userPw;

    @Column(length=40, nullable = false)
    private String userNm;

    @Column(length=100, nullable = false)
    private String email;

    @Column(length=11)
    private String mobile;

    @Lob
    private String termsAgree;

    @Enumerated(EnumType.STRING)
    @Column(length=10, nullable = false)
    private Role roles = Role.USER;

    private List<Integer> chkNo;

}