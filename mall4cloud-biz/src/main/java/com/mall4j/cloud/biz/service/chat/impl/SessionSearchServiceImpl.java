package com.mall4j.cloud.biz.service.chat.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.mall4j.cloud.api.biz.vo.SoldSessionFileVO;
import com.mall4j.cloud.api.biz.vo.StaffSessionVO;
import com.mall4j.cloud.api.platform.dto.StaffQueryDTO;
import com.mall4j.cloud.api.platform.dto.StaffQueryPageDTO;
import com.mall4j.cloud.api.platform.feign.StaffFeignClient;
import com.mall4j.cloud.api.platform.vo.StaffVO;
import com.mall4j.cloud.api.user.dto.UserStaffCpRelationSearchDTO;
import com.mall4j.cloud.api.user.feign.UserStaffCpRelationFeignClient;
import com.mall4j.cloud.api.user.vo.UserStaffCpRelationListVO;
import com.mall4j.cloud.api.biz.dto.cp.chat.SessionFileDTO;
import com.mall4j.cloud.biz.mapper.chat.SessionSearchDbDao;
import com.mall4j.cloud.api.biz.vo.SessionFileVO;
import com.mall4j.cloud.biz.model.chat.SessionFile;
import com.mall4j.cloud.biz.model.cp.CpCustGroup;
import com.mall4j.cloud.biz.service.chat.SessionSearchService;
import com.mall4j.cloud.biz.service.cp.CustGroupService;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.util.PageUtil;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.common.util.DateUtil;
import com.mall4j.cloud.common.util.LambdaUtils;
import lombok.extern.slf4j.Slf4j;
import ma.glasnost.orika.MapperFacade;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.*;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Function;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * 会话存档检索实现类
 *
 */
@Slf4j
@Service
public class SessionSearchServiceImpl implements SessionSearchService {

    @Autowired
    private MongoTemplate mongoTemplate;
    @Autowired
    private SessionSearchDbDao searchDbDao;
    @Autowired
    private StaffFeignClient feignClient;
    @Autowired
    private UserStaffCpRelationFeignClient relationFeignClient;
    @Autowired
    private CustGroupService custGroupService;
    @Autowired
    private MapperFacade mapperFacade;

