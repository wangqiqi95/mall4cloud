package com.mall4j.cloud.platform.controller.platform;

import cn.hutool.core.util.StrUtil;
import com.mall4j.cloud.api.biz.feign.QrcodeFeignClient;
import com.mall4j.cloud.api.platform.dto.CalcingDownloadRecordDTO;
import com.mall4j.cloud.api.platform.feign.DownloadCenterFeignClient;
import com.mall4j.cloud.api.platform.vo.SelectedStoreVO;
import com.mall4j.cloud.api.platform.vo.SoldTzStoreVO;
import com.mall4j.cloud.api.platform.vo.SysUserVO;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.exception.LuckException;
import com.mall4j.cloud.common.response.ResponseEnum;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.common.security.AuthUserContext;
import com.mall4j.cloud.common.util.NumberTo64;
import com.mall4j.cloud.common.vo.UploadExcelVO;
import com.mall4j.cloud.platform.dto.StoreQueryParamDTO;
import com.mall4j.cloud.platform.dto.StoreSelectedParamDTO;
import com.mall4j.cloud.platform.dto.TzStoreDTO;
import com.mall4j.cloud.platform.event.SoldStoreEvent;
import com.mall4j.cloud.platform.manager.StoreExcelManager;
import com.mall4j.cloud.platform.model.TzStore;
import com.mall4j.cloud.platform.service.SysUserService;
import com.mall4j.cloud.platform.service.TzStoreService;
import com.mall4j.cloud.platform.vo.CloseStoreStaffVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import ma.glasnost.orika.MapperFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * @author FrozenWatermelon
 * @date 2022-01-18 10:46:32
 */
@Slf4j
@RestController("platformTzStoreController")
@RequestMapping("/p/store")
@Api(tags = "platform-门店接口")
public class TzStoreController {

    @Autowired
    private TzStoreService tzStoreService;

    @Autowired
    private MapperFacade mapperFacade;

    @Autowired
    private QrcodeFeignClient qrcodeFeignClient;

    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private StoreExcelManager storeExcelManager;

    @Value("${invite.qrCode.directory.path}")
    private String inviteQrCodeDirectoryPath;

    @Autowired
    private DownloadCenterFeignClient downloadCenterFeignClient;
    @Autowired
    private ApplicationContext applicationContext;

    @PostMapping("/page")
    @ApiOperation(value = "获取列表", notes = "分页获取列表")
    public ServerResponseEntity<PageVO<TzStore>> page(@Valid PageDTO pageDTO, @RequestBody StoreQueryParamDTO storeQueryParamDTO) {
        PageVO<TzStore> tzStorePage = tzStoreService.page(pageDTO, storeQueryParamDTO);
        return ServerResponseEntity.success(tzStorePage);
    }

    @PostMapping("/storeListByCodes")
    @ApiOperation(value = "批量粘贴(门店编号)", notes = "批量粘贴(门店编号)")
    public ServerResponseEntity<List<TzStore>> storeListByCodes(@RequestBody StoreQueryParamDTO storeQueryParamDTO) {
        List<TzStore> tzStorePage = tzStoreService.storeListByCodes(storeQueryParamDTO);
        return ServerResponseEntity.success(tzStorePage);
    }

    @GetMapping
    @ApiOperation(value = "获取", notes = "根据storeId获取")
    public ServerResponseEntity<TzStore> getByStoreId(@RequestParam(value = "storeId", required = false) Long storeId,
                                                      @RequestParam(value = "orgId", required = false) Long orgId) {
        if (Objects.isNull(storeId) && Objects.isNull(orgId)) {
            return ServerResponseEntity.fail(ResponseEnum.HTTP_MESSAGE_NOT_READABLE);
        }
        TzStore store = null;
        if (!Objects.isNull(storeId)) {
            store = tzStoreService.getByStoreId(storeId);
        } else if (!Objects.isNull(orgId)) {
            store = tzStoreService.getByOrgId(orgId);
        }

        return ServerResponseEntity.success(store);
    }

    @PostMapping
    @ApiOperation(value = "保存", notes = "保存")
    public ServerResponseEntity<Void> save(@Valid @RequestBody TzStoreDTO tzStoreDTO) {
        tzStoreService.save(tzStoreDTO);
        return ServerResponseEntity.success();
    }

    @PutMapping
    @ApiOperation(value = "更新", notes = "更新")
    public ServerResponseEntity<CloseStoreStaffVO> update(@Valid @RequestBody TzStoreDTO tzStoreDTO) {
        //先保存组织节点
        CloseStoreStaffVO result = tzStoreService.update(tzStoreDTO);
        tzStoreService.removeCacheByStoreId(tzStoreDTO.getStoreId());
        return ServerResponseEntity.success(result);
    }

