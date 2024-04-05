package com.mall4j.cloud.product.controller.platform;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.net.URLDecoder;
import com.alibaba.excel.EasyExcel;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.exception.LuckException;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.common.rocketmq.OnsMQTemplate;
import com.mall4j.cloud.product.dto.ElPriceTagDTO;
import com.mall4j.cloud.product.listener.ElPriceTagSpuReadExcelListener;
import com.mall4j.cloud.product.listener.SpuCodeReadExcelListener;
import com.mall4j.cloud.product.model.ElPriceTag;
import com.mall4j.cloud.product.service.ElPriceProdService;
import com.mall4j.cloud.product.service.ElPriceTagService;
import com.mall4j.cloud.product.service.ElectronicSignService;
import com.mall4j.cloud.product.service.SpuExcelService;
import com.mall4j.cloud.product.vo.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import ma.glasnost.orika.MapperFacade;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 电子价签管理
 *
 * @author FrozenWatermelon
 * @date 2022-03-27 22:23:15
 */
@RestController("platformElPriceTagController")
@RequestMapping("/p/el/price/tag")
@Api(tags = "电子价签管理")
public class ElPriceTagController {

    @Autowired
    private ElPriceTagService elPriceTagService;
    @Autowired
    private ElPriceProdService elPriceProdService;
    @Autowired
    private SpuExcelService spuExcelService;
    @Autowired
	private MapperFacade mapperFacade;
    @Autowired
    private OnsMQTemplate aliElectronicSignTemplate;
    @Autowired
    private ElectronicSignService electronicSignService;

	@GetMapping("/page")
	@ApiOperation(value = "获取电子价签管理列表", notes = "分页获取电子价签管理列表")
	public ServerResponseEntity<PageVO<ElPriceTagVO>> page(@Valid PageDTO pageDTO,@RequestParam(value = "name",required = false) String name) {
        if(StringUtils.isNotEmpty(name))  name= URLDecoder.decode(name, Charset.forName("UTF-8"));
		PageVO<ElPriceTagVO> elPriceTagPage = elPriceTagService.page(pageDTO,name);
		return ServerResponseEntity.success(elPriceTagPage);
	}

    @GetMapping("/prodPage")
    @ApiOperation(value = "获取电子价签商品管理列表", notes = "获取电子价签商品管理列表")
    public ServerResponseEntity<PageVO<ElPriceProdVO>> prodPage(@Valid PageDTO pageDTO,
                                                                @RequestParam(value = "elId",required = false) String elId,
                                                                @RequestParam(value = "name",required = false) String name) {
        if(StringUtils.isNotEmpty(name))  name= URLDecoder.decode(name, Charset.forName("UTF-8"));
        PageVO<ElPriceProdVO> elPriceTagPage = elPriceTagService.prodPage(pageDTO,elId,name);
        return ServerResponseEntity.success(elPriceTagPage);
    }

	@GetMapping("/getById")
    @ApiOperation(value = "获取电子价签详情", notes = "获取电子价签详情")
    public ServerResponseEntity<ElPriceTagVO> getById(@RequestParam String id) {
        return ServerResponseEntity.success(elPriceTagService.getElPriceTagVOById(id));
    }

    @PostMapping("/save")
    @ApiOperation(value = "保存电子价签管理", notes = "保存电子价签管理")
    public ServerResponseEntity<Void> save(@Valid @RequestBody ElPriceTagDTO elPriceTagDTO) {
        ElPriceTag elPriceTag = mapperFacade.map(elPriceTagDTO, ElPriceTag.class);
        elPriceTag.setCreateTime(new Date());
        elPriceTag.setIsDeleted(0);
        elPriceTag.setProdCount(elPriceTagDTO.getProdList().size());
        elPriceTagService.save(elPriceTag);
        Long elId=elPriceTag.getId();
        //保存商品集合
        if(CollectionUtil.isNotEmpty(elPriceTagDTO.getProdList())){
            elPriceProdService.saveBatch(elId,elPriceTagDTO.getProdList());
        }

        return ServerResponseEntity.success();
    }

