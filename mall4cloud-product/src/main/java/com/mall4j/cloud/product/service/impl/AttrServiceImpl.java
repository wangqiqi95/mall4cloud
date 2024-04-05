package com.mall4j.cloud.product.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import com.mall4j.cloud.api.product.util.ProductLangUtil;
import com.mall4j.cloud.common.cache.constant.CacheNames;
import com.mall4j.cloud.common.cache.util.RedisUtil;
import com.mall4j.cloud.common.constant.Constant;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.util.PageAdapter;
import com.mall4j.cloud.common.database.util.PageUtil;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.exception.LuckException;
import com.mall4j.cloud.common.product.constant.AttrType;
import com.mall4j.cloud.common.product.dto.AttrLangDTO;
import com.mall4j.cloud.common.product.vo.AttrVO;
import com.mall4j.cloud.common.product.vo.CategoryVO;
import com.mall4j.cloud.common.response.ResponseEnum;
import com.mall4j.cloud.common.security.AuthUserContext;
import com.mall4j.cloud.product.dto.AttrDTO;
import com.mall4j.cloud.common.product.dto.SpuFilterPropertiesDTO;
import com.mall4j.cloud.product.mapper.AttrMapper;
import com.mall4j.cloud.product.model.Attr;
import com.mall4j.cloud.product.model.AttrLang;
import com.mall4j.cloud.product.model.AttrValue;
import com.mall4j.cloud.product.service.*;
import com.mall4j.cloud.product.vo.AppAttrValuesVo;
import com.mall4j.cloud.product.vo.AppAttrVo;
import lombok.extern.slf4j.Slf4j;
import ma.glasnost.orika.MapperFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 属性信息
 *
 * @author FrozenWatermelon
 * @date 2020-10-28 15:27:23
 */
@Slf4j
@Service
public class AttrServiceImpl extends ServiceImpl<AttrMapper,Attr> implements AttrService {

    @Autowired
    private AttrMapper attrMapper;
    @Autowired
    private AttrLangService attrLangService;
    @Autowired
    private AttrValueLangService attrValueLangService;
    @Autowired
    private AttrCategoryService attrCategoryService;
    @Autowired
    private AttrValueService attrValueService;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private SpuAttrValueService spuAttrValueService;
    @Autowired
    private AttrFilterConfigService attrFilterConfigService;
    @Autowired
    private MapperFacade mapperFacade;

    @Override
    public PageVO<AttrVO> page(PageDTO pageDTO, AttrDTO attrDTO) {
        PageVO<AttrVO> pageVO = new PageVO<>();
        attrDTO.setShopId(AuthUserContext.get().getTenantId());
        pageVO.setList(attrMapper.listAttr(new PageAdapter(pageDTO), attrDTO));
        ProductLangUtil.attrList(pageVO.getList());
        pageVO.setTotal(attrMapper.countAttr(attrDTO));
        pageVO.setPages(PageUtil.getPages(pageVO.getTotal(), pageDTO.getPageSize()));
        for (AttrVO attrVO : pageVO.getList()) {
            if(Objects.equals(attrVO.getAttrType(), AttrType.SALES.value())){
                attrVO.setSpuFilterProperties(attrFilterConfigService.getByAttrId(attrVO.getAttrId()));
            }
        }
        return pageVO;
    }

