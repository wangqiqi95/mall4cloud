package com.mall4j.cloud.group.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.map.MapUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mall4j.cloud.api.auth.bo.UserInfoInTokenBO;
import com.mall4j.cloud.api.coupon.vo.CouponListVO;
import com.mall4j.cloud.api.user.vo.TagVO;
import com.mall4j.cloud.api.user.vo.UserApiVO;
import com.mall4j.cloud.common.cache.constant.GroupCacheNames;
import com.mall4j.cloud.common.cache.util.RedisUtil;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.group.bo.AddPopUpAdAttachmentBO;
import com.mall4j.cloud.group.bo.AddPopUpAdPushRuleBO;
import com.mall4j.cloud.group.bo.PopUpAdParamsValidBO;
import com.mall4j.cloud.group.constant.*;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.common.security.AuthUserContext;
import com.mall4j.cloud.group.bo.AttachmentBO;
import com.mall4j.cloud.group.dto.*;
import com.mall4j.cloud.group.manager.*;
import com.mall4j.cloud.group.model.PopUpAd;
import com.mall4j.cloud.group.mapper.PopUpAdMapper;
import com.mall4j.cloud.group.service.PopUpAdService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mall4j.cloud.group.service.QuestionnaireService;
import com.mall4j.cloud.group.strategy.ad.attachment.BaseAttachmentHandler;
import com.mall4j.cloud.group.strategy.ad.attachment.enums.AttachmentHandlerEnum;
import com.mall4j.cloud.group.strategy.ad.rule.BasePushRuleHandler;
import com.mall4j.cloud.group.strategy.ad.rule.enums.PushRuleHandlerEnum;
import com.mall4j.cloud.group.vo.*;
import com.mall4j.cloud.group.vo.questionnaire.QuestionnaireDetailVO;
import lombok.extern.slf4j.Slf4j;
import ma.glasnost.orika.MapperFacade;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
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
@Slf4j
public class PopUpAdServiceImpl extends ServiceImpl<PopUpAdMapper, PopUpAd> implements PopUpAdService {

    @Autowired
    private BaseAttachmentHandler baseAttachmentHandler;

    @Autowired
    private BasePushRuleHandler basePushRuleHandler;

    @Autowired
    private PopUpAdAttachmentManager popUpAdAttachmentManager;

    @Autowired
    private PopUpAdManager popUpAdManager;

    @Autowired
    private PopUpAdUserOperateManager popUpAdUserOperateManager;

    @Autowired
    private PopUpAdRuleManager popUpAdRuleManager;

    @Autowired
    private PopUpAdStoreRelationManager popUpAdStoreRelationManager;

    @Autowired
    private PopUpAdPageManager popUpAdPageManager;

    @Autowired
    private MapperFacade mapperFacade;

    @Autowired
    private QuestionnaireService questionnaireService;


    /**
     * 新增广告
     * @param addPopUpAdDTO
     * */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public ServerResponseEntity createPopUpAd(AddPopUpAdDTO addPopUpAdDTO) {


        PopUpAdParamsValidBO bo = new PopUpAdParamsValidBO();
        BeanUtils.copyProperties(addPopUpAdDTO, bo);
        if (CollectionUtil.isNotEmpty(addPopUpAdDTO.getPopUpAdAttachmentList())){
            List<UpdatePopUpAdAttachmentDTO> popUpAdAttachmentDTOS = mapperFacade.mapAsList(addPopUpAdDTO.getPopUpAdAttachmentList(), UpdatePopUpAdAttachmentDTO.class);
            bo.setPopUpAdAttachmentList(popUpAdAttachmentDTOS);
        }


        if (CollectionUtil.isNotEmpty(addPopUpAdDTO.getPopUpAdPushRuleList())){
            List<UpdatePopUpAdPushRuleDTO> popUpAdPushRuleDTOS = mapperFacade.mapAsList(addPopUpAdDTO.getPopUpAdPushRuleList(), UpdatePopUpAdPushRuleDTO.class);
            bo.setPopUpAdPushRuleList(popUpAdPushRuleDTOS);
        }

        popUpAdManager.validParams(bo);

        UserInfoInTokenBO tokenUser = AuthUserContext.get();
//        tokenUser = new UserInfoInTokenBO();
//        tokenUser.setUserId(1L);

        PopUpAd popUpAd = new PopUpAd();
        BeanUtils.copyProperties(addPopUpAdDTO, popUpAd);
        save(popUpAd);


        if (CollectionUtil.isNotEmpty(addPopUpAdDTO.getPopUpAdPushRuleList())){

            List<AddPopUpAdPushRuleBO> addPopUpAdPushRuleBOS = mapperFacade.mapAsList(addPopUpAdDTO.getPopUpAdPushRuleList(), AddPopUpAdPushRuleBO.class);
            popUpAdRuleManager.addBatch(popUpAd.getPopUpAdId(), addPopUpAdPushRuleBOS, tokenUser.getUserId());
        }


        if (CollectionUtil.isNotEmpty(addPopUpAdDTO.getPopUpAdAttachmentList())){
            List<AddPopUpAdAttachmentBO> addPopUpAdAttachmentBOS = mapperFacade.mapAsList(addPopUpAdDTO.getPopUpAdAttachmentList(), AddPopUpAdAttachmentBO.class);
            popUpAdAttachmentManager.addBatch(addPopUpAdAttachmentBOS, popUpAd.getPopUpAdId(), tokenUser.getUserId());

        }


        if (CollectionUtil.isNotEmpty(addPopUpAdDTO.getStoreIdList())){
            popUpAdStoreRelationManager.addBatch(addPopUpAdDTO.getStoreIdList(), popUpAd.getPopUpAdId());
        }


        if (CollectionUtil.isNotEmpty(addPopUpAdDTO.getPageList())){
            popUpAdPageManager.addBatch(addPopUpAdDTO.getPageList(), tokenUser.getUserId(), popUpAd.getPopUpAdId());
        }


        return ServerResponseEntity.success();
    }

