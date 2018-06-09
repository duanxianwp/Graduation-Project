package com.sys.service;

import com.sys.dao.AccountMapper;
import com.sys.dao.UserMapper;
import com.sys.entity.Account;
import com.sys.entity.User;
import com.sys.exception.AppException;
import com.sys.utils.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * @author sys
 */
@Service
public class AccountService {

    @Autowired
    private AccountMapper accountDao;

    @Autowired
    private UserMapper userDao;

    /**
     * 登陆
     * @param account
     * @param password
     * @return
     */
    public User login(String account, String password) {

        Account accResult = accountDao.findByAccount(account);
        if (accResult != null && AlgorithmUtils.pwdEncry(password).equals(accResult.getPassword())) {

            User user = userDao.findByAccount(account);
            String token = UUID.randomUUID().toString();
            /**
             * account->token，token->user
             * 删除token
             * 更换account对应的value(新的token)
             * 添加新的token键值对
             */
            Object res = RedisUtils.get(account);
            if (res != null) {
                RedisUtils.delete(res.toString());
            }
            RedisUtils.set(account, token);
            RedisUtils.set(token, user);
            RedisUtils.setExpire(token, 60 * 60L);
            user.setToken(token);
            return user;
        } else {

            throw new AppException(StatusEnum.ACC_OR_PWD_ERROR.getCode(), StatusEnum.ACC_OR_PWD_ERROR.getMsg());
        }
    }

    @Transactional
    public Account updatePwd(String account, String pwd, String newPwd) {

        Account accResult = accountDao.findByAccount(account);
        if (accResult != null && accResult.getPassword().equals(AlgorithmUtils.pwdEncry(pwd))) {

            accResult.setPassword(AlgorithmUtils.pwdEncry(newPwd));
            accountDao.updateByPrimaryKeySelective(accResult);
            return accResult;
        } else {
            throw new AppException(StatusEnum.ACC_OR_PWD_ERROR.getCode(), StatusEnum.ACC_OR_PWD_ERROR.getMsg());
        }
    }

    @Transactional
    public Account resetPwd(String account, String pwd) {

        Account accResult = accountDao.findByAccount(account);
        accResult.setPassword(AlgorithmUtils.pwdEncry(pwd));
        accountDao.updateByPrimaryKeySelective(accResult);
        return accResult;

    }

    public void logout(String token) {

        RedisUtils.delete(token);
    }

    public String getEmail(String account) {

        String email = accountDao.getEmailByAccount(account);
        return email;
    }

    public void updEmail(User user, String email, String account) {

        if (user.getLevel() == PowerEnum.ADMIN.getLevel() || user.getAccount().equals(account)) {

            accountDao.updEmailByAccount(email, account);
        } else {
            throw new AppException(StatusEnum.NO_PERMISSION.getCode(), StatusEnum.NO_PERMISSION.getMsg());
        }
    }

    public String sendEmail(String account) {

        String email = getEmail(account);
        if (StringUtils.isBlank(email)) {
            throw new AppException(StatusEnum.DATA_NOT_EXSIST.getCode(), StatusEnum.DATA_NOT_EXSIST.getMsg());
        }
        EmailUtils.sendValidateCode(email);

        return handleEmailAddress(email);
    }

    /**
     * 替换Email中的部分字符 (char->*)
     * @param email
     * @return
     */
    public String handleEmailAddress(String email) {

        int i = email.indexOf("@");
        char[] chars = email.toCharArray();
        if (i == 1) {
            chars[0] = '*';
        } else if (i <= 3) {
            chars[1] = '*';
        } else {
            for (int x = 2; x < i - 1; x++) {
                chars[x] = '*';
            }
        }
        return new String(chars);
    }

    public Boolean validateCode(String account, String code) {

        String email = getEmail(account);
        Object o = RedisUtils.get(email);
        if (o != null) {

            return code.trim().equals(o.toString());
        } else {
            throw new AppException(StatusEnum.DATA_NOT_EXSIST.getCode(), "验证码已过期");
        }
    }

    public boolean forgetPwd(String account, String code, String password) {

        Boolean flag = validateCode(account, code);
        if (flag) {
            Account ac = resetPwd(account, password);
            return ac == null;
        } else {
            throw new AppException(StatusEnum.ILLEGAL_CLIENT.getCode(), "操作非法或长时间未操作");
        }
    }
}
