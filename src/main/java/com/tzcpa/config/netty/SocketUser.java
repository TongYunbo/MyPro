package com.tzcpa.config.netty;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Strings;
import lombok.Data;
/*import com.google.gson.Gson;*/

/**
 * 解析消息
 * 将前台发过来的消息解析成Mage
 * 后台发送消息到前台转成json字符串
 */
@Data
public class SocketUser {

    private static ObjectMapper gson = new ObjectMapper();
    /*private static Gson gson = new Gson();*/

    /**
     * 那个聊天室
     */
    private String table;
    /**
     * 用户id
     */
    private String id;
    /**
     * 用户名
     */
    private String name;
    /**
     * 所发送的消息
     */
    private String message;



    /**
     * 将json字符串转成Mage
     * @param message
     * @return
     * @throws Exception
     */
    public static SocketUser strJson2Mage(String message) throws Exception{
        return Strings.isNullOrEmpty(message) ? null : gson.readValue(message, SocketUser.class);
    }

    /**
     * 将Mage转成json字符串
     * @return
     * @throws Exception
     */
    public String toJson() throws Exception{
        return gson.writeValueAsString(this);
    }

    public SocketUser setTableId(String table) {
        this.setTable(table);
        return this;
    }
}
