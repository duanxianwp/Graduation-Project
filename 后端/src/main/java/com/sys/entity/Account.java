package com.sys.entity;

import java.io.Serializable;
import java.util.Date;


/**
 * @author sys
 */

public class Account implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer id;

    private String account;

    private String password;

    private String email;

    private Date createTime;

    private Date updateTime;

    public Account() {
    }

    public Account(String account, String password, String email, Date createTime, Date updateTime) {
        this.account = account;
        this.password = password;
        this.email = email;
        this.createTime = createTime;
        this.updateTime = updateTime;
    }

    public Account(Integer id, String account, String password, String email, Date createTime, Date updateTime) {
        this.id = id;
        this.account = account;
        this.password = password;
        this.email = email;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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
        return "Account{" +
                "id=" + id +
                ", account=" + account +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                '}';
    }
}
