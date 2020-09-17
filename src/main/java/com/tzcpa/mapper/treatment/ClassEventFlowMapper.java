package com.tzcpa.mapper.treatment;

import com.tzcpa.model.teacher.Clazz;
import com.tzcpa.model.treatment.ClassEventFlowDO;
import com.tzcpa.model.treatment.ClassQuestionDescDO;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

/**
 * @Author hanxf
 * @Description
 * @Date 15:11 2019/5/6
 * @Param
 * @return
 **/
public interface ClassEventFlowMapper {

    /**
     * 新增事件
     * @param param
     * @return
     */
	Integer insertClassEventFlow(ClassEventFlowDO param);

    /**
     * 初始化班级流程信息
     *
     * @param classId 班级id
     * @return
     */
    int initEventFlow(@Param(value = "classId") int classId);

	/**
	 * 获取下一个事件
	 * @param param
	 * @return
	 */
	ClassEventFlowDO getNextEvent(ClassEventFlowDO param);

	/**
	 * 查询模板流程列表的总条数
	 * @author WangYao
	 * @date 2019年5月7日
	 * @return
	 */
	int selectCount();

	/**
	 * 查询模板流程列表
	 * @author WangYao
	 * @date 2019年5月7日
	 * @param map  分页数据
	 * @return
	 */
	List<Map<String, Object>> getConfEventFlow(HashMap<String, Object> map);

	/**
	 * 根据eventKey判断在问题描述表中event_key是否存在
	 * @author WangYao
	 * @date 2019年5月7日
	 * @param descDo
	 * @return
	 */
	String getEventKeyByEid(ClassQuestionDescDO descDo);

	/**
	 * 修改班级的背景资料或题干描述
	 * @author WangYao
	 * @date 2019年5月7日
	 * @param descDo 问题描述对象
	 */
	void updateQuestion(ClassQuestionDescDO descDo);

	/**
	 * 添加班级的背景资料或题干描述
	 * @author WangYao
	 * @date 2019年5月7日
	 * @param descDo  问题描述对象
	 */
	void insertQuestion(ClassQuestionDescDO descDo);

	/**
	 * 修改模板流程信息
	 * @author WangYao
	 * @date 2019年5月8日
	 * @param descDo  问题描述对象
	 */
	void updateDescDo(ClassEventFlowDO descDo);

	/**
	 * 修改答题时限（分）
	 * @author WangYao
	 * @date 2019年5月8日
	 * @param descDo  问题描述对象
	 */
	void updateDuration(ClassQuestionDescDO descDo);

	/**
	 * 查询班级流程的总记录数
	 * @author WangYao
	 * @param classId  班级id
	 * @date 2019年5月8日
	 * @return
	 */
	int selectClassEventCount(int classId);
	
	/**
	 * 初始化班级流程配置信息
	 * @author WangYao
	 * @date 2019年5月8日
	 * @return
	 */
	List<Clazz> getIndexClassFlowInfo();

	/**
	 * 根据classId查询班级流程列表信息
	 * @author WangYao
	 * @date 2019年5月8日
	 * @param map  分页数据
	 * @return
	 */
	List<Map<String, Object>> getClassFlowInfoByClassId(HashMap<String, Object> map);

	/**
	 * 停用/启用班级流程的题干
	 * @author WangYao
	 * @date 2019年5月8日
	 * @param descDo  问题描述对象
	 */
	void updateClassFlow(ClassEventFlowDO descDo);

	/**
	 * 模板流程配置的修改
	 * @author WangYao
	 * @date 2019年5月13日
	 * @param descDo  问题描述对象
	 */
	void updateConfQuestion(ClassQuestionDescDO descDo);

	/**
	 * 根据eventId和classId判断在问题描述表中eventId是否存在
	 * @author WangYao
	 * @date 2019年5月13日
	 * @param descDo  问题描述对象
	 * @return
	 */
	String getEventKeyByEidAndCid(ClassQuestionDescDO descDo);

	/**
	 * 停用/启用班级的背景资料和题干
	 * @author WangYao
	 * @date 2019年5月13日
	 * @param questionDesc 问题描述
	 */
	void updateClassDuration(ClassQuestionDescDO questionDesc);

	/**
	 * 添加模板的背景资料或题干描述
	 * @author WangYao
	 * @date 2019年5月14日
	 * @param descDo  问题描述对象
	 */
	void insertQuestionConf(ClassQuestionDescDO descDo);

	/**
	 * 根据eventId和classId,id判断在问题描述表中eventId是否存在
	 * @author WangYao
	 * @date 2019年5月21日
	 * @param eventName
	 * @return
	 */
	int getEventKeyByEidAndCidAndId(String eventName);

	/**
	 * 根据问题名称查询conf表中的id
	 * @author WangYao
	 * @date 2019年5月21日
	 * @param eventName 事件名称
	 * @return
	 */
	int getEventIdFromConfByTitle(String eventName);

	/**
	 * 查询该老师下的班级
	 * @author WangYao
	 * @date 2019年6月3日
	 * @param teacherId
	 * @return
	 */
	List<Clazz> getTeacherClassFlowInfo(int teacherId);

}