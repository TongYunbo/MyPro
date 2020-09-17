package com.tzcpa.utils;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.RandomUtils;

import com.tzcpa.constant.VNCConstant;
import com.tzcpa.model.question.ImpactJudgeMethodParam;

/**
 * <p>Description: 比较值返回结果</p>
 *
 * @author WTL
 * @date 2019年5月13日
 */
public class CompareResultUtils {

    /**
     * 判定是否大于百分之80
     *
     * @param d
     * @return
     */
    public Boolean gtPercent80(ImpactJudgeMethodParam ijp) {
        if (ijp.getCv() > 0.8) {
            return true;
        }
        return false;
    }

    /**
     * 判定是否小于百分之50
     *
     * @param d
     * @return
     */
    public Boolean ltPercent50(ImpactJudgeMethodParam ijp) {
        if (ijp.getCv() < 0.5) {
            return true;
        }
        return false;
    }

    /**
     * 判定是否小于百分之80
     *
     * @param d
     * @return
     */
    public Boolean ltPercent80(ImpactJudgeMethodParam ijp) {
        if (ijp.getCv() < 0.8) {
            return true;
        }
        return false;
    }

    /**
     * 小于下限
     *
     * @param osMapper
     * @param year
     * @param d
     * @return
     */
    public Boolean ltLowerLimit(ImpactJudgeMethodParam ijp) {
        if (ijp.getCv() < ijp.getOsMapper().getVariableSection(ijp.getYear()).get("lowerLimit").doubleValue()) {
            return true;
        }
        return false;
    }

    /**
     * 大于等于上限
     *
     * @param osMapper
     * @param year
     * @param d
     * @return
     */
    public Boolean gtOrEtUpperLimit(ImpactJudgeMethodParam ijp) {
        if (ijp.getCv() >= ijp.getOsMapper().getVariableSection(ijp.getYear()).get("upperLimit").doubleValue()) {
            return true;
        }
        return false;
    }

    /**
     * 大于等于下限小于上限
     *
     * @param osMapper
     * @param year
     * @param d
     * @return
     */
    public Boolean gtOrEtLLAndLtUL(ImpactJudgeMethodParam ijp) {
        Map<String, BigDecimal> section = ijp.getOsMapper().getVariableSection(ijp.getYear());
        if (ijp.getCv() >= section.get("lowerLimit").doubleValue() && ijp.getCv() < section.get("upperLimit").doubleValue()) {
            return true;
        }
        return false;
    }

    /**
     * 判定是否小于百分之60
     *
     * @param d
     * @return
     */
    public Boolean ltPercent60(ImpactJudgeMethodParam ijp) {
        if (ijp.getCv() < 0.6) {
            return true;
        }
        return false;
    }

    /**
     * 存货周转率
     * @param osMapper
     * @param year
     * @param d
     * @return
     */
    public Boolean gtStockTurnover248(ImpactJudgeMethodParam ijp) {
        Map<String, Object> param = new HashMap<>();
        param.put("vCode", VNCConstant.CHZZLS);
        String stockTurnoverRate = ijp.getOsMapper().selectTeamRdv(param);
        if (ijp.getCv() > Double.parseDouble(stockTurnoverRate)) {
            return true;
        }
        return false;
    }

    /**
     * 存货周转率
     * @param osMapper
     * @param year
     * @param d
     * @return
     */
    public Boolean ltStockTurnover248(ImpactJudgeMethodParam ijp) {
        Map<String, Object> param = new HashMap<>();
        param.put("vCode", VNCConstant.CHZZLS);
        String stockTurnoverRate = ijp.getOsMapper().selectTeamRdv(param);
        if (ijp.getCv() <= Double.parseDouble(stockTurnoverRate)) {
            return true;
        }
        return false;
    }
    
    /**
     * 判断百分之八十的几率
     * @param osMapper
     * @param year
     * @param d
     * @return
     */
    public static Boolean judge80Change(ImpactJudgeMethodParam ijp){
    	int randomInt =  RandomUtils.nextInt(1,11);
    	if(randomInt <= 2){ //10里面2个数，小于等于2的概率就是20%
    		return false;
    	}
    	return true;
    }

    /**
     * 判断小于1
     * @param osMapper
     * @param year
     * @param d
     * @return
     */
    public Boolean ltOne(ImpactJudgeMethodParam ijp) {
        if (ijp.getCv() < 1) {
            return true;
        }
        return false;
    }

    /**
     * 判断大于1
     * @param osMapper
     * @param year
     * @param d
     * @return
     */
    public Boolean gtOne(ImpactJudgeMethodParam ijp) {
        if (ijp.getCv() > 1) {
            return true;
        }
        return false;
    }
    
    /**
     * 判断是否选择了外购
     * @param ijp
     * @return
     */
    public Boolean judgeIsWG(ImpactJudgeMethodParam ijp){
    	if (ijp.getOsMapper().getWGCount(ijp.getHse().getTeamId(), ijp.getHse().getClassId()) <= 0) {
			return true;
		}
    	return false;
    }

    /**
     *  校验数据是否为非0
     * @param data
     * @return
     */
    public static boolean whetherInvalid(String data) {

        if (data == null) {
            return false;
        } else {
            try {
                BigDecimal dec = new BigDecimal(data);
                if (null == dec) {
                    return false;
                } else if (dec.compareTo(new BigDecimal("0")) == 0) {
                    return false;
                }
            }catch (NumberFormatException e){
                return false;
            }

        }
        return true;
    }

}

