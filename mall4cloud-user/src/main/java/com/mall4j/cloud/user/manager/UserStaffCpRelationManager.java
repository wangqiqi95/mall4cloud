package com.mall4j.cloud.user.manager;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.stream.CollectorUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.mall4j.cloud.api.biz.dto.cp.NotifyMsgTemplateDTO;
import com.mall4j.cloud.api.biz.feign.WxCpCustNotifyFeignClient;
import com.mall4j.cloud.api.platform.dto.StaffQueryDTO;
import com.mall4j.cloud.api.platform.feign.StaffFeignClient;
import com.mall4j.cloud.api.platform.vo.StaffVO;
import com.mall4j.cloud.api.user.crm.request.QueryPointRequest;
import com.mall4j.cloud.api.user.crm.request.QueryTagCategoryPageRequest;
import com.mall4j.cloud.api.user.crm.request.QueryUserTagRequest;
import com.mall4j.cloud.api.user.crm.response.*;
import com.mall4j.cloud.api.user.dto.CRMUserTagDto;
import com.mall4j.cloud.api.user.dto.UserStaffCpRelationSearchDTO;
import com.mall4j.cloud.api.user.vo.UserStaffCpRelationListVO;
import com.mall4j.cloud.common.exception.Assert;
import com.mall4j.cloud.common.exception.LuckException;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.common.security.AuthUserContext;
import com.mall4j.cloud.common.util.LambdaUtils;
import com.mall4j.cloud.common.util.RandomUtil;
import com.mall4j.cloud.user.dto.UserFollowUpDTO;
import com.mall4j.cloud.user.mapper.CrmUserTagRelationMapper;
import com.mall4j.cloud.user.mapper.UserStaffCpRelationMapper;
import com.mall4j.cloud.user.model.CrmUserTagRelation;
import com.mall4j.cloud.user.model.UserStaffCpRelation;
import com.mall4j.cloud.user.model.UserStaffRelationFollowUp;
import com.mall4j.cloud.user.service.UserStaffCpRelationService;
import com.mall4j.cloud.user.service.UserStaffRelationFollowUpService;
import com.mall4j.cloud.user.service.crm.CrmService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Component
public class UserStaffCpRelationManager {


    @Autowired
    UserStaffCpRelationMapper userStaffCpRelationMapper;
    @Autowired
    UserStaffCpRelationService userStaffCpRelationService;
    @Autowired
    CrmService crmService;
    @Autowired
    UserStaffRelationFollowUpService userStaffRelationFollowUpService;
    @Autowired
    WxCpCustNotifyFeignClient notifyFeignClient;
    @Autowired
    StaffFeignClient staffFeignClient;
    @Autowired
    private CrmUserTagRelationMapper crmUserTagRelationMapper;

    public List<UserStaffCpRelationListVO> lastOneListByUserIdList(List<Long> userIdList){
        return userStaffCpRelationMapper.lastOneListByUserIdList(userIdList);
    }

    public List<UserStaffCpRelation> getByStaffIds(List<Long> staffIdList,List<String> selectTagUnionIdList,List<String> stgIdList){
        LambdaUpdateWrapper<UserStaffCpRelation> lambdaUpdateWrapper= new LambdaUpdateWrapper<UserStaffCpRelation>();
        lambdaUpdateWrapper.eq(UserStaffCpRelation::getStatus,1);
        lambdaUpdateWrapper.in(UserStaffCpRelation::getStaffId, staffIdList);
        if(CollUtil.isNotEmpty(stgIdList)){
            lambdaUpdateWrapper.in(UserStaffCpRelation::getStageId, stgIdList);
        }
        //查询标签下的会员列表的unionid
        if(CollUtil.isNotEmpty(selectTagUnionIdList)){
            if(CollUtil.isNotEmpty(selectTagUnionIdList)){
                lambdaUpdateWrapper.in(UserStaffCpRelation::getUserUnionId, selectTagUnionIdList);
            }
        }
        List<UserStaffCpRelation> relations=userStaffCpRelationMapper.selectList(lambdaUpdateWrapper);

        //筛选对应员工状态正常的客户
        List<Long> staffIds=relations.stream().map(item->item.getStaffId()).collect(Collectors.toList());
        StaffQueryDTO staffQueryDTO=new StaffQueryDTO();
        staffQueryDTO.setStatus(0);
        staffQueryDTO.setIsDelete(0);
        staffQueryDTO.setStaffIdList(staffIds);
        ServerResponseEntity<List<StaffVO>> staffResponseEntity=staffFeignClient.findByStaffQueryDTO(staffQueryDTO);
        List<UserStaffCpRelation> relationListVOS=new ArrayList<>();
        Map<Long,StaffVO> staffVOMap= LambdaUtils.toMap(staffResponseEntity.getData(),StaffVO::getId);
        for (UserStaffCpRelation datum : relations) {
            if(staffVOMap.containsKey(datum.getStaffId())){
                relationListVOS.add(datum);
            }
        }

        return relationListVOS;
    }

