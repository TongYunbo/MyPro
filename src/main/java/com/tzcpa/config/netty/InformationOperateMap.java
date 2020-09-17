package com.tzcpa.config.netty;

import com.tzcpa.model.treatment.UserInfo;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * 存储信息
 */
public class InformationOperateMap {

    public static ConcurrentMap<String, ConcurrentMap<String, InformationOperateMap>> map = new ConcurrentHashMap<>();

    private ChannelHandlerContext ctx;
    private UserInfo mage;

    private InformationOperateMap(ChannelHandlerContext ctx, UserInfo mage) {
        this.ctx = ctx;
        this.mage = mage;
    }

    /**
     * 添加用户信息
     * @param ctx
     * @param mage
     */
    public static void add(ChannelHandlerContext ctx, UserInfo mage) {
        InformationOperateMap iom = new InformationOperateMap(ctx, mage);
        ConcurrentMap<String, InformationOperateMap> cmap = new ConcurrentHashMap<>();
        if (map.containsKey(mage.getTable())) {
            map.get(mage.getTable()).put(mage.getId(), iom);
        } else {
            cmap.put(mage.getId(), iom);
            map.put(mage.getTable(), cmap);
        }
    }

    /**
     * 删除用户信息
     * @param id
     * @param table
     */
    public static void delete(String id, String table) {
        ConcurrentMap<String, InformationOperateMap> cmap = map.get(table);
        if (cmap.size() <= 1) {
            map.remove(table);
        } else {
            cmap.remove(id);
        }
    }


    /**
     * 判断是否存在该用户
     * @param mage
     * @return 存在false 不存在true
     */
    public static boolean isNo(UserInfo mage) {
        return map.containsKey(mage.getTable()) ? map.get(mage.getTable()).containsKey(mage.getId()) ? false : true : true;
    }

    /**
     * 给用户发送消息
     * @param mage
     * @throws Exception
     */
    public void sead(UserInfo mage) throws Exception{
        //this.ctx.channel().write(new TextWebSocketFrame(mage.toJson()));
        //this.ctx.flush();
        ctx.writeAndFlush(new TextWebSocketFrame(mage.toJson()));
    }

    public UserInfo getMage() {
        return mage;
    }
}
