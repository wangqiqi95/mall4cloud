package com.mall4j.cloud.user.service.dictionary;


import com.baomidou.mybatisplus.extension.service.IService;
import com.mall4j.cloud.common.constant.DictionaryEnum;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.user.model.dictionary.Dictionary;
import com.mall4j.cloud.user.vo.dictionary.DictionaryVO;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author ben
 * @since 2022-11-07
 */
public interface DictionaryService extends IService<Dictionary> {

    ServerResponseEntity<List<DictionaryVO>> getDictionaryVOList(DictionaryEnum dictionaryEnum);

}
