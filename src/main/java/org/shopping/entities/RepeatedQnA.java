package org.shopping.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity @Data @Builder
@NoArgsConstructor @AllArgsConstructor
@Table(indexes=@Index(name="idx_repeatedQnA_order", columnList = "listQnA DESC, createdAt"))
public class RepeatedQnA extends BaseMemberEntity  {

    @Id @GeneratedValue
    private Long _id;

    private String question;

    private String answer;

    private Long listQnA;

    @Column(name="_use")
    private boolean use;
}
