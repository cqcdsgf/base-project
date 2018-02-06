package com.sgf.base.security;

import com.sgf.base.BaseApplication;
import com.sgf.base.security.domain.AuthPermission;
import com.sgf.base.security.domain.AuthRole;
import com.sgf.base.security.domain.AuthUser;
import com.sgf.base.security.service.AuthPermissionService;
import com.sgf.base.security.service.AuthRoleService;
import com.sgf.base.security.service.AuthUserService;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Created by sgf on 2018\2\5 0005.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = BaseApplication.class)
public class AuthTest {
    private static final Logger logger = LoggerFactory.getLogger(AuthTest.class);

    @Autowired
    private AuthPermissionService authPermissionService;

    @Autowired
    private AuthRoleService authRoleService;

    @Autowired
    private AuthUserService authUserService;

    @Before
    public void clear(){
        authUserService.deleteAll();

        authRoleService.deleteAll();

        authPermissionService.deleteAll();


    }


    @Test
    //单独新增权限
    public void insertAuthPermission(){
        AuthPermission authPermission1 = new AuthPermission();

        authPermission1.setName("测试名称1");
        authPermission1.setSummary("测试摘要1");
        authPermission1.setUrl("/login/login1");
        authPermission1.setValue("loging:login1");


        AuthPermission authPermission2 = new AuthPermission();

        authPermission2.setName("测试名称2");
        authPermission2.setSummary("测试摘要2");
        authPermission2.setUrl("/login/login2");
        authPermission2.setValue("loging:login2");

        List<AuthPermission> authPermissionList = new ArrayList<AuthPermission>();
        authPermissionList.add(authPermission1);
        authPermissionList.add(authPermission2);

        List<AuthPermission> authPermissions = authPermissionService.save(authPermissionList);

        assertEquals(2,authPermissions.size());

    }

    @Test
    //单独更新权限
    public void updateAuthPermission(){
        AuthPermission authPermission1 = new AuthPermission();

        authPermission1.setName("测试名称1");
        authPermission1.setSummary("测试摘要1");
        authPermission1.setUrl("/login/login1");
        authPermission1.setValue("loging:login1");
        authPermissionService.save(authPermission1);


        ExampleMatcher matcher = ExampleMatcher.matching()
                .withMatcher("name", match -> match.endsWith())
                .withMatcher("summary", match -> match.startsWith());

        Example<AuthPermission> example = Example.of(authPermission1, matcher);

        AuthPermission authPermission = authPermissionService.findOne(example);

        authPermission.setSummary("更新摘要3");

        authPermissionService.save(authPermission);

        assertEquals("更新摘要3",authPermission.getSummary());
    }

    @Test
    public void opertAuthRole(){
        AuthRole authRole = new AuthRole();
        authRole.setSummary("dddd");
        authRole.setName("sss");

        authRole = authRoleService.save(authRole);

        assertEquals("dddd",authRole.getSummary());

    }


    @Test
    //操作角色权限关联
    public void opertAuthRoleAndPermission(){
        AuthRole authRole1 = new AuthRole();
        authRole1.setName("测试角色and权限222");
        authRole1.setSummary("测试摘要");

        AuthPermission authPermission1 = new AuthPermission();
        authPermission1.setName("测试权限and角色222");
        authPermission1.setUrl("/login/login?");
        authPermission1.setSummary("test");
        authPermission1.setValue("login:test");

        authRole1.getPermissions().add(authPermission1);
        //同时新角色，权限
        authRoleService.save(authRole1);

        AuthPermission authPermission2 = new AuthPermission();;
        authPermission2.setName("sssssss");
        authPermission2.setUrl("sssss");
        authPermission2.setSummary("ssssss");
        authPermission2.setValue("login:ssss");
        authPermissionService.save(authPermission2);

        //在原有基础上，添加角色对应的权限
        authRole1.getPermissions().add(authPermission2);
        authRole1 = authRoleService.save(authRole1);
        assertEquals(2,authRole1.getPermissions().size());

        //在原有基础上，删除角色对应的权限
        authRole1.getPermissions().remove(authPermission1);
        authRole1 = authRoleService.save(authRole1);
        assertEquals(1,authRole1.getPermissions().size());
    }

    @Test
    //单独操作用户表
    public void opertAuthUser(){
        AuthUser authUser = new AuthUser();
        authUser.setUsername("test");
        authUser.setPhone("18996110011");

        authUser = authUserService.save(authUser);

        assertEquals("18996110011",authUser.getPhone());

    }

    @Test
    //同时操作用户及角色
    public void opertUserAndRole(){
        AuthUser authUser = new AuthUser();
        authUser.setUsername("test");
        authUser.setPhone("18996110011");

        AuthRole authRole1 = new AuthRole();
        authRole1.setName("1111111");

        //同时新增用户及角色
        authUser.getRoles().add(authRole1);
        authUser = authUserService.save(authUser);
        assertEquals(1,authUser.getRoles().size());

        AuthRole authRole2 = new AuthRole();
        authRole2.setName("22222");
        authRoleService.save(authRole2);

        //在原有基础上，新增角色
        authUser.getRoles().add(authRole2);
        authUser = authUserService.save(authUser);
        assertEquals(2,authUser.getRoles().size());

        //在原有基础上，删除角色
        authUser.getRoles().remove(authRole1);
        authUser = authUserService.save(authUser);
        assertEquals(1,authUser.getRoles().size());

        authRoleService.delete(authRole1);

        //有外键约束，删除时会报错
        //authRoleService.delete(authRole2);
    }

}
