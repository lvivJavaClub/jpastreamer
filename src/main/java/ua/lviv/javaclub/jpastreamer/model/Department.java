package ua.lviv.javaclub.jpastreamer.model;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Set;

@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(onlyExplicitlyIncluded = true)
@Entity
public class Department {
    @EqualsAndHashCode.Include
    @ToString.Include
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Integer id;

    @ToString.Include
    @Column(nullable = false, columnDefinition = "VARCHAR(80)")
    private String title;

    @ToString.Include
    @Column
    private Boolean active;

    @OneToMany(mappedBy = "department")
    private Set<Person> persons;
}
