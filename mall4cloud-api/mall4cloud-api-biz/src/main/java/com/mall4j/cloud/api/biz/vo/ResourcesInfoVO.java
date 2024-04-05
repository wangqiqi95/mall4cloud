package com.mall4j.cloud.api.biz.vo;

import io.swagger.annotations.ApiModelProperty;

/**
 * @author cl
 * @date 2021-08-13 10:02:26
 */
public class ResourcesInfoVO {

    @ApiModelProperty(value = "服务器域名url",required=true)
    private String resourcesUrl;

    @ApiModelProperty(value = "文件上传的路径",required=true)
    private String filePath;

    public String getResourcesUrl() {
        return resourcesUrl;
    }

    public void setResourcesUrl(String resourcesUrl) {
        this.resourcesUrl = resourcesUrl;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    @Override
    public String toString() {
        return "ResourcesInfoVO{" +
                "resourcesUrl='" + resourcesUrl + '\'' +
                ", filePath='" + filePath + '\'' +
                '}';
    }
}
