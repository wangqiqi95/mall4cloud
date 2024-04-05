package com.mall4j.cloud.biz.service;

import com.mall4j.cloud.biz.model.AttachFileGroup;
import com.mall4j.cloud.biz.vo.AttachFileGroupVO;

import java.util.List;

/**
 *
 *
 * @author YXF
 * @date 2020-12-04 16:15:02
 */
public interface AttachFileGroupService {

	/**
	 * 获取列表
	 * @param type
	 * @return
	 */
	List<AttachFileGroupVO> list(Integer type);

	/**
	 * 根据id获取
	 *
	 * @param attachFileGroupId id
	 * @return
	 */
	AttachFileGroupVO getByAttachFileGroupId(Long attachFileGroupId);

	/**
	 * 保存
	 * @param attachFileGroup
	 */
	void save(AttachFileGroup attachFileGroup);

	/**
	 * 更新
	 * @param attachFileGroup
	 */
	void update(AttachFileGroup attachFileGroup);

	/**
	 * 根据id删除
	 * @param attachFileGroupId
	 */
	void deleteById(Long attachFileGroupId);

	/**
	 * 根据uid更新店铺id
	 * @param shopId
	 * @param uid
	 */
	void updateShopIdByUid(Long shopId, Long uid);
}
