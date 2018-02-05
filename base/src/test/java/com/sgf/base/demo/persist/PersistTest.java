package com.sgf.base.demo.persist;

import com.sgf.base.BaseApplication;
import javafx.application.Application;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = BaseApplication.class)
//@EnableAutoConfiguration
public class PersistTest {

    @Autowired
    StudentService studentService;

    @Autowired
    CourseService courseService;

    /**
     * 仅将被维护方对象添加进维护方对象Set中
     * 保存维护方对象
     */
    //@Test
    public void 多对多插入1(){
        Student s = new Student();
        s.setSName("一一一");

        Course c = new Course();
        c.setCName("语文");
        Course c1 = new Course();
        c1.setCName("dddd");

        s.getCourses().add(c);
        s.getCourses().add(c1);
        studentService.save(s);

    }

    /**
     * 仅将维护方对象添加进被维护方对象Set中
     * 保存被维护方对象
     */
    //@Test
   //@Transactional
    public void 多对多插入2(){
        Student s1 = new Student();
        s1.setSName("四四四");

        Student s2 = new Student();
        s2.setSName("五五五");

        Course c1 =  courseService.findByName("dddd");
        Course c2 =  courseService.findByName("语文");

        s1.getCourses().add(c1);
        s1.getCourses().add(c2);

        s2.getCourses().add(c1);
        s2.getCourses().add(c2);

       studentService.save(s1);
        studentService.save(s2);
    }

    /**
     * 将双方对象均添加进双方Set中
     * 保存被维护方对象
     */
   // @Test
    public void 多对多插入3(){
        Student s = new Student();
        s.setSName("三三三");
        Student s1 = new Student();
        s1.setSName("四四四");
        Course c = new Course();
        c.setCName("英语");

        c.getStudents().add(s);
        c.getStudents().add(s1);
        s.getCourses().add(c);
        s1.getCourses().add(c);
        courseService.save(c);

    }

    /**
     * 删除维护方对象
     */
    //@Test
    public void 删除维护方对象(){
        Student s = studentService.findByName("三三三");
        Student s1 = studentService.findByName("五五五");

        studentService.delete(s);
        studentService.delete(s1);
    }


    /**
     * 删除被维护方对象
     */
   // @Test
    public void 多对多删除2(){
        //Course c = courseService.findByName("英语");
        Course c = courseService.findByName("英语");
        courseService.delete(c);
    }




}
