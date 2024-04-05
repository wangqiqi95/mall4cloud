package com.mall4j.cloud.biz.service.cp.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.mall4j.cloud.api.platform.dto.StaffQueryDTO;
import com.mall4j.cloud.api.platform.feign.StaffFeignClient;
import com.mall4j.cloud.api.platform.vo.StaffOrgVO;
import com.mall4j.cloud.api.platform.vo.StaffVO;
import com.mall4j.cloud.biz.dto.cp.MomentsSendRecordPageDTO;
import com.mall4j.cloud.biz.mapper.cp.DistributionMomentsSendRecordMapper;
import com.mall4j.cloud.biz.model.cp.DistributionMomentsSendRecord;
import com.mall4j.cloud.biz.service.cp.DistributionMomentsSendRecordService;
import com.mall4j.cloud.biz.vo.cp.DistributionMomentsSendRecordVO;
import com.mall4j.cloud.biz.vo.cp.MomentSendRecordExcelVO;
import com.mall4j.cloud.biz.vo.cp.MomentSendRecordPageVO;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.util.PageUtil;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.exception.Assert;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.common.util.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 朋友圈 员工发送记录表
 *
 * @author FrozenWatermelon
 * @date 2023-11-03 14:22:45
 */
@Slf4j
@Service
public class DistributionMomentsSendRecordServiceImpl implements DistributionMomentsSendRecordService {

    @Autowired
    private DistributionMomentsSendRecordMapper distributionMomentsSendRecordMapper;
    @Autowired
    StaffFeignClient staffFeignClient;

    @Override
    public MomentSendRecordPageVO page(PageDTO pageDTO, MomentsSendRecordPageDTO request) {

        if(request.getMomentsId()==null){
            Assert.faild("朋友圈id不允许为空。");
        }

        MomentSendRecordPageVO momentSendRecordPageVO = new MomentSendRecordPageVO();
        if(StrUtil.isNotBlank(request.getName()) || CollUtil.isNotEmpty(request.getStoreIds())){
            StaffQueryDTO staffQueryDTO = new StaffQueryDTO();
            staffQueryDTO.setKeyword(request.getName());
            staffQueryDTO.setOrgIds(request.getStoreIds());
            ServerResponseEntity<List<StaffVO>> serverResponse =  staffFeignClient.findByStaffQueryDTO(staffQueryDTO);
            log.info("查询导购ids列表，参数:{},查询结果:{}", JSONObject.toJSONString(request),JSONObject.toJSONString(serverResponse));
            if(serverResponse!=null && serverResponse.isSuccess() && CollUtil.isNotEmpty(serverResponse.getData())){
                List<Long> staffIds =  serverResponse.getData().stream().map(StaffVO::getId).collect(Collectors.toList());
                request.setStaffIds(staffIds);
            }
        }


        PageVO<DistributionMomentsSendRecordVO> pageVO = PageUtil.doPage(pageDTO, () -> distributionMomentsSendRecordMapper.page(request));
        if(pageVO.getList().size()>0){
            List<Long> staffIds = pageVO.getList().stream().map(DistributionMomentsSendRecordVO::getStaffId).collect(Collectors.toList());

            StaffQueryDTO staffQueryDTO = new StaffQueryDTO();
            staffQueryDTO.setStaffIdList(staffIds);
            ServerResponseEntity<List<StaffVO>> serverResponse =  staffFeignClient.findByStaffQueryDTO(staffQueryDTO);
            Map<Long,StaffVO> staffMap = serverResponse.getData().stream().collect(Collectors.toMap(StaffVO :: getId, Function.identity()));

            for (DistributionMomentsSendRecordVO distributionMomentsSendRecordVO : pageVO.getList()) {
                if(staffMap.containsKey(distributionMomentsSendRecordVO.getStaffId())){
                    distributionMomentsSendRecordVO.setStaffName(staffMap.get(distributionMomentsSendRecordVO.getStaffId()).getStaffName());
                    distributionMomentsSendRecordVO.setStaffCode(staffMap.get(distributionMomentsSendRecordVO.getStaffId()).getStaffNo());
                    distributionMomentsSendRecordVO.setOrgs(staffMap.get(distributionMomentsSendRecordVO.getStaffId()).getOrgs());
                }
            }
        }


        momentSendRecordPageVO.setPageVO(pageVO);

        //获取当前朋友圈 当前查询条件下总记录数
        request.setStatus(null);
        PageVO<DistributionMomentsSendRecordVO> pageVO1 = PageUtil.doPage(pageDTO, () -> distributionMomentsSendRecordMapper.page(request));
        Integer totalNum = pageVO1.getTotal().intValue();
        //查询 当前朋友圈 当前查询条件下发送 总记录数
        request.setStatus(1);
        PageVO<DistributionMomentsSendRecordVO> pageVO2 = PageUtil.doPage(pageDTO, () -> distributionMomentsSendRecordMapper.page(request));
        Integer dealSendNum = pageVO2.getTotal().intValue();

        momentSendRecordPageVO.setUnSendCount((totalNum - dealSendNum));
        momentSendRecordPageVO.setDealSendCount(dealSendNum);

        // 已经完成推送数据 / 总数据   保留两位小数 四舍五入
        if(dealSendNum==0){
            momentSendRecordPageVO.setPushRate(BigDecimal.ZERO);
        }else{
            BigDecimal pushRate = BigDecimal.valueOf(pageVO2.getTotal()).divide(BigDecimal.valueOf(pageVO1.getTotal()),2,BigDecimal.ROUND_HALF_UP);
            momentSendRecordPageVO.setPushRate(pushRate);
        }


        return momentSendRecordPageVO;
    }

