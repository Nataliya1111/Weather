package com.nataliya.util;

import jakarta.servlet.http.Cookie;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CookieUtil {

    private static final String SESSION_ID_COOKIE = "sessionId";


    public static Cookie createSessionIdCookie(UUID sessionId, int sessionTimeout) {
        var cookie = new Cookie(SESSION_ID_COOKIE, sessionId.toString());
        cookie.setPath("/");
        cookie.setMaxAge(sessionTimeout);
        return cookie;
    }

    public static boolean isSessionIdCookiePresent(Cookie[] cookies) {
        return getSessionIdCookie(cookies).isPresent();
    }

    public static Optional<Cookie> getSessionIdCookie(Cookie[] cookies) {
        return cookies == null
                ? Optional.empty()
                : Arrays.stream(cookies)
                .filter(cookie -> (SESSION_ID_COOKIE.equals(cookie.getName())))
                .findFirst();
    }

}
