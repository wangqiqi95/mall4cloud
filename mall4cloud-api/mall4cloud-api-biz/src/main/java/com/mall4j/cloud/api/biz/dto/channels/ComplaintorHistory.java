package com.mall4j.cloud.api.biz.dto.channels;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class ComplaintorHistory {
    @ApiModelProperty(value = "历史操作类型,见ItemType" +
            "1\t申请平台介入\n" +
            "2\t用户留言\n" +
            "3\t商家留言\n" +
            "4\t提交投诉成功\n" +
            "5\t投诉已取消\n" +
            "6\t商家已超时\n" +
            "7\t用户补充凭证\n" +
            "8\t商家补充凭证\n" +
            "10\t待商家处理纠纷\n" +
            "11\t待平台处理\n" +
            "12\t取消平台介入\n" +
            "13\t平台处理中\n" +
            "14\t待用户补充凭证\n" +
            "16\t待商家补充凭证\n" +
            "18\t待双方补充凭证\n" +
            "20\t待商家确认\n" +
            "21\t商家申诉中\n" +
            "22\t调解完成\n" +
            "23\t待平台核实\n" +
            "24\t重新退款中\n" +
            "26\t调解关闭\n" +
            "30\t平台判定用户责任\n" +
            "31\t平台判定商家责任\n" +
            "32\t平台判定双方责任\n" +
            "33\t平台判定无责任\n" +
            "34\t平台判定申诉无效\n" +
            "35\t平台判定申诉生效\n" +
            "36\t平台判定退款有效\n" +
            "37\t平台判定退款无效\n" +
            "50\t用户发起退款\n" +
            "51\t商家拒绝退款\n" +
            "52\t用户取消申请\n" +
            "56\t待买家退货\n" +
            "57\t退货退款关闭\n" +
            "58\t待商家收货\n" +
            "59\t商家逾期未退款\n" +
            "60\t退款完成\n" +
            "61\t退货退款完成\n" +
            "62\t平台退款中\n" +
            "63\t平台退款失败\n" +
            "64\t待用户确认")
    private Integer item_type;
    @ApiModelProperty(value = "操作时间，Uinx时间戳")
    private Long time;
    @ApiModelProperty(value = "用户联系电话")
    private String phone_number;
    @ApiModelProperty(value = "相关文本内容")
    private String content;
    @ApiModelProperty(value = "相关图片media_id列表")
    private List<String> media_id_list;

    @ApiModelProperty(value = "售后类型, 1-仅退款 2-退货退款")
    private Integer after_sale_type;

    @ApiModelProperty(value = "售后原因，见AfterSaleReason" +
            "1\t拍错/多拍\n" +
            "2\t不想要了\n" +
            "3\t无快递信息\n" +
            "4\t包裹为空\n" +
            "5\t已拒签包裹\n" +
            "6\t快递长时间未送达\n" +
            "7\t与商品描述不符\n" +
            "8\t质量问题\n" +
            "9\t卖家发错货\n" +
            "10\t三无产品\n" +
            "11\t假冒产品\n" +
            "12\t其它")
    private Integer after_sale_reason;
}
