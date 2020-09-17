package com.tzcpa.service.treatment;

/**
 * @ClassName InitEventProcessService
 * @Description
 * @Author hanxf
 * @Date 2019/5/14 11:33
 * @Version 1.0
 **/
public interface InitEventProcessService {
    /**
     * 初始化班级事件流程数据
     *
     * @param classId 班级id
     * @return
     */
    void initEventProcess(int classId);
}
