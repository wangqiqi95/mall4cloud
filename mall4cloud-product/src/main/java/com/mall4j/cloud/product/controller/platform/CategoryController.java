package com.mall4j.cloud.product.controller.platform;

import com.mall4j.cloud.common.i18n.I18nMessage;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.api.product.dto.CategoryShopDTO;
import com.mall4j.cloud.product.service.CategoryShopService;
import com.mall4j.cloud.product.vo.CategoryShopVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * @Author lth
 * @Date 2021/4/26 9:22
 */
@RestController("platformCategoryController")
@RequestMapping("/p/category")
@Api(tags = "platform-分类信息")
public class CategoryController {

    @Autowired
    private CategoryShopService categoryShopService;

    @GetMapping("/signing_info_by_shopId")
    @ApiOperation(value = "根据店铺id获取分类签约信息", notes = "根据店铺id获取分类签约信息")
    public ServerResponseEntity<List<CategoryShopVO>> listByShopId(@RequestParam(value = "shopId") Long shopId) {
        List<CategoryShopVO> categoryShopVOList = categoryShopService.listByShopId(shopId, I18nMessage.getLang());
        return ServerResponseEntity.success(categoryShopVOList);
    }

    @PutMapping("/signing")
    @ApiOperation(value = "更新店铺签约分类", notes = "更新店铺签约分类")
    public ServerResponseEntity<Void> signing(@Valid @RequestBody List<CategoryShopDTO> categoryShopDTOList, @RequestParam(value = "shopId") Long shopId) {
        categoryShopService.signingCategory(categoryShopDTOList, shopId);
        return ServerResponseEntity.success();
    }
}
