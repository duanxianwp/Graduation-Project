package com.sys.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * @author sys
 */

public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer id;

    /**
     * 账号(学号或工号) NOT NULL
     */
    private String account;

    private String name;

    /**
     * 学院
     */
    private String college;

    /**
     * 专业
     */
    private String profession;

    /**
     * 班级
     */
    private String classroom;

    /**
     * 等级(管理员、老师、学生等) NOT NULL
     */
    private Integer level;

    /**
     * 学分
     */
    private Float score;

    private Date createTime;

    private Date updateTime;

    private String token;

    public User() {
    }

    public User(Integer id, String account, String name, String college, String profession, String classroom, Integer level) {
        this.id = id;
        this.account = account;
        this.name = name;
        this.college = college;
        this.profession = profession;
        this.classroom = classroom;
        this.level = level;
    }

    public User(String account, String name, String college, String profession, String classroom, Integer level, Float score, Date createTime, Date updateTime) {
        this.account = account;
        this.name = name;
        this.college = college;
        this.profession = profession;
        this.classroom = classroom;
        this.level = level;
        this.score = score;
        this.createTime = createTime;
        this.updateTime = updateTime;
    }

    public User(Integer id, String account, String name, String college, String profession, String classroom, Integer level, Float score, Date createTime, Date updateTime) {
        this.id = id;
        this.account = account;
        this.name = name;
        this.college = college;
        this.profession = profession;
        this.classroom = classroom;
        this.level = level;
        this.score = score;
        this.createTime = createTime;
        this.updateTime = updateTime;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCollege() {
        return college;
    }

    public void setCollege(String college) {
        this.college = college;
    }

    public String getProfession() {
        return profession;
    }

    public void setProfession(String profession) {
        this.profession = profession;
    }

    public String getClassroom() {
        return classroom;
    }

    public void setClassroom(String classroom) {
        this.classroom = classroom;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public Float getScore() {
        return score;
    }

    public void setScore(Float score) {
        this.score = score;
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

    public void setToken(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", account='" + account + '\'' +
                ", name='" + name + '\'' +
                ", college='" + college + '\'' +
                ", profession='" + profession + '\'' +
                ", classroom='" + classroom + '\'' +
                ", level=" + level +
                ", score=" + score +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                ", token='" + token + '\'' +
                '}';
    }
}

