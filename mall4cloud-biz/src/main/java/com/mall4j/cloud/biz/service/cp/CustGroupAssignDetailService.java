package com.mall4j.cloud.biz.service.cp;

import com.mall4j.cloud.biz.wx.cp.constant.AssignTypeEunm;
import com.mall4j.cloud.biz.wx.cp.constant.UserStaffCpRelationStatusEunm;
import com.mall4j.cloud.biz.dto.cp.CustGroupAssignDetailPageDTO;
import com.mall4j.cloud.biz.model.cp.CustGroupAssignDetail;
import com.mall4j.cloud.biz.model.cp.ResignAssignLog;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.vo.PageVO;
import me.chanjar.weixin.common.error.WxErrorException;

import java.util.List;


/**
 * 客群分配明细表 
 *
 * @author hwy
 * @date 2022-02-10 18:25:57
 */
public interface CustGroupAssignDetailService {

	/**
	 * 分页获取客群分配明细表 列表
	 * @param pageDTO 分页参数
	 * @param request
	 * @return 客群分配明细表 列表分页数据
	 */
	PageVO<CustGroupAssignDetail> page(PageDTO pageDTO, CustGroupAssignDetailPageDTO request);

	/**
	 * 根据客群分配明细表 id获取客群分配明细表 
	 *
	 * @param id 客群分配明细表 id
	 * @return 客群分配明细表 
	 */
	CustGroupAssignDetail getById(Long id);

	/**
	 * 保存客群分配明细表 
	 * @param custGroupAssignDetail 客群分配明细表 
	 */
	void save(CustGroupAssignDetail custGroupAssignDetail);
	/**
	 * 更新客群分配明细表
	 * @param custGroupAssignDetail 客群分配明细表
	 */
	void update(CustGroupAssignDetail custGroupAssignDetail);

	/**
	 * 重新分配
	 * @param detail 分配信息
	 */
	void reAssign(CustGroupAssignDetail detail);

	/**
	 * 客群继承
	 * @param details 分配详情
	 * @param assignLog 分配日志
	 * @param assignType 继承方式
	 */
    void syncCust(ResignAssignLog assignLog, List<CustGroupAssignDetail> details, AssignTypeEunm assignType) throws WxErrorException;

	/**
	 * 查询群信息同步到微信
	 * @return
	 */
	List<CustGroupAssignDetail> getGroupSycnList();

	/**
	 * 同步离职员工的客群到微信进行分配
	 * @param assignLog 分配日志
	 * @param list 分配详情记录
	 */
    void syncGroup(ResignAssignLog assignLog, List<CustGroupAssignDetail> list)  throws WxErrorException;

	/**
	 * 根据条件查询分配详细信息
	 * @param id 分配日志id
	 * @param externalUserid 客户外部联系人id
	 * @return 分配详情
	 */
    CustGroupAssignDetail selectAssignDetail(Long id, String externalUserid);

	void updateUserStaffCpRelationStatus(String externalUserId, String userId, UserStaffCpRelationStatusEunm status);

}
