package com.mall4j.cloud.api.biz.dto.channels.response;

import com.mall4j.cloud.api.biz.dto.channels.ComplaintorHistory;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class EcGetcomplaintorderResponse extends EcBaseResponse {
    @ApiModelProperty(value = "订单号")
    private Long order_id;
    @ApiModelProperty(value = "售后单号")
    private Long after_sale_order_id;
    @ApiModelProperty(value = "纠纷单状态" +
            "100\t待商家处理纠纷\n" +
            "101\t待客服处理\n" +
            "102\t取消客服介入\n" +
            "103\t客服处理中\n" +
            "104\t待用户补充凭证\n" +
            "105\t用户已补充凭证\n" +
            "106\t待商家补充凭证\n" +
            "107\t商家已补充凭证\n" +
            "108\t待双方补充凭证\n" +
            "109\t双方补充凭证超时\n" +
            "110\t待商家确认\n" +
            "111\t商家申诉中\n" +
            "112\t调解完成\n" +
            "113\t待客服核实\n" +
            "114\t重新退款中\n" +
            "115\t退款核实完成\n" +
            "116\t调解关闭\n" +
            "305\t用户补充凭证超时\n" +
            "307\t商家补充凭证超时\n")
    private Integer status;

    @ApiModelProperty(value = "纠纷历史")
    private List<ComplaintorHistory> history;

}