    @Override
    public PageVO<SessionFileVO> page(PageDTO pageDTO, SessionFileDTO dto) {
        int skip = (pageDTO.getPageNum()-1) * pageDTO.getPageSize();
        Query query = new Query();
        //mongodb 模糊查询
        if(StringUtils.isNotBlank(dto.getContent())){
            query.addCriteria(Criteria.where("content").regex(".*"+dto.getContent()+".*"));
        }
        //dto.setStartTime("2023-11-16 00:00:00");
        //dto.setEndTime("2023-11-17 23:59:59");
        if(StringUtils.isNotBlank(dto.getStartTime()) && StringUtils.isNotBlank(dto.getEndTime())){
            Date start = DateUtil.dateTime("yyyy-MM-dd HH:mm:ss",dto.getStartTime());
            Date end = DateUtil.dateTime("yyyy-MM-dd HH:mm:ss",dto.getEndTime());
            query.addCriteria(Criteria.where("createTime").gte(start).lte(end));
        }
        if(StringUtils.isNotBlank(dto.getFrom())){
            query.addCriteria(Criteria.where("from").is(dto.getFrom()));
        }
        if(dto.getQiWeiId() != null && dto.getQiWeiId().size()>0){
            query.addCriteria(Criteria.where("from").in(dto.getQiWeiId()));
        }
        if(StringUtils.isNotBlank(dto.getTolist())){
            query.addCriteria(Criteria.where("tolist").regex(".*"+dto.getTolist()+".*"));
        }
        if(StringUtils.isNotBlank(dto.getMsgtype())){
            query.addCriteria(Criteria.where("msgtype").is(dto.getMsgtype()));
        }
        if(StringUtils.isNotBlank(dto.getRoomid())){
            query.addCriteria(Criteria.where("roomid").is(dto.getRoomid()));
        }
        //先查询总条数
        long total = mongoTemplate.count(query, SessionFile.class);
        System.out.println(total);
        query.skip(skip);
        query.limit(pageDTO.getPageSize());
        //查询会话列表
        List<SessionFile> file = mongoTemplate.find(query, SessionFile.class);
        List<SessionFileVO> fileVO = mapperFacade.mapAsList(file, SessionFileVO.class);
        List<String> staffIds = fileVO.stream().map(SessionFileVO::getFrom).collect(Collectors.toList());
        List<SessionFileVO> roomList = fileVO.stream().filter(item->StringUtils.isNotBlank(item.getRoomid())).collect(Collectors.toList());
        List<String>  roomIds = new ArrayList<>();
        if(roomList != null && roomList.size()>0){
            roomIds = roomList.stream().map(SessionFileVO::getRoomid).collect(Collectors.toList());
        }
        List<SessionFileVO> userList = fileVO.stream().filter(item->StringUtils.isBlank(item.getRoomid()) && StringUtils.isNotBlank(item.getTolist())).collect(Collectors.toList());
        List<String> userIds = new ArrayList<>();
        if(userList != null && userList.size()>0){
            for (SessionFileVO s:userList) {
                userIds.add(s.getTolist().replace("\"","").replace("[","").replace("]",""));
            }
        }
        //发送方员工信息
        StaffQueryDTO staffDto = new StaffQueryDTO();
        staffDto.setQiWeiUserIds(staffIds);
        ServerResponseEntity<List<StaffVO>> staffList = feignClient.findByStaffQueryDTO(staffDto);
        ServerResponseEntity.checkResponse(staffList);
        Map<String,StaffVO> staffVOMap = LambdaUtils.toMap(staffList.getData().stream().filter(item->StrUtil.isNotEmpty(item.getQiWeiUserId())),StaffVO::getQiWeiUserId);
        //发送方客户信息
        UserStaffCpRelationSearchDTO userStaffCpRelationSearchDTO = new UserStaffCpRelationSearchDTO();
        userStaffCpRelationSearchDTO.initPage();
        userStaffCpRelationSearchDTO.setQiWeiUserIds(staffIds);
        ServerResponseEntity<List<UserStaffCpRelationListVO>> cpListVO = relationFeignClient.getUserStaffRelBy(userStaffCpRelationSearchDTO);
        ServerResponseEntity.checkResponse(cpListVO);
        Map<String,UserStaffCpRelationListVO> relationListVOMap = LambdaUtils.toMap(cpListVO.getData().stream().filter(item-> StrUtil.isNotEmpty(item.getQiWeiUserId())),UserStaffCpRelationListVO::getQiWeiUserId);
        //接收方员工信息
        staffDto.setQiWeiUserIds(userIds);
        ServerResponseEntity<List<StaffVO>> staList = feignClient.findByStaffQueryDTO(staffDto);
        ServerResponseEntity.checkResponse(staList);
        Map<String,StaffVO> staffMap = LambdaUtils.toMap(staList.getData().stream().filter(item->StrUtil.isNotEmpty(item.getQiWeiUserId())),StaffVO::getQiWeiUserId);
        //接收方客户信息
        userStaffCpRelationSearchDTO.setQiWeiUserIds(userIds);
        ServerResponseEntity<List<UserStaffCpRelationListVO>> userStaff = relationFeignClient.getUserStaffRelBy(userStaffCpRelationSearchDTO);
        ServerResponseEntity.checkResponse(userStaff);
        Map<String,UserStaffCpRelationListVO> userStaffMap = LambdaUtils.toMap(userStaff.getData().stream().filter(item-> StrUtil.isNotEmpty(item.getQiWeiUserId())),UserStaffCpRelationListVO::getQiWeiUserId);
        //接收方群聊名称
        List<CpCustGroup> groupList = custGroupService.getByIds(roomIds);
        Map<String,CpCustGroup> groupMap = LambdaUtils.toMap(groupList.stream().filter(item-> StrUtil.isNotEmpty(item.getId())),CpCustGroup::getId);
        //填充数据
        for (SessionFileVO s: fileVO) {
            if(StringUtils.isNotBlank(s.getTolist())){
                s.setTolist(s.getTolist().replace("\"","").replace("[","").replace("]",""));
            }
            if(staffVOMap.containsKey(s.getFrom())){//发送方是员工
                s.setStaffAvatar(staffVOMap.get(s.getFrom()).getAvatar());
                s.setStaffName(staffVOMap.get(s.getFrom()).getStaffName());
            }
            if(relationListVOMap.containsKey(s.getFrom())){//发送方是客户
                //s.setUserAvatar(relationListVOMap.get(s.getFrom()).getAvatar());
                //s.setUserName(relationListVOMap.get(s.getFrom()).getQiWeiNickName());
                s.setStaffAvatar(relationListVOMap.get(s.getFrom()).getAvatar());
                s.setStaffName(relationListVOMap.get(s.getFrom()).getStaffName());
            }
            if(StringUtils.isNotBlank(s.getRoomid())){
                if(groupMap.containsKey(s.getRoomid())){//接收方为群聊
                    s.setUserName(groupMap.get(s.getRoomid()).getGroupName());
                }
            }else{
                if(staffMap.containsKey(s.getTolist())){//接收方为员工
                    s.setUserAvatar(staffMap.get(s.getTolist()).getAvatar());
                    s.setUserName(staffMap.get(s.getTolist()).getStaffName());
                }
                if(userStaffMap.containsKey(s.getTolist())){//接收方为客户
                    s.setUserAvatar(userStaffMap.get(s.getTolist()).getAvatar());
                    s.setUserName(userStaffMap.get(s.getTolist()).getQiWeiNickName());
                }
            }
        }

//        List<String> staffId = fileVO.stream().map(SessionFileVO::getFrom).collect(Collectors.toList());
//        StaffQueryDTO staffDto = new StaffQueryDTO();
//        staffDto.setQiWeiUserIds(staffId);
//        ServerResponseEntity<List<StaffVO>> staffList = feignClient.findByStaffQueryDTO(staffDto);
//        ServerResponseEntity.checkResponse(staffList);
//        List<StaffVO> vo = staffList.getData();
//        UserStaffCpRelationSearchDTO userStaffCpRelationSearchDTO = new UserStaffCpRelationSearchDTO();
//        userStaffCpRelationSearchDTO.setPageSize(10);
//        userStaffCpRelationSearchDTO.setPageNum(1);
//        userStaffCpRelationSearchDTO.setQiWeiUserIds(staffId);
//        ServerResponseEntity<PageVO<UserStaffCpRelationListVO>> cpListVO = relationFeignClient.pageWithStaff(userStaffCpRelationSearchDTO);
//        List<UserStaffCpRelationListVO> cpList = new ArrayList<>();
//        if(cpListVO!=null && cpListVO.getData()!=null){
//            cpList = cpListVO.getData().getList();
//        }
//        for (SessionFileVO s: fileVO) {
//            if(vo!=null && vo.size()>0){ //发送方是员工
//                for (StaffVO staffVO :vo) {
//                    if(StringUtils.isNotBlank(staffVO.getQiWeiUserId()) && staffVO.getQiWeiUserId().equals(s.getFrom())){
//                        s.setStaffAvatar(staffVO.getAvatar());
//                        s.setStaffName(staffVO.getStaffName());
//                        break;
//                    }
//                }
//            }
//            if(cpList!=null && cpList.size()>0){ //发送方是客户
//                for (UserStaffCpRelationListVO listVO:cpList) {
//                    if(StringUtils.isNotBlank(listVO.getQiWeiUserId()) && listVO.getQiWeiUserId().equals(s.getFrom())){
//                        s.setUserAvatar(listVO.getAvatar());
//                        s.setUserName(listVO.getQiWeiNickName());
//                        break;
//                    }
//                }
//            }
//        }
        return PageUtil.mongodbPage(pageDTO, fileVO,total);
    }

