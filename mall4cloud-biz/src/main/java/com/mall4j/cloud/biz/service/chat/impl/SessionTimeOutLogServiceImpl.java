package com.mall4j.cloud.biz.service.chat.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.mall4j.cloud.api.biz.vo.SessionFileVO;
import com.mall4j.cloud.api.platform.dto.StaffQueryDTO;
import com.mall4j.cloud.api.platform.feign.StaffFeignClient;
import com.mall4j.cloud.api.platform.vo.StaffVO;
import com.mall4j.cloud.api.user.dto.UserStaffCpRelationSearchDTO;
import com.mall4j.cloud.api.user.feign.UserStaffCpRelationFeignClient;
import com.mall4j.cloud.api.user.vo.UserStaffCpRelationListVO;
import com.mall4j.cloud.biz.dto.chat.KeywordHitDTO;
import com.mall4j.cloud.biz.dto.chat.TimeOutLogDTO;
import com.mall4j.cloud.api.biz.dto.cp.NotifyMsgTemplateDTO;
import com.mall4j.cloud.biz.mapper.chat.SessionTimeOutMapper;
import com.mall4j.cloud.biz.mapper.chat.TimeOutLogMapper;
import com.mall4j.cloud.biz.model.chat.SessionFile;
import com.mall4j.cloud.biz.model.chat.TimeOutLog;
import com.mall4j.cloud.biz.model.cp.CpCustGroup;
import com.mall4j.cloud.biz.service.chat.SessionTimeOutLogService;
import com.mall4j.cloud.biz.service.cp.CustGroupService;
import com.mall4j.cloud.biz.service.cp.WxCpPushService;
import com.mall4j.cloud.biz.vo.chat.KeywordHitVO;
import com.mall4j.cloud.biz.vo.chat.SessionTimeOutVO;
import com.mall4j.cloud.biz.vo.chat.TimeOutLogVO;
import com.mall4j.cloud.biz.vo.cp.MaterialBrowseRecordByDayVO;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.util.PageUtil;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.common.util.LambdaUtils;
import lombok.extern.slf4j.Slf4j;
import ma.glasnost.orika.MapperFacade;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 超时回复统计实现类
 *
 */
@Slf4j
@Service
public class SessionTimeOutLogServiceImpl implements SessionTimeOutLogService {

    @Autowired
    private TimeOutLogMapper logMapper;
    @Autowired
    WxCpPushService pushService;
    @Autowired
    private MongoTemplate mongoTemplate;
    @Autowired
    SessionTimeOutMapper outMapper;
    @Autowired
    private UserStaffCpRelationFeignClient relationFeignClient;
    @Autowired
    private MapperFacade mapperFacade;
    @Autowired
    private StaffFeignClient feignClient;
    @Autowired
    private CustGroupService custGroupService;

    @Override
    public PageVO<TimeOutLogVO> page(PageDTO pageDTO, TimeOutLogDTO dto) {
        if(!initSelect(dto)){
            PageVO<TimeOutLogVO> pageVO=new PageVO<TimeOutLogVO>();
            pageVO.setPages(1);
            pageVO.setTotal(0L);
            pageVO.setList(null);
            return pageVO;
        }
        dto.setStatus("1");
        PageVO<TimeOutLogVO> VO = PageUtil.doPage(pageDTO, () -> logMapper.list(dto));
        List<TimeOutLogVO> list = VO.getList();
        addData(list);
        return PageUtil.mongodbPage(pageDTO, list,VO.getTotal());
    }

