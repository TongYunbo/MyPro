package com.tzcpa.config.netty;

import com.tzcpa.model.treatment.CountDownDO;
import com.tzcpa.model.treatment.UserInfo;
import com.tzcpa.service.student.QuestionService;
import lombok.extern.slf4j.Slf4j;

import java.util.Timer;
import java.util.TimerTask;

/**
 * @ClassName TimerAndTaskUsage
 * @Description
 * @Author wangzhangju
 * @Date 2019/5/20 14:40
 * @Version 6.0
 **/
@Slf4j
public class TimerAndTaskUsage {


    private Timer timer=null;
    private TimerTask task=null;
    private long time=0l;
    private UserInfo userInfo;
    private QuestionService questionService;

    public TimerAndTaskUsage(long time,UserInfo userInfo,QuestionService questionService){

        this.time = time;
        this.userInfo = userInfo;
        this.questionService = questionService;
        this.startTime();
    }
    public TimerAndTaskUsage(long time,UserInfo userInfo,QuestionService questionService,int i){

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
                try {
                    log.info("剩余时间TimerAndTaskUsage-table: " + userInfo.getTable() + "time:" + time);
                    time--;
                    //time倒计时结束 停止当前倒计时 启动缓冲区倒计时
                    if (time <= 0l) {

                        log.info("==========开启缓冲倒计时===="+userInfo.getTable());
                        InformationOperateMapMatching.matchingMap
                                .get(userInfo.getTable())
                                .get(userInfo.getId())
                                .getBufferTimeTask().startTime();
                        stopTime();
                    } else {
                        startTime();
                    }
                }catch (Exception e){
                    log.error("TimerAndTaskUsage 异常",userInfo);
                    if( userInfo==null || userInfo.getTable() == null || time <=0L){
                        stopTime();
                    }else{
                        startTime();
                    }
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
