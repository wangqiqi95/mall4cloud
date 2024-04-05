package com.mall4j.cloud.biz.model.live;

import com.mall4j.cloud.common.model.BaseModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class RoomToolsVO {
        /**
         * 是否关闭客服 0 开启，1 关闭
         */
        @ApiModelProperty(value = "是否关闭客服 0 开启，1 关闭")
        private Integer closeKf;
        /**
         * 是否关闭点赞  0 开启，1 关闭
         */
        @ApiModelProperty(value = "是否关闭点赞  0 开启，1 关闭")
        private Integer closeLike;
        /**
         * 是否关闭货架  0 开启，1 关闭
         */
        @ApiModelProperty(value = " 是否关闭货架  0 开启，1 关闭")
        private Integer closeGoods;
        /**
         * 是否关闭评论  0 开启，1 关闭
         */
        @ApiModelProperty(value = "是否关闭评论  0 开启，1 关闭")
        private Integer closeComment;
        /**
         * 是否关闭回放  0 开启，1 关闭
         */
        @ApiModelProperty(value = " 是否关闭回放  0 开启，1 关闭")
        private Integer closeReplay;
        /**
         * 是否关闭分享  0 开启，1 关闭
         */
        @ApiModelProperty(value = "是否关闭分享  0 开启，1 关闭")
        private Integer closeShare;
    }