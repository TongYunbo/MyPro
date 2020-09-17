package com.tzcpa.service.teacher.impl;

import com.tzcpa.constant.TeacherInfoConstant;
import com.tzcpa.mapper.teacher.ClassMapper;
import com.tzcpa.model.PageBean;
import com.tzcpa.model.teacher.Clazz;
import com.tzcpa.model.teacher.Teacher;
import com.tzcpa.service.teacher.ClassService;
import com.tzcpa.service.treatment.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;

/**
 * 用户管理---班级管理
 *
 * @author WangYao
 * <p>
 * 2019年4月28日
 */
@Service
public class ClassServiceImpl implements ClassService {

    @Resource
    private ClassMapper classMapper;
    @Resource
    private InitEventProcessService initEventProcessService;
    @Resource
    private InitQuestionDescService initQuestionDescService;
    @Resource
    private InitClassQuestionOptionService initClassQuestionOptionService;
    @Resource
    private InitClassAnswerService initClassAnswerService;
    @Resource
    private InitDataEffectService initDataEffectService;
    @Resource
    private InitClassIntermediateVariableService initClassIntermediateVariableService;
    @Resource
    private InitClassIntermediateService initClassIntermediate;
    @Resource
    private InitClassBalancedScorecardService initClassBalancedScorecardService;
    @Resource
    private InitClassBalanceSheetService initClassBalanceSheetService;
    @Resource
    private InitClassBalanceSheetDeployService initClassBalanceSheetDeployService;
    @Resource
    private InitClassProfitStatementService initClassProfitStatementService;
    @Resource
    private InitClassFixedService initClassFixedService;
    @Resource
    private InitClassFixedParamService initClassFixedParamService;
    @Resource
    private InitClassCostQuoteService initClassCostQuoteService;
    @Resource
    private InitClassSaleUnivalenceService initClassSaleUnivalenceService;
    @Resource
    private InitClassStrategicParametersService initClassStrategicParametersService;
    @Resource
    private InitClassMenuService classMenuService;
    @Resource
    private InitClassStrategyMapService initClassStrategyMapService;
    @Resource
    private InitClassPickupComparisonService initClassPickupComparisonService;
    @Resource
    private InitClassSyntheticAbilityService initClassSyntheticAbilityService;
    @Resource
    private InitClassBalancedScorecardTargetValueService initClassBalancedScorecardTargetValueService;
    /**
     * 查询班级列表信息
     * @author WangYao
     * @param currentPage 当前页
     * @return
     */
    @Override
    public PageBean<Clazz> findByPage(int currentPage) {
        HashMap<String, Object> map = new HashMap<String, Object>();
        PageBean<Clazz> pageBean = new PageBean<Clazz>();

        //封装当前页数
        pageBean.setCurrPage(currentPage);

        //每页显示的数据
        int pageSize = 5;
        pageBean.setPageSize(pageSize);

        //封装总记录数
        int totalCount = classMapper.selectCount();
        pageBean.setTotalCount(totalCount);

        //封装总页数
        double tc = totalCount;
        //向上取整
        Double num = Math.ceil(tc / pageSize);
        pageBean.setTotalPage(num.intValue());

        map.put("start", (currentPage - 1) * pageSize);
        map.put("size", pageBean.getPageSize());
        //封装每页显示的数据
        List<Clazz> lists = classMapper.findByPage(map);
        pageBean.setLists(lists);
        return pageBean;
    }

    /**
     * @author WangYao
     * 查询教师名称
     * @return
     */
    @Override
    public List<Teacher> getTeacherName() {
        return classMapper.getTeacherName();
    }