    /**
     * 修改广告
     * @param updatePopUpAdDTO
     * */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public ServerResponseEntity updatePopUpAd(UpdatePopUpAdDTO updatePopUpAdDTO) {

        //校验是否为时间推送规则，是的话校验是否设置了匹配的推送规则
        PopUpAdParamsValidBO bo = new PopUpAdParamsValidBO();
        BeanUtils.copyProperties(updatePopUpAdDTO, bo);
        bo.setPopUpAdAttachmentList(updatePopUpAdDTO.getPopUpAdAttachmentList());
        bo.setPopUpAdPushRuleList(updatePopUpAdDTO.getPopUpAdPushRuleList());
        popUpAdManager.validParams(bo);

        UserInfoInTokenBO tokenUser = AuthUserContext.get();
//        UserInfoInTokenBO tokenUser = new UserInfoInTokenBO();
//        tokenUser.setUserId(1L);

        PopUpAd popUpAd = lambdaQuery()
                .eq(PopUpAd::getPopUpAdId, updatePopUpAdDTO.getPopUpAdId())
                .eq(PopUpAd::getDeleted, Boolean.FALSE)
                .one();

        if (Objects.isNull(popUpAd)){
            return ServerResponseEntity.showFailMsg("广告不存在");
        }

        BeanUtils.copyProperties(updatePopUpAdDTO, popUpAd);
        popUpAd.setUpdateUserId(tokenUser.getUserId());
        popUpAd.setUpdateTime(LocalDateTime.now());

        updateById(popUpAd);

        //校验规则是否被修改或删除或新增
        List<PopUpAdRuleVO> ruleVOList = popUpAdRuleManager.getTheRuleByAdId(popUpAd.getPopUpAdId());


        if (CollectionUtil.isNotEmpty(updatePopUpAdDTO.getPopUpAdPushRuleList())){
            List<UpdatePopUpAdPushRuleDTO> popUpAdPushRuleDTOS = updatePopUpAdDTO.getPopUpAdPushRuleList().stream()
                    .filter(rule -> Objects.isNull(rule.getPopUpAdRuleId()))
                    .collect(Collectors.toList());

            //获取可能被编辑的规则
            List<UpdatePopUpAdPushRuleDTO> checkRuleList = updatePopUpAdDTO.getPopUpAdPushRuleList().stream()
                    .filter(rule -> Objects.nonNull(rule.getPopUpAdRuleId()))
                    .collect(Collectors.toList());

            if (CollectionUtil.isEmpty(checkRuleList)){
                popUpAdRuleManager.removeByAdId(popUpAd.getPopUpAdId());
            }


            if (CollectionUtil.isNotEmpty(popUpAdPushRuleDTOS)){
                List<AddPopUpAdPushRuleBO> addPopUpAdPushRuleBOS = mapperFacade.mapAsList(popUpAdPushRuleDTOS, AddPopUpAdPushRuleBO.class);

                popUpAdRuleManager.addBatch(popUpAd.getPopUpAdId(), addPopUpAdPushRuleBOS, tokenUser.getUserId());
            }


            if (CollectionUtil.isNotEmpty(checkRuleList)){

                List<Long> checkRuleIdList =  checkRuleList.stream()
                        .map(UpdatePopUpAdPushRuleDTO::getPopUpAdRuleId)
                        .collect(Collectors.toList());


                //聚合删除的规则
                List<Long> removeRuleIdList = ruleVOList.stream()
                        .filter(rule -> !checkRuleIdList.contains(rule.getPopUpAdRuleId()))
                        .map(PopUpAdRuleVO::getPopUpAdRuleId)
                        .collect(Collectors.toList());

                if (CollectionUtil.isNotEmpty(removeRuleIdList)){
                    popUpAdRuleManager.removeIdList(removeRuleIdList);
                }

                Map<Long, PopUpAdRuleVO> upAdRuleVOMap = ruleVOList.stream()
                        .collect(Collectors.toMap(PopUpAdRuleVO::getPopUpAdRuleId, x -> x));

                checkRuleList.stream().forEach(update -> {
                    PopUpAdRuleVO rule = upAdRuleVOMap.get(update.getPopUpAdRuleId());
                    update.setPopUpAdPushRule(updatePopUpAdDTO.getPushRule());
                    //比较修改，获取准确的修改人
                    popUpAdRuleManager.compareUpdate(rule, update, tokenUser.getUserId());
                });
            }
        }



        //校验素材附件会否被修改或删除或新增
        List<PopUpAdAttachmentVO> popUpAdAttachmentVOList = popUpAdAttachmentManager.getPopUpAdAttachmentByAdId(popUpAd.getPopUpAdId());

        List<UpdatePopUpAdAttachmentDTO> checkAttachmentList = updatePopUpAdDTO.getPopUpAdAttachmentList().stream()
                .filter(addAttachment -> Objects.nonNull(addAttachment.getPopUpAdMediaId()))
                .collect(Collectors.toList());

        if (CollectionUtil.isEmpty(checkAttachmentList)){
            popUpAdAttachmentManager.removeByAdId(popUpAd.getPopUpAdId());
        }


        List<UpdatePopUpAdAttachmentDTO> addAttachmentDTOList = updatePopUpAdDTO.getPopUpAdAttachmentList().stream()
                .filter(attachment -> Objects.isNull(attachment.getPopUpAdMediaId()))
                .collect(Collectors.toList());

        if (CollectionUtil.isNotEmpty(addAttachmentDTOList)){
            List<AddPopUpAdAttachmentBO> addPopUpAdAttachmentBOS = mapperFacade.mapAsList(addAttachmentDTOList, AddPopUpAdAttachmentBO.class);
            popUpAdAttachmentManager.addBatch(addPopUpAdAttachmentBOS, popUpAd.getPopUpAdId(), tokenUser.getUserId());
        }



        if (CollectionUtil.isNotEmpty(checkAttachmentList)){

            List<Long> checkAttachmentIdList = checkAttachmentList.stream()
                    .map(UpdatePopUpAdAttachmentDTO::getPopUpAdMediaId)
                    .collect(Collectors.toList());

            List<Long> removeAttachmentIdList = popUpAdAttachmentVOList.stream()
                    .filter(adAttachment -> !checkAttachmentIdList.contains(adAttachment.getPopUpAdMediaId()))
                    .map(PopUpAdAttachmentVO::getPopUpAdMediaId)
                    .collect(Collectors.toList());
            if (CollectionUtil.isNotEmpty(removeAttachmentIdList)){
                popUpAdAttachmentManager.removeBatchByIdList(removeAttachmentIdList);
            }

            Map<Long, PopUpAdAttachmentVO> attachmentVOMap = popUpAdAttachmentVOList.stream()
                    .collect(Collectors.toMap(PopUpAdAttachmentVO::getPopUpAdMediaId, addAttachment -> addAttachment));


            checkAttachmentList.stream().forEach(update -> {
                PopUpAdAttachmentVO popUpAdAttachmentVO = attachmentVOMap.get(update.getPopUpAdMediaId());
                popUpAdAttachmentManager.compareUpdate(popUpAdAttachmentVO, update, tokenUser.getUserId());
            });
        }

        //检查并修改应用页面数据
        List<PopUpAdPageVO> popUpAdPageVOList = popUpAdPageManager.getListByAdId(popUpAd.getPopUpAdId());

        List<UpdatePopUpAdPageDTO> checkPageList = updatePopUpAdDTO.getPageList().stream()
                .filter(page -> Objects.nonNull(page.getPopUpAdPageId()))
                .collect(Collectors.toList());

        if (CollectionUtil.isEmpty(checkPageList)){
            popUpAdPageManager.removeByAdId(popUpAd.getPopUpAdId());
        }

        List<String> addPageList = updatePopUpAdDTO.getPageList().stream()
                .filter(page -> Objects.isNull(page.getPopUpAdPageId()))
                .map(UpdatePopUpAdPageDTO::getPageUrl)
                .collect(Collectors.toList());
        if (CollectionUtil.isNotEmpty(addPageList)){
            popUpAdPageManager.addBatch(addPageList, tokenUser.getUserId(), popUpAd.getPopUpAdId());
        }



        if (CollectionUtil.isNotEmpty(checkPageList)){
            List<Long> checkPageIdList = checkPageList.stream()
                    .map(UpdatePopUpAdPageDTO::getPopUpAdPageId)
                    .collect(Collectors.toList());

            List<Long> removeIdList = popUpAdPageVOList.stream()
                    .filter(page -> !checkPageIdList.contains(page.getPopUpAdPageId()))
                    .map(PopUpAdPageVO::getPopUpAdPageId)
                    .collect(Collectors.toList());

            if (CollectionUtil.isNotEmpty(removeIdList)){
                popUpAdPageManager.removeBatch(removeIdList);
            }

            Map<Long, PopUpAdPageVO> popUpAdPageVOMap = popUpAdPageVOList.stream()
                    .collect(Collectors.toMap(PopUpAdPageVO::getPopUpAdPageId, page -> page));

            checkPageList.stream().forEach(page -> {
                PopUpAdPageVO popUpAdPageVO = popUpAdPageVOMap.get(page.getPopUpAdPageId());
                popUpAdPageManager.compareUpdate(popUpAdPageVO, page, tokenUser.getUserId());
            });
        }


        if (CollectionUtil.isNotEmpty(updatePopUpAdDTO.getStoreIdList())){
            popUpAdStoreRelationManager.removeByAdId(popUpAd.getPopUpAdId());

            popUpAdStoreRelationManager.addBatch(updatePopUpAdDTO.getStoreIdList(), popUpAd.getPopUpAdId());
        }

        RedisUtil.del(GroupCacheNames.ATTACHMENT+popUpAd.getPopUpAdId());

        return ServerResponseEntity.success();
    }


