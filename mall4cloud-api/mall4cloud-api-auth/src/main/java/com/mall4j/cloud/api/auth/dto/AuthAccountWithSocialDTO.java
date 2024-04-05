package com.mall4j.cloud.api.auth.dto;

/**
 * @author FrozenWatermelon
 * @date 2021/1/19
 */
public class AuthAccountWithSocialDTO {

    private AuthAccountDTO authAccount;

    private AuthSocialDTO authSocial;

    public AuthAccountDTO getAuthAccount() {
        return authAccount;
    }

    public void setAuthAccount(AuthAccountDTO authAccount) {
        this.authAccount = authAccount;
    }

    public AuthSocialDTO getAuthSocial() {
        return authSocial;
    }

    public void setAuthSocial(AuthSocialDTO authSocial) {
        this.authSocial = authSocial;
    }

    @Override
    public String toString() {
        return "AuthAccountWithSocialDTO{" +
                "authAccount=" + authAccount +
                ", authSocial=" + authSocial +
                '}';
    }
}
