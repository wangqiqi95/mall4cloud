package com.mall4j.cloud.product.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import com.mall4j.cloud.common.constant.Constant;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.util.PageUtil;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.exception.LuckException;
import com.mall4j.cloud.common.i18n.LanguageEnum;
import com.mall4j.cloud.common.product.vo.CategoryLangVO;
import com.mall4j.cloud.common.product.vo.CategoryVO;
import com.mall4j.cloud.common.response.ResponseEnum;
import com.mall4j.cloud.product.dto.CategoryDTO;
import com.mall4j.cloud.product.dto.CategoryLangDTO;
import com.mall4j.cloud.product.model.CategoryLang;
import com.mall4j.cloud.product.mapper.CategoryLangMapper;
import com.mall4j.cloud.product.service.CategoryLangService;
import ma.glasnost.orika.MapperFacade;
import org.springframework.stereotype.Service;

import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 分类-国际化表
 *
 * @author YXF
 * @date 2021-04-22 17:48:16
 */
@Service
public class CategoryLangServiceImpl implements CategoryLangService {

    @Autowired
    private CategoryLangMapper categoryLangMapper;
    @Autowired
    private MapperFacade mapperFacade;

    @Override
    public PageVO<CategoryLang> page(PageDTO pageDTO) {
        return PageUtil.doPage(pageDTO, () -> categoryLangMapper.list());
    }

    @Override
    public CategoryLang getByCategoryId(Long categoryId) {
        return categoryLangMapper.getByCategoryId(categoryId);
    }

    @Override
    public void save(List<CategoryLangDTO> categoryLangList, Long categoryId) {
        if (CollUtil.isEmpty(categoryLangList)) {
            return;
        }
        boolean hasDefaultLang = false;
        Iterator<CategoryLangDTO> iterator = categoryLangList.iterator();
        while (iterator.hasNext()) {
            CategoryLangDTO categoryLangDTO = iterator.next();
            if (StrUtil.isBlank(categoryLangDTO.getName())) {
                iterator.remove();
                continue;
            }
            if (Objects.equals(categoryLangDTO.getLang(), Constant.DEFAULT_LANG)) {
                hasDefaultLang = true;
            }
            categoryLangDTO.setCategoryId(categoryId);
        }
        if (CollUtil.isEmpty(categoryLangList) || !hasDefaultLang) {
            throw new LuckException(ResponseEnum.DATA_ERROR);
        }
        categoryLangMapper.batchSave(mapperFacade.mapAsList(categoryLangList, CategoryLang.class));
    }

    @Override
    public void update(List<CategoryLangDTO> categoryLangList, CategoryVO categoryDb) {
        List<Integer> langIds = categoryDb.getCategoryLangList().stream().map(CategoryLangVO::getLang).collect(Collectors.toList());
        List<CategoryLang> saveList = new ArrayList<>();
        List<CategoryLang> updateList = new ArrayList<>();
        boolean hasDefaultLang = false;
        for (CategoryLangDTO categoryLang : categoryLangList) {
            categoryLang.setCategoryId(categoryDb.getCategoryId());
            // 更新-已有语言信息，且分类名不为空
            if (langIds.contains(categoryLang.getLang()) && StrUtil.isNotBlank(categoryLang.getName())) {
                updateList.add(mapperFacade.map(categoryLang, CategoryLang.class));
                langIds.remove(categoryLang.getLang());
                if (Objects.equals(categoryLang.getLang(), Constant.DEFAULT_LANG)) {
                    hasDefaultLang = true;
                }
                continue;
            }
            // 删除-已有语言信息，但分类名为空
            else if (langIds.contains(categoryLang.getLang())) {
                continue;
            }
            // 新增-不存在的语言信息
            saveList.add(mapperFacade.map(categoryLang, CategoryLang.class));
        }
        // 没有默认语言-数据异常
        if (!hasDefaultLang) {
            throw new LuckException(ResponseEnum.DATA_ERROR);
        }
        if (CollUtil.isNotEmpty(saveList)) {
            categoryLangMapper.batchSave(saveList);
        }
        if (CollUtil.isNotEmpty(updateList)) {
            categoryLangMapper.batchUpdate(updateList);
        }
        if (CollUtil.isNotEmpty(langIds)) {
            categoryLangMapper.batchDelete(langIds, categoryDb.getCategoryId());
        }
    }

    @Override
    public void deleteById(Long categoryId) {
        categoryLangMapper.deleteById(categoryId);
    }

    @Override
    public void batchSave(List<CategoryLang> list) {
        categoryLangMapper.batchSave(list);
    }

    @Override
    public void existCategoryName(CategoryDTO categoryDTO) {
        List<String> names = categoryLangMapper.existCategoryName(categoryDTO);
        if (CollUtil.isNotEmpty(names)) {
            throw new LuckException("分类名" + names.toString() + "已存在，请重新输入");
        }
    }
}
