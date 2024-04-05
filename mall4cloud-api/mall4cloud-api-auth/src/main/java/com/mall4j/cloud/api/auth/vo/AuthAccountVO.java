package com.mall4j.cloud.api.auth.vo;

import io.swagger.annotations.ApiModelProperty;

/**
 * @author FrozenWatermelon
 * @date 2020/9/22
 */
public class AuthAccountVO {
    /**
     * 全平台用户唯一id
     */
    private Long uid;

    @ApiModelProperty("邮箱")
    private Long userId;

    @ApiModelProperty("邮箱")
    private String email;

    @ApiModelProperty("手机号")
    private String phone;

    @ApiModelProperty("用户名")
    private String username;

    @ApiModelProperty("密码")
    private String password;

    @ApiModelProperty("状态 1:启用 0:禁用 -1:删除")
    private Integer status;

    @ApiModelProperty("创建ip")
    private String createIp;

    /**
     * 系统类型见SysTypeEnum
     */
    @ApiModelProperty("系统类型 0.普通用户系统 1.商家端 2.平台端")
    private Integer sysType;

    /**
     * 是否是管理员
     */
    @ApiModelProperty("是否是管理员")
    private Integer isAdmin;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public String getCreateIp() {
        return createIp;
    }

    public void setCreateIp(String createIp) {
        this.createIp = createIp;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getSysType() {
        return sysType;
    }

    public void setSysType(Integer sysType) {
        this.sysType = sysType;
    }

    public Integer getIsAdmin() {
        return isAdmin;
    }

    public void setIsAdmin(Integer isAdmin) {
        this.isAdmin = isAdmin;
    }

    @Override
    public String toString() {
        return "AuthAccountVO{" +
                "uid=" + uid +
                ", userId=" + userId +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", status=" + status +
                ", createIp='" + createIp + '\'' +
                ", sysType=" + sysType +
                ", isAdmin=" + isAdmin +
                '}';
    }
}
