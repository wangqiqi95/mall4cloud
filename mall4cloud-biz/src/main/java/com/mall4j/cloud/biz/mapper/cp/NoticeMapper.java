package com.mall4j.cloud.biz.mapper.cp;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mall4j.cloud.biz.dto.cp.NoticeDTO;
import com.mall4j.cloud.biz.model.cp.Notice;
import com.mall4j.cloud.biz.vo.cp.NoticeVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;


@Mapper
public interface NoticeMapper extends BaseMapper<Notice> {
	
	List<NoticeVO> list(@Param("param") NoticeDTO param);

	void save(@Param("notice") Notice notice);

	void update(@Param("notice") Notice notice);

	void deleteById(@Param("id") Long id);

	NoticeVO getById(@Param("id") Long id);

	NoticeVO getByMsgType(@Param("msgType") int type);
}
