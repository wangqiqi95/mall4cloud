package com.mall4j.cloud.biz.mapper.cp;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mall4j.cloud.api.user.vo.SelectUserCountVO;
import com.mall4j.cloud.biz.dto.cp.CustGroupDTO;
import com.mall4j.cloud.biz.dto.cp.GetSelectedGroupListDTO;
import com.mall4j.cloud.biz.model.cp.CpCustGroup;
import com.mall4j.cloud.biz.vo.cp.CustGroupCountVO;
import com.mall4j.cloud.biz.vo.cp.CustGroupVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 客户群表
 *
 * @author hwy
 * @date 2022-02-16 09:29:57
 */
public interface CustGroupMapper extends BaseMapper<CpCustGroup> {

	/**
	 * 获取客户群表列表
	 * @return 客户群表列表
	 */
	List<CustGroupVO> list(@Param("et") CustGroupDTO request);

	/**
	 * 根据客户群表id获取客户群表
	 *
	 * @param id 客户群表id
	 * @return 客户群表
	 */
	CpCustGroup getById(@Param("id") String id);

	/**
	 * 保存客户群表
	 * @param custGroup 客户群表
	 */
	void save(@Param("et") CpCustGroup custGroup);

	/**
	 * 更新客户群表
	 * @param custGroup 客户群表
	 */
	void update(@Param("custGroup") CpCustGroup custGroup);

	/**
	 * 根据客户群表id删除客户群表
	 * @param id
	 */
	void deleteById(@Param("id") String id);
	/**
	 *	统计群聊数及客户数
	 * @param request 查询条件
	 * @return  CustGroupCountVO
	 */
	CustGroupCountVO count(@Param("et")CustGroupDTO request);

	/**
	 * 获取关联群的信息
	 * @param groupCodeDTO 查询条件
	 * @return  List<CustGroupVO>
	 */
	List<CpCustGroup> getSelectGroupList(@Param("et")GetSelectedGroupListDTO groupCodeDTO);
	/**
	 * 根据活动id查询所有关联的客户群
	 * @param codeId 活动id
	 * @return List<CustGroup>
	 */
	List<CpCustGroup> getCustGroupList(@Param("codeId")Long codeId);
	/**
	 * 根据群主员工id查询所有的群
	 * @param ownerId 群主员工id
	 * @return 关联的群的列表
	 */
    List<CpCustGroup> getCustGroupListByOwnerId(@Param("ownerId") Long ownerId);

    List<CpCustGroup> getListByChatIds(@Param("chatIds") List<String> chatIds);

	/**
	 * 离职分配，将分配的群的状态改为离职分配中
	 * @param ids 离职群id
	 */
    void updateBatchStatusToAssigning(@Param("ids")List<String> ids);

    List<SelectUserCountVO> selectCountByStaff(@Param("staffIds")List<Long> staffIds,@Param("startTime")String startTime,@Param("endTime")String endTime);

    List<SelectUserCountVO> selectCountUserByStaff(@Param("staffIds")List<Long> staffIds,@Param("startTime")String startTime,@Param("endTime")String endTime);

	List<CpCustGroup> getByIds(@Param("ids") List<String> ids);
}