    @Override
    public DistributionMomentsSendRecord getById(Long id) {
        return distributionMomentsSendRecordMapper.getById(id);
    }

    @Override
    public void save(DistributionMomentsSendRecord distributionMomentsSendRecord) {
        distributionMomentsSendRecordMapper.save(distributionMomentsSendRecord);
    }

    @Override
    public void update(DistributionMomentsSendRecord distributionMomentsSendRecord) {
        distributionMomentsSendRecordMapper.update(distributionMomentsSendRecord);
    }

    @Override
    public void deleteById(Long id) {
        distributionMomentsSendRecordMapper.deleteById(id);
    }

    @Override
    public void deleteByMomentId(Long id) {
        distributionMomentsSendRecordMapper.deleteByMomentId(id);
    }

    @Override
    public DistributionMomentsSendRecord getByMomentIdAndStaffId(Long momentsId, Long staffId) {
        return distributionMomentsSendRecordMapper.getByMomentIdAndStaffId(momentsId,staffId);
    }

    @Override
    public void doSend(Long id, String jobId) {
        distributionMomentsSendRecordMapper.doSend(id,jobId);
    }

    @Override
    public List<DistributionMomentsSendRecord> getMomentTaskResult() {
        return distributionMomentsSendRecordMapper.getMomentTaskResult();
    }

//    @Override
//    public void publish(Long id, String momentsId) {
//        distributionMomentsSendRecordMapper.publish(id,momentsId);
//    }

    @Override
    public List<DistributionMomentsSendRecord> getMomentCommentsList() {
        return distributionMomentsSendRecordMapper.getMomentCommentsList();
    }

    @Override
    public void updateMomentComment(Long id, int commentNum, int likeNum) {
        distributionMomentsSendRecordMapper.updateMomentComment(id,commentNum,likeNum);
    }

