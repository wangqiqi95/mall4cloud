package com.mall4j.cloud.biz.service.cp;


import com.baomidou.mybatisplus.extension.service.IService;
import com.mall4j.cloud.biz.model.cp.CpAutoGroupCodeUser;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.vo.PageVO;

/**
 * 自动拉群员工好友关系表
 *
 * @author gmq
 * @date 2023-11-08 10:20:46
 */
public interface CpAutoGroupCodeUserService extends IService<CpAutoGroupCodeUser> {

	/**
	 * 分页获取自动拉群员工好友关系表列表
	 * @param pageDTO 分页参数
	 * @return 自动拉群员工好友关系表列表分页数据
	 */
	PageVO<CpAutoGroupCodeUser> page(PageDTO pageDTO);

	/**
	 * 根据自动拉群员工好友关系表id获取自动拉群员工好友关系表
	 *
	 * @param id 自动拉群员工好友关系表id
	 * @return 自动拉群员工好友关系表
	 */
	CpAutoGroupCodeUser getById(Long id);

	/**
	 * 根据自动拉群员工好友关系表id删除自动拉群员工好友关系表
	 * @param id 自动拉群员工好友关系表id
	 */
	void deleteById(Long id);

	/**
	 * 获取已邀请人数
	 * @param codeId
	 * @return
	 */
	Integer countSendByCodeId(Long codeId);

	/**
	 * 获取已入群人数
	 * @param codeId
	 * @return
	 */
	Integer countJoinGroupByCodeId(Long codeId);
}
