package com.mall4j.cloud.user.feign;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.mall4j.cloud.api.user.crm.request.UpdateUserTagRequest;
import com.mall4j.cloud.api.user.crm.response.CrmResult;
import com.mall4j.cloud.api.user.dto.UpdateScoreDTO;
import com.mall4j.cloud.api.user.feign.crm.CrmUserFeignClient;
import com.mall4j.cloud.common.exception.LuckException;
import com.mall4j.cloud.common.response.ResponseEnum;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.user.service.crm.CrmService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

/**
 * 用户地址feign连接
 *
 * @author FrozenWatermelon
 * @date 2020/12/07
 */
@Slf4j
@RestController
public class CrmFeignController implements CrmUserFeignClient {

    @Autowired
    private CrmService crmService;


    /**
     * 更新用户标签
     * @param request
     * @return
     */
    @Override
    public ServerResponseEntity updateMemberTag(UpdateUserTagRequest request) {
        try {
            crmService.updateMemberTag(request);
            return ServerResponseEntity.success();
        }catch (Exception e){
            return ServerResponseEntity.showFailMsg(e.getLocalizedMessage());
        }
    }

    @Override
    public ServerResponseEntity<CrmResult<JSONArray>> editageUser(String unionId,String active,String mobileNumber) {
        try {
            return crmService.editageUser(unionId,active,mobileNumber);
        }catch (Exception e){
            return ServerResponseEntity.showFailMsg(e.getLocalizedMessage());
        }
    }

    /**
     * todo 待实现
     * @param updateScoreDTO
     * @return
     */
    @Override
    public ServerResponseEntity updateScore(UpdateScoreDTO updateScoreDTO) {
        return null;
    }
}
