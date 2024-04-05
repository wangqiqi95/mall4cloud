package com.mall4j.cloud.platform.controller.staff;

import com.mall4j.cloud.api.platform.dto.TentacleContentDTO;
import com.mall4j.cloud.api.platform.vo.TentacleContentVO;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.platform.service.TentacleContentService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 触点内容信息
 *
 * @author ZengFanChang
 * @date 2021-12-18 18:05:05
 */
@RestController("staffTentacleContentController")
@RequestMapping("/s/tentacle_content")
@Api(tags = "导购小程序-触点内容信息")
public class TentacleContentController {

    @Autowired
    private TentacleContentService tentacleContentService;

    /**
     * 创建或者查询触点对象
     * C端  分享之前调用生成触点对象
     * C端  A分享给B , B访问时  生成触点对象
     *
     * @return
     */
    @PostMapping("/findOrCreateByContent")
    @ApiOperation(value = "为各个转发,分享返回触点对象信息", notes = "为各个转发,分享返回触点对象信息")
    public ServerResponseEntity<TentacleContentVO> findOrCreateByContent(@RequestBody TentacleContentDTO tentacleContent) {
        return ServerResponseEntity.success(tentacleContentService.findOrCreateByContent(tentacleContent));
    }


    /**
     * 根据触点号查询触点信息
     *
     * @param tentacleNo 触点号
     * @return
     */
    @GetMapping("/findByTentacleNo")
    @ApiOperation(value = "根据触点号查询触点信息")
    public ServerResponseEntity<TentacleContentVO> findByTentacleNo(@RequestParam("tentacleNo") String tentacleNo) {
        return ServerResponseEntity.success(tentacleContentService.findByTentacleNo(tentacleNo));
    }

}
