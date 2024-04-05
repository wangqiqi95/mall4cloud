package com.mall4j.cloud.distribution.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.stream.CollectorUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.mall4j.cloud.api.distribution.dto.DistributionCommissionRateDTO;
import com.mall4j.cloud.api.distribution.vo.DistributionCommissionRateVO;
import com.mall4j.cloud.api.product.feign.ProductFeignClient;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.util.PageUtil;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.exception.LuckException;
import com.mall4j.cloud.common.product.dto.ProductSearchDTO;
import com.mall4j.cloud.common.product.vo.search.SpuCommonVO;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.distribution.constant.ActivityStatusEnum;
import com.mall4j.cloud.distribution.dto.DistributionRecommendActivityDTO;
import com.mall4j.cloud.distribution.dto.DistributionRecommendActivityQueryDTO;
import com.mall4j.cloud.distribution.dto.DistributionRecommendSpuQueryDTO;
import com.mall4j.cloud.distribution.enums.ActivityStatusEnums;
import com.mall4j.cloud.distribution.mapper.DistributionRecommendActivityMapper;
import com.mall4j.cloud.distribution.model.DistributionRecommendActivity;
import com.mall4j.cloud.distribution.service.DistributionCommissionService;
import com.mall4j.cloud.distribution.service.DistributionRecommendActivityService;
import com.mall4j.cloud.distribution.vo.DistributionRecommendActivityDetailVO;
import com.mall4j.cloud.distribution.vo.DistributionRecommendActivityVO;
import com.mall4j.cloud.distribution.vo.DistributionRecommendSpuListVO;
import com.mall4j.cloud.distribution.vo.DistributionRecommendSpuVO;
import com.sun.org.apache.bcel.internal.generic.I2F;
import lombok.extern.slf4j.Slf4j;
import ma.glasnost.orika.MapperFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 主推商品活动服务接口-实现类
 *
 * @author EricJeppesen
 * @date 2022/10/18 14:55
 */
@Service
@Slf4j
public class DistributionRecommendActivityServiceImpl implements DistributionRecommendActivityService {

    private DistributionRecommendActivityMapper distributionRecommendActivityMapper;

    @Autowired
    public void setDistributionRecommendActivityMapper(DistributionRecommendActivityMapper distributionRecommendActivityMapper) {
        this.distributionRecommendActivityMapper = distributionRecommendActivityMapper;
    }

    private MapperFacade mapperFacade;