    /**
     * 导出
     * @param dto
     * @return
     */
    @Override
    public List<SoldSessionFileVO> soldSessionFileExcel(SessionFileDTO dto) {
        Query query = new Query();
        //mongodb 模糊查询
        if(StringUtils.isNotBlank(dto.getContent())){
            query.addCriteria(Criteria.where("content").regex(".*"+dto.getContent()+".*"));
        }
        if(StringUtils.isNotBlank(dto.getStartTime()) && StringUtils.isNotBlank(dto.getEndTime())){
            Date start = DateUtil.dateTime("yyyy-MM-dd HH:mm:ss",dto.getStartTime());
            Date end = DateUtil.dateTime("yyyy-MM-dd HH:mm:ss",dto.getEndTime());
            query.addCriteria(Criteria.where("createTime").gte(start).lte(end));
        }
        if(StringUtils.isNotBlank(dto.getFrom())){
            query.addCriteria(Criteria.where("from").is(dto.getFrom()));
        }
        if(dto.getQiWeiId() != null && dto.getQiWeiId().size()>0){
            query.addCriteria(Criteria.where("from").in(dto.getQiWeiId()));
        }
        if(StringUtils.isNotBlank(dto.getTolist())){
            query.addCriteria(Criteria.where("tolist").regex(".*"+dto.getTolist()+".*"));
        }
        if(StringUtils.isNotBlank(dto.getMsgtype())){
            query.addCriteria(Criteria.where("msgtype").is(dto.getMsgtype()));
        }
        if(StringUtils.isNotBlank(dto.getRoomid())){
            query.addCriteria(Criteria.where("roomid").is(dto.getRoomid()));
        }
        //查询会话列表
        List<SessionFile> file = mongoTemplate.find(query, SessionFile.class);
        List<SessionFileVO> fileVOs = mapperFacade.mapAsList(file, SessionFileVO.class);
        if(CollUtil.isEmpty(fileVOs)){
            return ListUtil.empty();
        }
        //填充明细数据
        addDetailData(fileVOs,true);

        List<SoldSessionFileVO> fileVOS=new ArrayList<>();
        for (SessionFileVO sessionFileVO : fileVOs) {
            SoldSessionFileVO fileVO=new SoldSessionFileVO();
            fileVO.setMsgtime(sessionFileVO.getCreateTime());
            fileVO.setFrom(sessionFileVO.getFrom());
            if(sessionFileVO.getFrom().startsWith("wm")){
                fileVO.setFromName(sessionFileVO.getUserName());
            }else{
                fileVO.setFromName(sessionFileVO.getStaffName());
            }
            fileVO.setTolist(sessionFileVO.getTolist());
            if(sessionFileVO.getTolist().startsWith("wm")){
                fileVO.setTolistName(sessionFileVO.getUserName());
            }else{
                fileVO.setTolistName(sessionFileVO.getStaffName());
            }
            fileVO.setContent(sessionFileVO.getContent());
            fileVO.setMsgtype(sessionFileVO.getMsgtype());
            //TODO 附件ID

            fileVOS.add(fileVO);
        }

        return fileVOS;
    }

    public static void main(String[] strings){
        String tolist="\"HaiXiang\"";
        System.out.println(tolist.replace("\"",""));

    }
    private void addDetailData(List<SessionFileVO> fileVO,boolean appendToList){
        List<String> staffIds1 = fileVO.stream().filter(item->StrUtil.isNotEmpty(item.getFrom())).map(SessionFileVO::getFrom).collect(Collectors.toList());
        List<String> staffIds2 = fileVO.stream().filter(item->StrUtil.isNotEmpty(item.getTolist())).map(SessionFileVO::getTolist).collect(Collectors.toList());
        staffIds1.addAll(staffIds2);
        List<String> staffIds=new ArrayList<>();
        List<String> userIds=new ArrayList<>();
        for (String staffId : staffIds1) {
            if((!staffId.startsWith("wo") && !staffId.startsWith("wm")) && !staffIds.contains(staffId)){
                staffIds.add(staffId.replace("\"","").replace("[","").replace("]",""));
            }
            if(staffId.startsWith("wm") && !userIds.contains(staffId)){
                userIds.add(staffId.replace("\"","").replace("[","").replace("]",""));
            }
        }
        //员工信息
        StaffQueryDTO staffDto = new StaffQueryDTO();
        staffDto.setQiWeiUserIds(staffIds);
        ServerResponseEntity<List<StaffVO>> staffList = feignClient.findByStaffQueryDTO(staffDto);
        ServerResponseEntity.checkResponse(staffList);
        Map<String,StaffVO> staffVOMap = LambdaUtils.toMap(staffList.getData().stream().filter(item->StrUtil.isNotEmpty(item.getQiWeiUserId())),StaffVO::getQiWeiUserId);
       //客户信息
        UserStaffCpRelationSearchDTO userStaffCpRelationSearchDTO = new UserStaffCpRelationSearchDTO();
        userStaffCpRelationSearchDTO.initPage();
        userStaffCpRelationSearchDTO.setQiWeiUserIds(userIds);
        ServerResponseEntity<List<UserStaffCpRelationListVO>> cpListVO = relationFeignClient.getUserStaffRelBy(userStaffCpRelationSearchDTO);
        ServerResponseEntity.checkResponse(cpListVO);
        Map<String,UserStaffCpRelationListVO> relationListVOMap = LambdaUtils.toMap(cpListVO.getData().stream().filter(item-> StrUtil.isNotEmpty(item.getQiWeiUserId())),UserStaffCpRelationListVO::getQiWeiUserId);
       //填充数据
        for (SessionFileVO s: fileVO) {
            if(StringUtils.isNotBlank(s.getTolist())){
                s.setTolist(s.getTolist().replace("\"","").replace("[","").replace("]",""));
            }
            if(staffVOMap.containsKey(s.getFrom())){//发送方是员工
                s.setStaffAvatar(staffVOMap.get(s.getFrom()).getAvatar());
                s.setStaffName(staffVOMap.get(s.getFrom()).getStaffName());
            }
            if(relationListVOMap.containsKey(s.getFrom())){//发送方是客户
                s.setUserAvatar(relationListVOMap.get(s.getFrom()).getAvatar());
                s.setUserName(relationListVOMap.get(s.getFrom()).getQiWeiNickName());
            }
            if(appendToList){
                if(staffVOMap.containsKey(s.getTolist())){//接收方是客户
                    s.setStaffAvatar(staffVOMap.get(s.getTolist()).getAvatar());
                    s.setStaffName(staffVOMap.get(s.getTolist()).getStaffName());
                }
                if(relationListVOMap.containsKey(s.getTolist())){//接收方是客户
                    s.setUserAvatar(relationListVOMap.get(s.getTolist()).getAvatar());
                    s.setUserName(relationListVOMap.get(s.getTolist()).getQiWeiNickName());
                }
            }

        }
    }

