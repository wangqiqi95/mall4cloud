package com.mall4j.cloud.biz.service.cp;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mall4j.cloud.api.biz.dto.cp.CpUserGroupDTO;
import com.mall4j.cloud.api.biz.vo.UserJoinCustGroupVO;
import com.mall4j.cloud.biz.dto.cp.PageQueryCustInfo;
import com.mall4j.cloud.biz.model.cp.CpGroupCustInfo;
import com.mall4j.cloud.biz.vo.cp.CpGroupCustInfoVO;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import org.apache.ibatis.annotations.Param;
import org.springframework.web.bind.annotation.RequestBody;
import org.w3c.dom.stylesheets.LinkStyle;

import java.util.List;

/**
 * 客户群表
 *
 * @author hwy
 * @date 2022-02-16 09:29:57
 */
public interface GroupCustInfoService extends IService<CpGroupCustInfo> {

	PageVO<UserJoinCustGroupVO> appPageGroupByUser(CpUserGroupDTO dto);

	/**
	 * 分页获取客群客户表
	 *
	 * @param pageDTO 分页参数
	 * @return 客群客户表分页数据
	 */
	PageVO<CpGroupCustInfoVO> page(PageDTO pageDTO, PageQueryCustInfo request);

	List<CpGroupCustInfoVO> soldExcel(PageQueryCustInfo request);

	/**
	 * 根据客群客户表userId groupId获取
	 * @param userId 客户外部联系人id
	 * @param groupId 客群id
	 * @return 客群客户表
	 */
	CpGroupCustInfo getById(String userId,String groupId);

	/**
	 * 保存客群客户表
	 * @param groupCustInfo 客群客户表
	 */
	void saveCpGroupCustInfo(CpGroupCustInfo groupCustInfo);

	/**
	 * 更新客群客户表
	 * @param groupCustInfo 客群客户表
	 */
	void updateCpGroupCustInfo(CpGroupCustInfo groupCustInfo);

	/**
	 * 根据客群客户表 id删除客群客户表
	 * @param userId 客户外部联系人id
	 * @param groupId 客群id
	 */
	void deleteById(String userId,String groupId);

	/**
	 * 删除客群客户
	 * @param groupId 客群id
	 */
    void deleteByGroupId(String groupId,String updateBy);
}