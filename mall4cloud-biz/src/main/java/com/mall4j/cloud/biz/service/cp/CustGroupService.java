package com.mall4j.cloud.biz.service.cp;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mall4j.cloud.api.biz.dto.cp.CpCustGroupCountDTO;
import com.mall4j.cloud.api.biz.vo.CustGroupStaffCountVO;
import com.mall4j.cloud.biz.dto.cp.CustGroupDTO;
import com.mall4j.cloud.biz.dto.cp.GetSelectedGroupListDTO;
import com.mall4j.cloud.biz.model.cp.CpCustGroup;
import com.mall4j.cloud.biz.vo.cp.CustGroupCountVO;
import com.mall4j.cloud.biz.vo.cp.CustGroupVO;
import com.mall4j.cloud.biz.vo.cp.SoldGroupCustVO;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.vo.PageVO;

import java.util.List;

/**
 * 客户群表
 *
 * @author hwy
 * @date 2022-02-16 09:29:57
 */
public interface CustGroupService extends IService<CpCustGroup> {

	/**
	 * 分页获取客户群表列表
	 *
	 * @param pageDTO 分页参数
	 * @return 客户群表列表分页数据
	 */
	PageVO<CustGroupVO> page(PageDTO pageDTO, CustGroupDTO request);

	/**
	 * 导出群信息
	 * @param request
	 * @return
	 */
	List<SoldGroupCustVO> soldList(CustGroupDTO request);

	/**
	 * 根据客户群表id获取客户群表
	 *
	 * @param id 客户群表id
	 * @return 客户群表
	 */
	CpCustGroup getById(String id);

	/**
	 * 根据客户群表id删除客户群表
	 *
	 * @param id 客户群表id
	 */
	void deleteById(String id);

	/**
	 * 统计群聊数及客户数
	 *
	 * @param request 查询条件
	 * @return CustGroupCountVO
	 */
	CustGroupCountVO count(CustGroupDTO request);

	/**
	 * 给群加标签
	 *
	 * @param id     群id
	 * @param tagIds 标签id
	 */
	void addTag(String id, List<Long> tagIds);

	/**
	 * 查询已关联活动的群信息
	 *@param pageDTO 活动id
	 * @param groupCodeDTO 活动id
	 * @return PageVO<CustGroupVO>
	 */
	PageVO<CpCustGroup> getSelectGroupList(PageDTO pageDTO, GetSelectedGroupListDTO groupCodeDTO);

	/**
	 * 根据活动id查询所有关联的客户群
	 * @param id 活动id
	 * @return List<CustGroup>
	 */
    List<CpCustGroup> getCustGroupList(Long id);

	/**
	 * 根据群主员工id查询所有的群
	 * @param addBy 群主员工id
	 * @return 关联的群的列表
	 */
	List<CpCustGroup> getCustGroupListByOwnerId(Long addBy);

	/**
	 * 离职分配，将分配的群的状态改为离职分配中
	 * @param ids 离职群id
	 */
    void updateBatchStatusToAssigning(List<String> ids);

	/**
	 * 好友统计-员工群信息
	 * @return
	 */
    List<CustGroupStaffCountVO> groupCountByStaff(CpCustGroupCountDTO dto);

	/**
	 * 根据群聊id查询群聊信息
	 * @param ids
	 * @return
	 */
	List<CpCustGroup> getByIds(List<String> ids);
}