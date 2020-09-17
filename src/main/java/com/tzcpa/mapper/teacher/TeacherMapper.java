package com.tzcpa.mapper.teacher;

import com.tzcpa.model.teacher.Teacher;
import org.apache.ibatis.annotations.Param;

import java.util.HashMap;
import java.util.List;

/**
 * 用户管理---教师管理
 */
public interface TeacherMapper {

    /**
     * 查询教师列表信息(分页)
     *
     * @param map：start：起始值的下标；size：每页多少条数据
     * @return
     */
    List<Teacher> findByPage(@Param(value = "map") HashMap<String, Object> map);

    /**
     * 查询教师列表信息总记录数
     *
     * @return
     */
    Integer selectCount();

    /**
     * 根据前台传来的教师账号查询数据库是否有相同账号
     *
     * @param teacher：教师信息
     * @return
     */
    String selectAccount(Teacher teacher);

    /**
     * 添加教师对象信息
     *
     * @param teacher：添加的教师信息
     */
    int addTeacher(Teacher teacher);

    /**
     * 修改教师信息
     *
     * @param teacher：修改的教师信息
     */
    int updateTeacher(Teacher teacher);

    /**
     * 根据id查看教师密码
     *
     * @param id
     * @return
     */
    Integer selectPassword(int id);

    /**
     * 根据教师的账号和id查询教师名称
     *
     * @param teacher
     * @return
     */
    String selectNameByAccount(Teacher teacher);

    /**
     * 根据id查看教师账号
     *
     * @param teacher
     * @return
     */
    String selectAccountById(Teacher teacher);

    /**
     * 教师登录 根据账号查询教师信息
     *
     * @param account
     * @return
     */
    Teacher teacherLogin(String account);
}
