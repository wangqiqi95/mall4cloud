package com.mall4j.cloud.biz.service.cp;

import com.mall4j.cloud.api.platform.vo.StaffVO;
import com.mall4j.cloud.biz.wx.cp.constant.AssignTypeEunm;
import com.mall4j.cloud.biz.wx.cp.constant.PushStatusEunm;
import com.mall4j.cloud.biz.dto.cp.ResignAssignLogQueryDTO;
import com.mall4j.cloud.biz.dto.cp.StaffAssginDTO;
import com.mall4j.cloud.biz.dto.cp.StaffAssginGroupDTO;
import com.mall4j.cloud.biz.model.cp.CustGroupAssignDetail;
import com.mall4j.cloud.biz.model.cp.ResignAssignLog;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.vo.PageVO;
import me.chanjar.weixin.common.error.WxErrorException;

import java.util.List;


/**
 * 离职分配日志表
 *
 * @author hwy
 * @date 2022-01-24 11:05:43
 */
public interface ResignAssignLogService {

	/**
	 * 分页获取离职分配日志表列表
	 * @param pageDTO 分页参数
	 * @return 离职分配日志表列表分页数据
	 */
	PageVO<ResignAssignLog> page(PageDTO pageDTO, ResignAssignLogQueryDTO request);
	/**
	 * 更新离职分配日志表
	 * @param resignAssignLog 更新离职分配日志表
	 */
	void update(ResignAssignLog resignAssignLog);


	/**
	 * 离职分配日志表
	 * @param id id
	 * @return ResignAssignLog
	 */
	ResignAssignLog getById(Long id);

	/**
	 * 同步给微信
	 * @param  pushStatusEunm 推送状态
	 * @return List<ResignAssignLog>
	 */
	List<ResignAssignLog> sycnCustList( PushStatusEunm pushStatusEunm);

	/**
	 * 离职客户分配
	 * @param log 分配详情
	 */
	void custDimissionExtends(ResignAssignLog log) throws WxErrorException;

	/**
	 * 客群离职继承
	 * @param assignLog 离职分配记录
	 */
	void groupDimissionExtends(ResignAssignLog assignLog);

	/**
	 * 在职分配客群继承
	 * @param assignLog 离职分配记录
	 */
	void groupExtends(ResignAssignLog assignLog);

	/**
	 * 定时任务在职分配同步到微信
	 * @param assignLog 分配日志
	 */
	void custExtends(ResignAssignLog assignLog) throws WxErrorException;

	/**
	 * 检测客户接替情况
	 * @param assignLog 分配日志
	 * @param  assignType 分配方式
	 */
	void custReplayResult(ResignAssignLog assignLog,AssignTypeEunm assignType) throws WxErrorException;

	/**
	 * 客户分配
	 * @param staffVO 原添加客户
	 * @param replaceByStaff 替换客户
	 * @param request 分配信息
	 */
    void assignCust(StaffVO staffVO, StaffVO replaceByStaff, StaffAssginDTO request);
	/**
	 * 客群分配
	 * @param staffVO 原添加客户
	 * @param replaceByStaff 替换客户
	 * @param request 分配信息
	 */
	void assignGroup(StaffVO staffVO, StaffVO replaceByStaff, StaffAssginGroupDTO request);

	/**
	 *  重新分配
	 * @param detail
	 */
    void reAssign(CustGroupAssignDetail detail);
}

