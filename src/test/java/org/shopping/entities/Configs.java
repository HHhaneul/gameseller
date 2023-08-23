package org.shopping.entities;

import jakarta.persistence.*;
import lombok.Data;

@Entity @Data
public class Configs {

    @Id @Column(name="_code", length=45)
    private String code;

    @Lob
    @Column(name="_value")
    private String value;

}