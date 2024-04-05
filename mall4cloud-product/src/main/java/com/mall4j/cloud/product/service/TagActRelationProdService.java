package com.mall4j.cloud.product.service;

import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.product.dto.TagActivityPageDTO;
import com.mall4j.cloud.product.model.TagActRelationProd;
import com.mall4j.cloud.product.model.TagActivity;
import com.mall4j.cloud.product.vo.TagActivityVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 营销标签活动关联的商品表
 * @author hwy
 * @date 2022-03-12 14:28:10
 */
public interface TagActRelationProdService {

	/**
	 * 根据标签名称分页获取营销标签活动关联的商品表列表
	 * @param actId  活动id
	 * @return 营销标签活动关联的商品表列表分页数据
	 */
	List<TagActRelationProd> listByActId(@Param("actId") Long actId);

	List<TagActRelationProd> listsByActId(@Param("actId") Long actId);


	/**
	 * 保存营销标签活动关联的商品表
	 * @param tagActRelationProd 营销标签活动关联的商品表
	 */
	void save(@Param("et") TagActRelationProd tagActRelationProd);

	/**
	 * 根据营销标签活动关联的商品表id删除营销标签活动关联的商品表
	 * @param actId 活动id
	 * @param spuId 商品id
	 */
	void deleteById(@Param("actId") Long actId,@Param("spuId") Long spuId);

	/**
	 * 根据活动id删除
	 * @param actId 活动id
	 */
	void deleteByActId(@Param("actId") Long actId);
}
