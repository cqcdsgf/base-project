package com.sgf.app.home.web;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.List;

/**
 * Created by sgf on 2018\3\1 0001.
 */
@Service
public class TokenService {
    private static final Logger logger = LoggerFactory.getLogger(TokenService.class);


     long EXPIRATIONTIME = 432_000_000;     // 5天
     String SECRET = "P@ssw02d";            // JWT密码
     String TOKEN_PREFIX = "Bearer";        // Token前缀
     String HEADER_STRING = "Authorization";// 存放Token的Header Key
    String cookieName = "Authorization";
    private Method setHttpOnlyMethod;

    // JWT生成方法
    public void addAuthentication(HttpServletResponse response, String username) {

        // 生成JWT
        String JWT = Jwts.builder()
                // 保存权限（角色）
                .claim("authorities", "ROLE_ADMIN,AUTH_WRITE")
                // 用户名写入标题
                .setSubject(username)
                // 有效期设置
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATIONTIME))
                // 签名设置
                .signWith(SignatureAlgorithm.HS512, SECRET)
                .compact();

        // 将 JWT 写入 body
        Cookie cookie = new Cookie(cookieName, JWT);
        cookie.setMaxAge(432_000_000);
        cookie.setPath("/");
            cookie.setHttpOnly(true);

            response.addCookie(cookie);
    }

    // JWT验证方法
    public Authentication getAuthentication(HttpServletRequest request) {
        // 从Header中拿到token
        String token = request.getHeader(HEADER_STRING);


        Cookie[] cookies = request.getCookies();
                 //如果用户是第一次访问，那么得到的cookies将是null
                 if (cookies!=null) {
                     for (int i = 0; i < cookies.length; i++) {
                         Cookie cookie = cookies[i];
                         if (cookie.getName().equals(cookieName)) {
                             token = cookie.getValue();
                         }
                     }
                 }

        if (token != null) {
            // 解析 Token
            Claims claims = Jwts.parser()
                    // 验签
                    .setSigningKey(SECRET)
                    // 去掉 Bearer
                    .parseClaimsJws(token.replace(TOKEN_PREFIX, ""))
                    .getBody();

            // 拿用户名
            String user = claims.getSubject();

            // 得到 权限（角色）
            List<GrantedAuthority> authorities =  AuthorityUtils.commaSeparatedStringToAuthorityList((String) claims.get("authorities"));

            // 返回验证令牌
            return user != null ?
                    new UsernamePasswordAuthenticationToken(user, null, authorities) :
                    null;
        }
        return null;
    }



}
