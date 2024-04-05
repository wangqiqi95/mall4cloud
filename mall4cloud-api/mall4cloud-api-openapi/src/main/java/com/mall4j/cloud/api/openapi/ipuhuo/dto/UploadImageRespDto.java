package com.mall4j.cloud.api.openapi.ipuhuo.dto;

import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * 类描述：上传图片请求响应
 */
public class UploadImageRespDto implements Serializable, BaseResultDto {

	private static final long serialVersionUID = -6642514641638848943L;
	@ApiModelProperty(value = "店铺Id", required = true)
    private Integer shopid;

    @ApiModelProperty(value = "图片地址", required = true)
    private String imgurl;

    public UploadImageRespDto() {
    }

    public UploadImageRespDto(Integer shopid, String imgurl) {
        this.shopid = shopid;
        this.imgurl = imgurl;
    }

    public Integer getShopid() {
        return shopid;
    }

    public void setShopid(Integer shopid) {
        this.shopid = shopid;
    }

    public String getImgurl() {
        return imgurl;
    }

    public void setImgurl(String imgurl) {
        this.imgurl = imgurl;
    }

    @Override
    public String toString() {
        return "UploadImageRespDto{" + "shopid=" + shopid + ", imgurl='" + imgurl + '\'' + '}';
    }
}
