package com.mall4j.cloud.product.service.impl;

import cn.hutool.crypto.digest.DigestUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.mall4j.cloud.api.platform.feign.ConfigFeignClient;
import com.mall4j.cloud.common.cache.constant.ProductCacheNames;
import com.mall4j.cloud.common.cache.util.RedisUtil;
import com.mall4j.cloud.common.exception.LuckException;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.common.product.dto.SpuFilterPropertiesDTO;
import com.mall4j.cloud.product.dto.AttrFilterDto;
import com.mall4j.cloud.product.mapper.AttrPropertiesMapper;
import com.mall4j.cloud.product.model.AttrProperties;
import com.mall4j.cloud.product.service.AttrFilterConfigService;
import com.mall4j.cloud.product.service.AttrService;
import com.mall4j.cloud.product.vo.AppAttrVo;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.compress.utils.Lists;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @Description
 * @Author axin
 * @Date 2023-06-12
 **/
@Service
public class AttrFilterConfigServiceImpl implements AttrFilterConfigService {
    @Autowired
    private ConfigFeignClient configFeignClient;

    @Autowired
    private AttrService attrService;

    @Autowired
    private AttrPropertiesMapper attrPropertiesMapper;

    private static String SPU_FILTER_PROPERTIES="SPU_FILTER_PROPERTIES";

    @Override
    public SpuFilterPropertiesDTO getFilterProperties() {
        ServerResponseEntity<String> responseEntity = configFeignClient.getConfig(SPU_FILTER_PROPERTIES);
        return JSON.parseObject(responseEntity.getData(), SpuFilterPropertiesDTO.class);
    }

    @Override
    public List<AppAttrVo> getFilterPropertiesByAttrValue(AttrFilterDto dto) {
        String redisKey = ProductCacheNames.FILTER_PROPERTIES_ATTR+ DigestUtil.md5Hex(JSON.toJSONString(dto));
        if(RedisUtil.hasKey(redisKey)){
            return RedisUtil.get(redisKey);
        }
        List<AppAttrVo> appAttrVos= null;
        if(CollectionUtils.isNotEmpty(dto.getStyleTypes()) || CollectionUtils.isNotEmpty(dto.getSexs())) {
            List<Long> attrIds = attrPropertiesMapper.selectByTypeAndSex(dto);
            if(CollectionUtils.isEmpty(attrIds)){
                return null;
            }
            appAttrVos = attrService.listAppAttrValues(attrIds);
        }else{
            appAttrVos = attrService.listAppAttrValues(null);
        }
        RedisUtil.set(redisKey,appAttrVos,60*30);
        return appAttrVos;
    }

    @Override
    @Transactional
    public void save(SpuFilterPropertiesDTO dto) {
        //校验是否大于 5个默认属性
        if(dto.getDefaultProp()){
            Integer count = attrPropertiesMapper.selectCount(Wrappers.lambdaQuery(AttrProperties.class).eq(AttrProperties::getDefaultProp, true));
            if(count>=5){
                throw new LuckException("默认属性最多设置5个");
            }
        }
        attrPropertiesMapper.delete(Wrappers.lambdaQuery(AttrProperties.class).eq(AttrProperties::getAttrId,dto.getAttrId()));
        AttrProperties attrProperties = new AttrProperties();
        attrProperties.setDefaultProp(dto.getDefaultProp());
        attrProperties.setAttrId(dto.getAttrId());
        attrProperties.setVague(dto.getVague());
        attrProperties.setType(JSON.toJSONString(dto.getSpuStyleTypes()));
        attrProperties.setSex(JSON.toJSONString(dto.getSexMappings()));
        attrPropertiesMapper.insert(attrProperties);
    }

    @Override
    public void updateDefaultProp(Long attrId,Boolean defaultProp) {
        //校验是否大于 5个默认属性
        Integer count = attrPropertiesMapper.selectCount(Wrappers.lambdaQuery(AttrProperties.class).eq(AttrProperties::getDefaultProp, true));
        if(count>=5 && defaultProp){
            throw new LuckException("默认属性最多设置5个");
        }
        AttrProperties attrProperties = new AttrProperties();
        attrProperties.setDefaultProp(defaultProp);
        attrPropertiesMapper.update(attrProperties,Wrappers.lambdaUpdate(AttrProperties.class).eq(AttrProperties::getAttrId, attrId));

    }

    @Override
    public SpuFilterPropertiesDTO getByAttrId(Long attrId) {
        AttrProperties attrProperties = attrPropertiesMapper.selectOne(Wrappers.lambdaQuery(AttrProperties.class).eq(AttrProperties::getAttrId, attrId));
        SpuFilterPropertiesDTO dto = getSpuFilterPropertiesDTO(attrProperties);
        return dto;
    }

    @Override
    public List<SpuFilterPropertiesDTO> getByAttrIds(List<Long> attrId) {
        List<SpuFilterPropertiesDTO> dtos = Lists.newArrayList();
        if(CollectionUtils.isNotEmpty(attrId)) {
            List<AttrProperties> attrProperties = attrPropertiesMapper.selectList(Wrappers.lambdaQuery(AttrProperties.class).in(AttrProperties::getAttrId, attrId));
            if(CollectionUtils.isNotEmpty(attrProperties)) {
                for (AttrProperties attrProperty : attrProperties) {
                    SpuFilterPropertiesDTO dto = getSpuFilterPropertiesDTO(attrProperty);
                    dtos.add(dto);
                }
            }
        }
        return dtos;
    }



    @Override
    public void deleteByAttrId(Long attrId) {
        attrPropertiesMapper.delete(Wrappers.lambdaQuery(AttrProperties.class).eq(AttrProperties::getAttrId,attrId));
    }

    @Override
    @CacheEvict(value = ProductCacheNames.ATTR_FILTER_SPU_ID,allEntries = true)
    public void removeCacheAttrFilter(){}

    private SpuFilterPropertiesDTO getSpuFilterPropertiesDTO(AttrProperties attrProperty) {
        if(Objects.isNull(attrProperty)) {
            return null;
        }
        SpuFilterPropertiesDTO dto = new SpuFilterPropertiesDTO();
        dto.setSpuStyleTypes(Objects.nonNull(attrProperty.getType()) ? JSON.parseArray(attrProperty.getType(), SpuFilterPropertiesDTO.AttrMappingDTO.class) : null);
        dto.setSexMappings(Objects.nonNull(attrProperty.getSex()) ? JSON.parseArray(attrProperty.getSex(), SpuFilterPropertiesDTO.AttrMappingDTO.class) : null);
        dto.setDefaultProp(attrProperty.getDefaultProp());
        dto.setVague(attrProperty.getVague());
        dto.setAttrId(attrProperty.getAttrId());
        return dto;
    }
}
