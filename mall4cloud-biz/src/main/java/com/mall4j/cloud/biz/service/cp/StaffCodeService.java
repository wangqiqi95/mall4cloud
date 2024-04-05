package com.mall4j.cloud.biz.service.cp;

import com.mall4j.cloud.api.biz.dto.cp.StaffCodeCreateDTO;
import com.mall4j.cloud.biz.dto.cp.AttachmentExtDTO;
import com.mall4j.cloud.biz.dto.cp.StaffCodePageDTO;
import com.mall4j.cloud.biz.dto.cp.StaffCodeRefDTO;
import com.mall4j.cloud.biz.model.cp.StaffCode;
import com.mall4j.cloud.biz.vo.cp.StaffCodeVO;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.vo.PageVO;

import java.util.List;


/**
 * 员工活码表
 *
 * @author hwy
 * @date 2022-01-24 11:05:42
 */
public interface StaffCodeService {

	/**
	 * 分页获取员工活码表列表
	 * @param pageDTO 分页参数
	 * @param  request 查询条件
	 * @return 员工活码表列表分页数据
	 */
	PageVO<StaffCodeVO> page(PageDTO pageDTO, StaffCodePageDTO request);

	/**
	 * 根据员工活码表id获取员工活码表
	 *
	 * @param id 员工活码表id
	 * @return 员工活码表
	 */
	StaffCode getById(Long id);

	/**
	 * 保存员工活码表
	 * @param staffCode 员工活码表
	 */
	void save(StaffCode staffCode, AttachmentExtDTO attachMent, List<StaffCodeRefDTO> staffList);

	/**
	 * 更新员工活码表
	 * @param staffCode 员工活码表
	 */
	void update(StaffCode staffCode, AttachmentExtDTO attachMent, List<StaffCodeRefDTO> staffList);

	/**
	 *外部触发生成员工活动（员工批量导入、同步中台员工）
	 */
	void syncStaffCodeSUP(StaffCodeCreateDTO staffCodeCreateDTO);

	/**
	 * 根据员工活码表id删除员工活码表
	 * @param id 员工活码表id
	 */
	void deleteById(Long id);

	/**
	 * 查询配置活码信息
	 * @param id
	 * @param state
	 * @return
	 */
    StaffCode selectByStaffIdAndState(Long id, String state);


	/**
	 * 查询配置活码信息
	 * @param staffId
	 * @return
	 */
	List<StaffCode> selectByStaffId(Long staffId);

}
