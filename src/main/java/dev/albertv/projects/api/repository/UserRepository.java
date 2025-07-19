package dev.albertv.projects.api.repository;

import dev.albertv.projects.api.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.springframework.transaction.annotation.Propagation.MANDATORY;

@Repository
@Transactional(propagation = MANDATORY)
public interface UserRepository extends JpaRepository<User, Long> {

    // Find

    Optional<User> findByUsername(String username);

}