    /**
     * 新增班级信息
     * @author WangYao
     * @param className 班级名称
     * @param tId 教师id
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean addClassInfo(String className, int tId) {
        //1.班级名称不能重复,查询数据库中班级名称和className是否存在
        String str = classMapper.selectCnameByClassName(className);
        //如果存在，则不能添加
        if (str != null && str.length() != 0) {
            return false;
        } else {
            //如果不存在，则能添加
            //1.新增班级对象
            Clazz clazz = new Clazz();
            clazz.setClassName(className);
            clazz.setIsDel(TeacherInfoConstant.ISDELNO);
            classMapper.insertClassInfo(clazz);
            clazz.setTId(tId);
            //2.新增班级教师表的关联关系信息
            classMapper.insertClassTeacherInfo(clazz);

            // 数据初始化
            this.InitClassIntermediate(clazz);


            return true;
        }
    }

    /**
     * 初始化班级中间表数据
     *
     * @Author hanxf
     * @Date 10:30 2019/5/23
     * @param clazz 班级信息
     * @return void
    **/
    private void InitClassIntermediate(Clazz clazz) {
        //3.班级添加成功后会初始化数据  hanxf
        //初始化事件流程
        initEventProcessService.initEventProcess(clazz.getId());
        //初始化题目
        initQuestionDescService.initClassQuestionDesc(clazz.getId());
        //初始化选项
        initClassQuestionOptionService.initClassQuestionOption(clazz.getId());
        //初始化标准答案
        //计分
        initClassAnswerService.initClassAnswerScore(clazz.getId());
        //影响
        initClassAnswerService.initClassAnswerImfact(clazz.getId());
        //初始化数据影响
        initDataEffectService.initDataEffect(clazz.getId());
        //中间变量表影响
        initClassIntermediateVariableService.InitClassIntermediateVariable(clazz.getId());
        //初始化成本引用
        initClassCostQuoteService.initClassCostQuote(clazz.getId());
        //初始化销售量配置表
        initClassSaleUnivalenceService.initClassSaleUnivalence(clazz.getId());
        //初始化班级战略参数
        initClassStrategicParametersService.initClassStrategicParameters(clazz.getId());
        //初始化基准数据
        initClassIntermediate.initClassIntermediate(clazz.getId());
        //初始化战略地图
        initClassStrategyMapService.initClassStrategyMap(clazz.getId());
        //初始化皮卡对应表
        initClassPickupComparisonService.initClassPickupComparison(clazz.getId());

        //4.班级添加成功后会初始化数据 wangyao
        //初始化平衡记分卡
        initClassBalancedScorecardService.initClassBalancedScorecard(clazz.getId());
        //初始化平衡记分卡目标值
        initClassBalancedScorecardTargetValueService.initClassBalancedScorecardTargetValue(clazz.getId());
        //初始化利润表
        initClassProfitStatementService.initClassProfitStatement(clazz.getId());
        //初始化资产负债表
        initClassBalanceSheetService.initClassBalanceSheet(clazz.getId());
        //初始化资产负债表配置信息
        initClassBalanceSheetDeployService.initClassBalanceSheetDeploy(clazz.getId());
        //初始化固定费用表信息
        initClassFixedService.initClassFixed(clazz.getId());
        //初始化固定值配置信息
        initClassFixedParamService.initClassFixedParam(clazz.getId());
        //初始化班级菜单
        classMenuService.initClassMenu(clazz.getId());
        //初始化综合能力
        initClassSyntheticAbilityService.initClassSyntheticAbility(clazz.getId());

    }

    /**
     * 根据班级id查看教师列表
     *
     * @param classId 班级id
     * @return
     */
    @Override
    public List<Teacher> getClassToTeacherLists(int classId) {
        return classMapper.getClassToTeacherLists(classId);
    }

    /**
     * 修改班级信息
     * @author WangYao
     * @param id 班级id
     * @param className 班级名称
     * @param tId 教师Id
     * @return
     */
    @Override
    public boolean updateClassInfo(int id, String className, int tId) {
        Clazz clazz = new Clazz();
        clazz.setId(id);
        clazz.setClassName(className);
        clazz.setTId(tId);
        //1.根据id和className查看是否修改了className
        String cName = classMapper.getClassNameById(clazz);
        //1.(1)如果cName不为空，则表示没有修改className，直接进行修改
        if (cName != null && cName.length() != 0) {
            //2.修改班级信息：
            //2(1).根据id先删除关联表的所有信息
            classMapper.deleteClassTeacherByClassid(id);
            //2(2).再将信息插入关联表
            classMapper.insertClassTeacherInfo(clazz);
            //2(3).最后修改班级表的信息
            classMapper.updateClassNameById(clazz);
            return true;
        } else {
            //1.(2)如果cName为空，则表示修改了className
            //3.根据className判断className是否重复
            String clName = classMapper.selectCnameByClassName(className);
            if (clName != null && clName.length() != 0) {
                //3(1).如果clName不为空，则表示班级名称重复，不能进行修改
                return false;
            } else {
                //3(2).如果clName为空，则表示班级名称没重复，可以进行修改
                //2.修改班级信息：
                //2(1).根据id先删除关联表的所有信息
                classMapper.deleteClassTeacherByClassid(id);
                //2(2).再将信息插入关联表
                classMapper.insertClassTeacherInfo(clazz);
                //2(3).最后修改班级表的信息
                classMapper.updateClassNameById(clazz);
                return true;
            }
        }
    }

    @Override
    public List<Clazz> getClassByUser(String account,String className) {
        return classMapper.findClassByteacher(account,className);
    }

}
