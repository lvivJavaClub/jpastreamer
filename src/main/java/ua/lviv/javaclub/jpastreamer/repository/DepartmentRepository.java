package ua.lviv.javaclub.jpastreamer.repository;

import org.springframework.stereotype.Repository;
import ua.lviv.javaclub.jpastreamer.model.Department;

import java.util.List;

@Repository
public interface DepartmentRepository extends CustomJpaRepository<Department>, PersistAndMergeRepository<Department> {
    List<Department> findByActiveIsTrue();
}