    /**
     * 删除广告
     * @param adId 广告ID
     * */
    @Override
    public ServerResponseEntity remove(Long adId) {

        UserInfoInTokenBO tokenUser = AuthUserContext.get();

         lambdaUpdate()
                .eq(PopUpAd::getPopUpAdId, adId)
                .set(PopUpAd::getDeleted, RemoveStatusEnum.IS_REMOVE.getRemoveStatus())
                .set(PopUpAd::getUpdateUserId, tokenUser.getUserId())
                .set(PopUpAd::getUpdateTime, LocalDateTime.now())
                .update();

        return ServerResponseEntity.success();
    }


    /**
     * 禁用广告
     * @param adId 广告ID
     * */
    @Override
    public ServerResponseEntity<Boolean> enableOrDisableAd(Long adId) {

        PopUpAd popUpAd = getById(adId);

        UserInfoInTokenBO tokenUser = AuthUserContext.get();

        if (Objects.isNull(popUpAd)){
            return ServerResponseEntity.showFailMsg("广告不存在，请检查！");
        }

        if (popUpAd.getStatus().equals(PopUpAdStatusEnum.ENABLE.getAdStatus())){
            popUpAd.setStatus(PopUpAdStatusEnum.DISABLE.getAdStatus());
        }else {
            popUpAd.setStatus(PopUpAdStatusEnum.ENABLE.getAdStatus());
        }

        popUpAd.setUpdateUserId(tokenUser.getUserId());
        popUpAd.setUpdateTime(LocalDateTime.now());

        updateById(popUpAd);
        return ServerResponseEntity.success(popUpAd.getStatus());
    }

