package com.nataliya.util;

import jakarta.servlet.http.Cookie;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Arrays;
import java.util.UUID;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CookieUtil {

    private static final String SESSION_ID_COOKIE = "sessionId";


    public static boolean isSessionIdCookiePresent(Cookie[] cookies) {
        return cookies != null & Arrays.stream(cookies)
                .filter((cookie) -> (cookie.getName().equals(SESSION_ID_COOKIE))).findFirst().isPresent();
    }

    public static Cookie createSessionIdCookie(UUID sessionId, int sessionTimeout) {
        var cookie = new Cookie(SESSION_ID_COOKIE, sessionId.toString());
        cookie.setPath("/");
        cookie.setMaxAge(sessionTimeout);
        return cookie;
    }

}
