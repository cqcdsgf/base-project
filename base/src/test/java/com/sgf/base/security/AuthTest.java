package com.sgf.base.security;

import com.google.common.collect.Collections2;
import com.sgf.base.BaseApplication;
import com.sgf.base.security.dao.AuthPermissionDao;
import com.sgf.base.security.domain.AuthPermission;
import com.sgf.base.security.domain.AuthRole;
import com.sgf.base.security.service.AuthPermissionService;
import com.sgf.base.security.service.AuthRoleService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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


    @Test
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

        authPermissionService.save(authPermissionList);

    }

    @Test
    public void updateAuthPermission(){
        AuthPermission authPermission = authPermissionService.findOne(1L);

        authPermission.setSummary("更新摘要3");

        authPermissionService.save(authPermission);
    }

    @Test
    public void insertAuthRole(){
        AuthRole authRole1 = new AuthRole();
        authRole1.setName("测试角色");
        authRole1.setSummary("测试摘要");

        authRoleService.save(authRole1);

        AuthRole authRole2 = new AuthRole();
        authRole2.setName("测试角色");
        authRole2.setSummary("测试摘要");

        authRoleService.save(authRole2);

    }

    @Test
    public void insertAuthRoleAndPermission(){
        AuthRole authRole1 = new AuthRole();
        authRole1.setName("测试角色and权限");
        authRole1.setSummary("测试摘要");

        AuthPermission authPermission1 = new AuthPermission();
        authPermission1.setName("测试权限and角色");
        authPermission1.setUrl("/login/login?");
        authPermission1.setSummary("test");
        authPermission1.setValue("login:test");

        AuthPermission authPermission2 = new AuthPermission();
        authPermission2.setName("测试权限and角色2");
        authPermission2.setUrl("/login/login?");
        authPermission2.setSummary("test");
        authPermission2.setValue("login:test");

        AuthPermission authPermission3 = new AuthPermission();
        authPermission3.setName("测试权限and角色3");
        authPermission3.setUrl("/login/login?");
        authPermission3.setSummary("test");
        authPermission3.setValue("login:test");

        authRole1.getPermissions().add(authPermission1);
        authRole1.getPermissions().add(authPermission2);
        authRole1.getPermissions().add(authPermission3);

        authRoleService.save(authRole1);
    }

    @Test
    public void updateRoleAndPermission(){

        authRoleService.updateAndRemove();

    }



}
