package com.mall4j.cloud.product.service;

import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.product.dto.SpuCommDTO;
import com.mall4j.cloud.product.dto.SpuCommPageDTO;
import com.mall4j.cloud.product.model.SpuComm;
import com.mall4j.cloud.product.vo.SpuCommStatisticsStarVO;
import com.mall4j.cloud.product.vo.SpuCommStatisticsVO;
import com.mall4j.cloud.product.vo.SpuCommVO;

import java.util.List;

/**
 * 商品评论
 *
 * @author YXF
 * @date 2021-01-11 13:47:33
 */
public interface SpuCommService {

	/**
	 * 分页获取商品评论列表
	 *
	 * @param pageDTO 分页参数
	 * @param request  查询条件
	 * @return 商品评论列表分页数据
	 */
	PageVO<SpuCommVO> page(PageDTO pageDTO, SpuCommPageDTO request);

	/**
	 * 分页获取商品评论列表
	 *
	 * @param pageDTO 分页参数
	 * @param shopId  店铺id
	 * @return 商品评论列表分页数据
	 */
	PageVO<SpuCommVO> page(PageDTO pageDTO, Long shopId);

	/**
	 * 根据商品评论id获取商品评论
	 *
	 * @param spuCommId 商品评论id
	 * @return 商品评论
	 */
	SpuCommVO getBySpuCommId(Long spuCommId);

	/**
	 * 更新商品评论
	 *
	 * @param spuComm 商品评论
	 */
	void update(SpuComm spuComm);

	/**
	 * 根据商品评论id删除商品评论
	 *
	 * @param spuCommId
	 */
	void deleteById(Long spuCommId);

	/**
	 * 根据商品，获取商品评论分页
	 *
	 * @param pageDTO
	 * @param spuId    商品id
	 * @param evaluate 评论类型  null/0 全部，1好评 2中评 3差评 4有图
	 * @return
	 */
    PageVO<SpuCommVO> spuCommPage(PageDTO pageDTO, Long spuId, Integer evaluate);

	/**
	 * 返回商品评论统计数据(好评率 好评数量 中评数 差评数 有图)
	 *
	 * @param spuId
	 * @return
	 */
	SpuCommStatisticsVO getProdCommDataBySpuId(Long spuId);

	/**
	 * 根据用户，获取商品评论分页
	 *
	 * @param pageDTO
	 * @param userId
	 * @return
	 */
	PageVO<SpuCommVO> spuCommPageByUserId(PageDTO pageDTO, Long userId);

	/**
	 * 根据orderItemId和userId, 获取商品评论信息
	 *
	 * @param orderItemId
	 * @param userId
	 * @return
	 */
	SpuCommVO getSpuCommByOrderItemId(Long orderItemId, Long userId);

	/**
	 * 添加商品评论
	 *
	 * @param spuCommDTO
	 */
    void comm(SpuCommDTO spuCommDTO);

	/**
	 * 统计商品好评数量
	 *
	 * @param spuId
	 * @return
	 */
	int countGoodReview(Long spuId);

	/**
	 * 返回商品评论数据(按星数划分数量)(商品)
	 * @param spuId
	 * @return
	 */
	SpuCommStatisticsStarVO getProdCommDataByStar(Long spuId);

	List<SpuCommVO> list(SpuCommPageDTO request);
}
