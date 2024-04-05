package com.mall4j.cloud.api.platform.vo;

import io.swagger.annotations.ApiModelProperty;

/**
 * @author cl
 * @date 2021-08-06 14:20:02
 */
public class DistributionRecruitConfigApiVO {


    @ApiModelProperty("推广封面")
    private String pic;

    @ApiModelProperty("推广标题")
    private String title;

    @ApiModelProperty("推广内容")
    private String content;

    /**
     * 状态（0下线 1上线）
     */
    @ApiModelProperty("状态 0关 1开")
    private Integer state;

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    @Override
    public String toString() {
        return "DistributionRecruitConfigApiVO{" +
                "pic='" + pic + '\'' +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", state=" + state +
                '}';
    }
}
