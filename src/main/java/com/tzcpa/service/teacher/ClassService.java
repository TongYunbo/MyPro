package com.tzcpa.service.teacher;

import java.util.List;

import com.tzcpa.model.PageBean;
import com.tzcpa.model.teacher.Clazz;
import com.tzcpa.model.teacher.Teacher;

/**
 * 用户管理---班级管理
 *
 * @author WangYao
 * <p>
 * 2019年4月28日
 */
public interface ClassService {

    /**
     * 查询班级列表信息
     *
     * @param currentPage 当前页
     * @return
     */
    PageBean<Clazz> findByPage(int currentPage);

    /**
     * 查询教师名称
     *
     * @return
     */
    List<Teacher> getTeacherName();

    /**
     * 新增班级信息
     *
     * @param className 班级名称
     * @param tId 班级所对应的教师列表
     * @return
     */
    boolean addClassInfo(String className, int tId);

    /**
     * 根据班级id查看教师列表
     *
     * @param classId 班级id
     * @return
     */
    List<Teacher> getClassToTeacherLists(int classId);

    /**
     * 修改班级信息
     * @param id 班级id
     * @param className 班级名称
     * @param tId 教师id
     * @return
     */
    boolean updateClassInfo(int id, String className, int tId);

    /**
     *
     * @param account
     * @return
     */
    List<Clazz> getClassByUser ( String account ,String className);

}
