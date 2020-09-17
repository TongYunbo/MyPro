package com.tzcpa.service.teacher;

import com.tzcpa.model.PageBean;
import com.tzcpa.model.teacher.Teacher;

/**
 * 用户管理---教师管理
 *
 * @author WangYao
 * <p>
 * 2019年4月28日
 */
public interface TeacherService {

    /**
     * 查询教师列表信息(分页)
     *
     * @param currentPage：当前页数
     * @return
     */
    PageBean<Teacher> findByPage(int currentPage);

    /**
     * 添加教师对象
     *
     * @param teacher：添加的教师信息
     * @return
     */
    boolean addTeacher(Teacher teacher);

    /**
     * 停用教师
     *
     * @param isdel：是否删除 
     * @param id：根据此id停用教师
     * @return
     */
    Integer blockUpTeacher(int id, int isdel);

    /**
     * 修改教师信息
     *
     * @param teacher：修改后的教师信息
     * @return
     */
    boolean updateTeacher(Teacher teacher);

    /**
     * 重置教师密码
     *
     * @param id：根据此id重置密码
     * @return
     */
    boolean resetTeacher(int id);

}
