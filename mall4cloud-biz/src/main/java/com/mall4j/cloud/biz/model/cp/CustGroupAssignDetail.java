package com.mall4j.cloud.biz.model.cp;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.mall4j.cloud.api.platform.vo.StaffOrgVO;
import com.mall4j.cloud.api.user.vo.UserStaffCpRelationListVO;
import com.mall4j.cloud.biz.wx.cp.constant.FlagEunm;
import com.mall4j.cloud.biz.wx.cp.constant.StatusType;
import com.mall4j.cloud.common.model.BaseModel;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 客群分配明细表 
 *
 * @author hwy
 * @date 2022-02-10 18:25:57
 */
@NoArgsConstructor
@Data
public class CustGroupAssignDetail extends BaseModel implements Serializable{
    private static final long serialVersionUID = 1L;
    /**
     * 
     */
    private Long id;

    /**
     * 客户/群ID
     */
    private String custGroupId;


    /**
     * 群客户数
     */
    private Integer nums;

    /**
     * 离职操作ID
     */
    private Long resignId;

    /**
     * 原添加员工/原群主
     */
    private Long addBy;

    /**
     * 原添加员工/原群主
     */
    private String addByUserId;

    /**
     * 原添加员工姓名/群主姓名
     */
    private String addByName;

    /**
     * 接替员工
     */
    private Long replaceBy;
    /**
     * 原添加员工/原群主
     */
    private String replaceByUserId;
    /**
     * 接替员工姓名
     */
    private String replaceByName;

    /**
     * 客户姓名/群名
     */
    private String name;

    /**
     * 客户手机号码
     */
    private String mobile;

    /**
     * 客户等级
     */
    private String level;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 分配类型 0:在职分配--客户 1：离职分配-客户 2：离职分配-客群
     */
    private Integer assignType;

    /**
     * 接替员工门店
     */
    private Long storeId;

    /**
     * 接替员工门店名称
     */
    private String storeName;

    /**
     *0:客户 1：客群
     */
    private Integer type;

    /**
     * 分配状态 0 分配中  1 分配成功  2 分配失败
     */
    private Integer status;

    /**
     * 删除标识
     */
    private Integer flag;

	/**
	 * 创建人
	 */
	private Long createBy;

	/**
	 * 创建人姓名
	 */
	private String createName;

	private Date assignTime;

    private String remark;

    @TableField(exist = false)
    private List<StaffOrgVO> staffOrgs;

    public CustGroupAssignDetail(ResignAssignLog resignAssignLog, UserStaffCpRelationListVO userStaff){
        this.setAddBy(resignAssignLog.getAddBy());
        this.setAddByName(resignAssignLog.getAddStaffName());
        this.setReplaceBy(resignAssignLog.getReplaceBy());
        this.setReplaceByName(resignAssignLog.getReplaceStaffName());
        this.setResignId(resignAssignLog.getId());
        this.setAssignType(resignAssignLog.getAssignType());
        this.setFlag(FlagEunm.USE.getCode());
        this.setUpdateTime(resignAssignLog.getCreateTime());
        this.setCreateTime(resignAssignLog.getCreateTime());
        this.setStatus(StatusType.WX.getCode());
        this.setCreateBy(resignAssignLog.getCreateBy());
        this.setCreateName(resignAssignLog.getCreateName());
        this.setReplaceByUserId(resignAssignLog.getReplaceByUserId());
        this.setAddByUserId(resignAssignLog.getAddByUserId());
        this.setStoreId(resignAssignLog.getReplaceByStoreId());
        this.setStoreName(resignAssignLog.getReplaceByStoreName());
        this.setCreateTime(new Date());
        if(userStaff!=null){
            this.setCustGroupId(userStaff.getQiWeiUserId());
            this.setName(userStaff.getQiWeiNickName());
//            this.setMobile(userStaff.getCpRemarkMobiles());
            this.setNums(0);
        }
    }
}
