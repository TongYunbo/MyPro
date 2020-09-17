package com.tzcpa.service.question;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.tzcpa.constant.NormalConstant;
import com.tzcpa.constant.SearchFinanceConstant;
import com.tzcpa.constant.VNCConstant;
import com.tzcpa.constant.VariableConstant;
import com.tzcpa.exceptions.GetValueException;
import com.tzcpa.mapper.treatment.OSMapper;
import com.tzcpa.model.student.AnswerScoreDO;
import com.tzcpa.model.student.BalanceVariableDO;
import com.tzcpa.model.student.HseRequest;
import com.tzcpa.model.student.SearchFinanceDTO;
import com.tzcpa.service.student.AHseService;
import com.tzcpa.service.student.QuestionService;
import com.tzcpa.service.treatment.InitTeamIntermediateService;
import com.tzcpa.utils.FormatNumberUtils;
import org.springframework.aop.framework.AopContext;
import org.springframework.transaction.annotation.Transactional;


/**
 * <p>Description: 研发费用投入抽象</p>
 * @author WTL
 * @date 2019年5月22日
 */
public abstract class ARDEIService extends AHseService {
	
	/**
	 * 
	 * @param osMapper
	 * @param hse
	 * @param isAddBV 是否需要添加变量到平衡积分卡
	 * @param vList {"查询纪录的变量字符名字","所属车型","做延迟更新时需要的变量"} {"YF11","h6","YFYX1,YFYX2"}
	 */
	@Transactional(rollbackFor = Exception.class)
	public void handleRDEI(OSMapper osMapper, HseRequest hse, Boolean isAddBV, List<String[]> vList,
			InitTeamIntermediateService itidService, QuestionService questionService) throws Exception {
		try {
			
			//获取本年最终收入（营业收入）
			Long operationVal = osMapper.getFinanceVal(new SearchFinanceDTO(hse, SearchFinanceConstant.TTPS_TARGET_TABLE,
					SearchFinanceConstant.OR_TARGET_COLUMN,
					Integer.valueOf(hse.getTimeLine().substring(0, 4)), null/*"and vehicle_model = '"+ vModel +"'"*/));
			//获取营业收入为0的时候返回数据为null
			if (operationVal == null) {
				throw new GetValueException("获取"+ Integer.valueOf(hse.getTimeLine().substring(0, 4)) +"年的营业收入时为：" + operationVal);
			}
			
			//获取需要填报研发费用占本年总收入的最小系数
			Double confVariable = Double.valueOf(osMapper.getConfVariable(VNCConstant.YFTRMINXS, Integer.valueOf(hse.getTimeLine().substring(0, 4))));
			//如果填报的研发费用达不到本年收入乘最小系数值，则置成本年收入乘最小系数值
			if (operationVal * confVariable > Double.valueOf(hse.getAnswer().get(0)) * 1000000) {
				List<String> list = new ArrayList<>();
				list.add(FormatNumberUtils.formatDouble(operationVal * confVariable / 1000000, FormatNumberUtils.FORMAT_DOUBLE_DECIMAL_ZERO));
				hse.setAnswer(list);
			}
			
			AnswerScoreDO score = new AnswerScoreDO(hse.getClassId(), hse.getTeamId(), hse.getTimeLine());

			//添加研报费用
			((ARDEIService) AopContext.currentProxy()).doAddRDValue(osMapper, hse);

			//处理立即执行影响（由于处理立即执行影响的时候和战略无关，而下边的延迟影响和战略有关并且需要在循环变量集合的时候处理，所以将立即处理影响放到这）
			((ARDEIService) AopContext.currentProxy()).handleImmediateImpact(osMapper, score, hse);
			//处理延迟影响需要的变量信息{"YFYX1,YFYX2",yfyx(值)}
			List<String[]> vdList = new ArrayList<>();
			//做处理积分业务
			((ARDEIService) AopContext.currentProxy()).doScore(vdList, vList, score, hse, isAddBV, osMapper);
			//处理延迟执行的影响
			((ARDEIService) AopContext.currentProxy()).handleDelayedImpact(osMapper, hse, score, vdList);


			//做题过程中 研发费用填报的题目
			itidService.updateInitInDevelopmentCost(assemblyHandleRDEIVariables(hse));
			//初始化下一年的填报研发费用数据（需要在立即影响之前执行，因为影响的是要初始化的研发费用数据）2019-06-24，从影响之前放到了影响之后
			((AHseService) AopContext.currentProxy()).initNextYearRDCost(osMapper, hse, itidService);
		}catch (Exception e){
			throw e;
		}
	}
	
	/**
	 * 组装初始化研发费用数据参数
	 * @return
	 */
	public Map<String, Object> assemblyHandleRDEIVariables(HseRequest hse){
		Map<String, Object> map = new HashMap<>();
		map.put("classId",String.valueOf(hse.getClassId()));
        map.put("teamId",String.valueOf(hse.getTeamId()));
        map.put("year",String.valueOf(hse.getTimeLine().substring(0, 4)));
        map.put("inDevelopmentCost",String.valueOf(hse.getAnswer().get(0)));
        return map;
	}
	
