package com.sgf.base.demo.persist;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CourseService {

    @Autowired
    CourseDao courseDao;

    public Course save(Course course) {

        return courseDao.saveAndFlush(course);
    }

    public Course findByName(String cName) {

        return courseDao.findByCName(cName);

    }


    public void delete(Course course){
        courseDao.delete(course);
    }
}
