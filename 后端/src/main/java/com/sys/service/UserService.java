package com.sys.service;

import com.sys.dao.AccountMapper;
import com.sys.dao.UserMapper;
import com.sys.entity.Account;
import com.sys.entity.ExcelUser;
import com.sys.entity.User;
import com.sys.exception.AppException;
import com.sys.utils.*;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.util.*;

/**
 * @author sys
 */
@Service
public class UserService {

    @Autowired
    private UserMapper userDao;

    @Autowired
    private AccountMapper accountDao;

    @Autowired
    private AccountService accountService;

    /**
     * 删除一个用户
     * @param Id
     */
    public void deleteUser(Integer level, Integer Id) {

        ServiceUtils.Authentication(level, PowerEnum.ADMIN.getLevel());
        userDao.deleteByPrimaryKey(Id);
    }

    /**
     * 根据学院获取专业列表
     * @param college
     * @return
     */
    public List<String> getProfessionListByCollege(String college) {

        return userDao.getProfession(college);
    }

    /**
     * 根据(院系&&专业&&班级)查询用户列表
     */
    @Transactional
    public List<User> getUsersByUninCondition(User currentUser, String college, String profession, String classroom, Integer page) {

        /**
         * 鉴权
         * 该学院领导 level = 4 college
         * 该专业领导 level = 3 profession
         * 该班级老师 level = 2 profession classroom
         * 管理员 level = 9
         */
        int level = currentUser.getLevel().intValue();
        List<String> classes = null;
        if (level == 2) {

            classes = Arrays.asList(currentUser.getClassroom().split(","));
        }
        String currentProfession = currentUser.getProfession();
        if (level == 9 || level == 5 || (level == 2 && currentProfession.equals(profession) && classes.contains(classroom)) || (level == 3 && currentProfession.equals(profession)) || (level == 4 && currentUser.getCollege().equals(college))) {

            int start = (page - 1) * 10;
            List res = new ArrayList();
            if (StringUtils.isBlank(classroom)) {
                classroom = null;
            }
            List<User> result = null;
            //这边是前台传了个profession 实际上是college ,为了方便操作
            if (currentUser.getCollege().equals(profession)) {
                college = profession;
                profession = null;
                result = userDao.getScoreList(college, null, null, null);
            } else {
                result = userDao.getUsersByUninCondition(profession, classroom, start);
            }
            int rows = userDao.getRows();
            Iterator<User> iterator = result.iterator();
            while (iterator.hasNext()) {
                User next = iterator.next();
                Map map = new HashMap();
                map.put("id", next.getId());
                map.put("account", next.getAccount());
                map.put("name", next.getName());
                map.put("college", next.getCollege());
                map.put("profession", next.getProfession());
                map.put("classroom", next.getClassroom());
                map.put("score", next.getScore());
                map.put("sum", rows);
                res.add(map);
            }

            if (res.size() > 0) {
                return res;
            } else {
                throw new AppException(StatusEnum.DATA_NOT_EXSIST.getCode(), StatusEnum.DATA_NOT_EXSIST.getMsg());
            }
        } else {
            throw new AppException(StatusEnum.NO_PERMISSION.getCode(), StatusEnum.NO_PERMISSION.getMsg());
        }
    }


    /**
     * 获取用户学分数
     * @param account
     * @return
     */
    public Float getUserScore(String account) {

        User user = userDao.findByAccount(account);
        return user.getScore();
    }

    /**
     * 获取权限范围内的classes
     */
    public List<Map<String, String>> getClasses(User currentUser) {

        List<Map<String, String>> res = new ArrayList<>();

        Integer level = currentUser.getLevel();
        switch (level) {

            case 2:
                String profession = currentUser.getProfession();
                List list = Arrays.asList(currentUser.getClassroom().split(","));
                Map map;
                for (int i = 0; i < list.size(); i++) {
                    map = new HashMap();
                    map.put("profession", profession);
                    map.put("classroom", list.get(i));
                    res.add(map);
                }
                break;
            case 3:
                list = userDao.getClassroomByCollegeAndProfession(currentUser.getCollege(), currentUser.getProfession());
                dataHandle(res, list);
                map = new HashMap();
                map.put("profession", currentUser.getProfession());
                map.put("classroom", "");
                res.add(map);

                break;
            case 4:
                list = userDao.getClassroomByCollege(currentUser.getCollege());
                List<String> pro = userDao.getProfession(currentUser.getCollege());
                dataHandle(res, list);
                for (int i = 0; i < pro.size(); i++) {
                    map = new HashMap();
                    String s = pro.get(i);
                    if (StringUtils.isBlank(s)) {
                        map.put("profession", currentUser.getCollege());
                    } else {

                        map.put("profession", pro.get(i));
                    }
                    map.put("classroom", "");
                    res.add(map);
                }
                break;
            case 5:
                //这个不改了, 没时间
                list = userDao.getClassroom();
                dataHandle(res, list);
                break;
            case 9:
                list = userDao.getClassroom();
                dataHandle(res, list);
                break;
            default:
        }
        //list 去重
        BeanUtils.removeDuplicateWithOrder(res);
        return res;
    }

