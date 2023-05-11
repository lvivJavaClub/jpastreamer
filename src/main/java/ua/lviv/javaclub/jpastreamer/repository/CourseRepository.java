package ua.lviv.javaclub.jpastreamer.repository;

import org.springframework.stereotype.Repository;
import ua.lviv.javaclub.jpastreamer.model.Course;

import java.util.Date;
import java.util.List;

@Repository
public interface CourseRepository extends CustomJpaRepository<Course>, PersistAndMergeRepository<Course> {
    List<Course> findAllByDateBeginBefore(Date begin);
    List<Course> findAllByDateEndAfter(Date end);
}
