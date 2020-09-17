package com.tzcpa.config.netty;

import com.tzcpa.model.treatment.CountDownDO;
import com.tzcpa.model.treatment.UserInfo;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ConcurrentMap;

/**
 * 存储信息
 */
@Slf4j
public class InformationOperateMapMatching {

    //班级对象
    public static ConcurrentMap<String, ConcurrentMap<String, InformationOperateMapMatching>> matchingMap = new ConcurrentHashMap<>();
    //保存连接用户信息 如果是老师 就存班级id 如果是学生 保存"team"+teamId
    public static ConcurrentMap<String, String> loginMap = new ConcurrentHashMap<>();
    //保存连接用户信息 关闭连接时使用
    public static ConcurrentMap<String,List<String>>  channelUserMap = new ConcurrentHashMap();

    //倒计时任务
    public TimerAndTaskUsage timerAndTaskUsage;

    //缓冲时间 倒计时
    public  BufferTimeTask bufferTimeTask;
    //上下文
    private ChannelHandlerContext ctx;
    //用户信息
    private UserInfo userInfo;

    private InformationOperateMapMatching(ChannelHandlerContext ctx, UserInfo userInfo) {
        this.ctx = ctx;
        this.userInfo = userInfo;
    }

    /**
     * 添加到队列当中等待其他用户登录后匹配
     * @param userInfo
     */
    public static void setUserRelationInfo( ChannelHandlerContext ctx,UserInfo userInfo) {
        if(userInfo.getRole().equals("team")) {
            InformationOperateMapMatching.loginMap.put(userInfo.getId(), userInfo.getTable());
        }else{
            InformationOperateMapMatching.loginMap.put(userInfo.getTable(), userInfo.getTable());
        }
        List<String> users = new ArrayList<>();
        users.add(userInfo.getTable());
        users.add(userInfo.getId());
        users.add(userInfo.getAccount());
        InformationOperateMapMatching.channelUserMap.put(String.valueOf(ctx.channel().id()),users);
    }

    /**
     * 添加用户信息
     * @param ctx
     * @param userInfo
     */
    public static void add(ChannelHandlerContext ctx, UserInfo userInfo) {

        String userId = userInfo.getId();
        if(userInfo.getRole().equals("teacher")){
            userId = "teacher"+ userInfo.getTable();
        }
        //如果内存包含班级 替换班级中对应的用户信息
        if (matchingMap.containsKey(userInfo.getTable())) {
            InformationOperateMapMatching iom = InformationOperateMapMatching.matchingMap.get(userInfo.getTable()).get(userId);
            if(iom != null){
                if(ctx != null) {
                    iom.ctx = ctx;
                }
                iom.userInfo=userInfo;
            }else{
                iom = new InformationOperateMapMatching(ctx, userInfo);
            }
            matchingMap.get(userInfo.getTable()).put(userId, iom);
        }
        //如果内存不包含对应班级--新建新的班级 把用户信息保存到对应的班级
        else {
            InformationOperateMapMatching iom = new InformationOperateMapMatching(ctx, userInfo);
            ConcurrentMap<String, InformationOperateMapMatching> cmap = new ConcurrentHashMap<>();
            cmap.put(userId, iom);
            matchingMap.put(userInfo.getTable(), cmap);
        }

    }

    /**
     * 删除用户信息
     * @param id
     * @param table
     */
    public static void delete(String id, String table) {
        ConcurrentMap<String, InformationOperateMapMatching> cmap = matchingMap.get(table);
        if (cmap.size() <= 1) {
            matchingMap.remove(table);
        } else {
            cmap.remove(id);
        }
    }

    /**
     * 给用户发送消息
     * @param mage
     * @throws Exception
     */
    public void sead(UserInfo mage) throws Exception{
        this.ctx.channel().write(new TextWebSocketFrame(mage.toJson()));
        this.ctx.flush();
    }

    public UserInfo getUserInfo() {
        return this.userInfo;
    }

    /**
     *
     * @param table
     * @param whetherNextMonth 通知学生刷新题目
     */
    public static  void nextMonth(String table,int whetherNextMonth ,CountDownDO countDownDO){

        //设置老师倒计时
        InformationOperateMapMatching iomTeacher = InformationOperateMapMatching.matchingMap.get(table).get("teacher" + table);
        iomTeacher.getTimerAndTaskUsage().setTime(countDownDO.getRecidueMillSecond());
        iomTeacher.getTimerAndTaskUsage().startTime();

        InformationOperateMapMatching.matchingMap.get(table).forEach((key, iom) -> {
            try {
                UserInfo localUserInfo = iom.getUserInfo();
                countDownDO.setWhetherNextMonth(whetherNextMonth);
                countDownDO.setWhetherStart(2);
                localUserInfo.setCountDownDO(countDownDO);
                iom.sead(localUserInfo);
            } catch (Exception e) {
                log.error("发送消息异常 ===" +table, e);
        }
        });
    }

    public ChannelHandlerContext getChannelHandlerContext() {
        return this.ctx;
    }

    public InformationOperateMapMatching setTableId(String table) {
        this.userInfo.setTableId(table);
        return this;
    }

    public TimerAndTaskUsage getTimerAndTaskUsage() {
        return timerAndTaskUsage;
    }

    public void setTimerAndTaskUsage(TimerAndTaskUsage timerAndTaskUsage) {
        this.timerAndTaskUsage = timerAndTaskUsage;
    }

    public BufferTimeTask getBufferTimeTask() {
        return bufferTimeTask;
    }

    public void setBufferTimeTask(BufferTimeTask bufferTimeTask) {
        this.bufferTimeTask = bufferTimeTask;
    }
}
