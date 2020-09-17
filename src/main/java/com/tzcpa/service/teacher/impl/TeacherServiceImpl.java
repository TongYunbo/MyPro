package com.tzcpa.service.teacher.impl;

import java.util.HashMap;
import java.util.List;

import org.springframework.stereotype.Service;

import com.tzcpa.constant.TeacherInfoConstant;
import com.tzcpa.mapper.teacher.TeacherMapper;
import com.tzcpa.model.PageBean;
import com.tzcpa.model.teacher.Clazz;
import com.tzcpa.model.teacher.Teacher;
import com.tzcpa.model.teacher.Team;
import com.tzcpa.service.teacher.TeacherService;

import javax.annotation.Resource;

/**
 * 用户管理---教师管理
 *
 * @author WangYao
 * <p>
 * 2019年4月28日
 */
@Service
public class TeacherServiceImpl implements TeacherService {

    @Resource
    private TeacherMapper teacherMapper;

    /**
     * 查询教师列表信息(分页)
     *
     * @param currentPage：当前页数
     * @return
     */
    @Override
    public PageBean<Teacher> findByPage(int currentPage) {
        HashMap<String, Object> map = new HashMap<String, Object>();
        PageBean<Teacher> pageBean = new PageBean<Teacher>();

        //封装当前页数
        pageBean.setCurrPage(currentPage);

        //每页显示的数据
        int pageSize = 5;
        pageBean.setPageSize(pageSize);

        //封装总记录数
        int totalCount = teacherMapper.selectCount();
        pageBean.setTotalCount(totalCount);

        //封装总页数
        double tc = totalCount;
        //向上取整
        Double num = Math.ceil(tc / pageSize);
        pageBean.setTotalPage(num.intValue());

        map.put("start", (currentPage - 1) * pageSize);
        map.put("size", pageBean.getPageSize());
        //封装每页显示的数据
        List<Teacher> lists = teacherMapper.findByPage(map);
        pageBean.setLists(lists);
        return pageBean;
    }

    /**
     * 添加教师对象
     *
     * @param teacher：添加的教师信息
     * @return
     */
    @Override
    public boolean addTeacher(Teacher teacher) {
    	teacher.setIsDel(TeacherInfoConstant.ISDELNO);
    	teacher.setPassword(TeacherInfoConstant.DEFAULTPASSWORD);
        //教师账号不能重复：根据教师账号查询数据库是否有相同账号（并且不是停用状态）
        String account = teacherMapper.selectAccount(teacher);
        //有重复，则不能添加
        if (account != null && account.length() != 0) {
            return false;
        } else {//没有重复，则添加
            teacherMapper.addTeacher(teacher);
            return true;
        }
    }

    /**
     * 停用教师
     *
     * @param id：根据此id停用教师
     * @param isdel：是否删除教师 0：启用；2：禁用
     * @return
     */
    @Override
    public Integer blockUpTeacher(int id, int isdel) {
        Teacher teacher = new Teacher();
        //如果isdel的值为0，则可以进行禁用
        if (isdel == TeacherInfoConstant.ISDELNO) {
            teacher.setId(id);
            teacher.setIsDel(TeacherInfoConstant.ISDELYES);
            teacherMapper.updateTeacher(teacher);
            return 2;
        }
        //如果isdel的值为2，则可以进行启用
        else {
            teacher.setId(id);
            teacher.setIsDel(TeacherInfoConstant.ISDELNO);
            teacherMapper.updateTeacher(teacher);
            return 0;
        }
    }

    /**
     * 修改教师信息
     *
     * @param teacher：修改后的教师信息
     * @return
     */
    @Override
    public boolean updateTeacher(Teacher teacher) {
        //教师账号不能重复：根据教师账号查询数据库是否有相同账号（并且不是停用状态）
        //1.查看是否修改了account，则根据id查看account和数据库中的account是否相等
        String account = teacherMapper.selectAccountById(teacher);
        if (account != null && account.length() != 0) {
            //2.账号相等的话，则代表未修改account，则直接进行修改
            teacherMapper.updateTeacher(teacher);
            return true;
        } else {
            //2.账号不相等的话，则证明当前教师id修改了account，则需要根据传来的account判断account是否和  数据库中其他account(id不是当前的id)相等
            String str = teacherMapper.selectNameByAccount(teacher);
            if (str != null && str.length() != 0) {
                //3.如果str有值，则证明account重复，则不能修改，提示账号不能重复
                return false;
            } else {
                //3.如果没有值，则证明account是新账号，可以进行修改
                teacherMapper.updateTeacher(teacher);
                return true;
            }
        }
    }

    /**
     * 重置教师密码为默认密码
     *
     * @param id：根据此id重置密码
     * @return
     */
    @Override
    public boolean resetTeacher(int id) {
        Teacher teacher = new Teacher();
        teacher.setId(id);
        teacher.setPassword(TeacherInfoConstant.DEFAULTPASSWORD);
        teacherMapper.updateTeacher(teacher);
        return true;
    }

}
