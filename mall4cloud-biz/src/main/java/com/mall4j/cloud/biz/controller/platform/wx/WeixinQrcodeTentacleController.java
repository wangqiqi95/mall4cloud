package com.mall4j.cloud.biz.controller.platform.wx;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.net.URLDecoder;
import cn.hutool.core.util.StrUtil;
import com.mall4j.cloud.api.platform.dto.CalcingDownloadRecordDTO;
import com.mall4j.cloud.api.platform.feign.DownloadCenterFeignClient;
import com.mall4j.cloud.biz.dto.WeixinQrcodeTentacleDTO;
import com.mall4j.cloud.biz.dto.WeixinQrcodeTentacleStoreExportDTO;
import com.mall4j.cloud.biz.dto.WeixinQrcodeTentacleStoreItemDTO;
import com.mall4j.cloud.biz.model.WeixinQrcodeTentacle;
import com.mall4j.cloud.biz.service.WeixinQrcodeTentacleService;
import com.mall4j.cloud.biz.service.WeixinQrcodeTentacleStoreService;
import com.mall4j.cloud.biz.vo.WeixinQrcodeTentacleStoreItemVO;
import com.mall4j.cloud.biz.vo.WeixinQrcodeTentacleStoreVO;
import com.mall4j.cloud.biz.vo.WeixinQrcodeTentacleVO;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.common.security.AuthUserContext;
import com.mall4j.cloud.common.util.RandomUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import ma.glasnost.orika.MapperFacade;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.File;
import java.nio.charset.Charset;
import java.sql.Array;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 微信触点二维码
 *
 * @author FrozenWatermelon
 * @date 2022-03-09 16:04:27
 */
@RestController("multishopWeixinQrcodeTentacleController")
@RequestMapping("/p/weixin/qrcode/tentacle")
@Api(tags = "微信触点二维码管理")
public class WeixinQrcodeTentacleController {

    @Autowired
    private WeixinQrcodeTentacleService weixinQrcodeTentacleService;

    @Autowired
    private WeixinQrcodeTentacleStoreService weixinQrcodeTentacleStoreService;

    @Autowired
    private DownloadCenterFeignClient downloadCenterFeignClient;

    @Autowired
	private MapperFacade mapperFacade;

	@GetMapping("/page")
	@ApiOperation(value = "获取微信触点二维码列表", notes = "分页获取微信触点二维码列表")
	public ServerResponseEntity<PageVO<WeixinQrcodeTentacleVO>> page(@Valid PageDTO pageDTO,
                                                                   @ApiParam(value = "名称", required = false)
                                                                    @RequestParam(value = "name",required = false)String name,
                                                                   @ApiParam(value = "门店id", required = false)
                                                                   @RequestParam(value = "storeId",required = false)String storeId) {
        if(StringUtils.isNotEmpty(name))  name= URLDecoder.decode(name, Charset.forName("UTF-8"));
		PageVO<WeixinQrcodeTentacleVO> weixinQrcodeTentaclePage = weixinQrcodeTentacleService.page(pageDTO,name,storeId);
		return ServerResponseEntity.success(weixinQrcodeTentaclePage);
	}

    @GetMapping("/storeCodePage")
    @ApiOperation(value = "微信触点二维码-查看门店列表", notes = "微信触点二维码-查看门店列表")
    public ServerResponseEntity<PageVO<WeixinQrcodeTentacleStoreVO>> storeCodePage(@Valid PageDTO pageDTO,
                                                                          @RequestParam(value = "tentacleId",required = true)String tentacleId) {
        PageVO<WeixinQrcodeTentacleStoreVO> weixinQrcodeTentaclePage = weixinQrcodeTentacleStoreService.storeCodePage(pageDTO,tentacleId);
        return ServerResponseEntity.success(weixinQrcodeTentaclePage);
    }

    @PostMapping("/storeCodeItemPage")
    @ApiOperation(value = "微信触点二维码-查看门店列表详情", notes = "微信触点二维码-查看门店列表详情")
    public ServerResponseEntity<PageVO<WeixinQrcodeTentacleStoreItemVO>> storeCodeItemPage(@Valid @RequestBody WeixinQrcodeTentacleStoreItemDTO weixinQrcodeTentacleStoreItemDTO) {
        PageVO<WeixinQrcodeTentacleStoreItemVO> weixinQrcodeTentacleStoreItemPage = weixinQrcodeTentacleStoreService.storeCodeItemPage(weixinQrcodeTentacleStoreItemDTO);
        return ServerResponseEntity.success(weixinQrcodeTentacleStoreItemPage);
    }

