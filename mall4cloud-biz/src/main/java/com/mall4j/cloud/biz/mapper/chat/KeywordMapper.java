package com.mall4j.cloud.biz.mapper.chat;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mall4j.cloud.biz.dto.chat.KeywordDTO;
import com.mall4j.cloud.biz.dto.chat.SessionTimeOutDTO;
import com.mall4j.cloud.biz.model.chat.Keyword;
import com.mall4j.cloud.biz.model.chat.SessionTimeOut;
import com.mall4j.cloud.biz.vo.chat.KeywordVO;
import com.mall4j.cloud.biz.vo.chat.SessionTimeOutVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;


@Mapper
public interface KeywordMapper extends BaseMapper<Keyword> {
	
	List<KeywordVO> list(@Param("param") KeywordDTO param);

	void save(@Param("keyword") Keyword keyword);

	void update(@Param("keyword") Keyword keyword);

	void deleteById(@Param("id") Long id);

	KeywordVO getById(@Param("id") Long id);

	List<KeywordVO> getAll();
}
