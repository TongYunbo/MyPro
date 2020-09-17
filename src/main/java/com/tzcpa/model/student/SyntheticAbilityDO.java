package com.tzcpa.model.student;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * @ClassName SyntheticAbilityDTO
 * @Description
 * @Author hanxf
 * @Date 2019/6/6 10:30
 * @Version 1.0
 **/
@Data
@ToString
public class SyntheticAbilityDO implements Serializable {

	private static final long serialVersionUID = 1L;
	private String key;
	private Integer classId;
	private Integer teamId;
	private String teamName;
    private Float score;
}
