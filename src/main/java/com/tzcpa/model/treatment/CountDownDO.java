package com.tzcpa.model.treatment;

import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * @ClassName CountDownDTO
 * @Description
 * @Author wangzhangju
 * @Date 2019/5/20 9:40
 * @Version 6.0
 **/
@Data
public class CountDownDO {

    /**
     * 当前月数倒计时-毫秒数
     */
    private Long mothMillisecond;

    /**
     * 当前月数剩余时间
     */
    private Long recidueMillSecond;

    /**
     *  当前缓冲 剩余时间
     */
    private Long recidueBufferTime;

    /**
     * 当前题目倒计时-毫秒数
     */
    private List<Map<String ,Object>> topicMilliseconds;

    /**
     * 状态标识-是否开始倒计时 2-是 0-否 3-暂停
     */
    private int whetherStart;

    /**
     * 自动维护随机答案 错误信息
     */
    private String errorMsg ;

    /**
     * 是否跳到下一个月 1-是 0-否
     */
    private int whetherNextMonth;

    /**
     * 班级id
     */
    private int classId;

    /**
     * 当前年月
     */
    private String timeLine;
}
