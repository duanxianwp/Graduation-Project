package com.sys.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * @author sys
 */
public class CreditOption implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer id;

    /**
     * 类别
     */
    private String category;

    /**
     * 内容或等级(奖项)
     */
    private String rank;

    /**
     * 学分
     */
    private Float score;

    /**
     * 创建人ID
     */
    private Integer createId;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    public CreditOption() {
    }

    public CreditOption(String category, String rank, Float score, Integer createId, Date createTime, Date updateTime) {
        this.category = category;
        this.rank = rank;
        this.score = score;
        this.createId = createId;
        this.createTime = createTime;
        this.updateTime = updateTime;
    }

    public CreditOption(Integer id, String category, String rank, Float score, Integer createId, Date createTime, Date updateTime) {
        this.id = id;
        this.category = category;
        this.rank = rank;
        this.score = score;
        this.createId = createId;
        this.createTime = createTime;
        this.updateTime = updateTime;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getRank() {
        return rank;
    }

    public void setRank(String rank) {
        this.rank = rank;
    }

    public Float getScore() {
        return score;
    }

    public void setScore(Float score) {
        this.score = score;
    }

    public Integer getCreateId() {
        return createId;
    }

    public void setCreateId(Integer createId) {
        this.createId = createId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    @Override
    public String toString() {
        return "CreditOption{" +
                "id=" + id +
                ", category='" + category + '\'' +
                ", rank='" + rank + '\'' +
                ", score=" + score +
                ", createId=" + createId +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                '}';
    }
}
