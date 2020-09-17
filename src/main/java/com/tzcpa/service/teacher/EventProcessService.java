package com.tzcpa.service.teacher;

import java.util.HashMap;
import java.util.List;

import com.tzcpa.model.teacher.Clazz;

/**
 * @ClassName EventProcessService
 * @Description
 * @Author wangyao
 * @Date 2019/5/6 11:19
 * @Version 1.0
 **/
public interface EventProcessService {



	/**
	 * 查询模板流程列表
	 * @author WangYao
	 * @date 2019年5月7日
	 * @param currentPage 当前页数
	 * @param classId 班级id
	 * @param isTeaOrAdmin 是老师还是管理员
	 * @return
	 */
    HashMap<String, Object> getConfEventFlow(int currentPage, String classId, int isTeaOrAdmin);

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
	boolean insertOrUpdateQuestion(String bkgroundDesc, String questionDesc, int id, String questionId, String classId, String eventName, String questionYear);

	/**
	 * 停用/启用模板流程
	 * @author WangYao
	 * @date 2019年5月8日
	 * @param id 事件id
	 * @param isShow 是否展示 0：是；1：否
	 * @return
	 */
	int blockUpEventFlow(int id, int isShow);

	/**
	 * 停用/启用模板流程的背景资料
	 * @author WangYao
	 * @date 2019年5月8日
	 * @param id 问题id
	 * @param isShow 是否展示 0：是；1：否
	 * @param classId 班级id
	 * @param questionId 问题id
	 * @return
	 */
	int blockUpBackGroundDesc(int id, int isShow, String classId, String questionId);

	/**
	 * 修改答题时限（分）
	 * @author WangYao
	 * @date 2019年5月8日
	 * @param duration 本题的做题时间单位分
	 * @param id 问题描述的id
	 * @param classId 班级id
	 * @param questionId 问题id
	 * @return
	 */
	boolean updateDuration(int duration, int id, String classId, String questionId);

	/**
	 * 初始化班级流程配置信息
	 * @author WangYao
	 * @param isTeaOrAdmin 是老师还是管理员
	 * @param teacherId 教师id
	 * @date 2019年5月8日
	 * @return
	 */
	List<Clazz> indexClassEventFlow(int isTeaOrAdmin, int teacherId);

	/**
	 * 停用/启用班级流程的题干
	 * @author WangYao
	 * @date 2019年5月8日
	 * @param id 班级流程事件id
	 * @param isShow 是否展示 0：是；1：否
	 * @param classId 班级id
	 * @return
	 */
	int blockUpClassFlow(int id, int isShow, String classId);
}

