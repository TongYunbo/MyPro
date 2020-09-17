package com.tzcpa.mapper.teacher;

import java.util.HashMap;
import java.util.List;

import com.tzcpa.model.teacher.Clazz;
import com.tzcpa.model.teacher.StuRole;
import com.tzcpa.model.teacher.Team;
import org.apache.ibatis.annotations.Param;

/**
 * 用户管理页面---学员管理
 *
 * @author WangYao
 * <p>
 * 2019年5月5日
 */
public interface TeamMapper {

    /**
     * 选择性地添加团队信息
     *
     * @param team
     * @return
     */
    int insertTeam(Team team);

    /**
     * 选择性地修改团队信息
     *
     * @param team
     * @return
     */
    int updateTeam(Team team);

    /**
     * 学员登录 根据账号查询学员信息
     *
     * @param account
     * @return
     */
    Team teamLogin(String account);

    /**
     * 查询学员列表总记录数
     *
     * @return
     */
    Integer selectCount();

    /**
     * 查询团队列表信息
     *
     * @param map
     * @return
     */
    List<Team> findByPage(HashMap<String, Object> map);

    /**
     * 查询学员角色列表信息
     *
     * @return
     */
    List<StuRole> getStuRoleInfo();

    /**
     * 添加团队之班级回显
     *
     * @return
     */
    List<Clazz> getClazzInfo(@Param("teacherAccount") String teacherAccount,@Param(value = "flag") int flag);


    /**
     * 判断账号是否重复
     *
     * @param team
     * @return
     */
    String selectAccountByTeam(Team team);

    /**
     * 根据团队id查看班级信息
     *
     * @param id
     * @return
     */
    List<Clazz> getClazzInfoById(int id);

    /**
     * 判断团队账号是否重复
     *
     * @param team 团队对象
     * @return
     */
    String getAccountByNotId(Team team);

    /**
     * @author:     wangzhangju
     * @date:       2019/5/23 14:53
     * @param:      null
     * @return:
     * @exception:
     * @description: 获取班级下的所有team
     * @step:
     */
    List<Team> getTeamByClass( Integer classId);
	
	/**
     * 查询学员信息
     *
     * @param team
     * @return
     */
    Team selectTeam(Team team);
}
