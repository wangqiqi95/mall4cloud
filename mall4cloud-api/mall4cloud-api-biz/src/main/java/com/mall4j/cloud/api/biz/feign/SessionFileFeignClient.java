package com.mall4j.cloud.api.biz.feign;

import com.mall4j.cloud.api.biz.dto.cp.chat.SessionFileDTO;
import com.mall4j.cloud.api.biz.vo.SessionFileVO;
import com.mall4j.cloud.api.biz.vo.StaffSessionVO;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.feign.FeignInsideAuthConfig;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Date;
import java.util.List;


@FeignClient(value = "mall4cloud-biz",contextId = "sessionFile")
public interface SessionFileFeignClient {

    /**
     * 获取会话上下文
     * @param dto
     * @return 会话信息
     */
    @GetMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/sessionFile/getSessionFile")
    ServerResponseEntity<PageVO<SessionFileVO>> getSessionFile(@RequestBody SessionFileDTO dto);

    /**
     * 会话存档记录查询接口
     * @param dto
     * @return
     */
    @GetMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/sessionFile/sessionFileSearch")
    ServerResponseEntity<PageVO<SessionFileVO>> sessionFileSearch(@RequestBody  SessionFileDTO dto);

    /**
     * 会话总量接口
     * @param dto
     * @return
     */
    @GetMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/sessionFile/getStaffCount")
    ServerResponseEntity<StaffSessionVO> getStaffCount(@RequestBody  SessionFileDTO dto);
    /**
     * 获取与客户聊天的所有员工会话总量
     * @param dto
     * @return
     */
    @GetMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/sessionFile/getUserAndStaffCount")
    ServerResponseEntity<List<StaffSessionVO>> getUserAndStaffCount(@RequestBody  SessionFileDTO dto);

    /**
     * 获取会话存单token
     * @return
     */
    @GetMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/sessionFile/getMSGAUDITAccessToken")
    ServerResponseEntity<String> getMSGAUDITAccessToken();

    /**
     * 获取员工与会话最近联系时间
     * @param dto
     * @return
     */
    @GetMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/sessionFile/getLastTime")
    ServerResponseEntity<Date> getLastTime(@RequestBody  SessionFileDTO dto);
    /**
     * 获取群聊最近联系时间
     * @param dto
     * @return
     */
    @GetMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/sessionFile/getRoomLastTime")
    ServerResponseEntity<Date> getRoomLastTime(@RequestBody  SessionFileDTO dto);
}
