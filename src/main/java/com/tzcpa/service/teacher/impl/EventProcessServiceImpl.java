package com.tzcpa.service.teacher.impl;

import com.alibaba.fastjson.JSON;
import com.tzcpa.constant.TeacherInfoConstant;
import com.tzcpa.mapper.treatment.ClassEventFlowMapper;
import com.tzcpa.model.PageBean;
import com.tzcpa.model.teacher.Clazz;
import com.tzcpa.model.treatment.ClassEventFlowDO;
import com.tzcpa.model.treatment.ClassQuestionDescDO;
import com.tzcpa.service.teacher.EventProcessService;

import lombok.extern.slf4j.Slf4j;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

/**
 * @ClassName EventProcessServiceImpl
 * @Description
 * @Author wangyao
 * @Date 2019/5/6 11:24
 * @Version 1.0
 **/
@Service("eventProcessService")
@Slf4j
public class EventProcessServiceImpl implements EventProcessService {
    private Logger logger = LoggerFactory.getLogger(EventProcessServiceImpl.class);

    @Resource
	private ClassEventFlowMapper classEventFlowMapper;

    /**
	 * 查询模板流程列表
	 * @author WangYao
	 * @date 2019年5月7日
	 * @param currentPage 当前页数
	 * @param classId 班级id
	 * @param isTeaOrAdmin 是老师还是管理员
	 * @return
	 */
	@Override
	public HashMap<String, Object> getConfEventFlow(int currentPage, String id, int isTeaOrAdmin) {
		log.info("查询模板流程列表   currentPage={},classId={}", currentPage, id);
		HashMap<String, Object> map = new HashMap<String, Object>();
        PageBean<Map<String, Object>> pageBean = new PageBean<Map<String, Object>>();
        //封装当前页数
        pageBean.setCurrPage(currentPage);
        //每页显示的数据
        int pageSize = 5;
        pageBean.setPageSize(pageSize);
        //定义列表集合
        HashMap<String, Object> info = new HashMap<>();
        //判断班级id是否为空
        //不为空，则查询班级流程信息
        if(id!=null &&id.length()>0){
        	int classId = Integer.parseInt(id);
        	//封装总记录数
            int totalCount = classEventFlowMapper.selectClassEventCount(classId);
            pageBean.setTotalCount(totalCount);

            //封装总页数
            double tc = totalCount;
            //向上取整
            Double num = Math.ceil(tc / pageSize);
            pageBean.setTotalPage(num.intValue());

            map.put("start", (currentPage - 1) * pageSize);
            map.put("size", pageBean.getPageSize());
        	map.put("classId", classId);
        	//根据classId查询模板流程列表信息
            List<Map<String, Object>> lists = classEventFlowMapper.getClassFlowInfoByClassId(map);
            pageBean.setLists(lists);
            info.put("pageBean", pageBean);
            log.info("查询班级流程列表  info={}", JSON.toJSONString(info));
            return info;
        }
        //否则，查看模板流程列表信息
        else{
        	//是管理员，直接查看模板流程列表信息
        	if(isTeaOrAdmin == TeacherInfoConstant.isAdmin){
        		//封装总记录数
                int totalCount = classEventFlowMapper.selectCount();
                pageBean.setTotalCount(totalCount);

                //封装总页数
                double tc = totalCount;
                //向上取整
                Double num = Math.ceil(tc / pageSize);
                pageBean.setTotalPage(num.intValue());

                map.put("start", (currentPage - 1) * pageSize);
                map.put("size", pageBean.getPageSize());
            	//查询模板流程列表信息
                List<Map<String, Object>> lists = classEventFlowMapper.getConfEventFlow(map);
                
                //封装每页显示的数据
                pageBean.setLists(lists);
                info.put("pageBean", pageBean);
                log.info("查询模板流程列表  info={}", JSON.toJSONString(info));
                return info;
        	}
        	//是老师，返回null
        	else{
        		return null;
        	}
        }
	}

