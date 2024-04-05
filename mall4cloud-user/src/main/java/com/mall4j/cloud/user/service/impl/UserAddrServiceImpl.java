package com.mall4j.cloud.user.service.impl;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.mall4j.cloud.api.delivery.feign.AreaFeignClient;
import com.mall4j.cloud.api.delivery.vo.AreaVO;
import com.mall4j.cloud.common.cache.constant.UserCacheNames;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.user.model.UserAddr;
import com.mall4j.cloud.user.mapper.UserAddrMapper;
import com.mall4j.cloud.user.service.UserAddrService;
import com.mall4j.cloud.common.order.vo.UserAddrVO;
import lombok.extern.java.Log;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 用户地址
 *
 * @author FrozenWatermelon
 * @date 2020-12-07 15:50:02
 */
@Slf4j
@Service
public class UserAddrServiceImpl implements UserAddrService {

    @Autowired
    private UserAddrMapper userAddrMapper;
    @Autowired
    AreaFeignClient areaFeignClient;

    @Override
    public List<UserAddrVO> list(Long userId) {
        return userAddrMapper.list(userId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void save(UserAddr userAddr) {
        log.info("添加用户收货地址，保存参数信息:{}", JSONObject.toJSONString(userAddr));
        /**
         * 使用微信地址，前端传值不带省市区id。这里查询系统中省市区匹配id赋值
         */
        ServerResponseEntity<List<AreaVO>> areaResonse = areaFeignClient.allArea();

        Map<String, AreaVO> provinceMap = new HashMap<>();
        Map<String, AreaVO> cityMap = new HashMap<>();
        Map<String, AreaVO> areaMap = new HashMap<>();
        if (areaResonse != null && areaResonse.isSuccess() && areaResonse.getData().size() > 0) {
            List<AreaVO> areas = areaResonse.getData();
            Map<Integer, List<AreaVO>> allAreaMap = areas.stream().collect(Collectors.groupingBy(AreaVO::getLevel));

            //处理省的列表 去重复，单个名字只保留一个
            List<AreaVO> provinceList = allAreaMap.get(1).stream().collect(Collectors.collectingAndThen(
                    Collectors.toCollection(() -> new TreeSet<>(
                            Comparator.comparing(AreaVO::getAreaName))), ArrayList::new));
            provinceMap = provinceList.stream().collect(Collectors.toMap(AreaVO::getAreaName, p -> p));

            //处理市的列表 去重复，单个名字只保留一个
            List<AreaVO> cityList = allAreaMap.get(2).stream().collect(Collectors.collectingAndThen(
                    Collectors.toCollection(() -> new TreeSet<>(
                            Comparator.comparing(AreaVO::getAreaName))), ArrayList::new));
            cityMap = cityList.stream().collect(Collectors.toMap(AreaVO::getAreaName, p -> p));

            //处理区的列表 去重复，单个名字只保留一个
            List<AreaVO> areaList = allAreaMap.get(3).stream().collect(Collectors.collectingAndThen(
                    Collectors.toCollection(() -> new TreeSet<>(
                            Comparator.comparing(AreaVO::getAreaName))), ArrayList::new));
            areaMap = areaList.stream().collect(Collectors.toMap(AreaVO::getAreaName, p -> p));
        }

        if(userAddr.getProvinceId()==0 && StrUtil.isNotEmpty(userAddr.getProvince())){
            if(provinceMap.get(userAddr.getProvince()) !=null){
                userAddr.setProvinceId(provinceMap.get(userAddr.getProvince()).getAreaId());
            }
        }
        if(userAddr.getCityId()==0 && StrUtil.isNotEmpty(userAddr.getCity())){
            if(cityMap.get(userAddr.getCity()) !=null){
                userAddr.setCityId(cityMap.get(userAddr.getCity()).getAreaId());
            }
        }
        if(userAddr.getAreaId()==0 && StrUtil.isNotEmpty(userAddr.getArea())){
            if(areaMap.get(userAddr.getArea()) !=null){
                userAddr.setAreaId(areaMap.get(userAddr.getArea()).getAreaId());
            }
        }
        log.info("添加用户收货地址，替换省市区id后保存参数信息:{}", JSONObject.toJSONString(userAddr));
        if (userAddr.getIsDefault().equals(UserAddr.DEFAULT_ADDR)) {
            userAddrMapper.removeDefaultUserAddr(userAddr.getUserId());
        }
        userAddrMapper.save(userAddr);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(UserAddr userAddr) {
        log.info("更新用户收货地址，保存参数信息:{}", JSONObject.toJSONString(userAddr));
        /**
         * 使用微信地址，前端传值不带省市区id。这里查询系统中省市区匹配id赋值
         */
        ServerResponseEntity<List<AreaVO>> areaResonse = areaFeignClient.allArea();

        Map<String, AreaVO> provinceMap = new HashMap<>();
        Map<String, AreaVO> cityMap = new HashMap<>();
        Map<String, AreaVO> areaMap = new HashMap<>();
        if (areaResonse != null && areaResonse.isSuccess() && areaResonse.getData().size() > 0) {
            List<AreaVO> areas = areaResonse.getData();
            Map<Integer, List<AreaVO>> allAreaMap = areas.stream().collect(Collectors.groupingBy(AreaVO::getLevel));

            //处理省的列表 去重复，单个名字只保留一个
            List<AreaVO> provinceList = allAreaMap.get(1).stream().collect(Collectors.collectingAndThen(
                    Collectors.toCollection(() -> new TreeSet<>(
                            Comparator.comparing(AreaVO::getAreaName))), ArrayList::new));
            provinceMap = provinceList.stream().collect(Collectors.toMap(AreaVO::getAreaName, p -> p));

            //处理市的列表 去重复，单个名字只保留一个
            List<AreaVO> cityList = allAreaMap.get(2).stream().collect(Collectors.collectingAndThen(
                    Collectors.toCollection(() -> new TreeSet<>(
                            Comparator.comparing(AreaVO::getAreaName))), ArrayList::new));
            cityMap = cityList.stream().collect(Collectors.toMap(AreaVO::getAreaName, p -> p));

            //处理区的列表 去重复，单个名字只保留一个
            List<AreaVO> areaList = allAreaMap.get(3).stream().collect(Collectors.collectingAndThen(
                    Collectors.toCollection(() -> new TreeSet<>(
                            Comparator.comparing(AreaVO::getAreaName))), ArrayList::new));
            areaMap = areaList.stream().collect(Collectors.toMap(AreaVO::getAreaName, p -> p));
        }

        if(userAddr.getProvinceId()==0 && StrUtil.isNotEmpty(userAddr.getProvince())){
            if(provinceMap.get(userAddr.getProvince()) !=null){
                userAddr.setProvinceId(provinceMap.get(userAddr.getProvince()).getAreaId());
            }
        }
        if(userAddr.getCityId()==0 && StrUtil.isNotEmpty(userAddr.getCity())){
            if(cityMap.get(userAddr.getCity()) !=null){
                userAddr.setCityId(cityMap.get(userAddr.getCity()).getAreaId());
            }
        }
        if(userAddr.getAreaId()==0 && StrUtil.isNotEmpty(userAddr.getArea())){
            if(areaMap.get(userAddr.getArea()) !=null){
                userAddr.setAreaId(areaMap.get(userAddr.getArea()).getAreaId());
            }
        }
        log.info("更新用户收货地址，替换省市区id后保存参数信息:{}", JSONObject.toJSONString(userAddr));
        if (userAddr.getIsDefault().equals(UserAddr.DEFAULT_ADDR)) {
            userAddrMapper.removeDefaultUserAddr(userAddr.getUserId());
        }
        userAddrMapper.update(userAddr);
    }

    @Override
    public void deleteUserAddrByUserId(Long addrId, Long userId) {
        userAddrMapper.deleteById(addrId,userId);
    }

    @Override
    public UserAddrVO getUserAddrByUserId(Long addrId, Long userId) {
        // 获取用户默认地址
        if (addrId == 0) {
            return userAddrMapper.getUserDefaultAddrByUserId(userId);
        }
        return userAddrMapper.getByAddrId(addrId,userId);
    }

    @Override
    public int countByUserId(Long userId) {
        return userAddrMapper.countByUserId(userId);
    }

    @Override
    @CacheEvict(cacheNames = UserCacheNames.USER_DEFAULT_ADDR, key = "#userId")
    public void removeUserDefaultAddrCacheByUserId(Long userId) {

    }

}
