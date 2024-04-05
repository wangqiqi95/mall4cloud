package com.mall4j.cloud.flow.service.impl;

import com.google.common.collect.Lists;
import com.mall4j.cloud.api.delivery.feign.AreaFeignClient;
import com.mall4j.cloud.api.delivery.vo.AreaVO;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.flow.constant.SystemTypeEnum;
import com.mall4j.cloud.flow.dto.FlowAnalysisDTO;
import com.mall4j.cloud.flow.mapper.UserAnalysisMapper;
import com.mall4j.cloud.flow.service.UserVisitAnalysisService;
import com.mall4j.cloud.flow.vo.FlowUserAnalysisVO;
import com.mall4j.cloud.flow.vo.SystemVO;
import com.mall4j.cloud.flow.vo.UserAnalysisVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 流量分析—用户访问数据
 *
 * @author YXF
 * @date 2021-05-21 15:25:19
 */
@Service
public class UserVisitAnalysisServiceImpl implements UserVisitAnalysisService {

    @Autowired
    private UserAnalysisMapper userAnalysisMapper;

    @Autowired
    private AreaFeignClient areaFeignClient;

    @Override
    public FlowUserAnalysisVO getUserAnalysisData(FlowAnalysisDTO flowAnalysisDTO) {
        FlowUserAnalysisVO flowUserAnalysisVO = new FlowUserAnalysisVO();
        ServerResponseEntity<List<AreaVO>> provinceAreaRes = areaFeignClient.listProvinceArea();
        Map<Long, AreaVO> areaMap = provinceAreaRes.getData().stream().collect(Collectors.toMap(AreaVO::getAreaId, areaVO -> areaVO));
        // 访问深度
        flowUserAnalysisVO.setVisitPageList(this.getVisitPageList(flowAnalysisDTO));
        // 访问地域分布
        flowUserAnalysisVO.setVisitUserList(this.getUserAccessData(flowAnalysisDTO, areaMap));
        // 系统设备访问情况
        List<SystemVO> systemList = this.getSystemData(flowAnalysisDTO);
        for (SystemVO systemVO : systemList) {
            switch (Objects.requireNonNull(SystemTypeEnum.instance(systemVO.getSystemType()))) {
                case H5:
                    flowUserAnalysisVO.setH5(systemVO.getUserNums() == null ? 0 : systemVO.getUserNums());
                    break;
                case PC:
                    flowUserAnalysisVO.setPc(systemVO.getUserNums() == null ? 0 : systemVO.getUserNums());
                    break;
                case IOS:
                    flowUserAnalysisVO.setIos(systemVO.getUserNums() == null ? 0 : systemVO.getUserNums());
                    break;
                case ANDROID:
                    flowUserAnalysisVO.setAndroid(systemVO.getUserNums() == null ? 0 : systemVO.getUserNums());
                    break;
                case APPLETS:
                    flowUserAnalysisVO.setApplets(systemVO.getUserNums() == null ? 0 : systemVO.getUserNums());
                    break;
                default:
                    break;
            }
        }
        return flowUserAnalysisVO;
    }

    /**
     * 获取访问深度数据
     * @param flowAnalysisDTO
     * @return
     */
    private List<FlowUserAnalysisVO.VisitNum> getVisitPageList(FlowAnalysisDTO flowAnalysisDTO) {
        // 访问深度Map
        Map<Integer, FlowUserAnalysisVO.VisitNum> visitPageNumMap = new HashMap<>(16);
        List<UserAnalysisVO> userAnalysisVOList = userAnalysisMapper.listUserAnalysisByData(flowAnalysisDTO.getStartTime(), flowAnalysisDTO.getEndTime());
        for (UserAnalysisVO userAnalysisVO : userAnalysisVOList) {
            // 访问深度
            FlowUserAnalysisVO.VisitNum visitNum = null;
            Integer visitId = getVisitIdByVisitNums(userAnalysisVO.getVisitNums());
            if (!visitPageNumMap.containsKey(visitId)) {
                visitNum = new FlowUserAnalysisVO.VisitNum(visitId, 0);
                visitPageNumMap.put(visitId, visitNum);
            }
            visitNum = visitPageNumMap.get(visitId);
            visitNum.setUserNums(visitNum.getUserNums() + 1);
        }
        List<FlowUserAnalysisVO.VisitNum> visitNumList = Lists.newArrayList();
        // 分组
        for (int i = 1; i <= 8; i++) {
            if (visitPageNumMap.containsKey(i)) {
                visitNumList.add(visitPageNumMap.get(i));
            } else {
                FlowUserAnalysisVO.VisitNum visitNum = new FlowUserAnalysisVO.VisitNum(i, 0);
                visitNumList.add(visitNum);
            }
        }
        return visitNumList;
    }

    /**
     * 获取访问地域分布数据
     * @param flowAnalysisDTO
     * @param areaMap
     * @return
     */
    private List<UserAnalysisVO> getUserAccessData(FlowAnalysisDTO flowAnalysisDTO, Map<Long, AreaVO> areaMap) {
        List<UserAnalysisVO> userAnalysisList = userAnalysisMapper.listUserAccessDataByProvinceAndDate(flowAnalysisDTO);
        userAnalysisList.forEach(item -> {
            if (Objects.nonNull(areaMap.get(item.getProvinceId()))) {
                item.setProvinceName(areaMap.get(item.getProvinceId()).getAreaName());
            } else {
                item.setProvinceName("未知");
            }
        });
        return userAnalysisList;
    }

    /**
     * 获取系统访客数量
     * @param flowAnalysisDTO
     * @return
     */
    private List<SystemVO> getSystemData(FlowAnalysisDTO flowAnalysisDTO) {
        return userAnalysisMapper.systemTypeNums(flowAnalysisDTO);
    }

    /**
     *  获取访问编号
     * @param visitNums  (编号:访问页面数量)1:1 2:2 3:3 4:4 5:5 6:6-10 7:11-20 8:20+
     * @return
     */
    private Integer getVisitIdByVisitNums(Integer visitNums) {
        if (visitNums < 6){
            return visitNums;
        } else if ((visitNums >= 6 && visitNums <= 10)){
            //6-10个页面
            return 6;
        } else if ((visitNums >= 11 && visitNums <= 20)){
            //11-20个页面
            return 7;
        }
        return 8;
    }
}
