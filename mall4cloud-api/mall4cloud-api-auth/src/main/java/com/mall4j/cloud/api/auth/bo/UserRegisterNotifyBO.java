package com.mall4j.cloud.api.auth.bo;

import com.mall4j.cloud.api.auth.dto.AuthAccountDTO;
import com.mall4j.cloud.api.auth.dto.UserRegisterDTO;
import com.mall4j.cloud.api.auth.dto.UserRegisterExtensionDTO;

import java.util.List;

/**
 * 用户批量用户注册
 * @author cl
 * @date 2021-05-14 14:22:14
 */
public class UserRegisterNotifyBO {

    /**
     * 账户信息
     */
    private List<AuthAccountDTO> accountDTOList;
    /**
     * 用户信息
     */
    private List<UserRegisterDTO> userRegisterList;
    /**
     * 用户注册信息
     */
    private List<UserRegisterExtensionDTO> userRegisterExtensionDTOList;

    public UserRegisterNotifyBO() {

    }

    public UserRegisterNotifyBO(List<UserRegisterDTO> userRegisterList, List<UserRegisterExtensionDTO> userRegisterExtensionDTOList) {
        this.userRegisterList = userRegisterList;
        this.userRegisterExtensionDTOList = userRegisterExtensionDTOList;
    }

    public UserRegisterNotifyBO(List<AuthAccountDTO> accountDTOList, List<UserRegisterDTO> userRegisterList, List<UserRegisterExtensionDTO> userRegisterExtensionDTOList) {
        this.accountDTOList = accountDTOList;
        this.userRegisterList = userRegisterList;
        this.userRegisterExtensionDTOList = userRegisterExtensionDTOList;
    }

    public List<AuthAccountDTO> getAccountDTOList() {
        return accountDTOList;
    }

    public void setAccountDTOList(List<AuthAccountDTO> accountDTOList) {
        this.accountDTOList = accountDTOList;
    }

    public List<UserRegisterDTO> getUserRegisterList() {
        return userRegisterList;
    }

    public void setUserRegisterList(List<UserRegisterDTO> userRegisterList) {
        this.userRegisterList = userRegisterList;
    }

    public List<UserRegisterExtensionDTO> getUserRegisterExtensionDTOList() {
        return userRegisterExtensionDTOList;
    }

    public void setUserRegisterExtensionDTOList(List<UserRegisterExtensionDTO> userRegisterExtensionDTOList) {
        this.userRegisterExtensionDTOList = userRegisterExtensionDTOList;
    }

    @Override
    public String toString() {
        return "UserRegisterNotifyBO{" +
                "accountDTOList=" + accountDTOList +
                ", userRegisterList=" + userRegisterList +
                ", userRegisterExtensionDTOList=" + userRegisterExtensionDTOList +
                '}';
    }
}
