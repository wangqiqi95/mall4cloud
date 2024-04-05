package com.mall4j.cloud.group.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.map.MapUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mall4j.cloud.api.auth.bo.UserInfoInTokenBO;
import com.mall4j.cloud.api.coupon.constant.ActivitySourceEnum;
import com.mall4j.cloud.api.coupon.dto.BatchReceiveCouponDTO;
import com.mall4j.cloud.api.platform.dto.FinishDownLoadDTO;
import com.mall4j.cloud.api.platform.vo.StoreVO;
import com.mall4j.cloud.api.user.vo.UserApiVO;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.common.security.AuthUserContext;
import com.mall4j.cloud.group.bo.ExportPopUpAdOperateBO;
import com.mall4j.cloud.group.constant.PopUpAdUserOperateEnum;
import com.mall4j.cloud.group.dto.PopUpAdClickDTO;
import com.mall4j.cloud.group.dto.PopUpAdCouponReceiveDTO;
import com.mall4j.cloud.group.dto.PopUpAdUserOperatePageDTO;
import com.mall4j.cloud.group.manager.PopUpAdAttachmentManager;
import com.mall4j.cloud.group.manager.PopUpAdManager;
import com.mall4j.cloud.group.manager.PopUpAdUserOperateManager;
import com.mall4j.cloud.group.model.PopUpAdUserOperate;
import com.mall4j.cloud.group.mapper.PopUpAdUserOperateMapper;
import com.mall4j.cloud.group.service.PopUpAdUserOperateService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mall4j.cloud.group.service.async.AsyncPopUpAdOperateService;
import com.mall4j.cloud.group.vo.OperateStatisticsVO;
import com.mall4j.cloud.group.vo.PopUpAdAttachmentVO;
import com.mall4j.cloud.group.vo.PopUpAdUserOperateVO;
import com.mall4j.cloud.group.vo.PopUpAdVO;
import ma.glasnost.orika.MapperFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author ben
 * @since 2023-04-21
 */
@Service
public class PopUpAdUserOperateServiceImpl extends ServiceImpl<PopUpAdUserOperateMapper, PopUpAdUserOperate> implements PopUpAdUserOperateService {

   @Autowired
   private MapperFacade mapperFacade;

   @Autowired
   private PopUpAdUserOperateManager popUpAdUserOperateManager;

    @Autowired
    private PopUpAdManager popUpAdManager;

    @Autowired
    private AsyncPopUpAdOperateService asyncPopUpAdOperateService;

    @Autowired
    private PopUpAdAttachmentManager popUpAdAttachmentManager;


    /**
     * 广告统计
     * */
    @Override
    public ServerResponseEntity<OperateStatisticsVO> operateStatisticsByAdId(Long adId) {

        //浏览次数
        Integer browseCount = lambdaQuery()
                .eq(PopUpAdUserOperate::getPopUpAdId, adId)
                .eq(PopUpAdUserOperate::getOperate, PopUpAdUserOperateEnum.BROWSE.getOperate())
                .count();

        //浏览人数
        Long browseGroupByUserCount = lambdaQuery()
                .eq(PopUpAdUserOperate::getPopUpAdId, adId)
                .eq(PopUpAdUserOperate::getOperate, PopUpAdUserOperateEnum.BROWSE.getOperate())
                .groupBy(PopUpAdUserOperate::getUnionId)
                .list()
                .stream().count();

        //点击次数
        Integer clickCount = lambdaQuery()
                .eq(PopUpAdUserOperate::getPopUpAdId, adId)
                .eq(PopUpAdUserOperate::getOperate, PopUpAdUserOperateEnum.CLICK.getOperate())
                .count();

        //点击人数
        Long clickGroupByUserCount = lambdaQuery()
                .eq(PopUpAdUserOperate::getPopUpAdId, adId)
                .eq(PopUpAdUserOperate::getOperate, PopUpAdUserOperateEnum.CLICK.getOperate())
                .groupBy(PopUpAdUserOperate::getUnionId)
                .list()
                .stream().count();

        //广告统计视图类
        OperateStatisticsVO operateStatisticsVO = new OperateStatisticsVO();

        operateStatisticsVO.setBrowseCount(browseCount);
        operateStatisticsVO.setBrowsePeopleCount(browseGroupByUserCount.intValue());
        operateStatisticsVO.setClickCount(clickCount);
        operateStatisticsVO.setClickPeopleCount(clickGroupByUserCount.intValue());

        return ServerResponseEntity.success(operateStatisticsVO);
    }