    @DeleteMapping
    @ApiOperation(value = "删除", notes = "根据id删除")
    public ServerResponseEntity<Void> delete(@RequestParam Long storeId) {
        tzStoreService.deleteById(storeId);
        return ServerResponseEntity.success();
    }

    @PostMapping("/selected/list")
    @ApiOperation(value = "已选择门店列表", notes = "分页已选择门店列表")
    public ServerResponseEntity<PageVO<SelectedStoreVO>> selectedPage(@Valid PageDTO pageDTO, @RequestBody StoreSelectedParamDTO storeSelectedParamDTO) {
        PageVO<SelectedStoreVO> tzStorePage = tzStoreService.selectedPage(pageDTO, storeSelectedParamDTO);
        return ServerResponseEntity.success(tzStorePage);
    }

    @PostMapping("/org/list")
    @ApiOperation(value = "根据组织节点ID查询门店列表", notes = "根据组织节点Id查询门店列表")
    public ServerResponseEntity<List<TzStore>> orgList(@RequestParam Long orgId) {
        List<TzStore> tzStores = tzStoreService.listByOrgId(orgId);
        return ServerResponseEntity.success(tzStores);
    }

    @PostMapping("/org/batchList")
    @ApiOperation(value = "批量根据组织节点ID查询门店列表", notes = "批量根据组织节点ID查询门店列表")
    public ServerResponseEntity<List<TzStore>> orgLists(@RequestParam String orgIds) {
//        List<TzStore> tzStores = tzStoreService.listByOrgId(orgId);
        List<TzStore> tzStores = tzStoreService.listByOrgIds(orgIds);
        return ServerResponseEntity.success(tzStores);
    }

    @GetMapping("/downloadInviteQrCode")
    @ApiOperation(value = "邀请码下载", notes = "邀请码下载")
    public ServerResponseEntity<Void> downloadInviteQrCode(@RequestBody StoreQueryParamDTO storeQueryParamDTO,
                                                           HttpServletResponse response){
        ZipOutputStream zos = null;
        FileInputStream in = null;
        try {
            List<File> fileList = new ArrayList<>();
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
            PageDTO pageDTO = new PageDTO();
            pageDTO.setPageSize(1);
            pageDTO.setPageSize(500);
            PageVO<TzStore> tzStorePage = tzStoreService.page(pageDTO, storeQueryParamDTO);
            Integer totalPage = tzStorePage.getPages();
            if (totalPage > 0) {
                getFiles(fileList, tzStorePage.getList());
                for (Integer i = 2; i <= totalPage; i++) {
                    pageDTO.setPageSize(i);
                    getFiles(fileList, tzStorePage.getList());
                }
            }
            String fileName = "门店邀请码_" + dateFormat.format(new Date());
            String zipName = new String(fileName.getBytes(),"ISO-8859-1") + ".zip";
            response.setContentType("application/octet-stream");
            response.setHeader("content-type", "application/octet-stream");
            response.setHeader("Content-disposition","attachment; filename="+zipName);
            OutputStream outputStream = response.getOutputStream();
            zos = new ZipOutputStream(outputStream);
            if (!CollectionUtils.isEmpty(fileList)) {
                for (File file : fileList) {
                    byte[] buf = new byte[1024];
                    zos.putNextEntry(new ZipEntry(file.getName()));
                    int len;
                    in = new FileInputStream(file);
                    while ((len = in.read(buf)) != -1) {
                        zos.write(buf, 0, len);
                    }
                }
            }
        } catch (Exception e){
            log.error("downloadInviteQrCode Exception",e);
        } finally {
            try {
                if (null != in) {
                    in.close();
                }
                if (null != zos) {
                    zos.flush();
                    zos.close();
                }
            } catch (IOException e) {
                log.error("downloadInviteQrCode IOException",e);
            }
        }
        return ServerResponseEntity.success();
    }

