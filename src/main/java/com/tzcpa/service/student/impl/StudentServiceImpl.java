package com.tzcpa.service.student.impl;

import com.tzcpa.mapper.teacher.StuRoleMapper;
import com.tzcpa.mapper.teacher.TeamMapper;
import com.tzcpa.model.student.TeamAndRoleVO;
import com.tzcpa.model.teacher.StuRole;
import com.tzcpa.model.teacher.Team;
import com.tzcpa.service.student.StudentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 学员管理
 *
 * @author wangbj
 * <p>
 * 2019年4月28日
 */
@Service
public class StudentServiceImpl implements StudentService {

    private static Logger logger = LoggerFactory.getLogger(StudentServiceImpl.class);

    @Resource
    private TeamMapper teamMapper;

    @Resource
    private StuRoleMapper stuRoleMapper;

    /**
     * 完善团队信息
     * @param teamAndRoleVO
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> editTeamAndRole(TeamAndRoleVO teamAndRoleVO) {
        logger.info("Service.editTeamAndRole 进入");
        Map<String, Object> result = new HashMap<>();

        Team team = new Team();
        team.setTeamName(teamAndRoleVO.getTeamName());
        team.setTeamDesc(teamAndRoleVO.getTeamDesc());
        team.setTeamProspect(teamAndRoleVO.getTeamProspect());
        team.setId(teamAndRoleVO.getTeamId());
        team.setModifyPerson(teamAndRoleVO.getModifyPerson());
        teamMapper.updateTeam(team);
        logger.info("Service.editTeamAndRole 修改团队信息 成功");

        team = teamMapper.selectTeam(team);
        result.put("team", team);

        StuRole stuRole = new StuRole();
        stuRole.setTeamId(teamAndRoleVO.getTeamId());
        stuRole.setClassId(teamAndRoleVO.getClassId());

        stuRole.setRoleId(1);
        stuRole.setRoleName("CEO");
        stuRole.setUserName(teamAndRoleVO.getCeoName());
        stuRoleMapper.insertStuRole(stuRole);
        logger.info("Service.editTeamAndRole 新增CEO 成功");
        stuRole.setRoleId(4);
        stuRole.setRoleName("CFO");
        stuRole.setUserName(teamAndRoleVO.getCfoName());
        stuRoleMapper.insertStuRole(stuRole);
        logger.info("Service.editTeamAndRole 新增CFO 成功");
        stuRole.setRoleId(5);
        stuRole.setRoleName("CRO");
        stuRole.setUserName(teamAndRoleVO.getCroName());
        stuRoleMapper.insertStuRole(stuRole);
        logger.info("Service.editTeamAndRole 新增CRO 成功");
        stuRole.setRoleId(3);
        stuRole.setRoleName("COO");
        stuRole.setUserName(teamAndRoleVO.getCooName());
        stuRoleMapper.insertStuRole(stuRole);
        logger.info("Service.editTeamAndRole 新增COO 成功");
        stuRole.setRoleId(2);
        stuRole.setRoleName("CMO");
        stuRole.setUserName(teamAndRoleVO.getCmoName());
        stuRoleMapper.insertStuRole(stuRole);
        logger.info("Service.editTeamAndRole 新增CMO 成功");

        stuRole = new StuRole();
        stuRole.setClassId(team.getClassId());
        stuRole.setTeamId(team.getId());
        List<StuRole> stuRoleList = stuRoleMapper.selectStuRole(stuRole);
        result.put("stuRoleList", stuRoleList);

        logger.info("Service.editTeamAndRole 结束");
		return result;
    }

    @Override
    public List<Team> getTeamByClass(Integer classId) {
        return teamMapper.getTeamByClass(classId);
    }

}
