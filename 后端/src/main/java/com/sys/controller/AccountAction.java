package com.sys.controller;

import com.sys.config.controller.Auth;
import com.sys.config.controller.Token;
import com.sys.entity.Account;
import com.sys.entity.User;
import com.sys.service.AccountService;
import com.sys.utils.RequestUtils;
import com.sys.utils.ResultUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author sys
 */
@RestController
@RequestMapping(value = "/account")
public class AccountAction {


    @Autowired
    private AccountService accountService;

    /**
     * 登陆
     * @param account
     * @param password
     * @return
     */
    @PostMapping(value = "/login")
    public Object login(@RequestParam("account") String account,
                        @RequestParam("password") String password) {

        User user = accountService.login(account, password);
        return ResultUtils.success(user);
    }

    /**
     * 修改密码
     * @param pwd
     * @param newPwd
     * @param request
     * @return
     */
    @PutMapping(value = "/updatePwd")
    @Token
    public Object updatePwd(@RequestParam("pwd") String pwd,
                            @RequestParam("newPwd") String newPwd,
                            HttpServletRequest request) {

        User currentUser = RequestUtils.getCurrentUser(request);
        Account accResult = accountService.updatePwd(currentUser.getAccount(), pwd, newPwd);
        accResult.setPassword(newPwd);
        return ResultUtils.success(accResult);
    }

    /**
     * 退出
     * @param request
     * @return
     */
    @GetMapping(value = "/logout")
    @Token
    public Object logout(HttpServletRequest request) {

        Auth auth = (Auth) request.getAttribute("auth");
        String token = auth.getToken();
        accountService.logout(token);
        return ResultUtils.success();
    }

    /**
     * 忘记密码
     * @return
     */
    @PutMapping(value = "/forgetPwd")
    public Object forgetPwd(@RequestParam("account") String account,
                            @RequestParam("code") String code,
                            @RequestParam("password") String password) {

        boolean res = accountService.forgetPwd(account, code, password);
        return ResultUtils.success(res);
    }

    /**
     * 获取邮箱
     */
    @GetMapping("/getEmail")
    @Token
    public Object getEmail(HttpServletRequest request) {

        User currentUser = RequestUtils.getCurrentUser(request);
        String email = accountService.getEmail(currentUser.getAccount());
        return ResultUtils.success(email);
    }

    /**
     * 设置邮箱
     */
    @PutMapping("/setEmail")
    @Token
    public Object setEmail(@RequestParam("email") String email, HttpServletRequest request) {

        User currentUser = RequestUtils.getCurrentUser(request);
        accountService.updEmail(currentUser, email, currentUser.getAccount());
        return ResultUtils.success();
    }

    @GetMapping("/sendEmail")
    public Object sendEmail(@RequestParam("account") String account) {

        String s = accountService.sendEmail(account);
        return ResultUtils.success(s);
    }

    @GetMapping("/validateCode")
    public Object validateCode(@RequestParam("account") String account,
                               @RequestParam("code") String code) {

        Boolean res = accountService.validateCode(account, code);
        return ResultUtils.success(res);
    }
}
