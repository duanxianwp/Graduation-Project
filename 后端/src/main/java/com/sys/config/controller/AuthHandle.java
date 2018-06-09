package com.sys.config.controller;

import com.mysql.jdbc.StringUtils;
import com.sys.exception.AppException;
import com.sys.utils.RedisUtils;
import com.sys.utils.StatusEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@Component
public class AuthHandle extends HandlerInterceptorAdapter {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        if (request.getMethod().equals("OPTIONS")) return true;
        Auth auth = getAuth(request, handler);

        if (auth.getStatus() == 0 || auth.getData() != null) {

            request.setAttribute("auth", auth);
            return true;
        } else {
            throw new AppException(StatusEnum.UN_LOGIN.getCode(), StatusEnum.UN_LOGIN.getMsg());
        }
    }

    public Auth getAuth(HttpServletRequest request, Object handler) {

        if (handler instanceof HandlerMethod) {

            HandlerMethod handlerMethod = (HandlerMethod) handler;
            Token t = handlerMethod.getMethod().getDeclaredAnnotation(Token.class);

            Auth auth = new Auth();
            if (t == null) {

                auth.setStatus(0);

            } else {

                String token = request.getHeader("token");
                if (StringUtils.isEmptyOrWhitespaceOnly(token)) {
                    throw new AppException(StatusEnum.ILLEGAL_CLIENT.getCode(), StatusEnum.ILLEGAL_CLIENT.getMsg());
                }
                auth.setStatus(1);
                auth.setToken(token);
                auth.setData(RedisUtils.get(token));

            }
            return auth;
        } else {
            throw new AppException(StatusEnum.SERVER_ERROR.getCode(), StatusEnum.SERVER_ERROR.getMsg());
        }

    }
}
