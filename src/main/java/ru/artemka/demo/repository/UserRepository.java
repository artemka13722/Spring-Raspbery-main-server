package ru.artemka.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.artemka.demo.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    User findUserByNameIgnoreCase(String name);

    User findUserByEmailIgnoreCase(String email);
}
