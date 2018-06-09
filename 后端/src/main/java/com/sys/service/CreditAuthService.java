package com.sys.service;

import com.sys.dao.CreditAuthMapper;
import com.sys.entity.CreditAuth;
import com.sys.entity.CreditOption;
import com.sys.entity.User;
import com.sys.exception.AppException;
import com.sys.utils.PowerEnum;
import com.sys.utils.ServiceUtils;
import com.sys.utils.StatusEnum;
import com.sys.utils.TimeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * @author wp
 * @date 2018/5/4 16:12
 */
@Service
public class CreditAuthService {


    @Autowired
    private CreditAuthMapper creditAuthMapper;

    @Autowired
    private UserService userService;

    @Autowired
    private CreditOptionService creditOptionService;

    @Transactional
    public void newAuthRecord(Integer level, long creditId, String account) {

        User user = userService.getUserByAccount(level, account);
        if (user.getLevel() < 2 || user.getLevel() > 5) {
            throw new AppException(StatusEnum.UNKNOWN_EXCEPTION.getCode(), "授权失败,只能授权给教师");
        }

        List<CreditAuth> list = creditAuthMapper.getAuth((long) user.getId(), creditId);
        if (list.size() > 0) {
            throw new AppException(StatusEnum.DATA_AlREADY_EXSIST.getCode(), "不能重复授权");
        }

        CreditAuth creditAuth = new CreditAuth();
        creditAuth.setCreditId(creditId);
        creditAuth.setUserId((long) user.getId());
        creditAuth.setStatus(1);
        creditAuth.setCreateTime(new Date());
        int i = creditAuthMapper.insertSelective(creditAuth);
        if (i < 1) {
            throw new AppException(StatusEnum.DATA_NOT_EXSIST.getCode(), StatusEnum.DATA_NOT_EXSIST.getMsg());
        }
    }

    @Transactional
    public void undoAuth(Integer level, long id) {

        ServiceUtils.Authentication(level, PowerEnum.ADMIN.getLevel());
        CreditAuth creditAuth = new CreditAuth();
        creditAuth.setId(id);
        creditAuth.setStatus(0);
        creditAuth.setUpdateTime(new Date());
        int i = creditAuthMapper.updateByPrimaryKeySelective(creditAuth);
        if (i < 1) {
            throw new AppException(StatusEnum.SERVER_ERROR.getCode(), StatusEnum.SERVER_ERROR.getMsg());
        }
    }

    @Transactional
    public List<Map> getAuthListByAccount(Integer level, String account, Integer page) {

        ServiceUtils.Authentication(level, PowerEnum.ADMIN.getLevel());
        long start = (page - 1) * 10;
        User user = userService.getUserByAccount(level, account);
        if (user == null) {
            throw new AppException(StatusEnum.DATA_NOT_EXSIST.getCode(), StatusEnum.DATA_NOT_EXSIST.getMsg());
        }
        List<CreditAuth> authList = creditAuthMapper.getAuthList((long) user.getId(), start);
        int recordSum = creditAuthMapper.getRecordSum();
        List<Map> res = new ArrayList<>(authList.size());

        Map map;
        for (int i = 0; i < authList.size(); i++) {

            map = new HashMap();
            CreditAuth creditAuth = authList.get(i);
            Long creditId = creditAuth.getCreditId();
            Long userId = creditAuth.getUserId();
            map.put("id", creditAuth.getId());
            map.put("userId", userId);
            CreditOption option = creditOptionService.getCreditOptionInfoById(level, Math.toIntExact(creditId));
            map.put("category", option.getCategory());
            map.put("rank", option.getRank());
            map.put("createTime", TimeUtils.formatTime(creditAuth.getCreateTime().toString()));
            map.put("updateTime", TimeUtils.formatTime(creditAuth.getUpdateTime()));
            map.put("rows", recordSum);
            res.add(map);
        }

        return res;
    }

    public List<Long> getAuthListByUserId(Long id) {

        return creditAuthMapper.getCreditIds(id);
    }

    public boolean haveAuthByUserIdAndCreditId(Integer id, Integer creditId) {

        return creditAuthMapper.getAuthByUserIdAndCreditId(id, creditId) > 0;
    }


}