    public List<UserStaffCpRelation> getByQiWeiUserIds(List<String> userIds){
        LambdaUpdateWrapper<UserStaffCpRelation> lambdaUpdateWrapper= new LambdaUpdateWrapper<UserStaffCpRelation>();
        lambdaUpdateWrapper.eq(UserStaffCpRelation::getStatus,1);
        lambdaUpdateWrapper.in(UserStaffCpRelation::getQiWeiUserId, userIds);
        List<UserStaffCpRelation> list=userStaffCpRelationMapper.selectList(lambdaUpdateWrapper);
        return list;
//        if(CollUtil.isEmpty(list)){
//            return ListUtil.empty();
//        }
//        //qiWeiUserId 去重复【一个客户会对应多个员工】
//        return new ArrayList<>(list.stream().collect(Collectors.toMap(UserStaffCpRelation::getQiWeiUserId, s->s, (v1,v2)->v2)).values());
    }

    public List<UserStaffCpRelation> getByQiWeiUserUnionIds(List<String> userUnionIds){
        LambdaUpdateWrapper<UserStaffCpRelation> lambdaUpdateWrapper= new LambdaUpdateWrapper<UserStaffCpRelation>();
        lambdaUpdateWrapper.eq(UserStaffCpRelation::getStatus,1);
        lambdaUpdateWrapper.in(UserStaffCpRelation::getUserUnionId, userUnionIds);
        List<UserStaffCpRelation> list=userStaffCpRelationMapper.selectList(lambdaUpdateWrapper);
        return list;
//        if(CollUtil.isEmpty(list)){
//            return ListUtil.empty();
//        }
//        //qiWeiUserId 去重复【一个客户会对应多个员工】
//        return new ArrayList<>(list.stream().collect(Collectors.toMap(UserStaffCpRelation::getQiWeiUserId, s->s, (v1,v2)->v2)).values());
    }


    /**
     * 查询会员标签集合
     */
    public Map<String, List<String>> mergeGetUserTagNams(CRMUserTagDto crmUserTagDto) {
        if(CollUtil.isEmpty(crmUserTagDto.getUnionIds())){
            return MapUtil.empty();
        }
        List<CrmUserTagRelation> relations=crmUserTagRelationMapper.userTagListByDTO(crmUserTagDto);
        Map<String, List<String>> tagRelationValueMap = new HashMap<>();
        relations.forEach(tagRelation -> {
            if (tagRelationValueMap.containsKey(tagRelation.getUnionid())) {
                tagRelationValueMap.get(tagRelation.getUnionid()).add(tagRelation.getTagname());
            } else {
                List<String> tagNames = new ArrayList<>();
                tagNames.add(tagRelation.getTagname());
                tagRelationValueMap.put(tagRelation.getUnionid(), tagNames);
            }

            ;
        });
        return tagRelationValueMap;
    }

    public Map<String, Map<String, StringBuilder>> mergeGetUserTagNams1(CRMUserTagDto crmUserTagDto) {
        if(CollUtil.isEmpty(crmUserTagDto.getUnionIds())){
            return MapUtil.empty();
        }
        List<CrmUserTagRelation> relations=crmUserTagRelationMapper.userTagListByDTO(crmUserTagDto);
        Map<String, List<CrmUserTagRelation>> tagMaps =LambdaUtils.groupList(relations,CrmUserTagRelation::getUnionid);

        Map<String, Map<String, StringBuilder>> tagRelationMap = new HashMap<>();
        for (Map.Entry<String,List<CrmUserTagRelation>> entry: tagMaps.entrySet()){
            Map<String, StringBuilder> tagRelationValueMap = new HashMap<>();
            entry.getValue().forEach(tagRelation -> {
                if (tagRelationValueMap.containsKey(tagRelation.getTagname())) {
                    if(!tagRelationValueMap.get(tagRelation.getTagname()).toString().contains(":")){
                        tagRelationValueMap.get(tagRelation.getTagname()).append(":");
                    }
                    tagRelationValueMap.get(tagRelation.getTagname()).append(",").append(tagRelation.getTagvalue());
                } else {
                    StringBuilder stringBuilder=new StringBuilder();
                    stringBuilder.append(tagRelation.getTagname());
                    if(StrUtil.isNotEmpty(tagRelation.getTagvalue())){
                        stringBuilder.append(":").append(tagRelation.getTagvalue());
                    }
                    tagRelationValueMap.put(tagRelation.getTagname(), stringBuilder);
                }
            });
            if (!tagRelationMap.containsKey(entry.getKey())) {
                tagRelationMap.put(entry.getKey(), tagRelationValueMap);
            }
        }
        return tagRelationMap;
    }