    public void dataHandle(List<Map<String, String>> res, List<Map<String, String>> list) {

        for (Map value : list) {
            Map map = new HashMap();
            map.put("profession", value.get("profession"));
            map.put("classroom", value.get("classroom"));
            res.add(map);
        }
    }

    @Transactional
    public List<Map> getScoreList(User currentUser, Integer page) {

        Integer level = currentUser.getLevel();
        int start = (page - 1) * 10;
        List<User> list = new ArrayList<>();
        switch (level) {

            case 2:
                String profession = currentUser.getProfession();
                List classes = Arrays.asList(currentUser.getClassroom().split(","));
                list = userDao.getScoreListByParamList(profession, classes, start);
                break;
            case 3:
                String college = currentUser.getCollege();
                profession = currentUser.getProfession();
                list = userDao.getScoreList(college, profession, null, start);
                break;
            case 4:
                college = currentUser.getCollege();
                list = userDao.getScoreList(college, null, null, start);
                break;
            case 5:
                list = userDao.getScoreList(null, null, null, start);
                break;
            case 9:
                list = userDao.getScoreList(null, null, null, start);
                break;
            default:

        }
        int rows = userDao.getRows();
        List<Map> res = new ArrayList<>();
        for (User user : list) {

            Map map = new HashMap();
            map = BeanUtils.toMap(user);
            map.put("rows", rows);
            map.remove("createTime");
            map.remove("updateTime");
            map.remove("level");
            map.remove("token");
            res.add(map);
        }
        return res;
    }

    @Transactional
    public List<Map> getStuByAccount(User currentUser, String account, Integer page) {
        Integer level = currentUser.getLevel();
        if (level < 2 || level > 5) {
            throw new AppException(StatusEnum.NO_PERMISSION.getCode(), StatusEnum.NO_PERMISSION.getMsg());
        }
        int start = (page - 1) * 10;
        List<User> list = new ArrayList<>(1);
        String profession = currentUser.getProfession();
        String college = currentUser.getCollege();
        switch (level) {

            case 2:

                List classes = Arrays.asList(currentUser.getClassroom().split(","));
                list = userDao.getStuByAccount(account, profession, classes, start);
                break;
            case 3:
                list = userDao.getStuByAccount1(account, profession, start);
                break;
            case 4:
                list = userDao.getStuByAccount2(account, college, start);
                break;
            case 5:
                break;
        }

        int rows = userDao.getRows();
        List<Map> res = new ArrayList<>();
        for (User user : list) {

            Map map = new HashMap();
            map = BeanUtils.toMap(user);
            map.put("rows", rows);
            map.remove("createTime");
            map.remove("updateTime");
            map.remove("level");
            map.remove("token");
            res.add(map);
        }
        return res;
    }

    /**
     * 添加用户
     * @param user
     * @return
     */
    @Transactional
    public User addUser(Integer level, User user) {

        ServiceUtils.Authentication(level, PowerEnum.ADMIN.getLevel());

        //查询是否已存在
        String username = user.getAccount();
        if (userDao.findByAccount(username) != null) {
            throw new AppException(StatusEnum.DATA_AlREADY_EXSIST.getCode(), StatusEnum.DATA_AlREADY_EXSIST.getMsg());
        }

        user.setScore(0.0f);
        Date now = new Date();
        Account account = new Account();
        account.setAccount(user.getAccount());
        account.setPassword(AlgorithmUtils.pwdEncry(user.getAccount()));
        account.setCreateTime(now);
        account.setUpdateTime(now);
        user.setCreateTime(now);
        user.setUpdateTime(now);
        userDao.insertSelective(user);
        accountDao.insertSelective(account);

        return user;
    }

