package com.mall4j.cloud.biz.service.cp;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mall4j.cloud.biz.dto.cp.StaffCodePlusDTO;
import com.mall4j.cloud.biz.dto.cp.StaffCodePagePlusDTO;
import com.mall4j.cloud.biz.model.cp.CpStaffCodePlus;
import com.mall4j.cloud.biz.vo.cp.StaffCodeDetailPlusVO;
import com.mall4j.cloud.biz.vo.cp.StaffCodePlusVO;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.vo.PageVO;

import java.util.List;


/**
 * 员工活码表
 *
 * @author hwy
 * @date 2022-01-24 11:05:42
 */
public interface StaffCodePlusService extends IService<CpStaffCodePlus> {

	/**
	 * 分页获取员工活码表列表
	 * @param pageDTO 分页参数
	 * @param  request 查询条件
	 * @return 员工活码表列表分页数据
	 */
	PageVO<StaffCodePlusVO> page(PageDTO pageDTO, StaffCodePagePlusDTO request);

	/**
	 * 根据员工活码表id获取员工活码表
	 *
	 * @param id 员工活码表id
	 * @return 员工活码表
	 */
	CpStaffCodePlus getById(Long id);

	StaffCodeDetailPlusVO getDetailById(Long id);

	/**
	 * 保存员工活码表
	 * @param staffCode 员工活码表
	 */
	void save(StaffCodePlusDTO request);

	/**
	 * 更新员工活码表
	 * @param staffCode 员工活码表
	 */
	void update(StaffCodePlusDTO request);


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
    CpStaffCodePlus selectByStaffIdAndState(Long id, String state);


	/**
	 * 查询配置活码信息
	 * @param staffId
	 * @return
	 */
	List<CpStaffCodePlus> selectByStaffId(Long staffId);

}
