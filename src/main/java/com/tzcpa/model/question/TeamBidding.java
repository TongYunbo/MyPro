package com.tzcpa.model.question;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;

/**
 * 投标结果记录
 */
@Data
@ToString
public class TeamBidding implements Serializable {

    private static final long serialVersionUID = 1L;
    /**
     * id
     */
    private Integer id;
    /**
     * 班级id
     */
    private Integer classId;
    /**
     * 团队id
     */
    private Integer teamId;
    /**
     * 投标车型
     */
    private String vehicleType;
    /**
     * 单价
     */
    private Integer unitPrice;
    /**
     * 质保年限
     */
    private Integer warranty;
    /**
     * 总价
     */
    private Long totalPrice;
    /**
     * 数据入库时间
     */
    private Date inputTime;
    /**
     * 质保期限得分
     */
    private Integer qualitypriodScore;
    /**
     * 质保能力得分
     */
    private Integer qualityScore;
    /**
     * 汽车制造商实力得分
     */
    private Integer assetsScore;
    /**
     * 投标车型销售业绩得分
     */
    private Integer salesScore;
    /**
     * 投标价格得分
     */
    private Integer priceScore;
    /**
     * 最终得分
     */
    private String finalScore;
    
    /**
     * 所选车型得分
     */
    private Integer vmScore;

}