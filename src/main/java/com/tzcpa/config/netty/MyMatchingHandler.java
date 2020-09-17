package com.tzcpa.config.netty;


import com.tzcpa.model.teacher.Clazz;
import com.tzcpa.model.treatment.CountDownDO;
import com.tzcpa.model.treatment.UserInfo;
import com.tzcpa.service.student.QuestionService;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.handler.codec.http.*;
import io.netty.handler.codec.http.websocketx.*;
import io.netty.util.CharsetUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentMap;


/**
 * @author Dream
 * <p>
 * 自定义的Handler
 */
@Slf4j
@Component
@ChannelHandler.Sharable
public class MyMatchingHandler extends SimpleChannelInboundHandler<Object> {


    private WebSocketServerHandshaker handshaker;
    public static MyMatchingHandler myMatchingHandler;

    @Autowired
    QuestionService questionService;

    public MyMatchingHandler() {
    }

    @PostConstruct
    public void init() {
        myMatchingHandler = this;
        myMatchingHandler.questionService = this.questionService;
    }

    @Override
    protected void messageReceived(ChannelHandlerContext ctx, Object o) throws Exception {
        if (o instanceof FullHttpRequest) { // 传统的HTTP接入
            handleHttpRequest(ctx, (FullHttpRequest) o);
        } else if (o instanceof WebSocketFrame) { // WebSocket接入
            handleWebSocketFrame(ctx, (WebSocketFrame) o);
        }
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }

    @Override
    public void close(ChannelHandlerContext ctx, ChannelPromise promise) throws Exception {
        super.close(ctx, promise);

    }

