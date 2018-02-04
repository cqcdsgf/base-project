package com.sgf.base.demo.persist;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CourseDao extends JpaRepository<Course,String>{


    public Course findByCName(String cName);

}
