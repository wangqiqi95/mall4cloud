package com.mall4j.cloud.platform.controller.platform;

import cn.hutool.core.util.StrUtil;
import com.mall4j.cloud.api.biz.dto.WXShortLinkDTO;
import com.mall4j.cloud.api.biz.feign.ShortLinkClient;
import com.mall4j.cloud.api.leaf.feign.SegmentFeignClient;
import com.mall4j.cloud.common.cache.util.RedisUtil;
import com.mall4j.cloud.common.constant.Constant;
import com.mall4j.cloud.common.constant.DistributedIdKey;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.response.ResponseEnum;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.common.security.AuthUserContext;
import com.mall4j.cloud.platform.config.CustomConfig;
import com.mall4j.cloud.platform.dto.StoreRenovationDTO;
import com.mall4j.cloud.platform.dto.StoreRenovationMouduldParamDTO;
import com.mall4j.cloud.platform.dto.StoreRenovationPreviewDTO;
import com.mall4j.cloud.platform.dto.StoreRenovationSearchDTO;
import com.mall4j.cloud.platform.model.RenoApply;
import com.mall4j.cloud.platform.model.StoreRenovation;
import com.mall4j.cloud.platform.service.RenoApplyService;
import com.mall4j.cloud.platform.service.StoreRenovationService;
import com.mall4j.cloud.platform.vo.StoreRenovationMouduleVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import ma.glasnost.orika.MapperFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Date;
import java.util.Objects;

/**
 * 店铺装修信息
 *
 * @author FrozenWatermelon
 * @date 2022-01-18 10:46:32
 */
@Slf4j
@RestController("platformStoreRenovationController")
@RequestMapping("/p/store_renovation")
@Api(tags = "platform-店铺装修信息")
public class StoreRenovationController {

    @Autowired
    private StoreRenovationService storeRenovationService;
    @Autowired
    private RenoApplyService renoApplyService;

    @Autowired
    private MapperFacade mapperFacade;

    @Autowired
    private SegmentFeignClient segmentFeignClient;

    @Autowired
    private ShortLinkClient shortLinkClient;

    @Autowired
    private CustomConfig customConfig;

    public static final String STORE_RENOVATION_PREVIEW = "STORE_RENOVATION_PREVIEW";

    @GetMapping("/page")
    @ApiOperation(value = "获取店铺装修信息列表", notes = "分页获取店铺装修信息列表")
    public ServerResponseEntity<PageVO<StoreRenovation>> page(StoreRenovationSearchDTO searchDTO) {
        //增加适用门店 及状态筛选
        PageVO<StoreRenovation> storeRenovationPage = storeRenovationService.page(searchDTO);
        return ServerResponseEntity.success(storeRenovationPage);
    }

    @PostMapping("/module/page")
    @ApiOperation(value = "组件接口列表", notes = "组件接口")
    public ServerResponseEntity<PageVO<StoreRenovationMouduleVO>> modulePage(@Valid PageDTO pageDTO,@RequestBody StoreRenovationMouduldParamDTO paramDTO) {
        PageVO<StoreRenovationMouduleVO> storeRenovationPage = storeRenovationService.modulePage(pageDTO,paramDTO);
        return ServerResponseEntity.success(storeRenovationPage);
    }

    @GetMapping
    @ApiOperation(value = "获取店铺装修信息", notes = "根据renovationId获取店铺装修信息")
    public ServerResponseEntity<StoreRenovation> getByRenovationId(@RequestParam Long renovationId) {
        return ServerResponseEntity.success(storeRenovationService.getByRenovationId(renovationId));
    }

    @PostMapping
    @ApiOperation(value = "保存店铺装修信息", notes = "保存店铺装修信息")
    public ServerResponseEntity<Void> save(@Valid @RequestBody StoreRenovationDTO storeRenovationDTO) {
        StoreRenovation storeRenovation = mapperFacade.map(storeRenovationDTO, StoreRenovation.class);
        storeRenovation.setRenovationId(null);
        if (CollectionUtils.isEmpty(storeRenovationDTO.getRenoApplyStoreList())){
            storeRenovation.setStoreId(Constant.MAIN_SHOP);
        }
        ServerResponseEntity<Long> storeRenovationEntity  = segmentFeignClient.getSegmentId(DistributedIdKey.MALL4CLOUD_STORE_RENO);
        if (storeRenovationEntity.isSuccess()){
            storeRenovation.setRenovationId(storeRenovationEntity.getData());

            // 自动为保存的微页面生成短链接
            WXShortLinkDTO dto = new WXShortLinkDTO();
            dto.setPagePath(customConfig.getMicroPagePrefix() + storeRenovation.getRenovationId());
            ServerResponseEntity<String> shortLinkResp = shortLinkClient.generateShortLink(dto);
            if (shortLinkResp.isSuccess()) {
                storeRenovation.setShortLink(shortLinkResp.getData());
            }
        }
        storeRenovation.setCreateId(AuthUserContext.get().getUserId());
        storeRenovation.setCreateName(AuthUserContext.get().getUsername());
        if(Objects.nonNull(storeRenovation.getPushType())){
            if(storeRenovation.getPushType()==0){//立即发布
                storeRenovation.setStatus(1);
                storeRenovation.setPushStatus(1);
                storeRenovation.setPushBy(AuthUserContext.get().getUsername());
                storeRenovation.setPushTime(new Date());
            }
            if(storeRenovation.getPushType()==1){//定时发布
                storeRenovation.setStatus(0);
            }
        }
        storeRenovationService.save(storeRenovation);
        //保存适用门店参数
        if (!CollectionUtils.isEmpty(storeRenovationDTO.getRenoApplyStoreList())){
            storeRenovationDTO.getRenoApplyStoreList().forEach(storeId ->{
                RenoApply renoApply = new RenoApply();
                renoApply.setRenoId(storeRenovation.getRenovationId());
                renoApply.setStoreId(storeId);
                renoApply.setCreateTime(new Date());
                renoApply.setIsDeleted(0);
                renoApplyService.save(renoApply);
            });
        }
        return ServerResponseEntity.success();
    }