    @Override
    public PageVO<SessionFileVO> getSessionFile(PageDTO pageDTO, SessionFileDTO dto) {
        int skip = (pageDTO.getPageNum()-1) * pageDTO.getPageSize();
        int size = pageDTO.getPageSize();
        Query query = new Query();
        Query lastQuery = new Query();
        if(StringUtils.isNotBlank(dto.getFrom()) && StringUtils.isNotBlank(dto.getTolist())){
            Criteria from = new Criteria().andOperator(Criteria.where("from").is(dto.getFrom()),Criteria.where("tolist").regex(".*"+dto.getTolist()+".*"));
            Criteria toList = new Criteria().andOperator(Criteria.where("tolist").regex(".*"+dto.getFrom()+".*"),Criteria.where("from").regex(".*"+dto.getTolist()+".*"));
            query.addCriteria(new Criteria().orOperator(from,toList));
        }
        if(StringUtils.isNotBlank(dto.getStartTime())&&StringUtils.isNotBlank(dto.getEndTime())){//根据时间段筛选查询条件
            Date start = DateUtil.dateTime("yyyy-MM-dd HH:mm:ss",dto.getStartTime());
            Date end = DateUtil.dateTime("yyyy-MM-dd HH:mm:ss",dto.getEndTime());
            query.addCriteria(Criteria.where("createTime").gte(start).lte(end));
            query.with(Sort.by(Sort.Direction.DESC,"createTime"));
            //query.skip(skip);
            /*if(dto.getPageUpDown()==0){
                //query.addCriteria(Criteria.where("createTime").lte(start));
                query.addCriteria(Criteria.where("createTime").gte(start).lte(end));
                query.with(Sort.by(Sort.Direction.DESC,"createTime"));
            }else{
                //query.addCriteria(Criteria.where("createTime").gte(start));
                query.addCriteria(Criteria.where("createTime").gte(start).lte(end));
                query.with(Sort.by(Sort.Direction.ASC,"createTime"));
            }*/
        }/*else if(StringUtils.isNotBlank(dto.getStartTime())&&StringUtils.isBlank(dto.getEndTime())){//非时间段查询
            Date start = DateUtil.dateTime("yyyy-MM-dd HH:mm:ss",dto.getStartTime());
            //Date end = DateUtil.dateTime("yyyy-MM-dd HH:mm:ss",dto.getEndTime());
            //query.addCriteria(Criteria.where("createTime").gte(start).lte(end));
            //query.with(Sort.by(Sort.Direction.ASC,"createTime"));
            if(dto.getPageUpDown()==0){
                query.addCriteria(Criteria.where("createTime").lt(start));
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
        //查询会话列表
        List<SessionFile> file = mongoTemplate.find(query, SessionFile.class);
        List<SessionFileVO> fileVO = mapperFacade.mapAsList(file, SessionFileVO.class);
        List<String> staffId = fileVO.stream().map(SessionFileVO::getFrom).collect(Collectors.toList());
        StaffQueryDTO staffDto = new StaffQueryDTO();
        staffDto.setQiWeiUserIds(staffId);
        ServerResponseEntity<List<StaffVO>> staffList = feignClient.findByStaffQueryDTO(staffDto);
        List<StaffVO> vo = staffList.getData();
        //Map<String,StaffVO> map = vo.stream().collect(Collectors.toMap(StaffVO::getQiWeiUserId, Function.identity()));
        UserStaffCpRelationSearchDTO userStaffCpRelationSearchDTO = new UserStaffCpRelationSearchDTO();
        userStaffCpRelationSearchDTO.setPageSize(10);
        userStaffCpRelationSearchDTO.setPageNum(1);
        userStaffCpRelationSearchDTO.setQiWeiUserIds(staffId);
        ServerResponseEntity<PageVO<UserStaffCpRelationListVO>> cpListVO = relationFeignClient.pageWithStaff(userStaffCpRelationSearchDTO);
        List<UserStaffCpRelationListVO> cpList = new ArrayList<>();
        if(cpListVO!=null && cpListVO.getData()!=null){
            cpList = cpListVO.getData().getList();
        }
        for (SessionFileVO s: fileVO) {
            if(StringUtils.isNotBlank(s.getTolist())){
                s.setTolist(s.getTolist().replace("[","").replace("]",""));
            }
            /*StaffVO fo = map.get(s.getFrom());
            if(fo!=null){
                s.setStaffAvatar(fo.getAvatar());
                s.setStaffName(fo.getStaffName());
            }*/
            if(vo!=null && vo.size()>0){
                for (StaffVO staffVO :vo) {
                    if(StringUtils.isNotBlank(staffVO.getQiWeiUserId()) && staffVO.getQiWeiUserId().equals(s.getFrom())){
                        s.setStaffAvatar(staffVO.getAvatar());
                        s.setStaffName(staffVO.getStaffName());
                        break;
                    }
                }
            }
            if(cpList!=null && cpList.size()>0){
                for (UserStaffCpRelationListVO listVO:cpList) {
                    if(StringUtils.isNotBlank(listVO.getQiWeiUserId()) && listVO.getQiWeiUserId().equals(s.getFrom())){
                        s.setUserAvatar(listVO.getAvatar());
                        s.setUserName(listVO.getQiWeiNickName());
                        break;
                    }
                }
            }
        }
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
        return PageUtil.mongodbPage(pageDTO, fileVO,total);
    }

    @Override
    public StaffSessionVO getSessionCount(SessionFileDTO dto) {
        StaffSessionVO sessionVO = new StaffSessionVO();
        Query query = new Query();
        Query u = new Query();
        Query r = new Query();
        Query room = new Query();
        //Pattern comp = Pattern.compile("^((?!"+dto.getFrom()+").)*$",Pattern.CASE_INSENSITIVE);
        Pattern comp = Pattern.compile("^"+"wo"+".*$",Pattern.CASE_INSENSITIVE);
        Pattern comp1 = Pattern.compile("^"+"wm"+".*$",Pattern.CASE_INSENSITIVE);//群聊wm或wo开头的为客户发送
        if(StringUtils.isNotBlank(dto.getStartTime()) && StringUtils.isNotBlank(dto.getEndTime())){
            Date start = DateUtil.dateTime("yyyy-MM-dd HH:mm:ss",dto.getStartTime());
            Date end = DateUtil.dateTime("yyyy-MM-dd HH:mm:ss",dto.getEndTime());
            query.addCriteria(Criteria.where("createTime").gte(start).lte(end));
            u.addCriteria(Criteria.where("createTime").gte(start).lte(end));
        }
        if(StringUtils.isNotBlank(dto.getFrom())){
            query.addCriteria(Criteria.where("from").is(dto.getFrom()));
            u.addCriteria(Criteria.where("tolist").regex(".*"+dto.getFrom()+".*"));
            //r.addCriteria(Criteria.where("from").is(dto.getFrom()));
            //room.addCriteria(Criteria.where("from").is(comp));
            //room.addCriteria(Criteria.where("from").is(dto.getFrom()));
        }
        if(StringUtils.isNotBlank(dto.getTolist())){
            query.addCriteria(Criteria.where("tolist").regex(".*"+dto.getTolist()+".*"));
            u.addCriteria(Criteria.where("from").is(dto.getTolist()));
        }
        if(StringUtils.isNotBlank(dto.getMsgtype())){
            query.addCriteria(Criteria.where("msgtype").is(dto.getMsgtype()));
            u.addCriteria(Criteria.where("msgtype").is(dto.getMsgtype()));
        }else{
            query.addCriteria(Criteria.where("msgtype").ne("revoke"));
            u.addCriteria(Criteria.where("msgtype").ne("revoke"));
        }
        if(StringUtils.isNotBlank(dto.getRoomid())){
            Criteria from = new Criteria().andOperator(Criteria.where("from").regex(comp));
            Criteria from1 = new Criteria().andOperator(Criteria.where("from").regex(comp1));
            room.addCriteria(new Criteria().orOperator(from,from1));
            r.addCriteria(Criteria.where("roomid").is(dto.getRoomid()));
            room.addCriteria(Criteria.where("roomid").is(dto.getRoomid()));
            //查询群聊总次数
            long staffRoom = mongoTemplate.count(r,SessionFile.class);
            //查询员工好友群聊发送次数
            long roomCount = mongoTemplate.count(room,SessionFile.class);
            sessionVO.setRoomSend((int)staffRoom-(int)roomCount);
            sessionVO.setUserRoomCount((int)roomCount);
            sessionVO.setReceiveSum((int)staffRoom);
            r.limit(1);
            r.with(Sort.by(Sort.Direction.DESC,"createTime"));
            //查询群聊列表
            List<SessionFile> user = mongoTemplate.find(r,SessionFile.class);
            List<SessionFileVO> userVO = mapperFacade.mapAsList(user, SessionFileVO.class);
            if(userVO!= null && userVO.size()>0){
                sessionVO.setRecentlyTime(userVO.get(0).getCreateTime());
            }
            //查询群添加时间
            List<String> ids = new ArrayList<>();
            ids.add(dto.getRoomid());
            List<CpCustGroup> groupList = custGroupService.list(new LambdaUpdateWrapper<CpCustGroup>().in(CpCustGroup::getId, ids));
            if(groupList!=null && groupList.size()>0){
                sessionVO.setRoomTime(groupList.get(0).getCreateTime());
            }
        }else{
            query.addCriteria(Criteria.where("roomid").exists(false));
            u.addCriteria(Criteria.where("roomid").exists(false));
        }
        //查询员工发送条数
        long staffCount = mongoTemplate.count(query, SessionFile.class);
        //query.limit(1);
        //query.with(Sort.by(Sort.Direction.DESC,"createTime"));
        //查询员工发送条数列表
        //List<SessionFile> fileVO = mongoTemplate.find(query,SessionFile.class);
        //查询客户发送条数
        long userCount = mongoTemplate.count(u, SessionFile.class);
        u.limit(1);
        u.with(Sort.by(Sort.Direction.DESC,"createTime"));
        //查询客户发送条数列表
        List<SessionFile> user = mongoTemplate.find(u, SessionFile.class);
        List<SessionFileVO> userVO = mapperFacade.mapAsList(user, SessionFileVO.class);
        if(userVO!=null && userVO.size()>0){
            if(StringUtils.isBlank(dto.getRoomid())){
                sessionVO.setRecentlyTime(userVO.get(0).getCreateTime());
            }
        }
        sessionVO.setSendCount((int)staffCount);
        sessionVO.setUserSendCount((int)userCount);
        sessionVO.setSendSum((int)staffCount+(int)userCount);
        //查询客户添加时间
        ServerResponseEntity<UserStaffCpRelationListVO> cpListVO = relationFeignClient.getUserInfoByQiWeiUserId(dto.getFrom(),dto.getTolist());
        UserStaffCpRelationListVO listVO = cpListVO.getData();
        if(listVO != null){
            sessionVO.setAddTime(listVO.getCpCreateTime());
        }
        return sessionVO;
    }

    @Override
    public PageVO<StaffSessionVO> getUserSession(PageDTO pageDTO, SessionFileDTO dto) {
        StaffQueryPageDTO queryPageDTO = new StaffQueryPageDTO();
        queryPageDTO.setPageSize(pageDTO.getPageSize());
        queryPageDTO.setPageNum(pageDTO.getPageNum());
        queryPageDTO.setIsDelete(0);
        queryPageDTO.setStaffIdList(dto.getStaffIdList());
        ServerResponseEntity<PageVO<StaffVO>> pageList = feignClient.getStaffPage(queryPageDTO);
        ServerResponseEntity.checkResponse(pageList);
        List<StaffVO> listVO = pageList.getData().getList();
        //查询员工群聊接收消息
        List<StaffSessionVO> staffSessionVOList = new ArrayList<>();
        //获取数据
        getData(dto,listVO,staffSessionVOList);
        return PageUtil.mongodbPage(pageDTO, staffSessionVOList,pageList.getData().getTotal());
    }

    @Override
    public List<StaffSessionVO> getUserSessionList(SessionFileDTO dto) {
        StaffQueryDTO staffDto = new StaffQueryDTO();
        staffDto.setStaffIdList(dto.getStaffIdList());
        staffDto.setIsDelete(0);
        ServerResponseEntity<List<StaffVO>> staffList = feignClient.findByStaffQueryDTO(staffDto);
        ServerResponseEntity.checkResponse(staffList);
        if(CollUtil.isEmpty(staffList.getData())){
            return ListUtil.empty();
        }
        List<StaffVO> listVO = staffList.getData();
        //查询员工群聊接收消息
        List<StaffSessionVO> staffSessionVOList = new ArrayList<>();
        //获取数据
        getData(dto,listVO,staffSessionVOList);

        if(CollUtil.isEmpty(staffSessionVOList)){
            return ListUtil.empty();
        }
        return staffSessionVOList;
    }

    private void getData(SessionFileDTO dto,List<StaffVO> listVO,List<StaffSessionVO> staffSessionVOList){
        List<String> staffQW = listVO.stream().map(StaffVO ::getQiWeiUserId).collect(Collectors.toList());
        //staffQW.add("wmmXETLgAAFCyGybRJrbIA4P_T7CTJjw");
        //staffQW.add("HaiXiang");
        //查询员工发送消息一对一
        GroupOperation groupOperation = Aggregation.group(Fields.fields("from")).count().as("sendCount");
        Criteria c = Criteria.where("from").in(staffQW);
        Criteria e = Criteria.where("action").is("send");
        Criteria a = Criteria.where("roomid").exists(false);
        Criteria date = new Criteria();
        if(StringUtils.isNotBlank(dto.getStartTime()) && StringUtils.isNotBlank(dto.getEndTime())){
            Date start = DateUtil.dateTime("yyyy-MM-dd HH:mm:ss",dto.getStartTime());
            Date end = DateUtil.dateTime("yyyy-MM-dd HH:mm:ss",dto.getEndTime());
            date = Criteria.where("createTime").gte(start).lte(end);
        }
        Criteria b = new Criteria().andOperator(c,e,a,date);
        MatchOperation math = Aggregation.match(b);
        ProjectionOperation p = Aggregation.project("from");
        ProjectionOperation pj = Aggregation.project("from","sendCount").and("staffName").previousOperation();
        Aggregation ag = Aggregation.newAggregation(math,p,groupOperation,pj);
        List<StaffSessionVO> t = mongoTemplate.aggregate(ag, SessionFile.class,StaffSessionVO.class).getMappedResults();
        Map<String, StaffSessionVO> sendMap = t.stream().collect(Collectors.toMap(StaffSessionVO::getStaffName, Function.identity()));
        //查询员工发送消息群聊
        Criteria r = Criteria.where("roomid").exists(true);
        Criteria room = new Criteria().andOperator(c,e,r,date);
        MatchOperation ma = Aggregation.match(room);
        Aggregation agRoom = Aggregation.newAggregation(ma,p,groupOperation,pj);
        List<StaffSessionVO> ts = mongoTemplate.aggregate(agRoom, SessionFile.class,StaffSessionVO.class).getMappedResults();
        Map<String, StaffSessionVO> roomMap = ts.stream().collect(Collectors.toMap(StaffSessionVO::getStaffName, Function.identity()));
        //查询员工单聊接收消息
        GroupOperation groupList = Aggregation.group(Fields.fields("tolist")).count().as("sendCount");//分组
        List<String> toListStaff = new ArrayList<>();
        for (String str:staffQW) {
            toListStaff.add("[\""+str+"\"]");
        }
        Criteria toL = Criteria.where("tolist").in(toListStaff);
        Criteria li = new Criteria().andOperator(toL,e,a,date);//查询条件
        MatchOperation mathL = Aggregation.match(li);
        ProjectionOperation pL = Aggregation.project("tolist");//显示字段
        ProjectionOperation pjL = Aggregation.project("tolist","sendCount").and("staffName").previousOperation();
        Aggregation agL = Aggregation.newAggregation(mathL,pL,groupList,pjL);
        List<StaffSessionVO> tL = mongoTemplate.aggregate(agL, SessionFile.class,StaffSessionVO.class).getMappedResults();
        for (StaffSessionVO sto :tL) {
            sto.setStaffName(sto.getStaffName().replace("[\"","").replace("\"]",""));
        }
        Map<String, StaffSessionVO> sessionTl = tL.stream().collect(Collectors.toMap(StaffSessionVO::getStaffName, Function.identity()));

        //互动个数查询
        GroupOperation groupInter = Aggregation.group(Fields.fields("tolist","from")).count().as("sendCount")
                .first("from").as("from").first("tolist").as("tolist");
        Pattern comp = Pattern.compile("^"+"wo"+".*$",Pattern.CASE_INSENSITIVE);
        Pattern comp1 = Pattern.compile("^"+"wm"+".*$",Pattern.CASE_INSENSITIVE);
        Criteria from = Criteria.where("from").regex(comp);
        Criteria from1 = Criteria.where("from").regex(comp1);
        Criteria from2 = new Criteria().orOperator(from,from1);
        Criteria inter = new Criteria().andOperator(from2,a,toL,date);
        MatchOperation mathInter = Aggregation.match(inter);
        ProjectionOperation pInter = Aggregation.project("tolist","from");
        ProjectionOperation interP = Aggregation.project("tolist","sendCount","from");
        Aggregation agInter = Aggregation.newAggregation(mathInter,pInter,groupInter,interP);
        List<StaffSessionVO> tInter = mongoTemplate.aggregate(agInter,SessionFile.class,StaffSessionVO.class).getMappedResults();
        tInter = new ArrayList<>(tInter);
        for (int i =0;i<tInter.size();i++){
            int sum = 1;
            for (int j =i+1;j<tInter.size();j++){//循环删除重复的数据
                if(tInter.get(i).getTolist().equals(tInter.get(j).getTolist())){
                    sum = sum+1;
                    tInter.remove(j);
                    j--;
                }
            }
            tInter.get(i).setSendCount(sum);
            tInter.get(i).setStaffName(tInter.get(i).getTolist().replace("[\"","").replace("\"]",""));
        }
        Map<String, StaffSessionVO> interMap = tInter.stream().collect(Collectors.toMap(StaffSessionVO::getStaffName, Function.identity()));

        GroupOperation roomAct = Aggregation.group(Fields.fields("roomid")).count().as("sendCount");
        for (StaffVO staffVO:listVO) {
            Query query = new Query();
            Criteria roomTo = Criteria.where("tolist").regex(".*"+staffVO.getQiWeiUserId()+".*");
            Criteria roomFlag = Criteria.where("roomid").exists(true);
            Criteria roomList = new Criteria().andOperator(roomTo,roomFlag);
            if(StringUtils.isNotBlank(dto.getStartTime()) && StringUtils.isNotBlank(dto.getEndTime())){
                Date start = DateUtil.dateTime("yyyy-MM-dd HH:mm:ss",dto.getStartTime());
                Date end = DateUtil.dateTime("yyyy-MM-dd HH:mm:ss",dto.getEndTime());
                query.addCriteria(Criteria.where("createTime").gte(start).lte(end));
            }
            query.addCriteria(roomList);
            long total = mongoTemplate.count(query,SessionFile.class);
            //活跃个数查询
            Criteria roomF = Criteria.where("from").is(staffVO.getQiWeiUserId());
            Criteria roomF1 = Criteria.where("tolist").regex(".*"+staffVO.getQiWeiUserId()+".*");
            Criteria roomF2 = new Criteria().orOperator(roomF,roomF1);
            Criteria interRoom = new Criteria().andOperator(roomTo,roomFlag,roomF2,date);
            MatchOperation mathAct = Aggregation.match(interRoom);
            ProjectionOperation pAct = Aggregation.project("roomid");
            ProjectionOperation actP = Aggregation.project("roomid","sendCount").and("staffName").previousOperation();
            Aggregation agAct = Aggregation.newAggregation(mathAct,pAct,roomAct,actP);
            int actCount = mongoTemplate.aggregate(agAct,SessionFile.class,StaffSessionVO.class).getMappedResults().size();

            StaffSessionVO  sessionVO = new StaffSessionVO();
            sessionVO.setStaffName(staffVO.getStaffName());
            //sessionVO.setEmp(staffVO.getOrgName());
            sessionVO.setActiveCont(actCount);//活跃个数
            int receiveCount = 0;
            if(sessionTl.get(staffVO.getQiWeiUserId()) != null){
                receiveCount = sessionTl.get(staffVO.getQiWeiUserId()).getSendCount();
                sessionVO.setReceiveCount(receiveCount);//接收消息
            }else{
                sessionVO.setReceiveCount(0);//接收消息
            }
            int interCount = 0;
            if(interMap.get(staffVO.getQiWeiUserId()) != null){
                interCount = interMap.get(staffVO.getQiWeiUserId()).getSendCount();
                sessionVO.setInteractionCount(interCount);
            }else{
                sessionVO.setInteractionCount(0);//互动个数
            }
            sessionVO.setReceiveSum(receiveCount+(int)total);//接收消息总数
            sessionVO.setRoomReceive((int)total);//群接收消息
            //sessionVO.setSendSum(sendMap.get(staffVO.getQiWeiUserId()).getSendCount()+roomMap.get(staffVO.getQiWeiUserId()).getSendCount());//发送消息总数
            int sendCount = 0;
            if(sendMap.get(staffVO.getQiWeiUserId()) != null){
                sendCount = sendMap.get(staffVO.getQiWeiUserId()).getSendCount();
                sessionVO.setSendCount(sendCount);//发送消息
            }else{
                sessionVO.setSendCount(0);//发送消息
            }
            int roomSend = 0;
            if(roomMap.get(staffVO.getQiWeiUserId()) != null){
                roomSend = roomMap.get(staffVO.getQiWeiUserId()).getSendCount();
                sessionVO.setRoomSend(roomSend);//群发送消息
            }else{
                sessionVO.setRoomSend(0);
            }
            sessionVO.setSendSum(sendCount+roomSend);

            if(CollUtil.isNotEmpty(staffVO.getOrgs())){
                // 提取所有人员的名称，并用逗号分隔起来
                String orgNames = staffVO.getOrgs().stream()
                        .map(item -> item.getOrgName())   // 获取每个对象的名称字段
                        .collect(Collectors.joining(","));  // 使用逗号进行连接
                sessionVO.setEmp(orgNames);
            }

            staffSessionVOList.add(sessionVO);
        }
    }



    @Override
    public StaffSessionVO getStaffCount(SessionFileDTO dto) {
        Query query = new Query();
        if(StringUtils.isNotBlank(dto.getFrom()) && StringUtils.isNotBlank(dto.getTolist())){
            /*Criteria from = Criteria.where("from").is(dto.getFrom());
            Criteria toList = Criteria.where("tolist").is(dto.getTolist());
            query.addCriteria(new Criteria().orOperator(from,toList));*/
            Criteria from = new Criteria().andOperator(Criteria.where("from").is(dto.getFrom()),Criteria.where("tolist").regex(".*"+dto.getTolist()+".*"));
            Criteria toList = new Criteria().andOperator(Criteria.where("tolist").regex(".*"+dto.getFrom()+".*"),Criteria.where("from").regex(".*"+dto.getTolist()+".*"));
            query.addCriteria(new Criteria().orOperator(from,toList));
        }
        if(StringUtils.isNotBlank(dto.getStartTime()) && StringUtils.isNotBlank(dto.getEndTime())){
            Date start = DateUtil.dateTime("yyyy-MM-dd HH:mm:ss",dto.getStartTime());
            Date end = DateUtil.dateTime("yyyy-MM-dd HH:mm:ss",dto.getEndTime());
            query.addCriteria(Criteria.where("createTime").gte(start).lte(end));
        }
        if(StringUtils.isNotBlank(dto.getContent())){
            query.addCriteria(Criteria.where("content").regex(".*"+dto.getContent()+".*"));
        }
        if(StringUtils.isNotBlank(dto.getMsgtype())){
            query.addCriteria(Criteria.where("msgtype").is(dto.getMsgtype()));
        }
        query.addCriteria(Criteria.where("roomid").exists(false));
        long total = mongoTemplate.count(query, SessionFile.class);
        StaffSessionVO sessionVO = new StaffSessionVO();
        sessionVO.setSendSum((int)total);
        return sessionVO;
    }

    @Override
    public List<StaffSessionVO> getUserAndStaffCount(SessionFileDTO dto) {
        //分组参数
        GroupOperation groupOperation = Aggregation.group(Fields.fields("tolist","createTime","from")).count().as("sendCount")
                .first("from").as("from").first("tolist").as("tolist");
        Criteria c = Criteria.where("from").is(dto.getFrom());
        Criteria a = Criteria.where("roomid").exists(false);
        Criteria e = Criteria.where("tolist").regex(".*"+dto.getFrom()+".*");
        Criteria f = new Criteria().orOperator(c,e);
        Criteria b = new Criteria().andOperator(a,f);
        Date start = DateUtil.dateTime("yyyy-MM-dd HH:mm:ss",dto.getStartTime());
        Date end = DateUtil.dateTime("yyyy-MM-dd HH:mm:ss",dto.getEndTime());
        Criteria da = Criteria.where("createTime").gte(start).lte(end);
        Criteria match = new Criteria().andOperator(b,da);
        MatchOperation math = Aggregation.match(match);
        ProjectionOperation p = Aggregation.project("tolist","createTime","from").and(DateOperators.DateToString.dateOf("createTime").toString(("%Y-%m-%d"))).as("createTime");
        ProjectionOperation pj = Aggregation.project("tolist","sendCount","from","createTime");
        Aggregation ag = Aggregation.newAggregation(math,p,groupOperation,pj);
        //查询数据
        List<StaffSessionVO> list = mongoTemplate.aggregate(ag, SessionFile.class,StaffSessionVO.class).getMappedResults();
        list = new ArrayList<>(list);
        for (int i =0;i<list.size();i++){
            for (int j =i+1;j<list.size();j++){//循环删除重复的数据
                if(list.get(j).getTolist().contains(list.get(i).getFrom()) && list.get(i).getTolist().contains(list.get(j).getFrom()) & list.get(i).getCreateTime().equals(list.get(j).getCreateTime())){
                    int count = list.get(i).getSendCount()+list.get(j).getSendCount();
                    list.get(i).setSendCount(count);
                    list.remove(j);
                    j--;
                }
            }
        }
        log.info("企微会话移动端数据统计：{}", JSON.toJSONString(list));
        return list;
    }

    @Override
    public List<SessionFileVO> getSessionFileByMsgId(List<String> msgId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("msgid").in(msgId));
        List<SessionFile> file = mongoTemplate.find(query, SessionFile.class);
        List<SessionFileVO> fileVO = mapperFacade.mapAsList(file, SessionFileVO.class);
        List<String> staffId = fileVO.stream().map(SessionFileVO::getFrom).collect(Collectors.toList());
        StaffQueryDTO staffDto = new StaffQueryDTO();
        staffDto.setQiWeiUserIds(staffId);
        ServerResponseEntity<List<StaffVO>> staffList = feignClient.findByStaffQueryDTO(staffDto);
        List<StaffVO> vo = staffList.getData();
        //Map<String,StaffVO> map = vo.stream().collect(Collectors.toMap(StaffVO::getQiWeiUserId, Function.identity()));
        UserStaffCpRelationSearchDTO userStaffCpRelationSearchDTO = new UserStaffCpRelationSearchDTO();
        userStaffCpRelationSearchDTO.setPageSize(10);
        userStaffCpRelationSearchDTO.setPageNum(1);
        userStaffCpRelationSearchDTO.setQiWeiUserIds(staffId);
        ServerResponseEntity<PageVO<UserStaffCpRelationListVO>> cpListVO = relationFeignClient.pageWithStaff(userStaffCpRelationSearchDTO);
        List<UserStaffCpRelationListVO> cpList = new ArrayList<>();
        if(cpListVO!=null && cpListVO.getData()!=null){
            cpList = cpListVO.getData().getList();
        }
        for (SessionFileVO s: fileVO) {
            if(StringUtils.isNotBlank(s.getTolist())){
                s.setTolist(s.getTolist().replace("[","").replace("]",""));
            }
            if(vo!=null && vo.size()>0){
                for (StaffVO staffVO :vo) {
                    if(StringUtils.isNotBlank(staffVO.getQiWeiUserId()) && staffVO.getQiWeiUserId().equals(s.getFrom())){
                        s.setStaffAvatar(staffVO.getAvatar());
                        s.setStaffName(staffVO.getStaffName());
                        break;
                    }
                }
            }
            if(cpList!=null && cpList.size()>0){
                for (UserStaffCpRelationListVO listVO:cpList) {
                    if(StringUtils.isNotBlank(listVO.getQiWeiUserId()) && listVO.getQiWeiUserId().equals(s.getFrom())){
                        s.setUserAvatar(listVO.getAvatar());
                        s.setUserName(listVO.getQiWeiNickName());
                        break;
                    }
                }
            }
        }
        return fileVO;
    }

    @Override
    public SessionFile getSessionFile(String msgId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("msgid").is(msgId));
        return mongoTemplate.findOne(query,SessionFile.class);
    }

    @Override
    public Date getLastTime(SessionFileDTO dto) {
        Query query = new Query();
        if(StringUtils.isNotBlank(dto.getFrom()) && StringUtils.isNotBlank(dto.getTolist())){
            Criteria from = new Criteria().andOperator(Criteria.where("from").is(dto.getFrom()),Criteria.where("tolist").regex(".*"+dto.getTolist()+".*"));
            Criteria toList = new Criteria().andOperator(Criteria.where("tolist").regex(".*"+dto.getFrom()+".*"),Criteria.where("from").regex(".*"+dto.getTolist()+".*"));
            query.addCriteria(new Criteria().orOperator(from,toList));
        }
        query.addCriteria(Criteria.where("roomid").exists(false));
        query.limit(1);
        query.with(Sort.by(Sort.Direction.DESC,"createTime"));
        //查询最新的消息
        SessionFile user = mongoTemplate.findOne(query, SessionFile.class);
        if(user != null){
            return user.getCreateTime();
        }
        return null;
    }

    @Override
    public Date getRoomLastTime(SessionFileDTO dto) {
        Query query = new Query();
        query.addCriteria(Criteria.where("roomid").is(dto.getRoomid()));
        query.limit(1);
        query.with(Sort.by(Sort.Direction.DESC,"createTime"));
        //查询群聊最新的消息
        SessionFile user = mongoTemplate.findOne(query, SessionFile.class);
        if(user != null){
            return user.getCreateTime();
        }
        return null;
    }

}
