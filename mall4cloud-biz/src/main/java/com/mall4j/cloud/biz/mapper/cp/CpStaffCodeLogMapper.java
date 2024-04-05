package com.mall4j.cloud.biz.mapper.cp;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mall4j.cloud.biz.dto.cp.ReqStaffCodeLogDTO;
import com.mall4j.cloud.biz.model.cp.CpStaffCodeLog;
import com.mall4j.cloud.biz.vo.cp.CpStaffCodeLogVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 
 *
 */
public interface CpStaffCodeLogMapper extends BaseMapper<CpStaffCodeLog> {

	/**
	 * 获取列表
	 * @return 列表
	 */
	List<CpStaffCodeLogVO> listPage(@Param("logDto") ReqStaffCodeLogDTO reqStaffCodeLogDTO);

}
