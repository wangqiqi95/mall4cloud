package com.mall4j.cloud.api.auth.dto;

import java.util.List;

/**
 * @author cl
 * @date 2021-05-14 18:04:05
 */
public class AuthAccountUserDTO {

    private List<AuthAccountDTO> accountDTOList;

    private List<UserRegisterDTO> userRegisterList;

    private List<UserRegisterExtensionDTO> userRegisterExtensionDTOList;

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
        return "AuthAccountUserDTO{" +
                "accountDTOList=" + accountDTOList +
                ", userRegisterList=" + userRegisterList +
                ", userRegisterExtensionDTOList=" + userRegisterExtensionDTOList +
                '}';
    }
}
