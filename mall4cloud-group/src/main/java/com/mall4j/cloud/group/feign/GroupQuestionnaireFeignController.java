package com.mall4j.cloud.group.feign;

import com.mall4j.cloud.api.group.feign.GroupQuestionnaireFeignClient;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.group.service.QuestionnaireService;
import com.mall4j.cloud.group.vo.questionnaire.QuestionnaireDetailVO;
import ma.glasnost.orika.MapperFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

/**
 * @date 2023/5/26
 */
@RestController
public class GroupQuestionnaireFeignController implements GroupQuestionnaireFeignClient {

    @Autowired
    private QuestionnaireService questionnaireService;

    @Override
    public ServerResponseEntity<Boolean> checkUserAuth(Long id, Long storeId, Long userId) {
        Boolean aBoolean = questionnaireService.checkUserAuth(id, storeId, userId);
        return ServerResponseEntity.success(aBoolean);
    }
}
