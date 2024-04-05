package com.mall4j.cloud.platform.dto;

import javax.validation.constraints.NotBlank;

/**
 * 分销招募推广配置
 * @author cl
 * @date 2021-08-06 09:26:11
 */
public class DistributionRecruitConfigDTO {


    /**
     * 推广图
     */
    private String pic;

    /**
     * 推广标题
     */
    @NotBlank(message = "推广标题不能为空")
    private String title;

    /**
     * 推广内容
     */
    @NotBlank(message = "推广内容不能为空")
    private String content;

    /**
     * 推广链接
     */
    private String url;

    /**
     * 推广开关： 状态（0下线 1上线）
     */
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

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    @Override
    public String toString() {
        return "DistributionRecruitConfigDTO{" +
                "pic='" + pic + '\'' +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", url='" + url + '\'' +
                ", state=" + state +
                '}';
    }
}
