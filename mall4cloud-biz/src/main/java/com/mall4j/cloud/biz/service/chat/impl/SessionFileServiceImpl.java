package com.mall4j.cloud.biz.service.chat.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mall4j.cloud.api.biz.dto.cp.chat.SessionFileDTO;
import com.mall4j.cloud.api.biz.vo.SessionFileVO;
import com.mall4j.cloud.api.platform.dto.StaffQueryDTO;
import com.mall4j.cloud.api.platform.feign.StaffFeignClient;
import com.mall4j.cloud.api.platform.vo.StaffVO;
import com.mall4j.cloud.api.user.crm.model.UpdateTag;
import com.mall4j.cloud.api.user.crm.model.UpdateTagModel;
import com.mall4j.cloud.api.user.dto.UserStaffCpRelationSearchDTO;
import com.mall4j.cloud.api.user.feign.UserStaffCpRelationFeignClient;
import com.mall4j.cloud.api.user.vo.UserStaffCpRelationListVO;
import com.mall4j.cloud.api.biz.dto.cp.NotifyMsgTemplateDTO;
import com.mall4j.cloud.biz.mapper.chat.*;
import com.mall4j.cloud.biz.mapper.cp.CustGroupMapper;
import com.mall4j.cloud.biz.model.chat.*;
import com.mall4j.cloud.biz.model.cp.Config;
import com.mall4j.cloud.biz.model.cp.CpCustGroup;
import com.mall4j.cloud.biz.service.chat.SessionFileService;
import com.mall4j.cloud.biz.service.cp.ConfigService;
import com.mall4j.cloud.biz.service.cp.CustGroupService;
import com.mall4j.cloud.biz.service.cp.WxCpPushService;
import com.mall4j.cloud.biz.vo.chat.KeywordVO;
import com.mall4j.cloud.biz.vo.chat.SessionTimeOutVO;
import com.mall4j.cloud.biz.vo.chat.TimeOutLogVO;
import com.mall4j.cloud.biz.wx.wx.exception.SessionRequestMsg;
import com.mall4j.cloud.biz.wx.wx.util.MsgAuditAccessToken;
import com.mall4j.cloud.biz.wx.wx.util.RequestUtil;
import com.mall4j.cloud.biz.wx.wx.util.SessionDecryptUtil;
import com.mall4j.cloud.biz.wx.wx.util.WechatUtils;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.util.PageUtil;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.common.util.DateUtil;
import com.mall4j.cloud.common.util.LambdaUtils;
import com.mongodb.client.gridfs.GridFSUploadStream;
import com.tencent.wework.Finance;
import lombok.extern.slf4j.Slf4j;
import ma.glasnost.orika.MapperFacade;
import me.chanjar.weixin.cp.bean.msgaudit.WxCpCheckAgreeRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 会话存档记录实现类
 *
 */
@Slf4j
@Service
public class SessionFileServiceImpl implements SessionFileService {

    @Autowired
    private MongoTemplate mongoTemplate;
    @Autowired
    private SessionSearchDbDao searchDbDao;
    @Autowired
    private ConfigService configService;
    @Autowired
    private MongoGridFSDao mongoGridFSDao;
    @Autowired
    private KeywordHitMapper hitMapper;
    @Autowired
    private KeywordMapper keywordMapper;
    @Autowired
    private UserSessionAgreeMapper agreeMapper;
    @Autowired
    private UserStaffCpRelationFeignClient cpRelationFeignClient;
    @Autowired
    private TimeOutLogMapper logMapper;
    @Autowired
    private SessionTimeOutMapper outMapper;
    @Autowired
    private MapperFacade mapperFacade;
    @Autowired
    WorkDateMapper dateMapper;
    @Autowired
    EndStatementMapper statementMapper;
    @Autowired
    WxCpPushService pushService;
    @Autowired
    private StaffFeignClient feignClient;
//    @Autowired
//    private CrmManager crmManager;
    @Autowired
    private CustGroupMapper custGroupMapper;
    @Autowired
    private CustGroupService custGroupService;

    private static long sdk = 0;
    private static long timeout = 10 * 60 * 1000;

    @Override
    public PageVO<SessionFileVO> page(PageDTO pageDTO, SessionFileDTO dto) {
        int skip = (pageDTO.getPageNum()-1) * pageDTO.getPageSize();
        //int size = pageDTO.getPageSize();
        Query query = new Query();
        Query lastQuery = new Query();
        if(StringUtils.isNotBlank(dto.getFrom()) && StringUtils.isNotBlank(dto.getTolist())){
            Criteria from = new Criteria().andOperator(Criteria.where("from").is(dto.getFrom()),Criteria.where("tolist").regex(".*"+dto.getTolist()+".*"));
            Criteria toList = new Criteria().andOperator(Criteria.where("tolist").regex(".*"+dto.getFrom()+".*"),Criteria.where("from").is(dto.getTolist()));
            query.addCriteria(new Criteria().orOperator(from,toList));
            lastQuery.addCriteria(new Criteria().orOperator(from,toList));
        }
        if(StringUtils.isNotBlank(dto.getStartTime())&&StringUtils.isNotBlank(dto.getEndTime())){//根据时间段筛选查询条件
            Date start = DateUtil.dateTime("yyyy-MM-dd HH:mm:ss",dto.getStartTime());
            Date end = DateUtil.dateTime("yyyy-MM-dd HH:mm:ss",dto.getEndTime());
            query.addCriteria(Criteria.where("createTime").gte(start).lte(end));
            query.with(Sort.by(Sort.Direction.DESC,"createTime"));
            //query.skip(skip);
            /*if(dto.getPageUpDown()==0){
                query.addCriteria(Criteria.where("createTime").gte(start));
                //query.addCriteria(Criteria.where("createTime").gte(start).lte(end));
                query.with(Sort.by(Sort.Direction.ASC,"createTime"));
            }else{
                query.addCriteria(Criteria.where("createTime").lte(end));
                //query.addCriteria(Criteria.where("createTime").gte(start).lte(end));
                query.with(Sort.by(Sort.Direction.ASC,"createTime"));
            }*/
        }/*else if(StringUtils.isNotBlank(dto.getStartTime())&&StringUtils.isBlank(dto.getEndTime())){//非时间段查询
            Date start = DateUtil.dateTime("yyyy-MM-dd HH:mm:ss",dto.getStartTime());
            //Date end = DateUtil.dateTime("yyyy-MM-dd HH:mm:ss",dto.getEndTime());
            //query.addCriteria(Criteria.where("createTime").gte(start).lte(end));
            //query.with(Sort.by(Sort.Direction.ASC,"createTime"));
            if(dto.getPageUpDown()==0){
                query.addCriteria(Criteria.where("createTime").lte(start));
                query.with(Sort.by(Sort.Direction.DESC,"createTime"));
            }else{
                query.addCriteria(Criteria.where("createTime").gte(start));
                query.with(Sort.by(Sort.Direction.ASC,"createTime"));
            }
        }*/else{
            query.with(Sort.by(Sort.Direction.DESC,"createTime"));
            /*if(StringUtils.isBlank(dto.getRoomid())){
                lastQuery.limit(1);
                lastQuery.addCriteria(Criteria.where("roomid").exists(false));
                lastQuery.with(Sort.by(Sort.Direction.DESC,"createTime"));
                //查询最新的消息
                SessionFile user = mongoTemplate.findOne(lastQuery, SessionFile.class);
                if(user != null){
                    Date lastTime = user.getCreateTime();
                    String date = DateUtil.dateTime(lastTime);
                    String startTime = date+" 00:00:00";
                    String endTime = date+" 23:59:59";
                    Date start = DateUtil.dateTime("yyyy-MM-dd HH:mm:ss",endTime);
                    Date end = DateUtil.dateTime("yyyy-MM-dd HH:mm:ss",endTime);
                    log.info("会话记录分页接口-开始时间"+startTime+"====结束时间"+endTime);
                    query.addCriteria(Criteria.where("createTime").lte(start));
                    //query.addCriteria(Criteria.where("createTime").gte(start).lte(end));
                    //query.with(Sort.by(Sort.Direction.ASC,"createTime"));
                    query.with(Sort.by(Sort.Direction.DESC,"createTime"));
                }
            }*/
        }
        if(StringUtils.isNotBlank(dto.getContent())){
            query.addCriteria(Criteria.where("content").regex(".*"+dto.getContent()+".*"));
        }
        if(StringUtils.isNotBlank(dto.getMsgtype())){
            query.addCriteria(Criteria.where("msgtype").is(dto.getMsgtype()));
        }else{
            query.addCriteria(Criteria.where("msgtype").ne("revoke"));
        }
        if(StringUtils.isNotBlank(dto.getRoomid())){
            query.addCriteria(Criteria.where("roomid").is(dto.getRoomid()));
        }else{
            query.addCriteria(Criteria.where("roomid").exists(false));
        }
        //先查询总条数
        long total = mongoTemplate.count(query, SessionFile.class);
        log.info("会话记录分页接口-先查询总条数：{}",total);
        query.skip(skip);
        query.limit(pageDTO.getPageSize());
        log.info("会话记录分页接口-查询入参：{}",JSON.toJSONString(query));
        //查询会话列表
        List<SessionFile> file = mongoTemplate.find(query, SessionFile.class);
        List<SessionFileVO> fileVO=mapperFacade.mapAsList(file,SessionFileVO.class);
        List<String> staffId = fileVO.stream().map(SessionFileVO::getFrom).collect(Collectors.toList());
        StaffQueryDTO staffDto = new StaffQueryDTO();
        staffDto.setQiWeiUserIds(staffId);
        ServerResponseEntity<List<StaffVO>> staffList = feignClient.findByStaffQueryDTO(staffDto);
        ServerResponseEntity.checkResponse(staffList);
        Map<String,StaffVO> staffVOMap = LambdaUtils.toMap(staffList.getData().stream().filter(item->StrUtil.isNotEmpty(item.getQiWeiUserId())),StaffVO::getQiWeiUserId);
        UserStaffCpRelationSearchDTO userStaffCpRelationSearchDTO = new UserStaffCpRelationSearchDTO();
        userStaffCpRelationSearchDTO.setPageSize(10);
        userStaffCpRelationSearchDTO.setPageNum(1);
        userStaffCpRelationSearchDTO.setQiWeiUserIds(staffId);
        ServerResponseEntity<List<UserStaffCpRelationListVO>> cpListVO = cpRelationFeignClient.getUserStaffRelBy(userStaffCpRelationSearchDTO);
        ServerResponseEntity.checkResponse(cpListVO);
        Map<String,UserStaffCpRelationListVO> relationListVOMap = LambdaUtils.toMap(cpListVO.getData().stream().filter(item->StrUtil.isNotEmpty(item.getQiWeiUserId())),UserStaffCpRelationListVO::getQiWeiUserId);
        for (SessionFileVO s: fileVO) {
            if(StringUtils.isNotBlank(s.getTolist())){
                s.setTolist(s.getTolist().replace("[","").replace("]",""));
            }
            if(staffVOMap.containsKey(s.getFrom())){
                s.setStaffAvatar(staffVOMap.get(s.getFrom()).getAvatar());
                s.setStaffName(staffVOMap.get(s.getFrom()).getStaffName());
            }
            if(relationListVOMap.containsKey(s.getFrom())){
                s.setUserAvatar(relationListVOMap.get(s.getFrom()).getAvatar());
                s.setUserName(relationListVOMap.get(s.getFrom()).getQiWeiNickName());
            }
        }
        /*if(StringUtils.isNotBlank(dto.getStartTime())&&StringUtils.isNotBlank(dto.getEndTime())){
            if(StringUtils.isNotBlank(dto.getEndTime())){
                Date end = DateUtil.dateTime("yyyy-MM-dd HH:mm:ss",dto.getEndTime());
                for (int i =fileVO.size()-1;i>=0;i--) {
                    if(fileVO.get(i).getCreateTime().compareTo(end)>0){
                        System.out.println(fileVO.get(i).getCreateTime());
                        fileVO.remove(i);
                    }
                }
            }
        }*/
        /*if(pageDTO.getPageSize()> fileVO.size()){
            total = 1;
        }else{
            total = 2;
        }
        if(dto.getPageUpDown()==1){
            if(size > fileVO.size()){
                pageDTO.setPageSize(1);
            }else{
                pageDTO.setPageSize(2);
            }
        }else{
            if(size > fileVO.size()){
                total = 1;
            }else{
                total = 2;
            }
            if(StringUtils.isBlank(dto.getStartTime())){
                pageDTO.setPageSize(1);
            }else if(pageDTO.getPageNum()==2){
                pageDTO.setPageSize(1);
            }else{
                pageDTO.setPageSize(2);
            }
        }*/
        fileVO.sort(Comparator.comparing(SessionFileVO::getCreateTime));
        /*for (SessionFileVO f:fileVO) {
            System.out.println(f.getCreateTime()+"===");
        }*/
        return PageUtil.mongodbPage(pageDTO, fileVO,total);
    }

