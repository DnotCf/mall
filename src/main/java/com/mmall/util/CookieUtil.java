package com.mmall.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
public class CookieUtil {

    private final static String COOKIE_DOMAIN = ".tangshun.xin";
    private final static String COOKIE_NAME = "user_token";

    public static void addCookie(String token, HttpServletResponse response) {

        Cookie cookie = new Cookie(COOKIE_NAME, token);
      //  cookie.setDomain(COOKIE_DOMAIN);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        cookie.setMaxAge(60 * 60 * 24 * 30);
        response.addCookie(cookie);
        log.info("add Cookie  success");

    }

    public static String getCookie(HttpServletRequest request) {

        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie c : cookies) {
                if (StringUtils.equals(c.getName(), COOKIE_NAME)) {
                    log.info("get Cookie success name:{},value:{}", c.getName(), c.getValue());
                    return c.getValue();
                }
            }
        }

        return null;
    }

    public static void deleteCookie(HttpServletRequest request,HttpServletResponse response) {

        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie c : cookies) {
                if (StringUtils.equals(c.getName(), COOKIE_NAME)) {
                    c.setMaxAge(0);
                    response.addCookie(c);
                    log.info("delete Cookie success name:{},value:{}", c.getName(), c.getValue());
                    return;
                }
            }
        }
        return;
    }


}
