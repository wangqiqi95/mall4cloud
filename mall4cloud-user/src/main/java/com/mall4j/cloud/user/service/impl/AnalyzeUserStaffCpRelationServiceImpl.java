package com.mall4j.cloud.user.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import com.mall4j.cloud.api.biz.dto.UserRegisterQiWeiMsgDTO;
import com.mall4j.cloud.api.biz.feign.UserRegisterSendMsgFeignClient;
import com.mall4j.cloud.api.platform.dto.StaffQueryDTO;
import com.mall4j.cloud.api.platform.feign.StaffFeignClient;
import com.mall4j.cloud.api.platform.vo.StaffVO;
import com.mall4j.cloud.api.user.constant.ContactAutoTypeEnum;
import com.mall4j.cloud.api.user.constant.ContactChangeTypeEnum;
import com.mall4j.cloud.api.user.dto.UserStaffCpRelationSearchDTO;
import com.mall4j.cloud.api.user.dto.UserWeixinccountFollowSelectDTO;
import com.mall4j.cloud.api.user.vo.CountNotMemberUsersVO;
import com.mall4j.cloud.api.user.vo.UserStaffCpRelationListVO;
import com.mall4j.cloud.api.user.vo.UserWeixinAccountFollowDataListVo;
import com.mall4j.cloud.api.user.vo.cp.AnalyzeUserStaffCpRelationDetailVO;
import com.mall4j.cloud.api.user.vo.cp.AnalyzeUserStaffCpRelationExportVO;
import com.mall4j.cloud.api.user.vo.cp.AnalyzeUserStaffCpRelationListVO;
import com.mall4j.cloud.api.user.vo.cp.AnalyzeUserStaffCpRelationVO;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.util.PageUtil;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.exception.LuckException;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.common.util.LambdaUtils;
import com.mall4j.cloud.user.bo.GroupPushTaskCpRelationBO;
import com.mall4j.cloud.user.dto.analyze.AnalyzeUserStaffCpRelationDTO;
import com.mall4j.cloud.user.mapper.AnalyzeUserStaffCpRelationMapper;
import com.mall4j.cloud.user.mapper.UserStaffCpRelationMapper;
import com.mall4j.cloud.user.model.UserStaffCpRelation;
import com.mall4j.cloud.user.service.AnalyzeUserStaffCpRelationService;
import com.mall4j.cloud.user.service.UserStaffCpRelationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ma.glasnost.orika.MapperFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 
 *
 * @author FrozenWatermelon
 * @date 2022-02-15 19:39:12
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class AnalyzeUserStaffCpRelationServiceImpl extends ServiceImpl<UserStaffCpRelationMapper,UserStaffCpRelation> implements AnalyzeUserStaffCpRelationService {

    private final AnalyzeUserStaffCpRelationMapper analyzeCpRelationMapper;
    private final StaffFeignClient staffFeignClient;
    private final MapperFacade mapperFacade;

    /**
     * 渠道活码详情-好友统计分页列表
     * @param pageDTO
     * @param dto
     * @return
     */
    @Override
    public PageVO<AnalyzeUserStaffCpRelationListVO> getAnalyzeUSRFPage(PageDTO pageDTO, AnalyzeUserStaffCpRelationDTO dto) {
        PageVO<AnalyzeUserStaffCpRelationListVO> pageVO=PageUtil.doPage(pageDTO, () -> analyzeCpRelationMapper.selectAnalyzeUSRFPage(dto));

        initData(pageVO.getList());

        return pageVO;
    }

    @Override
    public List<AnalyzeUserStaffCpRelationExportVO> exportAnalyzeUSRFPage(AnalyzeUserStaffCpRelationDTO dto) {

        List<AnalyzeUserStaffCpRelationListVO> listVOS=analyzeCpRelationMapper.selectAnalyzeUSRFPage(dto);
        initData(listVOS);

        List<AnalyzeUserStaffCpRelationExportVO> backs=mapperFacade.mapAsList(listVOS,AnalyzeUserStaffCpRelationExportVO.class);
        for (AnalyzeUserStaffCpRelationExportVO back : backs) {
            if(StrUtil.isNotEmpty(back.getUserPhone())){
                try {
                    List<String> phones= JSONArray.parseArray(back.getUserPhone(),String.class);
                    if(CollUtil.isNotEmpty(phones)){
                        back.setUserPhone(phones.stream().collect(Collectors.joining(",")));
                    }
                }catch (Exception e){
                    log.info("exportAnalyzeUSRFPage {}",e);
                }

            }
        }

        return backs;
    }

    private void initData(List<AnalyzeUserStaffCpRelationListVO> listVOS){
        //获取接待员信息
        List<Long> staffIds=listVOS.stream().map(item->item.getStaffId()).collect(Collectors.toList());
        ServerResponseEntity<List<StaffVO>> responseEntity= staffFeignClient.getStaffByIds(staffIds);
        ServerResponseEntity.checkResponse(responseEntity);
        Map<Long,StaffVO> staffVOMap= LambdaUtils.toMap(responseEntity.getData(),StaffVO::getId);
        for (AnalyzeUserStaffCpRelationListVO relationListVO : listVOS) {
            if(staffVOMap.containsKey(relationListVO.getStaffId())){
                relationListVO.setStaffName(staffVOMap.get(relationListVO.getStaffId()).getStaffName());
            }
            /**
             * 好友关系状态
             * 被员工删除：3
             * 被客户删除：4
             * 删除是6
             * 其他 都是正常
             */
            if(Objects.nonNull(relationListVO.getContactChangeType())){
                if(relationListVO.getContactChangeType()< ContactChangeTypeEnum.DEL_EXTERNAL_CONTACT.getCode() ||
                        relationListVO.getContactChangeType()== ContactChangeTypeEnum.TRANSFER_FAIL.getCode()){
                    relationListVO.setContactChangeTypeName("正常");
                }else{
                    relationListVO.setContactChangeTypeName(ContactChangeTypeEnum.getDescName(relationListVO.getContactChangeType()));
                }
            }
            //0自动通过/1手动通过
            if(Objects.nonNull(relationListVO.getAutoType())){
                relationListVO.setAutoTypeName(ContactAutoTypeEnum.getDesc(relationListVO.getAutoType()));
            }
        }
    }

    /**
     * 渠道活码详情-新增客户折线图
     * @param dto
     * @return
     */
    @Override
    public AnalyzeUserStaffCpRelationDetailVO getAnalyzeNewUSRFList(AnalyzeUserStaffCpRelationDTO dto) {
        AnalyzeUserStaffCpRelationDetailVO detailVO=new AnalyzeUserStaffCpRelationDetailVO();
        List<AnalyzeUserStaffCpRelationVO> back=getBack(dto);
        List<AnalyzeUserStaffCpRelationVO> list=analyzeCpRelationMapper.selectAnalyzeNewUSRFList(dto);
        if(CollUtil.isNotEmpty(list)){
            Map<String,AnalyzeUserStaffCpRelationVO> relationVOMap=LambdaUtils.toMap(list,AnalyzeUserStaffCpRelationVO::getDateKey);
            for (AnalyzeUserStaffCpRelationVO relationVO : back) {
                if(relationVOMap.containsKey(relationVO.getDateKey())){
                    relationVO.setCount(relationVOMap.get(relationVO.getDateKey()).getCount());
                }
            }
        }
        //折线图数据
        detailVO.setRelationVOS(back);
        //总和数据
        detailVO.setCount(analyzeCpRelationMapper.countAnalyzeNewUSRF(dto));
        return detailVO;
    }

    private List<AnalyzeUserStaffCpRelationVO> getBack(AnalyzeUserStaffCpRelationDTO dto){
        List<AnalyzeUserStaffCpRelationVO> back=new ArrayList<>();
        if(StrUtil.isEmpty(dto.getStartTime()) || StrUtil.isEmpty(dto.getEndTime()) ){
            return back;
        }
        Date startTime=DateUtil.parse(dto.getStartTime(),"yyyy-MM-dd HH:mm:ss");
        Date endTime=DateUtil.parse(dto.getEndTime(),"yyyy-MM-dd HH:mm:ss");
        List<DateTime> dateTimes=DateUtil.rangeToList(startTime,endTime, DateField.DAY_OF_YEAR);//按天
        for (DateTime dateTime : dateTimes) {
            String time=DateUtil.format(dateTime,"yyyy-MM-dd");
            AnalyzeUserStaffCpRelationVO follow=new AnalyzeUserStaffCpRelationVO();
            follow.setDateKey(time);
            follow.setCount(0);
            back.add(follow);
        }
        return back;
    }

    /**
     * 渠道活码详情-所有客户折线图
     * @param dto
     * @return
     */
    @Override
    public List<AnalyzeUserStaffCpRelationVO> getAnalyzeAllUSRFList(AnalyzeUserStaffCpRelationDTO dto) {
        return null;
    }

    /**
     * 渠道活码详情-数据明细
     * @param dto
     * @return
     */
    @Override
    public PageVO<AnalyzeUserStaffCpRelationVO> getAnalyzeUSRFDetailPage(PageDTO pageDTO, AnalyzeUserStaffCpRelationDTO dto) {
        return null;
    }
}
