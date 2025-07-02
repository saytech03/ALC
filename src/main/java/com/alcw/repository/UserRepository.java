package com.alcw.repository;


import com.alcw.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.Optional;

public interface UserRepository extends MongoRepository<User, String> {
    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);
    Optional<User> findByResetToken(String resetToken);
    Optional<User> findByMembershipId(String membershipId);
}
