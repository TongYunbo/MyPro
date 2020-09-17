/**
 *
 */
package com.tzcpa.service.teacher.impl;

import com.tzcpa.constant.TeacherInfoConstant;
import com.tzcpa.mapper.teacher.TeamMapper;
import com.tzcpa.model.PageBean;
import com.tzcpa.model.teacher.Clazz;
import com.tzcpa.model.teacher.StuRole;
import com.tzcpa.model.teacher.Team;
import com.tzcpa.service.teacher.TeamService;
import com.tzcpa.service.treatment.InitTeamBalanceSheetService;
import com.tzcpa.service.treatment.InitTeamBalancedScorecardService;
import com.tzcpa.service.treatment.InitTeamIntermediateService;
import com.tzcpa.service.treatment.InitTeamProfitStatementService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;

/**
 * 用户管理页面---学员管理
 *
 * @author WangYao
 * <p>
 * 2019年5月5日
 */
@Service
public class TeamServiceImpl implements TeamService {

    @Resource
    private TeamMapper teamMapper;
    @Resource
    private InitTeamIntermediateService initTeamIntermediateService;
    @Resource
    private InitTeamBalancedScorecardService initTeamBalancedScorecardService;
    @Resource
    private InitTeamBalanceSheetService initTeamBalanceSheetService;
    @Resource
    private InitTeamProfitStatementService initTeamProfitStatementService;

    /**
     * 查询学员列表信息
     * @param currentPage 当前页
     * @return
     */
    @Override
    public HashMap<String, Object> findByPage(int currentPage) {
        HashMap<String, Object> map = new HashMap<String, Object>();
        PageBean<Team> pageBean = new PageBean<Team>();

        //封装当前页数
        pageBean.setCurrPage(currentPage);

        //每页显示的数据
        int pageSize = 5;
        pageBean.setPageSize(pageSize);

        //封装总记录数
        int totalCount = teamMapper.selectCount();
        pageBean.setTotalCount(totalCount);

        //封装总页数
        double tc = totalCount;
        //向上取整
        Double num = Math.ceil(tc / pageSize);
        pageBean.setTotalPage(num.intValue());

        map.put("start", (currentPage - 1) * pageSize);
        map.put("size", pageBean.getPageSize());
        //封装每页显示的数据
        //查询团队列表信息
        List<Team> lists = teamMapper.findByPage(map);
        //查询学员角色列表信息
        List<StuRole> stuRoleLists = teamMapper.getStuRoleInfo();
        //查询班级信息
        List<Clazz> clazzLists = teamMapper.getClazzInfo(null,0);
        pageBean.setLists(lists);
        HashMap<String, Object> teamLists = new HashMap<>();
        teamLists.put("pageBean", pageBean);
        teamLists.put("stuRoleLists", stuRoleLists);
        teamLists.put("clazzLists", clazzLists);
        return teamLists;
    }

    /**
     * 添加团队之班级回显
     * @return
     */
    @Override
    public List<Clazz> getClazzInfo(String teacherAccount) {

        List<Clazz> clazz = teamMapper.getClazzInfo(teacherAccount,1);
        return clazz;
    }


    /**
     * 添加团队信息
     * @param team
     * @return
     */
    @Override
    public boolean insertTeamInfo(Team team) throws Exception{
        //1.判断账号是否重复
        String account = teamMapper.selectAccountByTeam(team);
        //如果不为空，证明账号重复了，则提示账号不能重复
        if (account != null && account.length() != 0) {
            return false;
        } else {
            //如果为空，证明账号不重复，可以添加
        	team.setIsDel(TeacherInfoConstant.ISDELNO);
        	team.setPassword(TeacherInfoConstant.DEFAULTPASSWORD);
            teamMapper.insertTeam(team);

            //初始化团队的基础数据
            this.initTeamIntermediate(team);

            return true;
        }
    }

    /**
     * 初始化团队的基础数据
     *
     * @Author hanxf
     * @Date 11:00 2019/5/23
    **/
    private void initTeamIntermediate(Team team) throws Exception{
        //初始化团队中间表基准数据
        initTeamIntermediateService.initTeamIntermediate(team.getClassId(),team.getId());

        //初始化平衡记分卡
        initTeamBalancedScorecardService.initTeamBalancedScorecard(team.getClassId(),team.getId());

        //初始化资产负债表
        initTeamBalanceSheetService.initTeamBalanceSheetService(team.getClassId(),team.getId());

        //初始化利润表
        initTeamProfitStatementService.initTeamProfitStatement(team.getClassId(),team.getId());
    }

    /**
     * 重置团队密码为默认密码
     * @param id：团队id
     * @return
     */
    @Override
    public boolean resetTeamPwd(int id) {
        Team team = new Team();
        team.setId(id);
        team.setPassword(TeacherInfoConstant.DEFAULTPASSWORD);
        teamMapper.updateTeam(team);
        return true;
    }

    /**
     * 停用团队
     *
     * @param id：团队id
     * @param isdel：团队是否删除
     * @return
     */
    @Override
    public Integer blockUpTeam(int id, int isdel) {
        Team team = new Team();
        //如果isdel的值为0，则可以进行禁用
        if (isdel == TeacherInfoConstant.ISDELNO) {
            team.setId(id);
            team.setIsDel(TeacherInfoConstant.ISDELYES);
            teamMapper.updateTeam(team);
            return 1;
        }
        //如果isdel的值为2，则可以进行启用
        else {
            team.setId(id);
            team.setIsDel(TeacherInfoConstant.ISDELNO);
            teamMapper.updateTeam(team);
            return 0;
        }
    }

    /**
     * 根据团队id查看班级信息
     *
     * @param id 团队id
     * @return
     */
    @Override
    public List<Clazz> getClazzInfoById(int id) {
        return teamMapper.getClazzInfoById(id);
    }

    /**
     * 修改团队信息
     *
     * @param id      团队id
     * @param account 团队账号
     * @param classId 团队对应的班级id
     * @return
     */
    @Override
    public boolean updateTeamInfo(int id, String account, int classId) {
        Team team = new Team();
        team.setId(id);
        team.setAccount(account);
        team.setClassId(classId);
        //1.判断团队账号是否重复
        String act = teamMapper.getAccountByNotId(team);
        //如果查到的账号存在，则不能修改 ，提示账号不能重复
        if (act != null && act.length() != 0) {
            return false;
        } else {
            //如果没有查询到重复的账号，则修改
            teamMapper.updateTeam(team);
            return true;
        }
    }
}
