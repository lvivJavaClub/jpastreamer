package ua.lviv.javaclub.jpastreamer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.datafaker.Faker;
import org.springframework.stereotype.Component;
import ua.lviv.javaclub.jpastreamer.model.*;
import ua.lviv.javaclub.jpastreamer.repository.CourseRepository;
import ua.lviv.javaclub.jpastreamer.repository.DepartmentRepository;
import ua.lviv.javaclub.jpastreamer.repository.PersonCourseRepository;
import ua.lviv.javaclub.jpastreamer.repository.PersonRepository;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;
import java.util.stream.IntStream;
import java.util.stream.Stream;

@Slf4j
@RequiredArgsConstructor
@Component
public class DataGenerator {

    private final DepartmentRepository departmentRepository;
    private final CourseRepository courseRepository;
    private final PersonRepository personRepository;
    private final PersonCourseRepository personCourseRepository;

    private static final int ORIGIN = 1, MAX_DEPARTMENT = 10, MAX_COURSE = 20, MAX_PERSON = 30, MAX_COURSES_PER_PERSON = 3;

    public void generateAndSaveData(final boolean printInitialData) {
        final Random random = new Random(0);
        final Faker faker = new Faker(random);

        final AtomicInteger id = new AtomicInteger(ORIGIN);
        final Supplier<Department> departmentSupplier = () -> {
            final Department department = new Department();
            department.setId(id.getAndIncrement());
            department.setTitle(faker.team().name());
            department.setActive(faker.bool().bool());
            return department;
        };
        final List<Department> departments = Stream.generate(departmentSupplier).limit(MAX_DEPARTMENT).toList();
        departmentRepository.mergeAll(departments);

        final Date start = Date.from(LocalDate.of(2022, 1, 1).atStartOfDay()
                .atZone(ZoneId.systemDefault()).toInstant()),
                middle = Date.from(LocalDate.of(2023, 1, 1).atStartOfDay()
                        .atZone(ZoneId.systemDefault()).toInstant()),
                end = Date.from(LocalDate.of(2024, 1, 1).atStartOfDay()
                        .atZone(ZoneId.systemDefault()).toInstant());
        id.set(ORIGIN);
        final Supplier<Course> courseSupplier = () -> {
            final Course course = new Course();
            course.setId(id.getAndIncrement());
            course.setTitle(faker.marketing().buzzwords());
            course.setDateBegin(faker.date().between(start, middle));
            course.setDateEnd(faker.date().between(middle, end));
            return course;
        };
        final List<Course> courses = Stream.generate(courseSupplier).limit(MAX_COURSE).toList();
        courseRepository.mergeAll(courses);

        id.set(ORIGIN);
        final Supplier<Person> personSupplier = () -> {
            final Person person = new Person();
            person.setId(id.getAndIncrement());
            person.setFirstName(faker.name().firstName());
            person.setLastName(faker.name().lastName());
            person.setDepartment(departments.get(random.nextInt(ORIGIN, MAX_DEPARTMENT)));
            person.setActive(faker.bool().bool());
            return person;
        };
        final List<Person> persons = Stream.generate(personSupplier).limit(MAX_PERSON).toList();
        personRepository.mergeAll(persons);

        final List<PersonCourse> personCourses = new ArrayList<>();
        persons.forEach(person -> {
            final List<Course> currentPersonCourses = IntStream.generate(() -> random.nextInt(ORIGIN, MAX_COURSE))
                    .limit(random.nextInt(0, MAX_COURSES_PER_PERSON))
                    .mapToObj(courses::get)
                    .toList();
            currentPersonCourses.forEach(currentPersonCourse -> {
                final PersonCourse personCourse = new PersonCourse();
                personCourse.setId(new PersonCourseKey(person.getId(), currentPersonCourse.getId()));
                personCourse.setPerson(person);
                personCourse.setCourse(currentPersonCourse);
                personCourses.add(personCourse);
            });
        });
        personCourseRepository.mergeAll(personCourses);

        if(printInitialData) {
            log.info("Initial data.");
            log.info("Departments:");
            departments.forEach(department -> log.info("{}", department));
            log.info("Courses:");
            courses.forEach(course -> log.info("{}", course));
            log.info("Persons:");
            persons.forEach(person -> log.info("{}", person));
            log.info("Person courses:");
            personCourses.forEach(personCourse -> log.info("{}", personCourse));
        }
    }
}
