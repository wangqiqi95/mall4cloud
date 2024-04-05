package com.mall4j.cloud.biz.service.cp;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mall4j.cloud.biz.dto.cp.analyze.CpTagGroupCodeAnalyzeDTO;
import com.mall4j.cloud.biz.dto.cp.analyze.CpTagGroupCodeUserRefDTO;
import com.mall4j.cloud.biz.model.cp.CpTaskUserRef;
import com.mall4j.cloud.biz.vo.cp.analyze.CpTagGroupCodeSendUserVO;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.vo.PageVO;

import java.util.List;

/**
 * 群发任务客户关联表
 *
 * @author gmq
 * @date 2023-10-30 14:23:04
 */
public interface CpTaskUserRefService extends IService<CpTaskUserRef> {

	/**
	 * 标签建群-发送好友列表【移动端】
	 * @param pageDTO 分页参数
	 * @return 列表分页数据
	 */
	PageVO<CpTagGroupCodeSendUserVO> pageTagCodeRelUser(CpTagGroupCodeUserRefDTO dto);

	List<CpTaskUserRef> getListByTaskId(Long taskId);

	/**
	 * 根据群发任务客户关联表id获取群发任务客户关联表
	 *
	 * @param id 群发任务客户关联表id
	 * @return 群发任务客户关联表
	 */
	CpTaskUserRef getById(Long id);

	/**
	 * 根据群发任务客户关联表id删除群发任务客户关联表
	 * @param id 群发任务客户关联表id
	 */
	void deleteByTaskId(Long taskId);
}
