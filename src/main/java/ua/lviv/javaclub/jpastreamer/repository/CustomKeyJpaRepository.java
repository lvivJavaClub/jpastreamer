package ua.lviv.javaclub.jpastreamer.repository;

import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.Repository;
import ua.lviv.javaclub.jpastreamer.model.PersonCourseKey;

@NoRepositoryBean
public interface CustomKeyJpaRepository<T> extends Repository<T, PersonCourseKey> {
}
