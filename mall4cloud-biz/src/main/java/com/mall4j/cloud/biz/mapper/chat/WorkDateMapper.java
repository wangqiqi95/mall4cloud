package com.mall4j.cloud.biz.mapper.chat;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mall4j.cloud.biz.model.chat.WorkDate;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 工作时间表
 */
@Mapper
public interface WorkDateMapper extends BaseMapper<WorkDate> {

	void save(@Param("param") WorkDate work);

	void batchInsert(@Param("param") List<WorkDate> work);

	List<WorkDate> getBySessionId(@Param("sessionId") List<Long> id);
	void deleteById(@Param("id") Long sessionId);
}
