package com.mall4j.cloud.biz.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mall4j.cloud.biz.dto.TaskInfoDTO;
import com.mall4j.cloud.biz.dto.TaskInfoSearchParamDTO;
import com.mall4j.cloud.biz.model.TaskInfo;
import com.mall4j.cloud.biz.vo.cp.CustGroupVO;
import com.mall4j.cloud.biz.vo.cp.taskInfo.TaskInfoPageVO;
import com.mall4j.cloud.biz.vo.cp.taskInfo.TaskInfoVO;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.vo.PageVO;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;

/**
 * 任务信息表
 */
public interface TaskInfoService extends IService<TaskInfo> {

    /**
     * 保存任务信息
     * @param taskInfo 任务对象
     */
    void saveTaskInfo(TaskInfoDTO taskInfo);

    /**
     * 更新任务状态
     * @param id 任务id
     * @param status 任务状态
     */
    void updateTaskStatus(Long id, Integer status);

    /**
     * 分页查询任务信息
     * @param pageDTO 分页对象
     * @param taskInfoSearchParamDTO 查询条件
     */
    PageVO<TaskInfoPageVO> page(PageDTO pageDTO, TaskInfoSearchParamDTO taskInfoSearchParamDTO);

    /**
     * 结束任务
     * @param id 任务id
     */
    void endTask(Long id);

    /**
     * 复制任务
     * @param id 任务id
     */
    void copyTask(Long id);

    /**
     * 获取任务信息
     * @param id 任务id
     */
    TaskInfoVO getTaskInfo(Long id);

    /**
     * 导入客户信息
     * @param file 客户信息excel
     * @param uuid 任务临时id
     * @param response 响应对象
     */
    void importClients(MultipartFile file, String uuid, HttpServletResponse response);

    /**
     * 下载导入客户模板
     * @param response 响应对象
     */
    void downloadClientImportTemplate(HttpServletResponse response);
}

