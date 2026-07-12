package com.disha.taskmanager.util;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;

public class CookieUtil {

    private CookieUtil() {
    }

    public static void addRefreshTokenCookie(
            HttpServletResponse response,
            String refreshToken
    ) {

        Cookie cookie = new Cookie(
                "refreshToken",
                refreshToken
        );

        cookie.setHttpOnly(true);
        cookie.setSecure(false);
        cookie.setPath("/");
        cookie.setMaxAge(7 * 24 * 60 * 60);

        response.addCookie(cookie);
    }

    public static void clearRefreshTokenCookie(
            HttpServletResponse response
    ) {

        Cookie cookie = new Cookie(
                "refreshToken",
                null
        );

        cookie.setHttpOnly(true);
        cookie.setSecure(false);
        cookie.setPath("/");
        cookie.setMaxAge(0);

        response.addCookie(cookie);
    }

}