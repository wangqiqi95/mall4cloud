package com.mall4j.cloud.group.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.mall4j.cloud.api.biz.feign.WxMaApiFeignClient;
import com.mall4j.cloud.api.biz.vo.WeixinMaSubscriptTmessageSendVO;
import com.mall4j.cloud.api.biz.vo.WeixinMaSubscriptTmessageVO;
import com.mall4j.cloud.api.biz.vo.WeixinMaSubscriptUserRecordVO;
import com.mall4j.cloud.api.coupon.constant.ActivitySourceEnum;
import com.mall4j.cloud.api.coupon.dto.ReceiveCouponDTO;
import com.mall4j.cloud.api.coupon.feign.TCouponFeignClient;
import com.mall4j.cloud.api.docking.skq_crm.dto.PointChangeDto;
import com.mall4j.cloud.api.docking.skq_crm.feign.CrmCustomerPointFeignClient;
import com.mall4j.cloud.api.user.feign.TagClient;
import com.mall4j.cloud.api.user.feign.UserFeignClient;
import com.mall4j.cloud.api.user.vo.UserApiVO;
import com.mall4j.cloud.common.constant.MaSendTypeEnum;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.util.PageUtil;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.exception.Assert;
import com.mall4j.cloud.common.exception.LuckException;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.common.rocketmq.OnsMQTemplate;
import com.mall4j.cloud.common.security.AuthUserContext;
import com.mall4j.cloud.common.util.DateUtils;
import com.mall4j.cloud.common.util.ExcelUtil;
import com.mall4j.cloud.common.util.SkqUtils;
import com.mall4j.cloud.group.dto.questionnaire.*;
import com.mall4j.cloud.group.mapper.*;
import com.mall4j.cloud.group.model.*;
import com.mall4j.cloud.group.service.QuestionnaireExcelService;
import com.mall4j.cloud.group.service.QuestionnaireService;
import com.mall4j.cloud.group.service.QuestionnaireUserAnswerRecordService;
import com.mall4j.cloud.group.service.QuestionnaireUserGiftLogService;
import com.mall4j.cloud.group.vo.questionnaire.*;
import lombok.extern.slf4j.Slf4j;
import ma.glasnost.orika.MapperFacade;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 问卷信息表
 *
 * @author FrozenWatermelon
 * @date 2023-05-08 14:10:59
 */
@Slf4j
@Service
public class QuestionnaireServiceImpl implements QuestionnaireService {

    // 奖品领取 look redis key prefix
    private final String RECEIVE_KEY_PREFIX = "mall4cloud_group:receive::";

    @Autowired
    private QuestionnaireMapper questionnaireMapper;
    @Autowired
    private MapperFacade mapperFacade;
    @Autowired
    QuestionnaireFormMapper questionnaireFormMapper;
    @Autowired
    QuestionnaireGiftMapper questionnaireGiftMapper;
    @Autowired
    QuestionnaireShopMapper questionnaireShopMapper;
    @Autowired
    QuestionnaireUserMapper questionnaireUserMapper;
    @Autowired
    QuestionnaireUserAnswerRecordMapper questionnaireUserAnswerRecordMapper;
    @Autowired
    QuestionnaireUserAnswerRecordContentMapper questionnaireUserAnswerRecordContentMapper;
    @Autowired
    QuestionnaireUserAnswerRecordService questionnaireUserAnswerRecordService;
    @Autowired
    UserFeignClient userFeignClient;
    @Autowired
    CrmCustomerPointFeignClient crmCustomerPointFeignClient;
    @Autowired
    TCouponFeignClient tCouponFeignClient;
    @Autowired
    QuestionnaireUserGiftAddrMapper questionnaireUserGiftAddrMapper;
    @Autowired
    TagClient tagClient;
    @Autowired
    WxMaApiFeignClient wxMaApiFeignClient;
    @Resource
    private OnsMQTemplate sendMaTemplateMessage;

    @Autowired
    private QuestionnaireExcelService questionnaireExcelService;

    @Autowired
    private QuestionnaireUserGiftLogService questionnaireUserGiftLogService;

    @Autowired
    private RedissonClient redissonClient;

    @Override
    public PageVO<QuestionnaireDetailVO> page(PageDTO pageDTO, QuestionnairePageDTO param) {
        PageVO<QuestionnaireDetailVO> page = PageUtil.doPage(pageDTO, () -> questionnaireMapper.list(param));
        List<QuestionnaireDetailVO> list = page.getList();
        if (!list.isEmpty()) {
            List<Long> idList = list.stream().filter(filter -> !Objects.equals(1, filter.getIsAllShop())).map(item-> Integer.valueOf(item.getId()).longValue()).collect(Collectors.toList());
            if (!idList.isEmpty()) {
                List<QuestionnaireShopCountDTO> shopCountDTOList = questionnaireShopMapper.selectAllCountByActivityIds(idList);
                Map<Long, QuestionnaireShopCountDTO> activityIdByEntity = shopCountDTOList.stream().collect(Collectors.toMap(QuestionnaireShopCountDTO::getActivityId, Function.identity()));
                for (QuestionnaireDetailVO detailVO : list) {
                    if (!Objects.equals(1, detailVO.getIsAllShop())) {
                        QuestionnaireShopCountDTO orDefault = activityIdByEntity.getOrDefault(Integer.valueOf(detailVO.getId()).longValue(), new QuestionnaireShopCountDTO());
                        detailVO.setShopIds(orDefault.getShopIds());
                        detailVO.setApplyShopNum(orDefault.getCount());
                    }
                }
            }
        }

        for (QuestionnaireDetailVO detailVO : list) {
            Integer status = detailVO.getStatus();
            if (0 == status){
                detailVO.setActivityStatus(0);
                continue;
            }
            // 筛选已启用的活动
            if (1 == status){
                Date now = new Date();
                Date beginTime = detailVO.getActivityBeginTime();
                Date endTime = detailVO.getActivityEndTime();
                // 开始时间大于当前时间 活动未开始
                if (beginTime.after(now)){
                    detailVO.setActivityStatus(2);
                    continue;
                }
                // 当前时间大于结束时间 活动已结束
                if (now.after(endTime)){
                    detailVO.setActivityStatus(4);
                    continue;
                }
                // 最后一种情况就是活动中
                detailVO.setActivityStatus(3);
            }
        }

        return page;
    }

    @Override
    public PageVO<QuestionnaireUserAnswerRecordPageVO> answerPage(PageDTO pageDTO, AnswerPageDTO param) {
        return PageUtil.doPage(pageDTO, () -> questionnaireUserAnswerRecordMapper.page(param));
    }

    @Override
    public Questionnaire getById(Long id) {
        return questionnaireMapper.getById(id);
    }

