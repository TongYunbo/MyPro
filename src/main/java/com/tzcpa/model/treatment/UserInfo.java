package com.tzcpa.model.treatment;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Strings;
import com.tzcpa.config.netty.TimerAndTaskUsage;
import lombok.Data;


/**
 * @ClassName UserInfo
 * @Description
 * @Author wangzhangju
 * @Date 2019/5/17 10:24
 * @Version 6.0
 **/
@Data
public class UserInfo {

    private static ObjectMapper gson = new ObjectMapper();

    /**
     * 聊天室id
     */
    private String table;
    /**
     * 主键id 用来做管道的唯一标识 class1 team1
     */
    private String id;
    /**
     * 班级id
     */
    private Integer classId;


    /**
     * 小组id
     */
    private Integer teamId;

    /**
     * 登陆账号
     */
    private String account;

    /**
     * 登陆密码
     */
    private String password;

    /**
     * 用户名字
     */
    private String name ;

    /**
     *  老师--teacher 学生—-team
     */
    private String role;

    /**
     * webSocket发送消息
     */
    private String message;

    /**
     *  操作动作 1-连接 2-开始 3-暂停 4-添加时间 5-下一月
     */
    private int type;

    /**
     * 当前月份的题是否都答完
     */
    private boolean whetherEnd = true;

    private String cfoName;

    /**
     * 添加分钟数
     */
    private int addTime;

    /**
     * 缓冲剩余时间
     */
    private int bufferTime;

    /**
     *  连接状态 1001：连接成功 -1001：用户已经连接
     */
    private String status;

    /**
     * 倒计时对象
     */
    private CountDownDO countDownDO;

    /**
     * 时间倒计时线程
     */
//    private TimerAndTaskUsage timerAndTaskUsage;

    /**
     * 老师id
     */
    private Integer teacherId;

    /**
     * 将json字符串转成Mage
     * @param message
     * @return
     * @throws Exception
     */
    public static UserInfo strJson2Mage(String message) throws Exception{
        return Strings.isNullOrEmpty(message) ? null : gson.readValue(message, UserInfo.class);
    }

    /**
     * 将Mage转成json字符串
     * @return
     * @throws Exception
     */
    public String toJson() throws Exception{
        return gson.writeValueAsString(this);
    }

    
    public UserInfo setTableId(String table) {
        this.setTable(table);
        return this;
    }


    /**
     * 团队名称
     */
    private String teamName;

    /**
     * 团队描述(口号）
     */
    private String teamDesc;

    /**
     * 我们的愿景
     */
    private String teamProspect;
    
    
    public UserInfo(){}
    /**
     * 根据班级ID和团队ID构造
     * @param classId
     * @param teamId
     */
    public UserInfo(Integer classId, Integer teamId){
    	this.classId = classId;
    	this.teamId = teamId;
    }

}
