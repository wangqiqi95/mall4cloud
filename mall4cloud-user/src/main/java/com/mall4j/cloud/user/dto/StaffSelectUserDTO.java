package com.mall4j.cloud.user.dto;

import com.mall4j.cloud.common.database.dto.PageDTO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

/**
 * 导购端-会员-所有会员筛选条件查询入参
 * @author Peter_Tan
 * @date 2023/02/08
 */
@Data
public class StaffSelectUserDTO extends PageDTO {

    private Long staffId;

    /**
     * 加友状态筛选条件
     */
    @ApiModelProperty("是否加好友 0-否 1-是")
    private Integer addWechat;

    /**
     * 第一个标签信息
     */
    private Long firstTagId;

    /**
     * 会员标签列表大小
     */
    private Integer tagIdListSize;

    /**
     * 会员标签筛选条件
     */
    @ApiModelProperty("会员标签筛选条件")
    private List<Long> tagId;

    /**
     * 优惠券类别筛选条件
     */
    @ApiModelProperty(value = "是否有优惠券 0否1是  当标识为1时，下面的参数才有效", required = true)
    private Integer hasCouponFlag;

    @ApiModelProperty(value = "到期开始时间")
    private String beginTime;

    @ApiModelProperty(value = "到期结束时间")
    private String endTime;

    @ApiModelProperty(value = "优惠券名称")
    private String couponName;

    @ApiModelProperty(value = "到期时间")
    private Integer endDay;

    @ApiModelProperty(value = "优惠券图片标识（0：默认/1：礼品券/2：生日礼/3:员工券/4:升级礼/5：保级礼/6:服务券/7:满减券/8:折扣券/9:入会券/10:免邮券）")
    private List<Integer> couponPictures;

    @ApiModelProperty(value = "优惠券种类（0：普通优惠券/1：包邮券/2：券码导入/3：企业券）")
    private List<Integer> kinds;

    @ApiModelProperty(value = "userIds")
    private List<Long> userIds;

    @ApiModelProperty(value = "vipCodes")
    private List<String> vipCodes;

    @ApiModelProperty(value = "crmCouponIds")
    private List<String> crmCouponIds;


}