    private void getFiles(List<File> fileList, List<TzStore> tzStoreList) {
        if (!CollectionUtils.isEmpty(tzStoreList)) {
            tzStoreList.stream().forEach(tzStore -> {
                String fileName = tzStore.getStationName() + "_" + tzStore.getStoreCode();
                File newFile = new File(inviteQrCodeDirectoryPath  + File.separator + fileName);
                if (!newFile.exists()) {
                    String scene = "s=" + NumberTo64.to64(tzStore.getStoreId());
                    ServerResponseEntity<File> qrCodeResp = qrcodeFeignClient.getWxaCodeFile(scene,
                            "packageUser/pages/register-member/register-member", null);
                    if (qrCodeResp.isSuccess()) {
                        File file = qrCodeResp.getData();
                        try {
                            newFile.createNewFile();
                            FileInputStream fin = new FileInputStream(file);
                            FileOutputStream fout = new FileOutputStream(newFile, true);
                            byte [] b = new byte[1024];
                            while (fin.read(b) != -1) {
                                fout.write(b);
                            }
                            fin.close();
                            fout.close();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
                fileList.add(newFile);
            });
        }
    }
    //获取登录账号的可访问门店列表
    @GetMapping("storeList")
    @ApiOperation(value = "根据登陆账号信息查询可访问门店列鞭炮", notes = "根据组织节点Id查询门店列表")
    public ServerResponseEntity<PageVO<TzStore>> storeList(@Valid PageDTO pageDTO , @RequestParam(value = "keyword" ,required = false) String keyword) {
        Long userId = AuthUserContext.get().getUserId();
        SysUserVO byUserId = sysUserService.getByUserId(userId);
        PageVO<TzStore> tzStores = tzStoreService.storePlatPage(Long.parseLong(byUserId.getOrgId()),keyword,pageDTO);
        return ServerResponseEntity.success(tzStores);
    }

    @PostMapping("/sold_excel")
    @ApiOperation(value = "导出门店", notes = "导出门店")
    public ServerResponseEntity<String> soldStore(@RequestBody StoreQueryParamDTO storeQueryParamDTO, HttpServletResponse response) {
        try {
//            tzStoreService.soldStore(storeQueryParamDTO,response);

            CalcingDownloadRecordDTO downloadRecordDTO=new CalcingDownloadRecordDTO();
            downloadRecordDTO.setDownloadTime(new Date());
            downloadRecordDTO.setFileName(SoldTzStoreVO.EXCEL_NAME);
            downloadRecordDTO.setCalCount(0);
            downloadRecordDTO.setOperatorName(AuthUserContext.get().getUsername());
            downloadRecordDTO.setOperatorNo(""+ AuthUserContext.get().getUserId());
            ServerResponseEntity serverResponseEntity=downloadCenterFeignClient.newCalcingTask(downloadRecordDTO);
            Long downLoadHisId=null;
            if(serverResponseEntity.isSuccess()){
                downLoadHisId=Long.parseLong(serverResponseEntity.getData().toString());
            }

            SoldStoreEvent soldSpuEvent=new SoldStoreEvent();
            soldSpuEvent.setStoreQueryParamDTO(storeQueryParamDTO);
            soldSpuEvent.setDownLoadHisId(downLoadHisId);
            applicationContext.publishEvent(soldSpuEvent);

            return ServerResponseEntity.success("操作成功，请转至下载中心下载");
        }catch (Exception e){
            log.error("导出门店信息: "+e.getMessage());
            return ServerResponseEntity.showFailMsg("操作失败");
        }
    }

    @PostMapping("/importStore")
    @ApiOperation(value = "导入门店", notes = "导入门店")
    public ServerResponseEntity<UploadExcelVO> importStore(@RequestParam(value = "orgId",required = true)Long orgId,
                                                           @RequestParam(value = "excelFile",required = true) MultipartFile file) {
        if(file == null) {
            throw new LuckException("网络繁忙，请稍后重试");
        }
        String info=storeExcelManager.importStoreExcel(file,orgId);
        UploadExcelVO uploadExcelVO=new UploadExcelVO();
        uploadExcelVO.setDate(info);
        if(StrUtil.isNotBlank(info)) uploadExcelVO.setCount("1");
        return ServerResponseEntity.success(uploadExcelVO);
    }

    @PostMapping("/updateShow")
    @ApiOperation(value = "批量修改门店C端展示", notes = "批量修改门店C端展示")
    public ServerResponseEntity updateShow(@RequestBody List<Long> storeIds) {
        tzStoreService.updateShows(storeIds,1);
        return ServerResponseEntity.success("操作成功");
    }

    @PostMapping("/updateNoShow")
    @ApiOperation(value = "批量修改门店C端不展示", notes = "批量修改门店C端不展示")
    public ServerResponseEntity updateNoShow(@RequestBody List<Long> storeIds) {
        tzStoreService.updateShows(storeIds,0);
        return ServerResponseEntity.success("操作成功");
    }

    @GetMapping("/test")
    @ApiOperation(value = "批量修改门店经纬度", notes = "批量修改门店经纬度")
    public ServerResponseEntity updateStore() {
        tzStoreService.listByStoreLatLng();
        return ServerResponseEntity.success("操作成功");
    }

}
