package com.tzcpa.model.question;

import com.tzcpa.model.student.UacDO;

import lombok.Data;
import lombok.ToString;

/**
 * <p>Description: 影响资产负债表的数据影响结构</p>
 * @author WTL
 * @date 2019年7月5日
 */
@Data
@ToString
public class TBalanceSheetImpactDO {
	
	/**
	 * 班级ID
	 */
	private Integer classId;
	
	/**
	 * 团队ID
	 */
	private Integer teamId;
	
	/**
	 * 修改年
	 */
	private Integer year;
	
	/**
	 * 固定资产原价
	 */
	private Long originalValue;

	/**
	 * 无形资产
	 */
	private Long intangibleAssets;

	/**
	 * 累计折旧
	 */
	private Long aDepreciation;

	/**
	 * 股本
	 */
	private Long capitalization;

	/**
	 * 短期借款
	 */
	private Long stBorrowing;

	/**
	 * 商誉
	 */
	private Long goodWill;

	/**
	 * 应付债券
	 */
	private Long bondsPayable;

	/**
	 * 资本公积
	 */
	private Long capitalReserve;
	
	public TBalanceSheetImpactDO(){}
	
	public TBalanceSheetImpactDO(UacDO uac) {
		this.classId = uac.getClassId();
		this.teamId = uac.getTeamId();
		this.year = uac.getBYear();
	}

}

