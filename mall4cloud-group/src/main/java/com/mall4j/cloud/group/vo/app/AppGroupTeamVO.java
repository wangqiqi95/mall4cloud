package com.mall4j.cloud.group.vo.app;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.mall4j.cloud.common.serializer.ImgJsonSerializer;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;

/**
 * @author YXF
 * @date 2021/3/26
 */
public class AppGroupTeamVO {

    @ApiModelProperty("拼团团队id")
    private Long groupTeamId;

    @ApiModelProperty("活动成团人数")
    private Integer groupNumber;

    @ApiModelProperty("已参团人数")
    private Integer joinNum;

    @ApiModelProperty("结束时间")
    private Date endTime;

    @ApiModelProperty("服务器当前时间")
    private Date nowTime;

    @ApiModelProperty("团长user_Id")
    private Long shareUserId;

    @ApiModelProperty("团长昵称")
    private String shareNickName;

    @JsonSerialize(using = ImgJsonSerializer.class)
    @ApiModelProperty("团长头像")
    private String sharePic;

    @ApiModelProperty("团订单状态(0:待成团，1:拼团中，2:拼团成功，3:拼团失败)")
    private String status;

    /**
     * 店铺ID
     */
    @JsonIgnore
    private Long shopId;

    /**
     * 拼团活动ID
     */
    @JsonIgnore
    private Long groupActivityId;

    @ApiModelProperty("团购活动商品id")
    private Long groupSpuId;

    public Long getGroupSpuId() {
        return groupSpuId;
    }

    public void setGroupSpuId(Long groupSpuId) {
        this.groupSpuId = groupSpuId;
    }

    public Long getGroupTeamId() {
        return groupTeamId;
    }

    public void setGroupTeamId(Long groupTeamId) {
        this.groupTeamId = groupTeamId;
    }

    public Integer getGroupNumber() {
        return groupNumber;
    }

    public void setGroupNumber(Integer groupNumber) {
        this.groupNumber = groupNumber;
    }

    public Integer getJoinNum() {
        return joinNum;
    }

    public void setJoinNum(Integer joinNum) {
        this.joinNum = joinNum;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public Date getNowTime() {
        return nowTime;
    }

    public void setNowTime(Date nowTime) {
        this.nowTime = nowTime;
    }

    public Long getShareUserId() {
        return shareUserId;
    }

    public void setShareUserId(Long shareUserId) {
        this.shareUserId = shareUserId;
    }

    public String getShareNickName() {
        return shareNickName;
    }

    public void setShareNickName(String shareNickName) {
        this.shareNickName = shareNickName;
    }

    public String getSharePic() {
        return sharePic;
    }

    public void setSharePic(String sharePic) {
        this.sharePic = sharePic;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Long getShopId() {
        return shopId;
    }

    public void setShopId(Long shopId) {
        this.shopId = shopId;
    }

    public Long getGroupActivityId() {
        return groupActivityId;
    }

    public void setGroupActivityId(Long groupActivityId) {
        this.groupActivityId = groupActivityId;
    }

    @Override
    public String toString() {
        return "AppGroupTeamVO{" +
                "groupTeamId=" + groupTeamId +
                ", groupNumber=" + groupNumber +
                ", joinNum=" + joinNum +
                ", endTime=" + endTime +
                ", nowTime=" + nowTime +
                ", shareUserId='" + shareUserId + '\'' +
                ", shareNickName='" + shareNickName + '\'' +
                ", sharePic='" + sharePic + '\'' +
                ", status='" + status + '\'' +
                ", shopId=" + shopId +
                ", groupActivityId=" + groupActivityId +
                '}';
    }
}
