package com.festibook.festibook_backend.user.repository;

import com.festibook.festibook_backend.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByNickname(String nickname);
    boolean existsByNicknameAndIdNot(String nickname, Long id);  // 중복 확인 메서드

}
