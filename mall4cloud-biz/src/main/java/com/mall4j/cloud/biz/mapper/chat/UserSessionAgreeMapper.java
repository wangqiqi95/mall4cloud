package com.mall4j.cloud.biz.mapper.chat;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mall4j.cloud.biz.dto.chat.SessionAgreeDTO;
import com.mall4j.cloud.biz.model.chat.AgreeInfo;
import com.mall4j.cloud.biz.vo.chat.SessionAgreeVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;


@Mapper
public interface UserSessionAgreeMapper extends BaseMapper<AgreeInfo> {

	void batchInsert(@Param("agreeInfo") List<AgreeInfo> wordHit);

	List<SessionAgreeVO> list(@Param("param") SessionAgreeDTO param);

	List<SessionAgreeVO> agreeMonCount(@Param("param") SessionAgreeDTO param);
	List<SessionAgreeVO> agreeProportion(@Param("param") SessionAgreeDTO param);

	SessionAgreeVO agreeSum(@Param("param") SessionAgreeDTO param);

	List<AgreeInfo> getAgree(@Param("userId")List<String> userId,@Param("staffId") List<String> staffId);
	void batchUpdate(@Param("agreeInfo") List<AgreeInfo> agreeInfo);
}