    @Autowired
    public void setMapperFacade(MapperFacade mapperFacade) {
        this.mapperFacade = mapperFacade;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long createActivity(DistributionRecommendActivityDTO recommendActivityDTO, Long creatorId, String creatorName) {
        // 对象转换-并且防腐
        DistributionRecommendActivity entity = this.createDataFacade(recommendActivityDTO, creatorId, creatorName);
        // 处理活动主体
        Integer effectiveDatasource = distributionRecommendActivityMapper.create(entity);
        Long activityId = entity.getId();
        if (effectiveDatasource < 0 || activityId == null) {
            throw new LuckException("主推商品活动主体，插入数据库异常");
        }

        Integer applicableForAllStores = entity.getApplicableForAllStores();
        if (applicableForAllStores != 1) {
            // != 1 部分商品参与活动
            // 处理参与活动的门店
            List<Long> shopIds = recommendActivityDTO.getShopIds();
            Integer shopRelatesEffectiveRow = distributionRecommendActivityMapper.createShopRelates(activityId, shopIds);

            // 放入主表缓存中
            entity.setApplicableStoresAmount(shopRelatesEffectiveRow);
        }

        Integer applicableForAllGoods = entity.getApplicableForAllGoods();
        if (applicableForAllGoods != 1) {
            // != 1 部分商品参与
            // 处理参与活动的商品
            List<Long> spuIds = recommendActivityDTO.getSpuIds();
            Integer shopRelatesEffectiveRow = distributionRecommendActivityMapper.createSpuRelates(activityId, spuIds);

            // 放入主表缓存中
            entity.setActivityGoodsAmount(shopRelatesEffectiveRow);
        }

        // 处理活动主表缓存
        this.notifyCache(activityId);
        return entity.getId();
    }

    /**
     * 创建数据时的数据防腐
     *
     * @param recommendActivityDTO 外部数据来源
     * @param creatorId            数据创建人（标识）
     * @param creatorName          数据创建人（名称）
     * @return {@link com.mall4j.cloud.distribution.model.DistributionRecommendActivity} 本地数据库领域
     */
    private DistributionRecommendActivity createDataFacade(DistributionRecommendActivityDTO recommendActivityDTO, Long creatorId, String creatorName) {
        // 调用通用防腐
        this.commonFacade(recommendActivityDTO);

        // orika 实体转换
        DistributionRecommendActivity entity = mapperFacade.map(recommendActivityDTO, DistributionRecommendActivity.class);
        // 填充创建数据
        entity.setCreateTime(new Date());
        entity.setDeletedStatus(0);

        // 校验创建人数据
        if (creatorId == null || StrUtil.isEmpty(creatorName)) {
            throw new LuckException("创建人数据未填充");
        }
        // 填充创建人数据
        entity.setCreateUserId(creatorId);
        entity.setCreateUserName(creatorName);

        // 填充创建默认状态（0未开启）
        entity.setActivityStatus(0);

        // 数据排序填充默认值
        entity.setDataSort(1);

        return entity;
    }

    /**
     * 最大商品关联数量
     */
    private static final Integer MAXIMUM_GOODS_RELATE = 500;

    /**
     * 最大门店关联数量
     */
    private static final Integer MAXIMUM_SHOP_RELATE = 500;

    /**
     * 通用防腐层
     *
     * @param recommendActivityDTO 外部作用域数据传入
     */
    private void commonFacade(DistributionRecommendActivityDTO recommendActivityDTO) {
        // 时间校验
        Date activityBeginTime = recommendActivityDTO.getActivityBeginTime();
        Date activityEndTime = recommendActivityDTO.getActivityEndTime();
        if (activityBeginTime.compareTo(activityEndTime) > 0) {
            // 活动开始时间小于结束时间
            throw new LuckException("活动开始时间小于结束时间");
        }

        //  排除参与活动的商品的重复值
        Integer applicableForAllGoods = recommendActivityDTO.getApplicableForAllGoods();
        if (applicableForAllGoods != 1) {
            List<Long> spuIds = recommendActivityDTO.getSpuIds();
            // 非所有活动参与产品，但是参与活动的产品spuIds数组大小为0
            if (spuIds == null || spuIds.size() == 0) {
                throw new LuckException("无产品参与活动");
            }

            // Stream排除重复值
            List<Long> spuIdsAfterDistinct = spuIds.stream().distinct().collect(Collectors.toList());
            // 数据比对
            if (spuIdsAfterDistinct.size() != spuIds.size()) {
                throw new LuckException("参与活动的商品，存在重复数据。");
            }

            if (spuIdsAfterDistinct.size() > MAXIMUM_GOODS_RELATE) {
                throw new LuckException("超过最大活动关联商品数量，所关联SPU数量大于500");
            }
        }

        // 排除参与活动门店的重复值
        Integer applicableForAllStores = recommendActivityDTO.getApplicableForAllStores();
        if (applicableForAllStores != 1) {
            List<Long> shopIds = recommendActivityDTO.getShopIds();
            // 非所有门店参与，但是参与活动的门店shopIds数组大小为0
            if (shopIds == null || shopIds.size() == 0) {
                throw new LuckException("无门店参与活动");
            }
            // Stream排除重复值
            List<Long> shopIdsAfterDistinct = shopIds.stream().distinct().collect(Collectors.toList());
            if (shopIdsAfterDistinct.size() != shopIds.size()) {
                throw new LuckException("参与活动的门店，存在重复数据。");
            }

            if (shopIdsAfterDistinct.size() > MAXIMUM_SHOP_RELATE) {
                throw new LuckException("超过最大活动关联店铺数量，大于500家");
            }
        }
    }

    /**
     * 通知主表更新适用门店、适用商品SPU数量缓存
     *
     * @param activityId 活动标识
     * @return {@link java.lang.Integer} 数据库受影响行数
     */
    private Integer notifyCache(Long activityId) {
        DistributionRecommendActivity entity = distributionRecommendActivityMapper.getById(activityId);
        if (Objects.isNull(entity)) {
            throw new LuckException("数据库无此数据");
        }
        Integer applicableForAllGoods = entity.getApplicableForAllGoods();
        Integer applicableForAllStores = entity.getApplicableForAllStores();
        if (activityId == null) {
            return 0;
        }
        // 更新到数据库中
        return distributionRecommendActivityMapper.modifyCache(activityId, applicableForAllStores, applicableForAllGoods);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer modifyActivity(DistributionRecommendActivityDTO recommendActivityDTO, Long modifierId, String modifierName) {


        Long activityId = recommendActivityDTO.getId();
        List<Long> shopIds = recommendActivityDTO.getShopIds();
        List<Long> spuIds = recommendActivityDTO.getSpuIds();

        // 本次数据库操作实体
        DistributionRecommendActivity dataOperationEntity = this.modifyDataFacade(recommendActivityDTO, modifierId, modifierName);
        // 数据查询校验
        Date currentDate = new Date();
        DistributionRecommendActivity entity = distributionRecommendActivityMapper.getById(recommendActivityDTO.getId());
        Integer activityStatus = entity.getActivityStatus();
        Date activityBeginTime = entity.getActivityBeginTime();
        Date activityEndTime = entity.getActivityEndTime();
        // 转化活动状态为可处理的状态
        ActivityStatusEnum resolvableStatus = this.getActivityStatus(activityBeginTime, activityEndTime, currentDate, activityStatus);
        // 正在进行中的状态
        if (resolvableStatus == ActivityStatusEnum.PROCESSING) {
            throw new LuckException("活动进行中，请先暂停活动，再进行修改");
        }
        // 启用，但是还没到时间
        if (resolvableStatus == ActivityStatusEnum.ENABLE) {
            throw new LuckException("请先暂停活动，再进行修改");
        }


        // Notice!!!! 当活动已结束时，数据库内活动状态已启用
        dataOperationEntity.setActivityStatus(ActivityStatusEnum.DISABLE.getCode());
        // 数据库执行更新
        Integer modifyEffectiveRowData = distributionRecommendActivityMapper.modify(dataOperationEntity);

        Integer applicableForAllGoods = recommendActivityDTO.getApplicableForAllGoods();
        Integer applicableForAllStores = recommendActivityDTO.getApplicableForAllStores();

        // 移除当前活动作用域内商品SPU
        distributionRecommendActivityMapper.deleteSpuRelatesByActivityId(activityId);

        // 移除当前活动作用域内适用门店
        distributionRecommendActivityMapper.deleteShopRelatesByActivityId(activityId);

        if (applicableForAllGoods != null && applicableForAllGoods == 0) {
            // 处理新的活动作用域内商品SPU
            distributionRecommendActivityMapper.createSpuRelates(activityId, spuIds);
        }

        if (applicableForAllStores != null && applicableForAllStores == 0) {
            // 处理新的活动作用域内的门店
            distributionRecommendActivityMapper.createShopRelates(activityId, shopIds);
        }

        // 通知刷新缓存
        this.notifyCache(dataOperationEntity.getId());

        return modifyEffectiveRowData;
    }

    /**
     * 修改数据时数据防腐
     *
     * @param recommendActivityDTO 修改活动数据信息
     * @param modifierId           修改人（标识）
     * @param modifierName         修改人（姓名）
     * @return {@link DistributionRecommendActivity} 数据库作用域实体
     */
    public DistributionRecommendActivity modifyDataFacade(DistributionRecommendActivityDTO recommendActivityDTO, Long modifierId, String modifierName) {
        // 调用通用数据防腐
        this.commonFacade(recommendActivityDTO);

        // orika实体转换
        DistributionRecommendActivity entity = mapperFacade.map(recommendActivityDTO, DistributionRecommendActivity.class);

        // 主键判断
        Long id = entity.getId();
        if (id == null) {
            throw new LuckException("所修改的活动不存在");
        }

        // 数据修改人
        if (modifierId == null || StrUtil.isEmpty(modifierName)) {
            throw new LuckException("修改人数据为空");
        }
        entity.setUpdateUserId(modifierId);
        entity.setUpdateUserName(modifierName);

        return entity;
    }

    @Override
    public PageVO<DistributionRecommendActivityVO> listActivity(PageDTO pageDTO, DistributionRecommendActivityQueryDTO distributionRecommendActivityQueryDTO) {

        PageVO<DistributionRecommendActivityVO> pageVO = PageUtil.doPage(pageDTO, () -> distributionRecommendActivityMapper.selectActivity(distributionRecommendActivityQueryDTO));
        Date date = new Date();
        for (DistributionRecommendActivityVO activityPageVO : pageVO.getList()) {
            Date activityEndTime = activityPageVO.getActivityEndTime();
            Integer tempActivityStatus = activityPageVO.getActivityStatus();
            Date activityBeginTime = activityPageVO.getActivityBeginTime();

            if (!ActivityStatusEnums.NOT_ENABLED.getCode().equals(tempActivityStatus)) {
                if (DateUtil.isIn(date, activityBeginTime, activityEndTime)) {
                    activityPageVO.setStatusName(ActivityStatusEnums.IN_PROGRESS.getName());
                } else if (date.compareTo(activityBeginTime) < 0) {
                    activityPageVO.setStatusName(ActivityStatusEnums.NOT_START.getName());
                } else if (date.compareTo(activityEndTime) > 0) {
                    activityPageVO.setStatusName(ActivityStatusEnums.END.getName());
                }
            } else {
                activityPageVO.setStatusName("未启用");
            }
        }
        return pageVO;
    }

    /**
     * 获取活动状态
     *
     * @param beginDate      活动起始时间
     * @param endDate        活动结束时间
     * @param currencyDate   当前时间
     * @param currencyStatus 当前状态
     * @return 状态枚举
     */
    private ActivityStatusEnum getActivityStatus(Date beginDate, Date endDate, Date currencyDate, Integer currencyStatus) {
        if (Objects.equals(currencyStatus, ActivityStatusEnum.DISABLE.getCode())) {
            return ActivityStatusEnum.DISABLE;
        } else {
            if (currencyDate.compareTo(beginDate) > 0 && currencyDate.compareTo(endDate) < 0) {
                // 当前时间在活动开始时间和结束时间区间范围内
                return ActivityStatusEnum.PROCESSING;
            }
            if (currencyDate.compareTo(endDate) > 0) {
                // 当前时间在活动结束时间外
                return ActivityStatusEnum.ENDED;
            }
            if (currencyDate.compareTo(beginDate) < 0) {
                // 当前时间在活动开始之前
                return ActivityStatusEnum.ENABLE;
            }
        }
        return ActivityStatusEnum.ENABLE;
    }

    @Override
    public DistributionRecommendActivityDetailVO getById(Long id) {
        DistributionRecommendActivity entity = distributionRecommendActivityMapper.getById(id);
        if (entity == null) {
            return null;
        }

        // 转化为视图域
        DistributionRecommendActivityDetailVO result = mapperFacade.map(entity, DistributionRecommendActivityDetailVO.class);

        // 查询出商品SPU标识和查询出门店标识，并且填充到详情视图中
        Integer applicableForAllGoods = result.getApplicableForAllGoods();
        if (applicableForAllGoods == 0) {
            List<Long> spuIds = distributionRecommendActivityMapper.listActivitySpuRelates(result.getId());
            result.setSpuIds(spuIds);
        } else {
            result.setSpuIds(new ArrayList<>());
        }
        Integer applicableForAllStores = result.getApplicableForAllStores();
        if (applicableForAllStores == 0) {
            List<Long> shopIds = distributionRecommendActivityMapper.listActivityShopRelates(result.getId());
            result.setShopIds(shopIds);
        } else {
            result.setShopIds(new ArrayList<>());
        }
        return result;
    }

    @Override
    public Integer modifyActivityStatus(Long id, Integer enableDisableStatus) {
        if (id == null) {
            throw new LuckException("主推商品活动标识不得为空");
        }
        // 传递进来的数据非ActivityStatusEnum.ENABLE.getCode()和ActivityStatusEnum.DISABLE.getCode()
        if ((!Objects.equals(enableDisableStatus, ActivityStatusEnum.ENABLE.getCode())) &&
                (!Objects.equals(enableDisableStatus, ActivityStatusEnum.DISABLE.getCode()))) {
            throw new LuckException("设置主推商品活动状态错误");
        }

        return distributionRecommendActivityMapper.modifyActivityStatus(id, enableDisableStatus);
    }

    private ProductFeignClient productFeignClient;

    @Autowired
    public void setProductFeignClient(ProductFeignClient productFeignClient) {
        this.productFeignClient = productFeignClient;
    }

    @Override
    public PageVO<DistributionRecommendSpuListVO> listGoodsSpuForShoppingGuideAndWeike(PageDTO pageDTO, DistributionRecommendSpuQueryDTO distributionRecommendSpuQueryDTO) {
        PageVO<DistributionRecommendSpuListVO> resultPageVo = new PageVO<>();
        // 加载数据
        Long numberOfAllSpuRunningActivity = distributionRecommendActivityMapper.getNumberOfAllSpuRunningActivity(distributionRecommendSpuQueryDTO);
        PageVO<SpuCommonVO> remoteCommonPageVO;
        if (numberOfAllSpuRunningActivity > 0) {
            // 有一条所有商品SPU都参与的活动
            remoteCommonPageVO = this.listRemoteResult(pageDTO, distributionRecommendSpuQueryDTO, null);
        } else {
            // 当前有固定SPU参与活动
            List<Long> spuIds = distributionRecommendActivityMapper.listActivatingSpu(distributionRecommendSpuQueryDTO);
            //如果当前门店没有商品参与直接返回空
            if(CollUtil.isEmpty(spuIds)){
                resultPageVo.setList(Collections.emptyList());
                return resultPageVo;
            }
            remoteCommonPageVO = this.listRemoteResult(pageDTO, distributionRecommendSpuQueryDTO, spuIds);
        }


        // 将远程查询的实体转化为，本地实体以及获取查询出来的所有SPU
        if(remoteCommonPageVO==null ||  CollUtil.isEmpty(remoteCommonPageVO.getList())){
            resultPageVo.setList(Collections.emptyList());
            return resultPageVo;
        }

        List<SpuCommonVO> list = remoteCommonPageVO.getList();
        List<Long> currentSpuIds = new ArrayList<>();
        List<DistributionRecommendSpuListVO> result = new ArrayList<>();


        for (SpuCommonVO spuCommonVO : list) {
            Long spuId = spuCommonVO.getSpuId();
            currentSpuIds.add(spuId);
            DistributionRecommendSpuListVO singleResult = mapperFacade.map(spuCommonVO, DistributionRecommendSpuListVO.class);
            result.add(singleResult);
        }


        // 是否展示 导购、微客 价格
        boolean showCommissionRate = distributionRecommendSpuQueryDTO.isShowCommissionRate();
        if (showCommissionRate) {
            DistributionCommissionRateDTO distributionCommissionRateDTO = new DistributionCommissionRateDTO();
            distributionCommissionRateDTO.setSpuIdList(currentSpuIds);
            Map<Long, DistributionCommissionRateVO> distributionCommissionRate = distributionCommissionService.getDistributionCommissionRate(distributionCommissionRateDTO);
            // 填充分销金额
            for (DistributionRecommendSpuListVO distributionRecommendSpuListVO : result) {
                // 存入导购价格
                distributionRecommendSpuListVO.setGuideRate(distributionCommissionRate.get(distributionRecommendSpuListVO.getSpuId()).getGuideRate());
                // 存入微客价格
                distributionRecommendSpuListVO.setShareRate(distributionCommissionRate.get(distributionRecommendSpuListVO.getSpuId()).getShareRate());
            }

        }

        // 重新构建返回结果
        resultPageVo.setPages(remoteCommonPageVO.getPages());
        resultPageVo.setList(result);
        resultPageVo.setTotal(remoteCommonPageVO.getTotal());

        return resultPageVo;
    }

    private DistributionCommissionService distributionCommissionService;

    @Autowired
    public void setDistributionCommissionService(DistributionCommissionService distributionCommissionService) {
        this.distributionCommissionService = distributionCommissionService;
    }

    private PageVO<SpuCommonVO> listRemoteResult(PageDTO pageDTO, DistributionRecommendSpuQueryDTO distributionRecommendSpuQueryDTO, List<Long> spuIds) {
        ProductSearchDTO productSearch = mapperFacade.map(distributionRecommendSpuQueryDTO, ProductSearchDTO.class);
        productSearch.setPageNum(pageDTO.getPageNum());
        productSearch.setPageSize(pageDTO.getPageSize());
        if (spuIds != null && spuIds.size() > 0) {
            productSearch.setSpuIds(spuIds);
        }
        // 无SPU限制
        log.info("分销商品查询参数:{}", JSONObject.toJSONString(productSearch));
        // RPC-产品模块拉产品数据
        ServerResponseEntity<PageVO<SpuCommonVO>> responseEntity = productFeignClient.commonSearch(productSearch);
        if (responseEntity.isSuccess()) {
            log.info("分销商品查询数据：{}", JSONObject.toJSONString(responseEntity.getData()));
        }
        return responseEntity.getData();
    }

}