    @Override
    public List<MomentSendRecordExcelVO> orderSoldExcelList(PageDTO pageDTO, MomentsSendRecordPageDTO request) {
//        MomentSendRecordPageVO momentSendRecordPageVO = page(pageDTO,request);
//        PageVO<DistributionMomentsSendRecordVO> pageVO = momentSendRecordPageVO.getPageVO();

        if(request.getMomentsId()==null){
            Assert.faild("朋友圈id不允许为空。");
        }
        if(StrUtil.isNotBlank(request.getName()) || CollUtil.isNotEmpty(request.getStoreIds())){
            StaffQueryDTO staffQueryDTO = new StaffQueryDTO();
            staffQueryDTO.setKeyword(request.getName());
            staffQueryDTO.setOrgIds(request.getStoreIds());
            ServerResponseEntity<List<StaffVO>> serverResponse =  staffFeignClient.findByStaffQueryDTO(staffQueryDTO);
            log.info("查询导购ids列表，参数:{},查询结果:{}", JSONObject.toJSONString(request),JSONObject.toJSONString(serverResponse));
            if(serverResponse!=null && serverResponse.isSuccess() && CollUtil.isNotEmpty(serverResponse.getData())){
                List<Long> staffIds =  serverResponse.getData().stream().map(StaffVO::getId).collect(Collectors.toList());
                request.setStaffIds(staffIds);
            }
        }
        List<DistributionMomentsSendRecordVO> list = distributionMomentsSendRecordMapper.page(request);

        if(CollUtil.isEmpty(list)){
            return new ArrayList<>();
        }
        List<MomentSendRecordExcelVO> voList = new ArrayList<>();
        if(list.size()>0){
            List<Long> staffIds = list.stream().map(DistributionMomentsSendRecordVO::getStaffId).collect(Collectors.toList());

            StaffQueryDTO staffQueryDTO = new StaffQueryDTO();
            staffQueryDTO.setStaffIdList(staffIds);
            ServerResponseEntity<List<StaffVO>> serverResponse =  staffFeignClient.findByStaffQueryDTO(staffQueryDTO);
            Map<Long,StaffVO> staffMap = serverResponse.getData().stream().collect(Collectors.toMap(StaffVO :: getId, Function.identity()));

            for (DistributionMomentsSendRecordVO vo : list) {
                if(staffMap.containsKey(vo.getStaffId())){
                    vo.setStaffName(staffMap.get(vo.getStaffId()).getStaffName());
                    vo.setStaffCode(staffMap.get(vo.getStaffId()).getStaffNo());
                    vo.setOrgs(staffMap.get(vo.getStaffId()).getOrgs());
                }
                MomentSendRecordExcelVO recordExcelVO = new MomentSendRecordExcelVO();
                recordExcelVO.setStaffName(vo.getStaffName()+"/"+vo.getStaffCode());
                recordExcelVO.setQwLikeNum(vo.getQwLikeNum());
                recordExcelVO.setQwCommentNum(vo.getQwCommentNum());
                if(vo.getSendTime()!=null){
                    recordExcelVO.setSendTime(DateUtil.parseDateToStr("yyyy-MM-dd HH:mm:ss",vo.getSendTime()));
                }

                recordExcelVO.setStatusName(vo.getStatus()==0?"否":"是");

                if(CollUtil.isNotEmpty(vo.getOrgs())){
                    List<String> names = vo.getOrgs().stream()
                            .map(StaffOrgVO::getOrgName)
                            .collect(Collectors.toList());
                    recordExcelVO.setOrgNames(String.join(",", names));
                }
                voList.add(recordExcelVO);
            }
        }



//        List<MomentSendRecordExcelVO> voList = new ArrayList<>();
//        for (DistributionMomentsSendRecordVO vo : pageVO.getList()) {
//            MomentSendRecordExcelVO recordExcelVO = new MomentSendRecordExcelVO();
//            recordExcelVO.setStaffName(vo.getStaffName()+"/"+vo.getStaffCode());
//            recordExcelVO.setQwLikeNum(vo.getQwLikeNum());
//            recordExcelVO.setQwCommentNum(vo.getQwCommentNum());
//            if(vo.getSendTime()!=null){
//                recordExcelVO.setSendTime(DateUtil.parseDateToStr("yyyy-MM-dd HH:mm:ss",vo.getSendTime()));
//            }
//
//            recordExcelVO.setStatusName(vo.getStatus()==0?"否":"是");
//
//            if(CollUtil.isNotEmpty(vo.getOrgs())){
//                List<String> names = vo.getOrgs().stream()
//                        .map(StaffOrgVO::getOrgName)
//                        .collect(Collectors.toList());
//                recordExcelVO.setOrgNames(String.join(",", names));
//            }
//            voList.add(recordExcelVO);
//        }
        return voList;
    }

    @Override
    public List<DistributionMomentsSendRecord> getByMomentId(Long id) {
        return distributionMomentsSendRecordMapper.getByMomentId(id);
    }
}
