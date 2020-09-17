package com.tzcpa.service.treatment;

/**
 * @ClassName InitClassSaleUnivalenceService
 * @Description 初始化销售量配置表
 * @Author hanxf
 * @Date 2019/5/17 11:57
 * @Version 1.0
 **/
public interface InitClassSaleUnivalenceService {

    /**
     * 初始化班级销量单价
     *
     * @Author hanxf
     * @Date 11:58 2019/5/17
     * @param classId 班级id
     * @return void
    **/
    void initClassSaleUnivalence(int classId);
}
