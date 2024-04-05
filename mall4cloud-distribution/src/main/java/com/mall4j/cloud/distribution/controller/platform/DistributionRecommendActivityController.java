package com.mall4j.cloud.distribution.controller.platform;

import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.exception.LuckException;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.common.security.AuthUserContext;
import com.mall4j.cloud.distribution.dto.DistributionRecommendActivityDTO;
import com.mall4j.cloud.distribution.dto.DistributionRecommendActivityQueryDTO;
import com.mall4j.cloud.distribution.dto.DistributionRecommendActivityStatusModifyDTO;
import com.mall4j.cloud.distribution.service.DistributionRecommendActivityService;
import com.mall4j.cloud.distribution.vo.DistributionRecommendActivityDetailVO;
import com.mall4j.cloud.distribution.vo.DistributionRecommendActivityVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 主推商品活动 API控制器
 *
 * @author EricJeppesen
 * @date 2022/10/18 16:50
 */
@RestController("platformDistributionRecommendActivityController")
@RequestMapping("/p")
@Api(tags = "平台端-主推商品活动")
public class DistributionRecommendActivityController {

    private DistributionRecommendActivityService distributionRecommendActivityService;

    @Autowired
    public void setDistributionRecommendActivityService(DistributionRecommendActivityService distributionRecommendActivityService) {
        this.distributionRecommendActivityService = distributionRecommendActivityService;
    }

    /**
     * 创建主推商品活动
     *
     * @param distributionRecommendActivityDTO 创建数据
     * @return 空数据成功视图
     */
    @PostMapping("distribution_recommend_activity")
    @ApiOperation(value = "创建主推商品活动-主推商品活动", notes = "创建主推商品活动")
    public ServerResponseEntity<Void> createRecommendActivity(@RequestBody DistributionRecommendActivityDTO distributionRecommendActivityDTO) {
        Long userId = AuthUserContext.get().getUserId();
        String username = AuthUserContext.get().getUsername();
        try {
            distributionRecommendActivityService.createActivity(distributionRecommendActivityDTO, userId, username);
            return ServerResponseEntity.success();
        } catch (LuckException e) {
            e.printStackTrace();
            return ServerResponseEntity.showFailMsg(e.getMessage());
        }
    }

    /**
     * 修改主推商品活动
     *
     * @param distributionRecommendActivityDTO 修改内容
     * @return 空数据成功视图
     */
    @PutMapping("distribution_recommend_activity")
    @ApiOperation(value = "修改主推商品活动-主推商品活动", notes = "修改主推商品活动")
    public ServerResponseEntity<Void> modifyRecommendActivity(@RequestBody DistributionRecommendActivityDTO distributionRecommendActivityDTO) {
        Long userId = AuthUserContext.get().getUserId();
        String username = AuthUserContext.get().getUsername();
        try {
            distributionRecommendActivityService.modifyActivity(distributionRecommendActivityDTO, userId, username);
            return ServerResponseEntity.success();
        } catch (LuckException e) {
            e.printStackTrace();
            return ServerResponseEntity.showFailMsg(e.getMessage());
        }
    }

    /**
     * 查询活动详情
     *
     * @param id 主推商品活动标识
     * @return 单个活动详情
     */
    @GetMapping("distribution_recommend_activity/{id}")
    @ApiOperation(value = "查询主推商品活动详情-主推商品活动", notes = "查询主推商品活动详情")
    public ServerResponseEntity<DistributionRecommendActivityDetailVO> getRecommendActivityById(@PathVariable Long id) {
        try {
            return ServerResponseEntity.success(distributionRecommendActivityService.getById(id));
        } catch (LuckException e) {
            e.printStackTrace();
            return ServerResponseEntity.showFailMsg(e.getMessage());
        }
    }

    /**
     * 查询列表
     *
     * @param queryDTO 主推商品活动条件
     * @return 分页数据结果集
     */
    @PostMapping ("distribution_recommend_activities")
    @ApiOperation(value = "分页查询主推商品活动列表-主推商品活动", notes = "分页查询主推商品活动列表")
    public ServerResponseEntity<PageVO<DistributionRecommendActivityVO>> listRecommendActivities(@RequestBody  DistributionRecommendActivityQueryDTO queryDTO) {
        try {
            return ServerResponseEntity.success(distributionRecommendActivityService.listActivity(queryDTO.getPageDTO(), queryDTO));
        } catch (LuckException e) {
            e.printStackTrace();
            return ServerResponseEntity.showFailMsg(e.getMessage());
        }
    }

    /**
     * 修改主推商品活动状态
     *
     * @param distributionRecommendActivityStatusModifyDTO 修改内容
     * @return 空数据成功视图
     */
    @PutMapping("distribution_recommend_activity/activity_status")
    @ApiOperation(value = "修改主推商品活动状态-主推商品活动", notes = "修改主推商品活动状态")
    public ServerResponseEntity<Void> modifyActivityStatus(@RequestBody DistributionRecommendActivityStatusModifyDTO distributionRecommendActivityStatusModifyDTO) {
        try {
            distributionRecommendActivityService.modifyActivityStatus(distributionRecommendActivityStatusModifyDTO.getId(), distributionRecommendActivityStatusModifyDTO.getActivityStatus());
            return ServerResponseEntity.success();
        } catch (LuckException e) {
            e.printStackTrace();
            return ServerResponseEntity.showFailMsg(e.getMessage());
        }
    }


}