    @PutMapping("/update")
    @ApiOperation(value = "更新电子价签管理", notes = "更新电子价签管理")
    public ServerResponseEntity<Void> update(@Valid @RequestBody ElPriceTagDTO elPriceTagDTO) {
        ElPriceTag elPriceTag = mapperFacade.map(elPriceTagDTO, ElPriceTag.class);
        elPriceTag.setUpdateTime(new Date());
        elPriceTag.setProdCount(elPriceTagDTO.getProdList().size());
        elPriceTagService.update(elPriceTag);
        Long elId=elPriceTag.getId();
        //保存商品集合
        if(CollectionUtil.isNotEmpty(elPriceTagDTO.getProdList())){
            elPriceProdService.saveBatch(elId,elPriceTagDTO.getProdList());
        }

        return ServerResponseEntity.success();
    }

    @DeleteMapping("/delete")
    @ApiOperation(value = "删除电子价签管理", notes = "根据电子价签管理id删除电子价签管理")
    public ServerResponseEntity<Void> delete(@RequestParam String id) {
        elPriceTagService.deleteById(id);
        return ServerResponseEntity.success();
    }

    @DeleteMapping("/deleteByIds")
    @ApiOperation(value = "批量删除电子价签管理", notes = "批量删除电子价签管理")
    public ServerResponseEntity<Void> deleteByIds(@RequestParam List<String> ids) {
        elPriceTagService.deleteByIds(ids);
        return ServerResponseEntity.success();
    }

    @DeleteMapping("/deleteProd")
    @ApiOperation(value = "删除/批量删除电子价签商品", notes = "删除/批量删除电子价签商品")
    public ServerResponseEntity<Void> deleteProd(@ApiParam(value = "价签商品管理id集合",required = true)
                                                     @RequestParam(value = "ids",required = true) List<Long> ids) {
        elPriceProdService.deleteByIds(ids);
        return ServerResponseEntity.success();
    }

    @DeleteMapping("/deleteAllProd")
    @ApiOperation(value = "清空电子价签商品", notes = "清空电子价签商品")
    public ServerResponseEntity<Void> deleteAllProd(@ApiParam(value = "价签管理id",required = true)
                                                        @RequestParam(value = "elId",required = true) Long elId) {
        elPriceProdService.deleteByElId(elId);
        return ServerResponseEntity.success();
    }

    @ApiOperation(value = "批量导入模板")
    @PostMapping("/readExcelSpus")
    public ServerResponseEntity<List<SpuPageVO>> readExcelSpus(@RequestParam("excelFile") MultipartFile file) {
        if (file == null) {
            throw new LuckException("网络繁忙，请稍后重试");
        }
        try {
            Map<Integer, List<String>> errorMap = new HashMap<>(8);
            ElPriceTagSpuReadExcelListener elPriceTagSpuReadExcelListener=new ElPriceTagSpuReadExcelListener(spuExcelService, errorMap);
            EasyExcel.read(file.getInputStream(), ElPriceTagSpuReadExcelVO.class, elPriceTagSpuReadExcelListener).sheet().doRead();
            List<SpuPageVO> pageVOS=elPriceTagSpuReadExcelListener.getSpuPageVOS();
            return ServerResponseEntity.success(pageVOS);
        } catch (IOException e) {
            throw new LuckException(e.getMessage());
        }
    }

    @ApiOperation(value = "testProdElMq")
    @PostMapping("/testProdElMq")
    public ServerResponseEntity<Void> testProdElMq(@RequestParam List<Long> ids,
                                               @RequestParam(value = "mqType",required = false,defaultValue = "1") Integer mqType) {
        //1:同步商品 2:门店开关开启价签同步
        if(mqType==1){
            electronicSignService.syncSpu(ids);
        }else if(mqType==2){
            electronicSignService.syncStoreSpu(ids);
        }
        return ServerResponseEntity.success();
    }


}
