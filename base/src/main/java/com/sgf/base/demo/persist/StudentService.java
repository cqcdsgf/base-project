package com.sgf.base.demo.persist;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Service
public class StudentService {

    @Autowired
    StudentDao studentDao;

    @Autowired
    CourseDao courseDao;

    public Student save(Student student){

        return studentDao.save(student);


    }

    public Student findByName(String sName){

        return studentDao.findBySName(sName);

    }

    @Transactional
    public Student updateStudent(){
/*        Student s = new Student();
        s.setSName("二二二");*/


        Student s = studentDao.findBySName("二二二");

        Course c = courseDao.findByCName("dddd");
        s.getCourses().add(c);

        studentDao.save(s);

        return s;

    }


    public void delete(Student student){
        studentDao.delete(student);
    }

}
