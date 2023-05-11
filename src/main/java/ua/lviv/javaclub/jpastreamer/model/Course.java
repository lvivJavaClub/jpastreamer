package ua.lviv.javaclub.jpastreamer.model;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString
@Entity
public class Course {
    @EqualsAndHashCode.Include
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Integer id;

    @Column(nullable = false, columnDefinition = "VARCHAR(160)")
    private String title;

    @Column(nullable = false)
    private Date dateBegin;

    @Column(nullable = false)
    private Date dateEnd;
}
