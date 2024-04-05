package com.mall4j.cloud.biz.service.cp.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mall4j.cloud.api.platform.dto.StaffQueryDTO;
import com.mall4j.cloud.api.platform.feign.StaffFeignClient;
import com.mall4j.cloud.api.platform.vo.StaffVO;
import com.mall4j.cloud.api.user.dto.UserStaffCpRelationSearchDTO;
import com.mall4j.cloud.api.user.feign.UserStaffCpRelationFeignClient;
import com.mall4j.cloud.api.user.vo.UserStaffCpRelationListVO;
import com.mall4j.cloud.biz.dto.cp.DistributionMomentsCommentsDTO;
import com.mall4j.cloud.biz.dto.cp.DistributionMomentsCommentsPageDTO;
import com.mall4j.cloud.biz.mapper.cp.DistributionMomentsCommentsMapper;
import com.mall4j.cloud.biz.model.cp.DistributionMomentsSendRecord;
import com.mall4j.cloud.biz.service.cp.DistributionMomentsCommentsService;
import com.mall4j.cloud.biz.vo.cp.DistributionMomentsCommentsVO;
import com.mall4j.cloud.biz.wx.wx.util.WechatUtils;
import com.mall4j.cloud.common.database.util.PageUtil;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.common.util.LambdaUtils;
import me.chanjar.weixin.cp.bean.external.WxCpGetMomentComments;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.mall4j.cloud.biz.model.cp.DistributionMomentsComments;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.vo.PageVO;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 朋友圈互动明细数据
 *
 * @author gmq
 * @date 2024-03-04 16:47:40
 */
@Service
public class DistributionMomentsCommentsServiceImpl extends ServiceImpl<DistributionMomentsCommentsMapper,DistributionMomentsComments> implements DistributionMomentsCommentsService {

    @Autowired
    private DistributionMomentsCommentsMapper distributionMomentsCommentsMapper;
    @Autowired
    private StaffFeignClient staffFeignClient;
    @Autowired
    private UserStaffCpRelationFeignClient userStaffCpRelationFeignClient;

    @Override
    public PageVO<DistributionMomentsCommentsVO> page(PageDTO pageDTO, DistributionMomentsCommentsPageDTO dto) {
        PageVO<DistributionMomentsCommentsVO> pageVO=PageUtil.doPage(pageDTO, () -> distributionMomentsCommentsMapper.list(dto));

        if(CollUtil.isNotEmpty(pageVO.getList())){
            List<String> userIds=pageVO.getList().stream().map(item->item.getUserId()).distinct().collect(Collectors.toList());

            StaffQueryDTO staffQueryDTO=new StaffQueryDTO();
            staffQueryDTO.setQiWeiUserIds(userIds);
            ServerResponseEntity<List<StaffVO>>  staffResponseEntity=staffFeignClient.findByStaffQueryDTO(staffQueryDTO);
            ServerResponseEntity.checkResponse(staffResponseEntity);

            UserStaffCpRelationSearchDTO staffCpRelationSearchDTO=new UserStaffCpRelationSearchDTO();
            staffCpRelationSearchDTO.initPage();
            staffCpRelationSearchDTO.setQiWeiUserIds(userIds);
            ServerResponseEntity<List<UserStaffCpRelationListVO>> userResponseEntity=userStaffCpRelationFeignClient.getUserStaffRelBy(staffCpRelationSearchDTO);
            ServerResponseEntity.checkResponse(userResponseEntity);

            //员工信息
            Map<String,StaffVO> staffVOMap=LambdaUtils.toMap(staffResponseEntity.getData(),StaffVO::getQiWeiUserId);
            //客户信息
            Map<String,UserStaffCpRelationListVO> relationListVOMap=LambdaUtils.toMap(userResponseEntity.getData(),UserStaffCpRelationListVO::getQiWeiUserId);

            for (DistributionMomentsCommentsVO commentsVO : pageVO.getList()) {
                if(staffVOMap.containsKey(commentsVO.getUserId())){
                    commentsVO.setUserName(staffVOMap.get(commentsVO.getUserId()).getStaffName());
                }
                if(relationListVOMap.containsKey(commentsVO.getUserId())){
                    commentsVO.setUserName(relationListVOMap.get(commentsVO.getUserId()).getQiWeiNickName());
                    commentsVO.setCpRemark(relationListVOMap.get(commentsVO.getUserId()).getCpRemark());
                }
            }


        }

        return pageVO;
    }

