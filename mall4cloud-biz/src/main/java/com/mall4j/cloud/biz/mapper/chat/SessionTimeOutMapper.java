package com.mall4j.cloud.biz.mapper.chat;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mall4j.cloud.biz.dto.chat.SessionTimeOutDTO;
import com.mall4j.cloud.biz.model.chat.SessionTimeOut;
import com.mall4j.cloud.biz.vo.chat.SessionTimeOutVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;


@Mapper
public interface SessionTimeOutMapper extends BaseMapper<SessionTimeOut> {
	
	List<SessionTimeOutVO> list(@Param("param") SessionTimeOutDTO param);

	void save(@Param("timeOut") SessionTimeOut timeOut);

	void update(@Param("timeOut") SessionTimeOut timeOut);

	void deleteById(@Param("id") Long id);

	SessionTimeOutVO getById(@Param("id") Long id);

	/**
	 * 根据员工id查询最新的一条超时规则
	 * @param staffId
	 * @return
	 */
	SessionTimeOutVO getByStaffId(@Param("staffId") String staffId,@Param("type") Integer type);
}
