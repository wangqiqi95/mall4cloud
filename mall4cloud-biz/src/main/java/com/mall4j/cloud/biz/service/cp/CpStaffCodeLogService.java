package com.mall4j.cloud.biz.service.cp;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mall4j.cloud.biz.dto.cp.CpStaffCodeLogDTO;
import com.mall4j.cloud.biz.dto.cp.ReqStaffCodeLogDTO;
import com.mall4j.cloud.biz.vo.cp.CpStaffCodeLogVO;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.biz.model.cp.CpStaffCodeLog;

import java.util.List;

/**
 * 
 *
 */
public interface CpStaffCodeLogService extends IService<CpStaffCodeLog> {

	/**
	 * 分页获取列表
	 * @param pageDTO 分页参数
	 * @return 列表分页数据
	 */
	PageVO<CpStaffCodeLogVO> listPage(PageDTO pageDTO, ReqStaffCodeLogDTO reqStaffCodeLogDTO);

	List<CpStaffCodeLogVO> listData(ReqStaffCodeLogDTO reqStaffCodeLogDTO);

	void soldStaffCodeLog(ReqStaffCodeLogDTO reqStaffCodeLogDTO,Long downHistoryId);

	void saveErrorTo(List<CpStaffCodeLogDTO> logDTOList);

	void rCreateCode(List<Long> staffIds);

	void staffCodeErrorCreate();

	/**
	 * 根据id删除
	 * @param id id
	 */
	void deleteById(Long id);

	void deleteByStaffIds(List<Long> staffIds);
}
