package com.festibook.festibook_backend.jwt.entity;

import com.festibook.festibook_backend.core.BaseEntity;
import com.festibook.festibook_backend.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.Date;

@Entity
@Getter
@SuperBuilder
@NoArgsConstructor
public class RefreshToken extends BaseEntity {

    @Column(nullable = false, unique = true)
    private String token;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @Column(nullable = false)
    private Date expiryDate;

    public RefreshToken(String token, User user, Date expiryDate) {
        this.token = token;
        this.user = user;
        this.expiryDate = expiryDate;
    }
}
