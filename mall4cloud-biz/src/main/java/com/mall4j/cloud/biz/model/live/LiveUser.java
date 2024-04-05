

package com.mall4j.cloud.biz.model.live;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.mall4j.cloud.common.model.BaseModel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author lhd
 * @date 2020-11-20 17:49:56
 */
@Data
@ApiModel("直播成员")
public class LiveUser extends BaseModel implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 直播成员表
     */
    @ApiModelProperty("直播成员表")
    @TableId
    private Long liveUserId;
    /**
     * 店铺id
     */
    @ApiModelProperty("店铺id")
    private Long shopId;
    /**
     * 昵称
     */
    @ApiModelProperty("昵称")
    private String nickName;
    /**
     * 头像
     */
    @ApiModelProperty("头像")
    private String headingImg;
    /**
     * 微信号
     */
    @ApiModelProperty("微信号 必传")
    private String anchorWechat;
    /**
     * 脱敏微信号
     */
    @ApiModelProperty("脱敏微信号")
    private String userName;
    /**
     * 角色列表
     */
    @ApiModelProperty("角色列表")
    private String roles;
    /**
     * openId
     */
    @ApiModelProperty("openId")
    private String openId;

    @ApiModelProperty("角色列表 取值[1-管理员，2-主播，3-运营者]，设置超级管理员将无效")
    @TableField(exist = false)
    private List<Integer> roleList;

}