    @PostMapping("/storeCodeItemExcel")
    @ApiOperation(value = "微信触点二维码-门店列表详情导出", notes = "微信触点二维码-门店列表详情导出")
    public ServerResponseEntity<String> storeCodeItemExcel(@RequestBody WeixinQrcodeTentacleStoreItemDTO param){
        return ServerResponseEntity.success(weixinQrcodeTentacleStoreService.storeCodeItemExcel(param));
    }

    @PostMapping("/qrcodeTentacleStoreExcel")
    @ApiOperation(value = "微信触点二维码-门店列表批量导出", notes = "微信触点二维码-门店列表批量导出")
    public ServerResponseEntity<String> qrcodeTentacleStoreExcel(@RequestBody WeixinQrcodeTentacleStoreExportDTO param){
        return ServerResponseEntity.success(weixinQrcodeTentacleStoreService.qrcodeTentacleStoreExcel(param));
    }

	@GetMapping
    @ApiOperation(value = "获取微信触点二维码", notes = "根据id获取微信触点二维码")
    public ServerResponseEntity<WeixinQrcodeTentacle> getById(@RequestParam String id) {
        return ServerResponseEntity.success(weixinQrcodeTentacleService.getById(id));
    }

    @GetMapping("/sendToEmail")
    @ApiOperation(value = "二维码邮箱发送", notes = "二维码邮箱发送")
    public ServerResponseEntity<String> sendToEmail(@RequestParam(value = "id",required = true) String id,
                                                    @RequestParam(value = "receiveEmail",required = true) String receiveEmail) {
        return weixinQrcodeTentacleService.sendQrcodeZipToEmail(id,receiveEmail);
    }

    @PostMapping
    @ApiOperation(value = "保存微信触点二维码", notes = "保存微信触点二维码")
    public ServerResponseEntity<String> save(@Valid @RequestBody WeixinQrcodeTentacleDTO weixinQrcodeTentacleDTO) {

        CalcingDownloadRecordDTO downloadRecordDTO=new CalcingDownloadRecordDTO();
        downloadRecordDTO.setDownloadTime(new Date());
        downloadRecordDTO.setFileName(DateUtil.format(new Date(),"yyyyMMddHHmmss")+"微信触点二维码");
        downloadRecordDTO.setCalCount(0);
        downloadRecordDTO.setOperatorName(AuthUserContext.get().getUsername());
        downloadRecordDTO.setOperatorNo(""+AuthUserContext.get().getUserId());
        ServerResponseEntity serverResponseEntity=downloadCenterFeignClient.newCalcingTask(downloadRecordDTO);
        Long downLoadHisId=null;
        if(serverResponseEntity.isSuccess()){
            downLoadHisId=Long.parseLong(serverResponseEntity.getData().toString());
        }
        weixinQrcodeTentacleDTO.setDownLoadHisId(downLoadHisId);

        weixinQrcodeTentacleService.save(weixinQrcodeTentacleDTO);

        return ServerResponseEntity.success("批量操作需要时间，请稍后查看");
    }

    @PutMapping
    @ApiOperation(value = "更新微信触点二维码", notes = "更新微信触点二维码")
    public ServerResponseEntity<Void> update(@Valid @RequestBody WeixinQrcodeTentacleDTO weixinQrcodeTentacleDTO) {
        WeixinQrcodeTentacle weixinQrcodeTentacle = mapperFacade.map(weixinQrcodeTentacleDTO, WeixinQrcodeTentacle.class);
        weixinQrcodeTentacleService.update(weixinQrcodeTentacle);
        return ServerResponseEntity.success();
    }

    @DeleteMapping
    @ApiOperation(value = "删除微信触点二维码", notes = "根据微信触点二维码id删除微信触点二维码")
    public ServerResponseEntity<Void> delete(@RequestParam String id) {
        weixinQrcodeTentacleService.deleteById(id);
        return ServerResponseEntity.success();
    }
}