	/**
	 * 修改/添加背景资料和题干描述
	 * @author WangYao
	 * @date 2019年5月7日
	 * @param bkgroundDesc 背景资料描述
	 * @param questionDesc 问题描述
	 * @param id 事件id
	 * @param questionId 问题id
	 * @param classId 班级id
	 * @param eventName 事件名称
	 * @param questionYear 问题年限
	 * @return
	 */
	@Override
	public boolean insertOrUpdateQuestion(String bkgroundDesc, String questionDesc, int eventId, String questionId, String classId, String eventName, String questionYear) {
		log.info("修改/添加背景资料和题干描述  bkgroundDesc={},questionDesc={}, eventId={}, questionId={}, classId={}, eventName={}, questionYear={}", bkgroundDesc, questionDesc, eventId, questionId, classId, eventName, questionYear);
		ClassQuestionDescDO descDo = new ClassQuestionDescDO();
		//1.如果classId不为空，进行班级的修改和添加
		if(classId!=null && classId.length()>0){
			int cId=Integer.parseInt(classId);
			descDo.setClassId(cId);
			//判断questionId是否为空，为空直接添加，不为空则进行修改
			if(questionId!=null && questionId.length()>0){
				int qId = Integer.parseInt(questionId);
				descDo.setQuestionId(qId);
				descDo.setBackgroundDesc(bkgroundDesc);
				descDo.setQuestionDesc(questionDesc);
				classEventFlowMapper.updateQuestion(descDo);
				return true;
			}else{
				//根据问题名称查询conf表中的id作为EventId
				int id = classEventFlowMapper.getEventIdFromConfByTitle(eventName);
				//根据问题名称查询conf_desc表中的id作为questionId
				int qId = classEventFlowMapper.getEventKeyByEidAndCidAndId(eventName);
				descDo.setEventId(id);
				descDo.setQuestionId(qId);
				descDo.setBackgroundDesc(bkgroundDesc);
				descDo.setQuestionDesc(questionDesc);
				descDo.setIsShow(TeacherInfoConstant.ISSHOWYES);
				descDo.setQuestionTitle(eventName);
				descDo.setQuestionYear(Integer.valueOf(questionYear));
				descDo.setParentId(TeacherInfoConstant.parentId);
				classEventFlowMapper.insertQuestion(descDo);
				return true;
			}
			
		}
		//1.如果classId为空，进行模板的修改和添加
		else{
			descDo.setEventId(eventId);
			//2.根据eventId判断在问题描述表中eventId是否存在
			String eId = (String)classEventFlowMapper.getEventKeyByEid(descDo);
			//1(1).如果存在，则为修改
			if(eId!=null && eId.length()>0){
				descDo.setBackgroundDesc(bkgroundDesc);
				descDo.setQuestionDesc(questionDesc);
				classEventFlowMapper.updateConfQuestion(descDo);
				return true;
			}
			//1(2).如果不存在，则为添加
			else{
				descDo.setEventId(eventId);
				descDo.setBackgroundDesc(bkgroundDesc);
				descDo.setQuestionDesc(questionDesc);
				descDo.setIsShow(TeacherInfoConstant.ISSHOWYES);
				descDo.setQuestionTitle(eventName);
				descDo.setQuestionYear(Integer.valueOf(questionYear));
				descDo.setParentId(TeacherInfoConstant.parentId);
				classEventFlowMapper.insertQuestionConf(descDo);
				return true;
			}
		}
	}

	/**
	 * 停用/启用模板流程
	 * @author WangYao
	 * @date 2019年5月8日
	 * @param id 事件id
	 * @param isShow 是否展示 0：是；1：否
	 * @return
	 */
	@Override
	public int blockUpEventFlow(int id, int isShow) {
		ClassEventFlowDO descDo = new ClassEventFlowDO();
        //如果isShow的值为0，则可以进行禁用
        if (isShow == TeacherInfoConstant.ISSHOWYES) {
        	descDo.setId(id);
        	descDo.setIsShow(TeacherInfoConstant.ISSHOWNO);
        	classEventFlowMapper.updateDescDo(descDo);
            return 1;
        }
        //如果inShow的值为1，则可以进行启用
        else {
        	descDo.setId(id);
        	descDo.setIsShow(TeacherInfoConstant.ISSHOWYES);
            classEventFlowMapper.updateDescDo(descDo);
            return 0;
        }
	}

