package com.mall4j.cloud.biz.service.cp;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mall4j.cloud.biz.dto.cp.StaffCodePlusDTO;
import com.mall4j.cloud.biz.model.cp.CpStaffCodeRef;

import java.util.List;


/**
 * 员工活码表
 *
 * @author hwy
 * @date 2022-01-24 11:05:42
 */
public interface StaffCodeRefService extends IService<CpStaffCodeRef> {

	/**
	 * 根据活码id 删除
	 * @param codeId 员工活码表id
	 */
	void deleteByCodeId(Long codeId);

	/**
	 * 根据活码id查询关联的员工
	 * @param codeId
	 * @return
	 */
	List<CpStaffCodeRef> listByCodeId(Long codeId);

	void saveCodeRef(Long sourceId, Integer sourceFrom, StaffCodePlusDTO request);

	List<CpStaffCodeRef> getCodeStaffRefs(StaffCodePlusDTO request);
}
