package com.sys.controller;

import com.sys.config.controller.Token;
import com.sys.entity.ExcelUser;
import com.sys.entity.User;
import com.sys.service.AccountService;
import com.sys.service.UserService;
import com.sys.utils.RequestUtils;
import com.sys.utils.ResultUtils;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.net.URLEncoder;

/**
 * @author sys
 */
@RestController
@RequestMapping(value = "/user")
public class UserAction {

    @Autowired
    private UserService userService;

    @Autowired
    private AccountService accountService;


    /**
     * 批量导入用户
     * @return
     */
    @PostMapping("/batchAddUser")
    @Token
    public Object batchImportUser(MultipartFile file, HttpServletRequest request, HttpServletResponse response) {
        Integer level = RequestUtils.getCurrentUser(request).getLevel();
        userService.batchImportUser(level, file);
        return ResultUtils.success();
    }

    /**
     * 添加用户
     * @param request
     * @param user
     * @return
     */
    @PostMapping(value = "/addUser")
    @Token
    public Object addUser(HttpServletRequest request, User user) {

        User currentUser = RequestUtils.getCurrentUser(request);
        user = userService.addUser(currentUser.getLevel(), user);
        return ResultUtils.success(user);
    }

    /**
     * 更新用户信息
     * @param request
     * @param user
     * @return
     */
    @PutMapping(value = "/updUser")
    @Token
    public Object updateUser(@RequestParam("email") String email, User user, HttpServletRequest request) {

        User currentUser = RequestUtils.getCurrentUser(request);
        userService.updateUser(currentUser, user, email);
        return ResultUtils.success();
    }

    @GetMapping(value = "/getUser/{account}")
    @Token
    public Object getUserByAccount(@PathVariable String account, HttpServletRequest request) {

        User currentUser = RequestUtils.getCurrentUser(request);
        User user = userService.getUserByAccount(currentUser.getLevel(), account);
        Map map = new HashMap();
        map.put("id", user.getId());
        map.put("account", user.getAccount());
        map.put("name", user.getName());
        map.put("college", user.getCollege());
        map.put("profession", user.getProfession());
        map.put("classroom", user.getClassroom());
        map.put("level", user.getLevel());
        map.put("email", accountService.getEmail(account));
        return ResultUtils.success(map);
    }

    /**
     * 模糊查询用户信息
     * @return
     */
    @GetMapping(value = "/FuzzySearchUser")
    @Token
    public Object FuzzySearchUser(@RequestParam("account") String account,
                                  @RequestParam("name") String name,
                                  @RequestParam("page") Integer page, HttpServletRequest request) {
        User currentUser = RequestUtils.getCurrentUser(request);
        List res = userService.FuzzySearchUser(currentUser.getLevel(), account, name, page);
        return ResultUtils.success(res);

    }

    @GetMapping(value = "/getUserScore")
    @Token
    public Object getUserScore(HttpServletRequest request) {

        User currentUser = RequestUtils.getCurrentUser(request);
        Float score = userService.getUserScore(currentUser.getAccount());
        Map<String, Float> map = new HashMap<String, Float>(1);
        map.put("score", score);
        return ResultUtils.success(map);
    }

    @GetMapping(value = "/getUserByCondition")
    @Token
    public Object getUsersByUninCondition(@RequestParam("college") String college,
                                          @RequestParam("profession") String profession,
                                          @RequestParam("classroom") String classroom,
                                          @RequestParam("page") Integer page,
                                          HttpServletRequest request) {

        User currentUser = RequestUtils.getCurrentUser(request);
        List<User> users = userService.getUsersByUninCondition(currentUser, college, profession, classroom, page);
        return ResultUtils.success(users);
    }

    @GetMapping(value = "/getExcel")
    @Token
    public void getExcel(@RequestParam("college") String college,
                         @RequestParam("profession") String profession,
                         @RequestParam("classroom") String classroom,
                         HttpServletRequest request,
                         HttpServletResponse response) throws IOException {

        User currentUser = RequestUtils.getCurrentUser(request);
        Workbook file = userService.getExcel(currentUser, college, profession, classroom);
        response.setHeader("content-Type", "application/vnd.ms-excel");
        response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode("学分成绩排序表", "utf-8") + ".xlsx");
        file.write(response.getOutputStream());
    }

    @GetMapping(value = "/getClasses")
    @Token
    public Object getClasses(HttpServletRequest request) {

        User currentUser = RequestUtils.getCurrentUser(request);
        List<Map<String, String>> res = userService.getClasses(currentUser);
        return ResultUtils.success(res);
    }

    @GetMapping(value = "/getScoreList")
    @Token
    public Object getScoreList(@RequestParam("page") Integer page, HttpServletRequest request) {

        User currentUser = RequestUtils.getCurrentUser(request);
        List<Map> res = userService.getScoreList(currentUser, page);
        return ResultUtils.success(res);
    }

    @GetMapping(value = "/getStuByAccount")
    @Token
    public Object getStuByAccount(@RequestParam("account") String account, @RequestParam("page") Integer page,
                                  HttpServletRequest request) {

        User currentUser = RequestUtils.getCurrentUser(request);
        List<Map> res = userService.getStuByAccount(currentUser, account, page);
        return ResultUtils.success(res);
    }
}