    @Override
    public AttrVO getByAttrId(Long attrId) {
        AttrVO attrVO = attrMapper.getByAttrId(attrId);
        if (Objects.isNull(attrVO)) {
            throw new LuckException("属性不存在或已删除");
        }
        if (Objects.equals(attrVO.getAttrType(), AttrType.BASIC.value())) {
            attrVO.setCategories(attrCategoryService.listByAttrId(attrId));
            categoryService.getPathNames(attrVO.getCategories());
        }

        if(Objects.equals(attrVO.getAttrType(), AttrType.SALES.value())){
            attrVO.setSpuFilterProperties(attrFilterConfigService.getByAttrId(attrVO.getAttrId()));
        }

        return attrVO;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void save(AttrDTO attrDTO) {
        Attr attr = mapperFacade.map(attrDTO, Attr.class);
        attr.setAttrValues(mapperFacade.mapAsList(attrDTO.getAttrValues(), AttrValue.class));
        attr.setShopId(AuthUserContext.get().getTenantId());

        if (Objects.equals(AttrType.SALES.value(), attr.getAttrType()) && CollectionUtil.isNotEmpty(attrDTO.getAttrLangList())) {
            hasNameRepeated(attrDTO.getAttrLangList(), attr.getShopId(), null);
        }
        if(StrUtil.isBlank(attr.getName()) ){
            if(StrUtil.isNotBlank(attrDTO.getName())){
                attr.setName(attrDTO.getName());
            }
            if(StrUtil.isNotBlank(attrDTO.getNameZh())){
                attr.setName(attrDTO.getNameZh());
            }
        }
        attrMapper.saveAttr(attr);

        //保存搜索配置信息
        if(Objects.equals(AttrType.SALES.value(), attr.getAttrType()) && Objects.nonNull(attrDTO.getSpuFilterProperties())){
            SpuFilterPropertiesDTO spuFilterPropertiesDTO = attrDTO.getSpuFilterProperties();
            spuFilterPropertiesDTO.setAttrId(attr.getAttrId());
            attrFilterConfigService.save(spuFilterPropertiesDTO);
        }

        // 属性语言
        if(CollectionUtil.isNotEmpty(attrDTO.getAttrLangList())){
            checkAttrLangData(attrDTO.getAttrLangList());
            attrLangService.save(mapperFacade.mapAsList(attrDTO.getAttrLangList(), AttrLang.class), attr.getAttrId());
        }

        // 保存属性值
        attrValueService.save(attrDTO.getAttrValues(), attr.getAttrId());
        // 基本属性关联分类
        if (Objects.equals(AttrType.BASIC.value(), attr.getAttrType())) {
            // 保存属性分类关联信息
            attrCategoryService.save(attr.getAttrId(), attrDTO.getCategoryIds());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(AttrDTO attrDTO) {
        Attr attr = mapperFacade.map(attrDTO, Attr.class);
        AttrVO dbAttr = attrMapper.getByAttrId(attr.getAttrId());
        if(CollectionUtil.isNotEmpty(attrDTO.getAttrLangList())){
            if (Objects.equals(AttrType.SALES.value(), attr.getAttrType())) {
                hasNameRepeated(attrDTO.getAttrLangList(), attr.getShopId(), attr.getAttrId());
            }
            checkAttrLangData(attrDTO.getAttrLangList());
        }

        if(StrUtil.isBlank(attr.getName()) ){
            if(StrUtil.isNotBlank(attrDTO.getName())){
                attr.setName(attrDTO.getName());
            }
            if(StrUtil.isNotBlank(attrDTO.getNameZh())){
                attr.setName(attrDTO.getNameZh());
            }
        }

        // 更新属性信息
        attrMapper.updateAttr(attr);

        //更新搜索配置信息
        if(Objects.equals(AttrType.SALES.value(), attr.getAttrType()) && Objects.nonNull(attrDTO.getSpuFilterProperties())){
            SpuFilterPropertiesDTO spuFilterPropertiesDTO = attrDTO.getSpuFilterProperties();
            spuFilterPropertiesDTO.setAttrId(attr.getAttrId());
            attrFilterConfigService.save(spuFilterPropertiesDTO);
        }

        // 更新属性语言表
        if(CollectionUtil.isNotEmpty(attrDTO.getAttrLangList())){
            for (AttrLangDTO name : attrDTO.getAttrLangList()) {
                name.setAttrId(attr.getAttrId());
            }
            attrLangService.update(mapperFacade.mapAsList(attrDTO.getAttrLangList(), AttrLang.class), dbAttr);
        }
        // 更新属性值
        attrValueService.update(attrDTO, dbAttr);
        // 更新属性分类关联信息
        if (Objects.equals(dbAttr.getAttrType(), AttrType.BASIC.value())) {
            List<Long> ids = attrCategoryService.update(attr.getAttrId(), attrDTO.getCategoryIds());
            // 清除取消关联的分类的数据
            spuAttrValueService.deleteByCategoryIds(attr.getAttrId(), ids);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteById(Long attrId) {
        attrValueService.deleteByAttrId(attrId);
        attrLangService.deleteById(attrId);
        attrMapper.deleteById(attrId);
        attrFilterConfigService.deleteByAttrId(attrId);
    }

    @Override
    @Cacheable(cacheNames = CacheNames.ATTRS_BY_CATEGORY_KEY, key = "#categoryId")
    public List<AttrVO> getAttrsByCategoryIdAndAttrType(Long categoryId) {
        return attrMapper.getAttrsByCategoryIdAndAttrType(AttrType.BASIC.value(), categoryId, null);
    }

    @Override
    public List<Long> getAttrOfCategoryIdByAttrId(Long attrId) {
        AttrVO attr = attrMapper.getByAttrId(attrId);
        if (Objects.isNull(attr)) {
            throw new LuckException("属性不存在");
        }
        if (CollUtil.isEmpty(attr.getCategories())) {
            return new ArrayList<>();
        }
        return attr.getCategories().stream().map(CategoryVO::getCategoryId).collect(Collectors.toList());
    }

    @Override
    public void removeAttrByCategoryId(List<Long> categoryIds) {
        if (CollUtil.isEmpty(categoryIds)) {
            return;
        }
        List<String> keys = new ArrayList<>();
        for (Long categoryId : categoryIds) {
            keys.add(CacheNames.ATTRS_BY_CATEGORY_KEY + CacheNames.UNION + categoryId);
        }
        RedisUtil.deleteBatch(keys);
    }

    @Override
    public List<AttrVO> getShopAttrs(Long shopId) {
        return attrMapper.getAttrsByCategoryIdAndAttrType(AttrType.SALES.value(), null, shopId);
    }

    @Override
    public List<AppAttrVo> listAppAttrValues(List<Long> attrIdsReq) {
        List<AppAttrVo> appAttrVos=new ArrayList<>();
        List<AppAttrValuesVo> valuesVos=attrMapper.listAppAttrValues(attrIdsReq);
        if(CollectionUtil.isNotEmpty(valuesVos)){
            Map<String,List<AppAttrValuesVo>> valuesMap=valuesVos.stream().collect(Collectors.groupingBy(AppAttrValuesVo::getAttrName, LinkedHashMap::new, Collectors.toList()));
            List<Long> attrIds = valuesVos.stream().map(AppAttrValuesVo::getAttrId).collect(Collectors.toList());
            List<SpuFilterPropertiesDTO> spuFilterPropertiesDTOS = attrFilterConfigService.getByAttrIds(attrIds);

            Map<Long, SpuFilterPropertiesDTO> spuFilterPropertiesDTOMap = Optional.ofNullable(spuFilterPropertiesDTOS).orElse(Lists.newArrayList())
                    .stream().collect(Collectors.toMap(SpuFilterPropertiesDTO::getAttrId, Function.identity(), (k1, k2) -> k2));
            for (Map.Entry<String,List<AppAttrValuesVo>> entry : valuesMap.entrySet()) {
                AppAttrVo appAttrVo=new AppAttrVo();
                appAttrVo.setAttrName(entry.getKey());
                appAttrVo.setValuesList(entry.getValue());
                Collections.sort(appAttrVo.getValuesList(), new Comparator<AppAttrValuesVo>(){
                    @Override
                    public int compare(AppAttrValuesVo o1, AppAttrValuesVo o2){
                        try {
                            return new Double(o1.getAttrValueName()).compareTo(new Double(o2.getAttrValueName()));
                        }catch (Exception e){
                        }
                        return o1.getAttrValueId().compareTo(o2.getAttrValueId());
                    }
                });

                if(CollectionUtil.isNotEmpty(appAttrVo.getValuesList())) {
                    AppAttrValuesVo appAttrValuesVo = appAttrVo.getValuesList().get(0);
                    appAttrVo.setSpuFilterProperties(spuFilterPropertiesDTOMap.get(appAttrValuesVo.getAttrId()));
                }
                appAttrVos.add(appAttrVo);
            }
        }
        return appAttrVos;
    }

    /**
     * 判断属性名是否已存在
     * @param attrLangList
     * @param shopId
     * @param attrId
     */
    private void hasNameRepeated(List<AttrLangDTO> attrLangList, Long shopId, Long attrId) {
        List<AttrLang> dbAttrLangList = attrMapper.countAttrName(attrLangList, shopId, attrId);
        if (CollUtil.isEmpty(dbAttrLangList)) {
            return;
        }
        Set<String> nameSet = dbAttrLangList.stream().map(AttrLang::getName).collect(Collectors.toSet());
        throw new LuckException("属性名" + nameSet + "已存在");
    }

    /**
     * 检验属性国际化信息是否正确
     * @param attrLangList 属性国际化信息
     */
    private void checkAttrLangData(List<AttrLangDTO> attrLangList) {
        if (CollUtil.isEmpty(attrLangList)) {
            throw new LuckException("属性名不能为空");
        }
        boolean correct = false;
        Iterator<AttrLangDTO> iterator = attrLangList.iterator();
        while (iterator.hasNext()) {
            AttrLangDTO attrLangDTO = iterator.next();
            if (StrUtil.isBlank(attrLangDTO.getName())) {
                iterator.remove();
                continue;
            }
            if (Objects.equals(attrLangDTO.getLang(), Constant.DEFAULT_LANG)) {
                correct = true;
            }
        }
        if (CollUtil.isEmpty(attrLangList) || !correct) {
            throw new LuckException(ResponseEnum.DATA_ERROR);
        }
    }
}