    @Override
    public QuestionnaireDetailVO getDetailById(Long id) {
        Questionnaire questionnaire = questionnaireMapper.getById(id);
        Assert.isNull(questionnaire,"问卷不存在，请检查数据再试！");

        QuestionnaireDetailVO detailVO = new QuestionnaireDetailVO();
        BeanUtil.copyProperties(questionnaire,detailVO);

        QuestionnaireGift questionnaireGift = questionnaireGiftMapper.selectByActivityId(id);
        detailVO.setQuestionnaireGift(questionnaireGift);

        QuestionnaireForm questionnaireForm = questionnaireFormMapper.getById(id);
        detailVO.setContent(questionnaireForm.getContent());
        detailVO.setFormNames(JSONObject.parseArray(questionnaireForm.getFormNames(),QuestionnaireFormNames.class));

        // 取消展示用户userIdList
/*        List<QuestionnaireUser> users = questionnaireUserMapper.selectList(new LambdaQueryWrapper<QuestionnaireUser>().eq(QuestionnaireUser::getActivityId,id));
        List<Long> userIds = users.stream().map(QuestionnaireUser::getUserId).collect(Collectors.toList());
        detailVO.setUserIds(userIds);*/

        if(questionnaire.getIsAllShop() ==0 ){
            List<QuestionnaireShop> shops = questionnaireShopMapper.selectList(new LambdaQueryWrapper<QuestionnaireShop>().eq(QuestionnaireShop::getActivityId,id));
            List<Long> shopIds = shops.stream().map(QuestionnaireShop::getShopId).collect(Collectors.toList());
            detailVO.setStoreIds(shopIds);
        }
        // 浏览次数
        int browseCount = questionnaireUserAnswerRecordMapper.selectCount(new LambdaQueryWrapper<QuestionnaireUserAnswerRecord>()
                .eq(QuestionnaireUserAnswerRecord::getActivityId,id));
        // 提交次数
        int submitCount = questionnaireUserAnswerRecordMapper.selectCount(new LambdaQueryWrapper<QuestionnaireUserAnswerRecord>()
                .eq(QuestionnaireUserAnswerRecord::getActivityId,id)
                .eq(QuestionnaireUserAnswerRecord::getSubmitted,1));
        // 未提交次数
        int unSubmitCount = questionnaireUserAnswerRecordMapper.selectCount(new LambdaQueryWrapper<QuestionnaireUserAnswerRecord>()
                .eq(QuestionnaireUserAnswerRecord::getActivityId,id)
                .eq(QuestionnaireUserAnswerRecord::getSubmitted,0));
        // 总用户数（导入人数或标签涉及人数）
        Integer allUserCount = 0;
        // 会员名单
        allUserCount = questionnaireUserMapper.selectCountByActivity(questionnaire.getId());

        // 标签逻辑变更（注释部分为旧逻辑）
        /*if (questionnaire.getUserWhiteType() == 0){

        } else {
            // 标签涉及用户数
            String userTag = questionnaire.getUserTag();
            String[] split = userTag.split(",");
            if (split.length > 0) {
                List<Long> userTagList = Arrays.stream(split).map(item -> Integer.valueOf(item).longValue()).collect(Collectors.toList());
                ServerResponseEntity<Integer> response = tagClient.countByTag(userTagList);
                Integer data = response.getData();
                if (Objects.nonNull(data)){
                    allUserCount = data;
                }
            }
        }*/

        detailVO.setSubmitCount(submitCount);
        detailVO.setBrowseCount(browseCount);
        detailVO.setAllUserCount(allUserCount);
        detailVO.setUnSubmitCount(unSubmitCount);
        return detailVO;
    }

    @Override
    public QuestionnaireDetailVO appInfo(Long id, Long storeId,Long userId) {
        Questionnaire questionnaire = questionnaireMapper.getById(id);
        Assert.isNull(questionnaire,"问卷不存在，请检查数据再试！");
        QuestionnaireDetailVO detailVO = new QuestionnaireDetailVO();
        BeanUtil.copyProperties(questionnaire,detailVO);
        //说明未登录,按照未登录文案提示报错。
        if(userId==null ){
            Assert.faild(questionnaire.getUnRegTip());
        }

        // 权限检测
        String msg = checkAuth(storeId, userId, questionnaire);
        if (StrUtil.isNotBlank(msg)){
            Assert.faild(msg);
        }

        Integer status = detailVO.getStatus();
        if (0 == status){
            detailVO.setActivityStatus(0);
        } else if (1 == status){
            // 筛选已启用的活动
            Date now = new Date();
            Date beginTime = detailVO.getActivityBeginTime();
            Date endTime = detailVO.getActivityEndTime();
            // 开始时间大于当前时间 活动未开始
            if (beginTime.after(now)){
                detailVO.setActivityStatus(2);
            } else if (now.after(endTime)){
                // 当前时间大于结束时间 活动已结束
                detailVO.setActivityStatus(4);
            } else {
                // 最后一种情况就是活动中
                detailVO.setActivityStatus(3);
            }
        }

        QuestionnaireForm questionnaireForm = questionnaireFormMapper.getById(id);
        detailVO.setContent(questionnaireForm.getContent());
        detailVO.setFormNames(JSONObject.parseArray(questionnaireForm.getFormNames(),QuestionnaireFormNames.class));

        QuestionnaireGift questionnaireGift = questionnaireGiftMapper.selectByActivityId(id);
        detailVO.setQuestionnaireGift(questionnaireGift);

        // 是否发送订阅消息
        Boolean isSendSubscribe = Boolean.TRUE;
        if (Objects.equals(questionnaire.getIsSendSubscribe(),0)){
            isSendSubscribe = Boolean.FALSE;
        }
        //如果记录不存在，保存本次浏览记录
        QuestionnaireUserAnswerRecord record = questionnaireUserAnswerRecordService.getOrSave(id, userId, storeId, Boolean.FALSE, isSendSubscribe);
        detailVO.setAnswerRecord(record);

        QuestionnaireUserAnswerRecordContent content = questionnaireUserAnswerRecordContentMapper.getById(record.getId());
        if(content!=null && StrUtil.isNotEmpty(content.getContent())){
            detailVO.setAnswerRecordContent(JSONObject.parseArray(content.getContent(),QuestionnaireUserAnswer.class));
            detailVO.setIsSubmitted(1);
        } else {
            detailVO.setIsSubmitted(0);
        }

        //问卷实物时收货地址。
        detailVO.setAddr(questionnaireUserGiftAddrMapper.getById(record.getId()));
        return detailVO;
    }

