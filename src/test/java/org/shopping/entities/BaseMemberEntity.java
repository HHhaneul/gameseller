package org.shopping.entities;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Getter @Setter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseMemberEntity extends BaseEntity {

    @CreatedBy
    @Column(length=40, updatable = false)
    private String createdBy;

    @LastModifiedBy
    @Column(length=40, insertable = false)
    private String modifiedBy;
}