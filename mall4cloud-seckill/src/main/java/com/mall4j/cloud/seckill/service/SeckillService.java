package com.mall4j.cloud.seckill.service;

import com.mall4j.cloud.api.platform.dto.OfflineHandleEventDTO;
import com.mall4j.cloud.api.platform.vo.OfflineHandleEventVO;
import com.mall4j.cloud.api.platform.vo.SysConfigApiVO;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.product.dto.ProductSearchDTO;
import com.mall4j.cloud.common.product.vo.SekillActivitySpuVO;
import com.mall4j.cloud.seckill.constant.SeckillStatusEnum;
import com.mall4j.cloud.seckill.dto.SeckillDTO;
import com.mall4j.cloud.seckill.dto.SeckillSkuDTO;
import com.mall4j.cloud.seckill.model.Seckill;
import com.mall4j.cloud.seckill.vo.AppSeckillVO;
import com.mall4j.cloud.seckill.vo.SeckillAdminVO;
import com.mall4j.cloud.seckill.vo.SeckillSpuVO;
import com.mall4j.cloud.seckill.vo.SeckillVO;

import java.util.List;

/**
 * 秒杀信息
 *
 * @author lhd
 * @date 2021-03-30 14:53:09
 */
public interface SeckillService {

    /**
     * 分页获取秒杀信息列表
     * @param pageDTO 分页参数
     * @param seckillDTO
     * @return 秒杀信息列表分页数据
     */
    PageVO<SeckillVO> page(PageDTO pageDTO, SeckillDTO seckillDTO);

    /**
     * 根据秒杀信息id获取秒杀信息
     *
     * @param seckillId 秒杀信息id
     * @return 秒杀信息
     */
    SeckillVO getBySeckillId(Long seckillId);

    /**
     * 保存秒杀信息
     * @param seckill 秒杀信息
     * @param seckillSkuList
     */
    void saveSeckillAndItems(SeckillVO seckill, List<SeckillSkuDTO> seckillSkuList);

    /**
     * 根据秒杀信息id删除秒杀信息
     * @param seckillId
     */
    void deleteById(Long seckillId);

    /**
     * 清除秒杀信息缓存
     * @param seckillId 秒杀id
     */
    void removeSeckillCacheById(Long seckillId);

    /**
     * 更改活动的状态
     * @param groupActivityId
     * @param status
     */
    void updateStatus(Long groupActivityId, SeckillStatusEnum status);

    /**
     * 失效活动
     * @param seckillId 秒杀id
     * @param spuId 商品id
     */
    void invalidById(Long seckillId,Long spuId);

    /**
     * 修改秒杀信息
     * @param seckill 秒杀信息
     */
    void updateById(SeckillVO seckill);

    /**
     * 获取下线的事件记录
     * @param seckillActivityId
     * @return
     */
    OfflineHandleEventVO getOfflineHandleEvent(Long seckillActivityId);

    /**
     * 平台审核商家提交的申请
     * @param offlineHandleEventDto
     */
    void audit(OfflineHandleEventDTO offlineHandleEventDto);

    /**
     * 违规活动提交审核
     * @param offlineHandleEventDto
     */
    void auditApply(OfflineHandleEventDTO offlineHandleEventDto);

    /**
     * 返回时间段获取秒杀信息分页列表
     * @param seckillDTO 秒杀信息
     * @return 秒杀信息分页列表
     */
    AppSeckillVO pageBySelectLot(SeckillDTO seckillDTO);

    /**
     * 根据商品id获取秒杀活动信息
     * @param spuId
     * @return
     */
    SeckillVO getBySpuId(Long spuId);

    /**
     * 平台下架活动
     * @param offlineHandleEventDto
     * @param spuId
     */
    void offline(OfflineHandleEventDTO offlineHandleEventDto, Long spuId);

    /**
     * 根据商品id获取秒杀商品信息
     * @param selectedLot 所选批次
     * @param spuIds 商品ids
     * @return 秒杀商品信息
     */
    List<SekillActivitySpuVO> listBySelectLotAndSpuIds(Integer selectedLot, List<Long> spuIds);

    /**
     * 根据店铺id获取所有的秒杀时段信息
     *
     * @param pageDTO
     * @param shopId
     * @param type
     * @param specTime
     * @return 秒杀时段信息
     */
    PageVO<SeckillAdminVO> listByShopId(PageDTO pageDTO, Long shopId, Integer type, Long specTime);

    /**
     * 保存秒杀时段信息
     * @param config 秒杀时段信息
     */
    void saveSeckillTime(SysConfigApiVO config);

    /**
     * 根据时间返回秒杀商品信息列表
     * @param pageDTO
     * @param startTime 活动时间
     * @param productSearch 商品搜索信息
     * @return
     */
    PageVO<SeckillSpuVO> listSeckillSpuByTime(PageDTO pageDTO, Long startTime, ProductSearchDTO productSearch);

    /**
     * 获取应该结束但是没有结束的秒杀列表
     * @return 应该结束但是没有结束的秒杀列表
     */
    List<Seckill> listUnEndButNeedEndActivity();

    /**
     * 改变商品类型，结束正在进行的秒杀信息
     * @param seckillList 秒杀活动列表
     */
    void changeProdTypeBySeckillIdList(List<Seckill> seckillList);

    /**
     * 显示已经结束的秒杀信息列表,specTime时间搜索
     *
     * @param pageDTO
     * @param shopId
     * @param specTime
     * @return
     */
    PageVO<SeckillAdminVO> listEndSeckillByShopId(PageDTO pageDTO, Long shopId, Long specTime);

    /**
     * 获取正在参与秒杀活动的商家数量
     * @return 数量
     */
    Integer getJoinSeckillMerchantNum();

    /**
     * 根据商品ids下线所有的秒杀活动
     * @param spuIds 商品ids
     */
    void offlineSeckillBySpuIds(List<Long> spuIds);

    /**
     * 根据店铺id下线所有的秒杀活动
     * @param shopId 店铺id
     */
    void offlineSeckillByShopId(Long shopId);

    SeckillVO getBySeckillIdAndStoreId(Long seckillId, Long storeId);
}