    /**
     * 小程序端广告弹出
     * @param adIdList 广告ID集合
     * @param tempUid 用户临时ID
     * @param storeId 门店ID
     * */
    @Override
    public ServerResponseEntity<PopUpAdContainerVO> popUpByAdIdList(List<Long> adIdList, String tempUid, Long storeId, String entrance) {

        UserInfoInTokenBO tokenUser = AuthUserContext.get();

        PopUpAdContainerVO containerVO = null;

        String userUnionId;
        Long userId;
        if (Objects.isNull(tokenUser)){
            userId = null;
            userUnionId = popUpAdUserOperateManager.getUserUnionId(tempUid);
        }else {

            UserApiVO userApiVO = popUpAdUserOperateManager.getUserVOById(tokenUser.getUserId());
            userId = tokenUser.getUserId();
            userUnionId = userApiVO.getUnionId();
        }

        List<AttachmentBO> attachmentBOList;

        LocalDateTime now = LocalDateTime.now();

        //查询这些广告仍在生效的广告
        List<PopUpAd> popUpAdList = lambdaQuery()
                .in(PopUpAd::getPopUpAdId, adIdList)
                .eq(PopUpAd::getStatus, PopUpAdStatusEnum.ENABLE.getAdStatusIntValue())
                .le(PopUpAd::getActivityBeginTime, now)
                .ge(PopUpAd::getActivityEndTime, now)
                .eq(PopUpAd::getDeleted, RemoveStatusEnum.NOT_REMOVE.getRemoveStatus())
                .orderByDesc(PopUpAd::getCreateTime)
                .list();

        if (CollectionUtil.isNotEmpty(popUpAdList)){

            //校验是否需要弹出(通过缓存)
            popUpAdList = popUpAdList.stream()
                    .filter(popUpAd -> !RedisUtil.hasKey(popUpAdManager.createKey(popUpAd.getPopUpAdId())+userUnionId))
                    .collect(Collectors.toList());

            //校验弹出时段
            popUpAdList = popUpAdList.stream()
                    .filter(popUpAd -> basePushRuleHandler.validation(popUpAd,storeId,userId))
                    .limit(5)
                    .collect(Collectors.toList());

            Map<Long, Integer> autoOffSecondsMap = popUpAdList.stream()
                    .filter(ad -> Objects.nonNull(ad.getAutoOffSeconds()))
                    .collect(Collectors.toMap(PopUpAd::getPopUpAdId, PopUpAd::getAutoOffSeconds));


            //聚合广告ID
            adIdList = popUpAdList.stream()
                    .map(PopUpAd::getPopUpAdId)
                    .collect(Collectors.toList());

            //主要提前获取缓存中可获取的对象信息
            //构建广告优惠券容器对象
            List<PopUpAdFormCouponVO> popUpAdFormCouponList = new ArrayList<>();
            //构建广告图片容器对象
            List<PopUpAdFormImageVO> popUpAdFormImageVOList = new ArrayList<>();
            //构建广告视频容器对象
            List<PopUpAdFormVideoVO> popUpAdFormVideoVOList = new ArrayList<>();

            //如果无匹配广告
            if (CollectionUtil.isNotEmpty(adIdList)){

                //保存一份用来新增浏览记录
                List<Long> addIdList = new ArrayList<>();
                addIdList.addAll(adIdList);

                //创建相关BO对象
                attachmentBOList = popUpAdList.stream()
                        .map(popUpAd -> {
                            AttachmentBO attachmentBO = new AttachmentBO();
                            attachmentBO.setPopUpAdId(popUpAd.getPopUpAdId());
                            attachmentBO.setAttachmentType(popUpAd.getAttachmentType());
                            attachmentBO.setActivityEndTime(popUpAd.getActivityEndTime());
                            return attachmentBO;
                        }).collect(Collectors.toList());

                List<Long> filterAdIdList = new ArrayList<>();
                //尝试从缓存中获取数据
                attachmentBOList.stream()
                        .forEach(attachmentBO -> {
                            Object attachmentObj;
                            if (RedisUtil.hasKey(GroupCacheNames.ATTACHMENT+attachmentBO.getPopUpAdId())){

                                //保存已从缓存中获取到的ID
                                filterAdIdList.add(attachmentBO.getPopUpAdId());

                                attachmentObj = RedisUtil.get(GroupCacheNames.ATTACHMENT + attachmentBO.getPopUpAdId());

                                //获取缓存中的图片广告
                                if (attachmentBO.getAttachmentType().equals(AttachmentHandlerEnum.IMAGE.getContentType())){
                                    List<PopUpAdFormImageVO> popUpAdFormImageVOS = (List<PopUpAdFormImageVO>) attachmentObj;
                                    popUpAdFormImageVOList.addAll(popUpAdFormImageVOS);
                                }
                                //获取缓存中的视频广告
                                if (attachmentBO.getAttachmentType().equals(AttachmentHandlerEnum.VIDEO.getContentType())){
                                    PopUpAdFormVideoVO popUpAdFormVideoVO = (PopUpAdFormVideoVO)attachmentObj;
                                    popUpAdFormVideoVOList.add(popUpAdFormVideoVO);
                                }
                                //获取缓存中的优惠券广告
                                if (attachmentBO.getAttachmentType().equals(AttachmentHandlerEnum.COUPON.getContentType())){
                                    PopUpAdFormCouponVO popUpAdFormCouponVO = (PopUpAdFormCouponVO) attachmentObj;
                                    popUpAdFormCouponList.add(popUpAdFormCouponVO);
                                }
                            }

                        });

                //把缓存中已经获取到的广告ID剔除
                if (CollectionUtil.isNotEmpty(filterAdIdList)){
                    adIdList = adIdList.stream()
                            .filter(adId -> !filterAdIdList.contains(adId))
                            .collect(Collectors.toList());
                }

                if (CollectionUtil.isNotEmpty(adIdList)){
                    //获取所有附件素材
                    List<PopUpAdAttachmentVO> popUpAdAttachmentList = popUpAdAttachmentManager.getPopUpAdAttachmentByAdIdList(adIdList);

                    //根据广告ID进行附件素材分组
                    Map<Long, List<PopUpAdAttachmentVO>> popUpAdAttachmentVOMap = popUpAdAttachmentList.stream()
                            .collect(Collectors.groupingBy(PopUpAdAttachmentVO::getPopUpAdId));

                    //根据广告ID将附件素材赋值给对应的BO
                    List<Long> finalAdIdList = adIdList;
                    attachmentBOList = attachmentBOList.stream()
                            .filter(attachmentBO -> finalAdIdList.contains(attachmentBO.getPopUpAdId()))
                            .map(attachmentBO ->{
                                attachmentBO.setPopUpAdAttachmentVOList(popUpAdAttachmentVOMap.get(attachmentBO.getPopUpAdId()));
                                Integer autoOffSeconds;
                                if (MapUtil.isNotEmpty(autoOffSecondsMap)
                                        && Objects.nonNull(autoOffSeconds = autoOffSecondsMap.get(attachmentBO.getPopUpAdId()))){
                                    attachmentBO.setAutoOffSeconds(autoOffSeconds);
                                }

                                return attachmentBO;
                            }).collect(Collectors.toList());
//                        .forEach();

                    //获取最终的广告内容
                    containerVO = baseAttachmentHandler.extraction(attachmentBOList);
                    //把缓存中获取的相关广告数据放入最终数据容器
                    containerVO.getPopUpAdFormVideoList().addAll(popUpAdFormVideoVOList);

                    containerVO.getPopUpAdFormImageVOList().addAll(popUpAdFormImageVOList);

                    containerVO.getPopUpAdFormCouponList().addAll(popUpAdFormCouponList);
                }

                if (Objects.isNull(containerVO)){
                    containerVO = new PopUpAdContainerVO();
                    containerVO.setPopUpAdFormVideoList(popUpAdFormVideoVOList);
                    containerVO.setPopUpAdFormCouponList(popUpAdFormCouponList);
                    containerVO.setPopUpAdFormImageVOList(popUpAdFormImageVOList);
                }

                //新增浏览记录（异步）
                popUpAdUserOperateManager.asyncAddBatch(userUnionId, userId, addIdList, storeId, entrance);

                //根据广告频率设置缓存（异步）
                //缓存浏览记录
                popUpAdManager.setBrowseCache(popUpAdList,userUnionId);

                //响应广告信息
                return ServerResponseEntity.success(containerVO);
            }
        }

        return ServerResponseEntity.success();
    }

