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
public class Person {
    @EqualsAndHashCode.Include
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Integer id;

    @Column(nullable = false, columnDefinition = "VARCHAR(30)")
    private String firstName;

    @Column(nullable = false, columnDefinition = "VARCHAR(30)")
    private String lastName;

    @ManyToOne
    @JoinColumn
    private Department department;

    @Column
    private Boolean active;
}
