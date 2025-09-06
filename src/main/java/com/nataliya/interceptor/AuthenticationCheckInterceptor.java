package com.nataliya.interceptor;

import com.nataliya.controller.SearchLocationsController;
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
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class AuthenticationCheckInterceptor implements HandlerInterceptor {

    private static final String HOME_URL = "/";

    private final SessionService sessionService;
    private final UserMapper userMapper;

    @Override
    public boolean preHandle(HttpServletRequest request,
                             @NonNull HttpServletResponse response,
                             @NonNull Object handler) throws IOException {

        Cookie[] cookies = request.getCookies();
        Optional<Cookie> cookieOptional = CookieUtil.findSessionIdCookie(cookies);
        if (cookieOptional.isEmpty()) {
            return handleUnauthenticatedUser(request, response, handler);
        }
        Cookie sessionIdCookie = cookieOptional.get();
        Session session = sessionService.getValidSession(UUID.fromString(sessionIdCookie.getValue()));
        UserDto userDto = userMapper.userToUserDto(session.getUser());

        request.setAttribute("authUserDto", userDto);
        return true;
    }

    private boolean handleUnauthenticatedUser(HttpServletRequest request,
                                              HttpServletResponse response,
                                              Object handler) throws IOException {
        if (handler instanceof HandlerMethod handlerMethod) {
            Class<?> controllerClass = handlerMethod.getBeanType();

            if (controllerClass.equals(SearchLocationsController.class)) {
                response.sendRedirect(request.getContextPath() + HOME_URL);
                return false;
            }
        }
        return true;
    }
}