    @PostMapping("/putStoreRPreview")
    @ApiOperation(value = "保存店铺装修预览信息", notes = "保存店铺装修预览信息")
    public ServerResponseEntity<Void> putStoreRPreview(@Valid @RequestBody StoreRenovationPreviewDTO previewDTO) {
        if (StrUtil.isNotBlank(previewDTO.getContent())) {
            previewDTO.getContent().replace("https://xcx-prd.oss-cn-shanghai.aliyuncs.com","https://xcx-prd.skechers.cn");
        }
        RedisUtil.set(STORE_RENOVATION_PREVIEW,previewDTO.getContent(),86400);
        return ServerResponseEntity.success();
    }

    @PutMapping
    @ApiOperation(value = "更新店铺装修信息", notes = "更新店铺装修信息")
    public ServerResponseEntity<Void> update(@Valid @RequestBody StoreRenovationDTO storeRenovationDTO) {
        StoreRenovation storeRenovation = mapperFacade.map(storeRenovationDTO, StoreRenovation.class);
        storeRenovation.setUpdateId(AuthUserContext.get().getUserId());
        storeRenovation.setUpdateName(AuthUserContext.get().getUsername());
        storeRenovation.setUpdateTime(new Date());
        if (!CollectionUtils.isEmpty(storeRenovationDTO.getRenoApplyStoreList())){
            storeRenovation.setStoreId(0L);
        }else{
            storeRenovation.setStoreId(Constant.MAIN_SHOP);
        }
        if (StrUtil.isNotBlank(storeRenovation.getContent())) {
            storeRenovation.getContent().replace("https://xcx-prd.oss-cn-shanghai.aliyuncs.com","https://xcx-prd.skechers.cn");
        }

        if(Objects.nonNull(storeRenovation.getPushType())){
            if(storeRenovation.getPushType()==0){//立即发布
                storeRenovation.setStatus(1);
                storeRenovation.setPushStatus(1);
                storeRenovation.setPushBy(AuthUserContext.get().getUsername());
                storeRenovation.setPushTime(new Date());
            }
            if(storeRenovation.getPushType()==1){//定时发布
                storeRenovation.setStatus(0);
            }
        }

        storeRenovationService.update(storeRenovation);

        renoApplyService.deleteByRenoId(storeRenovation.getRenovationId());

        //保存适用门店参数
        if (!CollectionUtils.isEmpty(storeRenovationDTO.getRenoApplyStoreList())){
            storeRenovationDTO.getRenoApplyStoreList().forEach(storeId ->{
                RenoApply renoApply = new RenoApply();
                renoApply.setRenoId(storeRenovation.getRenovationId());
                renoApply.setStoreId(storeId);
                renoApply.setCreateTime(new Date());
                renoApply.setIsDeleted(0);
                renoApplyService.save(renoApply);
            });
        }

        //保存适用门店参数
        if (!CollectionUtils.isEmpty(storeRenovationDTO.getRenoApplyStoreList())){
                renoApplyService.updateStoreId(storeRenovationDTO.getRenoApplyStoreList(),storeRenovationDTO.getRenovationId());
        }
        return ServerResponseEntity.success();
    }

    @DeleteMapping
    @ApiOperation(value = "删除店铺装修信息", notes = "根据店铺装修信息id删除店铺装修信息")
    public ServerResponseEntity<Void> delete(@RequestParam Long renovationId) {
        storeRenovationService.deleteById(renovationId);
        return ServerResponseEntity.success();
    }

    @DeleteMapping("online/deleteByRenoIdAndStroreId")
    @ApiOperation(value = "删除店铺装修适用门店", notes = "根据店铺装修信息id和门店id删除适用门店")
    public ServerResponseEntity<Void> deleteByRenoIdAndStroreId(@RequestParam Long renovationId,@RequestParam Long storeId) {
        renoApplyService.deleteByRenoIdAndStroreId(renovationId,storeId);
        return ServerResponseEntity.success();
    }

    @PutMapping("online/{renovationId}")
    @ApiOperation(value = "上线", notes = "上线")
    public ServerResponseEntity<Boolean> online(@PathVariable Long renovationId) {
        StoreRenovation byRenovationId = storeRenovationService.getByRenovationId(renovationId);
        if (Objects.nonNull(byRenovationId)){
            log.info("手动操作上线 上线人:{} id:{}",AuthUserContext.get().getUsername(),renovationId);
            return ServerResponseEntity.success( storeRenovationService.online(renovationId));
        }
        return ServerResponseEntity.fail(ResponseEnum.DATA_ERROR,false);

    }


    @PutMapping("offline/{renovationId}")
    @ApiOperation(value = "下线", notes = "下线")
    public ServerResponseEntity<Boolean> offline(@PathVariable Long renovationId) {
        StoreRenovation byRenovationId = storeRenovationService.getByRenovationId(renovationId);
        if (Objects.nonNull(byRenovationId)){
            log.info("手动操作下线 下线人:{} id:{}",AuthUserContext.get().getUsername(),renovationId);
            return ServerResponseEntity.success( storeRenovationService.offline(renovationId));
        }
        return ServerResponseEntity.fail(ResponseEnum.DATA_ERROR,false);
    }
}
