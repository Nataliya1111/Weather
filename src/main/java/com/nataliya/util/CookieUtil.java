package com.nataliya.util;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Arrays;
import java.util.Optional;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CookieUtil {

    private static final String SESSION_ID_COOKIE = "sessionId";


    public static Cookie createSessionIdCookie(String cookieValue, int sessionTimeout) {
        var cookie = new Cookie(SESSION_ID_COOKIE, cookieValue);
        cookie.setPath("/");
        cookie.setMaxAge(sessionTimeout);
        return cookie;
    }

    public static boolean isSessionIdCookiePresent(Cookie[] cookies) {
        return findSessionIdCookie(cookies).isPresent();
    }

    public static Optional<Cookie> findSessionIdCookie(Cookie[] cookies) {
        return cookies == null
                ? Optional.empty()
                : Arrays.stream(cookies)
                .filter(cookie -> (SESSION_ID_COOKIE.equals(cookie.getName())))
                .findFirst();
    }

    public static void deleteSessionIdCookie(HttpServletResponse response) {
        Cookie newSessionIdCookie = createSessionIdCookie("", 0);
        response.addCookie(newSessionIdCookie);
    }

}
