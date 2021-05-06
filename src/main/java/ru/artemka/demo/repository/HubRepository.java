package ru.artemka.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.artemka.demo.model.Hub;

import java.util.Optional;

@Repository
public interface HubRepository extends JpaRepository<Hub, Integer> {
    Optional<Hub> findHubByAddress(String address);
}