	/**
	 * 添加填报研发费用
	 * @param osMapper
	 * @param hse
	 * @param vValue
	 */
	@Transactional(rollbackFor = Exception.class)
	public void doAddRDValue(OSMapper osMapper, HseRequest hse) throws Exception{
		//填报的研发费用转换成分
		try {
			Double v = Double.valueOf(hse.getAnswer().get(0)) * 1000000;
			osMapper.addRDV(hse.getClassId(), hse.getTeamId(), Integer.valueOf(hse.getTimeLine().substring(0, 4)),
					VNCConstant.TBYFFYTR, new BigDecimal(Double.valueOf(FormatNumberUtils.formatDouble(v, FormatNumberUtils.FORMAT_DOUBLE_DECIMAL_ZERO))));
		}catch (Exception e){
			throw e;
		}
	}
	
	/**
	 * 做处理积分业务
	 * @param vdList
	 * @param vList
	 * @param score
	 * @param hse
	 * @param isAddBV
	 * @param osMapper
	 * @throws Exception 
	 */
	@Transactional(rollbackFor = Exception.class)
	public void doScore(List<String[]> vdList, List<String[]> vList, AnswerScoreDO score, HseRequest hse, Boolean isAddBV, OSMapper osMapper) throws Exception{
		//积分值
		try {
			double integral = 0;
			//积分需要平分的个数，有的是需要比较两次最优
			int bq = vList.size();
			//查询本题得分
			List<Map<String, Object>> checkRes = osMapper.checkRes(score);
			//查询变量的区间取值，下面需要根据上限来判断得了多少分
			Map<String, BigDecimal> section = osMapper.getVariableSection(Integer.valueOf(hse.getTimeLine().substring(0, 4)));
			//处理变量的集合
			String[] v = null;
			Map<String, Double> yfyxMap = null;
			double yfyx = 0d;
			for (int i = 0; i < vList.size(); i++) {
				v = vList.get(i);
				//获得YFYX的值并添加YFFX到纪录表
				yfyxMap = getYFYXAndAddYFFX(osMapper, hse, isAddBV, v[1], v[0]);

				yfyx = yfyxMap.get("yfyx");
				//计算得分
				integral += ((ARDEIService) AopContext.currentProxy()).handleScore(osMapper, score, hse, checkRes, section, yfyxMap.get("yfyxZB")) / bq;

				vdList.add(new String[]{v[2], String.valueOf(yfyx), v[1]});

				// 如果为最后一条的时候添加YFYX值
				if (vList.size() - 1 == i) {
					osMapper.addRDV(hse.getClassId(), hse.getTeamId(), Integer.valueOf(hse.getTimeLine().substring(0, 4)),
							VNCConstant.YFFYZSRB, new BigDecimal(yfyxMap.get("yfyxZB")));
				}
			}
			//由于查询出来的答案是所有的答案所以需要删除一条数据
			checkRes.remove(0);
			//由于为多个的时候积分是需要计算出来的，所以需要更改积分
			checkRes.get(0).put("score", integral);
			//进行添加积分处理
			((AHseService) AopContext.currentProxy()).handleAddScore(osMapper, hse, checkRes);
		}catch (Exception e){
			throw e;
		}
	}
	
	/**
	 * 处理积分
	 * @param osMapper
	 * @param score
	 * @param hse
	 * @param checkRes
	 * @param section
	 * @param yfyx
	 * @return
	 */
	private double handleScore(OSMapper osMapper, AnswerScoreDO score, HseRequest hse,
			List<Map<String, Object>> checkRes, Map<String, BigDecimal> section, double yfyx) {
		double s = 0;
		// 判断填入的研发费用是否占上年收入比的3%，是的话为最优，得分最高取第一条数据的分数，否则取第二条数据的分数
		if (yfyx == section.get("upperLimit").doubleValue()) {
			s += Double.valueOf(checkRes.get(0).get("score").toString());
		} else {
			s += Double.valueOf(checkRes.get(1).get("score").toString());
		}
		return s;
	}
	
	/**
	 * 处理延迟执行影响
	 * @param osMapper
	 * @param score
	 * @param vList {"YFYX1,YFYX2",yfyx(值),"h6"}
	 * @throws Exception 
	 */
	private void handleDelayedImpact(OSMapper osMapper, HseRequest hse, AnswerScoreDO score, List<String[]> vList) throws Exception{
		List<String[]> viList = null;
		BalanceVariableDO YFYX1Info = null;
		BalanceVariableDO YFYX2Info = null;
		//组装变量
		for (String[] v : vList) {
			viList = new ArrayList<>();
			YFYX1Info = osMapper.getVariableInfo(v[0].split(",")[0], null, null, score.getClassId());
			YFYX2Info = osMapper.getVariableInfo(v[0].split(",")[1], null, null, score.getClassId());
			viList.add(new String[] {v[0].split(",")[0], NormalConstant.SUBTRACT_EFFECT, v[1],
					YFYX1Info.getCompareMethod(), score.getTimeLine().substring(0, 4)});
			viList.add(new String[] {v[0].split(",")[1], NormalConstant.SUBTRACT_EFFECT, v[1],
					YFYX2Info.getCompareMethod(), score.getTimeLine().substring(0, 4)});
			
			//添加所查影响的车型并添加所影响的车型的战略
			score.setVMSC(v[2], osMapper.getStrategic(hse, v[2]));
			//添加延迟影响
			addDelayedUpdateTask(osMapper, score, viList);
		}
	}
	
