package com.mall4j.cloud.biz.mapper.chat;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mall4j.cloud.biz.dto.chat.TimeOutLogDTO;
import com.mall4j.cloud.biz.model.chat.TimeOutLog;
import com.mall4j.cloud.biz.vo.chat.TimeOutLogVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 超时记录表
 */
@Mapper
public interface TimeOutLogMapper extends BaseMapper<TimeOutLog> {
	
	List<TimeOutLogVO> list(@Param("param") TimeOutLogDTO param);

	void save(@Param("timeOut") TimeOutLog outLog);

	void update(@Param("timeOut") TimeOutLog outLog);

	void deleteById(@Param("id") Long id);

	/**
	 * 根据客户id和员工id查询会话记录信息
	 * @param userId
	 * @param staffId
	 * @return
	 */
	TimeOutLogVO getByUserId(@Param("userId") String userId,@Param("staffId") String staffId);

	TimeOutLogVO getByRoomId(@Param("roomId") String roomId,@Param("staffId") String staffId);

	TimeOutLogVO getByStaffId(@Param("userId") String userId,@Param("staffId") String staffId);
	//统计触发人数
	Integer getCountPeople(@Param("param") TimeOutLogDTO param);
	//统计触发新增次数
	Integer getAddCount(@Param("param") TimeOutLogDTO param);
	//统计所有的触发次数
	Integer getCount(@Param("param") TimeOutLogDTO param);
	//统计每天的触发次数
	List<TimeOutLogVO> outLogChart(@Param("param") TimeOutLogDTO param);

	TimeOutLogVO getByRoomId(@Param("roomId") String roomId);

	TimeOutLogVO getByTimeOutId(@Param("timeOutId") String timeOutId);
}
