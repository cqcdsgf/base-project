package com.sgf.base.security;

import com.sgf.base.BaseApplication;
import com.sgf.base.security.dao.AuthPermissionDao;
import com.sgf.base.security.domain.AuthPermission;
import com.sgf.base.security.service.AuthPermissionService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Created by sgf on 2018\2\5 0005.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = BaseApplication.class)
public class AuthTest {
    private static final Logger logger = LoggerFactory.getLogger(AuthTest.class);

    @Autowired
    private AuthPermissionService authPermissionService;


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

        authPermissionService.create(authPermission1);
        authPermissionService.create(authPermission2);

    }

    @Test
    public void updateAuthPermission(){
        AuthPermission authPermission = authPermissionService.find(1L);

        authPermission.setSummary("更新摘要2");

        authPermissionService.update(authPermission);

    }

}
