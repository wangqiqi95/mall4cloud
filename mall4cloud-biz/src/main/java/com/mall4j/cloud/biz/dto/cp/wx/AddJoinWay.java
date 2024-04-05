package com.mall4j.cloud.biz.dto.cp.wx;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class AddJoinWay implements Serializable {
    public String config_id;
    public Integer scene =2 ;
    public String remark;
    public Integer auto_create_room=1;
    public String room_base_name;
    public Integer room_base_id =1;
    public List<String> chat_id_list;
    public String state;
    public AddJoinWay(String configId, String roomName, List<String> chatList, String state){
        this.room_base_name = roomName;
        this.chat_id_list = chatList;
        this.state = state;
        this.config_id = configId;
    }

    public AddJoinWay(String configId, String roomName, List<String> chatList, String state,Integer auto_create_room,String room_base_name,Integer room_base_id){
        this.room_base_name = roomName;
        this.chat_id_list = chatList;
        this.state = state;
        this.config_id = configId;
        this.auto_create_room=auto_create_room;
        this.room_base_name=room_base_name;
        this.room_base_id=room_base_id;
    }

}