    @Override
    public ServerResponseEntity<PageVO<PopUpAdUserOperateVO>> operateRecordByAdId(PopUpAdUserOperatePageDTO pageDTO) {

        IPage<PopUpAdUserOperate> page = new Page<>(pageDTO.getPageNum(), pageDTO.getPageSize());

        page = lambdaQuery()
                .eq(PopUpAdUserOperate::getPopUpAdId, pageDTO.getAdId())
                .orderByDesc(PopUpAdUserOperate::getCreateTime)
                .page(page);

        PopUpAdVO popUpAdVO = popUpAdManager.getById(pageDTO.getAdId());

        List<PopUpAdUserOperateVO> popUpAdUserOperateVOS = mapperFacade.mapAsList(page.getRecords(), PopUpAdUserOperateVO.class);

        //聚合门店ID并查询门店信息
        List<Long> storeIdList = popUpAdUserOperateVOS.stream()
                .map(PopUpAdUserOperateVO::getStoreId)
                .distinct()
                .collect(Collectors.toList());

        List<StoreVO> storeVOList = popUpAdUserOperateManager.getStoreByIdList(storeIdList);

        Map<Long, StoreVO> storeVOMap = new HashMap<>();
        if (CollectionUtil.isNotEmpty(storeVOList)){
            storeVOMap = storeVOList.stream()
                    .collect(Collectors.toMap(StoreVO::getStoreId, storeVO -> storeVO));
        }

        //聚合用户ID
        List<String> unionIdList = popUpAdUserOperateVOS.stream()
                .map(PopUpAdUserOperateVO::getUnionId)
                .distinct()
                .collect(Collectors.toList());

        List<UserApiVO> userApiVOList = popUpAdUserOperateManager.getUserApiVOByUnionIdList(unionIdList);
        Map<String, UserApiVO> userApiVOMap = new HashMap<>();
        if (CollectionUtil.isNotEmpty(userApiVOList)){
            userApiVOMap = userApiVOList.stream()
                    .collect(Collectors.toMap(UserApiVO::getUnionId, userApiVO -> userApiVO));
        }


        //门店以及用户信息匹配筛选赋值
        Map<Long, StoreVO> finalStoreVOMap = storeVOMap;
        Map<String, UserApiVO> finalUserApiVOMap = userApiVOMap;
        popUpAdUserOperateVOS.stream()
                .forEach(popUpAdUserOperateVO -> {
                    //获取门店相关的门店信息
                    StoreVO storeVO;
                    if (MapUtil.isNotEmpty(finalStoreVOMap)
                            && Objects.nonNull(storeVO = finalStoreVOMap.get(popUpAdUserOperateVO.getStoreId()))){
                        popUpAdUserOperateVO.setStoreCode(storeVO.getStoreCode());
                        popUpAdUserOperateVO.setStoreName(storeVO.getName());
                    }
                    //获取用户信息
                    UserApiVO userApiVO;
                    if (MapUtil.isNotEmpty(finalUserApiVOMap)
                            && Objects.nonNull(userApiVO = finalUserApiVOMap.get(popUpAdUserOperateVO.getUnionId()))){

                        popUpAdUserOperateVO.setVipNickName(userApiVO.getNickName());
                        popUpAdUserOperateVO.setVipCode(userApiVO.getVipcode());

                    }
                    //设置广告名称
                    popUpAdUserOperateVO.setPopUpAdName(popUpAdVO.getActivityName());
                });

        PageVO<PopUpAdUserOperateVO> pageVO = new PageVO<>();

        pageVO.setPages((int) page.getPages());
        pageVO.setTotal(page.getTotal());
        pageVO.setList(popUpAdUserOperateVOS);

        return ServerResponseEntity.success(pageVO);
    }

    @Override
    public ServerResponseEntity click(PopUpAdClickDTO popUpAdClickDTO) {

        UserInfoInTokenBO tokenUser = AuthUserContext.get();

        UserApiVO userApiVO = popUpAdUserOperateManager.getUserVOById(tokenUser.getUserId());

        popUpAdUserOperateManager.addClickBatch(userApiVO.getUnionId(), tokenUser.getUserId(),
                Arrays.asList(popUpAdClickDTO.getAdId()), popUpAdClickDTO.getStoreId(), popUpAdClickDTO.getEntrance());

        return ServerResponseEntity.success();
    }

    @Override
    public ServerResponseEntity downloadData(Long adId, HttpServletResponse response) {

        FinishDownLoadDTO finishDownLoadDTO;
        //拼接文件名称
        PopUpAdVO pop = popUpAdManager.getById(adId);
        StringBuffer fileName = new StringBuffer();
        fileName.append("开屏广告-");
        fileName.append(pop.getActivityName());
        fileName.append("-" + ExportPopUpAdOperateBO.EXCEL_NAME);

        finishDownLoadDTO = popUpAdUserOperateManager.createFinishDownLoadDTO(fileName.toString());
        if (Objects.isNull(finishDownLoadDTO)){
            return ServerResponseEntity.showFailMsg("文件下载失败，请重试");
        }

        asyncPopUpAdOperateService.AsyncExportData(adId, finishDownLoadDTO, pop.getActivityName());

        return ServerResponseEntity.success("操作成功，请转至下载中心下载");
    }