	/**
	 * 停用/启用模板流程的背景资料
	 * @author WangYao
	 * @date 2019年5月8日
	 * @param questionId 问题id
	 * @param isShow 是否展示 0：是；1：否
	 * @param classId 班级id
	 * @return
	 */
	@Override
	public int blockUpBackGroundDesc(int id, int isShow, String classId, String questionId) {
		ClassQuestionDescDO questionDesc = new ClassQuestionDescDO();
		//判断classId是否为空
		//如果不为空，则是班级流程的背景资料的停用或启用
		if(classId!=null && classId.length()>0){
			//如果isShow的值为0，则可以进行禁用，则将背景资料isShow修改为1
	        if (isShow == TeacherInfoConstant.ISSHOWYES) {
	        	Integer cId = Integer.valueOf(classId);
	        	questionDesc.setIsShow(TeacherInfoConstant.ISSHOWNO);
	        	questionDesc.setClassId(cId);
	        	questionDesc.setQuestionId(Integer.valueOf(questionId));
	        	classEventFlowMapper.updateClassDuration(questionDesc);
	            return 1;
	        }
	        //如果isShow的值为1，则可以进行启用
	        else {
	        	Integer cId = Integer.valueOf(classId);
	        	questionDesc.setIsShow(TeacherInfoConstant.ISSHOWYES);
	        	questionDesc.setClassId(cId);
	        	questionDesc.setQuestionId(Integer.valueOf(questionId));
	        	classEventFlowMapper.updateClassDuration(questionDesc);
	            return 0;
	        }
		}
		//如果为空，则是模板的背景资料的停用或启用
		else{
			//如果isShow的值为0，则可以进行禁用，则将背景资料isShow修改为1
	        if (isShow == TeacherInfoConstant.ISSHOWYES) {
	        	questionDesc.setId(id);
	        	questionDesc.setIsShow(TeacherInfoConstant.ISSHOWNO);
	        	classEventFlowMapper.updateDuration(questionDesc);
	            return 1;
	        }
	        //如果isShow的值为1，则可以进行启用
	        else {
	        	questionDesc.setId(id);
	        	questionDesc.setIsShow(TeacherInfoConstant.ISSHOWYES);;
	        	classEventFlowMapper.updateDuration(questionDesc);
	            return 0;
	        }
		}
	}

	/**
	 * 修改答题时限（分）
	 * @author WangYao
	 * @date 2019年5月8日
	 * @param duration 本题的做题时间单位分
	 * @param id 问题描述的id
	 * @param classId 班级id
	 * @return
	 */
	@Override
	public boolean updateDuration(int duration, int id, String classId, String questionId) {
		ClassQuestionDescDO descDo = new ClassQuestionDescDO();
		//判断classId是否为空
		//如果不为空，则是班级流程的修改时限
		if(classId!=null && classId.length()>0){
			Integer cId = Integer.valueOf(classId);
			descDo.setDuration(duration);
			descDo.setClassId(cId);
			descDo.setQuestionId(Integer.valueOf(questionId));
			classEventFlowMapper.updateClassDuration(descDo);
		}
		//否则，为模板流程的修改时限
		else{
			descDo.setDuration(duration);
			descDo.setId(id);
			classEventFlowMapper.updateDuration(descDo);
		}
		return true;
	}

	/**
	 * 初始化班级流程配置信息
	 * @author WangYao
	 * @date 2019年5月8日
	 * @return
	 */
	@Override
	public List<Clazz> indexClassEventFlow(int isTeaOrAdmin, int teacherId) {
		//如果是老师，只查询该老师下的班级
		if(isTeaOrAdmin == TeacherInfoConstant.isTea){
			return classEventFlowMapper.getTeacherClassFlowInfo(teacherId);
		}
		//如果是管理员，查询模板流程列表信息
		else{
	        return classEventFlowMapper.getIndexClassFlowInfo();
		}
	}

	/**
	 * 停用/启用班级流程的题干
	 * @author WangYao
	 * @date 2019年5月8日
	 * @param id 班级流程事件id
	 * @param isShow 是否展示 0：是；1：否
	 * @param classId 班级id
	 * @return
	 */
	@Override
	public int blockUpClassFlow(int id, int isShow, String classId) {
		ClassEventFlowDO descDo = new ClassEventFlowDO();
		Integer cId = Integer.valueOf(classId);
        //如果isShow的值为0，则可以进行禁用
        if (isShow == TeacherInfoConstant.ISSHOWYES) {
        	descDo.setClassId(cId);
        	descDo.setId(id);
        	descDo.setIsShow(TeacherInfoConstant.ISSHOWNO);
        	classEventFlowMapper.updateClassFlow(descDo);
            return 1;
        }
        //如果inShow的值为1，则可以进行启用
        else {
        	descDo.setClassId(cId);
        	descDo.setId(id);
        	descDo.setIsShow(TeacherInfoConstant.ISSHOWYES);
            classEventFlowMapper.updateClassFlow(descDo);
            return 0;
        }
	}

}