    /**
     *  断开连接 清空用户信息 如果是学生直接清空学生的用户信息 如果是老师断开连接 清空暂停状态的老师所有班级信息 和答完题的班级信息
     * @param ctx
     * @throws Exception
     */
    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        //NO.1 获取用户信息
        //NO.1-1 获取用户管道信息
        String channelId = this.getChennelId(ctx);
        List<String> channelInfo= InformationOperateMapMatching.channelUserMap.get(channelId);
        if(null != channelInfo && channelInfo.size()>2){
        String table = channelInfo.get(0);
        String userId = channelInfo.get(1);
        String account = channelInfo.get(2);
           //NO.2 清空学生 老师的用户信息
           //NO.2-1 清空学生用户信息
            if(InformationOperateMapMatching.loginMap.containsKey(userId)) {
                if (!("teacher" + table).equals(userId)) {
                    InformationOperateMapMatching.loginMap.remove(userId);
                    InformationOperateMapMatching.matchingMap.get(table).remove(userId);
                    if (InformationOperateMapMatching.matchingMap.get(table).isEmpty()) {
                        InformationOperateMapMatching.matchingMap.remove(table);
                    }
                    InformationOperateMapMatching.channelUserMap.remove(String.valueOf(ctx.channel().id()));
                }
                //NO2-2 清空老师对应的所有暂存中 已结束的信息
                else {
                    this.clearTeacherInfo(account, ctx, channelId);
                }
            }
       }
    }

    /**
     * 清空老师对应班级的所有暂停中 已结束的用户信息 如果为空 清空table
     */
    public void clearTeacherInfo(String account,ChannelHandlerContext ctx,String channelId ){
        //NO.1 清空暂存中的数据
        InformationOperateMapMatching.loginMap.forEach((key,account2) ->{
            if(account2.equals(account)){
                InformationOperateMapMatching teacherMaching =
                        InformationOperateMapMatching.matchingMap.get(key).get("teacher"+key);
                CountDownDO countDownDO = teacherMaching.getUserInfo().getCountDownDO();
                //NO.1-1 清空暂存中或已结束 的数据
                int unAnswered = questionService.getUnAnswerd(Integer.parseInt(key));
                if((countDownDO == null || countDownDO.getWhetherStart() == 3 || countDownDO.getWhetherStart() == 0) || unAnswered == 0) {
                    teacherMaching.timerAndTaskUsage = null;
                    InformationOperateMapMatching.loginMap.remove(key);
                    InformationOperateMapMatching.matchingMap.get(key).remove("teacher"+key);
                    if (InformationOperateMapMatching.matchingMap.get(key).isEmpty()) {
                        InformationOperateMapMatching.matchingMap.remove(key);
                    }
                    InformationOperateMapMatching.channelUserMap.remove(channelId);
                }
            }
        });

    }


    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {

        ctx.fireChannelInactive();
    }

    /**
     * 处理Http请求，完成WebSocket握手<br/>
     * 注意：WebSocket连接第一次请求使用的是Http
     *
     * @param ctx
     * @param request
     * @throws Exception
     */
    private void handleHttpRequest(ChannelHandlerContext ctx, FullHttpRequest request) throws Exception {
        // 如果HTTP解码失败，返回HTTP异常
        if (!request.getDecoderResult().isSuccess() || (!"websocket".equals(request.headers().get("Upgrade")))) {
            sendHttpResponse(ctx, request, new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.BAD_REQUEST));
            return;
        }

        // 正常WebSocket的Http连接请求，构造握手响应返回
        WebSocketServerHandshakerFactory wsFactory =
                new WebSocketServerHandshakerFactory("ws://" + request.headers().get(HttpHeaders.Names.HOST),
                        null, false);
        handshaker = wsFactory.newHandshaker(request);
        if (handshaker == null) { // 无法处理的websocket版本
            WebSocketServerHandshakerFactory.sendUnsupportedWebSocketVersionResponse(ctx.channel());
        } else { // 向客户端发送websocket握手,完成握手
            handshaker.handshake(ctx.channel(), request);
        }
    }

    /**
     * Http返回
     *
     * @param ctx
     * @param request
     * @param response
     */
    private static void sendHttpResponse(ChannelHandlerContext ctx, FullHttpRequest request, FullHttpResponse response) {
        // 返回应答给客户端
        if (response.getStatus().code() != 200) {
            ByteBuf buf = Unpooled.copiedBuffer(response.getStatus().toString(), CharsetUtil.UTF_8);
            response.content().writeBytes(buf);
            buf.release();
            HttpHeaders.setContentLength(response, response.content().readableBytes());
        }

        // 如果是非Keep-Alive，关闭连接
        ChannelFuture f = ctx.channel().writeAndFlush(response);
        if (!HttpHeaders.isKeepAlive(request) || response.getStatus().code() != 200) {
            f.addListener(ChannelFutureListener.CLOSE);
        }
    }

    /**
     * 处理Socket请求
     *
     * @param ctx
     * @param frame
     * @throws Exception
     */
    private synchronized void handleWebSocketFrame(ChannelHandlerContext ctx, WebSocketFrame frame) throws Exception {
        try {
            // 判断是否是关闭链路的指令
            if (frame instanceof CloseWebSocketFrame) {
                handshaker.close(ctx.channel(), (CloseWebSocketFrame) frame.retain());
                return;
            }
            // 判断是否是Ping消息
            if (frame instanceof PingWebSocketFrame) {
                ctx.channel().write(new PongWebSocketFrame(frame.content().retain()));
                return;
            }
            // 当前只支持文本消息，不支持二进制消息
            if ((frame instanceof TextWebSocketFrame)) {
                //获取发来的消息
                String text = ((TextWebSocketFrame) frame).text();
                log.info("userInfo : " + text);

                //NO.l 获取数据
                //NO.1-1 消息转成UserInfo
                UserInfo userInfo = UserInfo.strJson2Mage(text);
                if(userInfo.getTable() == null || userInfo.getClassId() == null || userInfo.getId() == null){
                    log.error("用户信息异常===="+text,new Exception("用户信息为空"));
                }
                ctx.fireUserEventTriggered(userInfo.getTable());
                   //区分 老师、学生
                String role = userInfo.getRole();

                //NO.1-2 获取当前月份题目的时间 不是下一题
                CountDownDO countDownDO = new CountDownDO();
                if (userInfo.getType() != 5) {
                     countDownDO = questionService.getCountDownTime(userInfo.getClassId(), userInfo.getTeamId());
                    userInfo.setCountDownDO(countDownDO);
                }else{
                    countDownDO.setWhetherNextMonth(0);
                }

                //NO.2 处理业务逻辑

                // 用户初次连接 创建管道 返回用户时间信息
                // NO.2-1 处理用户请求连接逻辑 没连接过-建立新连接 连接过-使用已有连接
                // 老师连接（关闭不清除连接）- 创建老师管道  实现到时间清空老师管道功能
                // 学生连接 （关闭清除连接）- 获取老师倒计时

                if (userInfo.getType() == 1) {
                    //NO.2-1-1 老师、学生 没有连接 执行的逻辑
                    if ( ( userInfo.getRole().equals("team") && !InformationOperateMapMatching.loginMap.containsKey(userInfo.getId()))
                            || (userInfo.getRole().equals("teacher") && !InformationOperateMapMatching.loginMap.containsKey(userInfo.getTable()) )) {

                        //保存用户相关信息
                        InformationOperateMapMatching.setUserRelationInfo(ctx,userInfo);
                        this.connect(userInfo,ctx,countDownDO,role);
                    }
                    //NO.2-1 处理老师续接
                    //       由于学生退出时关闭连接 所以不需要考虑学生续连
                    //       暂时考虑续传
                    else {
                        //保存用户相关信息
                        InformationOperateMapMatching.setUserRelationInfo(ctx,userInfo);
                        this.reconnect(userInfo,ctx,countDownDO);
                    }
                }
                // NO.2-2 老师点击开始（支持暂停后重新开始） -- 给每个链接管道发送信息
                else if (userInfo.getType() == 2 && userInfo.getRole().equals("teacher")) {
                    //NO. 如果是老师 设置定时任务--学生获取

                    this.start(userInfo, countDownDO, 2,ctx);
                }
                // NO.2-3 老师点击暂停 - 给每个链接管道发送信息
                else if (userInfo.getType() == 3 && userInfo.getRole().equals("teacher")) {
                     //清空倒计时
                    this.pause(userInfo, countDownDO , ctx);
                }
                // NO.2-3 老师添加时间  给每个链接管道发送信息
                else if (userInfo.getType() == 4 && userInfo.getRole().equals("teacher")) {
                    this.addTime(userInfo, countDownDO,ctx);
                }
                // NO.2-4 老师手动点击下一月 -- 问题自动跳到下一个月 并且学生自动查询下一个月的数据
                else if (userInfo.getType() == 5 && userInfo.getRole().equals("teacher")) {
                    this.nextMonth(userInfo,ctx);
                }
                else {
                            log.error("发送消息异常 ===" + userInfo.toString());
                }
            }

            if (!(frame instanceof TextWebSocketFrame)) {
//                Exception e = new UnsupportedOperationException(
//                        String.format("%s frame type not supported", frame.getClass().getName()));
//                log.error("数据传输类型错误",e);
            }

        }catch (Exception e){
            log.error("调用socket失败",e);
        }
    }

    /**
     *
     * @param userInfo
     * @param ctx
     * @param countDownDO
     * @param role
     * @throws Exception 处理老师连接（设置倒计时状态） 学生连接（老师未连接 老师连接）
     */
    public void connect(UserInfo userInfo,ChannelHandlerContext ctx,CountDownDO countDownDO,String role) throws Exception {


          //common：获取班级信息
        Clazz clazz = questionService.getClazz(userInfo.getClassId());
        //1：老师连接
        if(userInfo.getRole().equals("teacher")) {
            //1-1：暂停后连接-理论断开连接清空内存
            if(clazz.getAnswerState() == 3){

                //1-1-1：新建timerAndTaskUsage 设置倒计时状态为停止
                TimerAndTaskUsage timerAndTaskUsage = new TimerAndTaskUsage(clazz.getTimeRemain(),userInfo,questionService,0);
                //1-1-2： 新建bufferTimeTask 设置倒计时状态为停止
                BufferTimeTask bufferTimeTask = new BufferTimeTask(clazz.getBufferRemain(),countDownDO.getRecidueBufferTime(),userInfo,questionService,0);
                //1-1-3：从class表获取倒计时剩余时间 状态
                countDownDO.setWhetherStart(clazz.getAnswerState());
                countDownDO.setRecidueMillSecond(clazz.getTimeRemain());
                countDownDO.setRecidueBufferTime(clazz.getBufferRemain());
                //1-1-4： 修改上下文信息
                userInfo.setCountDownDO(countDownDO);
                InformationOperateMapMatching.add(ctx, userInfo);
                //1-1-5：设置倒计时
                InformationOperateMapMatching.matchingMap.get(userInfo.getTable()).get("teacher" + userInfo.getClassId())
                        .setTimerAndTaskUsage(timerAndTaskUsage);
                InformationOperateMapMatching.matchingMap.get(userInfo.getTable()).get("teacher" + userInfo.getClassId())
                        .setBufferTimeTask(bufferTimeTask);
                //1-1-6 给老师回消息
                InformationOperateMapMatching.matchingMap.get(userInfo.getTable()).get("teacher" + userInfo.getClassId()).sead(userInfo);
            }
            //1-2：开始后连接-重启
            else if(clazz.getAnswerState() == 2){
                //COmmon
                InformationOperateMapMatching iomTeacher = this.getMatching(userInfo.getTable(),userInfo.getId());
                //1-2-1：判断是否存在老师倒计时：
                // 1-2-1-1 老师倒计时不存在
                if(iomTeacher == null){

                //1-2-1-1-1：新建timerAndTaskUsage 设置倒计时状态为停止
                TimerAndTaskUsage timerAndTaskUsage = new TimerAndTaskUsage(clazz.getTimeRemain(),userInfo,questionService,0);
                //1-2-1-1-2： 新建bufferTimeTask 设置倒计时状态为停止
                BufferTimeTask bufferTimeTask = new BufferTimeTask(clazz.getBufferRemain(),countDownDO.getRecidueBufferTime(),userInfo,questionService,0);
                //1-2-1-1-3：从class表获取倒计时剩余时间 状态
                countDownDO.setWhetherStart(clazz.getAnswerState());
                countDownDO.setRecidueMillSecond(clazz.getTimeRemain());
                countDownDO.setRecidueBufferTime(clazz.getBufferRemain());
                //1-2-1-1-4： 修改上下文信息
                userInfo.setCountDownDO(countDownDO);
                InformationOperateMapMatching.add(ctx, userInfo);
                //1-2-1-1-5：设置倒计时
                InformationOperateMapMatching.matchingMap.get(userInfo.getTable()).get("teacher" + userInfo.getClassId())
                        .setTimerAndTaskUsage(timerAndTaskUsage);
                InformationOperateMapMatching.matchingMap.get(userInfo.getTable()).get("teacher" + userInfo.getClassId())
                        .setBufferTimeTask(bufferTimeTask);
                //1-2-1-1-6： 启动定时任务：答题剩余时间大于0-启动答题倒计时；否则启动缓冲时间倒计时
                if(clazz.getTimeRemain() != null && clazz.getTimeRemain() == 0L){
                    InformationOperateMapMatching.matchingMap.get(userInfo.getTable()).get("teacher" + userInfo.getClassId())
                            .getBufferTimeTask().startTime();
                }else{
                    InformationOperateMapMatching.matchingMap.get(userInfo.getTable()).get("teacher" + userInfo.getClassId())
                            .getTimerAndTaskUsage().startTime();
                }
                }

                // 1-2-1-2 老师倒计时存在
                else{
                    //1-2-1-2-1：从class表获取倒计时剩余时间 状态
                    countDownDO.setWhetherStart(clazz.getAnswerState());
                    //1-2-1-2-2：从倒计时获取当月答题剩余时间，当月缓冲剩余时间
                    Long bufferTime = iomTeacher.getBufferTimeTask().getMillTime();
                    Long quesTime = iomTeacher.getTimerAndTaskUsage().getMillTime();

                    if(bufferTime == 0L && quesTime == 0l){
                        //剩余时间都为0 设置倒计时剩余时间
                        InformationOperateMapMatching.matchingMap.get(userInfo.getTable()).get("teacher" + userInfo.getClassId())
                                .getTimerAndTaskUsage().setTime(clazz.getTimeRemain());
                        InformationOperateMapMatching.matchingMap.get(userInfo.getTable()).get("teacher" + userInfo.getClassId())
                                .getBufferTimeTask().setTime(clazz.getBufferRemain());
                        //1-2-1-2-2： 启动定时任务：答题剩余时间大于0-启动答题倒计时；否则启动缓冲时间倒计时
                        if(clazz.getTimeRemain() != null && clazz.getTimeRemain() == 0L){
                            InformationOperateMapMatching.matchingMap.get(userInfo.getTable()).get("teacher" + userInfo.getClassId())
                                    .getBufferTimeTask().startTime();
                        }else{
                            InformationOperateMapMatching.matchingMap.get(userInfo.getTable()).get("teacher" + userInfo.getClassId())
                                    .getTimerAndTaskUsage().startTime();
                        }
                    }else {
                        countDownDO.setRecidueBufferTime(iomTeacher.getBufferTimeTask().getMillTime());
                        countDownDO.setRecidueMillSecond(iomTeacher.getTimerAndTaskUsage().getMillTime());
                    }
                    //1-2-1-2-3：修改老师上下文信息
                    userInfo.setCountDownDO(countDownDO);
                    InformationOperateMapMatching.add(ctx, userInfo);

                    //1-2-1-2-3：启动定时任务：
                    if(clazz.getTimeRemain() != null && clazz.getTimeRemain() == 0L){
                        InformationOperateMapMatching.matchingMap.get(userInfo.getTable()).get("teacher" + userInfo.getClassId())
                                .getBufferTimeTask().startTime();
                    }else{
                        InformationOperateMapMatching.matchingMap.get(userInfo.getTable()).get("teacher" + userInfo.getClassId())
                                .getTimerAndTaskUsage().startTime();
                    }
                }
                //1-2-7：给老师回消息
                InformationOperateMapMatching.matchingMap.get(userInfo.getTable()).get("teacher" + userInfo.getClassId()).sead(userInfo);
            }
            //NO.1-3 当前班级所有的题目都答完
            else if(clazz.getAnswerState() == 4){
                countDownDO.setWhetherStart(clazz.getAnswerState());
                userInfo.setCountDownDO(countDownDO);
                InformationOperateMapMatching.add(ctx, userInfo);
                InformationOperateMapMatching.matchingMap.get(userInfo.getTable()).get("teacher" + userInfo.getClassId()).sead(userInfo);

            }
            //1-3：未开始过的连接
            else{
                //1-3-1： 新建timerAndTaskUsage 设置倒计时状态为停止
                TimerAndTaskUsage timerAndTaskUsage = new TimerAndTaskUsage(countDownDO.getRecidueBufferTime(),userInfo,questionService,0);
                //1-3-2： 新建bufferTimeTask 设置倒计时状态为停止
                BufferTimeTask bufferTimeTask = new BufferTimeTask(countDownDO.getRecidueBufferTime(),countDownDO.getRecidueBufferTime(),userInfo,questionService,0);

                //1-3-3：修改、保存上下文信息
                userInfo.setCountDownDO(countDownDO);
                if(clazz.getAnswerState() == 4){
                    countDownDO.setWhetherStart(clazz.getAnswerState());
                }
                InformationOperateMapMatching.add(ctx, userInfo);
                //1-3-4：设置倒计时
                InformationOperateMapMatching.matchingMap.get(userInfo.getTable()).get("teacher" + userInfo.getClassId())
                        .setTimerAndTaskUsage(timerAndTaskUsage);
                InformationOperateMapMatching.matchingMap.get(userInfo.getTable()).get("teacher" + userInfo.getClassId())
                        .setBufferTimeTask(bufferTimeTask);
                //1-3-5：给老师回消息
                InformationOperateMapMatching.matchingMap.get(userInfo.getTable()).get("teacher" + userInfo.getClassId()).sead(userInfo);
            }
        }
        //2：学生连接
        else{
            //common：获取老师用户信息
            InformationOperateMapMatching matchingTeacher = this.getMatching(userInfo.getTable(),"teacher" + userInfo.getClassId());
            //2-1：暂停后连接
            if(clazz.getAnswerState() == 3){
                //2-1-1：从class表获取倒计时剩余时间 状态
                countDownDO.setWhetherStart(clazz.getAnswerState());
                //2-1-2：设置剩余时间
                        // 老师连接从老师内存中取倒计时剩余时间，
                        //	老师未连接从class表获取倒计时剩余时间
               if(null != matchingTeacher){
                   countDownDO.setRecidueBufferTime(matchingTeacher.getBufferTimeTask().getMillTime());
                   countDownDO.setRecidueMillSecond(matchingTeacher.getTimerAndTaskUsage().getMillTime());
               }else{
                   countDownDO.setRecidueBufferTime(clazz.getBufferRemain());
                   countDownDO.setRecidueMillSecond(clazz.getTimeRemain());
               }
               //2-1-3：修改、保存上下文信息
                    userInfo.setCountDownDO(countDownDO);
                   InformationOperateMapMatching.add(ctx, userInfo);
                //2-1-4： 给学生回消息
                InformationOperateMapMatching.matchingMap.get(userInfo.getTable()).get(userInfo.getId())
                        .sead(userInfo);

            }
            //2-2：开始后连接
            else if(clazz.getAnswerState() == 2){
                //2-2-1：从class表获取倒计时剩余时间 状态
                countDownDO.setWhetherStart(clazz.getAnswerState());
                //2-2-2：设置剩余时间
                  //2-2-2-1：老师已经连接从内存中获取倒计时
                if(null != matchingTeacher){
                    countDownDO.setRecidueBufferTime(matchingTeacher.getBufferTimeTask().getMillTime());
                    countDownDO.setRecidueMillSecond(matchingTeacher.getTimerAndTaskUsage().getMillTime());
                }
                  //2-2-2-2：老师没有连接从class获取倒计时 再创建老师连接对象 启动倒计时
                else{
                    //2-2-2-2-1：设置剩余时间
                    countDownDO.setRecidueBufferTime(clazz.getBufferRemain());
                    countDownDO.setRecidueMillSecond(clazz.getTimeRemain());
                    //2-2-2-2-2：创建设置老师内存 倒计时
                    UserInfo userInfo1= new UserInfo();
                    BeanUtils.copyProperties(userInfo,userInfo1);
                    userInfo1.setId("teacher"+userInfo.getClassId());
                    userInfo1.setAccount(questionService.getAccountByClassId(userInfo.getClassId()));
                    userInfo1.setCountDownDO(countDownDO);
                        //设置loginMap、channelUserMap
//                    InformationOperateMapMatching.setUserRelationInfo(ctx,userInfo1);
                        //设置上下文信息
                        InformationOperateMapMatching.add(ctx,userInfo1);

                    TimerAndTaskUsage timerAndTaskUsage = new TimerAndTaskUsage(clazz.getTimeRemain(),userInfo1,questionService,0);
                    BufferTimeTask bufferTimeTask = new BufferTimeTask(clazz.getBufferRemain(),
                            countDownDO.getRecidueBufferTime(),userInfo1,questionService,0);
                      //设置定时任务
                    InformationOperateMapMatching.matchingMap.get(userInfo.getTable()).get("teacher" + userInfo.getClassId())
                            .setTimerAndTaskUsage(timerAndTaskUsage);
                    InformationOperateMapMatching.matchingMap.get(userInfo.getTable()).get("teacher" + userInfo.getClassId())
                            .setBufferTimeTask(bufferTimeTask);

                    //2-2-2-2-3： 启动定时任务：答题剩余时间大于0-启动答题倒计时；否则启动缓冲时间倒计时
                    if(clazz.getTimeRemain() != null && clazz.getTimeRemain() == 0L){
                        InformationOperateMapMatching.matchingMap.get(userInfo.getTable()).get("teacher" + userInfo.getClassId())
                                .getBufferTimeTask().startTime();
                    }else{
                        InformationOperateMapMatching.matchingMap.get(userInfo.getTable()).get("teacher" + userInfo.getClassId())
                                .getTimerAndTaskUsage().startTime();
                    }

                }
                //2-2-3：修改上下文信息
                userInfo.setCountDownDO(countDownDO);
                InformationOperateMapMatching.add(ctx, userInfo);
                //2-2-4：给学生回消息
                InformationOperateMapMatching.matchingMap.get(userInfo.getTable()).get(userInfo.getId())
                        .sead(userInfo);
            }
            //NO.2-3 当前班级所有的题目都答完
            else if(clazz.getAnswerState() == 4){
                countDownDO.setWhetherStart(clazz.getAnswerState());
                userInfo.setCountDownDO(countDownDO);
                InformationOperateMapMatching.add(ctx, userInfo);
                InformationOperateMapMatching.matchingMap.get(userInfo.getTable()).get(userInfo.getId()).sead(userInfo);
            }
            //2-4：未开始连接
            else{
                //2-3-1：修改、保存上下文信息
                if(clazz.getAnswerState() == 4){
                    countDownDO.setWhetherStart(clazz.getAnswerState());
                }
                userInfo.setCountDownDO(countDownDO);
                InformationOperateMapMatching.add(ctx, userInfo);
                //2-3-2：给学生回消息
                InformationOperateMapMatching.matchingMap.get(userInfo.getTable()).get(userInfo.getId()).sead(userInfo);
            }
        }

    }


    /**
     *
     * @param userInfo
     * @param ctx
     * @param countDownDO
     * @throws Exception
     *  老师、学生 重新连接进行中的任务 更新socket上下文 获取剩余时间
     */
     public void reconnect(UserInfo userInfo,ChannelHandlerContext ctx,CountDownDO countDownDO ) throws Exception{

         //common：获取班级信息
         Clazz clazz = questionService.getClazz(userInfo.getClassId());
         //1：老师重连
         if(userInfo.getRole().equals("teacher")) {
             //Common：获取老师内存信息
             InformationOperateMapMatching matchingTeacher =
                     InformationOperateMapMatching.matchingMap.get(userInfo.getTable()).get(userInfo.getId());
             //1-1：暂停后重连
             if(clazz.getAnswerState() == 3){
               //1-1-1:设置倒计时状态
                 countDownDO.setWhetherStart(clazz.getAnswerState());
                 //1-1-2：从接口设置倒计时剩余时间
                 countDownDO.setRecidueMillSecond(clazz.getTimeRemain());
                 countDownDO.setRecidueBufferTime(clazz.getBufferRemain());
                 //1-1-3：在内存设置老师倒计时时间
                 matchingTeacher.getTimerAndTaskUsage().setTime(clazz.getTimeRemain());
                 matchingTeacher.getBufferTimeTask().setTime(clazz.getBufferRemain());
                 //1-1-4：更新老师上下文
                 userInfo.setCountDownDO(countDownDO);
                 InformationOperateMapMatching.add(ctx, userInfo);
                 //1-1-5：给老师回消息
                 InformationOperateMapMatching.matchingMap.get(userInfo.getTable()).get(userInfo.getId()).sead(userInfo);
             }
             //1-2:老师开始中重连
             else if(clazz.getAnswerState() == 2){
                 //1-2-1:设置倒计时状态
                 countDownDO.setWhetherStart(clazz.getAnswerState());
                //1-2-2：从内存获取老师倒计时剩余时间
                 if(matchingTeacher.getBufferTimeTask().getMillTime() == 0l){
                     countDownDO.setRecidueMillSecond(clazz.getTimeRemain());
                     countDownDO.setRecidueBufferTime(clazz.getBufferRemain());
                     InformationOperateMapMatching.matchingMap.get(userInfo.getTable()).get("teacher"+userInfo.getClassId())
                             .getTimerAndTaskUsage().setTime(clazz.getTimeRemain());
                     InformationOperateMapMatching.matchingMap.get(userInfo.getTable()).get("teacher"+userInfo.getClassId())
                             .getBufferTimeTask().setTime(clazz.getBufferRemain());
                 }else {
                     countDownDO.setRecidueMillSecond(matchingTeacher.getTimerAndTaskUsage().getMillTime());
                     countDownDO.setRecidueBufferTime(matchingTeacher.getBufferTimeTask().getMillTime());
                 }
                 //1-2-3:更新老师上下文
                 userInfo.setCountDownDO(countDownDO);
                 InformationOperateMapMatching.add(ctx, userInfo);
                 //1-1-4：给老师回消息
                 InformationOperateMapMatching.matchingMap.get(userInfo.getTable()).get(userInfo.getId()).sead(userInfo);
             }
             //NO.1-3 当前班级所有的题目都答完
             else if(clazz.getAnswerState() == 4){
                 //common：暂停定时任务
                 if(null !=InformationOperateMapMatching.matchingMap.get(userInfo.getTable())
                         .get(userInfo.getId()).getBufferTimeTask()){
                     InformationOperateMapMatching.matchingMap.get(userInfo.getTable()).get(userInfo.getId())
                             .getBufferTimeTask().stopTime();
                 }
                 if(null !=InformationOperateMapMatching.matchingMap.get(userInfo.getTable())
                         .get(userInfo.getId()).getTimerAndTaskUsage()) {
                     InformationOperateMapMatching.matchingMap.get(userInfo.getTable()).get(userInfo.getId())
                             .getTimerAndTaskUsage().stopTime();
                 }
                 countDownDO.setWhetherStart(clazz.getAnswerState());
                 userInfo.setCountDownDO(countDownDO);
                 InformationOperateMapMatching.add(ctx, userInfo);
                 InformationOperateMapMatching.matchingMap.get(userInfo.getTable()).get(userInfo.getId()).sead(userInfo);
             }
             //1-4：老师未开始 重连
             else{
                 //1-3-1:从class表获取倒计时状态
                 countDownDO.setWhetherStart(clazz.getAnswerState());
                 //1-3-2:在内存设置老师倒计时时间
                 matchingTeacher.getBufferTimeTask().setTime(countDownDO.getRecidueBufferTime());
                 matchingTeacher.getTimerAndTaskUsage().setTime(countDownDO.getRecidueMillSecond());
                 //1-3-3:更新老师上下文
                 userInfo.setCountDownDO(countDownDO);
                 InformationOperateMapMatching.add(ctx, userInfo);
                 //1-3-4：给老师回消息
                 InformationOperateMapMatching.matchingMap.get(userInfo.getTable()).get(userInfo.getId()).sead(userInfo);
             }
         }
         //2:学生重连
         else{
             //COmmon：获取老师信息
             InformationOperateMapMatching matchingTeacher =
                     InformationOperateMapMatching.matchingMap.get(userInfo.getTable()).get("teacher"+userInfo.getClassId());
             //2-1：暂停后重连
             if(clazz.getAnswerState() == 3){
                 //2-1-1：从class表获取倒计时状态
                 countDownDO.setWhetherStart(clazz.getAnswerState());
                 //2-1-2：从class获取老师倒计时剩余时间
                 countDownDO.setRecidueBufferTime(clazz.getBufferRemain());
                 countDownDO.setRecidueMillSecond(clazz.getTimeRemain());
                 //2-1-3：更新学生上下文信息
                 userInfo.setCountDownDO(countDownDO);
                 InformationOperateMapMatching.add(ctx, userInfo);
                 //2-1-4：给学生回消息
                 InformationOperateMapMatching.matchingMap.get(userInfo.getTable()).get(userInfo.getId()).sead(userInfo);
             }
             //2-2：开始中重连
             else  if(clazz.getAnswerState() == 2){
                 //2-2-1：从class表获取倒计时状态
                 countDownDO.setWhetherStart(clazz.getAnswerState());
                 //2-2-2：从内存获取老师倒计时剩余时间
                 if(matchingTeacher.getBufferTimeTask().getMillTime() == 0l){
                     countDownDO.setRecidueMillSecond(clazz.getTimeRemain());
                     countDownDO.setRecidueBufferTime(clazz.getBufferRemain());
                     InformationOperateMapMatching.matchingMap.get(userInfo.getTable()).get("teacher"+userInfo.getClassId())
                             .getTimerAndTaskUsage().setTime(clazz.getTimeRemain());
                     InformationOperateMapMatching.matchingMap.get(userInfo.getTable()).get("teacher"+userInfo.getClassId())
                             .getBufferTimeTask().setTime(clazz.getBufferRemain());
                 }else {
                     countDownDO.setRecidueMillSecond(matchingTeacher.getTimerAndTaskUsage().getMillTime());
                     countDownDO.setRecidueBufferTime(matchingTeacher.getBufferTimeTask().getMillTime());
                 }
                 //2-2-3：更新学生上下文信息
                 userInfo.setCountDownDO(countDownDO);
                 InformationOperateMapMatching.add(ctx, userInfo);
                 //2-2-4：给学生回消息
                 InformationOperateMapMatching.matchingMap.get(userInfo.getTable()).get(userInfo.getId()).sead(userInfo);
             }
             //NO.2-3 当前班级所有的题目都答完
             else if(clazz.getAnswerState() == 4){
                 countDownDO.setWhetherStart(clazz.getAnswerState());
                 userInfo.setCountDownDO(countDownDO);
                 InformationOperateMapMatching.add(ctx, userInfo);
                 InformationOperateMapMatching.matchingMap.get(userInfo.getTable()).get(userInfo.getId()).sead(userInfo);
             }
             //2-4：未开始重连
             else{
                 //2-3-1：从class表获取倒计时状态
                 countDownDO.setWhetherStart(clazz.getAnswerState());
                 //2-3-2：更新学生上下文信息
                 userInfo.setCountDownDO(countDownDO);
                 InformationOperateMapMatching.add(ctx, userInfo);
                 //2-3-3：给学生回消息
                 InformationOperateMapMatching.matchingMap.get(userInfo.getTable()).get(userInfo.getId()).sead(userInfo);
             }
         }


     }


    public void start(UserInfo userInfo, CountDownDO countDownDO,  int wheatherStart,ChannelHandlerContext ctx) throws Exception {

         //commong：获取班级信息
        Clazz clazz = questionService.getClazz(userInfo.getClassId());
         //1：老师开始：
        if(userInfo.getRole().equals("teacher")){
            //Common：获取老师内存信息
            InformationOperateMapMatching matchingTeacher =
                    InformationOperateMapMatching.matchingMap.get(userInfo.getTable()).get(userInfo.getId());
          //1-1:老师暂停后开始
            if(clazz.getAnswerState() == 3){
              //1-1-1：从class表获取倒计时状态
                countDownDO.setWhetherStart(2);
                //1-1-2： 判断内存是否有老师倒计时
                     //存在-设置倒计时时间
                     //不存在-创建倒计时
                if(null != matchingTeacher){
                    InformationOperateMapMatching.matchingMap.get(userInfo.getTable()).get(userInfo.getId())
                            .getTimerAndTaskUsage().setTime(clazz.getTimeRemain());
                    InformationOperateMapMatching.matchingMap.get(userInfo.getTable()).get(userInfo.getId())
                            .getBufferTimeTask().setTime(clazz.getBufferRemain());
                    countDownDO.setRecidueBufferTime(clazz.getBufferRemain());
                    countDownDO.setRecidueMillSecond(clazz.getTimeRemain());
                }else{
                    TimerAndTaskUsage timerAndTaskUsage = new TimerAndTaskUsage(clazz.getTimeRemain(),userInfo,questionService,0);
                    BufferTimeTask bufferTimeTask = new BufferTimeTask(clazz.getBufferRemain(),
                            countDownDO.getRecidueBufferTime(),userInfo,questionService,0);
                    //设置loginMap、channelUserMap
                    countDownDO.setRecidueBufferTime(clazz.getBufferRemain());
                    countDownDO.setRecidueMillSecond(clazz.getTimeRemain());
                    userInfo.setCountDownDO(countDownDO);
                    InformationOperateMapMatching.setUserRelationInfo(ctx,userInfo);
                    //设置上下文信息
                    InformationOperateMapMatching.add(ctx,userInfo);
                    InformationOperateMapMatching.matchingMap.get(userInfo.getTable()).get(userInfo.getId())
                            .setTimerAndTaskUsage(timerAndTaskUsage);
                    InformationOperateMapMatching.matchingMap.get(userInfo.getTable()).get(userInfo.getId())
                            .setBufferTimeTask(bufferTimeTask);
                }
                //1-1-3：启动倒计时：答题倒计时大于0启动答题倒计时 否则启动缓冲倒计时
                if(clazz.getTimeRemain() != null && clazz.getTimeRemain() == 0L){
                    InformationOperateMapMatching.matchingMap.get(userInfo.getTable()).get( userInfo.getId())
                            .getBufferTimeTask().startTime();
                }else{
                    InformationOperateMapMatching.matchingMap.get(userInfo.getTable()).get(userInfo.getId())
                            .getTimerAndTaskUsage().startTime();
                }
                //1-1-4：修改class表状态到倒计时时间
                questionService.updateClassStatus(userInfo.getClassId(),2,userInfo.getAccount(),clazz.getTimeRemain(),
                        clazz.getBufferRemain());
                //1-1-5：更新老师上下文信息
                userInfo.setCountDownDO(countDownDO);
                InformationOperateMapMatching.add(ctx, userInfo);
                //1-1-6：给当前班级连接中的老师、学生发送消息
                this.sendAll(userInfo.getTable(),countDownDO);
            }
            //1-2:老师未开始点击开始
            else if(clazz.getAnswerState()==0){
                //1-2-1：从class表获取倒计时状态
                countDownDO.setWhetherStart(2);
                //1-2-2:修改class表倒计时状态、剩余时间
                questionService.updateClassStatus(userInfo.getClassId(),2,userInfo.getAccount(),countDownDO.getRecidueMillSecond(),countDownDO.getRecidueBufferTime());
                //1-2-3：设置老师答题倒计时、缓冲倒计时时间
                   //判断内存是否有老师倒计时：
                   //			      有：从接口获取倒计时设置到定时任务
                   //				  无：通过接口时间 创建倒计时 为停止
                if(null != matchingTeacher){
                    InformationOperateMapMatching.matchingMap.get(userInfo.getTable()).get(userInfo.getId())
                            .getTimerAndTaskUsage().setTime(countDownDO.getRecidueMillSecond());
                    InformationOperateMapMatching.matchingMap.get(userInfo.getTable()).get(userInfo.getId())
                            .getBufferTimeTask().setTime(countDownDO.getRecidueBufferTime());
                }else{
                    TimerAndTaskUsage timerAndTaskUsage = new TimerAndTaskUsage(countDownDO.getRecidueMillSecond(),userInfo,questionService,0);
                    BufferTimeTask bufferTimeTask = new BufferTimeTask(countDownDO.getRecidueBufferTime(),
                            countDownDO.getRecidueBufferTime(),userInfo,questionService,0);
                    //设置loginMap、channelUserMap
                    userInfo.setCountDownDO(countDownDO);
                    InformationOperateMapMatching.setUserRelationInfo(ctx,userInfo);
                    //设置上下文信息
                    InformationOperateMapMatching.add(ctx,userInfo);
                    InformationOperateMapMatching.matchingMap.get(userInfo.getTable()).get(userInfo.getId())
                            .setTimerAndTaskUsage(timerAndTaskUsage);
                    InformationOperateMapMatching.matchingMap.get(userInfo.getTable()).get(userInfo.getId())
                            .setBufferTimeTask(bufferTimeTask);
                }
                //1-2-4：启动倒计时：答题倒计时大于0启动答题倒计时 否则启动缓冲倒计时
                if(clazz.getTimeRemain() != null && clazz.getTimeRemain() == 0L){
                    InformationOperateMapMatching.matchingMap.get(userInfo.getTable()).get( userInfo.getId())
                            .getBufferTimeTask().startTime();
                }else{
                    InformationOperateMapMatching.matchingMap.get(userInfo.getTable()).get(userInfo.getId())
                            .getTimerAndTaskUsage().startTime();
                }
                //1-2-5:更新老师上下文信息
                userInfo.setCountDownDO(countDownDO);
                InformationOperateMapMatching.add(ctx, userInfo);
                //1-2-6：给当前班级连接中的老师、学生发送消息
                this.sendAll(userInfo.getTable(),countDownDO);
            }
        }

    }

    public void sendAll(String table , CountDownDO countDownDO) throws Exception{
        ConcurrentMap<String, InformationOperateMapMatching> map = InformationOperateMapMatching.matchingMap.get(table);
        InformationOperateMapMatching.matchingMap.get(table).forEach((key, iom) -> {
            try {
                UserInfo localUserInfo = iom.getUserInfo();
                localUserInfo.setCountDownDO(countDownDO);
                iom.sead(localUserInfo);
            } catch (Exception e) {
                log.error("发送消息异常 ===" +table, e);
            }
        });
    }

    /**
     *  暂停执行的操作
     * @param userInfo
     * @param countDownDO
     * @param ctx
     * @throws Exception
     */
    public void pause(UserInfo userInfo, CountDownDO countDownDO,ChannelHandlerContext ctx) throws Exception {

        //commong：获取班级信息
          Clazz clazz = questionService.getClazz(userInfo.getClassId());
          //
        InformationOperateMapMatching matchingTeacher =
                this.getMatching(userInfo.getTable(),userInfo.getId());;
        //1：老师暂存
        //common：暂停定时任务
        InformationOperateMapMatching.matchingMap.get(userInfo.getTable()).get(userInfo.getId())
                .getBufferTimeTask().stopTime();
        InformationOperateMapMatching.matchingMap.get(userInfo.getTable()).get(userInfo.getId())
                .getTimerAndTaskUsage().stopTime();
        //common：获取答题剩余时间、缓存剩余时间
           Long answerReciTime = InformationOperateMapMatching.matchingMap.get(userInfo.getTable()).get(userInfo.getId())
                   .getTimerAndTaskUsage().getMillTime();
        Long bufferReciTime = InformationOperateMapMatching.matchingMap.get(userInfo.getTable()).get(userInfo.getId())
                .getBufferTimeTask().getMillTime();
           //1-1：处理答题中暂存
           if(answerReciTime > 0L){
               //1-1-1：从定时任务获取倒计时间 修改class表答题状态、剩余时间
               questionService.updateClassStatus(userInfo.getClassId(),3,userInfo.getAccount(),
                       answerReciTime,bufferReciTime);
               //1-1-2:从倒计时获取剩余时间
               countDownDO.setRecidueMillSecond(answerReciTime);
               countDownDO.setRecidueBufferTime(bufferReciTime);
               //1-1-3:设置答题状态为暂停-3
               countDownDO.setWhetherStart(3);
               //1-1-4：修改老师上下文信息
               userInfo.setCountDownDO(countDownDO);
               InformationOperateMapMatching.add(ctx, userInfo);
               //1-1-5： 给当前班级连接中的老师、学生发送消息
               this.sendAll(userInfo.getTable(),countDownDO);
           }
           //1-2:处理缓存中暂停
          else if(bufferReciTime>0L){
           //1-2-1：从倒计时获取剩余时间
               countDownDO.setRecidueMillSecond(answerReciTime);
               countDownDO.setRecidueBufferTime(bufferReciTime);
           //1-2-2：修改class表答题状态、剩余时间
               questionService.updateClassStatus(userInfo.getClassId(),3,userInfo.getAccount(),
                       answerReciTime,bufferReciTime);
               //1-2-3:设置答题状态为暂停-3
               countDownDO.setWhetherStart(3);
            //1-2-4：修改老师上下文信息
               userInfo.setCountDownDO(countDownDO);
               InformationOperateMapMatching.add(ctx, userInfo);
               //1-2-5： 给当前班级连接中的老师、学生发送消息
               this.sendAll(userInfo.getTable(),countDownDO);
           }
    }

        public void addTime(UserInfo userInfo, CountDownDO countDownDO,ChannelHandlerContext ctx) throws Exception {

            //common 获取班级信息
            Clazz clazz = questionService.getClazz(userInfo.getClassId());
          //NO.1 暂停定时任务
            //common：暂停定时任务
            InformationOperateMapMatching.matchingMap.get(userInfo.getTable()).get("teacher"+userInfo.getTable())
                    .getBufferTimeTask().stopTime();
            InformationOperateMapMatching.matchingMap.get(userInfo.getTable()).get("teacher"+userInfo.getTable())
                    .getTimerAndTaskUsage().stopTime();
            //NO.2 获取倒计时剩余时间
            Long redicBufferTime = InformationOperateMapMatching.matchingMap.get(userInfo.getTable()).get(userInfo.getId())
                    .getBufferTimeTask().getMillTime();
            Long redicQuesTime = InformationOperateMapMatching.matchingMap.get(userInfo.getTable()).get(userInfo.getId())
                    .getTimerAndTaskUsage().getMillTime();
            //NO.3 答题倒计时时间 不为0 执行添加倒计时逻辑
            if(redicQuesTime > 0L){
                //3-1 给倒计时定时任务添加时间
                int addTime  = 60*userInfo.getAddTime();
                InformationOperateMapMatching.matchingMap.get(userInfo.getTable()).get(userInfo.getId())
                        .getTimerAndTaskUsage().setTime(redicQuesTime+addTime);
                //3-2 给当前有效月份最后一道题目添加时间 把倒计时剩余时间维护到class表
                questionService.addTime(userInfo.getClassId(), redicQuesTime+addTime,userInfo.getAddTime(),userInfo.getAccount());
                //3-3 返回前端剩余时间
                countDownDO.setMothMillisecond(addTime+countDownDO.getMothMillisecond());
                countDownDO.setRecidueMillSecond(redicQuesTime+addTime);
                countDownDO.setRecidueBufferTime(redicBufferTime);

            }
            //NO.4 答题倒计时为0 设置答题倒计时 缓冲倒计时
            else{
                countDownDO.setRecidueMillSecond(redicQuesTime);
                countDownDO.setRecidueBufferTime(redicBufferTime);
            }
            //5 从 class获取状态
            countDownDO.setWhetherStart(clazz.getAnswerState());
            //NO.5 如果倒计时状态进行中 开启倒计时任务
            if(redicQuesTime == 0l && clazz.getAnswerState() == 2){
                InformationOperateMapMatching.matchingMap.get(userInfo.getTable()).get(userInfo.getId())
                        .getBufferTimeTask().startTime();
            }else if(clazz.getAnswerState() == 2){
                InformationOperateMapMatching.matchingMap.get(userInfo.getTable()).get(userInfo.getId())
                        .getTimerAndTaskUsage().startTime();
            }

            //NO.6 给老师、学生发送消息
            this.sendAll(userInfo.getTable(),countDownDO);

        }

        public String getChennelId (ChannelHandlerContext context){

            return String.valueOf(context.channel().id());
        }

        public void nextMonth(UserInfo userInfo, ChannelHandlerContext ctx) throws Exception {

        //NO.1 调用下一月方法
            try {
                questionService.nextMonth(userInfo,ctx);
            }catch (Exception e){
                log.error("调用下一月失败",e);
                throw  e;
            }
        }

        public InformationOperateMapMatching getMatching(String table,String userId){
            ConcurrentMap<String, InformationOperateMapMatching>
                    concurrentMap = InformationOperateMapMatching.matchingMap.get(table);
        if(null == concurrentMap){
            return null;
        }else {
            return concurrentMap.get(userId);
        }
        }


}