    /**
     * 获取用户需要弹出广告的页面与广告数据
     * */
    @Override
    public ServerResponseEntity<Map<String, List<Long>>> getThePopUpAdTreeByUser(Long storeId) {

        UserInfoInTokenBO tokenUser = AuthUserContext.get();
        log.info("TOKEN USER DATA IS:{}",JSONObject.toJSONString(tokenUser));

        //获取用户服务门店ID
//        Long serviceStoreId = null;
//        if (Objects.nonNull(tokenUser)){
//            UserApiVO userApiVO = popUpAdUserOperateManager.getUserVOById(tokenUser.getUserId());
//            log.info("USER API VO DATA IS:{}",JSONObject.toJSONString(userApiVO));
//            if (Objects.nonNull(userApiVO)){
//                serviceStoreId = userApiVO.getServiceStoreId();
//            }
//        }

        log.info("SERVICE STORE ID IS {}", storeId);
        //查询当前用户相关门店可见广告
        List<PageAdByUserVO> pageAdList = popUpAdManager.getThePageAdByUser( storeId, LocalDateTime.now());

        //聚合所有广告中的指定的标签
        List<Long> tagIdList = pageAdList.stream()
                .filter(ad -> Objects.nonNull(ad.getUserTagId()))
                .map(PageAdByUserVO::getUserTagId)
                .collect(Collectors.toList());

        //查询用户是否符合标签人群
        if (Objects.isNull(tokenUser)){

            pageAdList = pageAdList.stream()
                    .filter(ad -> Objects.isNull(ad.getUserTagId()))
                    .collect(Collectors.toList());

        }else {

            if (CollectionUtil.isNotEmpty(tagIdList)){
                List<Long> checkList = popUpAdManager.checkTagUser(tagIdList);
                if (CollectionUtil.isNotEmpty(checkList)){
                    pageAdList = pageAdList.stream()
                            .filter(ad -> checkList.contains(ad.getUserTagId())
                                    || Objects.isNull(ad.getUserTagId()))
                            .collect(Collectors.toList());
                }else {
                    pageAdList = pageAdList.stream()
                            .filter(ad -> Objects.isNull(ad.getUserTagId()))
                            .collect(Collectors.toList());
                }
            }

        }

        //校验并相应相关广告信息
        if (CollectionUtil.isNotEmpty(pageAdList)){
            Map<String, List<PageAdByUserVO>> pageAdMap = pageAdList.stream()
                    .collect(Collectors.groupingBy(PageAdByUserVO::getPageUrl));

            Map<String, List<Long>> treeMap = new HashMap<>();

            for (String key:pageAdMap.keySet()) {
                List<PageAdByUserVO> pageAdByUserVOS = pageAdMap.get(key);

                treeMap.put(key,pageAdByUserVOS.stream().map(PageAdByUserVO::getPopUpAdId).collect(Collectors.toList()));
            }

            return ServerResponseEntity.success(treeMap);
        }
        return ServerResponseEntity.success();
    }

