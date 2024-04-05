package com.mall4j.cloud.biz.service.cp;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mall4j.cloud.biz.dto.cp.CpStaffCodeLogDTO;
import com.mall4j.cloud.biz.dto.cp.ReqStaffCodeLogDTO;
import com.mall4j.cloud.biz.model.cp.CpErrorCodeLog;
import com.mall4j.cloud.biz.model.cp.CpStaffCodeLog;
import com.mall4j.cloud.biz.vo.cp.CpStaffCodeLogVO;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.vo.PageVO;

import java.util.List;
import java.util.Map;

/**
 * 
 *
 */
public interface CpErrorCodeService {

	Map<String,CpErrorCodeLog> listData(String errorcode);
}