    /**
     * 权限检测
     * <p>没有权限会直接抛出异常</p>
     * @param storeId 门店ID
     * @param userId 用户ID
     * @param questionnaire 问卷信息
     */
    private String checkAuth(Long storeId, Long userId, Questionnaire questionnaire) {
        String msg = null;

        if (Objects.isNull(questionnaire)){
            msg = "问卷不存在，请检查数据再试！";
            return msg;
        }

        ServerResponseEntity<UserApiVO> serverResponse = userFeignClient.getUserById(userId);
        if(serverResponse==null || serverResponse.isFail()){
            msg = StrUtil.blankToDefault(questionnaire.getUnRegTip(),"问卷查询会员失败，请稍后再试。");
            return msg;
        }

        //不为全部门店且当前门店非适用门店
        if(questionnaire.getIsAllShop() == 0){
            int matchCount = questionnaireShopMapper.selectCount(new LambdaQueryWrapper<QuestionnaireShop>()
                    .eq(QuestionnaireShop::getActivityId, questionnaire.getId())
                    .eq(QuestionnaireShop::getShopId, storeId));
            log.info("当前访问门店：{},活动id:{}，匹配门店结果:{}",storeId,questionnaire.getId(),matchCount);
            if(matchCount<=0){
                msg = StrUtil.blankToDefault(questionnaire.getSubmitTip(),"您所访问门店未参加当前问卷活动！");
                return msg;
            }
        }

        //问卷会员名单 会员名单
        if(questionnaire.getUserWhiteType() == 0){
            int matchCount = questionnaireUserMapper.selectCount(new LambdaQueryWrapper<QuestionnaireUser>()
                    .eq(QuestionnaireUser::getActivityId, questionnaire.getId())
                    .eq(QuestionnaireUser::getUserId, userId));

            int AllCount = questionnaireUserMapper.selectCount(new LambdaQueryWrapper<QuestionnaireUser>()
                    .eq(QuestionnaireUser::getActivityId, questionnaire.getId()));

            // 非全部用户且 当前用户不在名单列表当中时
            if(AllCount > 0 && matchCount < 1){
                msg = StrUtil.blankToDefault(questionnaire.getUnInWhite(),"该问卷指定了参与人员！");
                return msg;
            }
        }

        //会员标签
        if(questionnaire.getUserWhiteType() == 1){
            //如果为标签类型，并且标签有值，判断当前会员是否符合类型
                // 标签逻辑变更（注释部分为旧逻辑）
            /*if(StrUtil.isNotEmpty(questionnaire.getUserTag())){
                List<Long> tags = Arrays.stream(StrUtil.splitToArray(questionnaire.getUserTag(),','))
                        .map(s -> Long.parseLong(s.trim())).collect(Collectors.toList());
                UserApiVO userApiVO = serverResponse.getData();
                ServerResponseEntity<Boolean> tagResponse = tagClient.isInTags(tags,userApiVO.getVipcode());
                log.info("查询问卷是否符合标签白名单，tags:{},vipcode:{},执行结果:{}",tags,userApiVO.getVipcode(),JSONObject.toJSONString(tagResponse));
                if(tagResponse==null || tagResponse.isFail() || !tagResponse.getData()){
                    msg = StrUtil.blankToDefault(questionnaire.getUnInWhite(),"该问卷指定了参与人员！");
                    return msg;
                }
            }*/


            int matchCount = questionnaireUserMapper.selectCount(new LambdaQueryWrapper<QuestionnaireUser>()
                    .eq(QuestionnaireUser::getActivityId, questionnaire.getId())
                    .eq(QuestionnaireUser::getUserId, userId));
            // 如果用户标签不为空且人数筛选小于1
            if (StrUtil.isNotBlank(questionnaire.getUserTag()) && matchCount < 1){
                msg = StrUtil.blankToDefault(questionnaire.getUnInWhite(),"该问卷指定了参与人员！");
                return msg;
            }

        }
        return msg;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Questionnaire save(QuestionnaireDTO questionnaireDTO) {
        Questionnaire questionnaire = mapperFacade.map(questionnaireDTO, Questionnaire.class);
        questionnaire.setCreateName(AuthUserContext.get().getUsername());
        questionnaire.setCreateId(AuthUserContext.get().getUserId());
        questionnaireMapper.save(questionnaire);

        //问卷表单内容
        QuestionnaireForm questionnaireForm = new QuestionnaireForm();
        questionnaireForm.setId(questionnaire.getId());
        questionnaireForm.setContent(questionnaireDTO.getContent());
        questionnaireForm.setFormNames(JSONObject.toJSONString(questionnaireDTO.getFormNames()));
        questionnaireFormMapper.save(questionnaireForm);

        //适用门店
        if(questionnaireDTO.getIsAllShop() == 0 && CollectionUtil.isNotEmpty(questionnaireDTO.getStoreIds())){
            List<QuestionnaireShop> shops = new ArrayList<>();
            for (Long storeId : questionnaireDTO.getStoreIds()) {
                QuestionnaireShop questionnaireShop = new QuestionnaireShop();
                questionnaireShop.setActivityId(questionnaire.getId());
                questionnaireShop.setShopId(storeId);
                shops.add(questionnaireShop);
            }
            questionnaireShopMapper.saveBatch(shops);
        }

        //适用人群
        // 如果名单类型 是用户导入
        if(questionnaireDTO.getUserWhiteType() == 0){
            String redisKey = questionnaireDTO.getRedisKey();
            List<QuestionnaireUserExcelVO> userExcelVOList = questionnaireExcelService.listUserVOByKey(redisKey);
            if (!userExcelVOList.isEmpty()){
                List<QuestionnaireUser> questionnaireUsers = new ArrayList<>();
                for (QuestionnaireUserExcelVO user: userExcelVOList) {
                    QuestionnaireUser questionnaireUser = new QuestionnaireUser();
                    questionnaireUser.setUserId(user.getUserId());
                    questionnaireUser.setActivityId(questionnaire.getId());
                    questionnaireUsers.add(questionnaireUser);
                }
                questionnaireUserMapper.saveBatch(questionnaireUsers);
            }

        } else if (questionnaireDTO.getUserWhiteType() == 1){
            // 如果名单类型 是标签导入
            if (StrUtil.isNotBlank(questionnaire.getUserTag())){
                String[] split = questionnaire.getUserTag().split(",");
                List<Long> tagIdList = Arrays.stream(split).map(item -> Integer.valueOf(item).longValue()).collect(Collectors.toList());
                ServerResponseEntity<List<Long>> response = tagClient.listUserIdByTag(tagIdList);
                List<Long> data = response.getData();
                List<QuestionnaireUser> questionnaireUsers = new ArrayList<>();
                for (Long userId : data) {
                    QuestionnaireUser questionnaireUser = new QuestionnaireUser();
                    questionnaireUser.setUserId(userId);
                    questionnaireUser.setActivityId(questionnaire.getId());
                    questionnaireUsers.add(questionnaireUser);
                }
                questionnaireUserMapper.saveBatch(questionnaireUsers);
            }
        }

        //礼品
        QuestionnaireGift questionnaireGift = new QuestionnaireGift();
        questionnaireGift.setActivityId(questionnaire.getId());
        questionnaireGift.setGiftId(questionnaireDTO.getGiftId());
        questionnaireGift.setGiftType(questionnaireDTO.getGiftType());
        questionnaireGift.setGiftName(questionnaireDTO.getGiftName());
        questionnaireGift.setGiftPic(questionnaireDTO.getGiftPic());
        questionnaireGift.setGameType(questionnaireDTO.getGameType());
        questionnaireGiftMapper.save(questionnaireGift);

        return questionnaire;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(QuestionnaireDTO questionnaireDTO) {
        Questionnaire dbQuestionnaire = questionnaireMapper.getById(questionnaireDTO.getId());
        if (Objects.isNull(dbQuestionnaire)) {
            Assert.faild("问卷不存在");
        }
        Integer dbUserWhiteType = dbQuestionnaire.getUserWhiteType();

        Questionnaire questionnaire = mapperFacade.map(questionnaireDTO, Questionnaire.class);
        questionnaire.setUpdateName(AuthUserContext.get().getUsername());
        questionnaire.setUpdateId(AuthUserContext.get().getUserId());
        questionnaireMapper.update(questionnaire);

        //问卷表单内容
        QuestionnaireForm questionnaireForm = new QuestionnaireForm();
        questionnaireForm.setId(questionnaire.getId());
        questionnaireForm.setContent(questionnaireDTO.getContent());
        questionnaireForm.setFormNames(JSONObject.toJSONString(questionnaireDTO.getFormNames()));
        questionnaireFormMapper.update(questionnaireForm);

        //适用门店
        questionnaireShopMapper.deleteByActivityId(questionnaire.getId());
        if(questionnaireDTO.getIsAllShop() == 0 && CollectionUtil.isNotEmpty(questionnaireDTO.getStoreIds())){
            List<QuestionnaireShop> shops = new ArrayList<>();
            for (Long storeId : questionnaireDTO.getStoreIds()) {
                QuestionnaireShop questionnaireShop = new QuestionnaireShop();
                questionnaireShop.setActivityId(questionnaire.getId());
                questionnaireShop.setShopId(storeId);
                shops.add(questionnaireShop);
            }
            questionnaireShopMapper.saveBatch(shops);
        }

        //适用人群
        // 如果名单类型 是用户导入
        if(questionnaireDTO.getUserWhiteType() == 0){
            // 如果该问卷原来是 标签导入，这个时候要进行一次原用户清除
            if (Objects.equals(dbUserWhiteType, 1)) {
                questionnaireUserMapper.deleteByActivityId(questionnaire.getId());
            }
            String redisKey = questionnaireDTO.getRedisKey();
            if (StrUtil.isNotBlank(redisKey)) {
                questionnaireUserMapper.deleteByActivityId(questionnaire.getId());
                List<QuestionnaireUserExcelVO> userExcelVOList = questionnaireExcelService.listUserVOByKey(redisKey);
                if (!userExcelVOList.isEmpty()){
                    List<QuestionnaireUser> questionnaireUsers = new ArrayList<>();
                    for (QuestionnaireUserExcelVO user: userExcelVOList) {
                        QuestionnaireUser questionnaireUser = new QuestionnaireUser();
                        questionnaireUser.setUserId(user.getUserId());
                        questionnaireUser.setActivityId(questionnaire.getId());
                        questionnaireUsers.add(questionnaireUser);
                    }
                    questionnaireUserMapper.saveBatch(questionnaireUsers);
                }
            }
        } else if (questionnaireDTO.getUserWhiteType() == 1){
            questionnaireUserMapper.deleteByActivityId(questionnaire.getId());
            // 如果名单类型 是标签导入
            if (StrUtil.isNotBlank(questionnaire.getUserTag())){
                String[] split = questionnaire.getUserTag().split(",");
                List<Long> tagIdList = Arrays.stream(split).map(item -> Integer.valueOf(item).longValue()).collect(Collectors.toList());
                ServerResponseEntity<List<Long>> response = tagClient.listUserIdByTag(tagIdList);
                List<Long> data = response.getData();
                List<QuestionnaireUser> questionnaireUsers = new ArrayList<>();
                for (Long userId : data) {
                    QuestionnaireUser questionnaireUser = new QuestionnaireUser();
                    questionnaireUser.setUserId(userId);
                    questionnaireUser.setActivityId(questionnaire.getId());
                    questionnaireUsers.add(questionnaireUser);
                }
                questionnaireUserMapper.saveBatch(questionnaireUsers);
            }
        }

        //礼品
        questionnaireGiftMapper.delete(new LambdaQueryWrapper<QuestionnaireGift>()
                .eq(QuestionnaireGift::getActivityId,questionnaire.getId()));

        QuestionnaireGift questionnaireGift = new QuestionnaireGift();
        questionnaireGift.setActivityId(questionnaire.getId());
        questionnaireGift.setGiftId(questionnaireDTO.getGiftId());
        questionnaireGift.setGiftType(questionnaireDTO.getGiftType());
        questionnaireGift.setGiftName(questionnaireDTO.getGiftName());
        questionnaireGift.setGiftPic(questionnaireDTO.getGiftPic());
        questionnaireGift.setGameType(questionnaireDTO.getGameType());
        questionnaireGiftMapper.save(questionnaireGift);
    }

    @Override
    public void enable(Long id) {
        Questionnaire questionnaire = new Questionnaire();
        questionnaire.setId(id);
        questionnaire.setStatus(1);
        questionnaire.setIsFirstEnabled(1);
        questionnaireMapper.update(questionnaire);
    }

    @Override
    public void disable(Long id) {
        Questionnaire questionnaire = new Questionnaire();
        questionnaire.setId(id);
        questionnaire.setStatus(0);
        questionnaireMapper.update(questionnaire);
    }

    @Override
    public void deleteById(Long id) {
        questionnaireMapper.deleteById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void submit(SubmitDTO submitDTO) {
        Questionnaire questionnaire = questionnaireMapper.getById(submitDTO.getActivityId());
        Assert.isNull(questionnaire,"问卷不存在,请检查数据再试！");

        Integer status = questionnaire.getStatus();
        if (0 == status){
            Assert.faild("活动未开启或已结束。");
        }
        // 筛选已启用的活动
        if (1 == status){
            Date now = new Date();
            Date beginTime = questionnaire.getActivityBeginTime();
            Date endTime = questionnaire.getActivityEndTime();
            // 开始时间大于当前时间 活动未开始
            if (beginTime.after(now)){
                Assert.faild("活动未开启或已结束。");
            }
            // 当前时间大于结束时间 活动已结束
            if (now.after(endTime)){
                Assert.faild("活动未开启或已结束。");
            }
        }

        // 是否发送订阅消息
        Boolean isSendSubscribe = Boolean.TRUE;
        if (questionnaire.getIsSendSubscribe() == 0){
            isSendSubscribe = Boolean.FALSE;
        }

        QuestionnaireUserAnswerRecord answerRecord = questionnaireUserAnswerRecordService.getOrSave(submitDTO.getActivityId(),submitDTO.getUserId(), submitDTO.getStoreId(), Boolean.FALSE, isSendSubscribe);
        if (answerRecord.getSubmitted() == 1){
            Assert.faild("请勿重复提交!");
        }
        answerRecord.setSubmitted(1);
        answerRecord.setSubmittedTime(new Date());
        questionnaireUserAnswerRecordService.update(answerRecord);

        QuestionnaireUserAnswerRecordContent userAnswerRecordContent = new QuestionnaireUserAnswerRecordContent();
        userAnswerRecordContent.setId(answerRecord.getId());
        userAnswerRecordContent.setUserId(answerRecord.getUserId());
        userAnswerRecordContent.setActivityId(answerRecord.getActivityId());
        userAnswerRecordContent.setContent(JSONObject.toJSONString(submitDTO.getAnswers()));
        questionnaireUserAnswerRecordContentMapper.save(userAnswerRecordContent);

        //todo 延时消息 检查是否领奖，如果没有领奖 发送领奖订阅消息提醒

    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void receivePrize(ReceivePrizeDTO receivePrizeDTO) {

        if (Objects.isNull(receivePrizeDTO.getUserId())){
            throw new LuckException("用户为空");
        }

        RLock lock = redissonClient.getLock(RECEIVE_KEY_PREFIX + receivePrizeDTO.getUserId());
        try {
            // 锁最大持续时间设置为 5秒
            lock.lock(5, TimeUnit.SECONDS);

            QuestionnaireUserAnswerRecord answerRecord;
            if (Objects.nonNull(receivePrizeDTO.getId())) {
                answerRecord = questionnaireUserAnswerRecordService.getById(receivePrizeDTO.getId());
            } else {
                answerRecord = questionnaireUserAnswerRecordService.getOrSave(receivePrizeDTO.getActivityId(),
                        receivePrizeDTO.getUserId(), receivePrizeDTO.getStoreId(), Boolean.FALSE, Boolean.FALSE);
            }

            if (answerRecord == null || answerRecord.getSubmitted() == 0) {
                Assert.faild("当前还未答题,不允许领奖。");
            }

            if (!receivePrizeDTO.getUserId().equals(answerRecord.getUserId())) {
                Assert.faild("当前还未答题,不允许领奖。");
            }

            if (Objects.equals(1, answerRecord.getAwarded())) {
                Assert.faild("不可重复已领奖");
            }

            Questionnaire questionnaire = questionnaireMapper.getById(answerRecord.getActivityId());
            Assert.isNull(questionnaire, "问卷不存在,请检查数据再试!");

            ServerResponseEntity<UserApiVO> userServerResponse = userFeignClient.getInsiderUserData(answerRecord.getUserId());
            if (userServerResponse == null || userServerResponse.isFail() || userServerResponse.getData() == null) {
                Assert.faild("用户不存在!");
            }
            UserApiVO userApiVO = userServerResponse.getData();

            // 当前只会有一个奖品 查询取第一个
            QuestionnaireGift questionnaireGift = questionnaireGiftMapper.selectOne(new LambdaQueryWrapper<QuestionnaireGift>()
                    .eq(QuestionnaireGift::getActivityId, answerRecord.getActivityId()));

            // 积分 直接加积分
            if (questionnaireGift.getGiftType() == 0) {
                //CRM调用查询积分，扣减积分
                PointChangeDto pointChangeDto = new PointChangeDto();
                pointChangeDto.setVipcode(userApiVO.getVipcode());
                pointChangeDto.setPoint_value(Integer.valueOf(questionnaireGift.getGiftId()));
                pointChangeDto.setPoint_channel("other");
                pointChangeDto.setPoint_type("SKX_JLJF");
                pointChangeDto.setRemark("问卷答卷奖励积分");
                pointChangeDto.setRequest_id(StrUtil.toString(answerRecord.getId()));
                pointChangeDto.setSource("问卷答卷奖励积分");
                ServerResponseEntity serverResponseEntity = crmCustomerPointFeignClient.pointChange(pointChangeDto);
                if (serverResponseEntity == null || serverResponseEntity.isFail()) {
                    log.error("问卷积分领取失败，执行参数：{},失败原因:{}", JSONObject.toJSONString(pointChangeDto), JSONObject.toJSONString(serverResponseEntity));
                    Assert.faild("积分领取失败,请联系客服");

                }
            } else if (questionnaireGift.getGiftType() == 1) {
                //优惠券 派发优惠券
                //赠送券
                String[] split = questionnaireGift.getGiftId().split(",");
                for (String giftId : split) {
                    ReceiveCouponDTO receiveCouponDTO = new ReceiveCouponDTO();
                    receiveCouponDTO.setCouponId(Integer.valueOf(giftId).longValue());
                    receiveCouponDTO.setActivityId(answerRecord.getActivityId());
                    receiveCouponDTO.setActivitySource(ActivitySourceEnum.DRAW_ACTIVITY.value());
                    receiveCouponDTO.setUserId(userApiVO.getUserId());
                    ServerResponseEntity serverResponse = tCouponFeignClient.receive(receiveCouponDTO);
                    if (serverResponse == null || serverResponse.isFail()) {
                        log.error("问卷优惠券领取失败，执行参数：{},失败原因:{}", JSONObject.toJSONString(receiveCouponDTO), JSONObject.toJSONString(serverResponse));
                        Assert.faild("派发优惠券失败，请联系客服");

                    }
                }
            } else if (questionnaireGift.getGiftType() == 2) {
                //抽奖这里没有抽奖次数的概念，是在抽奖游戏活动中判断是否可以抽奖。 不做任何操作

            } else if (questionnaireGift.getGiftType() == 3) {
                //实物  这里不做任何操作。只是修改记录的奖品发放状态。

            } else if (questionnaireGift.getGiftType() == -1) {
                // 无奖品 不做操作
            }

            answerRecord.setAwarded(1);
            answerRecord.setAwardedTime(new Date());
            questionnaireUserAnswerRecordService.update(answerRecord);
            questionnaireUserGiftLogService.saveUserGiftLog(receivePrizeDTO.getUserId(), questionnaireGift);

            // 是否发送订阅消息
            if (questionnaire.getIsSendSubscribe() == 1){
                sendSubscriptMessage(questionnaire, questionnaireGift, answerRecord.getUserId());
            }

        } catch (Exception e ){
            if (e instanceof LuckException){
                e.printStackTrace();
                throw new LuckException(e.getMessage());
            }else {
                throw e;
            }
        } finally {
            if(lock.isHeldByCurrentThread()){
                lock.unlock();
            }
        }

    }

    private void sendSubscriptMessage(Questionnaire questionnaire,QuestionnaireGift gift,Long userId){
        String giftName = "";
        boolean isSend = true;
        //礼品类型 -1无奖品 0积分 1优惠券 2抽奖 3实物
        if(gift.getGiftType() == 0) {
            giftName = gift.getGiftId()+" 积分";
        }else if(gift.getGiftType() == 1){
            giftName = "优惠券";
        }else if(gift.getGiftType() == 2) {
            giftName = "抽奖机会";
        }else if (gift.getGiftType() == 3){
            giftName = gift.getGiftName();
        } else if (gift.getGiftType() == -1) {
            giftName = "无奖品";
            isSend = false;
        }
        String tip = StrUtil.format("参与问卷可获得:{}",giftName);
        final String finalGiftName = giftName;
        //查询用户是否接受订阅，并发送订阅消息
        //查询问卷活动开始订阅模版
        List<String> userids = new ArrayList<>();
        userids.add(StrUtil.toString(userId));
        ServerResponseEntity<WeixinMaSubscriptTmessageVO> subscriptResp = wxMaApiFeignClient.getinsIderSubscriptTmessage(MaSendTypeEnum.QUESTIONNAIRE_AWARDED.getValue(), userids);
        log.info("问卷领奖: {}", JSONObject.toJSONString(subscriptResp));
        if (subscriptResp.isSuccess()) {
            //获取订阅用户
            WeixinMaSubscriptTmessageVO weixinMaSubscriptTmessageVO = subscriptResp.getData();
            List<WeixinMaSubscriptUserRecordVO> userRecords = weixinMaSubscriptTmessageVO.getUserRecords();
            if (!CollectionUtils.isEmpty(userRecords)){
                List<WeixinMaSubscriptTmessageSendVO> sendList = userRecords.stream().map(user -> {
                    /**
                     * 值替换
                     * 1会员业务 2优惠券业务 3订单业务 4退单业务 5线下活动  6积分商城 7每日签到 8直播通知 9微客审核
                     * 可以替换的模版为固定的， 根据对应类型在参考 mall4cloud.weixin_ma_subscript_tmessage_optional_value
                     * 当前积分商城 场景下 活动名称{activityName}、开始时间{beginTime}、结束时间{endTime} 温馨提示 {tip} 奖品{prize} 活动内容{activityContent}
                     * 构建参数map.
                     */
                    Map<String, String> paramMap = new HashMap();
                    paramMap.put("{activityName}", questionnaire.getTitle());
                    paramMap.put("{beginTime}", DateUtils.dateToString(questionnaire.getActivityBeginTime()));
                    paramMap.put("{endTime}", DateUtils.dateToString(questionnaire.getActivityEndTime()));
                    paramMap.put("{prize}", finalGiftName);
                    paramMap.put("{activityContent}", tip);
                    paramMap.put("{tip}", questionnaire.getGiftGrantTip());

                    List<WeixinMaSubscriptTmessageSendVO.MsgData> msgDataList = weixinMaSubscriptTmessageVO.getTmessageValues().stream().map(t -> {
                        WeixinMaSubscriptTmessageSendVO.MsgData msgData = new WeixinMaSubscriptTmessageSendVO.MsgData();
                        msgData.setName(t.getTemplateValueName());
                        msgData.setValue(StrUtil.isEmpty(paramMap.get(t.getTemplateValue())) ? t.getTemplateValue() : paramMap.get(t.getTemplateValue()));
                        return msgData;
                    }).collect(Collectors.toList());

                    wxMaApiFeignClient.updateUserRecordId(user.getId());
                    WeixinMaSubscriptTmessageSendVO sendVO = new WeixinMaSubscriptTmessageSendVO();
                    sendVO.setUserSubscriptRecordId(user.getId());
                    sendVO.setData(msgDataList);
                    sendVO.setPage(weixinMaSubscriptTmessageVO.getPage());
                    return sendVO;
                }).collect(Collectors.toList());
                log.info("问卷领奖，待发送列表集合:{}",JSONObject.toJSONString(sendList));
                // 奖品类型为无奖品就不需要发送消息了
                if (isSend) {
                    sendMaTemplateMessage.syncSend(sendList);
                    log.info("问卷领奖消息发送成功");
                }
            }
        }else {
            log.error("查询问卷领奖模版失败，返回值为：{}", JSONObject.toJSONString(subscriptResp));
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void fillShippingAddress(ShippingAddressDTO shippingAddressDTO) {
        QuestionnaireUserAnswerRecord answerRecord;
        if (Objects.isNull(shippingAddressDTO.getId())){
            answerRecord = questionnaireUserAnswerRecordMapper.selectOneByActivityIdInAndUserId(shippingAddressDTO.getActivityId(),
                    shippingAddressDTO.getUserId());
        } else {
            answerRecord = questionnaireUserAnswerRecordService.getById(shippingAddressDTO.getId());
        }

        if(answerRecord == null || answerRecord.getSubmitted()==0){
            Assert.faild("当前还未答题，不允许领奖。");
        }
        if(answerRecord.getShipped() == 1){
            Assert.faild("已经发货，不允许再填写收货地址");
        }

        Questionnaire questionnaire = questionnaireMapper.getById(answerRecord.getActivityId());
        Assert.isNull(questionnaire,"问卷不存在，请检查数据再试！");

        //当前只会有一个奖品 查询取第一个
        QuestionnaireGift questionnaireGift = questionnaireGiftMapper.selectList(new LambdaQueryWrapper<QuestionnaireGift>()
                .eq(QuestionnaireGift::getActivityId,answerRecord.getActivityId())).get(0);
        //如果未设置奖品或者奖品非实物奖品不需要发货
        if(questionnaireGift==null || questionnaireGift.getGiftType()!=3){
            Assert.faild("当前非实物奖励，不需要填写收货地址。");
        }

        // 设置地址填写状态
        QuestionnaireUserAnswerRecord recordUpdateBo = new QuestionnaireUserAnswerRecord();
        recordUpdateBo.setId(answerRecord.getId());
        recordUpdateBo.setIsSetAddr(1);
        questionnaireUserAnswerRecordMapper.updateById(recordUpdateBo);

        QuestionnaireUserGiftAddr giftAddr = questionnaireUserGiftAddrMapper.selectOneByActivityIdAndUserId(shippingAddressDTO.getActivityId(), shippingAddressDTO.getUserId());
        QuestionnaireUserGiftAddr questionnaireUserGiftAddr = new QuestionnaireUserGiftAddr();
        BeanUtil.copyProperties(shippingAddressDTO,questionnaireUserGiftAddr);
        if (Objects.nonNull(giftAddr)){
            questionnaireUserGiftAddr.setId(giftAddr.getId());
            questionnaireUserGiftAddrMapper.updateById(questionnaireUserGiftAddr);
        } else {
            questionnaireUserGiftAddrMapper.save(questionnaireUserGiftAddr);
        }
    }

    @Override
    public PageVO<QuestionnaireUserUnSubmitVO> pageCountUserUnSubmit(PageDTO pageDTO, Long activityId) {
        return PageUtil.doPage(pageDTO, () -> questionnaireUserAnswerRecordMapper.selectCountUnSubmit(activityId));
    }

    @Override
    public Boolean checkUserAuth(Long id, Long storeId, Long userId) {
        Questionnaire questionnaire = questionnaireMapper.getById(id);
        String msg = checkAuth(storeId, userId, questionnaire);
        if (StrUtil.isNotBlank(msg)){
            log.info(msg);
            return Boolean.FALSE;
        } else {
            Integer count = questionnaireUserAnswerRecordContentMapper.selectCountByUserIdAndActivityId(userId, id);
            if (count > 0){
                log.info("已填写过表单");
                return Boolean.FALSE;
            }
        }
        return Boolean.TRUE;
    }

    @Override
    public QuestionnaireUserGiftAddrVO userGiftAddrInfo(Long activityId, Long userId) {
        QuestionnaireUserGiftAddr questionnaireUserGiftAddr = questionnaireUserGiftAddrMapper.selectOneByActivityIdAndUserId(activityId, userId);
        return mapperFacade.map(questionnaireUserGiftAddr, QuestionnaireUserGiftAddrVO.class);
    }

    @Override
    public void exportUser(String redisKey, Long activityId, HttpServletResponse response) {
        if (StrUtil.isNotBlank(redisKey)) {
            List<QuestionnaireUserExcelVO> userExcelVOS = questionnaireExcelService.listUserVOByKey(redisKey);
            if (userExcelVOS.isEmpty()){
                throw new LuckException("名单为空");
            }
            ExcelUtil.soleExcel(response, userExcelVOS, "生效名单", 0, new int[0], QuestionnaireUserExcelVO.class);
        } else {
            List<QuestionnaireUser> questionnaireUserList = questionnaireUserMapper.selectListByActivityId(activityId);
            if (!questionnaireUserList.isEmpty()){
                List<Long> userIdList = new ArrayList<>();
                List<QuestionnaireUserExcelVO> questionnaireUserExcelVOList = new ArrayList<>();
                for (QuestionnaireUser questionnaireUser : questionnaireUserList) {
                    userIdList.add(questionnaireUser.getUserId());
                    if (userIdList.size() >= 2000){
                        addUserList(userIdList, questionnaireUserExcelVOList);
                    }
                }
                addUserList(userIdList, questionnaireUserExcelVOList);;
                // 下载中心
                String time = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
                String fileName = SkqUtils.getExcelFilePath()  + "/" + time + "问卷调查生效名单" + ".xlsx";
                Long downLoadHisId = questionnaireExcelService.getDownLoadHisId(time, "问卷调查生效名单:" + activityId);
                ExcelWriter excelWriter = ExcelUtil.getExcelWriter(fileName);
                WriteSheet writeSheet = ExcelUtil.getWriteSheet("sheet", QuestionnaireUserExcelVO.class);
                ExcelUtil.write(questionnaireUserExcelVOList, excelWriter, writeSheet, true);
                questionnaireExcelService.uploadFileAndFinishDown(fileName, time, downLoadHisId, questionnaireUserExcelVOList.size(), "问卷调查生效名单:" + activityId);
            } else {
                throw new LuckException("名单为空");
            }

        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deliverGift(QuestionnaireUserGiftAddrDTO dto) {
        Long id = dto.getId();
        String logisticsCompany = dto.getLogisticsCompany();
        String logisticsNumber = dto.getLogisticsNumber();

        if (Objects.isNull(id) || StrUtil.isBlank(logisticsCompany) || StrUtil.isBlank(logisticsNumber)){
            throw new LuckException("参数错误");
        }

        QuestionnaireUserGiftAddr questionnaireUserGiftAddr = questionnaireUserGiftAddrMapper.getById(id);
        if (Objects.isNull(questionnaireUserGiftAddr)){
            throw new LuckException("地址信息不存在");
        }

        QuestionnaireUserAnswerRecord questionnaireUserAnswerRecord = questionnaireUserAnswerRecordMapper
                .selectOneByActivityIdInAndUserId(questionnaireUserGiftAddr.getActivityId(),
                        questionnaireUserGiftAddr.getUserId());
        if (Objects.isNull(questionnaireUserAnswerRecord)){
            throw new LuckException("用户答题记录异常");
        }
        if (Objects.equals(questionnaireUserAnswerRecord.getShipped(), 1)){
            throw new LuckException("不能重复发货");
        } else {
            QuestionnaireUserAnswerRecord recordUpdateBo = new QuestionnaireUserAnswerRecord();
            recordUpdateBo.setId(questionnaireUserAnswerRecord.getId());
            recordUpdateBo.setShipped(1);
            questionnaireUserAnswerRecordMapper.updateById(recordUpdateBo);
        }

        QuestionnaireUserGiftAddr updateBo = new QuestionnaireUserGiftAddr();
        updateBo.setId(id);
        updateBo.setLogisticsCompany(logisticsCompany);
        updateBo.setLogisticsNumber(logisticsNumber);
        questionnaireUserGiftAddrMapper.update(updateBo);

    }

    @Override
    public QuestionnaireDetailVO simpleInfo(Long id) {
        Questionnaire questionnaire = questionnaireMapper.getById(id);
        Assert.isNull(questionnaire,"问卷不存在，请检查数据再试！");
        QuestionnaireDetailVO detailVO = new QuestionnaireDetailVO();
        BeanUtil.copyProperties(questionnaire,detailVO);
        Integer status = detailVO.getStatus();
        if (0 == status){
            detailVO.setActivityStatus(0);
        } else if (1 == status){
            // 筛选已启用的活动
            Date now = new Date();
            Date beginTime = detailVO.getActivityBeginTime();
            Date endTime = detailVO.getActivityEndTime();
            // 开始时间大于当前时间 活动未开始
            if (beginTime.after(now)){
                detailVO.setActivityStatus(2);
            } else if (now.after(endTime)){
                // 当前时间大于结束时间 活动已结束
                detailVO.setActivityStatus(4);
            } else {
                // 最后一种情况就是活动中
                detailVO.setActivityStatus(3);
            }
        }

        QuestionnaireForm questionnaireForm = questionnaireFormMapper.getById(id);
        detailVO.setContent(questionnaireForm.getContent());
        detailVO.setFormNames(JSONObject.parseArray(questionnaireForm.getFormNames(),QuestionnaireFormNames.class));

        return detailVO;
    }

    @Override
    public QuestionnaireResolveExcelVO updateCacheUser(QuestionnaireUpdateExcelDTO questionnaireUpdateExcelDTO) {
        QuestionnaireResolveExcelVO vo = new QuestionnaireResolveExcelVO();
        String redisKey = questionnaireUpdateExcelDTO.getRedisKey();
        Long activityId = questionnaireUpdateExcelDTO.getActivityId();
        // 删除已保存的活动用户名单
        if (Objects.nonNull(activityId)){
            // 是否删除所有用户
            if (questionnaireUpdateExcelDTO.getIsRemoveAllUser()){
                questionnaireUserMapper.deleteByActivityId(activityId);
            } else {
                questionnaireUserMapper.deleteByActivityIdAndUserId(activityId, questionnaireUpdateExcelDTO.getRemoveUserIds());
            }
            Integer count = questionnaireUserMapper.selectCountByActivity(activityId);
            vo.setCount(count);
        } else {
            // 删除缓存的excel用户名单
            List<QuestionnaireUserExcelVO> userExcelVOList = questionnaireExcelService.listUserVOByKey(redisKey);
            vo.setRedisKey(redisKey);
            if (userExcelVOList.isEmpty()) {
                throw new LuckException("缓存名单数据为空");
            } else {
                List<QuestionnaireUserExcelVO> newUserExcelVO = new ArrayList<>();
                // 是否删除所有用户
                if (questionnaireUpdateExcelDTO.getIsRemoveAllUser()){
                    questionnaireExcelService.updateExcelCache(redisKey, newUserExcelVO);
                } else {
                    Map<Long, QuestionnaireUserExcelVO> excelVOMapUserId = userExcelVOList.stream().collect(Collectors.toMap(QuestionnaireUserExcelVO::getUserId, Function.identity()));
                    List<Long> removeUserIds = questionnaireUpdateExcelDTO.getRemoveUserIds();
                    for (Long userId : removeUserIds) {
                        excelVOMapUserId.remove(userId);
                    }
                    newUserExcelVO = new ArrayList<>(excelVOMapUserId.values());
                    questionnaireExcelService.updateExcelCache(redisKey, newUserExcelVO);
                }
                vo.setCount(newUserExcelVO.size());
            }
        }
        return vo;
    }

    @Override
    public PageVO<QuestionnaireUserExcelVO> pageCacheUser(String redisKey, PageDTO pageDTO, String phone, Long activityId) {
        if (Objects.nonNull(activityId)){
            return getQuestionnaireUserExcelVOPageVO(pageDTO, phone, activityId);
        }

        List<QuestionnaireUserExcelVO> userExcelVOList = questionnaireExcelService.listUserVOByKey(redisKey);
        PageVO<QuestionnaireUserExcelVO> pageVO = new PageVO<>();
        if (StrUtil.isNotBlank(phone)){
            userExcelVOList = userExcelVOList.stream()
                    .filter(item -> StrUtil.equals(item.getPhone(), phone))
                    .collect(Collectors.toList());

        }

        pageVO.setTotal((long) userExcelVOList.size());
        Integer pages = userExcelVOList.size() / pageDTO.getPageSize() + (userExcelVOList.size() % pageDTO.getPageSize() > 0? 1: 0);
        pageVO.setPages(pages);

        List<QuestionnaireUserExcelVO> pageList = new ArrayList<>();
        int iNum = (pageDTO.getPageNum() - 1) * pageDTO.getPageSize();
        int iSize = pageDTO.getPageNum() * pageDTO.getPageSize();

        for (int i = iNum; i < iSize; i ++){
            if (i >= userExcelVOList.size()){
                continue;
            }
            pageList.add(userExcelVOList.get(i));
        }
        pageVO.setList(pageList);
        return pageVO;
    }

    @Override
    public void point(Long userId, Long activityId) {
        questionnaireUserAnswerRecordService.pointBrowseCount(userId, activityId);
    }

    private PageVO<QuestionnaireUserExcelVO> getQuestionnaireUserExcelVOPageVO(PageDTO pageDTO, String phone, Long activityId) {
        // 需要手机号查询
        if (StrUtil.isNotBlank(phone)){
            ServerResponseEntity<List<UserApiVO>> listServerResponseEntity = userFeignClient.listByPhone(Collections.singletonList(phone));
            List<UserApiVO> data = listServerResponseEntity.getData();
            Map<Long, UserApiVO> userApiVOMap = data.stream().collect(Collectors.toMap(UserApiVO::getUserId, Function.identity()));
            if (!data.isEmpty()){
                List<Long> userIdList = data.stream().map(UserApiVO::getUserId).collect(Collectors.toList());
                PageVO<QuestionnaireUser> page = PageUtil.doPage(pageDTO, () -> questionnaireUserMapper.selectListByActivityIdAndUserIds(activityId, userIdList));
                List<QuestionnaireUser> list = page.getList();
                List<QuestionnaireUserExcelVO> excelVOList = new ArrayList<>();
                for (QuestionnaireUser questionnaireUser : list) {
                    UserApiVO userApiVO = userApiVOMap.get(questionnaireUser.getUserId());
                    if (Objects.nonNull(userApiVO)){
                        QuestionnaireUserExcelVO map = mapperFacade.map(userApiVO, QuestionnaireUserExcelVO.class);
                        excelVOList.add(map);
                    }
                }

                PageVO<QuestionnaireUserExcelVO> pageVO = new PageVO<>();
                pageVO.setPages(page.getPages());
                pageVO.setTotal(page.getTotal());
                pageVO.setList(excelVOList);
                return pageVO;
            } else {
                PageVO<QuestionnaireUserExcelVO> vo = new PageVO<>();
                vo.setPages(0);
                vo.setTotal(0L);
                return vo;
            }

        } else {
            // 不需要手机号查询
            PageVO<QuestionnaireUser> page = PageUtil.doPage(pageDTO, () -> questionnaireUserMapper.selectListByActivityId(activityId));
            List<QuestionnaireUser> questionnaireUserList = page.getList();
            List<Long> userIdList = questionnaireUserList.stream().map(QuestionnaireUser::getUserId).collect(Collectors.toList());
            ServerResponseEntity<List<UserApiVO>> userByUserIds = userFeignClient.getUserByUserIds(userIdList);
            List<UserApiVO> data = userByUserIds.getData();
            List<QuestionnaireUserExcelVO> questionnaireUserExcelVOList = mapperFacade.mapAsList(data, QuestionnaireUserExcelVO.class);

            PageVO<QuestionnaireUserExcelVO> pageVO = new PageVO<>();
            pageVO.setPages(page.getPages());
            pageVO.setTotal(page.getTotal());
            pageVO.setList(questionnaireUserExcelVOList);
            return pageVO;
        }
    }

    private void addUserList(List<Long> userIdList, List<QuestionnaireUserExcelVO> questionnaireUserExcelVOList) {
        if (userIdList.isEmpty()){
            return;
        }
        ServerResponseEntity<List<UserApiVO>> userByUserIds = userFeignClient.getUserByUserIds(userIdList);
        userIdList.clear();
        List<UserApiVO> data = userByUserIds.getData();
        List<QuestionnaireUserExcelVO> userExcelVOList = mapperFacade.mapAsList(data, QuestionnaireUserExcelVO.class);
        questionnaireUserExcelVOList.addAll(userExcelVOList);
    }

}
