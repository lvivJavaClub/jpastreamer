package ua.lviv.javaclub.jpastreamer.model;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString
@Entity
public class PersonCourse {
    @EqualsAndHashCode.Include
    @EmbeddedId
    private PersonCourseKey id;

    @ManyToOne
    @MapsId("personId")
    @JoinColumn
    private Person person;

    @ManyToOne
    @MapsId("courseId")
    @JoinColumn
    private Course course;
}
