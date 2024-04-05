package com.mall4j.cloud.biz.service.cp;

import com.mall4j.cloud.biz.wx.cp.constant.OriginEumn;
import com.mall4j.cloud.biz.model.cp.MaterialMsg;

import java.util.List;

/**
 * 素材消息表
 *
 * @author hwy
 * @date 2022-01-25 20:33:45
 */
public interface MaterialMsgService {



	/**
	 * 保存素材消息表
	 * @param materialMsg 素材消息表
	 */
	void save(MaterialMsg materialMsg);


	/**
	 * 根据素材id删除
	 * @param matId 素材id
	 */
	void deleteByMatId(Long matId , OriginEumn originEumn);

	/**
	 * 根据素材id查询列表
	 * @param matId 素材id
	 * @return List<MaterialMsg>
	 */
    List<MaterialMsg> listByMatId(Long matId,OriginEumn originEumn);

	List<MaterialMsg> waitRefreshMediaIdList();

	void refreshMediaId(Long id,String mediaId);
}
