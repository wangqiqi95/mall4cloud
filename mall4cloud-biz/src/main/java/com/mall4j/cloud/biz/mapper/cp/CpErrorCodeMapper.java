package com.mall4j.cloud.biz.mapper.cp;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mall4j.cloud.biz.dto.cp.ReqStaffCodeLogDTO;
import com.mall4j.cloud.biz.model.cp.CpErrorCodeLog;
import com.mall4j.cloud.biz.model.cp.CpStaffCodeLog;
import com.mall4j.cloud.biz.vo.cp.CpStaffCodeLogVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 
 *
 */
public interface CpErrorCodeMapper extends BaseMapper<CpErrorCodeLog> {

	/**
	 * 获取列表
	 * @return 列表
	 */
	List<CpErrorCodeLog> list();

}
