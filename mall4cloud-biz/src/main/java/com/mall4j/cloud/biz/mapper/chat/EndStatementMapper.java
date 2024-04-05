package com.mall4j.cloud.biz.mapper.chat;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mall4j.cloud.biz.model.chat.EndStatement;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 结束语表
 */
@Mapper
public interface EndStatementMapper extends BaseMapper<EndStatement> {

	void save(@Param("param") EndStatement statement);

	void batchInsert(@Param("param") List<EndStatement> statement);

	List<EndStatement> getBySessionId(@Param("sessionId") List<Long> id);
	void deleteById(@Param("id") Long sessionId);
}