    /**
     * 管理后台广告分页查询
     * */
    @Override
    public ServerResponseEntity<PageVO<PopUpAdDataPageVO>> getPage(QueryPopUpAdPageDTO pageDTO) {

        IPage<PopUpAdDataPageVO> page = new Page<>(pageDTO.getPageNum(), pageDTO.getPageSize());

        //获取分页查询数据
        page = this.baseMapper.selectPageVO(page, pageDTO);

        page.getRecords().stream()
                .forEach(popUpAdPageVO -> {
                    //获取浏览次数
                    Integer browseCount = popUpAdUserOperateManager.getBrowseCountByAdId(popUpAdPageVO.getPopUpAdId());
                    //获取浏览人数
                    Integer browsePeopleCount = popUpAdUserOperateManager.getBrowsePeopleCountByAdId(popUpAdPageVO.getPopUpAdId());
                    //获取点击次数
                    Integer clickCount = popUpAdUserOperateManager.getClickCountByAdId(popUpAdPageVO.getPopUpAdId());
                    //获取点击人数
                    Integer clickPeopleCount = popUpAdUserOperateManager.getClickPeopleCountByAdId(popUpAdPageVO.getPopUpAdId());
                    //门店数量
                    Integer storeCount = popUpAdStoreRelationManager.getStoreCountByAdId(popUpAdPageVO.getPopUpAdId());
                    popUpAdPageVO.setBrowseCount(browseCount);
                    popUpAdPageVO.setBrowsePeopleCount(browsePeopleCount);
                    popUpAdPageVO.setClickCount(clickCount);
                    popUpAdPageVO.setClickPeopleCount(clickPeopleCount);
                    popUpAdPageVO.setStoreCount(storeCount);
        });

        PageVO<PopUpAdDataPageVO> pageVO = new PageVO();

        pageVO.setPages((int) page.getPages());
        pageVO.setList(page.getRecords());
        pageVO.setTotal(page.getTotal());

        return ServerResponseEntity.success(pageVO);
    }

