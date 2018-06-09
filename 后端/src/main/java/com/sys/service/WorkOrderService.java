package com.sys.service;

import com.sys.dao.WorkOrderMapper;
import com.sys.entity.CreditOption;
import com.sys.entity.User;
import com.sys.entity.WorkOrder;
import com.sys.exception.AppException;
import com.sys.utils.PowerEnum;
import com.sys.utils.ServiceUtils;
import com.sys.utils.StatusEnum;
import com.sys.utils.TimeUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * @author sys
 */
@Service
public class WorkOrderService {


    /**
     * 工单状态
     */
    public static Integer OPEN = 1;
    public static Integer PASS = 2;
    public static Integer REJECT = 3;


    @Autowired
    private WorkOrderMapper workOrderDao;

    @Autowired
    private UserService userService;

    @Autowired
    private CreditOptionService creditOptionService;

    @Autowired
    private CreditAuthService authService;

    /**
     * 发起一个工单
     * @param userId
     * @param workOrder
     * @return
     */
    public void openNewWorkOrder(Integer userId, WorkOrder workOrder) {

        workOrder.setUuid(UUID.randomUUID().toString().replace("-", ""));
        workOrder.setUserId(userId);
        workOrder.setStatus(OPEN);
        workOrder.setCreateTime(new Date(System.currentTimeMillis()));
        int i = workOrderDao.insertSelective(workOrder);
        if (i < 1) {
            throw new AppException(StatusEnum.FAILED.getCode(), StatusEnum.FAILED.getMsg());
        }
    }

    /**
     * 关闭一个工单
     * @param id
     * @param uuid
     */
   /* public void closeWorkOrder(Integer id, String uuid) {

        WorkOrder workOrder = workOrderDao.findByUuid(uuid);
        if (workOrder != null && !workOrder.getStatus().equals(CLOSE)) {

            if (workOrder.getCreditId().equals(id)) {
                workOrder.setStatus(CLOSE);
                workOrder.setEndTime(new Date(System.currentTimeMillis()));
                workOrderDao.updateByPrimaryKeySelective(workOrder);
            } else {
                throw new AppException(StatusEnum.NO_PERMISSION.getCode(), StatusEnum.NO_PERMISSION.getMsg());
            }
        } else {
            throw new AppException(StatusEnum.ORDER_NOT_EXIST.getCode(), StatusEnum.ORDER_NOT_EXIST.getMsg());
        }
    }*/

    /**
     * 工单审核通过
     * @param id
     * @param uuid
     */
    @Transactional(rollbackFor = Exception.class)
    public void passWorkOrder(User user, String uuid, Float otherScore) {

        WorkOrder workOrder = workOrderDao.findByUuid(uuid);
        if (workOrder != null) {
            //查询用户是否拥有该工单审核权。
            Integer creditId = workOrder.getCreditId();
            boolean res = authService.haveAuthByUserIdAndCreditId(user.getId(), creditId);
            if (!res) {
                throw new AppException(StatusEnum.NO_PERMISSION.getCode(), StatusEnum.NO_PERMISSION.getMsg());
            }

            if (workOrder.getStatus().equals(OPEN)) {
                Float score = creditOptionService.getScoreByCreditId(workOrder.getCreditId());
                Float sumScore = score + otherScore;
                userService.addScore(user, workOrder.getUserId(), sumScore);
                workOrder.setAuditorId(user.getId());
                workOrder.setStatus(PASS);
                workOrder.setScore(score);
                workOrder.setEndTime(new Date());
                workOrderDao.updateByPrimaryKeySelective(workOrder);
            } else {
                throw new AppException(StatusEnum.NO_PERMISSION.getCode(), StatusEnum.NO_PERMISSION.getMsg());
            }
        } else {
            throw new AppException(StatusEnum.ORDER_NOT_EXIST.getCode(), StatusEnum.ORDER_NOT_EXIST.getMsg());
        }
    }

    /**
     * 驳回工单
     * @param id
     * @param reply
     * @param uuid
     */
    @Transactional
    public void rejectWorkOrder(User user, String reply, String uuid) {

        WorkOrder workOrder = workOrderDao.findByUuid(uuid);
        if (workOrder != null) {
            //查询用户是否拥有该工单审核权。
            Integer creditId = workOrder.getCreditId();
            boolean res = authService.haveAuthByUserIdAndCreditId(user.getId(), creditId);
            if (!res) {
                throw new AppException(StatusEnum.NO_PERMISSION.getCode(), StatusEnum.NO_PERMISSION.getMsg());
            }
            if (workOrder.getStatus().equals(OPEN)) {

                workOrder.setAuditorId(user.getId());
                workOrder.setReply(reply);
                workOrder.setStatus(REJECT);
                workOrder.setEndTime(new Date());
                workOrderDao.updateByPrimaryKeySelective(workOrder);
            } else {
                throw new AppException(StatusEnum.NO_PERMISSION.getCode(), StatusEnum.NO_PERMISSION.getMsg());
            }
        } else {
            throw new AppException(StatusEnum.ORDER_NOT_EXIST.getCode(), StatusEnum.ORDER_NOT_EXIST.getMsg());
        }
    }

