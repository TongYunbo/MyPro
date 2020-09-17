package com.tzcpa.service.student;

import com.tzcpa.model.student.TeamAndRoleVO;
import com.tzcpa.model.teacher.Team;

import java.util.List;
import java.util.Map;

/**
 * 学员管理
 *
 * @author wangbj
 * <p>
 * 2019年4月28日
 */
public interface StudentService {

    /**
     * 完善团队信息
     * @param teamAndRoleVO
     */
    Map<String, Object> editTeamAndRole(TeamAndRoleVO teamAndRoleVO);

    List<Team> getTeamByClass(Integer classId);

}
