package org.shopping.entities;

import lombok.*;

import java.io.Serializable;

@EqualsAndHashCode
@NoArgsConstructor @AllArgsConstructor
public class BoardViewId implements Serializable {
    private Long id;
    private String uid;
}