    @Transactional
    public void updateUser(User currentUser, User user, String email) {

        ServiceUtils.Authentication(currentUser.getLevel(), PowerEnum.ADMIN.getLevel());
        accountService.updEmail(currentUser, email, user.getAccount());
        //设置账号为空，避免账号被修改
        user.setAccount(null);
        int i = userDao.updateByPrimaryKeySelective(user);
        if (i < 1) {
            throw new AppException(StatusEnum.FAILED.getCode(), StatusEnum.FAILED.getMsg());
        }
    }

    public User getUserByAccount(Integer level, String account) {

        ServiceUtils.Authentication(level, PowerEnum.ADMIN.getLevel());
        User user = userDao.findByAccount(account);
        if (user == null) {
            throw new AppException(StatusEnum.DATA_NOT_EXSIST.getCode(), StatusEnum.DATA_NOT_EXSIST.getMsg());
        } else {
            return user;
        }
    }

    @Transactional
    public List FuzzySearchUser(Integer level, String account, String name, Integer page) {

        ServiceUtils.Authentication(level, PowerEnum.ADMIN.getLevel());

        List<User> result;
        List<Map<String, Object>> res = new ArrayList<>();
        int start = (page - 1) * 10;

        if (!StringUtils.isEmpty(account)) {
            if (!StringUtils.isEmpty(name)) {

                result = userDao.FuzzySearchUserByAccountAndName(account, name, start);
            } else {
                result = userDao.FuzzySearchUserByAccount(account, start);
            }
        } else if (!StringUtils.isEmpty(name)) {

            result = userDao.FuzzySearchUserByName(name, start);
        } else {

            throw new AppException(StatusEnum.FAILED.getCode(), StatusEnum.FAILED.getMsg());
        }
        int rows = userDao.getRows();
        Iterator<User> iterator = result.iterator();
        while (iterator.hasNext()) {
            User next = iterator.next();
            Map map = BeanUtils.toMap(next);
            map.put("email", accountService.getEmail(next.getAccount()));
            map.put("updateTime", TimeUtils.formatTime(map.get("updateTime").toString()));
            map.put("level", LevelUtils.getLevel(Integer.parseInt(map.get("level").toString())));
            map.put("sum", rows);
            map.remove("createTime");
            map.remove("token");
            res.add(map);
        }

        if (res.size() > 0) {

            return res;
        } else {
            throw new AppException(StatusEnum.DATA_NOT_EXSIST.getCode(), StatusEnum.DATA_NOT_EXSIST.getMsg());
        }
    }

    public User getUser(Integer id) {

        User user = userDao.selectByPrimaryKey(id);
        return user;
    }

    public User getUser(String account) {

        User user = userDao.findByAccount(account);
        return user;
    }

    @Transactional
    public void addScore(User user, Integer userId, float score) {

        ServiceUtils.Authentication(user.getLevel(), PowerEnum.STUDENT.getLevel());
        User u = userDao.selectByPrimaryKey(userId);
        u.setScore(u.getScore() + score);
        userDao.updateByPrimaryKeySelective(u);
    }

    public void batchImportUser(Integer level, MultipartFile file) {

        String fileType = file.getOriginalFilename().split("\\.")[1];
        if (!(("xls").equals(fileType) || ("xlsx").equals(fileType))) {
            throw new AppException(StatusEnum.FAILED.getCode(), "文件类型不支持");
        }
        List<ExcelUser> users = POIUtils.importExcelFile(file, fileType);
        for (int i = 0; i < users.size(); i++) {

            ExcelUser user = users.get(i);
            if (user.getLevel() < 1 || getUser(user.getAccount()) != null) {

                continue;
            }
            addUser(level, user);
        }
    }

    public Workbook getExcel(User currentUser, String college, String profession, String classroom) {

        ServiceUtils.Authentication(currentUser.getLevel(), PowerEnum.TEACHER.getLevel());
        if (StringUtils.isBlank(classroom)) {
            classroom = null;
        }

        List<User> result = null;
        //这边是前台传了个profession 实际上是college ,为了方便操作
        if (currentUser.getCollege().equals(profession)) {
            college = profession;
            profession = null;
            result = userDao.getScoreList(college, null, null, null);
        } else {
            result = userDao.getUsersByUninCondition(profession, classroom, null);
        }
        Workbook workbook = POIUtils.exportExcelFile(result);
        return workbook;
    }
}
