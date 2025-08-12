package com.nataliya.interceptor;

import com.nataliya.dto.UserDto;
import com.nataliya.mapper.UserMapper;
import com.nataliya.model.Session;
import com.nataliya.service.SessionService;
import com.nataliya.util.CookieUtil;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class AuthenticationCheckInterceptor implements HandlerInterceptor {

    private static final String SIGN_IN_VIEW = "sign-in";

    private final SessionService sessionService;
    private final UserMapper userMapper;

    @Override
    public boolean preHandle(HttpServletRequest request,
                             @NonNull HttpServletResponse response,
                             @NonNull Object handler) throws Exception {

        Cookie[] cookies = request.getCookies();
        Optional<Cookie> cookieOptional = CookieUtil.findSessionIdCookie(cookies);
        if (cookieOptional.isEmpty()) {
            response.sendRedirect(SIGN_IN_VIEW);
            return false;
        }
        Cookie sessionIdCookie = cookieOptional.get();
        Session session = sessionService.getSession(UUID.fromString(sessionIdCookie.getValue()));
        UserDto userDto = userMapper.userToUserDto(session.getUser());

        request.setAttribute("authUserDto", userDto);

        return true;
    }

}
