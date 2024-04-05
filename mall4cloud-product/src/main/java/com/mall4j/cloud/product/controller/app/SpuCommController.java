package com.mall4j.cloud.product.controller.app;

import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.security.AuthUserContext;
import com.mall4j.cloud.product.dto.SpuCommDTO;
import com.mall4j.cloud.product.model.SpuComm;
import com.mall4j.cloud.product.service.SpuCommService;
import com.mall4j.cloud.product.vo.SpuCommStatisticsStarVO;
import com.mall4j.cloud.product.vo.SpuCommStatisticsVO;
import com.mall4j.cloud.product.vo.SpuCommVO;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Date;

/**
 * 商品评论
 *
 * @author YXF
 * @date 2021-01-11 13:47:33
 */
@RestController("appSpuCommController")
@RequestMapping("/spu_comm")
@Api(tags = "商品评论")
public class SpuCommController {

    @Autowired
    private SpuCommService spuCommService;

    @GetMapping("/ua/page")
    @ApiOperation(value = "获取商品评论列表(商品)", notes = "分页获取商品评论列表")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "spuId", value = "商品id", required = true),
            @ApiImplicitParam(name = "evaluate", value = "评论类型  null/0 全部，1好评 2中评 3差评 4有图", defaultValue = "0")
    })
    public ServerResponseEntity<PageVO<SpuCommVO>> page(@Valid PageDTO pageDTO, @RequestParam("spuId") Long spuId, @RequestParam("evaluate") Integer evaluate) {
        PageVO<SpuCommVO> spuCommPage = spuCommService.spuCommPage(pageDTO, spuId, evaluate);
        return ServerResponseEntity.success(spuCommPage);
    }

    @GetMapping("/ua/prod_comm_data")
    @ApiOperation(value = "返回商品评论数据(好评率 好评数量 中评数 差评数)(商品)", notes = "根据商品id获取")
    @ApiImplicitParam(name = "spuId", value = "商品id", required = true)
    public ServerResponseEntity<SpuCommStatisticsVO> getProdCommData(Long spuId) {
        return ServerResponseEntity.success(spuCommService.getProdCommDataBySpuId(spuId));
    }

    @GetMapping("/ua/prod_comm_data_by_star")
    @ApiOperation(value = "返回商品评论数据(按星数划分数量)(商品)", notes = "根据商品id获取")
    @ApiImplicitParam(name = "spuId", value = "商品id", required = true)
    public ServerResponseEntity<SpuCommStatisticsStarVO> getProdCommDataByStar(Long spuId) {
        return ServerResponseEntity.success(spuCommService.getProdCommDataByStar(spuId));
    }

    @GetMapping("/prod_comm_page_by_user")
    @ApiOperation(value = "根据用户返回评论分页数据(该用户发表的评论特殊显示)", notes = "传入页码")
    public ServerResponseEntity<PageVO<SpuCommVO>> getProdCommPageByUser(@Valid PageDTO pageDTO) {
        return ServerResponseEntity.success(spuCommService.spuCommPageByUserId(pageDTO, AuthUserContext.get().getUserId()));
    }

    @GetMapping("/ua/prod_comm_pag")
    @ApiOperation(value = "返回评论分页数据(未登陆时请求)", notes = "传入页码")
    public ServerResponseEntity<PageVO<SpuCommVO>> getProdCommPage(@Valid PageDTO pageDTO) {
        return ServerResponseEntity.success(spuCommService.spuCommPageByUserId(pageDTO, null));
    }

    @PostMapping("/comment")
    @ApiOperation(value = "添加评论")
    public ServerResponseEntity<Void> saveProdCommPage(@Valid @RequestBody SpuCommDTO spuCommDTO) {
        //追加评论 add by hwy20220306
        if(spuCommDTO.getIsAddit()!=null && spuCommDTO.getIsAddit()==1){
            SpuComm spuComm = new SpuComm();
            spuComm.setSpuCommId(spuCommDTO.getSpuCommId());
            spuComm.setAdditContent(spuCommDTO.getContent());
            spuComm.setAdditPics(spuCommDTO.getPics());
            spuComm.setAdditReplySts(0);
            spuComm.setAdditCreateTime(new Date());
            spuComm.setHasImages(spuComm.getAdditPics()==null?null:1);
            spuCommService.update(spuComm);
            return ServerResponseEntity.success();
        }
        spuCommService.comm(spuCommDTO);
        return ServerResponseEntity.success();
    }

    @GetMapping("/get_prod_comment")
    @ApiOperation(value = "根据itemId获取评论", notes = "根据itemId获取评论")
    public ServerResponseEntity<SpuCommVO> getProdComment(@RequestParam("orderItemId") Long orderItemId) {
        return ServerResponseEntity.success(spuCommService.getSpuCommByOrderItemId(orderItemId, AuthUserContext.get().getUserId()));
    }
}