    @Override
    public DistributionMomentsComments getById(Long id) {
        return distributionMomentsCommentsMapper.getById(id);
    }

    @Override
    public void saveTo(DistributionMomentsCommentsDTO commentsDTO) {
        DistributionMomentsSendRecord sendRecord=commentsDTO.getSendRecord();
        LambdaQueryWrapper<DistributionMomentsComments> lambdaQueryWrapper=new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(DistributionMomentsComments::getStaffSendRecordId,sendRecord.getId());
        lambdaQueryWrapper.eq(DistributionMomentsComments::getIsDelete,0);
        List<DistributionMomentsComments> comments=this.list(lambdaQueryWrapper);

        //评论
        List<DistributionMomentsComments> commentList=comments.stream().filter(item->item.getType()==0).collect(Collectors.toList());
        if(CollUtil.isEmpty(commentList)){
            List<DistributionMomentsComments> adds=new ArrayList<>();
            for (WxCpGetMomentComments.CommentLikeItem item : commentsDTO.getCommentList()) {
                DistributionMomentsComments comment=new DistributionMomentsComments();
                comment.setCreateBy("定时任务");
                comment.setCreateTime(WechatUtils.formatDate(item.getCreateTime().toString()));
                comment.setUserId(StrUtil.isNotEmpty(item.getExternalUserId())?item.getExternalUserId():item.getUserid());
                comment.setIsDelete(0);
                comment.setMomentsId(sendRecord.getMomentsId());
                comment.setStaffSendRecordId(sendRecord.getId());
                comment.setStaffId(sendRecord.getStaffId());
                comment.setStaffUserId(sendRecord.getQiweiUserId());
                comment.setType(0);
                adds.add(comment);
            }
            this.saveBatch(adds);
        }else{
            Map<String,DistributionMomentsComments> commentsMap= LambdaUtils.toMap(commentList,DistributionMomentsComments::getUserId);
            Map<String,DistributionMomentsComments> addcommentsMap=new HashMap<>();
            for (WxCpGetMomentComments.CommentLikeItem item : commentsDTO.getCommentList()) {
                if(StrUtil.isNotEmpty(item.getExternalUserId()) && !commentsMap.containsKey(item.getExternalUserId())){
                    DistributionMomentsComments comment=new DistributionMomentsComments();
                    comment.setCreateBy("定时任务");
                    comment.setCreateTime(WechatUtils.formatDate(item.getCreateTime().toString()));
                    comment.setUserId(item.getExternalUserId());
                    comment.setIsDelete(0);
                    comment.setMomentsId(sendRecord.getMomentsId());
                    comment.setStaffSendRecordId(sendRecord.getId());
                    comment.setStaffId(sendRecord.getStaffId());
                    comment.setStaffUserId(sendRecord.getQiweiUserId());
                    comment.setType(0);
                    if(!addcommentsMap.containsKey(item.getExternalUserId())){
                        addcommentsMap.put(item.getExternalUserId(),comment);
                    }
                }
                if(StrUtil.isNotEmpty(item.getUserid()) && commentsMap.containsKey(item.getUserid())){
                    DistributionMomentsComments comment=new DistributionMomentsComments();
                    comment.setCreateBy("定时任务");
                    comment.setCreateTime(WechatUtils.formatDate(item.getCreateTime().toString()));
                    comment.setUserId(item.getUserid());
                    comment.setIsDelete(0);
                    comment.setMomentsId(sendRecord.getMomentsId());
                    comment.setStaffSendRecordId(sendRecord.getId());
                    comment.setStaffId(sendRecord.getStaffId());
                    comment.setStaffUserId(sendRecord.getQiweiUserId());
                    comment.setType(0);
                    if(!addcommentsMap.containsKey(item.getUserid())){
                        addcommentsMap.put(item.getUserid(),comment);
                    }
                }
            }
            if(CollUtil.isNotEmpty(addcommentsMap)){
                this.saveBatch(new ArrayList<>(addcommentsMap.values()));
            }
        }

        //点赞
        List<DistributionMomentsComments> likeList=comments.stream().filter(item->item.getType()==1).collect(Collectors.toList());
        if(CollUtil.isEmpty(likeList)){
            List<DistributionMomentsComments> adds=new ArrayList<>();
            for (WxCpGetMomentComments.CommentLikeItem item : commentsDTO.getLikeList()) {
                DistributionMomentsComments comment=new DistributionMomentsComments();
                comment.setCreateBy("定时任务");
                comment.setCreateTime(WechatUtils.formatDate(item.getCreateTime().toString()));
                comment.setUserId(StrUtil.isNotEmpty(item.getExternalUserId())?item.getExternalUserId():item.getUserid());
                comment.setIsDelete(0);
                comment.setMomentsId(sendRecord.getMomentsId());
                comment.setStaffSendRecordId(sendRecord.getId());
                comment.setStaffId(sendRecord.getStaffId());
                comment.setStaffUserId(sendRecord.getQiweiUserId());
                comment.setType(1);
                adds.add(comment);
            }
            this.saveBatch(adds);
        }else{
            Map<String,DistributionMomentsComments> commentsMap= LambdaUtils.toMap(likeList,DistributionMomentsComments::getUserId);
            Map<String,DistributionMomentsComments> addcommentsMap=new HashMap<>();
            for (WxCpGetMomentComments.CommentLikeItem item : commentsDTO.getLikeList()) {
                if(StrUtil.isNotEmpty(item.getExternalUserId()) && !commentsMap.containsKey(item.getExternalUserId())){
                    DistributionMomentsComments comment=new DistributionMomentsComments();
                    comment.setCreateBy("定时任务");
                    comment.setCreateTime(WechatUtils.formatDate(item.getCreateTime().toString()));
                    comment.setUserId(item.getExternalUserId());
                    comment.setIsDelete(0);
                    comment.setMomentsId(sendRecord.getMomentsId());
                    comment.setStaffSendRecordId(sendRecord.getId());
                    comment.setStaffId(sendRecord.getStaffId());
                    comment.setStaffUserId(sendRecord.getQiweiUserId());
                    comment.setType(1);
                    if(!addcommentsMap.containsKey(item.getExternalUserId())){
                        addcommentsMap.put(item.getExternalUserId(),comment);
                    }
                }
                if(StrUtil.isNotEmpty(item.getUserid()) && commentsMap.containsKey(item.getUserid())){
                    DistributionMomentsComments comment=new DistributionMomentsComments();
                    comment.setCreateBy("定时任务");
                    comment.setCreateTime(WechatUtils.formatDate(item.getCreateTime().toString()));
                    comment.setUserId(item.getUserid());
                    comment.setIsDelete(0);
                    comment.setMomentsId(sendRecord.getMomentsId());
                    comment.setStaffSendRecordId(sendRecord.getId());
                    comment.setStaffId(sendRecord.getStaffId());
                    comment.setStaffUserId(sendRecord.getQiweiUserId());
                    comment.setType(1);
                    if(!addcommentsMap.containsKey(item.getUserid())){
                        addcommentsMap.put(item.getUserid(),comment);
                    }
                }
            }
            if(CollUtil.isNotEmpty(addcommentsMap)){
                this.saveBatch(new ArrayList<>(addcommentsMap.values()));
            }
        }

    }


}
