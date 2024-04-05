package com.mall4j.cloud.platform.controller.app;

import com.mall4j.cloud.api.platform.dto.TentacleContentDTO;
import com.mall4j.cloud.api.platform.vo.TentacleContentVO;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.platform.model.TentacleContent;
import com.mall4j.cloud.platform.service.TentacleContentService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import ma.glasnost.orika.MapperFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * 触点内容信息
 *
 * @author ZengFanChang
 * @date 2021-12-18 18:05:05
 */
@RestController("appTentacleContentController")
@RequestMapping("/tentacle_content")
@Api(tags = "触点内容信息")
public class TentacleContentController {

    @Autowired
    private TentacleContentService tentacleContentService;

    @Autowired
    private MapperFacade mapperFacade;

    @GetMapping("/page")
    @ApiOperation(value = "获取触点内容信息列表", notes = "分页获取触点内容信息列表")
    public ServerResponseEntity<PageVO<TentacleContent>> page(@Valid PageDTO pageDTO) {
        PageVO<TentacleContent> tentacleContentPage = tentacleContentService.page(pageDTO);
        return ServerResponseEntity.success(tentacleContentPage);
    }

    @GetMapping
    @ApiOperation(value = "获取触点内容信息", notes = "根据id获取触点内容信息")
    public ServerResponseEntity<TentacleContent> getById(@RequestParam Long id) {
        return ServerResponseEntity.success(tentacleContentService.getById(id));
    }

    @PostMapping
    @ApiOperation(value = "保存触点内容信息", notes = "保存触点内容信息")
    public ServerResponseEntity<Void> save(@Valid @RequestBody TentacleContentDTO tentacleContentDTO) {
        TentacleContent tentacleContent = mapperFacade.map(tentacleContentDTO, TentacleContent.class);
        tentacleContentService.save(tentacleContent);
        return ServerResponseEntity.success();
    }

    @PutMapping
    @ApiOperation(value = "更新触点内容信息", notes = "更新触点内容信息")
    public ServerResponseEntity<Void> update(@Valid @RequestBody TentacleContentDTO tentacleContentDTO) {
        TentacleContent tentacleContent = mapperFacade.map(tentacleContentDTO, TentacleContent.class);
        tentacleContentService.update(tentacleContent);
        return ServerResponseEntity.success();
    }

    @DeleteMapping
    @ApiOperation(value = "删除触点内容信息", notes = "根据触点内容信息id删除触点内容信息")
    public ServerResponseEntity<Void> delete(@RequestParam Long id) {
        tentacleContentService.deleteById(id);
        return ServerResponseEntity.success();
    }


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
