package ua.lviv.javaclub.jpastreamer;

import com.speedment.jpastreamer.application.JPAStreamer;
import com.speedment.jpastreamer.projection.Projection;
import jakarta.persistence.EntityManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.support.TransactionTemplate;
import ua.lviv.javaclub.jpastreamer.model.Department;
import ua.lviv.javaclub.jpastreamer.model.Department$;
import ua.lviv.javaclub.jpastreamer.model.Person;
import ua.lviv.javaclub.jpastreamer.model.Person$;
import ua.lviv.javaclub.jpastreamer.repository.DepartmentRepository;
import ua.lviv.javaclub.jpastreamer.repository.PersonRepository;

import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.speedment.jpastreamer.field.predicate.Inclusion.START_INCLUSIVE_END_INCLUSIVE;
import static com.speedment.jpastreamer.streamconfiguration.StreamConfiguration.of;

@Slf4j
@SpringBootApplication
public class JpastreamerApplication implements CommandLineRunner {

    public static void main(final String[] args) {
        SpringApplication.run(JpastreamerApplication.class, args);
    }

    @Autowired
    private EntityManager entityManager;
    @Autowired
    private DepartmentRepository departmentRepository;
    @Autowired
    private PersonRepository personRepository;
    @Autowired
    private TransactionTemplate transactionTemplate;

    @Autowired
    private DataGenerator dataGenerator;

    private JPAStreamer jpaStreamer;

    @Override
    public void run(final String[] args) {
        dataGenerator.generateAndSaveData(false);

        jpaStreamer = JPAStreamer.of(entityManager.getEntityManagerFactory());

        exampleFindActiveEntities();
        filteringExample();
        comparingExample();
        paginationAndSortingExample();
        projectionExample();
        joinExample();
        transactionExample();

        jpaStreamer.close();
    }

    private static void printEach(final Iterable<?> iterable) {
        iterable.forEach(item -> System.out.println(item.toString()));
        System.out.println();
    }

    private void exampleFindActiveEntities() {
        System.out.println("Active departments:");
        printEach(departmentRepository.findByActiveIsTrue());
        System.out.println("Active persons:");
        printEach(personRepository.findByActiveIsTrue());
    }

    private void filteringExample() {
        // Filtering persons
        System.out.println("Active person which last name is not beginning with 'B' (case insensitive):");
        printEach(jpaStreamer.stream(Person.class)
                .filter(Person$.active.asBoolean().orElse(false)
                        .and(Person$.lastName.startsWithIgnoreCase("B").negate()))
                .toList());
    }

    private void comparingExample() {
        // Comparable
        System.out.println("Departments with IDs between 3 and 7, inclusive:");
        printEach(jpaStreamer.stream(Department.class)
                .filter(Department$.id.between(3, 7, START_INCLUSIVE_END_INCLUSIVE))
                .toList());
    }

    private void paginationAndSortingExample() {
        // Pagination and sorting
        System.out.println("Persons sorted by active state in descending order, then ID in ascending order, skip first 10, total of 10:");
        printEach(jpaStreamer.stream(Person.class)
                .sorted(Person$.active.reversed().thenComparing(Person$.id))
                .skip(10)
                .limit(10)
                .toList());
    }

    private void projectionExample() {
        // Projections
        System.out.println("Departments (only ID and title):");
        printEach(jpaStreamer.stream(Department.class)
                .map(Projection.select(Department$.id, Department$.title))
                .toList());
    }

    private void joinExample() {
        // Join
        // One-to-many
        System.out.println("Active departments and their member count:");
        final Map<Department, Long> departmentPersonsMap = jpaStreamer
                .stream(of(Department.class).joining(Department$.persons))
                .filter(Department$.active.asBoolean())
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
        printEach(departmentPersonsMap.entrySet().stream()
                .map(entry -> String.format("%s - %d member(s)", entry.getKey().toString(), entry.getValue()))
                .toList());

        // Many-to-one
        final Map<Person, Department> personDepartmentMap = jpaStreamer
                .stream(of(Person.class).joining(Person$.department))
                .filter(Person$.active.asBoolean())
                .collect(Collectors.toMap(Function.identity(), Person::getDepartment));
        System.out.println("Active persons and departments:");
        printEach(personDepartmentMap.entrySet().stream()
                .map(entry -> String.format("%s / %s", entry.getKey().toString(), entry.getValue().toString()))
                .toList());
    }

    private void transactionExample() {
        System.out.println("Surround all person names with '*' in transaction:");
        transactionTemplate.executeWithoutResult(t -> jpaStreamer.stream(Person.class)
                .forEach(p -> {
                    p.setFirstName("*" + p.getFirstName() + "*");
                    p.setLastName("*" + p.getLastName() + "*");
                }));
        printEach(jpaStreamer.stream(Person.class).toList());
    }

}
