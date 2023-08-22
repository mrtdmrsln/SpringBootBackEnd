package com.example.CS308BackEnd2.repository;

import com.example.CS308BackEnd2.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    User findByName(String name);


    User deleteByEmail(String email);

    User deleteById(long id);

    Optional<User> findByEmail(String email);

    Boolean existsByEmail(String email);
}
