package com.sgf.app;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * Created by sgf on 2018\1\15 0015.
 */
public class BCryptPasswordEncoderTest {

    public static void main(String[] args) {
        String pass = "admin";
        BCryptPasswordEncoder bcryptPasswordEncoder = new BCryptPasswordEncoder();
        String hashPass = bcryptPasswordEncoder.encode(pass);
        System.out.println(hashPass);

        boolean f = bcryptPasswordEncoder.matches("admin",hashPass);
        System.out.println(f);

    }


}
