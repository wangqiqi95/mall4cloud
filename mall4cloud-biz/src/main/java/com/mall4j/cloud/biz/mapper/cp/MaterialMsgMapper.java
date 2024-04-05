package com.mall4j.cloud.biz.mapper.cp;

import com.mall4j.cloud.biz.model.cp.MaterialMsg;
import org.apache.ibatis.annotations.Param;

import java.util.List;


/**
 * 素材消息表
 *
 * @author hwy
 * @date 2022-01-25 20:33:45
 */
public interface MaterialMsgMapper {

	/**
	 * 保存素材消息表
	 * @param materialMsg 素材消息表
	 */
	void save(@Param("materialMsg") MaterialMsg materialMsg);

	/**deleteByMatId
	 * 根据素材id删除
	 * @param matId 素材id
	 */
    void deleteByMatId(@Param("matId")Long matId,@Param("origin")Integer origin);

	/**
	 * 更新
	 * @param id
	 * @param mediaId
	 */
	void refreshMediaId(@Param("id")Long id,@Param("mediaId")String mediaId);

	/**
	 * 根据素材id查询
	 * @param matId 素材id
	 * @return List<MaterialMsg>
	 */
	List<MaterialMsg> listByMatId(@Param("matId")Long matId,@Param("origin")Integer origin);

	/**
	 * 查询待刷新资源列表
	 * @return
	 */
	List<MaterialMsg> waitRefreshMediaIdList();
}
