package com.security.demoJWT.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.security.demoJWT.enums.UserType.EnumCourse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;


@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Course implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long courseId;
    @Enumerated(EnumType.STRING)
    private EnumCourse courseName;
    @ManyToOne
    private CourseCategory category;
    @ManyToMany(fetch = FetchType.EAGER,mappedBy = "courses")
    @JsonBackReference
    private List<User> users;

}