    private boolean  initSelect(TimeOutLogDTO dto){
        //根据触发人名称搜索
        boolean flag=true;
        if(StrUtil.isNotEmpty(dto.getUserName())){
            //筛选客户
            UserStaffCpRelationSearchDTO userStaffCpRelationSearchDTO=new UserStaffCpRelationSearchDTO();
            userStaffCpRelationSearchDTO.initPage();
            userStaffCpRelationSearchDTO.setQiWeiNickName(dto.getUserName());
            ServerResponseEntity<List<UserStaffCpRelationListVO>> responseEntity=relationFeignClient.getUserStaffRelBy(userStaffCpRelationSearchDTO);
            ServerResponseEntity.checkResponse(responseEntity);
            List<String> userQiWeiIds=responseEntity.getData().stream().filter(item->StrUtil.isNotEmpty(item.getQiWeiUserId())).map(item->item.getQiWeiUserId()).distinct().collect(Collectors.toList());
            if(CollUtil.isEmpty(userQiWeiIds)){
                flag=false;
            }else{
                dto.setUserIds(userQiWeiIds);
                dto.setUserName(null);
                flag=true;
            }
        }
        if(StrUtil.isNotEmpty(dto.getStaffName())){
            //筛选员工
            StaffQueryDTO staffQueryDTO=new StaffQueryDTO();
            staffQueryDTO.setStaffName(dto.getStaffName());
            ServerResponseEntity<List<StaffVO>> responseEntityStaff=feignClient.findByStaffQueryDTO(staffQueryDTO);
            ServerResponseEntity.checkResponse(responseEntityStaff);
            List<String> staffQiWeiIds=responseEntityStaff.getData().stream().filter(item->StrUtil.isNotEmpty(item.getQiWeiUserId())).map(item->item.getQiWeiUserId()).distinct().collect(Collectors.toList());
            if(CollUtil.isEmpty(staffQiWeiIds)){
                flag=false;
            }else{
                dto.setStaffQiWeiId(staffQiWeiIds);
                dto.setStaffName(null);
                flag=true;
            }

        }
        return flag;
    }

    private void addData(List<TimeOutLogVO> list){
        List<String> staffId = list.stream().map(TimeOutLogVO:: getStaffId).collect(Collectors.toList());
        List<String> customId = list.stream().map(TimeOutLogVO:: getUserId).collect(Collectors.toList());

        StaffQueryDTO staffDto = new StaffQueryDTO();
        staffDto.setQiWeiUserIds(staffId);
        ServerResponseEntity<List<StaffVO>> staffList = feignClient.findByStaffQueryDTO(staffDto);
        ServerResponseEntity.checkResponse(staffList);
        Map<String,StaffVO> staffVOMap = LambdaUtils.toMap(staffList.getData().stream()
                .filter(item->StrUtil.isNotEmpty(item.getQiWeiUserId())).distinct().collect(Collectors.toList())
                , StaffVO::getQiWeiUserId);

        UserStaffCpRelationSearchDTO userStaffCpRelationSearchDTO = new UserStaffCpRelationSearchDTO();
        userStaffCpRelationSearchDTO.setPageSize(10);
        userStaffCpRelationSearchDTO.setPageNum(1);
        userStaffCpRelationSearchDTO.setQiWeiUserIds(customId);
        ServerResponseEntity<List<UserStaffCpRelationListVO>> cpListVO = relationFeignClient.getUserStaffRelBy(userStaffCpRelationSearchDTO);
        ServerResponseEntity.checkResponse(cpListVO);
        Map<String,UserStaffCpRelationListVO> relationListVOMap = LambdaUtils.toMap(cpListVO.getData().stream().filter(item-> StrUtil.isNotEmpty(item.getQiWeiUserId())),UserStaffCpRelationListVO::getQiWeiUserId);

        for (TimeOutLogVO outLogVO:list) {
            if(staffVOMap.containsKey(outLogVO.getStaffId())){
                outLogVO.setStaffName(staffVOMap.get(outLogVO.getStaffId()).getStaffName());
            }

            if(relationListVOMap.containsKey(outLogVO.getUserId())){
                outLogVO.setUserName(relationListVOMap.get(outLogVO.getUserId()).getQiWeiNickName());
            }
        }
    }

    @Override
    public List<TimeOutLogVO> soldExcel(TimeOutLogDTO dto) {
        List<TimeOutLogVO> list=logMapper.list(dto);
        addData(list);
        return list;
    }

    @Override
    public List<TimeOutLogVO> outLogChart(TimeOutLogDTO dto) {
        Date startTime=DateUtil.parse(dto.getStartTime(),"yyyy-MM-dd HH:mm:ss");
        Date endTime=DateUtil.parse(dto.getEndTime(),"yyyy-MM-dd HH:mm:ss");
        List<DateTime> dateTimes=DateUtil.rangeToList(startTime,endTime, DateField.DAY_OF_YEAR);//按天
        List<TimeOutLogVO> backs=new ArrayList<>();
        for (DateTime dateTime : dateTimes) {
            String time=DateUtil.format(dateTime,"yyyy-MM-dd");
            TimeOutLogVO logVO=new TimeOutLogVO();
            logVO.setAddCount(0);
            logVO.setDayTime(time);
            backs.add(logVO);
        }
        List<TimeOutLogVO> list=logMapper.outLogChart(dto);
        if(CollUtil.isNotEmpty(list)){
            Map<String, TimeOutLogVO> dayVOMap= LambdaUtils.toMap(list,TimeOutLogVO::getDayTime);
            for (TimeOutLogVO dayVO : backs) {
                if(dayVOMap.containsKey(dayVO.getDayTime())){
                    dayVO.setAddCount(dayVOMap.get(dayVO.getDayTime()).getAddCount());
                }
            }
        }
        return backs;
    }

