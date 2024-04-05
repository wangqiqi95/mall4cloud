package com.mall4j.cloud.biz.model.cp;

import java.io.Serializable;
import java.util.Date;

import com.mall4j.cloud.api.platform.vo.StaffVO;
import com.mall4j.cloud.biz.wx.cp.constant.FlagEunm;
import com.mall4j.cloud.biz.wx.cp.constant.PushStatusEunm;
import com.mall4j.cloud.biz.wx.cp.constant.StatusType;
import com.mall4j.cloud.biz.dto.cp.StaffAssginDTO;
import com.mall4j.cloud.biz.dto.cp.StaffAssginGroupDTO;
import com.mall4j.cloud.common.model.BaseModel;
import com.mall4j.cloud.common.security.AuthUserContext;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 离职分配日志表
 *
 * @author hwy
 * @date 2022-01-24 11:05:43
 */
@NoArgsConstructor
@Data
@EqualsAndHashCode(callSuper = true)
public class ResignAssignLog extends BaseModel implements Serializable{
    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    @ApiModelProperty("分配id")
    private Long id;

    /**
     * 操作人
     */
    @ApiModelProperty("操作人id")
    private Long createBy;
    /**
     * 操作人门店id
     */
    @ApiModelProperty("操作人门店id")
    private Long storeId;
    /**
     * 操作人名称
     */
    @ApiModelProperty("操作人名称")
    private String createName;

    /**
     * 分配类型
     * AssignTypeEnum
     */
    @ApiModelProperty("分配类型: 0:在职分配-客户/1:离职分配-客户/2:离职分配-客群/3:在职分配-客群")
    private Integer assignType;

    /**
     * 原添加员工
     */
    @ApiModelProperty("原添加员工Id")
    private Long addBy;

    /**
     * 原添加员工
     */
    @ApiModelProperty("原添加员工企业微信Id")
    private String addByUserId;
    /**
     * 原添加员工
     */
    @ApiModelProperty("原添加员工姓名")
    private String addStaffName;


    /**
     * 接替员工
     */
    @ApiModelProperty("接替员工id")
    private Long replaceBy;
    /**
     * 原添加员工
     */
    @ApiModelProperty("原添替换员工企业微信Id")
    private String replaceByUserId;

    /**
     * 接替员工
     */
    @ApiModelProperty("接替员工姓名")
    private String replaceStaffName;

    /**
     * 需要分配的客群数
     */
    @ApiModelProperty("需要分配的客群数")
    private Integer assignTotal;

    /**
     * 分配成功的人数/群数
     */
    @ApiModelProperty("分配成功的人数")
    private Integer successTotal;

    /**
     * 状态
     */
    @ApiModelProperty("状态 0 待接替  1 完成接替 ")
    private Integer status;

    /**
     * 信息
     */
    @ApiModelProperty("提示信息")
    private String msg;

    /**
     * 推送消息情况  0 待推送 1 推送完成
     */
    private Integer pushStatus;

    /**
     * 删除标识
     */
    private Integer flag;

    private Long replaceByStoreId;

    private String replaceByStoreName;

    public  ResignAssignLog(StaffAssginDTO request){
        this.setMsg(request.getMsg());
        this.setAssignType(request.getAssignType());
        this.setCreateBy(AuthUserContext.get().getUserId());
        this.setCreateName(AuthUserContext.get().getUsername());
        this.setStoreId(AuthUserContext.get().getStoreId());
        this.setSuccessTotal(0);
        this.setAssignTotal(0);
        this.setStatus(StatusType.WX.getCode());
        this.setFlag(FlagEunm.USE.getCode());
        this.setPushStatus(PushStatusEunm.CREATE.getCode());
        this.setCreateTime(new Date());
        this.setUpdateTime(this.getCreateTime());
    }

    public  ResignAssignLog(StaffAssginGroupDTO request){
        this.setAssignType(request.getAssignType());
        this.setCreateBy(AuthUserContext.get().getUserId());
        this.setCreateName(AuthUserContext.get().getUsername());
        this.setStoreId(AuthUserContext.get().getStoreId());
        this.setSuccessTotal(0);
        this.setAssignTotal(0);
        this.setPushStatus(PushStatusEunm.CREATE.getCode());
        this.setStatus(StatusType.WX.getCode());
        this.setFlag(FlagEunm.USE.getCode());
        this.setCreateTime(new Date());
        this.setUpdateTime(this.getCreateTime());
    }


    public void initStaffInfo(StaffVO staffVO, StaffVO replaceByStaff,Integer totalCust) {
        if(replaceByStaff!=null) {
            this.setReplaceBy(replaceByStaff.getId());
            this.setReplaceByUserId(replaceByStaff.getQiWeiUserId());
            this.setReplaceStaffName(replaceByStaff.getStaffName());
            this.setReplaceByStoreId(replaceByStaff.getStoreId());
            this.setReplaceByStoreName(replaceByStaff.getStoreName());
        }
        this.setAddBy(staffVO.getId());
        this.setAddByUserId(staffVO.getQiWeiUserId());
        this.setAddStaffName(staffVO.getStaffName());
        this.setAssignTotal(totalCust);
        this.setPushStatus(PushStatusEunm.CREATE.getCode());
    }
}