    public static void main(String[] strings){
        String json="[{\"enable\":\"Y\",\"id\":\"3\",\"originid\":\"wmRqXCCgAAEf1dx-UDUwoyQoPRThHGkg\",\"tagid\":\"1\",\"tagname\":\"test1\",\"tagvalue\":\"value1-2\",\"unionid\":\"oET9gxEY_wM8gAMIQgjNL9II2bkw\"},{\"enable\":\"Y\",\"id\":\"2\",\"originid\":\"wmRqXCCgAAEf1dx-UDUwoyQoPRThHGkg\",\"tagid\":\"2\",\"tagname\":\"test2\",\"tagvalue\":\"value2-1\",\"unionid\":\"oET9gxEY_wM8gAMIQgjNL9II2bkw\"},{\"enable\":\"Y\",\"id\":\"1\",\"originid\":\"wmRqXCCgAAEf1dx-UDUwoyQoPRThHGkg\",\"tagid\":\"1\",\"tagname\":\"test1\",\"tagvalue\":\"value1-1\",\"unionid\":\"oET9gxEY_wM8gAMIQgjNL9II2bkw\"}]";
        List<CrmUserTagRelation> relations= JSONArray.parseArray(json,CrmUserTagRelation.class);
        log.info("mergeGetUserTag->{}", JSON.toJSONString(relations));
        Map<String, StringBuilder> tagRelationValueMap = new HashMap<>();
        relations.forEach(tagRelation -> {
            if (tagRelationValueMap.containsKey(tagRelation.getTagname())) {
                if(!tagRelationValueMap.get(tagRelation.getTagname()).toString().contains(":")){
                    tagRelationValueMap.get(tagRelation.getTagname()).append(":");
                }
                tagRelationValueMap.get(tagRelation.getTagname()).append(",").append(tagRelation.getTagvalue());
            } else {
                StringBuilder stringBuilder=new StringBuilder();
                stringBuilder.append(tagRelation.getTagname());
                if(StrUtil.isNotEmpty(tagRelation.getTagvalue())){
                    stringBuilder.append(":").append(tagRelation.getTagvalue());
                }
                tagRelationValueMap.put(tagRelation.getTagname(), stringBuilder);
            }
        });
        System.out.println("map->"+JSON.toJSONString(tagRelationValueMap.values()));
    }

    /**
     * 查询会员标签集合
     * @param id
     */
    public List<UserTag> mergeGetUserTag(Long id,String userId,String unionId){
        //获取用户标签
        List<CrmUserTagRelation>  tagRelations=crmUserTagRelationMapper.userTagList(null,unionId);
        if(CollUtil.isEmpty(tagRelations)){
            return ListUtil.empty();
        }
        Map<String,UserTag> tagRelationValueMap= new HashMap<>();
        tagRelations.forEach(tagRelation->{
                if(tagRelationValueMap.containsKey(tagRelation.getTagid())){
                    tagRelationValueMap.get(tagRelation.getTagid()).getTagValues().add(tagRelation.getTagvalue());
                }else{
                    UserTag userTag=new UserTag();
                    userTag.setTagId(tagRelation.getTagid());
                    userTag.setName(tagRelation.getTagname());
                    List<String> tagValue=new ArrayList<>();
                    tagValue.add(tagRelation.getTagvalue());
                    userTag.setTagValues(tagValue);
                    tagRelationValueMap.put(tagRelation.getTagid(),userTag);
                }

            ;});
        List<UserTag> fullUserTags = new ArrayList<>(tagRelationValueMap.values());
        return fullUserTags;

        /**
         * 查询全量会员标签数据。
         */
//        boolean fullTagFlag = true;
//        int fullTagPageNo = 0;
//        int fullTagPageSize = 1000;
//        List<QueryTagCategoryPage> fullTagList = new ArrayList<>();
//
//        while(fullTagFlag){
//            QueryTagCategoryPageRequest queryTagCategoryPageRequest = new QueryTagCategoryPageRequest();
//            queryTagCategoryPageRequest.setPageNo(fullTagPageNo);
//            queryTagCategoryPageRequest.setPageSize(fullTagPageSize);
//            CrmResult<QueryTagCategoryPageResponse> tagResponse = crmService.queryTagCategory(queryTagCategoryPageRequest);
//            QueryTagCategoryPageResponse pageResponse = tagResponse.getResult();
//            //查询结果放到总标签集合中
//            fullTagList.addAll(pageResponse.getContent());
//            //判断是否还有下一页数据
//            if(pageResponse.getTotalElement() > (fullTagPageNo+1)*fullTagPageSize ){
//                fullTagPageNo++;
//            }else{
//                fullTagFlag=false;
//            }
//        }
//
//        List<String> fullTags = fullTagList.stream().map(QueryTagCategoryPage::getId).collect(Collectors.toList());
//
//        List<UserTag> fullUserTags = new ArrayList<>();
//        /**
//         * 分页查询当前会员是否有对应标签数据
//         */
//        List<List<String>> splitFullTags =  ListUtil.split(fullTags,200);
//        for (List<String> tag : splitFullTags) {
//            QueryUserTagRequest request = new QueryUserTagRequest();
//            request.setUnionid(staffCpRelation.getUserUnionId());
//            request.setTagIds(tag);
//            CrmResult<List<UserTag>> userTagResponse = crmService.queryMemberTag(request);
//            if(userTagResponse.isSuccess() && CollUtil.isNotEmpty(userTagResponse.getResult())){
//                fullUserTags.addAll(userTagResponse.getResult());
//            }
//        }
//        return fullUserTags;
    }

