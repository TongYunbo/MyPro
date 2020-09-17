package com.tzcpa.controller.teacher;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.tzcpa.constant.Constant;
import com.tzcpa.controller.result.ResponseResult;
import com.tzcpa.model.PageBean;
import com.tzcpa.model.teacher.Clazz;
import com.tzcpa.model.teacher.Teacher;
import com.tzcpa.service.teacher.ClassService;
import com.tzcpa.utils.UserSessionUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 用户管理---班级管理
 *
 * @author WangYao
 * <p>
 * 2019年4月28日
 */
@RestController
@RequestMapping("teacher/class")
@Slf4j
public class ClassController {

    @Autowired
    private ClassService classService;

    /**
     * 查询班级列表信息
     * @author WangYao
     * @param jsonObject:currentPage 当前页
     * @return
     */
    @RequestMapping("/list")
    public ResponseResult<Map<String, Object>> getClassList(@RequestBody JSONObject jsonObject) {
    	log.info("查询班级列表信息 参数jsonObject={}",jsonObject);
        int currentPage = jsonObject.getInteger("currentPage");
        PageBean<Clazz> page = classService.findByPage(currentPage);
        HashMap<String, Object> map = new HashMap<>();
        map.put("classList", page);
        return new ResponseResult<Map<String, Object>>(map);
    }

    /**
     * 查询教师名称（添加班级之教师列表回显）
     * @author WangYao
     * @return
     */
    @RequestMapping("getTeacherName")
    public ResponseResult<Map<String, Object>> getTeacherName() {
        HashMap<String, Object> map = new HashMap<>();
        List<Teacher> teacherLists = classService.getTeacherName();
        map.put("teacherLists", teacherLists);
        return new ResponseResult<Map<String, Object>>(map);
    }

    /**
     * 新增班级信息
     * @author WangYao
     * @param jsonObject className:班级名称；teacherLists：班级所对应的的教师列表
     * @return
     */
    @RequestMapping("/save")
    public ResponseResult<String> saveClassInfo(@RequestBody JSONObject jsonObject) {
    	log.info("新增班级信息 参数jsonObject={}",jsonObject);
        String className = jsonObject.getString("className");
        int tId = jsonObject.getInteger("tId");
        boolean b = classService.addClassInfo(className, tId);
        if (b == true) {
            return new ResponseResult<String>("0", "添加成功");
        } else {
            return new ResponseResult<String>("0", "班级名称已存在！");
        }
    }

    /**
     * 根据班级id查看教师列表
     * @author WangYao
     * @param jsonObject classId：班级id；teacherLists：班级所对应的的教师列表
     * @return
     */
    @RequestMapping("/getClassToTeacherLists")
    public ResponseResult<Map<String, Object>> getClassToTeacherLists(@RequestBody JSONObject jsonObject) {
    	log.info("根据班级id查看教师列表 参数jsonObject={}",jsonObject);
        int classId = jsonObject.getInteger("id");
        List<Teacher> teacherList = classService.getClassToTeacherLists(classId);
        HashMap<String, Object> map = new HashMap<>();
        map.put("teacherList", teacherList);
        return new ResponseResult<Map<String, Object>>(map);
    }

    /**
     * 修改班级信息
     * @author WangYao
     * @param jsonObject id:班级id；className：班级名称；teacherLists：班级所对应的教师列表
     * @return
     */
    @RequestMapping("/updateClassInfo")
    public ResponseResult<String> updateClassInfo(@RequestBody JSONObject jsonObject) {
    	log.info("修改班级信息 参数jsonObject={}",jsonObject);
        int id = (int) jsonObject.get("id");
        String className = jsonObject.getString("className");
        int tId = jsonObject.getInteger("tId");
        boolean b = classService.updateClassInfo(id, className, tId);
        if (b == true) {
            return new ResponseResult<String>("0", "修改成功");
        } else {
            return new ResponseResult<String>("0", "班级名称已存在！");
        }
    }

    /**
     * @author:     wangzhangju
     * @date:       2019/5/24 9:38
     * @param:      null
     * @return:
     * @exception:
     * @description: 根据当前登陆账号查询班级
     * @step:
     */
    @RequestMapping(value = "getClassByUser", method = RequestMethod.POST)
    public ResponseResult getClassByUser( @RequestBody JSONObject jsonObject) {
        PageInfo<Clazz> pageInfo=null;
        try {
            int pageNum = jsonObject.getInteger("pageNum");
            String className = jsonObject.getString("className");
            PageHelper.startPage(pageNum, 10);
            Teacher teacher = UserSessionUtil.getTeacher();
            String account = teacher.getAccount();
            if(Constant.ACCOUNT_ADMIN.equals(account)){
                account = null;
            }
            List<Clazz>  clazzList = classService.getClassByUser(teacher.getAccount(),className);
            pageInfo = new PageInfo<Clazz>(clazzList);
        }catch (Exception e){
            log.error("查询用户下的班级失败",e);
        }
        return ResponseResult.success(pageInfo);
    }


}