    /**
     * 查询一个工单
     * @param uuid
     * @return
     */
    public WorkOrder findByUuid(Integer id, String uuid) {

        //todo 权限(自己或者管理员)
        WorkOrder order = workOrderDao.findByUuid(uuid);
        return order;
    }

    /**
     * 查看订单列表
     * @param id
     * @param status
     * @return
     */
    @Transactional
    public List<Map> findByStatus(User currentUser, String account, Integer status, Integer page) {

        if (!(currentUser.getAccount().equals(account) && currentUser.getLevel() == 1)) {
            ServiceUtils.Authentication(currentUser.getLevel(), PowerEnum.TEACHER.getLevel());
        }
        if (status == 0) {
            status = null;
        }
        Integer userId = null;
        if (StringUtils.isNotBlank(account)) {
            userId = userService.getUser(account).getId();
        }
        int start = (page - 1) * 10;
        //查看自己权限
        List<WorkOrder> orders;
        if (currentUser.getLevel() > 1) {

            List<Long> categoryIds = authService.getAuthListByUserId((long) currentUser.getId());
            if (categoryIds.size() > 0) {
                orders = workOrderDao.findByStatusAndCreditIds(userId, status, start, categoryIds);
            }else {
                throw new AppException(StatusEnum.DATA_NOT_EXSIST.getCode(),StatusEnum.DATA_NOT_EXSIST.getMsg());
            }
        } else {

            orders = workOrderDao.findByStatus(userId, status, start);
        }
        int rows = workOrderDao.getRows();
        List<Map> res = new ArrayList<>();
        for (WorkOrder workOrder : orders) {

            Map map = new HashMap();
            User user = userService.getUser(workOrder.getUserId());
            CreditOption creditOption = creditOptionService.getCreditOptionById(workOrder.getCreditId());
            map.put("id", workOrder.getUuid());
            map.put("account", user.getAccount());
            map.put("name", user.getName());
            map.put("category", creditOption.getCategory());
            map.put("rank", creditOption.getRank());
            map.put("createTime", TimeUtils.formatTime(workOrder.getCreateTime().toString()));
            if (workOrder.getEndTime() != null) {
                map.put("endTime", TimeUtils.formatTime(workOrder.getEndTime().toString()));
            } else {
                map.put("endTime", "");
            }
            map.put("status", workOrder.getStatus());
            map.put("score", workOrder.getScore());
            map.put("msg", workOrder.getMsg());
            map.put("reply", workOrder.getReply());
            map.put("url", workOrder.getUrl());
            map.put("creditId", workOrder.getCreditId());
            if (workOrder.getAuditorId() != null) {

                map.put("authorName", userService.getUser(workOrder.getAuditorId()).getName());
            } else {
                map.put("authorName", "");
            }
            map.put("rows", rows);
            res.add(map);
        }

        if (res.size() > 0) {

            return res;
        } else {
            throw new AppException(StatusEnum.DATA_NOT_EXSIST.getCode(), StatusEnum.DATA_NOT_EXSIST.getMsg());
        }
    }

    @Transactional
    public Object getOrderSum(User user) {

        Integer level = user.getLevel();
        if (level < 2 || level > 5) {
            throw new AppException(StatusEnum.NO_PERMISSION.getCode(), StatusEnum.NO_PERMISSION.getMsg());
        }

        List<Long> list = authService.getAuthListByUserId((long) user.getId());
        Long creditId;
        long pengdingSum = 0;
        long handledSum = 0;
        for (int i = 0; i < list.size(); i++) {

            creditId = list.get(i);
            //获取待处理工单数
            pengdingSum += workOrderDao.getRowsByPending(Math.toIntExact(creditId));
        }
        //获取自己处理的工单数
        handledSum += workOrderDao.getRowsByHandled(user.getId());
        Map map = new HashMap();
        map.put("pending", pengdingSum);
        map.put("handled", handledSum);
        return map;
    }
}
