package com.sys.controller;

import com.sys.config.controller.Token;
import com.sys.entity.User;
import com.sys.entity.WorkOrder;
import com.sys.service.WorkOrderService;
import com.sys.utils.RequestUtils;
import com.sys.utils.ResultUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * @author sys
 */
@RestController
@RequestMapping(value = "/workOrder")
public class WorkOrderAction {

    @Autowired
    private WorkOrderService orderService;

    @PostMapping(value = "/openNewOrder")
    @Token
    public Object openNewWorkOrder(HttpServletRequest request, WorkOrder workOrder) {

        User currentUser = RequestUtils.getCurrentUser(request);
        orderService.openNewWorkOrder(currentUser.getId(), workOrder);
        return ResultUtils.success(workOrder);
    }

   /* @GetMapping(value = "/closeOrder")
    @Token
    public Object closeWorkOrder(@RequestParam(value = "uuid") String uuid,
                                 HttpServletRequest request) {

        User currentUser = RequestUtils.getCurrentUser(request);
        orderService.closeWorkOrder(currentUser.getId(), uuid);
        return ResultUtils.success();
    }*/

    @GetMapping(value = "/getWorkOrderList")
    @Token
    public Object getWorkOrderList(@RequestParam("account") String account,
                                   @RequestParam("status") Integer status,
                                   @RequestParam("page") Integer page,
                                   HttpServletRequest request) {

        User currentUser = RequestUtils.getCurrentUser(request);
        List<Map> res = orderService.findByStatus(currentUser, account, status, page);
        return ResultUtils.success(res);
    }

    @PostMapping(value = "/passWorkOrder")
    @Token
    public Object passWorkOrder(@RequestParam(value = "id") String uuid,
                                @RequestParam(value = "otherScore", defaultValue = "0.0") Float otherScore,
                                HttpServletRequest request) {
        User currentUser = RequestUtils.getCurrentUser(request);
        orderService.passWorkOrder(currentUser, uuid, otherScore);
        return ResultUtils.success();

    }

    @PostMapping(value = "/rejectWorkOrder")
    @Token
    public Object rejectWorkOrder(@RequestParam("id") String uuid,
                                  @RequestParam("msg") String reply,
                                  HttpServletRequest request) {

        User currentUser = RequestUtils.getCurrentUser(request);
        orderService.rejectWorkOrder(currentUser, reply, uuid);
        return ResultUtils.success();

    }

    @GetMapping(value = "/getOrderSum")
    @Token
    public Object getOrderSumByPending(HttpServletRequest request){

        User currentUser = RequestUtils.getCurrentUser(request);
        Object map = orderService.getOrderSum(currentUser);
        return ResultUtils.success(map);
    }
}
