package com.festibook.festibook_backend.jwt.repository;

import com.festibook.festibook_backend.jwt.entity.RefreshToken;
import com.festibook.festibook_backend.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByToken(String token);
    void deleteByUser(User user);
}
