package com.mall4j.cloud.biz.mapper.chat;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mall4j.cloud.biz.model.chat.KeyCustom;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 关键词敏感词性质表
 */
@Mapper
public interface KeyCustomMapper extends BaseMapper<KeyCustom> {

	void save(@Param("param") KeyCustom custom);

	KeyCustom getByName(@Param("param") KeyCustom custom);

	List<KeyCustom> getByIds(@Param("ids") List<Long> ids);
}
