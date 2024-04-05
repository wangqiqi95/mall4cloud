package com.mall4j.cloud.api.auth.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author FrozenWatermelon
 * @date 2020/9/22
 */
public class AuthAccountDTO {

    /**
     * 用户名
     */
    @NotBlank(message = "username not blank")
    private String username;

    /**
     * 密码
     */
    private String password;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 手机号码
     */
    private String phone;

    /**
     * 创建ip
     */
    private String createIp;

    /**
     * 状态 1:启用 0:禁用 -1:删除
     */
    @NotNull(message = "status not null")
    private Integer status;

    /**
     * 系统类型见SysTypeEnum 0.普通用户系统 1.商家端
     */
    @NotNull(message = "sysType not null")
    private Integer sysType;

    /**
     * 用户id
     */
    @NotNull(message = "userId not null")
    private Long userId;

    /**
     * 所属租户
     */
    @NotNull(message = "tenantId not null")
    private Long tenantId;

    /**
     * 是否是管理员
     */
    @NotNull(message = "isAdmin not null")
    private Integer isAdmin;

    /**
     * 是否通过店铺审核
     */
    private Integer isPassShop;

    /**
     * 第三方系统unionid
     */
    private String bizUnionid;

    public String getBizUnionid() {
        return bizUnionid;
    }

    public void setBizUnionid(String bizUnionid) {
        this.bizUnionid = bizUnionid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getCreateIp() {
        return createIp;
    }

    public void setCreateIp(String createIp) {
        this.createIp = createIp;
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

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getTenantId() {
        return tenantId;
    }

    public void setTenantId(Long tenantId) {
        this.tenantId = tenantId;
    }

    public Integer getIsAdmin() {
        return isAdmin;
    }

    public void setIsAdmin(Integer isAdmin) {
        this.isAdmin = isAdmin;
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

    public Integer getIsPassShop() {
        return isPassShop;
    }

    public void setIsPassShop(Integer isPassShop) {
        this.isPassShop = isPassShop;
    }

    @Override
    public String toString() {
        return "AuthAccountDTO{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", createIp='" + createIp + '\'' +
                ", status=" + status +
                ", sysType=" + sysType +
                ", userId=" + userId +
                ", tenantId=" + tenantId +
                ", isAdmin=" + isAdmin +
                ", isPassShop=" + isPassShop +
                '}';
    }
}
