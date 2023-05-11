package ua.lviv.javaclub.jpastreamer.repository;

import org.springframework.stereotype.Repository;
import ua.lviv.javaclub.jpastreamer.model.Person;

import java.util.List;

@Repository
public interface PersonRepository extends CustomJpaRepository<Person>, PersistAndMergeRepository<Person> {
    List<Person> findByActiveIsTrue();
}