    @Override
    public TimeOutLogVO outLogCount(TimeOutLogDTO dto) {
        //查询所有的超时次数
        int sumCount = logMapper.getCount(dto);
        //查询时间段的超时次数
        int addCount = logMapper.getAddCount(dto);
        //查询去重的触发人数
        int count = logMapper.getCountPeople(dto);
        TimeOutLogVO VO = new TimeOutLogVO();
        VO.setAddCount(addCount);
        VO.setSumCount(sumCount);
        VO.setCountPeople(count);
        return VO;
    }

    @Override
    public void sendMessage() {
        PageDTO pageDTO = new PageDTO();
        pageDTO.setPageNum(1);
        pageDTO.setPageSize(10);
        TimeOutLogDTO dto = new TimeOutLogDTO();
        dto.setStatus("0");
        PageVO<TimeOutLogVO> logVOPageVO = PageUtil.doPage(pageDTO, () -> logMapper.list(dto));
        if(logVOPageVO != null && logVOPageVO.getTotal()>0){
            selectOutLog(logVOPageVO,logVOPageVO.getPages(),1);
        }
    }

    @Override
    public TimeOutLogVO getByTimeOutId(String timeOutId) {
        return logMapper.getByTimeOutId(timeOutId);
    }

    private void selectOutLog(PageVO<TimeOutLogVO> logVOPageVO,int pages,int pageNum){
        if(pages == pageNum){
            sendOutLogMsg(logVOPageVO);
        }else{
            sendOutLogMsg(logVOPageVO);
            pageNum = pageNum+1;
            TimeOutLogDTO dto = new TimeOutLogDTO();
            dto.setStatus("0");
            PageDTO pageDTO = new PageDTO();
            pageDTO.setPageNum(pageNum);
            pageDTO.setPageSize(10);
            PageVO<TimeOutLogVO> pageVO = PageUtil.doPage(pageDTO, () -> logMapper.list(dto));
            if(pageVO != null && pageVO.getTotal()>0){
                selectOutLog(pageVO,pages,pageNum);
            }
        }
    }

    private void sendOutLogMsg(PageVO<TimeOutLogVO> logVOPageVO){
        try{
            List<TimeOutLogVO> list = logVOPageVO.getList();
            for (TimeOutLogVO vo:list) {
                if(StringUtils.isNotBlank(vo.getRoomId())){
                    Query query = new Query();
                    query.addCriteria(Criteria.where("roomid").is(vo.getRoomId()));
                    query.limit(1);
                    query.with(Sort.by(Sort.Direction.DESC,"createTime"));
                    //查询该群聊最新的一条记录
                    List<SessionFile> file = mongoTemplate.find(query, SessionFile.class);
                    List<SessionFileVO> fileVO = mapperFacade.mapAsList(file, SessionFileVO.class);
                    log.info("群聊记录====="+file);
                    if(fileVO!=null && fileVO.size()>0){//如果查询到
                        SessionFileVO sessionFile = fileVO.get(0);
                        if(vo.getStaffId().contains(sessionFile.getFrom())){//表示最后一条信息是员工发的
                            extracted(vo, sessionFile,1);
                        }
                        //表示最新的数据为客户发送拿客户发送时间和当前时间做对比
                        if(sessionFile.getFrom().startsWith("wo") || sessionFile.getFrom().startsWith("wm")){
                            noSessionFile(vo,1);
                        }
                    }else{//如果没查询到拿客户发送时间和当前时间做对比
                        noSessionFile(vo,1);
                    }
                }else{
                    Query query = new Query();
                    query.limit(1);
                    query.addCriteria(Criteria.where("from").is(vo.getStaffId().replace("[\"","").replace("\"]","")));
                    query.addCriteria(Criteria.where("tolist").regex(".*"+vo.getUserId()+".*"));
                    query.with(Sort.by(Sort.Direction.DESC,"createTime"));
                    //查询员工的最后一次会话
                    List<SessionFile> file = mongoTemplate.find(query, SessionFile.class);
                    List<SessionFileVO> fileVO = mapperFacade.mapAsList(file, SessionFileVO.class);
                    if(fileVO != null && fileVO.size()>0){
                        extracted(vo, fileVO.get(0),0);
                    }else{//如果没查询到拿客户发送时间和当前时间做对比
                        noSessionFile(vo,0);
                    }
                }
            }
        }catch (Exception e){
            log.error("",e);
        }
    }

