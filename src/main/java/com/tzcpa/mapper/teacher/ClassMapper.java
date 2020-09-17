package com.tzcpa.mapper.teacher;

import java.util.List;
import java.util.Map;

import com.tzcpa.model.teacher.Clazz;
import com.tzcpa.model.teacher.Teacher;
import org.apache.ibatis.annotations.Param;

/**
 * 用户管理---班级管理
 *
 * @author WangYao
 * <p>
 * 2019年4月28日
 */
public interface ClassMapper {

    /**
     * 查询班级列表总记录数
     *
     * @return
     */
    int selectCount();

    /**
     * 分页查询班级列表信息 
     * 
     * @param map ：start：起始值的下标；size：每页多少条数据
     * @return
     */
    List<Clazz> findByPage(Map<String, Object> map);


    List<Clazz> findClassByteacher(@Param("account") String account,@Param(value = "className") String className);
    /**
     * 查询教师名称
     *
     * @return
     */
    List<Teacher> getTeacherName();

    /**
     * 新增班级对象
     *
     * @param clazz 班级对象
     * @return
     */
    Integer insertClassInfo(Clazz clazz);

    /**
     * 新增班级教师表的关联关系信息
     *
     * @param clazz 班级对象
     */
    void insertClassTeacherInfo(Clazz clazz);

    /**
     * 根据班级id查看教师列表
     *
     * @param classId 班级id
     * @return
     */
    List<Teacher> getClassToTeacherLists(int classId);

    /**
     * 查询数据库中班级名称和className是否有重复
     *
     * @param className 班级名称
     * @return
     */
    String selectCnameByClassName(String className);

    /**
     * 根据id和className查看是否修改了className
     *
     * @param clazz 班级对象
     * @return
     */
    String getClassNameById(Clazz clazz);

    /**
     * 根据id先删除关联表的所有信息
     *
     * @param id 班级id
     */
    void deleteClassTeacherByClassid(int id);

    /**
     * 修改班级表的信息
     *
     * @param clazz 班级对象
     */
    int updateClassNameById(Clazz clazz);

    Clazz getClassByid(@Param("classId") int classId);


    int addTime(Map<String,Object> paramMap);

    String getAccountByClassId(@Param("classId") Integer classId);


}
