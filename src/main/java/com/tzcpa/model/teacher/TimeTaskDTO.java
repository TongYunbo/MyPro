package com.tzcpa.model.teacher;

import lombok.Data;
import lombok.ToString;

/**
 * @ClassName TimeTaskDTO
 * @Description
 * @Author wangzhangju
 * @Date 2019/5/14 15:46
 * @Version 6.0
 **/
@Data
@ToString
public class TimeTaskDTO {

    /**
     * 剩余时间
     */
    private Long time;

    /**
     * 添加时间
     */
    private Long addTime;

    /**
     * 操作类型 1-开始 2-暂停 3-添加时间 4-结束
     */
    private int opeType;
}
