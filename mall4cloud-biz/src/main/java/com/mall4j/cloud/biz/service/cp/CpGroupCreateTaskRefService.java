package com.mall4j.cloud.biz.service.cp;


import com.baomidou.mybatisplus.extension.service.IService;
import com.mall4j.cloud.biz.model.cp.CpGroupCreateTaskRef;

import java.util.List;

/**
 * 
 *
 * @author gmq
 * @date 2024-01-19 15:49:54
 */
public interface CpGroupCreateTaskRefService extends IService<CpGroupCreateTaskRef> {

	List<CpGroupCreateTaskRef> listByGroupId(String groupId);


	void saveTo(String groupId,Integer scope,List<String> tagIds);

	void deleteByGroupId(String groupId);
}
