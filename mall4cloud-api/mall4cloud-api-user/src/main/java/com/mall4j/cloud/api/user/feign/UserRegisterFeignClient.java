package com.mall4j.cloud.api.user.feign;

import com.mall4j.cloud.api.user.dto.FriendlyWalkRegisterRequest;
import com.mall4j.cloud.api.user.dto.UserScoreLockDTO;
import com.mall4j.cloud.api.user.vo.UserApiVO;
import com.mall4j.cloud.common.feign.FeignInsideAuthConfig;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import org.apache.ibatis.annotations.Param;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "mall4cloud-user",contextId = "user-register")
public interface UserRegisterFeignClient {

    /**
     * friendlyWalk 注册接口
     * @param friendlyWalkRegisterRequest 参数
     * @return 是否成功
     */
    @PostMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/user/friendlyWalkRegister")
    ServerResponseEntity<String> friendlyWalkRegister(@RequestBody FriendlyWalkRegisterRequest friendlyWalkRegisterRequest);

    /**
     * 视频号4。0注册接口  如果会员存在直接返回，如果会员不存在则添加一个临时会员账户。
     * @param unionId
     * @return 是否成功
     */
    @GetMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/user/ecChannelRegister")
    ServerResponseEntity<UserApiVO> ecChannelRegister(@RequestParam("unionId") String unionId);
}
