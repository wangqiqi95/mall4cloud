package com.mall4j.cloud.distribution.service.impl;

import cn.hutool.core.util.StrUtil;
import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import com.alibaba.fastjson.JSONObject;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.util.PageUtil;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.exception.LuckException;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.common.security.AuthUserContext;

import com.mall4j.cloud.distribution.dto.DistributionStoreActivityQueryDTO;
import com.mall4j.cloud.distribution.dto.DistributionStoreActivityUpdateDTO;
import com.mall4j.cloud.distribution.mapper.DistributionStoreActivityMapper;
import com.mall4j.cloud.distribution.mapper.DistributionStoreActivityUserMapper;
import com.mall4j.cloud.distribution.model.DistributionStoreActivity;
import com.mall4j.cloud.distribution.model.DistributionStoreActivitySizes;
import com.mall4j.cloud.distribution.model.DistributionStoreActivityUser;
import com.mall4j.cloud.distribution.service.DistributionStoreActivityService;
import com.mall4j.cloud.distribution.vo.DistributionStoreActivityCountVO;
import com.mall4j.cloud.distribution.vo.DistributionStoreActivityProvinceCountVO;
import com.mall4j.cloud.distribution.vo.StoreActivityProvinceVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.TreeSet;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 门店活动
 *
 * @author gww
 * @date 2021-12-26 21:17:59
 */
@Service
@Slf4j
public class DistributionStoreActivityServiceImpl implements DistributionStoreActivityService {

    @Autowired
    private DistributionStoreActivityMapper distributionStoreActivityMapper;
    @Autowired
    private DistributionStoreActivityUserMapper distributionStoreActivityUserMapper;

    @Override
    public PageVO<DistributionStoreActivity> page(PageDTO pageDTO, DistributionStoreActivityQueryDTO distributionStoreActivityQueryDTO) {
        PageVO<DistributionStoreActivity> pageVO = PageUtil.doPage(pageDTO, () -> distributionStoreActivityMapper.list(distributionStoreActivityQueryDTO));
        if (!CollectionUtils.isEmpty(pageVO.getList())) {
            List<Long> activityIdList = pageVO.getList().stream().map(DistributionStoreActivity :: getId).collect(Collectors.toList());
            Map<Long, List<DistributionStoreActivityUser>> applyMap = distributionStoreActivityUserMapper.listByActivityIdIdList(activityIdList)
                    .stream().filter(d -> d.getStatus() == 0).collect(Collectors.groupingBy(DistributionStoreActivityUser :: getActivityId));
            Map<Long, List<DistributionStoreActivityUser>> signMap = distributionStoreActivityUserMapper.listByActivityIdIdList(activityIdList)
                    .stream().filter(d -> d.getSignStatus() == 1).collect(Collectors.groupingBy(DistributionStoreActivityUser :: getActivityId));
            pageVO.getList().stream().forEach(distributionStoreActivity -> {
                Long activityId = distributionStoreActivity.getId();
                distributionStoreActivity.setApplyNum(0);
                distributionStoreActivity.setSignNum(0);
                if (applyMap.containsKey(activityId)) {
                    distributionStoreActivity.setApplyNum(applyMap.get(activityId).size());
                }
                if (signMap.containsKey(activityId)) {
                    distributionStoreActivity.setSignNum(signMap.get(activityId).size());
                }
                Date now = new Date();
                if(distributionStoreActivity.getStatus() == 0){
                    distributionStoreActivity.setActivityStatus(3);
                }
                if(distributionStoreActivity.getStatus() == 1){
                    if (now.before(distributionStoreActivity.getStartTime())) {
                        distributionStoreActivity.setActivityStatus(0);
                    }
                    if (now.before(distributionStoreActivity.getEndTime()) && now.after(distributionStoreActivity.getStartTime())) {
                        distributionStoreActivity.setActivityStatus(1);
                    }
                    if (now.after(distributionStoreActivity.getEndTime())) {
                        distributionStoreActivity.setActivityStatus(2);
                    }
                }
            });
        }
        return pageVO;
    }

