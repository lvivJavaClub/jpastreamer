package ua.lviv.javaclub.jpastreamer.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Embeddable
public class PersonCourseKey implements Serializable {
    @Column
    private Integer personId;

    @Column
    private Integer courseId;
}
