package com.sgf.base.security.service;

import com.sgf.base.security.dao.SysUserRepository;
import com.sgf.base.security.domain.SysUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by sgf on 2017\12\26 0026.
 */
public class CustomUserDetailsService implements UserDetailsService{

    @Autowired
    SysUserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username)  {
        SysUser user = userRepository.findByUsername(username);
        if(user == null){
            throw new UsernameNotFoundException(username + " 用户名不存在");
        }
        return user;
    }

/*    @Resource(name="testDao")
    private TestDao testDao;*/

/*    @Override
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException, DataAccessException {
        System.out.println("username" + username);
        List<GrantedAuthority> auths = new ArrayList<GrantedAuthority>();
        List<String> authorityName = this.testDao.getAuthorityName(username);
        for(String roleName : authorityName) {
            System.out.println(roleName);
            SimpleGrantedAuthority authority = new SimpleGrantedAuthority(roleName);
            auths.add(authority);
        }
        String pwd = this.testDao.getPWD(username);
        return new User(username,pwd,true,true,true,true,auths);

    }*/
}
