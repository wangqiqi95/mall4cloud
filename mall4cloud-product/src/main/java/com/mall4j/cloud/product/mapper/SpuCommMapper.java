package com.mall4j.cloud.product.mapper;

import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.product.dto.SpuCommPageDTO;
import com.mall4j.cloud.product.model.SpuComm;
import com.mall4j.cloud.product.vo.SpuCommStatisticsStarVO;
import com.mall4j.cloud.product.vo.SpuCommStatisticsVO;
import com.mall4j.cloud.product.vo.SpuCommVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 商品评论
 *
 * @author YXF
 * @date 2021-01-11 13:47:33
 */
public interface SpuCommMapper {
	/**
	 * 获取商品评论列表
	 *
	 * @param request 查询条件
	 * @return 商品评论列表
	 */
	List<SpuCommVO> pageList(@Param("et") SpuCommPageDTO request);

	/**
	 * 获取商品评论列表
	 *
	 * @param shopId 店铺id
	 * @return 商品评论列表
	 */
	List<SpuCommVO> list(@Param("shopId") Long shopId);

	/**
	 * 根据商品评论id获取商品评论
	 *
	 * @param spuCommId 商品评论id
	 * @return 商品评论
	 */
	SpuCommVO getBySpuCommId(@Param("spuCommId") Long spuCommId);

	/**
	 * 保存商品评论
	 *
	 * @param spuComm 商品评论
	 */
	void save(@Param("spuComm") SpuComm spuComm);

	/**
	 * 更新商品评论
	 *
	 * @param spuComm 商品评论
	 */
	void update(@Param("spuComm") SpuComm spuComm);

	/**
	 * 根据商品评论id删除商品评论
	 *
	 * @param spuCommId
	 */
	void deleteById(@Param("spuCommId") Long spuCommId);

	/**
	 * 根据商品，获取商品评论分页
	 *
	 * @param pageDTO
	 * @param spuId    商品id
	 * @param evaluate 评论类型  null/0 全部，1好评 2中评 3差评 4有图
	 * @return
	 */
	List<SpuCommVO> spuCommPage(@Param("pageDTO") PageDTO pageDTO, @Param("spuId") Long spuId, @Param("evaluate") Integer evaluate);

	/**
	 * 返回商品评论统计数据(好评数量 中评数 差评数 有图)
	 *
	 * @param spuId
	 * @return
	 */
	SpuCommStatisticsVO getProdCommDataBySpuId(@Param("spuId") Long spuId);

	/**
	 * 返回商品评论数据(按星数划分数量)(商品)
	 * @param spuId
	 * @return
	 */
	SpuCommStatisticsStarVO getProdCommDataByStar(@Param("spuId") Long spuId);

	/**
	 * 获取用户评论列表
	 *
	 * @param userId
	 * @return
	 */
	List<SpuCommVO> spuCommListByUserId(Long userId);

	/**
	 * 根据orderItemId和userId, 获取商品评论信息
	 *
	 * @param orderItemId
	 * @param userId
	 * @return
	 */
	SpuCommVO getSpuCommByOrderItemId(@Param("orderItemId") Long orderItemId, @Param("userId") Long userId);

	/**
	 * 统计商品好评数量
	 *
	 * @param spuId
	 * @return
	 */
    int countGoodReview(@Param("spuId") Long spuId);
}
