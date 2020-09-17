package com.tzcpa.controller.teacher;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.tzcpa.constant.TeacherInfoConstant;
import com.tzcpa.controller.result.ResponseResult;
import com.tzcpa.model.PageBean;
import com.tzcpa.model.teacher.Teacher;
import com.tzcpa.service.teacher.TeacherService;

import lombok.extern.slf4j.Slf4j;

/**
 * 用户管理---教师管理
 *
 * @author WangYao
 * <p>
 * 2019年4月28日
 */
@RestController
@RequestMapping("/teacher")
@Slf4j
public class TeacherController {

    @Autowired
    private TeacherService teacherService;

    /**
     * 查询教师列表信息
     *
     * @param jsonObject:currentPage 当前页
     * @return
     */
    @RequestMapping("/list")
    public ResponseResult<Map<String, Object>> getTeacherList(@RequestBody JSONObject jsonObject) {
    	log.info("查询教师列表信息 参数jsonObject={}",jsonObject);
        int currentPage = (int) jsonObject.get("currentPage");
        PageBean<Teacher> page = teacherService.findByPage(currentPage);
        HashMap<String, Object> map = new HashMap<>();
        map.put("teacherList", page);
        return new ResponseResult<Map<String, Object>>(map);
    }

    /**
     * 添加教师对象
     *
     * @param teacher 教师信息对象
     * @return
     */
    @RequestMapping("/add")
    public ResponseResult<String> addTeacher(@RequestBody Teacher teacher) {
    	log.info("添加教师对象 参数teacher={}",teacher);
        if (teacherService.addTeacher(teacher)) {
            return new ResponseResult<String>("0", "添加成功");
        } else {
            return new ResponseResult<String>("0", "账号不能重复");
        }
    }

    /**
     * 停用教师
     *
     * @param jsonObject:id 教师id;isdel：是否删除教师  0：启用；2：禁用
     * @return
     */
    @RequestMapping("/blockUp")
    public ResponseResult<String> blockUpTeacher(@RequestBody JSONObject jsonObject) {
    	log.info("停用教师 参数jsonObject={}",jsonObject);
        int id = (Integer) jsonObject.get("id");
        int isdel = (Integer) jsonObject.get("isdel");
        Integer isDel = teacherService.blockUpTeacher(id, isdel);
        if (isDel == TeacherInfoConstant.ISDELNO) {
            return new ResponseResult<String>("0", "已启用");
        } else {
            return new ResponseResult<String>("0", "已禁用");
        }
    }

    /**
     * 修改教师信息
     *
     * @param teacher 教师信息对象
     * @return
     */
    @RequestMapping("/update")
    public ResponseResult<String> updateTeacher(@RequestBody Teacher teacher) {
    	log.info("修改教师信息 参数teacher={}",teacher);
        boolean b = teacherService.updateTeacher(teacher);
        if (b == true) {
            return new ResponseResult<String>("0", "修改成功");
        } else {
            return new ResponseResult<String>("0", "账号不能重复");
        }
    }

    /**
     * 重置教师密码为默认密码
     *
     * @param jsonObject:id 教师id
     * @return
     */
    @RequestMapping("/reset")
    public ResponseResult<String> resetTeacher(@RequestBody JSONObject jsonObject) {
    	log.info("重置教师密码为默认密码 参数jsonObject={}",jsonObject);
        int id = (Integer) jsonObject.get("id");
        boolean b = teacherService.resetTeacher(id);
        if (b == true) {
            return new ResponseResult<String>("0", "操作成功", "true");
        } else {
            return new ResponseResult<String>("0", "操作失败", "false");
        }
    }
}
