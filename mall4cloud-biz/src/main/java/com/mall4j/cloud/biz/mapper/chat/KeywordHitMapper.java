package com.mall4j.cloud.biz.mapper.chat;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mall4j.cloud.biz.dto.chat.KeywordHitDTO;
import com.mall4j.cloud.biz.dto.chat.KeywordHitRecomdDTO;
import com.mall4j.cloud.biz.model.chat.KeyWordHit;
import com.mall4j.cloud.biz.model.chat.Keyword;
import com.mall4j.cloud.biz.vo.chat.KeywordHitRecomdVO;
import com.mall4j.cloud.biz.vo.chat.KeywordHitVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;


@Mapper
public interface KeywordHitMapper extends BaseMapper<KeyWordHit> {
	
	List<KeywordHitVO> list(@Param("param") KeywordHitDTO param);

	void save(@Param("wordHit") KeyWordHit wordHit);

	void batchInsert(@Param("wordHit") List<KeyWordHit> wordHit);

	List<KeywordHitVO> getTop(@Param("param") KeywordHitDTO param);

	List<KeywordHitRecomdVO> selectAppRecomds(@Param("param") KeywordHitRecomdDTO param);
}