    @Override
    public ServerResponseEntity couponReceive(PopUpAdCouponReceiveDTO popUpAdCouponReceiveDTO) {

        UserInfoInTokenBO tokenUser = AuthUserContext.get();

        UserApiVO userApiVO = popUpAdUserOperateManager.getUserVOById(tokenUser.getUserId());

        //获取所需的所有广告信息
        List<PopUpAdVO> popUpAdVOList = popUpAdManager.getListByIdList(popUpAdCouponReceiveDTO.getAdIdList());

//        LocalDateTime now = LocalDateTime.now();

        //查询相关广告的点击（领取）记录
//        List<PopUpAdUserOperate> popUpAdUserOperateList = lambdaQuery()
////                .select(PopUpAdUserOperate::getPopUpAdId, PopUpAdUserOperate::getUnionId)
//                .in(PopUpAdUserOperate::getPopUpAdId, popUpAdCouponReceiveDTO.getAdIdList())
//                .eq(PopUpAdUserOperate::getUnionId, userApiVO.getUnionId())
//                .eq(PopUpAdUserOperate::getOperate, PopUpAdUserOperateEnum.CLICK.getOperate())
//                .list();

        //聚合相关广告点击记录
//        Map<Long, PopUpAdUserOperate> operateMap = new HashMap<>();
//        if (CollectionUtil.isNotEmpty(popUpAdUserOperateList)){
//            Map<Long, PopUpAdUserOperate> check = popUpAdUserOperateList.stream()
//                    .collect(Collectors.toMap(PopUpAdUserOperate::getPopUpAdId, x -> x));
//            operateMap.putAll(check);
//        }

//        List<Long> receiveAdList = new ArrayList<>();
//        //遍历校验，把合适的广告ID存放到receiveAdList
//        popUpAdVOList.stream()
//                .forEach(popUpAdVO -> {
//                    boolean adEndFlag = true;
//                    if (now.isAfter(popUpAdVO.getActivityEndTime())){
//                        adEndFlag = false;
//                    }
//
//                    if (now.isBefore(popUpAdVO.getActivityBeginTime())){
//                        adEndFlag = false;
//                    }
//                    PopUpAdUserOperate pop = operateMap.get(popUpAdVO.getPopUpAdId());
//                    if (Objects.nonNull(pop)){
//                        adEndFlag = false;
//                    }
//                    if (adEndFlag){
//                        receiveAdList.add(popUpAdVO.getPopUpAdId());
//                    }
//                });

        List<Long> adIdList = popUpAdVOList.stream().map(PopUpAdVO::getPopUpAdId).collect(Collectors.toList());
        if (CollectionUtil.isNotEmpty(adIdList)){
            //把符合条件的广告相关的附件（优惠券）提取出来
            List<PopUpAdAttachmentVO> attachmentVOList = popUpAdAttachmentManager.getPopUpAdAttachmentByAdIdList(adIdList);

            //聚合相关附件（优惠券）的Map
            Map<Long, List<PopUpAdAttachmentVO>> attachmentMap = attachmentVOList.stream()
                    .collect(Collectors.groupingBy(PopUpAdAttachmentVO::getPopUpAdId));

            //遍历符合条件的广告并发送相关优惠券
            for (Long adId : attachmentMap.keySet()) {
                List<Long> couponIdList = attachmentVOList.stream()
                        .map(PopUpAdAttachmentVO::getBusinessId)
                        .collect(Collectors.toList());

                BatchReceiveCouponDTO batchReceiveCouponDTO = new BatchReceiveCouponDTO();

                batchReceiveCouponDTO.setCouponIds(couponIdList);
                batchReceiveCouponDTO.setUserId(tokenUser.getUserId());
                batchReceiveCouponDTO.setActivityId(adId);
                batchReceiveCouponDTO.setActivitySource(ActivitySourceEnum.POP_UP_AD_COUPON.value());

                //发券
                popUpAdUserOperateManager.batchReceive(batchReceiveCouponDTO);
                PopUpAdUserOperate add = new PopUpAdUserOperate();
                add.setOperate(PopUpAdUserOperateEnum.CLICK.getOperate());
                add.setPopUpAdId(adId);
                add.setVipUserId(tokenUser.getUserId());
                add.setStoreId(popUpAdCouponReceiveDTO.getStoreId());
                add.setUnionId(userApiVO.getUnionId());
                this.save(add);

//                //发券成功则新增相关点击记录
//                if (batchReceive){
//                    P
//                }else {
//                    log.error("USER COUPON RECEIVE IS FAIL, AD ID IS: "+adId);
//                }
            }
        }

        return ServerResponseEntity.success();
    }

}