    /**
     * 查询广告详情
     * @param adId 广告ID
     * */
    @Override
    public ServerResponseEntity<PopUpAdVO> getPopUpAdById(Long adId) {

        //查询广告详情
        PopUpAd popUpAd = lambdaQuery()
                .eq(PopUpAd::getPopUpAdId, adId)
                .eq(PopUpAd::getDeleted, RemoveStatusEnum.NOT_REMOVE.getRemoveStatus())
                .one();


        if (Objects.nonNull(popUpAd)){
            PopUpAdVO popUpAdVO = new PopUpAdVO();
            BeanUtils.copyProperties(popUpAd, popUpAdVO);
            //如果非全部门店查询门店总数
            if (Objects.nonNull(popUpAd.getIsAllShop()) && popUpAd.getIsAllShop().equals(PopUpAdShopEnum.NOT_ALL_SHOP.getIsAllShop())){
                Integer storeCount = popUpAdStoreRelationManager.getStoreCountByAdId(adId);

                popUpAdVO.setStoreCount(storeCount);
            }
            //查询相关素材附件列表
            List<PopUpAdAttachmentVO> popUpAdAttachmentVOList = popUpAdAttachmentManager.getPopUpAdAttachmentByAdId(adId);
            //查询广告推送规则
            List<PopUpAdRuleVO> popUpAdRuleVOList = popUpAdRuleManager.getTheRuleByAdId(adId);
            //查询广告设置的页面列表
            List<PopUpAdPageVO> popUpAdPageVOList = popUpAdPageManager.getListByAdId(popUpAd.getPopUpAdId());

            popUpAdVO.setAdAttachmentList(popUpAdAttachmentVOList);
            popUpAdVO.setRuleList(popUpAdRuleVOList);
            popUpAdVO.setPageList(popUpAdPageVOList);

            if (Objects.nonNull(popUpAd.getUserTagId())){
                TagVO tag = popUpAdManager.getTagById(popUpAd.getUserTagId());
                if (Objects.nonNull(tag)){
                    popUpAdVO.setUserTagId(tag.getTagId());
                    popUpAdVO.setUserTagName(tag.getTagName());
                }
            }

            if (Objects.nonNull(popUpAd.getIsAllShop()) && popUpAd.getIsAllShop().equals(PopUpAdShopEnum.NOT_ALL_SHOP.getIsAllShop())){
                List<Long> storeIdList = popUpAdStoreRelationManager.getStoreIdListByAdId(popUpAd.getPopUpAdId());
                popUpAdVO.setStoreIdList(storeIdList);
            }

            //聚合业务ID
            List<Long> businessIdList = popUpAdAttachmentVOList.stream()
                    .map(PopUpAdAttachmentVO::getBusinessId)
                    .collect(Collectors.toList());

            //校验是否有业务ID
            if (CollectionUtil.isNotEmpty(businessIdList)){

                //校验是否为优惠券类型的业务
                if (popUpAd.getAttachmentType().equals(AttachmentHandlerEnum.COUPON.getContentType())){
                    //获取优惠券信息
                    List<CouponListVO> couponList = popUpAdManager.getCouponList(businessIdList);
                    //校验优惠券信息
                    if (CollectionUtil.isNotEmpty(couponList)){
                        //聚合优惠券map
                        Map<Long, CouponListVO> couponMap = couponList.stream()
                                .collect(Collectors.toMap(CouponListVO::getId, coupon -> coupon));

                        //设置优惠券信息
                        popUpAdAttachmentVOList.stream()
                                .forEach(adAttachment -> {
                                    CouponListVO couponListVO;
                                    if (Objects.nonNull(couponListVO = couponMap.get(adAttachment.getBusinessId()))){
                                        adAttachment.setCoupon(couponListVO);
                                    }
                        });
                    }
                }

                if (popUpAd.getAttachmentType().equals(AttachmentHandlerEnum.QUESTIONNAIRE.getContentType())){
                    popUpAdAttachmentVOList.stream().forEach(adAttachmentVO -> {
                        QuestionnaireDetailVO questionnaireDetailVO = questionnaireService.getDetailById(adAttachmentVO.getBusinessId());
                        adAttachmentVO.setQuestionnaire(questionnaireDetailVO);
                    });
                }
            }

            return ServerResponseEntity.success(popUpAdVO);
        }


        return ServerResponseEntity.success();
    }

}
