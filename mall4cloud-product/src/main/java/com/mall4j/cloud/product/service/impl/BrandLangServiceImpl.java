package com.mall4j.cloud.product.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.mall4j.cloud.common.constant.Constant;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.util.PageUtil;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.exception.LuckException;
import com.mall4j.cloud.common.product.vo.CategoryLangVO;
import com.mall4j.cloud.common.response.ResponseEnum;
import com.mall4j.cloud.product.model.BrandLang;
import com.mall4j.cloud.product.mapper.BrandLangMapper;
import com.mall4j.cloud.product.model.CategoryLang;
import com.mall4j.cloud.product.service.BrandLangService;
import org.springframework.stereotype.Service;

import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 品牌-国际化表
 *
 * @author YXF
 * @date 2021-04-26 15:17:37
 */
@Service
public class BrandLangServiceImpl implements BrandLangService {

    @Autowired
    private BrandLangMapper brandLangMapper;

    @Override
    public PageVO<BrandLang> page(PageDTO pageDTO) {
        return PageUtil.doPage(pageDTO, () -> brandLangMapper.list());
    }

    @Override
    public BrandLang getByBrandId(Long brandId) {
        return brandLangMapper.getByBrandId(brandId);
    }

    @Override
    public void save(List<BrandLang> brandLangList, Long brandId) {
        if (CollUtil.isEmpty(brandLangList)) {
            return;
        }
        saveOrUpdate(brandLangList, brandId, null);
    }

    @Override
    public void update(List<BrandLang> brandLangList, Long brandId) {
        List<Integer> langIds = brandLangMapper.langIdsByBrandId(brandId);
        saveOrUpdate(brandLangList, brandId, langIds);
    }

    /**
     * 处理品牌语言列表的保存或更新信息
     * @param brandLangList 语言信息列表
     * @param brandId 品牌id
     * @param langIds 语言id列表
     */
    private void saveOrUpdate(List<BrandLang> brandLangList, Long brandId, List<Integer> langIds) {
        // 没有默认语言-数据异常
        long count = brandLangList.stream().filter(brandLang -> Objects.equals(brandLang.getLang(), Constant.DEFAULT_LANG)).count();
        if (count == 0) {
            throw new LuckException(ResponseEnum.DATA_ERROR);
        }
        // 空指针处理
        if (Objects.isNull(langIds)) {
            langIds = new ArrayList<>();
        }
        List<BrandLang> saveList = new ArrayList<>();
        Iterator<BrandLang> iterator = brandLangList.iterator();
        while (iterator.hasNext()) {
            BrandLang brandLang = iterator.next();
            if (Objects.equals(brandLang.getLang(), Constant.DEFAULT_LANG)) {
                if (StrUtil.isBlank(brandLang.getName())) {
                    throw new LuckException("品牌中文名称不能为为空");
                }
            }
            brandLang.setBrandId(brandId);
            // 删除-名称与描述都为空
            if (StrUtil.isBlank(brandLang.getName()) && StrUtil.isBlank(brandLang.getDesc())) {
                iterator.remove();
                continue;
            }
            // 修改-已有语言信息，但品牌名称未null
            else if (langIds.contains(brandLang.getLang())) {
                langIds.remove(brandLang.getLang());
                continue;
            }
            // 新增-不存在语言信息
            saveList.add(brandLang);
        }
        if (CollUtil.isNotEmpty(brandLangList)) {
            brandLangMapper.batchUpdate(brandLangList);
        }
        if (CollUtil.isNotEmpty(saveList)) {
            brandLangMapper.batchSave(saveList);
        }
        if (CollUtil.isNotEmpty(langIds)) {
            brandLangMapper.batchDelete(langIds, brandId);
        }
    }

    @Override
    public void deleteById(Long brandId) {
        brandLangMapper.deleteById(brandId);
    }

    @Override
    public List<BrandLang> listByBrandNames(Set<String> brandNames, Long shopId) {
        return brandLangMapper.listByBrandNames(brandNames, shopId);
    }
}
