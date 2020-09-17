/**
 *
 */
package com.tzcpa.service.teacher;

import com.tzcpa.model.teacher.Clazz;
import com.tzcpa.model.teacher.Team;

import java.util.HashMap;
import java.util.List;

/**
 * 用户管理页面---学员管理
 *
 * @author WangYao
 * <p>
 * 2019年5月5日
 */
public interface TeamService {

    /**
     * 查询学员列表（分页）
     *
     * @param currentPage
     * @return
     */
    HashMap<String, Object> findByPage(int currentPage);

    /**
     * 添加团队之班级回显
     *
     * @return
     */
    List<Clazz> getClazzInfo(String teacherAccount);


    /**
     * 添加团队信息
     *
     * @param team
     * @return
     */
    boolean insertTeamInfo(Team team) throws Exception;

    /**
     * 重置团队密码为默认密码
     * @param id：团队id
     * @return
     */
    boolean resetTeamPwd(int id);

    /**
     * 停用团队
     *
     * @param id：团队id
     * @param isdel：团队是否删除
     * @return
     */
    Integer blockUpTeam(int id, int isdel);

    /**
     * 根据团队id查看班级信息
     *
     * @param id 团队id
     * @return
     */
    List<Clazz> getClazzInfoById(int id);

    /**
     * 修改团队信息
     *
     * @param id      团队id
     * @param account 团队账号
     * @param classId 团队对应的班级id
     * @return
     */
    boolean updateTeamInfo(int id, String account, int classId);

}
