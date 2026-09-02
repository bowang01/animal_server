package com.example.config.interceptor;

import cn.hutool.core.util.StrUtil;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.example.entity.User;
import com.example.exception.ServiceException;
import com.example.service.IUserService;
import com.example.common.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class JwtInterceptor implements HandlerInterceptor {

    @Autowired
    private IUserService userService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String token = request.getHeader("token");
        if (StrUtil.isBlank(token)) {
            token = request.getParameter("token");
        }
        // Skip if handler is not a method
/*        if(handler instanceof HandlerMethod) {
            AuthAccess annotation = ((HandlerMethod) handler).getMethodAnnotation(AuthAccess.class);
            if (annotation != null) {
                return true;
            }
        }*/
        if (handler instanceof HandlerMethod) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;

            AuthAccess annotation =
                    handlerMethod.getMethodAnnotation(AuthAccess.class);

            System.out.println("========== JWT DEBUG ==========");
            System.out.println("Request URI: " + request.getRequestURI());
            System.out.println("Handler: " + handlerMethod.getMethod());
            System.out.println("AuthAccess: " + annotation);

            if (annotation != null) {
                System.out.println("========== SKIP JWT ==========");
                return true;
            }
        }
        // Authenticate
        if (StrUtil.isBlank(token)) {
            throw new ServiceException(Constants.CODE_401, "No token, please login again");
        }
        // Get user id from token
        String userId;
        try {
            userId = JWT.decode(token).getAudience().get(0);
        } catch (JWTDecodeException j) {
            throw new ServiceException(Constants.CODE_401, "Token verification failed, please login again");
        }
        // Query user by token user id
        User user = userService.getById(userId);
        if (user == null) {
            throw new ServiceException(Constants.CODE_401, "User not found, please login again");
        }
        // Verify token signed with user password
        JWTVerifier jwtVerifier = JWT.require(Algorithm.HMAC256(user.getPassword())).build();
        try {
            jwtVerifier.verify(token); // Verify token
        } catch (JWTVerificationException e) {
            throw new ServiceException(Constants.CODE_401, "Token verification failed, please login again");
        }
        return true;
    }
}
