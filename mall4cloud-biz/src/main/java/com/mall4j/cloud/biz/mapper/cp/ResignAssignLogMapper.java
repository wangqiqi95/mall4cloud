package com.mall4j.cloud.biz.mapper.cp;

import com.mall4j.cloud.biz.dto.cp.ResignAssignLogQueryDTO;
import com.mall4j.cloud.biz.model.cp.ResignAssignLog;
import io.swagger.models.auth.In;
import org.apache.ibatis.annotations.Param;

import java.util.List;


/**
 * 离职分配日志表
 *
 * @author hwy
 * @date 2022-01-24 11:05:43
 */
public interface ResignAssignLogMapper {

	/**
	 * 获取离职分配日志表列表
	 * @return 离职分配日志表列表
     * @param request 查询条件
	 */
	List<ResignAssignLog> list(@Param("et")ResignAssignLogQueryDTO request);
	/**
	 * 保存离职分配日志表
	 * @param resignAssignLog 离职分配日志表
	 */
	void save(@Param("et") ResignAssignLog resignAssignLog);
	/**
	 * 更新离职分配日志表
	 * @param resignAssignLog 离职分配日志表
	 */
	void update(@Param("resignAssignLog") ResignAssignLog resignAssignLog);

	ResignAssignLog getById(Long id);

	/**
	 * 定时查询同步微信
	 * @param pushStatus 推送状态
	 * @return
	 */
    List<ResignAssignLog> sycnCustList(@Param("pushStatus") Integer pushStatus);

	/**
	 * 关闭分配日志的推送状态
	 * @param id 分配日志id
	 */
	void completePush(@Param("id") Long id);
	/**
	 * 关闭分配日志的推送状态
	 * @param id 分配日志id
	 */
	void completeReplace(@Param("id") Long id);
	/**
	 * 更新分配日志的成功条数
	 * @param id
	 */
	void updateSuccess(Long id);
}
