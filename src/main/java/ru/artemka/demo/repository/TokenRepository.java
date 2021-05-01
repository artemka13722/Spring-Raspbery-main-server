package ru.artemka.demo.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.NoRepositoryBean;
import ru.artemka.demo.model.token.Token;

@NoRepositoryBean
public interface TokenRepository<T extends Token> extends CrudRepository<T, Integer> {
    T findByStringToken(String token);

    void deleteByStringToken(String token);

    void deleteByUserId(int userId);
}
