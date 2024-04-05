package com.mall4j.cloud.user.service.impl.dictionary;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mall4j.cloud.common.constant.DictionaryEnum;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.user.mapper.dictionary.DictionaryMapper;
import com.mall4j.cloud.user.model.dictionary.Dictionary;
import com.mall4j.cloud.user.service.dictionary.DictionaryService;
import com.mall4j.cloud.user.vo.dictionary.DictionaryVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author ben
 * @since 2022-11-07
 */
@Service
public class DictionaryServiceImpl extends ServiceImpl<DictionaryMapper, Dictionary> implements DictionaryService {

    @Autowired
    private DictionaryMapper dictionaryMapper;

    @Override
    public ServerResponseEntity<List<DictionaryVO>> getDictionaryVOList(DictionaryEnum dictionaryEnum) {
        List<Dictionary> dictionaryList = dictionaryMapper.selectList(
                new LambdaQueryWrapper<Dictionary>()
                        .eq(Dictionary::getDictionaryType, dictionaryEnum)
        );

        List<DictionaryVO> dictionaryVOList = dictionaryList.stream().map(dictionary -> {
            DictionaryVO dictionaryVO = new DictionaryVO();
            BeanUtils.copyProperties(dictionary, dictionaryVO);
            return dictionaryVO;
        }).collect(Collectors.toList());
        return ServerResponseEntity.success(dictionaryVOList);
    }
}
