package com.mall4j.cloud.biz.dto.cp.wx;

import com.mall4j.cloud.biz.vo.cp.AttachMentVO;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class SendWelcomeMsg implements Serializable {

    public String welcome_code;
    public WelcomeText text;
    public List<AttachMentVO> attachments;

    public void setText(String text){
         this.text = new WelcomeText(text);
    }
    @Data
    public class  WelcomeText{
        private  String content;
        public WelcomeText(String content){this.content=content;}
    }
}
