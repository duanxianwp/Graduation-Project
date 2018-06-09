package com.sys.service;

import com.sys.dao.AccountMapper;
import com.sys.dao.CreditOptionMapper;
import com.sys.dao.UserMapper;
import com.sys.entity.CreditOption;
import com.sys.entity.User;
import com.sys.exception.AppException;
import com.sys.utils.*;
import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.*;

/**
 * @author sys
 */
@Service
public class CreditOptionService {

    @Autowired
    private CreditOptionMapper creditOptionDao;

    @Autowired
    private UserMapper userMapper;

    /**
     * 创建一条奖项记录
     * @param creditOption
     * @return
     */
    @Transactional
    public CreditOption createCreditOption(Integer id, Integer level, CreditOption creditOption) {

        ServiceUtils.Authentication(level, PowerEnum.ADMIN.getLevel());

        //判断此项是否已存在
        List<CreditOption> options = creditOptionDao.getOptions(creditOption.getCategory(), creditOption.getRank(), 0);
        if (options.size() > 0) {
            throw new AppException(StatusEnum.DATA_AlREADY_EXSIST.getCode(), StatusEnum.DATA_AlREADY_EXSIST.getMsg());
        }
        Date now = new Date();
        creditOption.setCreateTime(now);
        creditOption.setUpdateTime(now);
        creditOption.setCreateId(id);
        creditOptionDao.insertSelective(creditOption);
        return creditOption;
    }

    /**
     * 更新一条奖项记录
     * @param creditOption
     * @return
     */
    @Transactional
    public CreditOption updateCreditOption(Integer level, CreditOption creditOption) {

        ServiceUtils.Authentication(level, PowerEnum.ADMIN.getLevel());
        Date now = new Date();
        creditOption.setUpdateTime(now);
        int i = creditOptionDao.updateByPrimaryKeySelective(creditOption);
        if (i > 0) {
            return creditOption;
        } else {
            throw new AppException(StatusEnum.DATA_NOT_EXSIST.getCode(), StatusEnum.DATA_NOT_EXSIST.getMsg());
        }
    }

    /**
     * 删除一条奖项记录
     * @param id
     */
    @Transactional
    public void deleteCreditOption(Integer level, Integer id) {

        ServiceUtils.Authentication(level, PowerEnum.ADMIN.getLevel());
        int i = creditOptionDao.deleteByPrimaryKey(id);
        if (i < 1) {
            throw new AppException(StatusEnum.DATA_NOT_EXSIST.getCode(), StatusEnum.DATA_NOT_EXSIST.getMsg());
        }
    }

    /**
     * 查询单条奖项记录
     * @param id
     * @return
     */
    public CreditOption getCreditOptionInfoById(Integer level, Integer id) {

        CreditOption creditOption = creditOptionDao.selectByPrimaryKey(id);

        if (creditOption == null) {
            throw new AppException(StatusEnum.DATA_NOT_EXSIST.getCode(), StatusEnum.DATA_NOT_EXSIST.getMsg());
        } else {
            return creditOption;
        }
    }

    /**
     * 获取奖项列表
     * @return
     */
    @Transactional
    public List<CreditOption> findAll(Integer page) {

        int start = (page - 1) * 10;
        List<CreditOption> list = creditOptionDao.findAll(start);
        int rows = creditOptionDao.getRecordSum();
        Iterator<CreditOption> iterator = list.iterator();
        List res = new ArrayList();
        while (iterator.hasNext()) {
            CreditOption next = iterator.next();
            Map map = BeanUtils.toMap(next);
            map.put("rows", rows);
            map.put("createTime", TimeUtils.formatTime(map.get("createTime").toString()));
            map.put("updateTime", TimeUtils.formatTime(map.get("updateTime").toString()));
            map.put("createName", userMapper.selectByPrimaryKey(Integer.parseInt(map.get("createId").toString())).getName());
            res.add(map);
        }
        if (res.size() > 0) {
            return res;
        } else {
            throw new AppException(StatusEnum.DATA_NOT_EXSIST.getCode(), StatusEnum.DATA_NOT_EXSIST.getMsg());
        }
    }

    /**
     * 查询奖项类别(All)
     * @return
     */
    public List<String> findCategory() {
        return creditOptionDao.findCategory();
    }

    /**
     * 根据类别查询奖项列表
     * @param category
     * @return
     */
    public List<CreditOption> getOptions(String category, String rank, Integer page) {

        int start = (page - 1) * 10;
        if (StringUtils.isEmpty(rank)) {
            rank = null;
        }
        List<CreditOption> list = creditOptionDao.getOptions(category, rank, start);
        int rows = creditOptionDao.getRecordSum();
        Iterator<CreditOption> iterator = list.iterator();
        List res = new ArrayList();
        while (iterator.hasNext()) {
            CreditOption next = iterator.next();
            Map map = BeanUtils.toMap(next);
            map.put("rows", rows);
            map.put("createTime", TimeUtils.formatTime(map.get("createTime").toString()));
            map.put("updateTime", TimeUtils.formatTime(map.get("updateTime").toString()));
            map.put("createName", userMapper.selectByPrimaryKey(Integer.parseInt(map.get("createId").toString())).getName());
            res.add(map);
        }
        if (res.size() > 0) {
            return res;
        } else {
            throw new AppException(StatusEnum.DATA_NOT_EXSIST.getCode(), StatusEnum.DATA_NOT_EXSIST.getMsg());
        }

    }

    public CreditOption getCreditOptionById(Integer id) {

        CreditOption creditOption = creditOptionDao.selectByPrimaryKey(id);
        return creditOption;
    }

    public Float getScoreByCreditId(Integer creditId) {

        Float score = creditOptionDao.selectByPrimaryKey(creditId).getScore();
        return score;
    }

    public List<CreditOption> getOptionsByCategory(String category) {
        return creditOptionDao.getOptionsByCategory(category);
    }
}