    /**
     *  获取触发人信息
     * @param staffId
     * @return
     */
    private String getTriggerName(String roomId,String staffId){
        log.info("超时提供获取触发人信息：roomId:{} staffId:{}",roomId,staffId);
        String staffName="";
        if(StrUtil.isNotEmpty(roomId)){
            CpCustGroup cpCustGroup=custGroupService.getById(roomId);
            if(Objects.nonNull(cpCustGroup)){
                staffName=cpCustGroup.getGroupName();
            }
        }else{
            ServerResponseEntity<StaffVO> staffResponseEntity=feignClient.getStaffByQiWeiUserId(staffId);
            StaffVO staffVO=staffResponseEntity.getData();
            if(staffVO != null ){
                staffName = staffVO.getStaffName();
            }
        }
        return staffName;
    }

    private void noSessionFile(TimeOutLogVO vo,int type) {
        SessionTimeOutVO outVO = null;
        String staffId ="";
        if(type == 1){
            String staff = vo.getStaffId();
            String[] ml = staff.replaceAll("\"","").replace("[","").replace("]","").split(",");
            for (String s:ml) {
                outVO = outMapper.getByStaffId(s,1);
                if(outVO != null){
                    staffId = s;
                    break;
                }
            }
        }else{
            outVO = outMapper.getByStaffId(vo.getStaffId().replace("[\"","").replace("\"]",""),0);
            staffId = vo.getStaffId().replace("[\"","").replace("\"]","");
        }
        if(outVO != null){
            log.info("nooutVo======="+outVO.getId());
            Date sendTime = vo.getSendTime();//客户发送消息时间
            Date endTime = new Date();
            Instant start = sendTime.toInstant();
            Instant end = endTime.toInstant();
            long min = ChronoUnit.MINUTES.between(start,end);//相隔几分钟
            long timeOut = Long.parseLong(outVO.getTimeOut());//超时分钟
            ServerResponseEntity<UserStaffCpRelationListVO> cpListVO = relationFeignClient.getUserInfoByQiWeiUserId(staffId, vo.getUserId());
            UserStaffCpRelationListVO listVO = cpListVO.getData();
            String userName = "";
            if(listVO != null ){
                userName = listVO.getQiWeiNickName();
            }
            //触发人信息
            String staffName=getTriggerName(vo.getRoomId(),staffId);

            if(outVO.getRemind()==1){//通知提醒开启才需要发送通知
                if(min>timeOut || min<0){
                    TimeOutLog logOut = mapperFacade.map(vo,TimeOutLog.class);
                    logOut.setRuleName(outVO.getId()+outVO.getName());
                    logOut.setTimeOutId(outVO.getId());
                    logOut.setStatus("1");
                    logOut.setTriggerTime(new Date());
                    logMapper.update(logOut);
                    if(outVO.getRemindOpen()==0){
                        if(!outVO.getRemindPeople().contains("2")){
//                            pushService.timeOutNotify(staffId,userName,(int)timeOut);
                            NotifyMsgTemplateDTO.TimeOut timeOutMsg=NotifyMsgTemplateDTO.TimeOut.builder()
                                    .timeOutTime(String.valueOf(timeOut))
                                    .offendUser(staffName)
                                    .userName(userName)
                                    .offendOutTime(cn.hutool.core.date.DateUtil.format(logOut.getTriggerTime(),"yyyy-MM-dd HH:mm:ss"))
                                    .build();
                            pushService.timeOutNotify(NotifyMsgTemplateDTO.builder().qiWeiStaffId(staffId).timeOut(timeOutMsg).build());
                        }else if(!outVO.getRemindPeople().contains("1")){
                            String[] peop =outVO.getStaff().split(",");
                            for (String st:peop) {
//                                pushService.timeOutNotify(st,userName,(int)timeOut);
                                NotifyMsgTemplateDTO.TimeOut timeOutMsg=NotifyMsgTemplateDTO.TimeOut.builder()
                                        .timeOutTime(String.valueOf(timeOut))
                                        .offendUser(staffName)
                                        .userName(userName)
                                        .offendOutTime(cn.hutool.core.date.DateUtil.format(logOut.getTriggerTime(),"yyyy-MM-dd HH:mm:ss"))
                                        .build();
                                pushService.timeOutNotify(NotifyMsgTemplateDTO.builder().qiWeiStaffId(st).timeOut(timeOutMsg).build());
                            }
                        }else if(outVO.getRemindPeople().contains("1") && outVO.getRemindPeople().contains("2")){
//                            pushService.timeOutNotify(staffId,userName,(int)timeOut);
                            NotifyMsgTemplateDTO.TimeOut timeOutMsg=NotifyMsgTemplateDTO.TimeOut.builder()
                                    .timeOutTime(String.valueOf(timeOut))
                                    .offendUser(staffName)
                                    .userName(userName)
                                    .offendOutTime(cn.hutool.core.date.DateUtil.format(logOut.getTriggerTime(),"yyyy-MM-dd HH:mm:ss"))
                                    .build();
                            pushService.timeOutNotify(NotifyMsgTemplateDTO.builder().qiWeiStaffId(staffId).timeOut(timeOutMsg).build());
                            String[] peop =outVO.getStaff().split(",");
                            for (String st:peop) {
//                                pushService.timeOutNotify(st,userName,(int)timeOut);
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
                        String finalStaffId = staffId;
                        SessionTimeOutVO finalOutVO = outVO;
                        new Thread(()->{
                            try {
                                int time = Integer.parseInt(finalOutVO.getRemindTime());
                                Thread.sleep(1000*60*60*time);
                                if(!finalOutVO.getRemindPeople().contains("2")){
//                                    pushService.timeOutNotify(finalStaffId, finalUserName,(int)timeOut);
                                    NotifyMsgTemplateDTO.TimeOut timeOutMsg=NotifyMsgTemplateDTO.TimeOut.builder()
                                            .timeOutTime(String.valueOf(timeOut))
                                            .offendUser(finalStaffName)
                                            .userName(finalUserName)
                                            .offendOutTime(cn.hutool.core.date.DateUtil.format(logOut.getTriggerTime(),"yyyy-MM-dd HH:mm:ss"))
                                            .build();
                                    pushService.timeOutNotify(NotifyMsgTemplateDTO.builder().qiWeiStaffId(finalStaffId).timeOut(timeOutMsg).build());
                                }else if(!finalOutVO.getRemindPeople().contains("1")){
                                    String[] peop = finalOutVO.getStaff().split(",");
                                    for (String st:peop) {
//                                        pushService.timeOutNotify(st, finalUserName,(int)timeOut);
                                        NotifyMsgTemplateDTO.TimeOut timeOutMsg=NotifyMsgTemplateDTO.TimeOut.builder()
                                                .timeOutTime(String.valueOf(timeOut))
                                                .offendUser(finalStaffName)
                                                .userName(finalUserName)
                                                .offendOutTime(cn.hutool.core.date.DateUtil.format(logOut.getTriggerTime(),"yyyy-MM-dd HH:mm:ss"))
                                                .build();
                                        pushService.timeOutNotify(NotifyMsgTemplateDTO.builder().qiWeiStaffId(st).timeOut(timeOutMsg).build());
                                    }
                                }else if(finalOutVO.getRemindPeople().contains("1") && finalOutVO.getRemindPeople().contains("2")){
//                                    pushService.timeOutNotify(finalStaffId, finalUserName,(int)timeOut);
                                    NotifyMsgTemplateDTO.TimeOut timeOutMsg=NotifyMsgTemplateDTO.TimeOut.builder()
                                            .timeOutTime(String.valueOf(timeOut))
                                            .offendUser(finalStaffName)
                                            .userName(finalUserName)
                                            .offendOutTime(cn.hutool.core.date.DateUtil.format(logOut.getTriggerTime(),"yyyy-MM-dd HH:mm:ss"))
                                            .build();
                                    pushService.timeOutNotify(NotifyMsgTemplateDTO.builder().qiWeiStaffId(finalStaffId).timeOut(timeOutMsg).build());
                                    String[] peop = finalOutVO.getStaff().split(",");
                                    for (String st:peop) {
//                                        pushService.timeOutNotify(st, finalUserName,(int)timeOut);
                                        timeOutMsg=NotifyMsgTemplateDTO.TimeOut.builder()
                                                .timeOutTime(String.valueOf(timeOut))
                                                .offendUser(finalStaffName)
                                                .userName(finalUserName)
                                                .offendOutTime(cn.hutool.core.date.DateUtil.format(logOut.getTriggerTime(),"yyyy-MM-dd HH:mm:ss"))
                                                .build();
                                        pushService.timeOutNotify(NotifyMsgTemplateDTO.builder().qiWeiStaffId(st).timeOut(timeOutMsg).build());
                                    }
                                }
                            } catch (InterruptedException e) {
                                throw new RuntimeException(e);
                            }
                        }).start();
                    }
                }
            }
        }
    }

    private void extracted(TimeOutLogVO vo, SessionFileVO sessionFile, int type) {
        String staffId = sessionFile.getFrom();
        SessionTimeOutVO outVO = outMapper.getByStaffId(staffId,type);
        log.info("extracted-->SessionTimeOutVO:{}", JSON.toJSONString(outVO));
        Date sendTime = vo.getSendTime();//客户发送消息时间
        Date staffTime = sessionFile.getCreateTime();//员工发送消息发送消息时间
        Instant start = sendTime.toInstant();
        Instant end = staffTime.toInstant();
        long min = ChronoUnit.MINUTES.between(start,end);//相隔几分钟
        long timeOut = Long.parseLong(outVO.getTimeOut());//超时分钟
        log.info("min==============="+outVO.getId()+"min=="+min+"timeout==="+timeOut+"logid=="+vo.getId());
        ServerResponseEntity<UserStaffCpRelationListVO> cpListVO = relationFeignClient.getUserInfoByQiWeiUserId(staffId, vo.getUserId());
        UserStaffCpRelationListVO listVO = cpListVO.getData();
        String userName = "";
        if(listVO != null ){
            userName = listVO.getQiWeiNickName();
        }
        //触发人信息
        String staffName=getTriggerName(vo.getRoomId(),staffId);

        if(outVO.getRemind()==1){//通知提醒开启才需要发送通知
            if(min>timeOut  || min<0){
                TimeOutLog logOut = mapperFacade.map(vo,TimeOutLog.class);
                logOut.setRuleName(outVO.getId()+outVO.getName());
                logOut.setTimeOutId(outVO.getId());
                logOut.setStatus("1");
                logOut.setTriggerTime(new Date());
                logMapper.update(logOut);
                if(outVO.getRemindOpen()==0){
                    if(!outVO.getRemindPeople().contains("2")){
//                        pushService.timeOutNotify(staffId,userName,(int)timeOut);
                        NotifyMsgTemplateDTO.TimeOut timeOutMsg=NotifyMsgTemplateDTO.TimeOut.builder()
                                .timeOutTime(String.valueOf(timeOut))
                                .offendUser(staffName)
                                .userName(userName)
                                .offendOutTime(cn.hutool.core.date.DateUtil.format(logOut.getTriggerTime(),"yyyy-MM-dd HH:mm:ss"))
                                .build();
                        pushService.timeOutNotify(NotifyMsgTemplateDTO.builder().qiWeiStaffId(staffId).timeOut(timeOutMsg).build());
                    }else if(!outVO.getRemindPeople().contains("1")){
                        String[] peop =outVO.getStaff().split(",");
                        for (String st:peop) {
//                            pushService.timeOutNotify(st,userName,(int)timeOut);
                            NotifyMsgTemplateDTO.TimeOut timeOutMsg=NotifyMsgTemplateDTO.TimeOut.builder()
                                    .timeOutTime(String.valueOf(timeOut))
                                    .offendUser(staffName)
                                    .userName(userName)
                                    .offendOutTime(cn.hutool.core.date.DateUtil.format(logOut.getTriggerTime(),"yyyy-MM-dd HH:mm:ss"))
                                    .build();
                            pushService.timeOutNotify(NotifyMsgTemplateDTO.builder().qiWeiStaffId(st).timeOut(timeOutMsg).build());
                        }
                    }else if(outVO.getRemindPeople().contains("1") && outVO.getRemindPeople().contains("2")){
//                        pushService.timeOutNotify(staffId,userName,(int)timeOut);
                        NotifyMsgTemplateDTO.TimeOut timeOutMsg=NotifyMsgTemplateDTO.TimeOut.builder()
                                .timeOutTime(String.valueOf(timeOut))
                                .offendUser(staffName)
                                .userName(userName)
                                .offendOutTime(cn.hutool.core.date.DateUtil.format(logOut.getTriggerTime(),"yyyy-MM-dd HH:mm:ss"))
                                .build();
                        pushService.timeOutNotify(NotifyMsgTemplateDTO.builder().qiWeiStaffId(staffId).timeOut(timeOutMsg).build());
                        String[] peop =outVO.getStaff().split(",");
                        for (String st:peop) {
//                            pushService.timeOutNotify(st,userName,(int)timeOut);
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
                            int time = Integer.parseInt(outVO.getRemindTime());
                            Thread.sleep(1000*60*60*time);
                            if(!outVO.getRemindPeople().contains("2")){
//                                pushService.timeOutNotify(staffId, finalUserName,(int)timeOut);
                                NotifyMsgTemplateDTO.TimeOut timeOutMsg=NotifyMsgTemplateDTO.TimeOut.builder()
                                        .timeOutTime(String.valueOf(timeOut))
                                        .offendUser(finalStaffName)
                                        .userName(finalUserName)
                                        .offendOutTime(cn.hutool.core.date.DateUtil.format(logOut.getTriggerTime(),"yyyy-MM-dd HH:mm:ss"))
                                        .build();
                                pushService.timeOutNotify(NotifyMsgTemplateDTO.builder().qiWeiStaffId(staffId).timeOut(timeOutMsg).build());
                            }else if(!outVO.getRemindPeople().contains("1")){
                                String[] peop =outVO.getStaff().split(",");
                                for (String st:peop) {
//                                    pushService.timeOutNotify(st, finalUserName,(int)timeOut);
                                    NotifyMsgTemplateDTO.TimeOut timeOutMsg=NotifyMsgTemplateDTO.TimeOut.builder()
                                            .timeOutTime(String.valueOf(timeOut))
                                            .offendUser(finalStaffName)
                                            .userName(finalUserName)
                                            .offendOutTime(cn.hutool.core.date.DateUtil.format(logOut.getTriggerTime(),"yyyy-MM-dd HH:mm:ss"))
                                            .build();
                                    pushService.timeOutNotify(NotifyMsgTemplateDTO.builder().qiWeiStaffId(st).timeOut(timeOutMsg).build());
                                }
                            }else if(outVO.getRemindPeople().contains("1") && outVO.getRemindPeople().contains("2")){
//                                pushService.timeOutNotify(staffId, finalUserName,(int)timeOut);
                                NotifyMsgTemplateDTO.TimeOut timeOutMsg=NotifyMsgTemplateDTO.TimeOut.builder()
                                        .timeOutTime(String.valueOf(timeOut))
                                        .offendUser(finalStaffName)
                                        .userName(finalUserName)
                                        .offendOutTime(cn.hutool.core.date.DateUtil.format(logOut.getTriggerTime(),"yyyy-MM-dd HH:mm:ss"))
                                        .build();
                                pushService.timeOutNotify(NotifyMsgTemplateDTO.builder().qiWeiStaffId(staffId).timeOut(timeOutMsg).build());
                                String[] peop =outVO.getStaff().split(",");
                                for (String st:peop) {
//                                    pushService.timeOutNotify(st, finalUserName,(int)timeOut);
                                    timeOutMsg=NotifyMsgTemplateDTO.TimeOut.builder()
                                            .timeOutTime(String.valueOf(timeOut))
                                            .offendUser(finalStaffName)
                                            .userName(finalUserName)
                                            .offendOutTime(cn.hutool.core.date.DateUtil.format(logOut.getTriggerTime(),"yyyy-MM-dd HH:mm:ss"))
                                            .build();
                                    pushService.timeOutNotify(NotifyMsgTemplateDTO.builder().qiWeiStaffId(st).timeOut(timeOutMsg).build());
                                }
                            }
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    }).start();
                }
            }
        }
    }


}
