package com.mall4j.cloud.biz.service.cp;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mall4j.cloud.biz.dto.cp.GroupCodeDTO;
import com.mall4j.cloud.biz.dto.cp.GroupCodePageDTO;
import com.mall4j.cloud.biz.model.cp.CpGroupCode;
import com.mall4j.cloud.biz.vo.cp.GroupCodeVO;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.vo.PageVO;

/**
 * 群活码表
 *
 * @author hwy
 * @date 2022-02-16 15:17:19
 */
public interface GroupCodeService extends IService<CpGroupCode> {

	/**
	 * 分页获取群活码表列表
	 * @param pageDTO 分页参数
	 * @return 群活码表列表分页数据
	 */
	PageVO<GroupCodeVO> page(PageDTO pageDTO, GroupCodePageDTO request);

	/**
	 * 根据群活码表id获取群活码表
	 *
	 * @param id 群活码表id
	 * @return 群活码表
	 */
	CpGroupCode getById(Long id);

	GroupCodeVO getDetailById(Long id);

	/**
	 * 根据群活码表id删除群活码表
	 * @param id 群活码表id
	 */
	void deleteById(Long id);

	/**
	 * 添加群活码信息及关联群
	 * @param groupCode
	 */
    Long createOrUpdateGroupCode(GroupCodeDTO groupCode);

}
