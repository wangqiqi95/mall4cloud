package com.mall4j.cloud.seckill.mapper;

import cn.hutool.core.date.DateTime;
import com.mall4j.cloud.common.database.util.PageAdapter;
import com.mall4j.cloud.common.product.dto.ProductSearchDTO;
import com.mall4j.cloud.common.product.vo.SekillActivitySpuVO;
import com.mall4j.cloud.common.product.vo.search.SpuAdminVO;
import com.mall4j.cloud.seckill.dto.SeckillDTO;
import com.mall4j.cloud.seckill.model.Seckill;
import com.mall4j.cloud.seckill.vo.SeckillAdminVO;
import com.mall4j.cloud.seckill.vo.SeckillSpuVO;
import com.mall4j.cloud.seckill.vo.SeckillVO;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * 秒杀信息
 *
 * @author lhd
 * @date 2021-03-30 14:53:09
 */
public interface SeckillMapper {

    /**
     * 获取秒杀信息列表
     *
     * @param seckillDTO
     * @return 秒杀信息列表
     */
    List<SeckillVO> list(@Param("seckill") SeckillDTO seckillDTO);

    /**
     * 根据秒杀信息id获取秒杀信息
     *
     * @param seckillId 秒杀信息id
     * @return 秒杀信息
     */
    SeckillVO getBySeckillId(@Param("seckillId") Long seckillId);

    /**
     * 保存秒杀信息
     *
     * @param seckill 秒杀信息
     */
    void save(@Param("seckill") SeckillVO seckill);

    /**
     * 更新秒杀信息
     *
     * @param seckill 秒杀信息
     */
    void update(@Param("seckill") SeckillVO seckill);

    /**
     * 根据秒杀信息id删除秒杀信息
     *
     * @param seckillId
     */
    void deleteById(@Param("seckillId") Long seckillId);

    /**
     * 根据秒杀部分条件获取秒杀商品数量
     *
     * @param seckillDTO
     * @return
     */
    Long countBySeckillConditions(@Param("seckill") SeckillDTO seckillDTO);

    /**
     * 根据商品id获取秒杀活动信息
     *
     * @param spuId
     * @return
     */
    SeckillVO getBySpuId(@Param("spuId") Long spuId);

    /**
     * 更新秒杀活动剩余库存
     *
     * @param seckillId
     * @param prodCount
     * @return 返回结果
     */
    int updateStocksById(@Param("seckillId") Long seckillId, @Param("prodCount") Integer prodCount);

    /**
     * 根据商品id获取秒杀商品信息
     *
     * @param selectedLot 所选批次
     * @param spuIds      商品ids
     * @return 秒杀商品信息
     */
    List<SekillActivitySpuVO> listBySelectLotAndSpuIds(@Param("selectedLot") Integer selectedLot, @Param("spuIds") List<Long> spuIds);

    /**
     * 获取首页的秒杀信息
     *
     * @param seckillDTO 筛选信息
     * @return 秒杀信息
     */
    List<SeckillVO> listBySelectLot(@Param("seckill") SeckillDTO seckillDTO);

    /**
     * 根据店铺id获取所有的秒杀时段信息
     *
     * @param shopId   店铺id
     * @param specDate 筛选时间
     * @param isClosed 筛选是否活动结束状态 0.未开始，1.已结束 ,2.正在进行中
     * @return 秒杀时段信息
     */
    List<SeckillAdminVO> listByShopId(@Param("shopId") Long shopId, @Param("specDate") String specDate, @Param("isClosed") Integer isClosed);

    /**
     * 删除所有的秒杀活动(逻辑删除)
     *
     * @param seckillList 需要删除的列表
     */
    void updateListToDelete(@Param("seckillList") List<SeckillVO> seckillList);

    /**
     * 查询根据时段的所有秒杀信息
     *
     * @param selectedLots 时段信息
     * @return 秒杀信息
     */
    List<SeckillVO> listBySelectLotList(@Param("selectedLots") List<Integer> selectedLots);

    /**
     * 通过商品ids获取秒杀信息
     *
     * @param productSearchDTO 时段
     * @param startTime        开始时间
     * @return 秒杀信息
     */
    List<SeckillSpuVO> listByStartTimeAndSelectedLot(@Param("productSearchDTO") ProductSearchDTO productSearchDTO, @Param("startTime") DateTime startTime);

    /**
     * 还原秒杀库存
     *
     * @param orderId 订单id
     */
    void returnStocksByOrderId(@Param("orderId") Long orderId);

    /**
     * 获取应该结束但是没有结束的秒杀列表
     *
     * @return 应该结束但是没有结束的秒杀列表
     */
    List<Seckill> listUnEndButNeedEndActivity();

    /**
     * 更新秒杀活动状态
     *
     * @param seckillList
     */
    void changeSeckillActivityStatusBySeckillIdList(@Param("seckillList") List<Seckill> seckillList);

    /**
     * 获取正在参与秒杀活动的商家数量
     *
     * @return 数量
     */
    Integer getJoinSeckillMerchantNum();

    /**
     * 根据商品ids下线所有的秒杀活动
     *
     * @param spuIds 商品ids
     */
    void offlineSeckillBySpuIds(@Param("spuIds") List<Long> spuIds);

    /**
     * 商品信息列表
     *
     * @param pageAdapter 分页参数
     * @param startTime   开始时间
     * @param prodSearch  搜索参数
     * @return 参与秒杀的商品id集合
     */
    List<SpuAdminVO> listBySearchParam(@Param("page") PageAdapter pageAdapter, @Param("startTime") Date startTime, @Param("prodSearch") ProductSearchDTO prodSearch);

    /**
     * listBySearchParam的统计总数
     *
     * @param startTime  开始时间
     * @param prodSearch 搜索参数
     * @return
     */
    int countListBySearchParam(@Param("startTime") Date startTime, @Param("prodSearch") ProductSearchDTO prodSearch);

    /**
     * 根据店铺id，获取秒杀商品id
     *
     * @param shopId 店铺id
     * @return 秒杀商品id列表
     */
    List<Long> listSpuIdIdByShopId(@Param("shopId") Long shopId);

    SeckillVO getBySeckillIdAndStoreId(@Param("seckillId") Long seckillId, @Param("storeId") Long storeId);
}