    @Override
    public PageVO<DistributionStoreActivity> pageEffect(PageDTO pageDTO, DistributionStoreActivityQueryDTO distributionStoreActivityQueryDTO) {
        return PageUtil.doPage(pageDTO, () -> distributionStoreActivityMapper.listEffect(distributionStoreActivityQueryDTO));
    }

    @Override
    public DistributionStoreActivityCountVO count() {
        DistributionStoreActivityCountVO countVO = new DistributionStoreActivityCountVO();
        List<DistributionStoreActivity> distributionStoreActivityList = distributionStoreActivityMapper.listEffect(new DistributionStoreActivityQueryDTO());
        if (!CollectionUtils.isEmpty(distributionStoreActivityList)) {
            Date now = new Date();
            Long notStart  = distributionStoreActivityList.stream().filter(d -> now.before(d.getStartTime())).count();
            Long inProgress = distributionStoreActivityList.stream().filter(d -> now.after(d.getStartTime())
                    && now.before(d.getEndTime())).count();
            Long finished = distributionStoreActivityList.stream().filter(d -> now.after(d.getEndTime())).count();
            countVO.setFinished(finished);
            countVO.setInProgress(inProgress);
            countVO.setNotStart(notStart);
        }
        return countVO;
    }

    @Override
    public PageVO<DistributionStoreActivity> appPageEffect(PageDTO pageDTO, DistributionStoreActivityQueryDTO distributionStoreActivityQueryDTO) {
        Long userId = AuthUserContext.get().getUserId();
        distributionStoreActivityQueryDTO.setUserId(userId);
        PageVO<DistributionStoreActivity> pageVO = PageUtil.doPage(pageDTO, () -> distributionStoreActivityMapper.listAppEffect(distributionStoreActivityQueryDTO));
        if (!CollectionUtils.isEmpty(pageVO.getList())) {
            List<DistributionStoreActivityUser> distributionStoreActivityUserList = distributionStoreActivityUserMapper.listByUserId(userId);
            Map<Long, Integer> distributionStoreActivityUserMap = distributionStoreActivityUserList.stream().collect(Collectors.toMap(DistributionStoreActivityUser :: getActivityId,
                            DistributionStoreActivityUser :: getStatus));
            List<Long> activityIdList = pageVO.getList().stream().map(DistributionStoreActivity :: getId).collect(Collectors.toList());
            Map<Long, List<DistributionStoreActivityUser>> applyMap = distributionStoreActivityUserMapper.listByActivityIdIdList(activityIdList)
                    .stream().filter(d -> d.getStatus() == 0).collect(Collectors.groupingBy(DistributionStoreActivityUser :: getActivityId));
            Date now = new Date();
            pageVO.getList().stream().forEach(distributionStoreActivity -> {
                distributionStoreActivity.setApplyStatus(0);
                distributionStoreActivity.setApplyNum(0);
                Integer status = distributionStoreActivityUserMap.get(distributionStoreActivity.getId());
                if (status != null) {
                    distributionStoreActivity.setApplyStatus(status == 0 ? 1 : 2);
                }
                if (applyMap.containsKey(distributionStoreActivity.getId())) {
                    distributionStoreActivity.setApplyNum(applyMap.get(distributionStoreActivity.getId()).size());
                }
                if (now.before(distributionStoreActivity.getStartTime())) {
                    distributionStoreActivity.setActivityStatus(0);
                } else if (now.after(distributionStoreActivity.getStartTime()) && now.before(distributionStoreActivity.getEndTime())) {
                    distributionStoreActivity.setActivityStatus(1);
                } else if (now.after(distributionStoreActivity.getEndTime())) {
                    distributionStoreActivity.setActivityStatus(2);
                }
            });
        }
        return pageVO;
    }

