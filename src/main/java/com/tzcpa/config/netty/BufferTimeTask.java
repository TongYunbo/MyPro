package com.tzcpa.config.netty;

import com.tzcpa.model.treatment.UserInfo;
import com.tzcpa.service.student.QuestionService;
import lombok.extern.slf4j.Slf4j;

import java.util.Timer;
import java.util.TimerTask;

/**
 * @ClassName BufferTimeTask
 * @Description 缓冲时间倒计时
 * @Author wangzhangju
 * @Date 2019/7/1 16:31
 * @Version 6.0
 **/
@Slf4j
public class BufferTimeTask {

    private Timer timer=null;
    private TimerTask task=null;
    private long time=0l;
    private long initTime=0l;
    private UserInfo userInfo;
    private QuestionService questionService;

    /**
     *  初始化时间 开始倒计时
     * @param time
     * @param userInfo
     * @param questionService
     */
    public BufferTimeTask(long time,long startTime,UserInfo userInfo,QuestionService questionService){
        this.initTime = startTime;
        this.time = time;
        this.userInfo = userInfo;
        this.questionService = questionService;
        this.startTime();
    }

    /**
     *  初始化时间 不开始倒计时
     * @param time
     * @param userInfo
     * @param questionService
     * @param i
     */
    public BufferTimeTask(long time,long startTime,UserInfo userInfo,QuestionService questionService,int i){
        this.initTime = startTime;
        this.time = time;
        this.userInfo = userInfo;
        this.questionService = questionService;
    }
    public void setTime(long time) {//时长设置
        this.time=time;
    }
    public void startTime() {
        timer=new Timer();
        task=new TimerTask() {
            public void run() {
                log.info("剩余时间BufferTimeTask-table: "+userInfo.getTable()+"time:"+time);
                time--;
                //如果time没减完就递归执行此方法。
                if(time == (initTime-10l)){

                    //自动维护答案
                    Thread t = new Thread() {
                        public synchronized void run() {
                            try {
                                log.info("==========维护随机答案==="+userInfo.getTable());
                                questionService.autoAnswer(userInfo);
                                if (this.isInterrupted()) {
                                    log.info("自动跳到下一月结束");
                                }
                            } catch ( Exception e) {
                                //自动维护随机答案失败 把答题状态置为暂停
                                    questionService.errorPause(userInfo);
                                log.error("BufferTimeTask-倒计时结束 自动维护答案失败", e);
                                stopTime();
                            }
                        }
                    };
                    t.start();
                    startTime();
                }
                //题目跳转到下一个月
                else if(time == 0l){
                    Thread t = new Thread() {
                        public synchronized void run() {
                    try {
                        log.info("==========跳转到下一个月==="+userInfo.getTable());
                        questionService.sendMsg(userInfo);
                        if (this.isInterrupted()) {
                            log.info("=======跳转到下一个月结束====");
                            stopTime();
                        }
                    } catch (Exception e) {
                        //自动跳转下一月失败 把答题状态置为暂停
                        questionService.errorPause(userInfo);
                        log.error("BufferTimeTask-倒计时结束 跳转到下一月失败",e);
                        stopTime();
                    }
                        }
                    };
                    t.start();

                }else{
                    startTime();
                }
            }
        };
        timer.schedule(task, 1000);//设置延时数值，和任务。
    }

    public void stopTime(){
        if(timer!= null ) {
            timer.cancel();
            timer.purge();
        }
    }

    public long getMillTime(){
        return time;
    }

    public void addTime(long time){
        this.time = this.time+time;
    }
}
