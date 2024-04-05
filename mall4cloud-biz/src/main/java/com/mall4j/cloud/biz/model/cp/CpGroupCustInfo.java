package com.mall4j.cloud.biz.model.cp;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.mall4j.cloud.biz.wx.cp.constant.FlagEunm;
import com.mall4j.cloud.biz.wx.cp.constant.StatusType;
import com.mall4j.cloud.biz.wx.wx.util.WechatUtils;
import com.mall4j.cloud.common.model.BaseModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import me.chanjar.weixin.cp.bean.external.WxCpUserExternalGroupChatInfo;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

@Data
@EqualsAndHashCode(callSuper = true)
public class CpGroupCustInfo extends BaseModel implements Serializable {

    /**
     *
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 客群id
     */
    private String groupId;

    /**
     * 活码id
     */
    private Long codeId;

    /**
     *
     */
    private Integer codeSource;

    /**
     * 客户id
     */
    private String userId;

    /**
     * 好友关系表id
     */
    private String cpUserRelId;

    /**
     * 1 - 企业成员,2 - 外部联系人
     */
    private Integer type;

    /**
     * 1 - 由群成员邀请入群（直接邀请入群）2 - 由群成员邀请入群（通过邀请链接入群）3 - 通过扫描群二维码入群
     */
    private Integer joinScene;

    /**
     * 退群方式: 0 - 自己退群 / 1 - 群主/群管理员移出
     */
    private Integer quitScene;

    /**
     * 邀请者的userid
     */
    private String invitorUserId;

    /**
     * 群里的昵称
     */
    private String groupNickname;

    /**
     * 名字
     */
    private String name;

    /**
     * 状态
     */
    private Integer status;

    /**
     * 删除标志
     */
    private Integer flag;

    /**
     *
     */
    private Date joinTime;

    /**
     *
     */
    private String state;

    /**
     * 邀请送达状态：0未送达/1送达
     */
    private Integer sendStatus;

    /**
     * 是否入群: 0否/1是
     */
    private Integer joinStatus;

    /**
     * 员工id
     */
    private Long staffId;

    /**
     *
     */
    private String updateBy;

    /**
     *
     */
    private String createBy;

    /**
     * 是否管理员：0否/1是
     */
    private Integer isAdmin;

    public void buildDate(CpGroupCustInfo groupCustInfo, WxCpUserExternalGroupChatInfo.GroupMember item,CpCustGroup custGroup){
        groupCustInfo.setState(item.getState());
        groupCustInfo.setGroupId(custGroup.getId());
        groupCustInfo.setFlag(FlagEunm.USE.getCode());
        groupCustInfo.setStatus(StatusType.YX.getCode());
        groupCustInfo.setUserId(item.getUserId());
        groupCustInfo.setUpdateTime(groupCustInfo.getCreateTime());
        groupCustInfo.setInvitorUserId(item.getInvitor()!=null?item.getInvitor().getUserId():null);
        groupCustInfo.setJoinTime(WechatUtils.formatDate(item.getJoinTime().toString()));
        groupCustInfo.setName(item.getName());
        groupCustInfo.setGroupNickname(item.getGroupNickname());
        groupCustInfo.setJoinScene(item.getJoinScene());
        groupCustInfo.setJoinStatus(1);
        groupCustInfo.setType(item.getType());
        groupCustInfo.setStaffId(custGroup.getOwnerId());
        if(Objects.isNull(groupCustInfo.getId())){
            groupCustInfo.setCreateTime(new Date());
            groupCustInfo.setCreateBy("群表更通知回调");
        }else{
            groupCustInfo.setUpdateTime(new Date());
            groupCustInfo.setUpdateBy("群表更通知回调");
        }
    }
}
