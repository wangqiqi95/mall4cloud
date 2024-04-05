package com.mall4j.cloud.biz.controller.app.staff;


import com.mall4j.cloud.biz.dto.chat.KeywordHitRecomdDTO;
import com.mall4j.cloud.biz.service.chat.KeywordHitService;
import com.mall4j.cloud.biz.vo.chat.KeywordHitRecomdVO;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.common.security.AuthUserContext;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.validation.Valid;

/**
 * 命中关键词
 */
@Slf4j
@RestController("staffkeywordHitController")
@RequestMapping("/s/keywordHit")
@Api(tags = "导购小程序会话存档关键词")
public class KeywordHitController {

    @Autowired
    private KeywordHitService hitService;


    @GetMapping("/appRecomdPage")
    @ApiOperation(value = "推荐内容", notes = "推荐内容")
    public ServerResponseEntity<PageVO<KeywordHitRecomdVO>> page(@Valid KeywordHitRecomdDTO recomdDTO)  {
        PageVO<KeywordHitRecomdVO> materialPage = hitService.appRecomdPage(recomdDTO);
        return ServerResponseEntity.success(materialPage);
    }

}
