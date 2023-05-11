package ua.lviv.javaclub.jpastreamer.repository;

import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.Repository;

import java.util.Optional;

@NoRepositoryBean
public interface CustomJpaRepository<T> extends Repository<T, Integer> {
    Optional<T> findById(Integer id);
    Iterable<T> findAllById(Iterable<Integer> ids);
}