    @Override
    public List<StoreActivityProvinceVO> appProvinceEffect() {
        DistributionStoreActivityQueryDTO queryDTO = new DistributionStoreActivityQueryDTO();
        List<DistributionStoreActivity> distributionStoreActivityList = distributionStoreActivityMapper.listAppEffect(queryDTO);
        if (!CollectionUtils.isEmpty(distributionStoreActivityList)) {
            List<StoreActivityProvinceVO> provinceVOList = distributionStoreActivityList.stream().map(dsa -> {
                StoreActivityProvinceVO storeActivityProvinceVO = new StoreActivityProvinceVO();
                storeActivityProvinceVO.setCode(dsa.getProvinceCode());
                storeActivityProvinceVO.setName(dsa.getProvinceName());
                return storeActivityProvinceVO;
            }).collect(Collectors.toList());
            return provinceVOList.stream().collect(Collectors.collectingAndThen(Collectors.toCollection(() ->
                    new TreeSet<>(Comparator.comparing(t -> t.getCode()))), ArrayList ::new));
        }
       return Collections.emptyList();
    }

    @Override
    public List<DistributionStoreActivityProvinceCountVO> groupByProvince() {
        return distributionStoreActivityMapper.groupByProvince();
    }

    @Override
    public DistributionStoreActivity getById(Long id) {
        DistributionStoreActivity distributionStoreActivity = distributionStoreActivityMapper.getById(id);
        if (Objects.nonNull(distributionStoreActivity)) {
            Date now = new Date();
            if (now.before(distributionStoreActivity.getStartTime())) {
                distributionStoreActivity.setActivityStatus(0);
            } else if (now.after(distributionStoreActivity.getStartTime())
                    && now.before(distributionStoreActivity.getEndTime())) {
                distributionStoreActivity.setActivityStatus(1);
            } else if (now.after(distributionStoreActivity.getEndTime())) {
                distributionStoreActivity.setActivityStatus(2);
            }
        }
        return distributionStoreActivity;
    }

    @Override
    public void save(DistributionStoreActivity distributionStoreActivity) {
        distributionStoreActivityMapper.save(distributionStoreActivity);
    }

    @Override
    public void update(DistributionStoreActivity distributionStoreActivity) {
        if (distributionStoreActivity.getId() == null) {
            throw new LuckException("id不能为空");
        }
        distributionStoreActivityMapper.update(distributionStoreActivity);
    }

    @Override
    public void updateStatus(DistributionStoreActivityUpdateDTO distributionStoreActivityUpdateDTO) {
        if (CollectionUtils.isEmpty(distributionStoreActivityUpdateDTO.getIdList())) {
            throw new LuckException("ID集合不能为空");
        }
        distributionStoreActivityMapper.updateStatusBatch(distributionStoreActivityUpdateDTO);
    }

    @Override
    public List<String> importExcel(MultipartFile file) {
        log.info("导入文件为：{}",file);

        // 1.获取上传文件输入流
        InputStream inputStream = null;
        try {
            inputStream = file.getInputStream();
        } catch (Exception e) {
            log.error("获取输入流异常");
            e.printStackTrace();
        }

        //调用 hutool 方法读取数据 默认调用第一个sheet
        ExcelReader excelReader = ExcelUtil.getReader(inputStream);

        //校验模板的正确性
        List<Object> objects = excelReader.readRow(0);
        log.info("模板表头信息：{}", JSONObject.toJSONString(objects));

        //列名和对象属性名一致
        excelReader.addHeaderAlias("尺码","size");

        //读取为Bean列表，Bean中的字段名为标题，字段值为标题对应的单元格值。
        List<String> sizes = new ArrayList();
        List<DistributionStoreActivitySizes> importObjects = excelReader.readAll(DistributionStoreActivitySizes.class);
        if (importObjects != null) {
            for (int i = 0; i < importObjects.size(); i++) {
                DistributionStoreActivitySizes size = importObjects.get(i);
                if (size != null && StrUtil.isNotBlank(size.getSize())) {
                    sizes.add(size.getSize());
                }
            }
        }
        return sizes;
    }

}
