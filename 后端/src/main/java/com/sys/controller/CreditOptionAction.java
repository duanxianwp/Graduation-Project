package com.sys.controller;

import com.sys.config.controller.Auth;
import com.sys.config.controller.Token;
import com.sys.entity.CreditOption;
import com.sys.entity.User;
import com.sys.exception.AppException;
import com.sys.service.CreditOptionService;
import com.sys.utils.RequestUtils;
import com.sys.utils.ResultUtils;
import com.sys.utils.StatusEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author sys
 */
@RestController
@RequestMapping(value = "/creditOption")
public class CreditOptionAction {

    @Autowired
    private CreditOptionService creditOptionService;

    /**
     * 添加一条奖项记录
     * @param creditOption
     * @return
     */
    @PostMapping(value = "/add")
    @Token
    public Object addCreditOption(HttpServletRequest request, CreditOption creditOption) {

        User currentUser = RequestUtils.getCurrentUser(request);
        creditOption = creditOptionService.createCreditOption(currentUser.getId(), currentUser.getLevel(), creditOption);
        return ResultUtils.success(creditOption);
    }

    /**
     * 更新一条奖项记录
     * @param creditOption
     * @return
     */
    @PutMapping(value = "/update")
    @Token
    public Object updateCreditOption(HttpServletRequest request, CreditOption creditOption) {

        if (creditOption.getId() == null) {
            throw new AppException(StatusEnum.PARAM_ERROR.getCode(), StatusEnum.PARAM_ERROR.getMsg());
        }
        User currentUser = RequestUtils.getCurrentUser(request);
        creditOption = creditOptionService.updateCreditOption(currentUser.getLevel(), creditOption);
        return ResultUtils.success(creditOption);

    }

    @DeleteMapping(value = "/delete")
    @Token
    public Object deleteCreditOption(@RequestParam(value = "id") Integer Id, HttpServletRequest request) {

        if (Id == null || Id < 1) {
            throw new AppException(StatusEnum.PARAM_ERROR.getCode(), StatusEnum.PARAM_ERROR.getMsg());
        }
        User currentUser = RequestUtils.getCurrentUser(request);
        creditOptionService.deleteCreditOption(currentUser.getLevel(), Id);
        return ResultUtils.success();
    }

    /**
     * 查询奖项列表
     * @return
     */
    @GetMapping(value = "/getAllOptions")
    public Object getAllCreditOptions(@RequestParam(value = "page") Integer page) {

        List<CreditOption> options = creditOptionService.findAll(page);
        return ResultUtils.success(options);
    }

    @GetMapping(value = "/getCategory")
    @Token
    public Object getCategory() {

        List<String> list = creditOptionService.findCategory();
        return ResultUtils.success(list);
    }

    /**
     * 根据类别和内容查询奖项列表
     * @param category
     * @return
     */
    @GetMapping(value = "/getOptions")
    @Token
    public Object getOptions(@RequestParam(value = "category") String category,
                                       @RequestParam(value = "rank") String rank,
                                       @RequestParam(value = "page") Integer page) {

        List<CreditOption> options = creditOptionService.getOptions(category,rank, page);
        return ResultUtils.success(options);
    }

    @GetMapping(value = "/getOptionsByCategory")
    @Token
    public Object getOptions(@RequestParam(value = "category") String category) {

        List<CreditOption> options = creditOptionService.getOptionsByCategory(category);
        return ResultUtils.success(options);
    }

    @GetMapping(value = "/getScoreByCreditId")
    @Token
    public Object getScoreByCreditId(@RequestParam(value = "creditId") Integer creditId){

        Float res = creditOptionService.getScoreByCreditId(creditId);
        return ResultUtils.success(res);
    }
}