	/**
	 * 处理立即执行的影响
	 * @param score
	 * @param hse
	 */
	@Transactional(rollbackFor = Exception.class)
	public Map<String, Object> handleImmediateImpact(OSMapper osMapper, AnswerScoreDO score, HseRequest hse) throws Exception {
		//组装立即执行所需变量
		try {
			List<String[]> viList = handleImmediateVariable(osMapper, hse);
			score.setIIQ(false, hse.getQuestionId());
			//执行可以立即执行的影响并且不受战略的影响
			return ((AHseService) AopContext.currentProxy()).handleFinanceImpact(osMapper, score, hse, viList);
		}catch (Exception e){
			throw e;
		}
	}
	
	/**
	 * 获取YFYX的值,并添加YFFX变量到纪录表
	 * @param osMapper
	 * @param hse
	 * @param isAddBV 是否添加变量到平衡积分卡
	 * @param vModel 查询哪个车型
	 * @param aCharacter 计算变量用到的哪个变量
	 * @return
	 */
	private Map<String, Double> getYFYXAndAddYFFX(OSMapper osMapper, HseRequest hse, Boolean isAddBV, String vModel, String aCharacter) throws Exception{
		Map<String, Double> map = new HashMap<>();
		//获取本年最终收入（营业收入）
		Long operationVal = osMapper.getFinanceVal(new SearchFinanceDTO(hse, SearchFinanceConstant.TTPS_TARGET_TABLE,
				SearchFinanceConstant.OR_TARGET_COLUMN,
				Integer.valueOf(hse.getTimeLine().substring(0, 4)), null/*"and vehicle_model = '"+ vModel +"'"*/));
		
		//获取运算时用到的变量的纪录值(如果为研发费用投入6的时候WEY车型没有变量去相乘所以查询出的纪录为null)
		Map<String, Object> record = osMapper.getVariableRecord(hse.getClassId(), hse.getTeamId(), aCharacter);
		double YFVal = record == null ? 1 : Double
				.parseDouble(handleVariableVal(record.get("unit") == null ? "" : record.get("unit").toString(),
						Double.valueOf(record.get("variableVal").toString())));
		
		//目前仅有研发费用投入1需要添加
		if (isAddBV != null && isAddBV) {
			//添加平衡计分卡数据到表中
			osMapper.addBalancedVariable(
					new BalanceVariableDO(hse.getTeamId(), hse.getClassId(), VariableConstant.YFFX, String.valueOf(YFVal*0.8*100), NormalConstant.UNIT_BFH));
		}
		
		//获取营业收入为0的时候返回数据为null
		if (operationVal == null) {
			throw new GetValueException("获取"+ Integer.valueOf(hse.getTimeLine().substring(0, 4)) +"年的营业收入时为：" + operationVal);
		}
		
		//填报的需要变换单位为分 * 1000000    (最终计算结果保留两位小数)
		//做研发占比得分用
		map.put("yfyx", Double.valueOf(FormatNumberUtils.formatDouble(Double.valueOf(hse.getAnswer().get(0)) * 1000000 * YFVal / (operationVal == null ? 0 : operationVal) * 100, FormatNumberUtils.FORMAT_DOUBLE_DECIMAL_TWO)));
		map.put("yfyxZB", Double.valueOf(FormatNumberUtils.formatDouble(Double.valueOf(hse.getAnswer().get(0)) * 1000000 / (operationVal == null ? 0 : operationVal) * 100, FormatNumberUtils.FORMAT_DOUBLE_DECIMAL_TWO)));
		
		return map;
	}
	
	/**
	 * 处理及时处理的变量信息
	 * @param hse
	 * @return
	 */
	private List<String[]> handleImmediateVariable(OSMapper osMapper, HseRequest hse) throws Exception{
		List<String[]> viList = new ArrayList<>();
		//查询所有车型所对应的分摊比例
		List<Map<String, Object>> modelRatio = osMapper.findVehicleModelRatio(
				Integer.valueOf(hse.getTimeLine().substring(0, 4)) + 1, hse.getClassId(),
				SearchFinanceConstant.AEDE_TARGET_COLUMN);
		for (Map<String, Object> mr : modelRatio) {
			viList.add(new String[] { VariableConstant.TBYFFYFT + StringUtils.upperCase(mr.get("vehicleModel").toString()),
							NormalConstant.REPLACEMENT_VALUE, FormatNumberUtils.formatDouble(Double.valueOf(hse.getAnswer().get(0)) * 1000000 * Double.valueOf(mr.get("ratio").toString()), FormatNumberUtils.FORMAT_DOUBLE_DECIMAL_TWO) });
		}
		return viList;
	}

}

