package com.mall4j.cloud.biz.service.cp;


import com.baomidou.mybatisplus.extension.service.IService;
import com.mall4j.cloud.biz.model.cp.CpMediaRef;

import java.util.List;

/**
 * 
 *
 * @author gmq
 * @date 2023-11-27 11:29:05
 */
public interface CpMediaRefService extends IService<CpMediaRef> {

	/**
	 * 根据id获取
	 * @return
	 */
	CpMediaRef getById(Integer sourceFrom,String sourceId);

	/**
	 * 保存
	 * @param cpMediaRef 
	 */
	void saveCpMediaRef(CpMediaRef cpMediaRef);

	void refreshMediaId();

}
