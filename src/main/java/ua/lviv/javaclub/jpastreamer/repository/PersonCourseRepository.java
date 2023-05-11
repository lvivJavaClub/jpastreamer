package ua.lviv.javaclub.jpastreamer.repository;

import ua.lviv.javaclub.jpastreamer.model.PersonCourse;

@org.springframework.stereotype.Repository
public interface PersonCourseRepository extends CustomKeyJpaRepository<PersonCourse>,
        PersistAndMergeRepository<PersonCourse> {
}
