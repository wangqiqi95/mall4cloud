package com.mall4j.cloud.biz.model.cp;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

import com.mall4j.cloud.biz.wx.cp.constant.FlagEunm;
import com.mall4j.cloud.biz.wx.cp.constant.StatusType;
import com.mall4j.cloud.biz.wx.wx.util.WechatUtils;
import com.mall4j.cloud.common.model.BaseModel;
import io.swagger.models.auth.In;
import lombok.Data;
import lombok.EqualsAndHashCode;
import me.chanjar.weixin.cp.bean.external.WxCpUserExternalGroupChatInfo;

/**
 * 客户群表
 *
 * @author hwy
 * @date 2022-02-16 09:29:57
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class CpCustGroup extends BaseModel implements Serializable{
    private static final long serialVersionUID = 1L;

    /**
     * 
     */
    private String id;
    /**
     * 群名称
     */
    private String groupName;

    /**
     * 群公告
     */
    private String notice;

    /**
     * 群主员工id
     */
    private Long ownerId;
    /**
     * 
     */
    private Date createTime;

    /**
     * 
     */
    private Date updateTime;

    /**
     * 0-无效/1-有效/2-接替中
     */
    private Integer status;

    /**
     * 客户群跟进状态。
     * 0 - 跟进人正常
     * 1 - 跟进人离职
     * 2 - 离职继承中
     * 3 - 离职继承完成
     */
    private Integer adminStatus;

    /**
     * 
     */
    private Integer flag;

    /**
     * 
     */
    private Long storeId;

    /**
     * 
     */
    private String qrCode;

    /**
     * 客户总人数
     */
    private Long totalCust;

    /**
     * 员工总人数
     */
    private Long totalStaffCust;

    /**
     * 总人数
     */
    private Integer total;

    /**
     *
     */
    private Integer totalLimit;

    private String userId;

    private String ownerName;

    private Date expireDate;

    /**
     *
     */
    private String storeName;

    private Integer groupType;


    public void initData(CpCustGroup custGroup, WxCpUserExternalGroupChatInfo.GroupChat groupChat,Integer adminStatus){
        if(Objects.nonNull(adminStatus)){
            custGroup.setAdminStatus(adminStatus);
        }
        custGroup.setCreateTime(Objects.nonNull(groupChat.getCreateTime())? WechatUtils.formatDate(groupChat.getCreateTime().toString()):new Date());
        custGroup.setFlag(FlagEunm.USE.getCode());
        custGroup.setStatus(StatusType.YX.getCode());
        custGroup.setUpdateTime(custGroup.getCreateTime());
        custGroup.setId(groupChat.getChatId());
        custGroup.setGroupName(groupChat.getName());
        custGroup.setUserId(groupChat.getOwner());
        custGroup.setTotalLimit(500);
        custGroup.setTotal(groupChat.getMemberList() != null ? groupChat.getMemberList().size() : 0);
        custGroup.setTotalCust(groupChat.getMemberList() != null ? groupChat.getMemberList().stream().filter(item -> item.getType() == 2).count(): 0);
        custGroup.setTotalStaffCust(groupChat.getMemberList() != null ? groupChat.getMemberList().stream().filter(item -> item.getType() == 1).count(): 0);
    }


}
