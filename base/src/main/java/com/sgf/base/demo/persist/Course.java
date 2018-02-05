package com.sgf.base.demo.persist;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Entity
@NoArgsConstructor
@Getter
@Setter
public class Course implements Serializable{

    private static final long serialVersionUID = -3375086392013504614L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String cName;
    @ManyToMany(fetch = FetchType.LAZY,mappedBy = "courses")
    private Set<Student> students = new HashSet<>();













}
