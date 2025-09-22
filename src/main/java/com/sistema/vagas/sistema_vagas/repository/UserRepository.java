package com.sistema.vagas.sistema_vagas.repository;

import com.sistema.vagas.sistema_vagas.model.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByUsername(String userName);
    boolean existsByUsername(String userName);

}
