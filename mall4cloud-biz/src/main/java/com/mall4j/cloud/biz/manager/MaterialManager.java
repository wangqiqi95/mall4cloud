package com.mall4j.cloud.biz.manager;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.mall4j.cloud.api.biz.dto.cp.NotifyMsgTemplateDTO;
import com.mall4j.cloud.api.platform.feign.StaffFeignClient;
import com.mall4j.cloud.api.platform.vo.StaffVO;
import com.mall4j.cloud.api.user.constant.BuildTagFromEnum;
import com.mall4j.cloud.api.user.crm.model.UpdateTag;
import com.mall4j.cloud.api.user.crm.model.UpdateTagModel;
import com.mall4j.cloud.api.user.crm.request.UpdateUserTagRequest;
import com.mall4j.cloud.api.user.crm.response.UserTag;
import com.mall4j.cloud.api.user.dto.UserStaffCpRelationSearchDTO;
import com.mall4j.cloud.api.user.feign.UserStaffCpRelationFeignClient;
import com.mall4j.cloud.api.user.feign.crm.CrmUserFeignClient;
import com.mall4j.cloud.api.user.vo.UserStaffCpRelationListVO;
import com.mall4j.cloud.biz.dto.cp.wx.MeterialBrowseDTO;
import com.mall4j.cloud.biz.mapper.cp.CpMaterialBrowseRecordMapper;
import com.mall4j.cloud.biz.mapper.cp.CpMaterialUseRecordMapper;
import com.mall4j.cloud.biz.model.cp.CpMaterialBrowseRecord;
import com.mall4j.cloud.biz.model.cp.CpMaterialLable;
import com.mall4j.cloud.biz.model.cp.Material;
import com.mall4j.cloud.biz.service.cp.CpMaterialLableService;
import com.mall4j.cloud.biz.service.cp.MaterialService;
import com.mall4j.cloud.biz.service.cp.WxCpPushService;
import com.mall4j.cloud.common.exception.Assert;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.common.util.DateUtil;
import com.mall4j.cloud.common.util.LambdaUtils;
import com.mall4j.cloud.common.util.RandomUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class MaterialManager {

    @Autowired
    CpMaterialUseRecordMapper materialUseRecordMapper;
    @Autowired
    CpMaterialBrowseRecordMapper materialBrowseRecordMapper;
    @Autowired
    CpMaterialLableService materialLableService;
    @Autowired
    MaterialService materialService;
    @Autowired
    CrmUserFeignClient crmUserFeignClient;
    @Autowired
    WxCpPushService wxCpPushService;
    @Autowired
    StaffFeignClient staffFeignClient;
    @Autowired
    UserStaffCpRelationFeignClient userStaffCpRelationFeignClient;
//    @Autowired
//    private CrmManager crmManager;

    /**
     * 素材浏览
     * @param request
     */
    @Transactional(rollbackFor = Exception.class)
    public void browse(MeterialBrowseDTO request) {
        log.info("浏览素材记录:{}", JSON.toJSONString(request));
        if(request.getUnionid()==null){
            return;
        }
        if(StrUtil.isEmpty(request.getBrowseId())){
            Assert.faild("browseId不允许为空。");
        }
        Material material = materialService.getById(request.getMeterialId());
        if(material==null){
            Assert.faild("素材不存在。");
        }
        CpMaterialBrowseRecord dbMaterialBrowseRecord = materialBrowseRecordMapper.getByBrowseId(request.getBrowseId());
        CpMaterialBrowseRecord recordUnionId = materialBrowseRecordMapper.getByUnionIdAndMatId(request.getMeterialId(),request.getUnionid());
        if(dbMaterialBrowseRecord==null){
            CpMaterialBrowseRecord materialBrowseRecord = new CpMaterialBrowseRecord();
            materialBrowseRecord.setUnionId(request.getUnionid());
            materialBrowseRecord.setBrowseDuration(0);
            materialBrowseRecord.setMatId(request.getMeterialId());
            materialBrowseRecord.setStatus(0);
            materialBrowseRecord.setStaffId(request.getStaffId());
            materialBrowseRecord.setBrowseId(request.getBrowseId());
            materialBrowseRecord.setCreateTime(new Date());
            materialBrowseRecordMapper.save(materialBrowseRecord);

            //更新浏览与访客数
            int browseCount= Objects.nonNull(material.getBrowseCount())?material.getBrowseCount()+1:1;
            material.setBrowseCount(browseCount);
            if(Objects.isNull(recordUnionId)){
                int visitorCount= Objects.nonNull(material.getVisitorCount())?material.getVisitorCount()+1:1;
                material.setVisitorCount(visitorCount);
            }
            materialService.update(material);

            return;
        }

        Long dataDiff = DateUtil.getNowDate().getTime() - dbMaterialBrowseRecord.getCreateTime().getTime();
        //获取浏览时长
        Integer browseDuration = Math.toIntExact(dataDiff / 1000);
        materialBrowseRecordMapper.updateBrowseDuration(dbMaterialBrowseRecord.getId(),browseDuration);
    }

    /**
     * 根据浏览时长，给满足条件浏览记录的会员打上对应的标签
     */
    @Transactional(rollbackFor = Exception.class)
    public void setUserTagByMaterialBrowse(){
        //查询半小时未更新并且未设置标签的浏览记录数据
        List<CpMaterialBrowseRecord> records = materialBrowseRecordMapper.getUnSetTagRecord();

        List<String> unionIds=records.stream().map(item->item.getUnionId()).collect(Collectors.toList());
        UserStaffCpRelationSearchDTO userStaffCpRelationSearchDTO=new UserStaffCpRelationSearchDTO();
        userStaffCpRelationSearchDTO.initPage();
        userStaffCpRelationSearchDTO.setUnionIds(unionIds);
        ServerResponseEntity<List<UserStaffCpRelationListVO>> responseEntity=userStaffCpRelationFeignClient.getUserStaffRelAll(userStaffCpRelationSearchDTO);
//        Map<String,UserStaffCpRelationListVO> relationListVOMap= LambdaUtils.toMap(responseEntity.getData(),UserStaffCpRelationListVO::getUserUnionId);
        Map<String,UserStaffCpRelationListVO> relationListVOMap= responseEntity.getData().stream().
                collect(Collectors.toMap(UserStaffCpRelationListVO::getUserUnionId, s->s, (v1,v2)->v2));

        List<Long> staffId=records.stream().map(item->item.getStaffId()).collect(Collectors.toList());
        ServerResponseEntity<List<StaffVO>> staffResponseEntity = staffFeignClient.getStaffByIds(staffId);
        Map<Long,StaffVO> stringStaffVOMap= LambdaUtils.toMap(staffResponseEntity.getData(),StaffVO::getId);

        for (CpMaterialBrowseRecord record : records) {
            List<CpMaterialLable> lables = materialLableService.listByMatId(record.getMatId());
            if(CollUtil.isEmpty(lables)){
                materialBrowseRecordMapper.finish(record.getId(),null);
                continue;
            }
            log.info("给满足条件浏览记录的会员打上对应的标签:record:{} lables:{}",JSON.toJSONString(record),JSON.toJSONString(lables));
            List<CpMaterialLable> materialLables = lables.stream()
                    .filter(s->record.getBrowseDuration()>=s.getInteractionSecond())
                    .filter(s->StrUtil.isNotEmpty(s.getRadarLabalName()))
                    .collect(Collectors.toList());
            log.info("给满足条件浏览记录的会员打上对应的标签:record:{} filter-lables:{}",JSON.toJSONString(record),JSON.toJSONString(materialLables));
            if(CollUtil.isEmpty(materialLables)){
                materialBrowseRecordMapper.finish(record.getId(),null);
                continue;
            }
            List<UpdateTag> appendTags=new ArrayList<>();
            List<String> appendTagNames=new ArrayList<>();
            for (CpMaterialLable materialLable : materialLables) {
                /**
                 *需要解析JSON
                 * [{\"tagId\":\"20231116093837293125460740000001\",\"tagName\":\"test\"},{\"tagId\":\"20231116093903227125460740000002\",\"tagName\":\"test2\",\"tagValues\":[\"枚举1\"]}]
                 */
                // 调用接口打标签
                try {
                    UpdateTagModel updateTagModel=new UpdateTagModel();
                    updateTagModel.setQiWeiUserUnionIds(ListUtil.toList(record.getUnionId()));
                    updateTagModel.setTags(materialLable.getRadarLabalName());
                    //打标签动作待实现。
//                    crmManager.updateUserTagByCrm(updateTagModel, BuildTagFromEnum.MATERIAL_BROWSE.getCode());

                    List<UpdateTag> list=JSONArray.parseArray(materialLable.getRadarLabalName(),UpdateTag.class);
                    for (UpdateTag s : list) {
                        appendTags.add(s);
                        if(!appendTagNames.contains(s.getTagName())){
                            appendTagNames.add(s.getTagName());
                        }
                    }
                    log.info("----素材互动雷达打标签  -------：【SUCCESS】");
                }catch (Exception e){
                    log.error("更新会员标签失败。",e);
                }
            }

            //更新浏览记录状态
            String labalName=JSON.toJSONString(appendTags);
            materialBrowseRecordMapper.finish(record.getId(),labalName);

            if( !stringStaffVOMap.containsKey(record.getStaffId())){
                log.error(StrUtil.format("查询导购失败，跳过给员工发信息。素材浏览记录id:{},查询参数:{}",record.getId(),record.getStaffId()));
                continue;
            }
            StaffVO staffVO = stringStaffVOMap.get(record.getStaffId());
            if(!relationListVOMap.containsKey(record.getUnionId())){
                log.error(StrUtil.format("查询客户失败，跳过给员工发信息。素材浏览记录id:{},查询参数:{}",record.getId(),record.getUnionId()));
                continue;
            }

            //要给员工发信息
            UserStaffCpRelationListVO relationListVO=relationListVOMap.get(record.getUnionId());
            String tag=appendTagNames.stream().map(item->item).distinct().collect(Collectors.joining(","));
            Material material = materialService.getById(record.getMatId());
            NotifyMsgTemplateDTO.Material materialMsg=NotifyMsgTemplateDTO.Material.builder()
                    .nickName(relationListVO.getQiWeiNickName())
                    .userName(StrUtil.isNotBlank(relationListVO.getCpRemark())?relationListVO.getCpRemark():relationListVO.getQiWeiNickName())
                    .materialName(material.getMatName())
                    .browseTime(record.getBrowseDuration().toString())
                    .browseDate(cn.hutool.core.date.DateUtil.format(record.getCreateTime(),"yyyy-MM-dd HH:mm:ss"))
                    .tag(tag)
                    .build();
            wxCpPushService.materialNotify(NotifyMsgTemplateDTO.builder().userId(""+relationListVO.getId()).qiWeiStaffId(staffVO.getQiWeiUserId()).material(materialMsg).build());
        }
    }


    public static void main(String[] strings){
        String json="[{\"tagId\":\"20240122145436759159717140000264\",\"tagName\":\"手动标签-值标签枚举\",\"tagValues\":[\"枚举1\",\"枚举2\"]},{\"tagId\":\"20240118155743128159717140000099\",\"tagName\":\"test标签\",\"tagValues\":[\"01\"]},{\"tagId\":\"20231116093903227125460740000002\",\"tagName\":\"test2\",\"tagValues\":[\"枚举1\"]}]";

//        List<String> list=JSONArray.parseArray(json,String.class);
        List<UpdateTag> list= JSONArray.parseArray(json,UpdateTag.class);
        List<UpdateTag> appendTags=new ArrayList<>();
        for (UpdateTag s : list) {
            appendTags.add(s);
        }

        String labalName=JSON.toJSONString(appendTags);
        System.out.println(labalName);
    }

}
