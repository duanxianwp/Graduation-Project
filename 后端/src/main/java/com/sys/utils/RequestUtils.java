package com.sys.utils;

import com.sys.config.controller.Auth;
import com.sys.entity.User;

import javax.servlet.http.HttpServletRequest;

public class RequestUtils {

    public static User getCurrentUser(HttpServletRequest request) {

        Auth auth = (Auth) request.getAttribute("auth");
        return ((User) auth.getData());
    }
}