    /**
     * 调用接口获取用户积分数据
     * @param id
     */
    public String getUserPoint(Long id){
        UserStaffCpRelation staffCpRelation = userStaffCpRelationService.getById(id);
        QueryPointRequest request = new QueryPointRequest();
        request.setUnionId(staffCpRelation.getUserUnionId());
        ServerResponseEntity<CrmResult<QueryPointResponse>> crmResult = crmService.queryPonint(request);
        //
        if(!crmResult.isSuccess() || crmResult.getData()==null || crmResult.getData().getResult()==null){
            return "0";
        }
        return crmResult.getData().getResult().getPoint();
    }

    /**
     * 添加跟进记录
     * @param request
     */
    @Transactional(rollbackFor = Exception.class)
    public void followUp(UserFollowUpDTO request,Long adminStaffId){
        UserStaffCpRelation staffCpRelation = userStaffCpRelationService.getById(request.getRelationId());

        UserStaffRelationFollowUp userStaffRelationFollowUp = new UserStaffRelationFollowUp();
        userStaffRelationFollowUp.setRelationId(request.getRelationId());
        userStaffRelationFollowUp.setUnionId(staffCpRelation.getUserUnionId());
        userStaffRelationFollowUp.setUserId(staffCpRelation.getQiWeiUserId());
        userStaffRelationFollowUp.setContent(request.getContent());
        userStaffRelationFollowUp.setChatIds(StrUtil.join(",",request.getChatIds()));
        userStaffRelationFollowUp.setImgsUrl(StrUtil.join(",",request.getImgUrl()));
        if(CollUtil.isNotEmpty(request.getStaffIds())){
            List<String> staffIds=request.getStaffIds().stream().distinct().collect(Collectors.toList());
            userStaffRelationFollowUp.setStaffIds(StrUtil.join(",",staffIds));
        }
        userStaffRelationFollowUp.setOrderId(request.getOrderId());
        userStaffRelationFollowUp.setFollowUpId(request.getFollowUpId());
        userStaffRelationFollowUp.setCreateName(AuthUserContext.get().getUsername());
        userStaffRelationFollowUp.setOrigin(1);
        userStaffRelationFollowUp.setCreateById(adminStaffId);
        userStaffRelationFollowUpService.save(userStaffRelationFollowUp);

        if(CollUtil.isNotEmpty(request.getStaffIds())){
            List<String> staffIds=request.getStaffIds().stream().distinct().collect(Collectors.toList());
            for (String staffId : staffIds) {

                ServerResponseEntity<StaffVO> staffResponse = staffFeignClient.getStaffById(Long.parseLong(staffId));
                StaffVO staffVO = staffResponse.getData();
                if( staffResponse==null || staffResponse.isFail()){
                    log.error(StrUtil.format("查询导购失败，跳过给员工发信息。查询参数:{},查询结果:{}",staffId, JSONObject.toJSONString(staffResponse)));
                    Assert.faild("查询导购失败");
                }
                //要给员工发信息
                String nickName=staffCpRelation.getQiWeiNickName();
                String userName=StrUtil.isNotEmpty(staffCpRelation.getCpRemark())?staffCpRelation.getCpRemark():staffCpRelation.getQiWeiNickName();
                String relationId=""+staffCpRelation.getId();
                String staffName=staffVO.getStaffName();
                String staffUserId=staffVO.getQiWeiUserId();
                String createDate=DateUtil.format(new Date(),"yyyy-MM-dd HH:mm:ss");
                ServerResponseEntity<String> notifyResponse=notifyFeignClient.followNotify(nickName,userName,staffName,createDate,relationId,staffUserId);
//                ServerResponseEntity<String> notifyResponse = notifyFeignClient.followNotify(staffVO.getQiWeiUserId(),staffVO.getStaffName(),staffCpRelation.getQiWeiNickName());
                log.info("给导购发送跟进记录结束，执行结果:{}",JSONObject.toJSONString(notifyResponse));
            }
        }
    }


}
