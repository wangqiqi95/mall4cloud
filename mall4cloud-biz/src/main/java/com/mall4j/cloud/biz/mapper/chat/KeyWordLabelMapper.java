package com.mall4j.cloud.biz.mapper.chat;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mall4j.cloud.biz.model.chat.KeyCustom;
import com.mall4j.cloud.biz.model.chat.KeyWordLabel;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 关键词标签表
 */
@Mapper
public interface KeyWordLabelMapper extends BaseMapper<KeyWordLabel> {

	void save(@Param("param") KeyWordLabel label);

	KeyWordLabel getByName(@Param("param") KeyWordLabel label);

	void batchInsert(@Param("param") List<KeyWordLabel> label);

	List<KeyWordLabel> getByTagId(@Param("ids") List<String> ids);
}
