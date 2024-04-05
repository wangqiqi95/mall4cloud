package com.mall4j.cloud.product.controller.admin;

import com.mall4j.cloud.common.constant.Constant;
import com.mall4j.cloud.common.product.vo.BrandVO;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.product.dto.BrandDTO;
import com.mall4j.cloud.product.service.BrandService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 品牌信息
 *
 * @author FrozenWatermelon
 * @date 2020-10-28 15:27:24
 */
@RestController("adminBrandController")
@RequestMapping("/mp/brand")
@Api(tags = "admin-品牌信息")
public class BrandController {

    @Autowired
    private BrandService brandService;

    @GetMapping("/list_by_params")
    @ApiOperation(value = "根据参数获取平台品牌列表", notes = "根据参数获取平台品牌列表")
    public ServerResponseEntity<List<BrandVO>> listByParams(BrandDTO brandDTO) {
        brandDTO.setShopId(Constant.PLATFORM_SHOP_ID);
        List<BrandVO> brandVOList = brandService.listByParams(brandDTO);
        return ServerResponseEntity.success(brandVOList);
    }
}
