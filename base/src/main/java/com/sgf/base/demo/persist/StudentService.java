package com.sgf.base.demo.persist;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class StudentService {

    @Autowired
    StudentDao studentDao;

    public Student save(Student student){

        return studentDao.saveAndFlush(student);


    }

    public Student findByName(String sName){

        return studentDao.findBySName(sName);

    }


    public void delete(Student student){
        studentDao.delete(student);
    }

}
