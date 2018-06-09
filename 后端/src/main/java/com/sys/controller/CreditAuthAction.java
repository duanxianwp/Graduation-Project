package com.sys.controller;

import com.sys.config.controller.Token;
import com.sys.entity.CreditAuth;
import com.sys.service.CreditAuthService;
import com.sys.utils.RequestUtils;
import com.sys.utils.ResultUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * @author wp
 * @date 2018/5/4 16:32
 */
@RestController
@RequestMapping("/creditAuth")
public class CreditAuthAction {

    @Autowired
    private CreditAuthService service;

    @PostMapping("/addAuthRecord")
    @Token
    public Object AuthRecord(@RequestParam("creditId") Long creditId, @RequestParam("account") String account,
                             HttpServletRequest request) {

        Integer level = RequestUtils.getCurrentUser(request).getLevel();
        service.newAuthRecord(level, creditId, account);
        return ResultUtils.success();

    }

    @PutMapping("/undoAuth")
    @Token
    public Object undoAuth(@RequestParam("id") Long id, HttpServletRequest request) {

        Integer level = RequestUtils.getCurrentUser(request).getLevel();
        service.undoAuth(level, id);
        return ResultUtils.success();
    }

    @GetMapping("/getAuthList")
    @Token
    public Object getAuthList(@RequestParam("account") String account, @RequestParam("page") Integer page, HttpServletRequest request) {

        Integer level = RequestUtils.getCurrentUser(request).getLevel();
        List<Map> list = service.getAuthListByAccount(level, account, page);
        return ResultUtils.success(list);
    }

}
