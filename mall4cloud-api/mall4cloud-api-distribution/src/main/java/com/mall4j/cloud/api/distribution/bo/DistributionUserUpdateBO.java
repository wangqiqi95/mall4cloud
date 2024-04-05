package com.mall4j.cloud.api.distribution.bo;

/**
 * 修改用户分销员的昵称或者头像
 * @author cl
 * @date 2021-08-14 15:59:51
 */
public class DistributionUserUpdateBO {
    /**
     * ID
     */
    private Long userId;

    /**
     * 用户昵称
     */
    private String nickName;

    /**
     * 头像图片路径
     */
    private String pic;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    @Override
    public String toString() {
        return "DistributionUserUpdateBO{" +
                "userId=" + userId +
                ", nickName='" + nickName + '\'' +
                ", pic='" + pic + '\'' +
                '}';
    }
}
