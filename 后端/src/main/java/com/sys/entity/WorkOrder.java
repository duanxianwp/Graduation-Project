package com.sys.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * @author sys
 */

public class WorkOrder implements Serializable{

    private static final long serialVersionUID = 1L;

    private Long Id;

    /**
     * 工单ID
     */
    private String uuid;

    /**
     * 发起人ID
     */
    private Integer userId;

    /**
     * 奖项ID
     */
    private Integer creditId;

    /**
     * 发起说明
     */
    private String msg;

    /**
     * 奖项证明图片URL
     */
    private String url;

    /**
     * 驳回说明
     */
    private String reply;

    /**
     * 工单计分
     */
    private Float score;

    /**
     * 工单状态
     */
    private Integer status;

    /**
     * 审核人ID
     */
    private Integer auditorId;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 结束时间
     */
    private Date endTime;

    public WorkOrder() {
    }

    public WorkOrder(String uuid, Integer userId, Integer creditId, String msg, String url, String reply, Integer status, Integer auditorId, Date createTime, Date endTime) {
        this.uuid = uuid;
        this.userId = userId;
        this.creditId = creditId;
        this.msg = msg;
        this.url = url;
        this.reply = reply;
        this.status = status;
        this.auditorId = auditorId;
        this.createTime = createTime;
        this.endTime = endTime;
    }

    public WorkOrder(Long id, String uuid, Integer userId, Integer creditId, String msg, String url, String reply, Integer status, Integer auditorId, Date createTime, Date endTime) {
        Id = id;
        this.uuid = uuid;
        this.userId = userId;
        this.creditId = creditId;
        this.msg = msg;
        this.url = url;
        this.reply = reply;
        this.status = status;
        this.auditorId = auditorId;
        this.createTime = createTime;
        this.endTime = endTime;
    }

    public Float getScore() {
        return score;
    }

    public void setScore(Float score) {
        this.score = score;
    }

    public Long getId() {
        return Id;
    }

    public void setId(Long id) {
        Id = id;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getCreditId() {
        return creditId;
    }

    public void setCreditId(Integer creditId) {
        this.creditId = creditId;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getAuditorId() {
        return auditorId;
    }

    public void setAuditorId(Integer auditorId) {
        this.auditorId = auditorId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getReply() {
        return reply;
    }

    public void setReply(String reply) {
        this.reply = reply;
    }

    @Override
    public String toString() {
        return "WorkOrder{" +
                "Id=" + Id +
                ", uuid='" + uuid + '\'' +
                ", userId=" + userId +
                ", creditId=" + creditId +
                ", msg='" + msg + '\'' +
                ", url='" + url + '\'' +
                ", reply='" + reply + '\'' +
                ", score=" + score +
                ", status=" + status +
                ", auditorId=" + auditorId +
                ", createTime=" + createTime +
                ", endTime=" + endTime +
                '}';
    }
}