    @Override
    public void save(SessionFile sessionFile) {
        querySession();
    }

    private void querySession(){
        boolean flag = querySessionAndSave();
        if(flag){
            querySession();
        }
    }

    private void initSDK(){
        if (sdk == 0) {
            Config config = configService.getConfig();
            sdk = Finance.NewSdk();
            Finance.Init(sdk,"wwf98894bf27379e85","NIOroyVVW2yJWyRJAS7HjkDkxOlnNqnqSjWeI8qrjEE");
//            Finance.Init(sdk,config.getSessionCpId(),config.getSessionSecret());
        }
    }

    private boolean querySessionAndSave() {

        initSDK();

        List<SessionFile> list = new ArrayList();
        Query query = new Query();
        query.limit(1);
        query.with(Sort.by(Sort.Direction.DESC,"seq"));
        //查询会话列表
        List<SessionFile> fileVO = mongoTemplate.find(query, SessionFile.class);
        log.info("filevo=======>"+fileVO);
        long seq = 0;
        if(fileVO != null && fileVO.size()>0){
            seq = fileVO.get(0).getSeq();
        }
        long limit = 1000;
        long slice = Finance.NewSlice();
        long ret = Finance.GetChatData(sdk, seq, limit, "", "",timeout,slice);
        if (ret != 0) {
            log.warn("获取存档消息 " + ret);
            log.info("查询会话存档记录失败======>"+ret);
        } else {
            String conten = Finance.GetContentFromSlice(slice);
            //String conten = "{\"errcode\":0,\"errmsg\":\"ok\",\"chatdata\":[{\"seq\":1,\"msgid\":\"1781138608579135365_1700116714256_external\",\"publickey_ver\":1,\"encrypt_random_key\":\"dbDySbFaEYaYQdxQrufjyTnRlB3stAEuuDHRHyqq1YvQCrvapKk3IFXRhd+TeDspgzFjHXJittVeb/DgwpvTns7skvt3PHcdJDQtSJ1Yh+0oxE2THy8IWhdBuYa2TD/I6kgoLMSF3sA5aYk8401g7CJAgOxEZtOuEogIHIBbIQisxLaG72bJ4EBCt9aEHo5tLkMatAGS6wKBLLREeaCaPYBlMyLl844nTbficvwb75Tu8F6gThNZ5pq6UHJtYkB/+qwxkmmJp+EX10BhIJ6GHkGY2EL/1swBhZekJE5/BKX6XWCdzdLp8T1tBrvbnoaufTod1OARU1w8KFZlU6wYTA==\",\"encrypt_chat_msg\":\"eHjyaMYFb+utv6i5xC4MnA7ZNFpjV/lfqg2aiKG6Nops2um6VuWTZpmF44x2wqK4wjk+1ONw6FvqjT3jKEJWp2yAD11HSMujON8rdUvtdH71Qhuv9z0+Fi7+WAjKK1j1BNb0VUW98HqdR3LP2pemuRySUMXwFVNXljlMaFyXDB9v3rAGv4DC96zvfn1NjB3zyrp9tXIScX+oJ+sXvFmBK+zTqVTzp5rVReU2qoYS5ChYrN7MHk3JKINFWk3XLe6WqdftDnEf4VyDEbfbxLAvu80HLYEdymCZXmfvPzS+6p9qicW4a9LwE+X7/NVwa6QaGarDBm57Dd9unmu8CXa/bkPrBOkWmIpnTggc4nM1Zcr6Dz8grpotZOfre+VRZz5diKDHZ\"},{\"seq\":2,\"msgid\":\"1344580015563658543_1700116828759_external\",\"publickey_ver\":1,\"encrypt_random_key\":\"LbaeIph1U23pQZyTYBuRiQm+nZ/dA1nocau4RRcdcyWF60EIWIv28VIgdau8oOMDaDwc+7XrERNLyqi1nACyaAKglU7dqp5WFb36QlVIFVrnCWPA8pwhnP3w6qacv9/iuE6sG3t/+AN/E0BFhrzWY+e2emMowKl517w4uIRpyutNInCJbxCk2cC6dKQDwraV55HrrV/ZmrjOcPUNgzYDKWXl5tze1RNJRpO7opBn57Vc8Y8KNSDLxF+fqBU3iQW5QcnyFpST0DkVBUr+8mgeoXqF9xkDJuoC1XAxqTwrJ072g4FD/Yze0wTqQ9CzbA3kOXEAD04RXIaIXTGSBPAXCA==\",\"encrypt_chat_msg\":\"5ME6BK2FBpAdQ7Ya12tZNaIAcmFURFLesO1hoAqioE0yKgdoRvNAYZir3Fx/fm4ERLabvMjMP0RkiqmHGosIaxxy56gs+yahYOKDYXmCMWK/zwtmwJB5Be23Ra+QXCkpytleIY8KxBKRF28ZM8yPuU21wfW4/MmQAJo1FGn3QEp56v9NaEJGZgZQDIVIVBoqH1LT1+r850PjTyeMJMPDr7ukoWyrsrGV4ZMP72BiAH31iZt3kduKPtxkFcjQPCOQAKgFr7kaHhjDmQOO2XbyrA2msguELjaOeyI28E5CRHx58t62V1Lq7ksZhZt9ojKWiIf1TLU+j4BJ7jzPvfaFJy5z2ecQV9xU+26vNX5oULPdoTogCIHBOcYDErhPOvk4l+aUTyzcV8Eo7/3NXHS7aDyyRDFqLjE5o/fj7njiJHaEeZqMx5yNNlU1w/X027Huehh3X1SvD2tp6L09qODK2w5nRx0UEcC//3xrwXi5+BtiPKzhrOX8LLLIj5lawSdT8Zd8qsqudifXBYPJbHyTE37i0EEv3i0+1uWw6J0KSb7NHLtZ+T5kcFpJ0NVTX7fAQY3FWyKLvfgtR/ikvVwdp46RNXQwgY6TxnTEAmmsY+d6J+n9LeTCPfdq7Qbnea4fdund4shuxAh+pO4Ocq0VKTfjS+ekP18M/V+LIvOCDRPsjQOd9PAI1Rolq83trQESeP7HlRM9sitz9/hyVaCuAXZlFof0pHH9ZegELPkVA9GcDLGuUsMYCwFqda310JlsmQgbTuKZJ2CsKNVBrbH9fUImqjDewbG8TIsqda30v9QZdUN3+UiNJ8EM3NBjtHSEtCjyGAJN/oigTp9GiChynd9FGqCp4Wu83297sBRtUhJuBU6yygejqGdzP0lH823UX1DwH6zcSdgPK7f6i8jGRvE7XLwZIltbrbcvWIbYOKdz3wi9AYzZ+fvGSIW6Fo+ZYUeE4rFsf2iqylho7Krhiz2Wl0M72cr3D2QbZYieUvdKUvx75cZug9tmaQDOlONVomx7/m0CkJLhbsqecBgdkUNPdOwIWNPkBWvgVj488viMcuOad4+tEnwr2Ektn/TOvxsUdiastdZDvy6iLDOfb/hAk6b0OrqUfsLwkIUZPYyUrOn7x61UE0oYs9fRwzk6LmdtSg8SZxLv+AXUfoiIrJUaI4RuP9HJzhtKi1VcQr3haqLHeI3DvSUYwOv4q9BRerU+CJNxtrvrIPD7famrtEjXeGMHnjY63+V5TU3p7cBesuqnM4onxwTa8SPBd4T35oEv37BdM1TU7jBNfGagSiEjK0VheAvAVxCtct42KlOc3smxAaEXvtmh4AHiLjFEPb0amVBDkTHYP5fY3YMImQtL03G0m4+L28AtHrC0kZdtssgFENeA44Hu0QlVtgDLYidGdxD52tqMi6v+Esam6tGjHEH1WsilDUaeKfnlWfCXlRQUz2bgKCIbx+ulSHuPO9VXspcl9aT84I1NRuaF6CQeiZ3oNtfVpOeTL2324oV1ZGZsEBxjf6gS3oZGJpLaNsNfxEo/qJniDCBdIQTYCpZ4Mm6mIGZwLLc8WwnkqvDqnIE5twP3gzhVVEQfKOqkx4/sYlXJg+Sh8LcjQP8kOKKN0w==IsI++M7\"},{\"seq\":3,\"msgid\":\"15528636514865715637_1700116829232_external\",\"publickey_ver\":1,\"encrypt_random_key\":\"YRD7NH7CusFxnKcTJWcKcnsO4bogtFwAgvp58JUcWwXEmX8OCgSK7CcJz/LHRgI4J02f62K5Ye5LvEpOMygsJedBSxTPSMpkk1yhMpH0o6SltojDVCqVNqiIys46SdXlubGNdqobg1q6nhoOmlw7Y8U1GtBV4TNT5SfqeDZ/69YS+pswpOSubJRGqk150yYx+Xh+j7zJQYhwlEpFpTWaJtEqHe0PbCrRTvWn3enyci6ZjzWWfQSZ/ALHH/wjXfvEVVIaFUySdgMRPDAyeWiEWsTo4fv8wH+gFmY9SeuquMqs49pTHn17c6CIav/br8lSK2iuR8sWJi99X6YpBOWgDQ==\",\"encrypt_chat_msg\":\"zE6DY7HArWvnjLydB/x7fG2KrERObJ4tNiG0jC8GgfbKWKeLPIyWYf2aC1sa78C/4wMR74EXT6kUCnKz70agSoIZJV/X5uWi3c8YqFAOcdSVYHKHcqQ1v7/1L5SwcWIMCgxPVLpw9SxFIzmoOYjRQHQXYrxR3lMQAzpqwkfb2lPyW49e6IZuhI6KOnPERxyGZZlxd04mieUWLYOjMF+94WXc1CzOLtiPImfVzbR34fXCL/6gyGGodSeja2jXctabuljkF738FPf9QYuOT2fVy1IIe90M8fYpYeNfp0Jth257YvvmjH69RPt1YgLJLWYClq7ERhTu1sZ2uXrZUEOAAd0nDxZNH6ByycjVDnZwLicbPpgpPUyxGwIiieA+9dZrBP9nutj9YSxaVlLE01SKnzUSc/E6tofCJ86+YI4Rg+7NYwQwIgaL6o4ngukby3qlh1uSvonuRJm/UQtUVE9Ey2x7h1NqPPnhs1LHDz56ybV2l23C1jvkcS1UsPo5UdamF4i0TSixiq1ziHtDigGXKaVtMuOfY6UVIyGmJHXGuhrtOCLuzUSRcyjmMZarDUwYScQnuUyovCEqIXKN5CC7u84340k0B6aJspkXWGhWJrd2AyB4N5QYxmBBd6pQGAHKIzam2DQ+yHkwtC41kQq1GUci1aHWmxTnzUZWTQmLGPb/JY/1g5/6yAWfRPNskgBpDQFEty+Y73S8myIK0pYDCMPCLNEU9RkD9yw3HjMuPqyd4DdViOx72Eu1c9xC3lXsl7rPdgr2mu9KHZoCPIOAA0wNJVZBagRc6AtWSY0ExZmUIAdMPW/z70BX5rfAhtOC9vrnxIW9yt8t+lCKTZ69fi0F1/YSbzIYePV8eVB8l+MzHdiBR7niDC4aGl4y3kSTCTNEq5RhX3AODqPOYbrMTCsw7OdEOWre96nq7PX/kyR/EpG04M/D/tZZBc+nZvU/K0tHk9ETcCL0S7N5kDN+8h9rF/IoPjhP3ejf+6ZOy9hpcjXUI345E+lJSYDLV7xqToU+IxXp1k20SzFT8U2koMGconge1GDLtKzOEoUe7Rcbg5WJMKXeIrNcfQXYuE8vShgbxKPxPKwq19xL+Z+8pMWb5eJ3kErldPKFvij8N9l6iGU0H7wS+MlyCmucu05H/QjrKJ22/E2isKyZwBfhbbmqll2Ch6EdhO6Z4zlT6Wym9dILgJHYhYAkqvmOyurtBc2jEA3SfOk7OGbwOLTWIY5IjqKlYZeOxG4BlanHPQzut6/xm6thLOnz1s9sH7k1D5h6UKS2QU8RhLQNVmNIeaI2bnRNFplb+UguzAZjR+45lFZZjQv10S3pzq/EMuoSgx4DFXyUs6zmNdgXGom1IUMuX7BnU3YzciCshni6ZT/2Q5myBz/5S4Uh02549uvsVhaeUDz9QSbWHjgCrXlaqQhDvjxcg64Xhw35tWNDhtKaJdZY8/+sTp6Ul/D1TZXd5ouDH/fw68LixYEOC8Fquj104AlFckEUzG6kTKl+tiKXqG0dCqfR3TBFeFTlaDjVgjqtxbNc0/zmYKWczwq2UDeYl6vna5R7WfNov9X4yeC7BLRdflRMU4KSXfZLtdyHH9HaZx4H9aBB2dmS40p9LxeA==iFyaazJ4bo\"},{\"seq\":4,\"msgid\":\"2298684514065073937_1700185564782\",\"publickey_ver\":1,\"encrypt_random_key\":\"NWR9OgALRDMpdt8sgogMw5Cx0kcO9Fr+m4NobKHrBJNp4S7rCTOBo9C8LfXIBFYnb9m7md7ntMNPL6JfCkamlT6d+vjbpPkz6f7xzupb2UEquU06i8ZySqppH6cVNNF7WRYHMwrT9RNg2RavnfXwfNHB6aw0vF/29DKwD8AbFQSvZuV9udIB63gMGcO+W+BTxkVy3Wk37PBj2bH31Rwmu0zNH9MuKktPnHnU1KjfqmcmdKNozVZSnuU1dIpYti1WorHE4VXQDoBs7FafpcHkNMXk8teSr9c4+hrwiVfFXmvjwQmadlGYA2M8wgjjVMAGJNerWzIh7ZWS+1KaoO9D/Q==\",\"encrypt_chat_msg\":\"CASBdv0wRG0IGIpEQCdvkfsRfNpvYN/irnj6VcY81Luh1LzfXWH31GC5VPmRThInlCfUuDubPDABjcrh4OurjKDwRPvNGfcBksrbfhCpwVaL+Kv8GCpjvPZ9HkxOwVqjYOq+gcY42bFcImFxL7ENQ2zSsDgbx31ecc1JrjK1OeU7RXLjRzf+E8dnTYwFrTlIigt2CLwtx26RXpxp4QqqKWT54VN5geCXNeGy5yUlc3yjXNV9oP5zTh4h2L8Ye7G9rj8TA5GezKiw9BGKEgwMK7n8XD38lQCVBtZ8HRAn6oiNsePEy4jov5iCzB/wpyGzTcjlLGlhW6NZkgeI82ido2wRAGdVVxsXVoraqonldr7mwYdfR2GhwfjSuMcIAwjjx/EgrDe9QeOVFJgjOWEqTNuMqgPu3hxsZq7QjlmkfvUWmdIzo24HWYa02XsXZM0d9+kL0WfJj/FKlJhFo36aDLgLrTqwh/gpGZQXTwup9AGpmfwRx8VQRRL0FI/E4E/tZ4HTrZ3STZzGFdWX6FvcPXJjuHgKOS1giQ4VMqloWyFuXld7I0v7Z3N2ax7kYesUgjn4B+12YkUdrsRDNzltyMbxvy4gJSOqO9L8N8=9kmhEI3QW6\"},{\"seq\":5,\"msgid\":\"7311022830649069278_1700186230162\",\"publickey_ver\":1,\"encrypt_random_key\":\"PC5kxghMNPlzCFQ/PPQbEeLoT/P2KuDmKuAxnsE1pAoVhInTMEkh6f0/zvnCl2ogty6BTm+/itFsCIufALei4mun7qm0XRiVBYun8gb5ZjgcQBPAe4HmDFMMtPx5GW0rJqhKmRODqz9BVRgs4kRVl4S4QHW25/2mZtZ0HxgDi3HL0fLlnt4y01q32RnNlIoO48BMvmzYpvhCRuRqg3W70YzXKdI9awim7MR2X+TC2lRdWENlutufVrvm/BddA6mnL/0sO+YyBxnjNN052jjNHyL9uNsnK8dpWj3+mbtB7OOaMNQcxTBsWGXGygbq8tENUDvv4HJEW9MIdj3xnOCLWA==\",\"encrypt_chat_msg\":\"5iRkCPWitRpgIb0skYkZdgWJFtXOhf03fHXkxEo41OITMhvIMndwciJGSfV9QdapUX8zekOnsQP6Ifd9EclHCQc4Vi+aQEpq2SSFVSvNQfxv5nI1Wp0XRtVaV3tIlnY9OmwjR1/u1996A+0fy7uBN3uvzrUsNRk1Ed28+8WC4mBOUx/rRlUEpKpDbRvDCY5SDzIMiHOR40UaW8s/PJ3h7LbmItZrZeZHyli68=Xz8slu0ZVx\"},{\"seq\":6,\"msgid\":\"19842294776159877_1700189035325\",\"publickey_ver\":1,\"encrypt_random_key\":\"asn/BBVTtscgDNzs56E2ZjqYtzOZQaHcfcv+i2hvIJZqSDZqVq81qF1KXXSRpmjMlG/6hrDrI1WM/sDfh/9PNDvOC0Dnkc7Lsi5D2VgLqe4bjLhOwlL+BhtcCyC86TsI5tbRr19J1az2q21U3txLpkbwL/Fa6UQYAp5xe0epfcwgr0XH+Hzl0U5w3TUMTEcYl4xmSPFu+YL5VYd/LvnG+QMHNKOp6wg+LutcdidcKxAuPaBodzH9hWGMw3Jgi6A4r9rpnaKPFtTFxZLj+mviCjI/DbiyAQNktLhA/ORlKv6RR910qrCyXvWzXh8QDORk7VZnRw+1kElC4Yi0AkXgtg==\",\"encrypt_chat_msg\":\"wn+O2b/XCHX3mDcl63BFZ/mbZafVsVmRUtLAWKVvwoGFhUtdo3liioP0S3DnRvtyWzPSfiH5ALsr+5LAri3d4jsbU+4xbf73y7bl9YbZnAouwJjNCOS+WZdzEZFckkjvp+vdL4eeS3QIvyWDYhOcc6BkZD6dMeJFPNvnpC7JioW58mfK+apPh0V44kx+HNbT5opt39SdC4VUYKmFRy8GP1HBkAfZfagotJ+ZtfbZdXDOI1/ThJZPKvphk7r\"},{\"seq\":7,\"msgid\":\"15551170165114941175_1700276362818\",\"publickey_ver\":2,\"encrypt_random_key\":\"qLUAkDOMXczJTWUHbJpbInWeo6zMvJ3U/TtMky6VwRrDp6c3LvXi8lNAWgh09ev/fjfw5PoyDBSYsjVV97QlqkoFS4ORZUuYPfzDhhdmYMwlhO4f2PcN9AYHObQ67I2gsItDDxl2I9dDL/Ej1RP6sa3G/JmpxWRNN49qDPTt03VRE+IdXDfphLsI5rm1N+76yNK72efvBgZ33eFqT6EweWaDXufZJkwIXFuFzUvyZi3WgPxqeAidckgYtgdO7ZknrS6fr3s96d/e4/hCx8yRJz2Jkf73Ak2nRK6V9PL9oLg8zwU6aEWj4V6lwXHHm+B6GeeMnKWKXjKv/BZVinCGwA==\",\"encrypt_chat_msg\":\"FgrDUvI9Vjk7bl79I1dthvcy8LmNjpGAQ5rtuQPU731yiKI9Jc55LfiFnpW3LX4kyeVcKAqIFev+3RgaHVnOMFgpjU0gkofJUkdUJveSuY9CpF8678/ZJMShI8Gv5f71ppOVu/OICIh3Tw+/3uSaE1QnY81i1SaAtzYIjbRlUKXa23O+cmH/g5yCadDQTJbffKTabG2bo55VRbkkw0X925yNa9r3k0mIyazZk+CI3F9oJldJ5RGnaQjYIB8ReWHsP3uMgwBZTmD/5QEpMWSWNKJomyaBuYjdRfeLGUW52DI9/qBtd7OlX7oxvE1iLJlWj9RPDtxjylXNQl3Ya+b0i2wunKD5mo9Qznzzh/QwuYGfXLzB5azvhsCYervoTAQxYr/HQL2hF7f8HMY3DrwXItNVXQTBALqMxol39A57/K1pd3TvVTUB6jUw/hU9+fSUohvmfw1XF1igWfPx27TKBFn4xoZo2yEmQlhJ5wbHSZttiHYgITKsziyeh2ihgVPbAEd4s1WJHd3UAFRukl+pps2+TGJ44VlvxX8DY8RNZOwFPaorJz+5koj9NCasNJYPc4FiR3Iv4VnafJCrag==qd\"}]}";
            //String conten = "{\"errcode\":0,\"errmsg\":\"ok\",\"chatdata\":[{\"seq\":8,\"msgid\":\"14392102519757081978_1700468813016_external\",\"publickey_ver\":2,\"encrypt_random_key\":\"IeT+gs/XEuWeRfwUvhUNwfWSnqecGH01qjb/YCeQb/LpHextocggOFU8eaH0anlHA4zZnYQjXnwaqOPv/yVOkOG4HKBFHPW0gBYt3XqvwWHorRODjzBQhg1ti7/16s3ibsSVgsmKKcH7IBNjQw/flyqYluTinMuA8kO40Wz1K+Af6as/8lzbkzvTZA9oEj48k6UzxWJyaJFcdClEWsA71C6yiZ1o8zDM+wEtWzTmngielW/4Sw3IBYWywEwDB6MwLGy35lzYjYC+vPgfm7jTr7NhiN4+gISDBX9xiwx0aL5xhM8jcb5+IRO0K1YOwZW4Drmn2NaxOQSk+96pP9XUfg==\",\"encrypt_chat_msg\":\"fKqhHL+SzvCjkzGvI0SrZYByyTZDyE2YnOprPemEfZb1KynobWpG/SiM8IW6ioGlqz7OvXUIsBL5lfroEepkpMWgcMjF0lotF9wXmFCNQkRzTAJ74Q8Sg5LrlX+uuYKm+W7LB1EgVaVUvqtAgmW60itmeQdB0CjsayIIxqDnXzryChG+sfWRuju0YpBL9nHoFLgwRB6EG8+C0yPMW/g7ps6EcjPaWPhapxDSujzUM9+K3oaOG+HKmMhDU/SpZTQe9vxXZ49ZOELGhl8IclT6ibwT+IaUktnmodSIjqS6gqIoY1ZP/oAG/+O6Y9b2c43sgbmJbUHNhbHFDvem5ehLx9CzNQ6hinEFvK76HkkRMsym94EVjdhIpmr3uV2SbSjlxaSB90D4aILIp8cvMGYK8FHBACFoGlDrl7i5f/sarEMD2m7+wmj0gvOaeM8R9Vq/S8fk+0W1g==Q\"},{\"seq\":9,\"msgid\":\"15862899279738947571_1700470247172_external\",\"publickey_ver\":2,\"encrypt_random_key\":\"I7YR+UFY2gs9AdseMRYbNzEg5KR5WG2MwGNLTGm04ERM4BqqbcdaqisMAf9qjv+BbbRNKRKckvMOd82Apvy0lTbdtbUhksWdQ4q4+D7++Jy668bKyNBcrnd5HP2nj/P2/9CCaQGZRU+rYOOoQrmR9tSgxXY9HT3s+mFgkM/n+ZMjVF91ojenghkf2SqSocDjAVu57JBR9w/bveRPTdOOgFV/8A+xtZ16AvhZMLwa+31tD50DDy/K+WdbCTDS7bnSfZxnoOLMdhPHxZXIsNPX0h7ZN9ClhedGWk4B50hKOftMvt2NrCL8BOVyldckzwErqw94XL3ixoeXKJ7DhUqKJg==\",\"encrypt_chat_msg\":\"nUw1KG2bAW7Lo8y2czr60mP6DTdXqnn6DYM6C7OEZBO60OHl9Ix+/demy4LrdJ1YFxdjYWqL+oPaDaL83VL/rCedpcol0Sm8od2J2s1Ub0SMzyArapxDNAfEy1/jL/QBprZld0sC9VtRhwakwmQI7Y3M6x6c5YsDYqhJKFQIwrZeobz4bi59GXQRtZM3lhCJ+kaIuhL4EBpIR/4BPEhZ2pQxP8VfbB3826rCj6XLD7TRs8baGduNlXYSexXqqhBqG4fr1Hj4iNl7mh5veec2YPhauN9xXZLk+t1x/9hw56+YSKDak2HF7+oTTakk2z2RB/w8v77V1+nhWMf3Xv39VY7q6uqX1z2iLClgEHjAEljFg/Ztrzm7wBRd/bZpiOjHC7WoVWW6aL8xoF7Jv/0uFv4gHYqdRa+3y8UpU8dmioxV/1ayoYvMskn3X4FznvLC140ln8sp0x7oUEyqN7rOzM1lbWifHvpliePxD1LaEz+x2bo9PwRR6yDyAWa5AUuVTFOFHsUa+xRcyA/I/lp+dXkYgbCh05jzFpfj+MtZEbVvb93BY+SP0nYd3JMPtwO4My/Y4qL5Lwo5hVL/7sp03VbJS36EKF/5nIz9Iw8H6pL9VIwqO07svY3fsvWXTzD94rZ3rJNrSYsMSPCPrHfZ0eIRZWiT3W/Nvwl4jxOl3Qm+JIRrKdkox+GH1QL2Uu5QsPSBHPaL49wkOdE91Kp/kkmuR2PqsO7kD6KyZ6+WU4h9JDQz129eB6j3s6yvhureuavLXMdRV06RpWEpRYJPXQj84+6csZhxZZo8aB5YNVvPh1TdjI3iov1nIRgv8KnSoU4hFgum0vkFyCwhz6+fOD8ppN130CGQ7Rw/TQu4DJ/PtqqhlVSTL7xlye8WSvX+nvhLKi+4q3L2+Yn8glISvwktG2x3dFyipszkESISG/lMkSRHEu4n65OD2oXcizP0dFUAGAdLJos6fq1/OHc7jeK7hIXWVcLv39DFBrpD4zIV/eNEFi2igEZZifTYEcSOoCH9FsYO4VHE4Lgl2EiTm3QmY4HHcavg40IsN+t5HYqMbyHbR64oBfjQ/EGMmVB1zcA2S16WipUl3xsOjyMkusizUQMfmumPPZUWkNLZ3Ggin7mph1kDOmM36nev6PuyIKTE0kbmTwHbyaLMZFY7ZnVXIRnBoqiB9nR3ItnRRJ0wYwa9CDtPDwcdo1vwJeAo7Sf6lig+Zo/D6wR63fY37lEcMXWCWpOlJEZSK3QBEnbqz58Mo1MKns00XuywbnnUxpTIzBPMmSHijBIEnxQBy2io1xo+mR99qlGxAOR3hSHlZ2WZsMwtHsuQVfOfrbmweIwm0K5pA48XXfngMVCXs3/DuafOBx1H8zdeFgjoexc0q4GoX+aSoEVk6KHkyGQNqOTf98q4Dz+WMLSSwGfYJiw2S3r3208MmhN9tGYkgu2yImGS9EZnGaiXkiqJgLpgq1nouwkQx20b9WiWL+CSjzm3IkY8vldt1GbzO3sdtHNXAfvJ1tsV33HJN0T8QkE8FfrlKSnNHJhN2FoOhMPiqcgxxH4/O5JolCQiuQHxuVNJpdbp3L+11e81OnqrerXp3MzbQoovNb2mFObrDJ4EQ6A==86ESK1e\"},{\"seq\":10,\"msgid\":\"5388703556548078124_1700549696491\",\"publickey_ver\":2,\"encrypt_random_key\":\"d5FvmJoLs0ngoJkXTLLHncv/q+dpmFlbOpLqjBVB/ypKWDRo/VNdjazIPey4DLcxbWQHLHKTfKgp6GLQ3P6lbNWh2att5NSQYLy85yGK0UKCP8jVWQ4ZbZjSGq3yuF/HNJKJ+hDaxzcwPSQRhoCSD4zEwCIJVd1j/vxIJyJ+Lt67o9i4g0LoHqlhf/4wOBWJKOKPGcVu54iEs58gcOwiVawh8J9rFSKKZoVGhSGo1gbuOuNtlknQstswyUVOubYRA8+oeMGetle2hvXyQkvr5y8Qqa2uGNCkHrLK2vOfTFFVtOpbK+bzJD8ZSj4uhxRPww/7kHSS+e7r6kaBui+CzQ==\",\"encrypt_chat_msg\":\"VyZE4Wx6jlFDCUUfOBpD6NgwxaIVzcF7qv2DcGP3lD4GrYIJDWqx8nfatrWvDNqlXLKc25YZSJzHl2vp+Bs6gNxP8KotQ8WlzB7GQuXom4RzuKfYZOCB7mC/bQTWqj02SLcF9KaR8QFYxmJQGxXtBf36oB7VSuwrnJV6k6txOelapWsyRx6Pdndx8WWwCPmn//cb8jzw/C53L9CXIrwF6MB6X7T4Trv7U5NYTLxS9xta7/Q=JSMG5\"},{\"seq\":11,\"msgid\":\"31888448437053554_1700550857833\",\"publickey_ver\":2,\"encrypt_random_key\":\"kAkNzxBuIMbUqiot0pf9e5BjSMScDE/EatAbYCacBz3Lnztf4yAA4Xdp87Y9fVNEHxKCj0qGSJiZTrnrujJZWP2zkfRaSmLZdTfoxALirmVTAhog5AE14aRaB0IPoXioe3GZCMftMB2g8m4wDM1riJ/3w8K++f13+LfCR0u/FiC5XRh/9dkWZkAS+clJjHuOP6im5vq4U/9b79rJ2tsMfygPHgqpnABK8Mv6lvVdugIEOMZ6/BMwmBh1bScPJYN6FQCGU4MJdG/oK1XgK6HDiFN/V/xshfsHhJyCJyAqmZ4fwmdTCtTwZWkuuNfYGQ541R7TSYjSyc1IbAH6UEhYmQ==\",\"encrypt_chat_msg\":\"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Nh/RtE9XX\"},{\"seq\":12,\"msgid\":\"17395107174388079993_1700564521975\",\"publickey_ver\":2,\"encrypt_random_key\":\"YSYTuluhL/7GIXrOjvZrfATztLaFb2uzijsji2vq1QL/72t9pcmaKbdGtePIICKSZMQ4/gIMPWLCcTz/VDCL9P+wkEGqcFDXU6MNsczTG9W+2kCEXV7G8HNC3gdPm46S1x9wls8GF0fQtszoBuIJgnMh58/McUlDsz/IQevVl67vDjRQ8o7dqC0JeLXN25Ms1rVDXP8bKiP67rLThuTbQG4ty+0ji+svbGuAl3XYD4hVX7fcvA5wglICoxskN8YYWF/9kne4HYYBVrZjY/UTd8+JhOiUoCiBUPpGenHFiwCXAgNHOGRsy8JXkcYf+NTKtwojkMmWmOaPznU+i2rlEg==\",\"encrypt_chat_msg\":\"nL5RG40xIvVKE9HXDFOcxwz2vKut7t53W8B0l4bl+XLOHJfA71TsTDvP7zCNFsFwFwKR+fHNfoy0U2IbPsG8MkXJwkw3ffLd041OTYNCotvzQyc0X07TaAMnIRSZbQHg6+x8E2hcCWD5/zi8pDfX1/XI2QCoKpIR6+4siZXFsbxd1/gOm4mTSLDD8Z9xWt3e4yZwChjyo5vFMGaBQ6MyBlnwQZ0HPJulpar8RcCmQ=JCiBW7hDA8\"},{\"seq\":13,\"msgid\":\"5670868938303448581_1700620842827\",\"publickey_ver\":2,\"encrypt_random_key\":\"TNpSpCToLFdE6PnFNt5+lD76R23ASQDC/pZxXA/JFOjehY1XYOSBWIYNqfZYBWznQQRadw9W6iwtr1X3JOp5f7YlS9OKhKlxbhl9qlO5M9XQvEXdjN/ZutfZmVg8CZ52uml255SATh1LOnxbnxZBRBV0ZcK0EiYCnYv+RWZ2BVo+zHctgglsl0g/DOf6PhUx90k5WezL667iPzde5GoQdCcWnBRgCPLmSCHggExCkypUfu3NBeMNvN86Dy6pE8K83vEzaiuI4q/pV7CXCQ6JGTUpwXLZh5JbUBputFkhRM6eDBlyNz2W67fyOTmqbuBDxQIHBUf0t1Z5mYHIxUW7RQ==\",\"encrypt_chat_msg\":\"LGrvoYh77Zk2yS6xeTbN45c/o63bzQbJnezRPMPBQCCmHZTP0/1v5XwXIKq6P+6BAgaclu9HqcB7/sRtxNJSU39HwwfXvxHUujkhmJdt1Qn3u3ms86onGv1+/AXYKjnGvx8GHyLBYLheEjjv84jaiqkmchoSyK93ww6ZoxvcMG5cWE3YH0u1XAaRoX/R1ZrVVqppDvHBpJM9FtJDqRv8+kUOWgsWJGk=/UuyRC1KF\"},{\"seq\":14,\"msgid\":\"5874986596517033551_1700646098868\",\"publickey_ver\":2,\"encrypt_random_key\":\"aXdmAVHoLkKl7XXXKmU4OLoSjlSClbVpN8C/a0PNzP37h+6i1Ay5DQMMUcpRu5Ty1lOJ/AMVgh6pkTmAv1nezwPcVIW4kSdXHyA8q3o3CNNSva1HdwCksLP105jSTit10navfHgqgNSJUtJp5tr8lsejo50xvU7YadctW+qly9C/3dKatD/shDCjOYwByf5V/xkq2xNfndFtuWiO9Wq+sRaMk/ko6MPl1YWK3lo5jaIgmQDDDU7N0wV/phBE5PVSx7oQRFdZXD4gxI9xJ7htmz4PpbbteKOERIGT+gd9tPB0E6n47299D+cCLyxArbtXkh0j2STbDeY0ffiT2oG6/g==\",\"encrypt_chat_msg\":\"4C8br7gWVmtgdP/OVxt8RAwa3kzGcfD8iq/TtenNpjW91JTtuXTfzP3wf9w476kxMHCA992OQ/PAV48VTWIqbD23ZlsGHEQPZMKdkWKkPYTE4iRTLerVLZvhBU+0CGzRloxtfEGobpcYoSU7gRKmiMZoIYOJCR28m3Wx2eWN3nt21+LxQ8Y62yL/5oftVAxlFvqRjLvsM7Ah+hGjROR5E1nWjfhhybDyhIj36Sxn7ic=Cb0Znq68+\"},{\"seq\":15,\"msgid\":\"12652517539337353653_1700717940044\",\"publickey_ver\":2,\"encrypt_random_key\":\"EX0I+H+Gtnxy6Oyv3MO3DS6J02WbW+08u+nYjsdq5XNKpotFJb3lLSeDhSQHWZ/V3rIep8SsrV3uL0pd6Wrhd9Yn1pkC2MAGyIly1QHrdsmf7AdjLpggTTl0KE3tugkhlDlmx7bfL2khxwLVTsZBlrCZVTT70B2SFCrYfIBIehS2zFNgN2eVRwDZq6pvo2Vt1RFmdkSc/z/CSLwIlwj9JWY1K6moZSOE7XHbbC+apoElP4oZpjAH8RZw/lrkaUhI+6iDGIyEMAPY/19shOkAkbWA9wBdQQRHaEncyUNsd72MH7Z+6G4I/0pBcuKDNKZkGcsqru9pGou3VxpWKUgGYw==\",\"encrypt_chat_msg\":\"KkWuMgC3uvF3PEoj1iNjMbSDaIHqaohZWAn+55s2YLMYZ1AV+E02vr+5QcSIhdEMYtX0J8+BFC48sHePXkKdRhqEA9w1uP/5mocjwVEbaOh9HNf78kRNOOZQqOMj5HfAXAaJieHQGWMQMFo8Ykib7PQRRalS8nTouuS3lCzpbCqFFBQTXLyxGOb3x5sGVOe6NN+yKqyQLFFMtPzLpFUOV36Eyl7sBfrweK4R4zODPLUGVAaDwbgrUii5Wrb4rGd6DcTB1UKoj\"},{\"seq\":16,\"msgid\":\"16394247857451204764_1700718395234_external\",\"publickey_ver\":2,\"encrypt_random_key\":\"UrKfbCg7ulXzb/R1OufWkLpedKQxx+hXMz7njtAB/eAu40yJinFb0tSRsE+mDjpoqBhBLMWjybZQJqP598mNtIKSqIyfdnYWu3mk61lBOQOYgtddHwL53+LnvnJP74Ka2/yPDO7YL1ysIO29tK2O6SfqxkR0N5gUCGHpL+lauihsVVK7E49E8rwwyoaa7qdGUvL87PB4PEMbeBQyFLlErFFbt47DS8Wjaqwxwx1UpvJpwiS1dUKXHQtOLPHcdInRbfa7gkun/gWrewKaikutNo4iOx5VMmymQ0CL9rZtaLLbPrg1IXf/Dth2etjFcKEnJcOqy4wmjxfaKTuTMZ/r2A==\",\"encrypt_chat_msg\":\"SzySg37xUkdPllCDC+Qkz94ZqhuPfhvyncqBYEPVberGua264p+U3OxpfpRIPMmawbELiVf0ixEgqIWQseOJQ1Zf6qgS2jIlVFOMZ/mRKXut4U7M/IN+ZV5X2TkLv4jzpjsJGR8A0QIcb3vJEdsjwMUUm6/AIniuIHwfEYqXFzmCGmGDCJ7tt1jzwtOqMb/xZjz+UYifM0KXYlwgCQdoBknp7/rCyBD4EFrqa9TXCVmhyzqQBDNXULDF6Md/vrfTeVmGm3fw28h5iDFSoY+P2pUlL+RyV2GYdNY2VVBT7uO3fzy5Turf4DQlhbB7X75TuIkm7ZdjnOKYyhunRPmwI635wNmnpLjq8qx91jG9SZ7Cke+4yrJIx7uWTYkCbbAdOZ88gQoFBQJ5p7PZs2BSZYnJtvReB0x8/0uBCnP9mmGWnUJkvDH0VqzDPV9pcV0wSslXv+Psd\"}]}";
            log.info("会话存档返回记录，sessionFile===>"+conten);
            JsonArray jsonElements = JsonParser.parseString(conten).getAsJsonObject().get("chatdata").getAsJsonArray();
            if (null != jsonElements && jsonElements.size() > 0) {
                try{
                    List<KeywordVO> keyList =  keywordMapper.getAll();
                    long newSlice = Finance.NewSlice();
                    for (JsonElement jsonElement : jsonElements) {
                        SessionFile file = new SessionFile();
                        JsonObject jsonObject = jsonElement.getAsJsonObject();
                        long LocalSEQ = jsonObject.get("seq").getAsLong();
                        String type = jsonObject.get("publickey_ver").getAsString();
                        String key = SessionDecryptUtil.decryptData(jsonObject.get("encrypt_random_key").getAsString(),type);
                        int i = Finance.DecryptData(sdk, key, jsonObject.get("encrypt_chat_msg").getAsString(), newSlice);
                        if (i == 0) {
                            String msg = Finance.GetContentFromSlice(newSlice);
                            JSONObject jo = JSONObject.parseObject(msg);
                            String msgId = "";
                            String from = "";
                            String tolist = "";
                            String roomId = "";
                            String content = "";
                            long msgDate = 0;
                            if(jo.get("msgid")!=null){
                                msgId = jo.get("msgid").toString();
                                file.setMsgid(msgId);
                            }
                            if(jo.get("action")!=null){
                                file.setAction(jo.get("action").toString());
                            }
                            if(jo.get("from")!=null){
                                from = jo.get("from").toString();
                                file.setFrom(from);
                            }
                            if(jo.get("tolist")!=null){
                                tolist = jo.get("tolist").toString();
                                file.setTolist(tolist);
                            }
                            if(jo.get("roomid")!=null){
                                roomId = jo.get("roomid").toString();
                                if(StringUtils.isNotBlank(roomId)){
                                    file.setRoomid(roomId);
                                }
                            }
                            if(jo.get("msgtime")!=null){
                                long msgTime = Long.parseLong(jo.get("msgtime").toString());
                                msgDate = msgTime;
                                file.setMsgtime(msgTime);
                                file.setCreateTime(new Date(msgTime));
                            }
                            if(jo.get("time")!=null){
                                long msgTime = Long.parseLong(jo.get("time").toString());
                                file.setMsgtime(msgTime);
                                file.setCreateTime(new Date(msgTime));
                            }
                            if(jo.get("text")!=null){
                                JSONObject object = JSONObject.parseObject(jo.get("text").toString());
                                content = object.get("content").toString();
                                file.setContent(content);
                            }
                            if(jo.get("msgtype")!=null){
                                String msgType = jo.get("msgtype").toString();
                                file.setMsgtype(msgType);
                                if("image".equals(msgType)){
                                    String sdkfileid = "";
                                    if(jo.get("image")!=null){
                                        JSONObject object = JSONObject.parseObject(jo.get("image").toString());
                                        sdkfileid = object.get("sdkfileid").toString();
                                        FileModel model = getFileModel(jo.get("image").toString(),"image",msgId);
                                        GridFSUploadStream uploadStream = insertChatData(model);
                                        try{
                                            boolean fl = getMediaData(uploadStream,"",sdkfileid);
                                            if(fl){
                                                System.out.println("文件写入成功");
                                            }
                                        }catch (Exception e){
                                            e.printStackTrace();
                                            log.info("image文件写入失败：{}", e);
                                        }
                                        file.setContent(jo.get("image").toString());
                                    }
                                }else if("text".equals(msgType)){
                                    int t = 0;
                                    if(StringUtils.isBlank(roomId)){
                                        t = 1;
                                    }
                                    List<KeyWordHit> hitList = new ArrayList<>();
                                    for (KeywordVO vo:keyList) {
                                        String mate = vo.getMate();
                                        String [] mates = mate.split(",");
                                        boolean flag = false;
                                        String staffQiWeiId = "";
                                        CpCustGroup cust = new CpCustGroup();
                                        if(StringUtils.isNotBlank(roomId)){//群聊通知人取群主
                                            cust =  custGroupMapper.getById(roomId);
                                            if(cust!=null){
                                                tolist = cust.getUserId();
                                            }
                                        }
                                        if(vo.getSuitRange().equals("2") && !from.startsWith("wo") && !from.startsWith("wm")){
                                            flag = isFlag(file, from, tolist, content, t, hitList, vo, mates, flag);
                                        }else if(vo.getSuitRange().equals("1") && (from.startsWith("wo") || from.startsWith("wm"))){
                                            flag = isFlag(file, from, tolist, content, t, hitList, vo, mates, flag);
                                        }else if(vo.getSuitRange().equals("1,2") || vo.getSuitRange().equals("2,1") ){
                                            flag = isFlag(file, from, tolist, content, t, hitList, vo, mates, flag);
                                        }
                                        if(vo.getIsRemind()==1){//判断是否需要发消息
                                            //发送命中关键词消息
                                            if(flag){
                                                String userName = "";
                                                if(from.startsWith("wo") || from.startsWith("wm")){//客户发送消息命中取tolist
                                                    staffQiWeiId = tolist.replace("[\"","").replace("\"]","");
                                                }else{//员工发送消息命中取from
                                                    staffQiWeiId = from;
                                                    //查询员工名称
                                                    ServerResponseEntity<StaffVO> staffResponseEntity=feignClient.getStaffByQiWeiUserId(staffQiWeiId);
                                                    StaffVO staffVO=staffResponseEntity.getData();
                                                    if(staffVO != null ){
                                                        userName = staffVO.getStaffName();
                                                    }
                                                }
                                                if(StringUtils.isNotBlank(roomId)){
                                                    staffQiWeiId = cust.getUserId();
                                                }
                                                ServerResponseEntity<UserStaffCpRelationListVO> cpListVO = cpRelationFeignClient.getUserInfoByQiWeiUserId(staffQiWeiId, from);
                                                UserStaffCpRelationListVO listVO = cpListVO.getData();
                                                if(listVO != null ){
                                                    userName = listVO.getQiWeiNickName();
                                                }
                                                String tag=null;
                                                if(vo.getIsLabel()==1){//判断是否开启打标签，开启才需要
                                                    if(from.startsWith("wo") || from.startsWith("wm")){//为wo或者wm开头的是外部人员
                                                        List<UpdateTag> updateTags= JSONArray.parseArray(vo.getTags(),UpdateTag.class);
                                                        tag=updateTags.stream().map(item->item.getTagName()).collect(Collectors.joining(","));
                                                    }
                                                }
                                                if(!vo.getRemindPeople().contains("2")){
//                                                    pushService.mateNotify(staffQiWeiId,userName,vo.getSensitives());
                                                    NotifyMsgTemplateDTO.Hitkeyword hitkeyword=NotifyMsgTemplateDTO.Hitkeyword.builder()
                                                            .mate(vo.getSensitives())
                                                            .offendUser(userName)
                                                            .offendOutTime(cn.hutool.core.date.DateUtil.format(new Date(),"yyyy-MM-dd HH:mm:ss"))
                                                            .tag(tag)
                                                            .build();
                                                    pushService.mateNotify(NotifyMsgTemplateDTO.builder().qiWeiStaffId(staffQiWeiId).hitkeyword(hitkeyword).build());
                                                }else if(!vo.getRemindPeople().contains("1")){
                                                    String[] staff = vo.getStaff().split(",");
                                                    for (String s:staff) {
//                                                        pushService.mateNotify(s,userName,vo.getSensitives());

                                                        NotifyMsgTemplateDTO.Hitkeyword hitkeyword=NotifyMsgTemplateDTO.Hitkeyword.builder()
                                                                .mate(vo.getSensitives())
                                                                .offendUser(userName)
                                                                .offendOutTime(cn.hutool.core.date.DateUtil.format(new Date(),"yyyy-MM-dd HH:mm:ss"))
                                                                .tag(tag)
                                                                .build();
                                                        pushService.mateNotify(NotifyMsgTemplateDTO.builder().qiWeiStaffId(s).hitkeyword(hitkeyword).build());
                                                    }
                                                }else if(vo.getRemindPeople().contains("1") && vo.getRemindPeople().contains("2")){
//                                                    pushService.mateNotify(staffQiWeiId,userName,vo.getSensitives());

                                                    NotifyMsgTemplateDTO.Hitkeyword hitkeyword=NotifyMsgTemplateDTO.Hitkeyword.builder()
                                                            .mate(vo.getSensitives())
                                                            .offendUser(userName)
                                                            .offendOutTime(cn.hutool.core.date.DateUtil.format(new Date(),"yyyy-MM-dd HH:mm:ss"))
                                                            .tag(tag)
                                                            .build();
                                                    pushService.mateNotify(NotifyMsgTemplateDTO.builder().qiWeiStaffId(staffQiWeiId).hitkeyword(hitkeyword).build());
                                                    String[] staff = vo.getStaff().split(",");
                                                    for (String s:staff) {
//                                                        pushService.mateNotify(s,userName,vo.getSensitives());

                                                        hitkeyword=NotifyMsgTemplateDTO.Hitkeyword.builder()
                                                                .mate(vo.getSensitives())
                                                                .offendUser(userName)
                                                                .offendOutTime(cn.hutool.core.date.DateUtil.format(new Date(),"yyyy-MM-dd HH:mm:ss"))
                                                                .tag(tag)
                                                                .build();
                                                        pushService.mateNotify(NotifyMsgTemplateDTO.builder().qiWeiStaffId(s).hitkeyword(hitkeyword).build());
                                                    }
                                                }
                                                if(vo.getIsLabel()==1){//判断是否开启打标签，开启才需要
                                                    if(from.startsWith("wo") || from.startsWith("wm")){//为wo或者wm开头的是外部人员
                                                        UpdateTagModel tagModel = new UpdateTagModel();
                                                        List<String> f = new ArrayList<>();
                                                        f.add(from);
                                                        tagModel.setQiWeiUserIds(f);
                                                        tagModel.setTags(vo.getTags());
//                                                        crmManager.updateUserTagByCrm(tagModel,4);
                                                    }
                                                }
                                            }
                                        }
                                    }
                                    log.info("敏感词命中数据=======>size"+hitList.size());
                                    if(hitList!=null && hitList.size()>0){
                                        hitMapper.batchInsert(hitList);
                                    }
                                }else if("voice".equals(msgType)){
                                    if(jo.get("voice")!=null){
                                        JSONObject object = JSONObject.parseObject(jo.get("voice").toString());
                                        String sdkfileid = object.get("sdkfileid").toString();
                                        FileModel model = getFileModel(jo.get("voice").toString(),"voice",msgId);
                                        GridFSUploadStream uploadStream = insertChatData(model);
                                        try{
                                            boolean fl = getMediaData(uploadStream,"",sdkfileid);
                                            if(fl){
                                                log.info("文件写入成功");
                                            }
                                        }catch (Exception e){
                                            e.printStackTrace();
                                            log.info("voice文件写入失败：{}", e);
                                        }
                                    }
                                    file.setContent(jo.get("voice").toString());
                                }else if("video".equals(msgType)){
                                    if(jo.get("video")!=null){
                                        JSONObject object = JSONObject.parseObject(jo.get("video").toString());
                                        String sdkfileid = object.get("sdkfileid").toString();
                                        FileModel model = getFileModel(jo.get("video").toString(),"video",msgId);
                                        GridFSUploadStream uploadStream = insertChatData(model);
                                        try{
                                            boolean fl = getMediaData(uploadStream,"",sdkfileid);
                                            if(fl){
                                                log.info("文件写入成功");
                                            }
                                        }catch (Exception e){
                                            e.printStackTrace();
                                            log.info("video文件写入失败：{}", e);
                                        }
                                    }
                                    file.setContent(jo.get("video").toString());
                                }else if("card".equals(msgType)){
                                    file.setContent(jo.get("card").toString());
                                }else if("file".equals(msgType)){
                                    if(jo.get("file")!=null){
                                        JSONObject object = JSONObject.parseObject(jo.get("file").toString());
                                        String sdkfileid = object.get("sdkfileid").toString();
                                        FileModel model = getFileModel(jo.get("file").toString(),"file",msgId);
                                        GridFSUploadStream uploadStream = insertChatData(model);
                                        try{
                                            boolean fl = getMediaData(uploadStream,"",sdkfileid);
                                            if(fl){
                                                log.info("文件写入成功");
                                            }
                                        }catch (Exception e){
                                            e.printStackTrace();
                                            log.info("file文件写入失败：{}", e);
                                        }
                                    }
                                    file.setContent(jo.get("file").toString());
                                }else if("link".equals(msgType)){
                                    file.setContent(jo.get("link").toString());
                                }else if("weapp".equals(msgType)){
                                    file.setContent(jo.get("weapp").toString());
                                }else if("location".equals(msgType)){
                                    file.setContent(jo.get("location").toString());
                                }else if("emotion".equals(msgType)){
                                    if(jo.get("emotion")!=null){
                                        JSONObject object = JSONObject.parseObject(jo.get("emotion").toString());
                                        String sdkfileid = object.get("sdkfileid").toString();
//                                        FileModel model = getFileModel(jo.get("emotion").toString(),"emotion",msgId);
//                                        GridFSUploadStream uploadStream = insertChatData(model);
//                                        boolean fl = getMediaData(uploadStream,"",sdkfileid);
                                        StringBuilder streamBaseData=new StringBuilder();
                                        boolean fl=getEmotionData(streamBaseData,"",sdkfileid);
                                        if(fl){
                                            file.setStreamBaseData(streamBaseData.toString());
                                            log.info("表情文件读取成功");
                                        }
                                    }
                                    file.setContent(jo.get("emotion").toString());
                                }else if("chatrecord".equals(msgType)){
                                    file.setContent(jo.getString("chatrecord"));
                                }else if("revoke".equals(msgType)){
                                    //撤回
                                    file.setContent(jo.getString("revoke"));
                                }else if("disagree".equals(msgType)){
                                    file.setContent(jo.getString("disagree"));
                                }else if("disagree".equals(msgType)){
                                    file.setContent(jo.get("disagree").toString());
                                }else if("collect".equals(msgType)){
                                    file.setContent(jo.getString("collect"));
                                }else if("redpacket".equals(msgType)){
                                    file.setContent(jo.getString("redpacket"));
                                }else if("meeting".equals(msgType)){
                                    file.setContent(jo.getString("meeting"));
                                }else if("docmsg".equals(msgType)){
                                    file.setContent(jo.getString("docmsg"));
                                }
                                //如果包含external则为外部消息，表示员工和客户会话
                                if(msgId.contains("external")){
                                    log.info("会话聊天超时=================");
                                    //如果当前记录为客户发送记录，查询客户与员工最新的聊天记录
                                    String tolists = tolist.replace("[\"","").replace("\"]","");
                                    TimeOutLogVO logVO = null;
                                    //如果当前记录为员工发送记录，查询员工月客户的聊天记录
                                    TimeOutLogVO stVO = null;
                                    if(StringUtils.isNotBlank(roomId)){
                                        logVO = logMapper.getByRoomId(roomId,tolists);
                                        TimeOutLogVO outLogVO = logMapper.getByRoomId(roomId);//查询群聊信息
                                        if(outLogVO!=null && outLogVO.getStaffId().contains(from)){//如果群里里包含该客户证明是这个群聊的消息
                                            stVO = outLogVO;
                                        }
                                    }else{
                                        logVO = logMapper.getByUserId(from,tolists);
                                        stVO = logMapper.getByStaffId(from,tolists);
                                    }
                                    log.info("stVo======="+JSON.toJSONString(logVO));
                                    log.info("logVO======="+JSON.toJSONString(logVO));
                                    //判断是不是该员工和会话的聊天如果是判断超时规则
                                    if(logVO != null ){
                                        //if(logVO.getStaffId().contains(from) && tolist.contains(logVO.getUserId())){
                                        if(stVO != null){
                                            //查询该员工最新的超时规则
                                            SessionTimeOutVO timeOut = outMapper.getByStaffId(from,0);
                                            if(timeOut!=null){
                                                sendMessage(timeOut,stVO,msgDate,from);
                                            }else{//如果超时规则没有则删除该条聊天记录
                                                logMapper.deleteById(stVO.getId());
                                            }
                                        }

                                    }else{//添加客户与员工的聊天记录
                                        //保存新的聊天记录
                                        //获取发送人id，如果以"wo"或"wm"开头则为外部联系人的external_userid，
                                        if(from.startsWith("wo") || from.startsWith("wm")){
                                            //查询该员工最新的超时规则
                                            SessionTimeOutVO outVO = null;
                                            log.info("会话超时tolist======="+tolist);
                                            if(StringUtils.isNotBlank(roomId)){
                                                String[] ml = tolist.replaceAll("\"","").replace("[","").replace("]","").split(",");
                                                for (String s:ml) {
                                                    outVO = outMapper.getByStaffId(s,1);
                                                    if(outVO != null){
                                                        break;
                                                    }
                                                }
                                            }else{
                                                outVO = outMapper.getByStaffId(tolist.replace("[\"","").replace("\"]",""),0);
                                            }
                                            if(outVO != null){//员工超时规则不为空才保存
                                                String dateStr = DateUtil.dateTime(new Date(msgDate));
                                                Calendar calendar = Calendar.getInstance();
                                                Date date = new SimpleDateFormat("yyyy-MM-dd").parse(dateStr);
                                                calendar.setTime(date);
                                                //获取发送消息的时间为周几
                                                int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
                                                SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
                                                DateFormat da = DateFormat.getTimeInstance();
                                                String now = sdf.format(new Date(msgDate));//da.format(new Date(msgDate));//获取时间段
                                                String NewNow = da.format(WechatUtils.formatDate(String.valueOf(msgDate)));
                                                log.info("消息发送时间段now======="+now+"====newNow"+NewNow);
                                                List<Long> ids = new ArrayList<>();
                                                ids.add(outVO.getId());
                                                List<WorkDate> workDateList = dateMapper.getBySessionId(ids);
                                                List<EndStatement> endStatementList = statementMapper.getBySessionId(ids);
                                                boolean flag = false;
                                                for (WorkDate work:workDateList) {
                                                    String startTime = work.getStartTime();
                                                    String endTime = work.getEndTime();
                                                    flag = DateUtil.isEffectiveDate(sdf.parse(now),sdf.parse(startTime),sdf.parse(endTime));
                                                    if(flag){
                                                        break;
                                                    }
                                                }
                                                boolean statement = false;
                                                if("text".equals(msgType)&& endStatementList!=null){//如果消息为文本才需要判断结束语
                                                    for (EndStatement en:endStatementList) {
                                                        String mate = en.getMate();
                                                        String [] mates = mate.split(",");
                                                        for (String m:mates) {
                                                            if(content.contains(m)){
                                                                statement = true;
                                                                break;
                                                            }
                                                        }
                                                        if(statement){
                                                            break;
                                                        }
                                                    }
                                                }
                                                log.info("当前日期为周=="+dayOfWeek+"==flag==="+flag+"===statement==="+statement);
                                                if(outVO.getWorkDate().contains(String.valueOf(dayOfWeek)) && flag && !statement){//客户发送消息的时间满足上班时间和时间段,并且没有命中结束语
                                                    TimeOutLog log = new TimeOutLog();
                                                    log.setStatus("0");
                                                    if(StringUtils.isNotBlank(roomId)){
                                                        log.setStaffId(tolist.replace("[\"","").replace("\"]","").replace("\"",""));
                                                        log.setUserId(from);
                                                        log.setRoomId(roomId);
                                                    }else{
                                                        log.setStaffId(tolist.replace("[\"","").replace("\"]","").replace("\"",""));
                                                        log.setUserId(from);
                                                    }
                                                    //log.setSendTime(WechatUtils.formatDate(String.valueOf(msgDate)));
                                                    log.setSendTime(new Date(msgDate));
                                                    logMapper.save(log);
                                                }
                                            }
                                        }
                                    }

                                }
                            }
                            file.setSeq(LocalSEQ);
                            file.setUpdateTime(new Date());
                            list.add(file);
                        }
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
            log.info("保存会话存档数据：{}", JSON.toJSONString(list));
            if(list != null && list.size()>0){
                try {
                    Map<String,SessionFile> fileMap = new HashMap<>();
                    List<String> fileList = new ArrayList<>();
                    for (SessionFile fl:list) {
                        if("revoke".equals(fl.getMsgtype())){
                            JSONObject object = JSONObject.parseObject(fl.getContent());
                            String preMsgid = object.get("pre_msgid").toString();
                            fileMap.put(preMsgid,fl);
                            fileList.add(preMsgid);
                        }
                    }
                    for (SessionFile fl:list) {
                        if(fileMap.get(fl.getMsgid())!=null){
                            fl.setRevoke("1");
                        }else{
                            fl.setRevoke("0");
                        }
                    }

                    long start = new Date().getTime();
                    mongoTemplate.insertAll(list);
                    long end = new Date().getTime();
                    Query se = new Query();
                    se.addCriteria(Criteria.where("msgid").in(fileList));
                    Update up = new Update();
                    up.set("revoke","1");
                    mongoTemplate.updateMulti(se,up,SessionFile.class);
                    log.info("保存会话存档数据成功 =======>end, 耗时：{}s",((end - start) / 1000.0) );
                }catch (Exception e){
                    e.printStackTrace();
                    log.info("保存会话存档数据失败：{}", e);
                }

            }
        }
        Finance.FreeSlice(slice);
        return list.size() == limit ? true:false;
    }

    private static boolean isFlag(SessionFile file, String from, String tolist, String content, int t, List<KeyWordHit> hitList, KeywordVO vo, String[] mates, boolean flag) {
        for (String str: mates) {
            log.info("敏感词======="+str);
            if(content.contains(str)){
                flag = true;
                KeyWordHit wordHit = new KeyWordHit();
                wordHit.setType(t);
                wordHit.setTriggerId(from);
                wordHit.setTriggerTime(file.getCreateTime());
                wordHit.setLabel(vo.getTags());
                wordHit.setSensitives(vo.getSensitives());
                wordHit.setMate(vo.getMate());
                wordHit.setStaff(vo.getStaffName());
                wordHit.setKeyId(vo.getId());
                wordHit.setHitTime(file.getMsgtime());
                wordHit.setHitName(str);
                if(from.startsWith("wo") || from.startsWith("wm")){//如果命中的是客户提醒人为员工
                    wordHit.setStaffId(tolist.replace("[\"","").replace("\"]",""));
                }else{//如果命中的是员工提醒人还是为员工
                    wordHit.setStaffId(from);
                }
                hitList.add(wordHit);
                break;
            }
        }
        return flag;
    }

    /**
     *  获取触发人信息
     * @param outVO
     * @param staffId
     * @return
     */
    private String getTriggerName(SessionTimeOutVO outVO,String staffId){
        String staffName="";
//        if(StrUtil.isNotEmpty(outVO.getRoomId())){
//            CpCustGroup cpCustGroup=custGroupService.getById(outVO.getRoomId());
//            if(Objects.nonNull(cpCustGroup)){
//                staffName=cpCustGroup.getGroupName();
//            }
//        }else{
            ServerResponseEntity<StaffVO> staffResponseEntity=feignClient.getStaffByQiWeiUserId(staffId);
            StaffVO staffVO=staffResponseEntity.getData();
            if(staffVO != null ){
                staffName = staffVO.getStaffName();
            }
//        }
        return staffName;
    }

    /**
     * 保存图片、文件、音频等信息
     * @param fileModel
     * @return
     */
    private GridFSUploadStream insertChatData(FileModel fileModel){
        return mongoGridFSDao.save(fileModel);
    }
    //是否发送超时提醒
    private void sendMessage(SessionTimeOutVO outVO,TimeOutLogVO logVO,long time,String form){
        try{
            Date sendTime = logVO.getSendTime();//客户发送消息时间
            Date staffTime = new Date(time);//员工发送消息发送消息时间
            Instant start = sendTime.toInstant();
            Instant end = staffTime.toInstant();
            long min = ChronoUnit.MINUTES.between(start,end);//相隔几分钟
            long timeOut = Long.parseLong(outVO.getTimeOut());//超时分钟
            ServerResponseEntity<UserStaffCpRelationListVO> cpListVO = cpRelationFeignClient.getUserInfoByQiWeiUserId(form, logVO.getUserId());
            UserStaffCpRelationListVO listVO = cpListVO.getData();
            String userName = "";
            if(listVO != null ){
                userName = listVO.getQiWeiNickName();
            }
            //触发人信息
            String staffName=getTriggerName(outVO,form);

            //满足发送消息提醒outVO.getWorkDate().contains(String.valueOf(dayOfWeek)) && flag
            if(min>timeOut || min<0){//消息发送时间要在员工上班的时间段里才去算超时
                log.info("消息超时提醒，staffId：{}",form);
                TimeOutLog logOut = mapperFacade.map(logVO,TimeOutLog.class);
                logOut.setRuleName(outVO.getId()+outVO.getName());
                logOut.setTimeOutId(outVO.getId());
                logOut.setStatus("1");
                logOut.setUserName(userName);
                logOut.setTriggerTime(new Date());
                logMapper.update(logOut);
                log.info("超时提醒发送消息，staffId：{}",form);
                if(outVO.getRemindOpen()==0){
                    if(!outVO.getRemindPeople().contains("2")){
                        NotifyMsgTemplateDTO.TimeOut timeOutMsg=NotifyMsgTemplateDTO.TimeOut.builder()
                                .timeOutTime(String.valueOf(timeOut))
                                .offendUser(staffName)
                                .userName(userName)
                                .offendOutTime(cn.hutool.core.date.DateUtil.format(logOut.getTriggerTime(),"yyyy-MM-dd HH:mm:ss"))
                                .build();
                        pushService.timeOutNotify(NotifyMsgTemplateDTO.builder().qiWeiStaffId(form).timeOut(timeOutMsg).build());
                    }else if(!outVO.getRemindPeople().contains("1")){
                        String[] peop =outVO.getStaff().split(",");
                        for (String st:peop) {
                            NotifyMsgTemplateDTO.TimeOut timeOutMsg=NotifyMsgTemplateDTO.TimeOut.builder()
                                    .timeOutTime(String.valueOf(timeOut))
                                    .offendUser(staffName)
                                    .userName(userName)
                                    .offendOutTime(cn.hutool.core.date.DateUtil.format(logOut.getTriggerTime(),"yyyy-MM-dd HH:mm:ss"))
                                    .build();
                            pushService.timeOutNotify(NotifyMsgTemplateDTO.builder().qiWeiStaffId(st).timeOut(timeOutMsg).build());
                        }
                    }else if(outVO.getRemindPeople().contains("1") && outVO.getRemindPeople().contains("2")){
                        NotifyMsgTemplateDTO.TimeOut timeOutMsg=NotifyMsgTemplateDTO.TimeOut.builder()
                                .timeOutTime(String.valueOf(timeOut))
                                .offendUser(staffName)
                                .userName(userName)
                                .offendOutTime(cn.hutool.core.date.DateUtil.format(logOut.getTriggerTime(),"yyyy-MM-dd HH:mm:ss"))
                                .build();
                        pushService.timeOutNotify(NotifyMsgTemplateDTO.builder().qiWeiStaffId(form).timeOut(timeOutMsg).build());
                        String[] peop =outVO.getStaff().split(",");
                        for (String st:peop) {
                            timeOutMsg=NotifyMsgTemplateDTO.TimeOut.builder()
                                    .timeOutTime(String.valueOf(timeOut))
                                    .offendUser(staffName)
                                    .userName(userName)
                                    .offendOutTime(cn.hutool.core.date.DateUtil.format(logOut.getTriggerTime(),"yyyy-MM-dd HH:mm:ss"))
                                    .build();
                            pushService.timeOutNotify(NotifyMsgTemplateDTO.builder().qiWeiStaffId(st).timeOut(timeOutMsg).build());
                        }
                    }
                }else{
                    String finalUserName = userName;
                    String finalStaffName = staffName;
                    new Thread(()->{
                        try {
                            int outTime = Integer.parseInt(outVO.getRemindTime());
                            Thread.sleep(1000*60*60*outTime);
                            if(!outVO.getRemindPeople().contains("2")){
                                NotifyMsgTemplateDTO.TimeOut timeOutMsg=NotifyMsgTemplateDTO.TimeOut.builder()
                                        .timeOutTime(String.valueOf(timeOut))
                                        .offendUser(finalStaffName)
                                        .userName(finalUserName)
                                        .offendOutTime(cn.hutool.core.date.DateUtil.format(logOut.getTriggerTime(),"yyyy-MM-dd HH:mm:ss"))
                                        .build();
                                pushService.timeOutNotify(NotifyMsgTemplateDTO.builder().qiWeiStaffId(form).timeOut(timeOutMsg).build());
                            }else if(!outVO.getRemindPeople().contains("1")){
                                String[] peop =outVO.getStaff().split(",");
                                for (String st:peop) {
                                    NotifyMsgTemplateDTO.TimeOut timeOutMsg=NotifyMsgTemplateDTO.TimeOut.builder()
                                            .timeOutTime(String.valueOf(timeOut))
                                            .offendUser(finalStaffName)
                                            .userName(finalUserName)
                                            .offendOutTime(cn.hutool.core.date.DateUtil.format(logOut.getTriggerTime(),"yyyy-MM-dd HH:mm:ss"))
                                            .build();
                                    pushService.timeOutNotify(NotifyMsgTemplateDTO.builder().qiWeiStaffId(st).timeOut(timeOutMsg).build());
                                }
                            }else if(outVO.getRemindPeople().contains("1") && outVO.getRemindPeople().contains("2")){
                                NotifyMsgTemplateDTO.TimeOut timeOutMsg=NotifyMsgTemplateDTO.TimeOut.builder()
                                        .timeOutTime(String.valueOf(timeOut))
                                        .offendUser(finalStaffName)
                                        .userName(finalUserName)
                                        .offendOutTime(cn.hutool.core.date.DateUtil.format(logOut.getTriggerTime(),"yyyy-MM-dd HH:mm:ss"))
                                        .build();
                                pushService.timeOutNotify(NotifyMsgTemplateDTO.builder().qiWeiStaffId(form).timeOut(timeOutMsg).build());

//                                pushService.timeOutNotify(form, finalUserName,(int)timeOut);
                                String[] peop =outVO.getStaff().split(",");
                                for (String st:peop) {

                                    timeOutMsg=NotifyMsgTemplateDTO.TimeOut.builder()
                                            .timeOutTime(String.valueOf(timeOut))
                                            .offendUser(finalStaffName)
                                            .userName(finalUserName)
                                            .offendOutTime(cn.hutool.core.date.DateUtil.format(logOut.getTriggerTime(),"yyyy-MM-dd HH:mm:ss"))
                                            .build();
                                    pushService.timeOutNotify(NotifyMsgTemplateDTO.builder().qiWeiStaffId(st).timeOut(timeOutMsg).build());

//                                    pushService.timeOutNotify(st, finalUserName,(int)timeOut);
                                }
                            }
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    }).start();
                }
            }else{//不满足超时规则，删除该条聊天记录
                logMapper.deleteById(logVO.getId());
            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }
    //保存客户与员工的聊天记录
    private void saveTimeOut(){

    }

    private FileModel getFileModel(String msg,String type,String msgId){
        JSONObject object = JSONObject.parseObject(msg);
        FileModel fileModel = new FileModel();
        fileModel.setFileType(type);
        fileModel.setFileName(msgId);
        switch(type){
            case "image":
            case "video":
            case "file":
                fileModel.setLength((Long.valueOf(object.get("filesize").toString())));
                fileModel.setMD5(object.get("md5sum").toString());
                fileModel.setFileSDKField(object.get("sdkfileid").toString());
                break;
            case "voice":
                fileModel.setLength((Long.valueOf(object.get("voice_size").toString())));
                fileModel.setMD5(object.get("md5sum").toString());
                fileModel.setFileSDKField(object.get("sdkfileid").toString());
                break;
            case "emotion":
                fileModel.setLength((Long.valueOf(object.get("imagesize").toString())));
                fileModel.setMD5(object.get("md5sum").toString());
                fileModel.setFileSDKField(object.get("sdkfileid").toString());
                break;
        }
        return fileModel;
    }

    private boolean getMediaData(GridFSUploadStream gridFSUploadStream,String indexbuf, String sdkField){
        initSDK();
        long slice = Finance.NewMediaData();
        long ret = Finance.GetMediaData(sdk, indexbuf, sdkField, "", "",timeout,slice);
        if (ret != 0) {
            log.info("获取媒体数据 " + ret);
        } else {
            log.info("upload media  "+sdkField);
            byte[] b = Finance.GetData(slice);
            boolean isFinish = Finance.IsMediaDataFinish(slice) > 0;
            String outIndex = Finance.GetOutIndexBuf(slice);
            gridFSUploadStream.write(b);
            b = null;
            Finance.FreeMediaData(slice);
            if (isFinish) {
                log.info("upload media finish "+sdkField);
                gridFSUploadStream.flush();
                gridFSUploadStream.close();
            } else {
                getMediaData(gridFSUploadStream,outIndex, sdkField);
            }
            return true;
        }
        return false;
    }
    private boolean getEmotionData(StringBuilder stringBuilder,String indexbuf, String sdkField){
        initSDK();
        long slice = Finance.NewMediaData();
        long ret = Finance.GetMediaData(sdk, indexbuf, sdkField, "", "",timeout,slice);
        if (ret != 0) {
            log.info("获取媒体数据 " + ret);
        } else {
            log.info("getEmotionData  sdkField:{}",sdkField);
            byte[] b = Finance.GetData(slice);
            boolean isFinish = Finance.IsMediaDataFinish(slice) > 0;
            String outIndex = Finance.GetOutIndexBuf(slice);
            stringBuilder.append(Base64.getEncoder().encodeToString(b));
            b = null;
            Finance.FreeMediaData(slice);
            log.info("getEmotionData finish:{}",isFinish);
            if (isFinish) {
                return true;
            }
            getEmotionData(stringBuilder,outIndex, sdkField);
        }
        return false;
    }

    @Override
    public List<String> permitUser() {
        return null;
    }

    @Override
    public void singleAgree() {
        //查询所有员工和客户的信息
        UserStaffCpRelationSearchDTO dto = new UserStaffCpRelationSearchDTO();
        dto.setPageNum(1);
        dto.setPageSize(100);
        dto.setStatus(1);
        ServerResponseEntity<PageVO<UserStaffCpRelationListVO>> listVo = cpRelationFeignClient.pageWithStaff(dto);
        List<UserStaffCpRelationListVO> cpRelationListVOS = listVo.getData().getList();
        Integer page = listVo.getData().getPages();
        getStaffAgree(cpRelationListVOS,page,dto.getPageNum());
    }

    private void getStaffAgree(List<UserStaffCpRelationListVO> listVO,Integer page,Integer pageNum){
        log.info("page========"+page+"=====pageNum==="+pageNum);
        if(page == pageNum){
            saveStaffAgree(listVO);
        }else{
            saveStaffAgree(listVO);
            pageNum = pageNum+1;
            UserStaffCpRelationSearchDTO dto = new UserStaffCpRelationSearchDTO();
            dto.setPageNum(pageNum);
            dto.setPageSize(100);
            dto.setStatus(1);
            ServerResponseEntity<PageVO<UserStaffCpRelationListVO>> listVo = cpRelationFeignClient.pageWithStaff(dto);
            List<UserStaffCpRelationListVO> cpRelationListVOS = listVo.getData().getList();
            getStaffAgree(cpRelationListVOS,page,pageNum);
        }
    }

    private void saveStaffAgree(List<UserStaffCpRelationListVO> listVO){
        String result = "";
        WxCpCheckAgreeRequest req = new WxCpCheckAgreeRequest();
        List<WxCpCheckAgreeRequest.Info> i = new ArrayList<>();
        List<String> userIds = new ArrayList<>();
        List<String> staffIds = new ArrayList<>();
        try{
            //设置会话同意情况参数
            if(listVO != null && listVO.size()>0){
                List<Long> staffIdList=listVO.stream()
                        .filter(item->Objects.nonNull(item.getStaffId()))
                        .map(item->item.getStaffId()).collect(Collectors.toList());
                ServerResponseEntity<List<StaffVO>> staffResponse=feignClient.getStaffByIds(staffIdList);
                Map<Long,StaffVO> staffVOMap=LambdaUtils.toMap(staffResponse.getData().stream().filter(item->item.getStatus()==0&&item.getIsDelete()==0),StaffVO::getId);
                List<UserStaffCpRelationListVO> staffList = new ArrayList<>();
                for (UserStaffCpRelationListVO vo: listVO) {
                    StaffVO staffVO=staffVOMap.get(vo.getStaffId());
                    if(Objects.isNull(staffVO)){
                        log.info("获取会话同意情况失败，根据员工id查询未找到对应员工");
                        continue;
                    }
                    staffList.add(vo);
                    /*WxCpCheckAgreeRequest.Info info = new WxCpCheckAgreeRequest.Info();
                    info.setUserid(vo.getQiWeiStaffId());
                    info.setExteranalOpenId(vo.getQiWeiUserId());
                    *//*info.setUserid("HaiXiang");
                    info.setExteranalOpenId("wmmXETLgAAYNVXw-VAFvFYH3gFWz_R8Q");*//*
                    i.add(info);
                    userIds.add(vo.getQiWeiUserId());
                    staffIds.add(vo.getQiWeiStaffId());
                    *//*userIds.add("wmmXETLgAAYNVXw-VAFvFYH3gFWz_R8Q");
                    staffIds.add("HaiXiang");*/
                }
                if(staffList != null && staffList.size()>0){
                    for (UserStaffCpRelationListVO relationListVO:staffList) {
                        WxCpCheckAgreeRequest.Info info = new WxCpCheckAgreeRequest.Info();
                        info.setUserid(relationListVO.getQiWeiStaffId());
                        info.setExteranalOpenId(relationListVO.getQiWeiUserId());
                        i.add(info);
                        userIds.add(relationListVO.getQiWeiUserId());
                        staffIds.add(relationListVO.getQiWeiStaffId());
                    }
                }
            }
            /*WxCpCheckAgreeRequest.Info info = new WxCpCheckAgreeRequest.Info();
            info.setUserid("MingTianDeMingTian");
            info.setExteranalOpenId("wmmXETLgAAYNVXw-VAFvFYH3gFWz_R8Q");
            i.add(info);*/
            List<AgreeInfo> agreeVOList = agreeMapper.getAgree(userIds,staffIds);
            req.setInfo(i);
            RequestUtil util = new RequestUtil();
            String BASE_ADDRESS = "https://qyapi.weixin.qq.com/cgi-bin/msgaudit/check_single_agree";
            //调用会话同意情况接口
            SessionRequestMsg msg = util.requestJsonPostDone(BASE_ADDRESS,new MsgAuditAccessToken().getMSGAUDITAccessToken(configService),req.toJson());
            result = msg.getMessage();
            System.out.println(msg.getMessage());
            if(StringUtils.isNotBlank(msg.getMessage())){
                JSONObject object = JSONObject.parseObject(msg.getMessage());
                if("0".equals(object.getString("errcode"))){
                    String agree = object.get("agreeinfo").toString();
                    List<AgreeInfo> agreeInfos =JSONObject.parseArray(agree, AgreeInfo.class);
                    for (AgreeInfo agr: agreeInfos) {
                        agr.setChangeTime(WechatUtils.formatDate(agr.getStatusChangeTime().toString()));
                    }
                    Map<String, AgreeInfo> partsMap = agreeVOList.stream().collect(Collectors.toMap(ks ->
                            ks.getUserId()+ks.getExteranalOpenId(), key -> key));
                    List<String> keys = agreeVOList.stream().map(ag -> ag.getUserId()+ag.getExteranalOpenId()).collect(Collectors.toList());
                    List<AgreeInfo> addList = new ArrayList<>();
                    List<AgreeInfo> updateList = new ArrayList<>();
                    for (AgreeInfo vo:agreeInfos) {
                        if(keys.contains(vo.getUserId()+vo.getExteranalOpenId())){
                            AgreeInfo ag = partsMap.get(vo.getUserId()+vo.getExteranalOpenId());
                            vo.setId(ag.getId());
                            updateList.add(vo);
                        }else{
                            addList.add(vo);
                        }
                    }
                    if(addList != null && addList.size()>0){
                        agreeMapper.batchInsert(addList);
                    }
                    if(updateList != null && updateList.size()>0){
                        agreeMapper.batchUpdate(updateList);
                    }
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            log.info("调用同意情况，执行参数：{},返回结果：{}", JSONObject.toJSONString(req.toJson()),JSONObject.toJSONString(result));
        }
    }

    @Override
    public InputStream getFile(String fileName) throws Exception{
        return mongoGridFSDao.getFile(fileName);
    }

    @Override
    public void delete(String msgId) {
        searchDbDao.deleteById(msgId);
    }

}
