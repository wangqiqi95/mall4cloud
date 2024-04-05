package com.mall4j.cloud.product.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.mall4j.cloud.api.order.feign.OrderCommFeignClient;
import com.mall4j.cloud.api.user.feign.UserFeignClient;
import com.mall4j.cloud.api.user.vo.UserApiVO;
import com.mall4j.cloud.common.constant.Constant;
import com.mall4j.cloud.common.constant.StatusEnum;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.util.PageUtil;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.exception.LuckException;
import com.mall4j.cloud.common.order.vo.OrderItemLangVO;
import com.mall4j.cloud.common.order.vo.OrderItemVO;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.common.security.AuthUserContext;
import com.mall4j.cloud.common.util.Arith;
import com.mall4j.cloud.common.util.IpHelper;
import com.mall4j.cloud.product.dto.SpuCommDTO;
import com.mall4j.cloud.product.dto.SpuCommPageDTO;
import com.mall4j.cloud.product.mapper.SpuMapper;
import com.mall4j.cloud.product.model.SpuComm;
import com.mall4j.cloud.product.mapper.SpuCommMapper;
import com.mall4j.cloud.product.service.SpuCommService;
import com.mall4j.cloud.product.service.SpuExtensionService;
import com.mall4j.cloud.product.vo.SpuCommStatisticsStarVO;
import com.mall4j.cloud.product.vo.SpuCommStatisticsVO;
import com.mall4j.cloud.product.vo.SpuCommVO;
import io.seata.spring.annotation.GlobalTransactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 商品评论
 *
 * @author YXF
 * @date 2021-01-11 13:47:33
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class SpuCommServiceImpl implements SpuCommService {

    private final  SpuCommMapper spuCommMapper;

    private final UserFeignClient userFeignClient;

    private final SpuMapper spuMapper;

    private final OrderCommFeignClient orderCommFeignClient;

    private final SpuExtensionService spuExtensionService;

    @Override
    public PageVO<SpuCommVO> page(PageDTO pageDTO, SpuCommPageDTO request) {
        return PageUtil.doPage(pageDTO, () -> spuCommMapper.pageList(request));
    }
    @Override
    public List<SpuCommVO> list( SpuCommPageDTO request) {
        return spuCommMapper.pageList(request);
    }

    @Override
    public PageVO<SpuCommVO> page(PageDTO pageDTO, Long shopId) {
        return PageUtil.doPage(pageDTO, () -> spuCommMapper.list(shopId));
    }

    @Override
    public SpuCommVO getBySpuCommId(Long spuCommId) {
        return spuCommMapper.getBySpuCommId(spuCommId);
    }

    @Override
    public void update(SpuComm spuComm) {
        spuCommMapper.update(spuComm);
    }

    @Override
    public void deleteById(Long spuCommId) {
        spuCommMapper.deleteById(spuCommId);
    }

    @Override
    public PageVO<SpuCommVO> spuCommPage(PageDTO pageDTO, Long spuId, Integer evaluate) {
        PageVO<SpuCommVO> spuCommPage = PageUtil.doPage(pageDTO, () -> spuCommMapper.spuCommPage(pageDTO, spuId, evaluate));
        if (CollectionUtil.isEmpty(spuCommPage.getList())) {
            return spuCommPage;
        }
        Set<Long> userIds = new HashSet<>();
        Set<Long> orderItemIds = new HashSet<>();
        spuCommPage.getList().forEach(spuComm -> {
            orderItemIds.add(spuComm.getOrderItemId());
            // 非匿名评价
            if (!Objects.equals(spuComm.getIsAnonymous(), SpuComm.anonymous)) {
                userIds.add(spuComm.getUserId());
            }
        });
       // ServerResponseEntity<List<OrderItemLangVO>> orderItemRes = orderCommFeignClient.listOrderItemLangByIds(new ArrayList<>(orderItemIds));

        ServerResponseEntity<List<OrderItemVO>> orderItemRes = orderCommFeignClient.listOrderItemByIds(new ArrayList<>(orderItemIds));
        ServerResponseEntity<List<UserApiVO>> userResponse = userFeignClient.getUserByUserIds(new ArrayList<>(userIds));
        Map<Long, UserApiVO> userMap = userResponse.getData().stream().collect(Collectors.toMap(UserApiVO::getUserId, u -> u));
        Map<Long, OrderItemVO> orderItemLangMap = orderItemRes.getData().stream().collect(Collectors.toMap(OrderItemVO::getOrderItemId, o -> o));
        spuCommPage.getList().forEach(spuComm -> {
            OrderItemVO orderItemVO = orderItemLangMap.get(spuComm.getOrderItemId());
            if (Objects.nonNull(orderItemVO)) {
                spuComm.setSkuName(orderItemVO.getSkuName());
            }
            if (!Objects.equals(spuComm.getIsAnonymous(), SpuComm.anonymous)) {
                UserApiVO userVO = userMap.get(spuComm.getUserId());
                if (Objects.nonNull(userVO)) {
                    spuComm.setNickName(userVO.getNickName());
                    spuComm.setUserPic(userVO.getPic());
                }
            }
        });
        return spuCommPage;
    }

    @Override
    public SpuCommStatisticsVO getProdCommDataBySpuId(Long spuId) {
        SpuCommStatisticsVO spuCommStatistics = spuCommMapper.getProdCommDataBySpuId(spuId);
        //计算出好评率
        if(spuCommStatistics.getPraiseNumber() == 0 || spuCommStatistics.getNumber() == 0){
            spuCommStatistics.setPositiveRating(0.0);
            return spuCommStatistics;
        }
        spuCommStatistics.setPositiveRating(Arith.div(spuCommStatistics.getPraiseNumber() * 100,spuCommStatistics.getNumber(),2));
        return spuCommStatistics;
    }

    @Override
    public SpuCommStatisticsStarVO getProdCommDataByStar(Long spuId) {
        SpuCommStatisticsStarVO spuCommStatisticsStarVO = spuCommMapper.getProdCommDataByStar(spuId);
        return spuCommStatisticsStarVO;
    }

    @Override
    public PageVO<SpuCommVO> spuCommPageByUserId(PageDTO pageDTO, Long userId) {
        PageVO<SpuCommVO> page = PageUtil.doPage(pageDTO, () -> spuCommMapper.spuCommListByUserId(userId));
        ServerResponseEntity<UserApiVO> userResponse = userFeignClient.getUserData(userId);
        UserApiVO userApiVO = userResponse.getData();
        for (SpuCommVO spuCommVO : page.getList()) {
            spuCommVO.setNickName(userApiVO.getNickName());
            spuCommVO.setPics(userApiVO.getPic());
        }
        return page;
    }

    @Override
    public SpuCommVO getSpuCommByOrderItemId(Long orderItemId, Long userId) {
        SpuCommVO spuCommVO = spuCommMapper.getSpuCommByOrderItemId(orderItemId, userId);
        ServerResponseEntity<OrderItemVO> orderItemRes = orderCommFeignClient.getOrderItemByOrderItemId(spuCommVO.getOrderItemId());
        if (!orderItemRes.isSuccess()) {
            throw new LuckException("获取订单信息出错");
        }
        OrderItemVO orderItemVO = orderItemRes.getData();
        spuCommVO.setSkuName(orderItemVO.getSkuName());
        spuCommVO.setTransactionTime(orderItemVO.getCreateTime());
        return spuCommVO;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @GlobalTransactional(rollbackFor = Exception.class)
    public void comm(SpuCommDTO spuCommDTO) {
        ServerResponseEntity<OrderItemVO> orderItemResponse = orderCommFeignClient.getByOrderItemId(spuCommDTO.getOrderItemId());
        if (!orderItemResponse.isSuccess()) {
            throw new LuckException(orderItemResponse.getMsg());
        }
        OrderItemVO orderItemVO = orderItemResponse.getData();
        // refundStatus = RefundStatusEnum.DISAGREE.value()
        int refundStatus = 4;
        if (Objects.nonNull(orderItemVO.getRefundStatus()) && !Objects.equals(refundStatus,orderItemVO.getRefundStatus())) {
            throw new LuckException("[" +orderItemVO.getSpuName()+"]" +"该商品正在进行售后处理，不能进行评价!");
        }
        Long spuId = getSpuIdByOrderItemId(spuCommDTO.getOrderItemId());
        // 保存商品评论
        saveSpuComm(spuId, spuCommDTO,orderItemVO);
        // 更新商品扩展信息表商品评价数量
        spuExtensionService.changeCommentNum(spuId);
        // 更新订单项评论状态
        orderCommFeignClient.updateOrderItemComm(spuCommDTO.getOrderItemId());
    }

    @Override
    public int countGoodReview(Long spuId) {
        return spuCommMapper.countGoodReview(spuId);
    }

    /**
     * 获取评论的spuId，同时校验评论的
     * @param orderItemId
     * @return
     */
    private Long getSpuIdByOrderItemId(Long orderItemId) {
        ServerResponseEntity<Long> orderResponse  = orderCommFeignClient.getSpuIdByOrderItemId(orderItemId);
        if (orderResponse.isFail()) {
            throw new LuckException(orderResponse.getMsg());
        }
        Long spuId = null;
        try {
            spuId = Long.valueOf(orderResponse.getData().toString());
        } catch (Exception e) {
            throw new LuckException("评论的商品数据有误，请刷新后重试");
        }
        return spuId;
    }

    /**
     * 保存商品评论信息
     * @param spuId
     * @param spuCommDTO
     */
    private void saveSpuComm(Long spuId, SpuCommDTO spuCommDTO,OrderItemVO orderItemVO) {
        if(StrUtil.length(spuCommDTO.getContent()) > Constant.MAX_FIELD_LIMIT){
            // 截取字符串
            spuCommDTO.setContent(StrUtil.subWithLength(spuCommDTO.getContent(),0,500));
        }
        if (StrUtil.isBlank(spuCommDTO.getPics())){
            spuCommDTO.setPics(null);
        }
        Long shopId = spuMapper.getShopIdBySpuId(spuId);
        // 插入评论
        SpuComm spuComm = new SpuComm();
        spuComm.setShopId(shopId);
        spuComm.setSpuId(spuId);
        spuComm.setOrderItemId(spuCommDTO.getOrderItemId());
        spuComm.setUserId(AuthUserContext.get().getUserId());
        spuComm.setScore(spuCommDTO.getScore());
        spuComm.setContent(spuCommDTO.getContent());
        spuComm.setPics(spuCommDTO.getPics());
        spuComm.setReplySts(0);
        spuComm.setIsAnonymous(spuCommDTO.getIsAnonymous());
        // 平台不需要控评
        spuComm.setStatus(StatusEnum.ENABLE.value());
        spuComm.setPostip(IpHelper.getIpAddr());
        if (spuComm.getScore() > Constant.MEDIUM_RATING) {
            spuComm.setEvaluate(1);
        } else if (spuComm.getScore().equals(Constant.MEDIUM_RATING)) {
            spuComm.setEvaluate(2);
        } else if (spuComm.getScore() < Constant.MEDIUM_RATING) {
            spuComm.setEvaluate(3);
        }
        //ADD BY HWY 20220306 增加关联字段
        spuComm.setHasImages(spuComm.getPics()==null?0:1);
        spuComm.setOrderId(orderItemVO.getOrderId());
        spuComm.setOrderNumber(orderItemVO.getOrderNumber());
        ServerResponseEntity<UserApiVO> response =  userFeignClient.getInsiderUserData(spuComm.getUserId());
        if(response!=null && response.getData()!=null) {
            spuComm.setMobile(response.getData().getUserMobile());
            spuComm.setUserName(response.getData().getNickName());
        }
        spuCommMapper.save(spuComm);
    }
}
