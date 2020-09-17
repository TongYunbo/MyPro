package com.tzcpa.controller.teacher;

import com.alibaba.fastjson.JSONObject;
import com.tzcpa.controller.result.ResponseResult;
import com.tzcpa.model.teacher.Clazz;
import com.tzcpa.service.teacher.EventProcessService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName EventProcessController
 * @Description
 * @Author hanxf
 * @Date 2019/5/6 15:28
 * @Version 1.0
 **/
@RestController
@RequestMapping("/eventProcess")
@Slf4j
public class EventProcessController {

    @Autowired
    private EventProcessService eventProcessService;
    
    /**
     * 查询模板流程列表
     * @author WangYao
     * @date 2019年5月7日
     * @param jsonObject currentPage：当前页数；classId：班级id
     * @return
     */
    @RequestMapping("/conf/list")
    public ResponseResult<Map<String, Object>> getConfEventFlow(@RequestBody JSONObject jsonObject){
    	log.info("查询模板流程列表 参数jsonObject={}",jsonObject);
    	int currentPage = jsonObject.getInteger("currentPage");
    	String classId = jsonObject.getString("classId");
    	int isTeaOrAdmin = jsonObject.getInteger("isTeaOrAdmin");
        HashMap<String, Object> info = eventProcessService.getConfEventFlow(currentPage, classId, isTeaOrAdmin);
        Map<String, Object> map =new HashMap<>();
        map.put("info", info);
        return new ResponseResult<Map<String, Object>>(map);
    }
    
    /**
     * 修改/添加 模板/班级流程 的背景资料和题干描述
     * @author WangYao
     * @date 2019年5月7日
     * @param jsonObject bkgroundDesc：背景资料描述；questionDesc：题干描述；id：事件id；questionId：问题id；classId：班级id；eventName：事件名称；timeLine：时间线
     * @return
     */
    @RequestMapping("/conf/insertOrUpdateQuestion")
    public ResponseResult<String> insertOrUpdateQuestion(@RequestBody JSONObject jsonObject){
        log.info("添加修改班级 参数jsonObject={}",jsonObject);
    	String bkgroundDesc = (String)jsonObject.get("bkgroundDesc");
    	String questionDesc = (String)jsonObject.get("questionDesc");
    	int eventId = (int)jsonObject.get("id");
    	String questionId = (String)jsonObject.get("questionId");
    	String classId = (String)jsonObject.get("classId");
    	String eventName = (String)jsonObject.get("eventName");
    	String timeLine = (String)jsonObject.get("timeLine");
    	String questionYear = timeLine.substring(0, 4);
    	boolean b = eventProcessService.insertOrUpdateQuestion(bkgroundDesc, questionDesc, eventId, questionId, classId, eventName, questionYear);
    	if(b==true){
    		return new ResponseResult<String>("0","操作成功！");
    	}else{
    		return new ResponseResult<String>("-1","操作失败！");
    	}
    }
    
    /**
     * 停用/启用模板流程的题干
     * @author WangYao
     * @date 2019年5月8日
     * @param jsonObject id：模板流程的id；isShow：是否展示：1：否；0：是
     * @return
     */
    @RequestMapping("/conf/blockUpQuestion")
    public ResponseResult<String> blockUpEventFlow(@RequestBody JSONObject jsonObject){
    	log.info("停用/启用模板流程的题干 参数jsonObject={}",jsonObject);
    	int id = (int) jsonObject.get("id");
    	int isShow = (int) jsonObject.get("isShow");
    	int isshow = eventProcessService.blockUpEventFlow(id, isShow);
        if (isshow == 0) {
            return new ResponseResult<String>("0", "已启用");
        } else {
            return new ResponseResult<String>("0", "已禁用");
        }
    }
    
    /**
     * 停用/启用模板流程的背景资料
     * @author WangYao
     * @date 2019年5月8日
     * @param jsonObject questionId：问题描述的id；isShow：背景资料是否显示：显示：0 背景禁用：1；classId：班级id
     * @return
     */
    @RequestMapping("/conf/blockUpBkGround")
    public ResponseResult<String> blockUpBackGroundDesc(@RequestBody JSONObject jsonObject){
    	log.info("停用/启用模板流程的背景资料 参数jsonObject={}",jsonObject);
    	int id = (int) jsonObject.get("id");
    	int isShow = (int) jsonObject.get("isShow");
    	String classId = (String)jsonObject.get("classId");
    	String questionId = jsonObject.getString("questionId");
    	int isshow = eventProcessService.blockUpBackGroundDesc(id, isShow, classId, questionId);
        if (isshow == 0) {
            return new ResponseResult<String>("0", "已启用");
        } else {
            return new ResponseResult<String>("0", "已禁用");
        }
    }
    
    /**
     * 修改答题时限（分）
     * @author WangYao
     * @date 2019年5月8日
     * @param jsonObject duration：本题的做题时间单位分；id：问题描述的id；classId：班级id
     * @return
     */
    @RequestMapping("/conf/updateDuration")
    public ResponseResult<String> updateDuration(@RequestBody JSONObject jsonObject){
    	log.info("修改答题时限（分） 参数jsonObject={}",jsonObject);
    	int duration = jsonObject.getInteger("duration");
    	int id = jsonObject.getInteger("id");
    	String classId = jsonObject.getString("classId");
    	String questionId = jsonObject.getString("questionId");
    	boolean b = eventProcessService.updateDuration(duration, id, classId, questionId);
    	if(b==true){
    		return new ResponseResult<String>("0","修改成功！");
    	}else{
    		return new ResponseResult<String>("-1","修改失败！");
    	}
    }
    
    /**
     * 初始化班级流程配置信息
     * @author WangYao
     * @date 2019年5月8日
     * @param
     * @return
     */
    @RequestMapping("/conf/index")
    public ResponseResult<Map<String, Object>> indexClassEventFlow(@RequestBody JSONObject jsonObject){
    	log.info("添加修改班级  jsonObject={}", jsonObject);
    	int isTeaOrAdmin = jsonObject.getInteger("isTeaOrAdmin");
    	int teacherId = jsonObject.getInteger("teacherId");
    	List<Clazz> lists = eventProcessService.indexClassEventFlow(isTeaOrAdmin, teacherId);
    	Map<String, Object> map = new HashMap<>();
    	map.put("lists", lists);
        return new ResponseResult<Map<String, Object>>(map);
    }
    
    /**
     * 停用/启用班级流程的题干
     * @author WangYao
     * @date 2019年5月8日
     * @param jsonObject id：班级流程的id；isShow：是否展示：1：否；0：是；classId：班级id
     * @return
     */
    @RequestMapping("/conf/blockUpClassFlow")
    public ResponseResult<String> blockUpClassFlow(@RequestBody JSONObject jsonObject){
    	log.info("停用/启用班级流程的题干 参数jsonObject={}",jsonObject);
    	int id = (int) jsonObject.get("id");
    	int isShow = (int) jsonObject.get("isShow");
    	String classId = (String)jsonObject.get("classId");
    	int isshow = eventProcessService.blockUpClassFlow(id, isShow, classId);
        if (isshow == 0) {
            return new ResponseResult<String>("0", "已启用");
        } else {
            return new ResponseResult<String>("0", "已禁用");
        }
    }
}
